
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

SELECT * FROM sysdb_add_unique_index('team', 'uidx_team_name', '"name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST');