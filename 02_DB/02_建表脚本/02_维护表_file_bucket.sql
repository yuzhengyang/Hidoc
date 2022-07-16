-- public.file_bucket definition

-- Drop table

-- DROP TABLE public.file_bucket;

CREATE TABLE IF NOT EXISTS public.file_bucket (
	id varchar(20) NOT NULL,
	user_id varchar(20) NULL,
	"name" varchar(255) NULL,
	is_open bool NULL,
	CONSTRAINT sys_file_bucket_pkey PRIMARY KEY (id)
);