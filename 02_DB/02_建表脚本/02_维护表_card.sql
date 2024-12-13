
-- ----------------------------
-- Table structure for card
-- ----------------------------

CREATE TABLE IF NOT EXISTS "card" (
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
  "is_delete" bool DEFAULT false,
  CONSTRAINT card_pkey PRIMARY KEY (id)
)
;

