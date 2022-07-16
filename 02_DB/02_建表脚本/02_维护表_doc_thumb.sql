-- Drop table

-- DROP TABLE public.doc_thumb;

CREATE TABLE IF NOT EXISTS public.doc_thumb (
	id varchar(255) NOT NULL,
	table_name varchar(64) NULL,
	data_id varchar(64) NULL,
	user_id varchar(20) NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	is_supporter bool NULL,
	is_protester bool NULL,
	CONSTRAINT doc_thumb_pkey PRIMARY KEY (id)
);
