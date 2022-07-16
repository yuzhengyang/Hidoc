-- public.data_coll_plan definition

-- Drop table

-- DROP TABLE public.data_coll_plan;

CREATE TABLE IF NOT EXISTS public.data_coll_plan (
	id varchar(20) NOT NULL,
	"name" varchar(255) NOT NULL,
	description text NULL,
	start_time timestamp NOT NULL,
	stop_time timestamp NOT NULL,
	is_enable bool NOT NULL,
	data_count int8 NULL,
	"token" varchar(64) NULL,
	create_user_id varchar(20) NULL,
	owner_user_id varchar(20) NULL,
	is_delete bool NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	CONSTRAINT dc_plan_pkey PRIMARY KEY (id)
);