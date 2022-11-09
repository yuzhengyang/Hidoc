CREATE TABLE IF NOT EXISTS "limit_access_log" (
  "id" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "limit_access_id" varchar(20) COLLATE "pg_catalog"."default",
  "address" varchar(1024) COLLATE "pg_catalog"."default",
  "version" varchar(1024) COLLATE "pg_catalog"."default",
  "account" varchar(1024) COLLATE "pg_catalog"."default",
  "machine" varchar(1024) COLLATE "pg_catalog"."default",
  "hour" varchar(1024) COLLATE "pg_catalog"."default",
  "position" varchar(1024) COLLATE "pg_catalog"."default",
  "email" varchar(1024) COLLATE "pg_catalog"."default",
  "mac" varchar(1024) COLLATE "pg_catalog"."default",
  "is_access" bool,
  "message" varchar(1024) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  CONSTRAINT limit_access_log_pkey PRIMARY KEY (id)
)
;