-- public.doc_history definition

-- Drop table

-- DROP TABLE public.doc_history;

CREATE TABLE IF NOT EXISTS public.doc_history (
	id varchar(20) NOT NULL,
	doc_id varchar(20) NULL,
	collected_id varchar(20) NULL,
	doc_type varchar(255) NULL,
	title varchar(255) NULL,
	"content" text NULL,
	content_length int8 NULL,
	content_type varchar(255) NULL,
	tag varchar(255) NULL,
	serial_number int4 NULL,
	create_time timestamp NULL,
	create_user_id varchar(20) NULL,
	update_user_id varchar(20) NULL,
	CONSTRAINT doc_history_pkey PRIMARY KEY (id)
);