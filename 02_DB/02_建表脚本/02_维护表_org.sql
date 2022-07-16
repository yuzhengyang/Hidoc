CREATE TABLE IF NOT EXISTS "org" (
  "id" varchar(20)  NOT NULL,
  "name" varchar(255) ,
  "description" text ,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "frozen_time" timestamp(6),
  "is_frozen" bool,
  "join_email_prefix" varchar(255) ,
  "join_email_suffix" varchar(255) ,
  "join_password" varchar(255) ,
  "join_free" bool,
  "join_quota_limit" int4,
  "join_quota_used" int4,
  "create_user_id" varchar(20) ,
  "update_user_id" varchar(20) ,
	CONSTRAINT org_pkey PRIMARY KEY (id)
)
;
COMMENT ON COLUMN "org"."name" IS '名称';
COMMENT ON COLUMN "org"."description" IS '描述';
COMMENT ON COLUMN "org"."create_time" IS '创建时间';
COMMENT ON COLUMN "org"."update_time" IS '更新时间';
COMMENT ON COLUMN "org"."frozen_time" IS '冻结时间';
COMMENT ON COLUMN "org"."is_frozen" IS '是否冻结';
COMMENT ON COLUMN "org"."join_email_prefix" IS '自动加入邮箱前缀';
COMMENT ON COLUMN "org"."join_email_suffix" IS '自动加入邮箱后缀';
COMMENT ON COLUMN "org"."join_password" IS '加入密码';
COMMENT ON COLUMN "org"."join_free" IS '自由加入';
COMMENT ON COLUMN "org"."join_quota_limit" IS '名额限制';
COMMENT ON COLUMN "org"."join_quota_used" IS '已用名额';
COMMENT ON COLUMN "org"."create_user_id" IS '创建用户';
COMMENT ON COLUMN "org"."update_user_id" IS '更新用户';



SELECT * FROM sysdb_add_column('org', 'update_user_id', 'varchar(20)');