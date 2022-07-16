-- public.sys_menu definition

-- Drop table

-- DROP TABLE public.sys_menu;

CREATE TABLE IF NOT EXISTS public.sys_menu (
	id varchar(20) NOT NULL,
	"name" varchar(255) NULL,
	uri varchar(255) NULL,
	icon varchar(255) NULL,
	CONSTRAINT sys_menu_pkey PRIMARY KEY (id)
);