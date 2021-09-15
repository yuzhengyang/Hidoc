-- Table: public.sys_file

-- DROP TABLE public.sys_file;

CREATE TABLE IF NOT EXISTS public.sys_file
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    ext character varying(255) COLLATE pg_catalog."default",
    size bigint,
    path character varying(255) COLLATE pg_catalog."default",
    create_time timestamp without time zone,
    user_id character varying(20) COLLATE pg_catalog."default",
    md5 character varying(255) COLLATE pg_catalog."default",
    sha1 character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT sys_file_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_file
    OWNER to postgres;

COMMENT ON TABLE public.sys_file
    IS '文件信息表（物理文件信息）';