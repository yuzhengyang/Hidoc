-- Table: public.sys_access_log

-- DROP TABLE public.sys_access_log;

CREATE TABLE IF NOT EXISTS public.sys_access_log
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    ip character varying(255) COLLATE pg_catalog."default",
    create_time timestamp without time zone,
    uri character varying(255) COLLATE pg_catalog."default",
    method character varying(100) COLLATE pg_catalog."default",
    elapsed_time bigint,
    user_id character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT sys_access_log_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_access_log
    OWNER to postgres;