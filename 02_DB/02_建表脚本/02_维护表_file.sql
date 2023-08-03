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
	is_delete bool DEFAULT false,
	delete_time timestamp NULL,
	is_clean bool DEFAULT false,
	clean_time timestamp NULL,
	download_time timestamp NULL,
	download_count int8 DEFAULT 0,
	CONSTRAINT sys_file_pkey PRIMARY KEY (id)
);



SELECT * FROM sysdb_add_column('file', 'is_delete', 'bool DEFAULT false');
SELECT * FROM sysdb_add_column('file', 'delete_time', 'timestamp NULL');
SELECT * FROM sysdb_add_column('file', 'is_clean', 'bool DEFAULT false');
SELECT * FROM sysdb_add_column('file', 'clean_time', 'timestamp NULL');
SELECT * FROM sysdb_add_column('file', 'download_time', 'timestamp NULL');
SELECT * FROM sysdb_add_column('file', 'download_count', 'int8 DEFAULT 0');
SELECT * FROM sysdb_add_column('file', 'check_status', 'varchar(10) NULL');
SELECT * FROM sysdb_add_column('file', 'check_time', 'timestamp NULL');


-- 更新字段默认值
UPDATE file SET is_delete = false WHERE is_delete IS NULL;
UPDATE file SET is_clean = false WHERE is_clean IS NULL;
UPDATE file SET download_count = 0 WHERE download_count IS NULL;