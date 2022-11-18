
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
  "join_rule_type" varchar(255) ,
  "join_rule_desc" varchar(2048) ,
  CONSTRAINT team_pkey PRIMARY KEY (id)
)
;