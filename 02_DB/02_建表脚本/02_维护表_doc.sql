-- public.doc definition

-- Drop table

-- DROP TABLE public.doc;

CREATE TABLE IF NOT EXISTS public.doc (
	id varchar(20) NOT NULL,
	collected_id varchar(20) NULL,
	doc_type varchar(255) NULL,
	title varchar(255) NULL,
	"content" text NULL,
	content_length int8 NULL,
	content_type varchar(255) NULL,
	tag varchar(255) NULL,
	supporter_count int8 NULL,
	protester_count int8 NULL,
	serial_number int4 NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	delete_time timestamp NULL,
	create_user_id varchar(20) NULL,
	update_user_id varchar(20) NULL,
	delete_user_id varchar(20) NULL,
	lock_user_id varchar(20) NULL,
	lock_time timestamp NULL,
	owner_user_id varchar(20) NULL,
	is_delete bool NULL,
	CONSTRAINT doc_pkey PRIMARY KEY (id)
);


-- 增加字段


SELECT * FROM sysdb_add_column('doc', 'parent_doc_id', 'varchar(20) NULL');
SELECT * FROM sysdb_add_column('doc', 'supporter_count', 'int8 NULL');
SELECT * FROM sysdb_add_column('doc', 'protester_count', 'int8 NULL');
SELECT * FROM sysdb_add_column('doc', 'delete_time', 'timestamp NULL');
SELECT * FROM sysdb_add_column('doc', 'delete_user_id', 'varchar(20) NULL');
SELECT * FROM sysdb_add_column('doc', 'lock_time', 'timestamp NULL');