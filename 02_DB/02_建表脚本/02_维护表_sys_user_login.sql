
CREATE TABLE IF NOT EXISTS public.sys_user_login (
	token varchar(128) NOT NULL,
	user_id varchar(128) NOT NULL,
	ip varchar(255) NULL,
	login_time timestamp NULL,
	expiry_time timestamp NULL,
	user_info jsonb NULL,
	CONSTRAINT sys_user_login_pkey PRIMARY KEY (token)
);

SELECT * FROM sysdb_add_column('sys_user_login', 'user_info', 'jsonb NULL');