-- public.doc_access_log definition

-- Drop table

-- DROP TABLE public.doc_access_log;

CREATE TABLE IF NOT EXISTS public.doc_access_log (
	id varchar(20) NULL,
	ip varchar(64) NULL,
	create_date date NULL,
	create_time timestamp NULL,
	user_id varchar(20) NULL,
	collected_id varchar(20) NULL,
	doc_id varchar(20) NULL,
	create_user_id varchar(20) NULL,
	owner_user_id varchar(20) NULL,
	CONSTRAINT doc_access_log_pkey PRIMARY KEY (id)
);



-- 处理历史数据
UPDATE doc_access_log SET user_id = '' WHERE user_id IS NULL;