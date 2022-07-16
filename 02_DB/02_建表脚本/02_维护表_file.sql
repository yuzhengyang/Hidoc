-- public.file definition

-- Drop table

-- DROP TABLE public.file;

CREATE TABLE IF NOT EXISTS public.file (
	id varchar(20) NOT NULL,
	"name" varchar(255) NULL,
	ext varchar(255) NULL,
	"size" int8 NULL,
	"path" varchar(255) NULL,
	create_time timestamp NULL,
	user_id varchar(20) NULL,
	md5 varchar(255) NULL,
	sha1 varchar(255) NULL,
	CONSTRAINT sys_file_pkey PRIMARY KEY (id)
);