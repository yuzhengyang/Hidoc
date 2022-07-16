
-- ----------------------------
-- Table structure for team_member_log
-- ----------------------------
CREATE TABLE IF NOT EXISTS "team_member_log" (
  "id" varchar(20)  NOT NULL,
  "team_id" varchar(20) ,
  "user_id" varchar(20) ,
  "create_user_id" varchar(20) ,
  "create_time" timestamp(6),
	CONSTRAINT team_member_log_pkey PRIMARY KEY (id)
)
;
