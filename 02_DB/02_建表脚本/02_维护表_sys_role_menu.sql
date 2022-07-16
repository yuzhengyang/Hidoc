-- public.sys_role_menu definition

-- Drop table

-- DROP TABLE public.sys_role_menu;

CREATE TABLE IF NOT EXISTS public.sys_role_menu (
	id varchar(20) NOT NULL,
	role_id varchar(20) NULL,
	menu_id varchar(20) NULL,
	CONSTRAINT sys_role_menu_pkey PRIMARY KEY (id)
);