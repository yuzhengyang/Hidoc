
CREATE TABLE IF NOT EXISTS "card_level" (
  "id" varchar(20)  NOT NULL,
  "card_id" varchar(20)  NOT NULL,
  "name" varchar(255) ,
  "description" text ,
  "data_package" text ,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "create_user_id" varchar(20) ,
  "update_user_id" varchar(20) ,
  "member_count" int4,
  "is_enable" bool DEFAULT false,
  "is_allow_apply" bool DEFAULT false,
  "allow_email" text ,
  "allow_email_suffix" varchar(255) ,
  "lock_user_id" varchar(20) ,
  "lock_time" timestamp(6),
  "lock_duration" int4,
  "lock_version" int8 NOT NULL DEFAULT 0,
  "lock_key" varchar(255) NOT NULL DEFAULT '0',
  CONSTRAINT card_level_pkey PRIMARY KEY (id)
)
;

SELECT * FROM sysdb_add_unique_index('card_level', 'uidx_card_level_card_id_name', 'card_id, name');


