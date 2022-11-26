
-- ----------------------------
-- Table structure for team
-- ----------------------------

CREATE TABLE IF NOT EXISTS "team" (
  "id" varchar(20)  NOT NULL,
  "name" varchar(255) ,
  "description" text ,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "delete_time" timestamp(6),
  "create_user_id" varchar(20) ,
  "update_user_id" varchar(20) ,
  "delete_user_id" varchar(20) ,
  "owner_user_id" varchar(20) ,
  "member_count" int4,
  "join_rule" jsonb,
  "is_delete" bool DEFAULT false,
  CONSTRAINT team_pkey PRIMARY KEY (id)
)
;
COMMENT ON COLUMN "public"."team"."id" IS '主键ID';
COMMENT ON COLUMN "public"."team"."name" IS '团队名称';
COMMENT ON COLUMN "public"."team"."description" IS '团队描述';
COMMENT ON COLUMN "public"."team"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."team"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."team"."delete_time" IS '删除时间';
COMMENT ON COLUMN "public"."team"."create_user_id" IS '创建用户ID';
COMMENT ON COLUMN "public"."team"."update_user_id" IS '更新用户ID';
COMMENT ON COLUMN "public"."team"."delete_user_id" IS '删除用户ID';
COMMENT ON COLUMN "public"."team"."owner_user_id" IS '所属用户ID';
COMMENT ON COLUMN "public"."team"."member_count" IS '成员数量';
COMMENT ON COLUMN "public"."team"."join_rule" IS '自动加入规则';
COMMENT ON COLUMN "public"."team"."is_delete" IS '是否删除';



SELECT * FROM sysdb_add_unique_index('team', 'uidx_team_name', '"name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST');