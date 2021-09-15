-- Table: public.sys_file_download_log

-- DROP TABLE public.sys_file_download_log;

CREATE TABLE IF NOT EXISTS public.sys_file_download_log
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    ip character varying(255) COLLATE pg_catalog."default",
    create_time timestamp without time zone,
    cursor_id character varying(20) COLLATE pg_catalog."default",
    file_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT sys_file_download_log_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_file_download_log
    OWNER to postgres;

COMMENT ON TABLE public.sys_file_download_log
    IS '文件下载记录';