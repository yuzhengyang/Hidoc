-- Table: public.sys_user_file_conf

-- DROP TABLE public.sys_user_file_conf;

CREATE TABLE IF NOT EXISTS public.sys_user_file_conf
(
    user_id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    create_time timestamp without time zone,
    expiry_time timestamp without time zone,
    space_limit bigint,
    used_space bigint,
    url_prefix character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT sys_user_file_conf_pkey PRIMARY KEY (user_id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_user_file_conf
    OWNER to postgres;

COMMENT ON TABLE public.sys_user_file_conf
    IS '用户文件上传配置信息（配额）';