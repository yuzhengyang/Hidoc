-- public.sys_role definition

-- Drop table

-- DROP TABLE public.sys_role;

CREATE TABLE IF NOT EXISTS public.sys_role (
	id varchar(20) NOT NULL,
	"name" varchar(255) NULL,
	"desc" varchar(255) NULL,
	CONSTRAINT sys_role_pkey PRIMARY KEY (id)
);