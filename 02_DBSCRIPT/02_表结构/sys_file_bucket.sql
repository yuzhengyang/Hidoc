-- Table: public.sys_file_bucket

-- DROP TABLE public.sys_file_bucket;

CREATE TABLE IF NOT EXISTS public.sys_file_bucket
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    user_id character varying(20) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    is_open boolean,
    CONSTRAINT sys_file_bucket_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_file_bucket
    OWNER to postgres;

COMMENT ON TABLE public.sys_file_bucket
    IS '文件桶信息';