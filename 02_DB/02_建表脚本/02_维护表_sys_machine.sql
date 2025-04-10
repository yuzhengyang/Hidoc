-- public.sys_machine definition

-- Drop table

-- DROP TABLE public.sys_machine;

CREATE TABLE IF NOT EXISTS public.sys_machine (
	machine_id varchar(255) NOT NULL,
	ip varchar(255) NULL,
	mac varchar(255) NULL,
	data_center_id int4 NULL,
	worker_id int4 NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	secret_key varchar(255) NULL,
	api_url varchar(255) NULL,
	CONSTRAINT sys_machine_pkey PRIMARY KEY (machine_id)
);



-- Ôö¼Ó×Ö¶Î

SELECT * FROM sysdb_add_column('sys_machine', 'create_time', 'timestamp NULL');
SELECT * FROM sysdb_add_column('sys_machine', 'update_time', 'timestamp NULL');
SELECT * FROM sysdb_add_column('sys_machine', 'secret_key', 'varchar(255) NULL');
SELECT * FROM sysdb_add_column('sys_machine', 'api_url', 'varchar(255) NULL');

