-- public.java_doc_menu definition

-- Drop table

-- DROP TABLE public.java_doc_menu;

CREATE TABLE IF NOT EXISTS public.java_doc_menu (
	id varchar(20) NOT NULL,
	menu varchar(255) NULL,
	parent_id varchar(20) NULL,
	project_id varchar(20) NULL,
	menu_path varchar(1024) NULL,
	"level" int4 NULL,
	CONSTRAINT doc_menu_pkey PRIMARY KEY (id)
);

-- 增加字段


-- SELECT * FROM sysdb_add_column('java_doc_menu', 'return_desc', 'varchar(1024) NULL');


-- 修改字段长度
SELECT * FROM sysdb_alter_column('java_doc_menu', 'id', 'type varchar(2048)');
SELECT * FROM sysdb_alter_column('java_doc_menu', 'parent_id', 'type varchar(2048)');
SELECT * FROM sysdb_alter_column('java_doc_menu', 'project_id', 'type varchar(2048)');