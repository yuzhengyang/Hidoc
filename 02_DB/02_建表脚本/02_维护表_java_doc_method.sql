-- public.java_doc_method definition

-- Drop table

-- DROP TABLE public.java_doc_method;

CREATE TABLE IF NOT EXISTS public.java_doc_method (
	id varchar(20) NOT NULL,
	class_id varchar(20) NOT NULL,
	project_id varchar(20) NOT NULL,
	create_user_id varchar(20) NULL,
	create_time timestamp(6) NULL,
	"name" varchar(1024) NULL,
	qualifier varchar(2048) NULL,
	return_type varchar(1024) NULL,
	params text NULL,
	params_json jsonb NULL,
	comment_info text NULL,
	comment_scene text NULL,
	comment_limit text NULL,
	comment_example text NULL,
	comment_log text NULL,
	comment_keywords text NULL,
	comment_log_json jsonb NULL,
	class_name varchar(1024) NULL,
	project_name varchar(1024) NULL,
	source_code text NULL,
	is_struct bool NULL,
	comment_menu text NULL,
	CONSTRAINT java_doc_method_pkey PRIMARY KEY (id)
);

-- 增加字段


SELECT * FROM sysdb_add_column('java_doc_method', 'return_desc', 'varchar(1024) NULL');
SELECT * FROM sysdb_add_column('java_doc_method', 'throwses', 'varchar(2048) NULL');
SELECT * FROM sysdb_add_column('java_doc_method', 'throwses_json', 'jsonb NULL');

-- 修改字段长度
SELECT * FROM sysdb_alter_column('java_doc_method', 'id', 'type varchar(2048)');
SELECT * FROM sysdb_alter_column('java_doc_method', 'class_id', 'type varchar(2048)');
SELECT * FROM sysdb_alter_column('java_doc_method', 'project_id', 'type varchar(2048)');