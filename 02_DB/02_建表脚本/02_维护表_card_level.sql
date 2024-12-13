
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
  "allow_apply_email" varchar(255) ,
  "lock_user_id" varchar(20) ,
  "lock_time" timestamp(6),
  "lock_duration" int4,
  "lock_version" varchar(255) ,
  CONSTRAINT card_level_pkey PRIMARY KEY (id)
)
;

SELECT * FROM sysdb_add_unique_index('card_level', 'uidx_card_level_card_id_name', 'card_id, name');


SELECT * FROM sysdb_add_column('card_level', 'allow_apply_email', 'varchar(255)');
SELECT * FROM sysdb_add_column('card_level', 'lock_version', 'varchar(255)');


