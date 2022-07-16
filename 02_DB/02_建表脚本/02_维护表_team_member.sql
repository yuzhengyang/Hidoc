
-- ----------------------------
-- Table structure for team_member
-- ----------------------------
CREATE TABLE IF NOT EXISTS "team_member" (
  "id" varchar(20)  NOT NULL,
  "team_id" varchar(20) ,
  "user_id" varchar(20) ,
  "doc_collected_permission" jsonb,
  "doc_permission" jsonb,
  "notice_permission" jsonb,
	CONSTRAINT team_member_pkey PRIMARY KEY (id)
)
;
