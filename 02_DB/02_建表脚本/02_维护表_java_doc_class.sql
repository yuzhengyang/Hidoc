-- public.java_doc_class definition

-- Drop table

-- DROP TABLE public.java_doc_class;

CREATE TABLE IF NOT EXISTS public.java_doc_class (
	id varchar(2048) NOT NULL,
	project_id varchar(20) NOT NULL,
	create_user_id varchar(20) NULL,
	create_time timestamp(6) NULL,
	"name" varchar(1024) NULL,
	package_info varchar(2048) NULL,
	imports text NULL,
	imports_json jsonb NULL,
	qualifier varchar(2048) NULL,
	comment_info text NULL,
	comment_scene text NULL,
	comment_limit text NULL,
	comment_example text NULL,
	comment_log text NULL,
	comment_keywords text NULL,
	comment_log_json jsonb NULL,
	original_document text NULL,
	project_name varchar(1024) NULL,
	is_struct bool NULL,
	comment_menu text NULL,
	CONSTRAINT java_doc_class_pkey PRIMARY KEY (id)
);


-- 增加字段

-- SELECT * FROM sysdb_add_column('java_doc_class', 'menu_id', 'varchar(20) NULL');


-- 修改字段长度
SELECT * FROM sysdb_alter_column('java_doc_class', 'id', 'type varchar(2048)');
SELECT * FROM sysdb_alter_column('java_doc_class', 'project_id', 'type varchar(2048)');