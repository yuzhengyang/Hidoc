
-- ----------------------------
-- Table structure for team
-- ----------------------------
CREATE TABLE IF NOT EXISTS "team" (
  "id" varchar(20)  NOT NULL,
  "name" varchar(255) ,
  "description" text ,
  "create_user_id" varchar ,
  "owner_user_id" varchar ,
  "create_time" timestamp(6),
	CONSTRAINT team_pkey PRIMARY KEY (id)
)
;
