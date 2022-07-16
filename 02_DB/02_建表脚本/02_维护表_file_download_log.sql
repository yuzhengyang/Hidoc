-- public.file_download_log definition

-- Drop table

-- DROP TABLE public.file_download_log;

CREATE TABLE IF NOT EXISTS public.file_download_log (
	id varchar(20) NOT NULL,
	ip varchar(255) NULL,
	create_time timestamp NULL,
	cursor_id varchar(20) NULL,
	file_name varchar(255) NULL,
	CONSTRAINT sys_file_download_log_pkey PRIMARY KEY (id)
);