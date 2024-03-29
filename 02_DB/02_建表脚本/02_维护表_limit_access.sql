CREATE TABLE IF NOT EXISTS "limit_access" (
  "id" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "token" varchar(255) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "description" varchar(2048) COLLATE "pg_catalog"."default",
  "is_enable" bool,
  "allow_hours" text COLLATE "pg_catalog"."default",
  "forbid_hours" text COLLATE "pg_catalog"."default",
  "allow_addresses" text COLLATE "pg_catalog"."default",
  "forbid_addresses" text COLLATE "pg_catalog"."default",
  "allow_positions" jsonb,
  "forbid_positions" jsonb,
  "allow_accounts" text COLLATE "pg_catalog"."default",
  "forbid_accounts" text COLLATE "pg_catalog"."default",
  "allow_email_suffix" text COLLATE "pg_catalog"."default",
  "forbid_email_suffix" text COLLATE "pg_catalog"."default",
  "allow_versions" text COLLATE "pg_catalog"."default",
  "forbid_versions" text COLLATE "pg_catalog"."default",
  "allow_macs" text COLLATE "pg_catalog"."default",
  "forbid_macs" text COLLATE "pg_catalog"."default",
  "create_user_id" varchar(20) COLLATE "pg_catalog"."default",
  "owner_user_id" varchar(20) COLLATE "pg_catalog"."default",
  "is_delete" bool,
  "create_time" timestamp(0),
  "update_time" timestamp(0),
  "delete_time" timestamp(0),
  CONSTRAINT limit_access_pkey PRIMARY KEY (id)
)
;



-- 增加字段
SELECT * FROM sysdb_add_column('limit_access', 'allow_machines', 'text');
SELECT * FROM sysdb_add_column('limit_access', 'forbid_machines', 'text');
