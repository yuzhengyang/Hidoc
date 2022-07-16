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
	is_open bool NULL,
	is_delete bool NULL,
	CONSTRAINT doc_collected_pkey PRIMARY KEY (id)
);


SELECT * FROM sysdb_add_column('doc_collected', 'is_login_access', 'bool NULL');
SELECT * FROM sysdb_add_column('doc_collected', 'password_access', 'varchar(255) NULL');