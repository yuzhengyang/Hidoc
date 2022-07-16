
-- ----------------------------
-- Table structure for notice
-- ----------------------------
CREATE TABLE IF NOT EXISTS "notice" (
  "id" varchar(20)  NOT NULL,
  "text" text ,
  "create_user_id" varchar(20) ,
  "create_time" timestamp(6),
	CONSTRAINT notice_pkey PRIMARY KEY (id)
)
;
