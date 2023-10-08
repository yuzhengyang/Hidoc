
CREATE TABLE IF NOT EXISTS public.soft_sup_base (
	id varchar(20) NOT NULL,
	"name" varchar(255) NOT NULL,
	description text NULL,
	"token" varchar(64) NULL,
	create_user_id varchar(20) NULL,
	update_user_id varchar(20) NULL,
	owner_user_id varchar(20) NULL,
	is_delete bool NULL,
	create_time timestamp NULL,
	update_time timestamp NULL,
	CONSTRAINT pk_soft_sup_base PRIMARY KEY (id)
);