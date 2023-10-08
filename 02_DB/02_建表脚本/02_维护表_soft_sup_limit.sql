
CREATE TABLE IF NOT EXISTS public.soft_sup_limit (
	id varchar(20) NOT NULL,
	base_id varchar(20) NOT NULL,
	is_enable bool NOT NULL,
	"name" varchar(255) NOT NULL,
	limit_type varchar(255) NULL,
	limit_content text NULL,
	create_user_id varchar(20) NULL,
	update_user_id varchar(20) NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	CONSTRAINT pk_soft_sup_limit PRIMARY KEY (id)
);