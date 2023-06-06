-- public.file_bucket definition

-- Drop table

-- DROP TABLE public.file_bucket;

CREATE TABLE IF NOT EXISTS public.file_bucket (
	id varchar(20) NOT NULL,
	user_id varchar(20) NULL,
	"name" varchar(255) NULL,
	is_open bool NULL,
	is_delete bool NULL,
	CONSTRAINT sys_file_bucket_pkey PRIMARY KEY (id)
);


SELECT * FROM sysdb_add_column('file_bucket', 'is_delete', 'bool DEFAULT false');


SELECT * FROM sysdb_add_unique_index('file_bucket', 'uidx_file_bucket_user_id_name', '"user_id", "name"');