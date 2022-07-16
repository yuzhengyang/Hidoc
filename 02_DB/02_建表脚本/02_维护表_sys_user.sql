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
	is_frozen bool NULL,
	online_time timestamp NULL,
	vip_level int4,
	CONSTRAINT sys_user_pkey PRIMARY KEY (id)
);

SELECT * FROM sysdb_add_column('sys_user', 'online_time', 'timestamp NULL');
SELECT * FROM sysdb_add_column('sys_user', 'vip_level', 'int4');

SELECT * FROM sysdb_add_unique_index('sys_user', 'name', '"name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST');

