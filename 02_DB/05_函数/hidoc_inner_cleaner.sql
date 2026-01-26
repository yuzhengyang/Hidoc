CREATE OR REPLACE FUNCTION "public"."hidoc_inner_cleaner"()
  RETURNS "pg_catalog"."int8" AS $BODY$
	DECLARE
	v_result  int8;
	v_temp    int8;
	BEGIN

    RAISE NOTICE '开始清理数据';
		
		
    SELECT COUNT(*) INTO v_temp FROM data_coll WHERE create_time < CURRENT_DATE - INTERVAL '10 days';
    DELETE FROM data_coll WHERE create_time < CURRENT_DATE - INTERVAL '7 days';
	  v_result := v_result + v_temp;
		
		
    SELECT COUNT(*) INTO v_temp FROM sys_access_log WHERE begin_time < CURRENT_DATE - INTERVAL '100 days';
    DELETE FROM sys_access_log WHERE begin_time < CURRENT_DATE - INTERVAL '30 days';
	  v_result := v_result + v_temp;
		
		
    SELECT COUNT(*) INTO v_temp FROM sys_access_log WHERE uri = '/datacoll/create';
    DELETE FROM sys_access_log WHERE uri = '/datacoll/create';
	  v_result := v_result + v_temp;
		
		
		
		
    RAISE NOTICE '清理数据完成';
		
	  RETURN v_result;
		
END$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100