
CREATE TABLE IF NOT EXISTS "card_user" (
  "id" varchar(20)  NOT NULL,
  "card_id" varchar(20)  NOT NULL,
  "level_ids" text  NOT NULL,
  "user_email" varchar(255) ,
  "user_password" varchar(255) ,
  "signatures" text ,
  "salt" varchar(255) ,
  "description" text ,
  "create_mode" varchar(20) ,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "create_user_id" varchar(20) ,
  "update_user_id" varchar(20) ,
  "is_enable" bool DEFAULT false,
  CONSTRAINT card_user_pkey PRIMARY KEY (id)
)
;

SELECT * FROM sysdb_add_unique_index('card_user', 'uidx_card_user_cu', 'card_id, user_email');