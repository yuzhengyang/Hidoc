-- public.sys_access_log definition

-- Drop table

-- DROP TABLE public.sys_access_log;

CREATE TABLE IF NOT EXISTS public.sys_access_log (
	id varchar(20) NOT NULL,
	ip varchar(255) NOT NULL,
	begin_time timestamp NULL,
	end_time timestamp NULL,
	uri varchar(1024) NOT NULL,
	"method" varchar(255) NOT NULL,
	elapsed_time int8 NULL,
	user_id varchar(20) NULL,
	step varchar(255) NULL,
	begin_max_memory int8 NULL,
	begin_total_memory int8 NULL,
	begin_free_memory int8 NULL,
	end_max_memory int8 NULL,
	end_total_memory int8 NULL,
	end_free_memory int8 NULL,
	probably_use_memory int8 NULL,
	thread_name bpchar(255) NULL,
	"exception" text NULL,
	CONSTRAINT sys_access_log_pkey PRIMARY KEY (id, ip, uri, method)
);



-- 增加字段

SELECT * FROM sysdb_add_column('sys_access_log', 'machine_id', 'varchar(255) NULL');

SELECT * FROM sysdb_add_column('sys_access_log', 'point_id', 'varchar(255) NULL');

SELECT * FROM sysdb_add_column('sys_access_log', 'access_origin', 'varchar(255) NULL');
