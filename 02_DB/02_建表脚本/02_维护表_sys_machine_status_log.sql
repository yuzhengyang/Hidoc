-- public.sys_machine_status_log definition

-- Drop table

-- DROP TABLE public.sys_machine_status_log;

CREATE TABLE IF NOT EXISTS public.sys_machine_status_log (
	id varchar(20) NOT NULL,
	machine_id varchar(32) NULL,
	create_time timestamp NULL,
	cpu int4 NULL,
	ram int8 NULL,
	disk int8 NULL,
	app_cpu int4 NULL,
	app_ram int8 NULL,
	ss_long text NULL,
	CONSTRAINT sys_machine_status_log_pkey PRIMARY KEY (id)
);