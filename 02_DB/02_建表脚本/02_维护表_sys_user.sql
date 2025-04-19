-- public.sys_user definition

-- Drop table

-- DROP TABLE public.sys_user;

CREATE TABLE IF NOT EXISTS public.sys_user (
	id varchar(20) NOT NULL,
	avatar varchar(255) NULL,
	"name" varchar(255) NOT NULL,
	real_name varchar(255) NULL,
	email varchar(255) NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	"password" varchar(255) NULL,
	"totp_seed" varchar(255) NULL,
	"totp_create_time" timestamp NULL,
	is_sleep bool DEFAULT false,
	is_frozen bool DEFAULT false,
	online_time timestamp NULL,
	login_time timestamp NULL,
	vip_level int4,
	CONSTRAINT sys_user_pkey PRIMARY KEY (id)
);

SELECT * FROM sysdb_add_column('sys_user', 'online_time', 'timestamp NULL');
SELECT * FROM sysdb_add_column('sys_user', 'login_time', 'timestamp NULL');
SELECT * FROM sysdb_add_column('sys_user', 'vip_level', 'int4');
SELECT * FROM sysdb_add_column('sys_user', 'roles', 'jsonb NULL');
SELECT * FROM sysdb_add_column('sys_user', 'totp_seed', 'varchar(255) NULL');
SELECT * FROM sysdb_add_column('sys_user', 'totp_create_time', 'timestamp NULL');
SELECT * FROM sysdb_add_column('sys_user', 'is_sleep', 'bool DEFAULT false');


SELECT * FROM sysdb_add_unique_index('sys_user', 'uidx_sys_user_name', '"name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST');
SELECT * FROM sysdb_add_unique_index('sys_user', 'uidx_sys_user_email', '"email" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST');


-- �޸�����
UPDATE sys_user SET is_sleep = false WHERE is_sleep IS NULL;
