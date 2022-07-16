-- public.file_cursor definition

-- Drop table

-- DROP TABLE public.file_cursor;

CREATE TABLE IF NOT EXISTS public.file_cursor (
	id varchar(20) NOT NULL,
	bucket_id varchar(20) NULL,
	file_id varchar(20) NULL,
	file_name varchar(255) NULL,
	user_id varchar(20) NULL,
	create_time timestamp NULL,
	"version" varchar(20) NULL,
	expiry_time timestamp NULL,
	collected_id varchar(20) NULL,
	uname varchar(200) NULL,
	is_delete bool NULL,
	delete_user_id varchar(20) NULL,
	delete_time timestamp NULL,
	CONSTRAINT sys_file_cursor_pkey PRIMARY KEY (id)
);


SELECT * FROM sysdb_add_column('file_cursor', 'is_delete', 'bool NULL');
SELECT * FROM sysdb_add_column('file_cursor', 'delete_user_id', 'varchar(20) NULL');
SELECT * FROM sysdb_add_column('file_cursor', 'delete_time', 'timestamp NULL');