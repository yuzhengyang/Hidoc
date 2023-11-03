
CREATE TABLE IF NOT EXISTS public.soft_sup_version (
	id varchar(20) NOT NULL,
	base_id varchar(20) NOT NULL,
	"version" varchar(255) NOT NULL,
	is_necessary bool NULL,
	description text NULL,
	create_user_id varchar(20) NULL,
	update_user_id varchar(20) NULL,
	is_delete bool NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	url_first varchar(512) NULL,
	url_second varchar(512) NULL,
	url_third varchar(512) NULL,
	md5 varchar(64) NULL,
	release_path varchar(512) NULL,
	num int8 NULL,
	CONSTRAINT pk_soft_sup_version PRIMARY KEY (id)
);