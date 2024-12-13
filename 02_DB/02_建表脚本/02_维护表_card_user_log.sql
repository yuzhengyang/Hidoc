
CREATE TABLE IF NOT EXISTS "card_user_log" (
  "id" varchar(20)  NOT NULL,
  "card_id" varchar(20)  NOT NULL,
  "level_id" varchar(20)  NOT NULL,
  "user_id" varchar(20)  NOT NULL,
  "signatur" text ,
  "create_time" timestamp(6),
  "create_user_id" varchar(20) ,
  CONSTRAINT card_user_log_pkey PRIMARY KEY (id)
)
;
