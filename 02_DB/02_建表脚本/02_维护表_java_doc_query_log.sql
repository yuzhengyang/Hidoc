CREATE TABLE IF NOT EXISTS "java_doc_query_log" (
  "id" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "trace_id" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "user_id" varchar(20) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "query_type" varchar(1024) COLLATE "pg_catalog"."default",
  "query_value" varchar(1024) COLLATE "pg_catalog"."default",
  CONSTRAINT java_doc_query_log_pkey PRIMARY KEY (id)
)
;