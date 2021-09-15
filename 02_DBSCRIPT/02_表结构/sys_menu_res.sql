-- Table: public.sys_menu_res

-- DROP TABLE public.sys_menu_res;

CREATE TABLE IF NOT EXISTS public.sys_menu_res
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    menu_id character varying(20) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    type character varying(255) COLLATE pg_catalog."default",
    uri character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT sys_menu_res_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_menu_res
    OWNER to postgres;