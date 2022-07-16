-- public.sys_user_file_conf definition

-- Drop table

-- DROP TABLE public.sys_user_file_conf;

CREATE TABLE IF NOT EXISTS public.sys_user_file_conf (
	user_id varchar(20) NOT NULL,
	create_time timestamp NULL,
	expiry_time timestamp NULL,
	space_limit int8 NULL,
	used_space int8 NULL,
	url_prefix varchar(255) NULL,
	CONSTRAINT sys_user_file_conf_pkey PRIMARY KEY (user_id)
);