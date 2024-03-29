-- public.doc_collected definition

-- Drop table

-- DROP TABLE public.doc_collected;

CREATE TABLE IF NOT EXISTS public.doc_collected (
	id varchar(20) NOT NULL,
	"name" varchar(255) NULL,
	description text NULL,
	create_user_id varchar(20) NULL,
	owner_user_id varchar(20) NULL,
	create_time timestamp NULL,
	is_login_access bool DEFAULT false,
	is_open bool DEFAULT false,
	is_delete bool DEFAULT false,
	is_templet bool DEFAULT false,
    team_id_list jsonb,
	doc_count int8 DEFAULT 0,
	CONSTRAINT doc_collected_pkey PRIMARY KEY (id)
);


SELECT * FROM sysdb_add_column('doc_collected', 'is_login_access', 'bool DEFAULT false');
SELECT * FROM sysdb_add_column('doc_collected', 'password_access', 'varchar(255) NULL');
SELECT * FROM sysdb_add_column('doc_collected', 'is_templet', 'bool DEFAULT false');
SELECT * FROM sysdb_add_column('doc_collected', 'team_id_list', 'jsonb');
SELECT * FROM sysdb_add_column('doc_collected', 'doc_count', 'int8 DEFAULT 0');


UPDATE doc_collected SET is_open = false WHERE is_open IS NULL;
UPDATE doc_collected SET is_delete = false WHERE is_delete IS NULL;
UPDATE doc_collected SET is_templet = false WHERE is_templet IS NULL;
UPDATE doc_collected SET is_login_access = false WHERE is_login_access IS NULL;



