-- Table: public.doc_history

-- DROP TABLE public.doc_history;

CREATE TABLE IF NOT EXISTS public.doc_history
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    doc_id character varying(20) COLLATE pg_catalog."default",
    collected_id character varying(20) COLLATE pg_catalog."default",
    doc_type character varying(255) COLLATE pg_catalog."default",
    title character varying(255) COLLATE pg_catalog."default",
    content text COLLATE pg_catalog."default",
    content_length bigint,
    content_type character varying(255) COLLATE pg_catalog."default",
    tag character varying(255) COLLATE pg_catalog."default",
    serial_number integer,
    create_time timestamp without time zone,
    create_user_id character varying(20) COLLATE pg_catalog."default",
    update_user_id character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT doc_history_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.doc_history
    OWNER to postgres;

COMMENT ON TABLE public.doc_history
    IS '文档修改历史';

COMMENT ON COLUMN public.doc_history.id
    IS '主键id';

COMMENT ON COLUMN public.doc_history.doc_id
    IS '文档id';

COMMENT ON COLUMN public.doc_history.collected_id
    IS '文档集id';

COMMENT ON COLUMN public.doc_history.doc_type
    IS '文档类型';

COMMENT ON COLUMN public.doc_history.title
    IS '标题';

COMMENT ON COLUMN public.doc_history.content
    IS '内容';

COMMENT ON COLUMN public.doc_history.content_length
    IS '内容长度';

COMMENT ON COLUMN public.doc_history.content_type
    IS '内容类型';

COMMENT ON COLUMN public.doc_history.tag
    IS '标签';

COMMENT ON COLUMN public.doc_history.serial_number
    IS '排序序号';

COMMENT ON COLUMN public.doc_history.create_time
    IS '创建时间';

COMMENT ON COLUMN public.doc_history.create_user_id
    IS '创建用户id';

COMMENT ON COLUMN public.doc_history.update_user_id
    IS '更新用户id';