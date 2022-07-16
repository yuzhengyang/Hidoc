CREATE TABLE IF NOT EXISTS "server_man_exe_log" (
  "id" varchar(20)  NOT NULL,
  "cmd_id" varchar(20) ,
  "content_ta" text ,
  "content_tb" text ,
  "content_tc" text ,
  "begin_time" timestamp(6),
  "end_time" timestamp(6),
  "result_ta" text ,
  "result_tb" text ,
  "result_tc" text ,
  "machine_id" varchar(20) ,
  "run_user_id" varchar(20) ,
  "dialog_id" varchar(255) ,
  CONSTRAINT server_man_exe_log_pkey PRIMARY KEY (id)
)
;
COMMENT ON TABLE "public"."server_man_exe_log" IS '服务管理执行日志';

