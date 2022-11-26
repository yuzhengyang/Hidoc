
-- ----------------------------
-- Table structure for team_member
-- ----------------------------

CREATE TABLE IF NOT EXISTS "team_member" (
  "id" varchar(20)  NOT NULL,
  "team_id" varchar(20) ,
  "user_id" varchar(20) ,
  "create_time" timestamp(6),
  "create_user_id" varchar(20) ,
  "update_time" timestamp(6),
  "update_user_id" varchar(20) ,
  "permission_rule" jsonb,
  CONSTRAINT team_member_pkey PRIMARY KEY (id)
)
;
COMMENT ON COLUMN "public"."team_member"."id" IS '主键ID';
COMMENT ON COLUMN "public"."team_member"."team_id" IS '团队ID';
COMMENT ON COLUMN "public"."team_member"."user_id" IS '用户ID';
COMMENT ON COLUMN "public"."team_member"."create_time" IS '创建时间';
COMMENT ON COLUMN "public"."team_member"."create_user_id" IS '创建人ID';
COMMENT ON COLUMN "public"."team_member"."update_time" IS '更新时间';
COMMENT ON COLUMN "public"."team_member"."update_user_id" IS '更新人ID';
COMMENT ON COLUMN "public"."team_member"."permission_rule" IS '权限规则';
