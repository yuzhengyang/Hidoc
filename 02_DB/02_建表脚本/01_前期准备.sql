/* ******************************************
 * 版本          日期           修改人              备注 
 * 7.0.2.1       20220101       yuzhengyang         创建数据库前的准备工作
 * ******************************************/


-- ----------------------------
-- 判断数据库类型函数
-- ----------------------------
CREATE OR REPLACE FUNCTION "sysdb_get_db_type"()
  RETURNS "pg_catalog"."int2" AS $BODY$
	DECLARE
	v_db_type  int2;
	BEGIN
	-- 判断数据库类型
	-- 是否为ADBPG，通过是否安装greenplum工具判断
	-- 0：普通pg数据库
	-- 1：阿里adbpg 或 基于greenplum的数据库
	
	SELECT COUNT(1) INTO v_db_type FROM pg_namespace WHERE nspname = 'gp_toolkit';
	
	RAISE NOTICE '数据库类型： [%] （0-pg，1-adbpg/gp）',v_db_type;
	
	RETURN v_db_type;
END$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
  
  
-- ----------------------------
-- 建表
-- ----------------------------	
CREATE OR REPLACE FUNCTION "sysdb_create_table"(IN in_base "varchar", IN in_dist "varchar", IN in_part "varchar", IN in_part_def "varchar")
	RETURNS "pg_catalog"."int2" AS $BODY$
	DECLARE
	  v_db_type  int2;
	  v_sql      varchar;
	BEGIN

	-- 根据数据库类型组合语句
	-- 0-PG库   ：字段定义+分区定义
	-- 1-ADBPG库：字段定义+分布定义+分区定义+分区默认
	SELECT sysdb_get_db_type INTO v_db_type FROM sysdb_get_db_type();
	if v_db_type = 0 THEN
		v_sql := in_base || ' ' || in_part;
	ELSE
		v_sql := in_base || ' ' || in_dist || ' ' || in_part || ' ' || in_part_def;
	END IF;
	
	RAISE NOTICE '建表SQL语句为：%',v_sql;
	EXECUTE v_sql;
	
	RETURN 1;
END$BODY$
	LANGUAGE plpgsql;
  
-- ----------------------------
-- 添加表字段函数
-- ----------------------------
CREATE OR REPLACE FUNCTION "sysdb_add_column"(IN in_table_name "varchar", IN in_column_name "varchar", IN in_description "varchar")
	RETURNS void AS $BODY$
	DECLARE
	
	  v_exist_column  int2;
	BEGIN

  SELECT COUNT(1) INTO v_exist_column FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = current_schema() AND TABLE_NAME = in_table_name AND COLUMN_NAME = in_column_name;
  IF v_exist_column = 0 THEN
    EXECUTE 'ALTER TABLE "' || in_table_name || '" ADD COLUMN "' || in_column_name || '" ' || in_description || ' ; ';
  END IF;
	
	RETURN;
END$BODY$
	LANGUAGE plpgsql;

-- ----------------------------
-- 修改表字段名称函数
-- ----------------------------
CREATE OR REPLACE FUNCTION "sysdb_rename_column"(IN in_table_name "varchar", IN in_column_name_a "varchar", IN in_column_name_b "varchar")
	RETURNS void AS $BODY$
	DECLARE	
	  v_exist_column  int2;
	BEGIN

  SELECT COUNT(1) INTO v_exist_column FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = current_schema() AND TABLE_NAME = in_table_name AND COLUMN_NAME = in_column_name_a;
  IF v_exist_column = 1 THEN
    EXECUTE 'ALTER TABLE "' || in_table_name || '" RENAME "' || in_column_name_a || '" TO "' || in_column_name_b || '" ; ';
  END IF;
	
	RETURN;
END$BODY$
	LANGUAGE plpgsql;
	
-- ----------------------------
-- 修改表字段
-- ----------------------------
CREATE OR REPLACE FUNCTION "sysdb_alter_column"(IN in_table_name "varchar", IN in_column_name "varchar", IN in_description "varchar")
	RETURNS void AS $BODY$
	DECLARE	
	  v_exist_column  int2;
	  v_sql      varchar;
	BEGIN

  SELECT COUNT(1) INTO v_exist_column FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = current_schema() AND TABLE_NAME = in_table_name AND COLUMN_NAME = in_column_name;
  IF v_exist_column = 1 THEN
      v_sql := 'ALTER TABLE ' || in_table_name || ' ALTER COLUMN ' || in_column_name || ' ' || in_description;
	  RAISE NOTICE 'SQL语句为：%',v_sql;
	  EXECUTE v_sql;
  END IF;
	
	RETURN;
END$BODY$
	LANGUAGE plpgsql;

-- ----------------------------
-- 创建索引
-- ----------------------------
CREATE OR REPLACE FUNCTION "sysdb_add_index"(IN in_table_name "varchar", IN in_index_name "varchar", IN in_index_description "varchar")
	RETURNS void AS $BODY$
	DECLARE	
	  v_exist_index  int2;
	BEGIN

  SELECT COUNT(1) INTO v_exist_index FROM pg_indexes WHERE tablename = in_table_name AND indexname = in_index_name;
  IF v_exist_index = 0 THEN
    EXECUTE 'CREATE INDEX ' || in_index_name || ' ON "' || in_table_name || '" USING btree (' || in_index_description || ');';
  END IF;
	
	RETURN;
END$BODY$
	LANGUAGE plpgsql;


-- ----------------------------
-- 创建唯一索引
-- ----------------------------
CREATE OR REPLACE FUNCTION "sysdb_add_unique_index"(IN in_table_name "varchar", IN in_index_name "varchar", IN in_index_description "varchar")
	RETURNS void AS $BODY$
	DECLARE	
	  v_exist_index  int2;
	BEGIN

  SELECT COUNT(1) INTO v_exist_index FROM pg_indexes WHERE tablename = in_table_name AND indexname = in_index_name;
  IF v_exist_index = 0 THEN
    EXECUTE 'CREATE UNIQUE INDEX ' || in_index_name || ' ON "' || in_table_name || '" USING btree (' || in_index_description || ');';
  END IF;
	
	RETURN;
END$BODY$
	LANGUAGE plpgsql;
	

-- ----------------------------
-- 复制表
-- ----------------------------
CREATE OR REPLACE FUNCTION "sysdb_copy_table"(IN in_table_name "varchar", IN in_from_table_name "varchar", IN in_recreate_param "varchar")
	RETURNS void AS $BODY$
	DECLARE	
		v_exist_table	int2;
		v_exist_row		int8;
	BEGIN
	
	SELECT COUNT(1) INTO v_exist_table FROM pg_class WHERE relname = in_table_name;
	RAISE NOTICE '检查表是否存在: [%] （0-不存在，1-存在）',v_exist_table;
	
    IF in_recreate_param = 'FORCE-DROP-TABLE' THEN
	
		RAISE NOTICE '强制删除重建，不会保留已有目标表的数据，可能导致数据丢失: [%]',in_recreate_param;
		EXECUTE 'DROP TABLE IF EXISTS ' || in_table_name;
		RAISE NOTICE '！目标表删除完成';
		
	ELSIF in_recreate_param = 'DROP-NULL' THEN
	
		RAISE NOTICE '如果目标表为空，则删除重建: [%]',in_recreate_param;
		IF v_exist_table <> 0 THEN
			EXECUTE 'SELECT COUNT(1) FROM ' || in_table_name || '' INTO v_exist_row;
			RAISE NOTICE '检查表是否有数据: [%] 行',v_exist_row;
			IF v_exist_row = 0 THEN
				EXECUTE 'DROP TABLE IF EXISTS ' || in_table_name;
				RAISE NOTICE '！目标表删除完成';
			END IF;
		END IF;
	
	ELSE
	
	END IF;
    
	-- 复制表
	EXECUTE 'CREATE TABLE IF NOT EXISTS ' || in_table_name || ' (LIKE ' || in_from_table_name || ' INCLUDING ALL);';
	RAISE NOTICE '目标表复制完成';
	
	RETURN;
END$BODY$
	LANGUAGE plpgsql;


-- ----------------------------
-- 修改表字段类型函数（暂不支持，后续扩展）
-- ----------------------------