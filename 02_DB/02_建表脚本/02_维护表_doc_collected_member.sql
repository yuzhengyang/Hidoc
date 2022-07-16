-- public.doc_collected_member definition

-- Drop table

-- DROP TABLE public.doc_collected_member;

CREATE TABLE IF NOT EXISTS public.doc_collected_member (
	id varchar(20) NOT NULL,
	collected_id varchar(20) NULL,
	user_id varchar(20) NULL,
	create_user_id varchar(20) NULL,
	create_time timestamp NULL,
	allow_edit bool NULL,
	CONSTRAINT doc_collected_member_pkey PRIMARY KEY (id)
);