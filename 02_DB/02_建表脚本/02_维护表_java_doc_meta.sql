-- public.java_doc_method definition

-- Drop table

-- DROP TABLE public.java_doc_meta;

CREATE TABLE IF NOT EXISTS public.java_doc_meta (
	id varchar(2048) NOT NULL,
	create_user_id varchar(20) NOT NULL,
	create_time timestamp(6) NOT NULL,
	is_struct bool NULL,
	is_deprecated bool NULL,
	meta_type varchar(256) NULL,
	
	class_id varchar(2048) NULL,
	class_name varchar(1024) NULL,
	project_id varchar(2048) NOT NULL,
	project_name varchar(1024) NOT NULL,
	
	package_info varchar(2048) NULL,
	imports text NULL,
	
	"name" varchar(1024) NULL,
	annotations text NULL,
	qualifier varchar(2048) NULL,
	return_type varchar(1024) NULL,
	return_desc varchar(1024) NULL,
	params text NULL,
	throwses text NULL,
	
	comment_info text NULL,
	comment_scene text NULL,
	comment_limit text NULL,
	comment_example text NULL,
	comment_log text NULL,
	comment_keywords text NULL,
	comment_menu text NULL,
	
	source_code text NULL,
	CONSTRAINT java_doc_meta_pkey PRIMARY KEY (id)
);



-- 增加字段
SELECT * FROM sysdb_add_column('java_doc_meta', 'is_deprecated', 'bool NULL');
SELECT * FROM sysdb_add_column('java_doc_meta', 'annotations', 'text NULL');


-- 修改字段长度
-- SELECT * FROM sysdb_alter_column('java_doc_meta', 'id', 'type varchar(2048)');