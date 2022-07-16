CREATE TABLE IF NOT EXISTS "app_conf" (
  "item" varchar(255)  NOT NULL,
  "key" varchar(255)  NOT NULL,
  "value" varchar(255) ,
  "ps" varchar(255) ,
  "index" int8,
	CONSTRAINT app_conf_pkey PRIMARY KEY ("item", "key")
)
;
COMMENT ON COLUMN "app_conf"."item" IS '项';
COMMENT ON COLUMN "app_conf"."key" IS '键';
COMMENT ON COLUMN "app_conf"."value" IS '值';
COMMENT ON COLUMN "app_conf"."ps" IS '说明';
COMMENT ON COLUMN "app_conf"."index" IS '序号';
