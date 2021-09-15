-- Table: public.doc_collected_member

-- DROP TABLE public.doc_collected_member;

CREATE TABLE IF NOT EXISTS public.doc_collected_member
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    collected_id character varying(20) COLLATE pg_catalog."default",
    user_id character varying(20) COLLATE pg_catalog."default",
    create_user_id character varying(20) COLLATE pg_catalog."default",
    create_time timestamp without time zone,
    allow_edit boolean,
    CONSTRAINT doc_collected_member_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.doc_collected_member
    OWNER to postgres;

COMMENT ON TABLE public.doc_collected_member
    IS '文档成员';

COMMENT ON COLUMN public.doc_collected_member.id
    IS '主键id';

COMMENT ON COLUMN public.doc_collected_member.collected_id
    IS '文档集id';

COMMENT ON COLUMN public.doc_collected_member.user_id
    IS '用户id';

COMMENT ON COLUMN public.doc_collected_member.create_user_id
    IS '创建用户id';

COMMENT ON COLUMN public.doc_collected_member.create_time
    IS '创建时间';