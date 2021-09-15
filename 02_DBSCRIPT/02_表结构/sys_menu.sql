-- Table: public.sys_menu

-- DROP TABLE public.sys_menu;

CREATE TABLE IF NOT EXISTS public.sys_menu
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    uri character varying(255) COLLATE pg_catalog."default",
    icon character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT sys_menu_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_menu
    OWNER to postgres;