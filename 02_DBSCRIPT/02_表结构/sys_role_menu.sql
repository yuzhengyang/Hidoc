-- Table: public.sys_role_menu

-- DROP TABLE public.sys_role_menu;

CREATE TABLE IF NOT EXISTS public.sys_role_menu
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    role_id character varying(20) COLLATE pg_catalog."default",
    menu_id character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT sys_role_menu_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_role_menu
    OWNER to postgres;