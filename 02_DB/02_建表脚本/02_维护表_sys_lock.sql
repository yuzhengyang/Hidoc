
CREATE TABLE IF NOT EXISTS "public"."sys_lock" (
  "key" varchar(128) NOT NULL,
  "val" varchar(128) NOT NULL,
  "user_id" varchar(20),
  "create_time" timestamp(0) NOT NULL,
  "expire_time" timestamp(0) NOT NULL,
  "lock_count" int8 NOT NULL DEFAULT 0,
  CONSTRAINT "sys_lock_pkey" PRIMARY KEY ("key", "val")
)
;


COMMENT ON COLUMN "public"."sys_lock"."key" IS '锁的KEY';

COMMENT ON COLUMN "public"."sys_lock"."val" IS '锁的VAL';

COMMENT ON COLUMN "public"."sys_lock"."user_id" IS '锁定用户ID';

COMMENT ON COLUMN "public"."sys_lock"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."sys_lock"."expire_time" IS '过期时间';