-- Table: public.sys_role

-- DROP TABLE public.sys_role;

CREATE TABLE IF NOT EXISTS public.sys_role
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    "desc" character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT sys_role_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_role
    OWNER to postgres;