-- public.sys_menu_res definition

-- Drop table

-- DROP TABLE public.sys_menu_res;

CREATE TABLE IF NOT EXISTS public.sys_menu_res (
	id varchar(20) NOT NULL,
	menu_id varchar(20) NULL,
	"name" varchar(255) NULL,
	"type" varchar(255) NULL,
	uri varchar(255) NULL,
	CONSTRAINT sys_menu_res_pkey PRIMARY KEY (id)
);