-- Table: public.sys_urse_role

-- DROP TABLE public.sys_urse_role;

CREATE TABLE IF NOT EXISTS public.sys_urse_role
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    user_id character varying(20) COLLATE pg_catalog."default",
    role_id character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT sys_urse_role_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_urse_role
    OWNER to postgres;