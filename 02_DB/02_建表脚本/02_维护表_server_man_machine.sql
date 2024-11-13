CREATE TABLE IF NOT EXISTS "server_man_machine" (
  "id" varchar(20)  NOT NULL,
  "name" varchar(255) ,
  "description" varchar(2048) ,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "create_user_id" varchar(20) ,
  "update_user_id" varchar(20) ,
  "owner_user_id" varchar(20) ,
  "type" varchar(255) ,
  "address" varchar(255) ,
  "port" int2,
  "username" varchar(255) ,
  "password" varchar(255) ,
  "is_delete" bool,
  CONSTRAINT server_man_machine_pkey PRIMARY KEY (id)
)
;
COMMENT ON COLUMN "public"."server_man_machine"."name" IS '服务名称';
COMMENT ON COLUMN "public"."server_man_machine"."description" IS '服务描述';
COMMENT ON COLUMN "public"."server_man_machine"."type" IS '服务类型';
COMMENT ON COLUMN "public"."server_man_machine"."address" IS 'ip地址';
COMMENT ON COLUMN "public"."server_man_machine"."port" IS '端口号';
COMMENT ON COLUMN "public"."server_man_machine"."username" IS '账号';
COMMENT ON COLUMN "public"."server_man_machine"."password" IS '密码';
COMMENT ON TABLE "public"."server_man_machine" IS '服务管理服务器信息';


-- 增加字段

SELECT * FROM sysdb_add_column('server_man_machine', 'teams_execute', 'text NULL');