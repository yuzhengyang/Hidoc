-- Table: public.doc_collected

-- DROP TABLE public.doc_collected;

CREATE TABLE IF NOT EXISTS public.doc_collected
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    create_user_id character varying(20) COLLATE pg_catalog."default",
    owner_user_id character varying(20) COLLATE pg_catalog."default",
    create_time timestamp without time zone,
    is_open boolean,
    is_delete boolean,
    CONSTRAINT doc_collected_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.doc_collected
    OWNER to postgres;

COMMENT ON TABLE public.doc_collected
    IS '文档集';

COMMENT ON COLUMN public.doc_collected.id
    IS '主键';

COMMENT ON COLUMN public.doc_collected.name
    IS '名称';

COMMENT ON COLUMN public.doc_collected.description
    IS '描述';

COMMENT ON COLUMN public.doc_collected.create_user_id
    IS '创建用户id';

COMMENT ON COLUMN public.doc_collected.owner_user_id
    IS '所属用户id';