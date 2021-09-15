-- Table: public.sys_user

-- DROP TABLE public.sys_user;

CREATE TABLE IF NOT EXISTS public.sys_user
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    avatar character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    real_name character varying(255) COLLATE pg_catalog."default",
    email character varying(255) COLLATE pg_catalog."default",
    create_time timestamp without time zone,
    update_time timestamp without time zone,
    password character varying(255) COLLATE pg_catalog."default",
    is_frozen boolean,
    CONSTRAINT sys_user_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_user
    OWNER to postgres;

COMMENT ON TABLE public.sys_user
    IS '用户信息';

COMMENT ON COLUMN public.sys_user.id
    IS '主键';

COMMENT ON COLUMN public.sys_user.avatar
    IS '头像';

COMMENT ON COLUMN public.sys_user.name
    IS '名称';

COMMENT ON COLUMN public.sys_user.real_name
    IS '实名';

COMMENT ON COLUMN public.sys_user.email
    IS '邮箱';

COMMENT ON COLUMN public.sys_user.create_time
    IS '创建时间';

COMMENT ON COLUMN public.sys_user.update_time
    IS '更新时间';

COMMENT ON COLUMN public.sys_user.password
    IS '密码';
-- Index: name

-- DROP INDEX public.name;

CREATE INDEX name
    ON public.sys_user USING btree
    (name COLLATE pg_catalog."default" ASC NULLS LAST)
    TABLESPACE pg_default;

COMMENT ON INDEX public.name
    IS '用户名唯一索引';