-- Table: public.sys_file_cursor

-- DROP TABLE public.sys_file_cursor;

CREATE TABLE IF NOT EXISTS public.sys_file_cursor
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    bucket_id character varying(20) COLLATE pg_catalog."default",
    file_id character varying(20) COLLATE pg_catalog."default",
    file_name character varying(255) COLLATE pg_catalog."default",
    user_id character varying(20) COLLATE pg_catalog."default",
    create_time timestamp without time zone,
    version character varying(20) COLLATE pg_catalog."default",
    expiry_time timestamp without time zone,
    collected_id character varying(20) COLLATE pg_catalog."default",
    uname character varying(200) COLLATE pg_catalog."default",
    CONSTRAINT sys_file_cursor_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_file_cursor
    OWNER to postgres;

COMMENT ON TABLE public.sys_file_cursor
    IS '文件指针信息（指向文件信息）';

COMMENT ON COLUMN public.sys_file_cursor.id
    IS '主键';

COMMENT ON COLUMN public.sys_file_cursor.bucket_id
    IS '所属桶ID';

COMMENT ON COLUMN public.sys_file_cursor.file_id
    IS '指向文件ID';

COMMENT ON COLUMN public.sys_file_cursor.file_name
    IS '上传文件名称';

COMMENT ON COLUMN public.sys_file_cursor.user_id
    IS '上传用户ID';

COMMENT ON COLUMN public.sys_file_cursor.create_time
    IS '上传时间';

COMMENT ON COLUMN public.sys_file_cursor.version
    IS '版本号';

COMMENT ON COLUMN public.sys_file_cursor.expiry_time
    IS '过期时间';

COMMENT ON COLUMN public.sys_file_cursor.collected_id
    IS '所属文集ID';

COMMENT ON COLUMN public.sys_file_cursor.uname
    IS '下载唯一文件名';