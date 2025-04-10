

CREATE TABLE IF NOT EXISTS "public"."server_man_output" (
  "id" varchar(20) NOT NULL,
  "dialog_id" varchar(20) NOT NULL,
  "serial_number" int8,
  "create_time" timestamp(6) NOT NULL,
  "output" text,
  CONSTRAINT "server_man_output_pkey" PRIMARY KEY ("id")
)
;

CREATE INDEX "idx_server_man_output_ds" ON "public"."server_man_output" USING btree (
  "dialog_id" "pg_catalog"."text_ops" ASC NULLS LAST,
  "serial_number" "pg_catalog"."int8_ops" ASC NULLS LAST
);