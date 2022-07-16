-- public.data_coll definition

-- Drop table

-- DROP TABLE public.data_coll;

CREATE TABLE IF NOT EXISTS public.data_coll (
	id varchar(20) NOT NULL,
	create_time timestamp NOT NULL,
	plan_id varchar(20) NOT NULL,
	data_source varchar(64) NULL,
	client_type varchar(64) NULL,
	ip varchar(64) NULL,
	mac varchar(64) NULL,
	sender_id varchar(64) NULL,
	sender_name varchar(64) NULL,
	"data" jsonb NULL,
	data_type varchar(64) NULL,
	CONSTRAINT dc_data_pkey PRIMARY KEY (id)
);