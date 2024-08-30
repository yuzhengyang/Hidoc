-- public.java_doc_project definition

-- Drop table

-- DROP TABLE public.java_doc_project;

CREATE TABLE IF NOT EXISTS public.java_doc_project (
	id varchar(20) NOT NULL,
	"name" varchar(64) NOT NULL,
	description text NULL,
	teams_read text NULL,
	teams_code text NULL,
	create_user_id varchar(20) NULL,
	update_user_id varchar(20) NULL,
	create_time timestamp(6) NULL,
	update_time timestamp(6) NULL,
	CONSTRAINT java_doc_project_pkey PRIMARY KEY (id)
);

-- �����ֶ�
SELECT * FROM sysdb_add_column('java_doc_project', 'token', 'varchar(1024) NULL');
SELECT * FROM sysdb_add_column('java_doc_project', 'teams_read', 'text NULL');
SELECT * FROM sysdb_add_column('java_doc_project', 'teams_code', 'text NULL');

-- �޸��ֶγ���
SELECT * FROM sysdb_alter_column('java_doc_project', 'id', 'type varchar(2048)');