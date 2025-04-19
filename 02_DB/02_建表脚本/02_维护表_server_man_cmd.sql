CREATE TABLE IF NOT EXISTS "server_man_cmd" (
  "id" varchar(20)  NOT NULL,
  "name" varchar(255) ,
  "description" varchar(2048) ,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "execute_time" timestamp(6),
  "create_user_id" varchar(20) ,
  "update_user_id" varchar(20) ,
  "owner_user_id" varchar(20) ,
  "type" varchar(255) ,
  "is_delete" bool,
  "machine_id" varchar(20) ,
  "content_ta" text ,
  "content_tb" text ,
  "content_tc" text ,
  "interval" int8,
  CONSTRAINT server_man_cmd_pkey PRIMARY KEY (id)
)
;
COMMENT ON COLUMN "public"."server_man_cmd"."name" IS '服务名称';
COMMENT ON COLUMN "public"."server_man_cmd"."description" IS '服务描述';
COMMENT ON COLUMN "public"."server_man_cmd"."type" IS '命令类型';
COMMENT ON TABLE "public"."server_man_cmd" IS '服务管理命令信息';


-- 增加字段

SELECT * FROM sysdb_add_column('server_man_cmd', 'is_share', 'bool');
SELECT * FROM sysdb_add_column('server_man_cmd', 'interval', 'int8');
SELECT * FROM sysdb_add_column('server_man_cmd', 'execute_time', 'timestamp(6)');