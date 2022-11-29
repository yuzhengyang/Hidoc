
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



SELECT * FROM sysdb_add_column('team_member', 'permission_rule', 'jsonb NULL');