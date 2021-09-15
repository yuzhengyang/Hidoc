-- Table: public.doc

-- DROP TABLE public.doc;

CREATE TABLE IF NOT EXISTS public.doc
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    collected_id character varying(20) COLLATE pg_catalog."default",
    doc_type character varying(255) COLLATE pg_catalog."default",
    title character varying(255) COLLATE pg_catalog."default",
    content text COLLATE pg_catalog."default",
    content_length bigint,
    content_type character varying(255) COLLATE pg_catalog."default",
    tag character varying(255) COLLATE pg_catalog."default",
    serial_number integer,
    create_time timestamp without time zone,
    update_time timestamp without time zone,
    create_user_id character varying(20) COLLATE pg_catalog."default",
    update_user_id character varying(20) COLLATE pg_catalog."default",
    lock_user_id character varying(20) COLLATE pg_catalog."default",
    owner_user_id character varying(20) COLLATE pg_catalog."default",
    is_delete boolean,
    CONSTRAINT doc_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.doc
    OWNER to postgres;

COMMENT ON TABLE public.doc
    IS '文档信息';

COMMENT ON COLUMN public.doc.id
    IS '主键';

COMMENT ON COLUMN public.doc.collected_id
    IS '文档集id';

COMMENT ON COLUMN public.doc.doc_type
    IS '文档类型';

COMMENT ON COLUMN public.doc.title
    IS '标题';

COMMENT ON COLUMN public.doc.content
    IS '内容';

COMMENT ON COLUMN public.doc.content_length
    IS '内容长度';

COMMENT ON COLUMN public.doc.content_type
    IS '内容类型';

COMMENT ON COLUMN public.doc.tag
    IS '标签';

COMMENT ON COLUMN public.doc.serial_number
    IS '排序序号';

COMMENT ON COLUMN public.doc.create_time
    IS '创建时间';

COMMENT ON COLUMN public.doc.update_time
    IS '更新时间';

COMMENT ON COLUMN public.doc.create_user_id
    IS '创建用户id';

COMMENT ON COLUMN public.doc.update_user_id
    IS '更新用户id';

COMMENT ON COLUMN public.doc.lock_user_id
    IS '锁定用户id';

COMMENT ON COLUMN public.doc.owner_user_id
    IS '所属用户id';