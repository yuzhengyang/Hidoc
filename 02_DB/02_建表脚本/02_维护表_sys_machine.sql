-- public.sys_machine definition

-- Drop table

-- DROP TABLE public.sys_machine;

CREATE TABLE IF NOT EXISTS public.sys_machine (
	machine_id varchar(255) NOT NULL,
	ip varchar(255) NULL,
	mac varchar(255) NULL,
	data_center_id int4 NULL,
	worker_id int4 NULL,
	CONSTRAINT sys_machine_pkey PRIMARY KEY (machine_id)
);