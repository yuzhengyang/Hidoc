
CREATE TABLE IF NOT EXISTS public.soft_sup_client (
	id varchar(512) NOT NULL,
	base_id varchar(20) NOT NULL,
	"client_type" varchar(255) NULL,
	"name" varchar(255) NULL,
	"email" varchar(255) NULL,
	"ip" varchar(255) NULL,
	"ip_location" varchar(255) NULL,
	"map_location" varchar(512) NULL,
	"version" varchar(255) NULL,
	is_limit_access bool NULL,
	forbid_limit_id varchar(20) NOT NULL,
	free_time int8 NULL,
	work_time int8 NULL,
	free_time_per int4 NULL,
	work_time_per int4 NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	"lock_version" varchar(255) NULL,
	remark varchar(512) NULL,
	CONSTRAINT pk_soft_sup_client PRIMARY KEY (id)
);