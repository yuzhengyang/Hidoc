-- public.sys_urse_role definition

-- Drop table

-- DROP TABLE public.sys_urse_role;

CREATE TABLE IF NOT EXISTS public.sys_urse_role (
	id varchar(20) NOT NULL,
	user_id varchar(20) NULL,
	role_id varchar(20) NULL,
	CONSTRAINT sys_urse_role_pkey PRIMARY KEY (id)
);