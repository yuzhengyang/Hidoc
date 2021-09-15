-- Table: public.sys_machine

-- DROP TABLE public.sys_machine;

CREATE TABLE IF NOT EXISTS public.sys_machine
(
    machine_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    ip character varying(255) COLLATE pg_catalog."default",
    mac character varying(255) COLLATE pg_catalog."default",
    data_center_id integer,
    worker_id integer,
    CONSTRAINT sys_machine_pkey PRIMARY KEY (machine_id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_machine
    OWNER to postgres;

COMMENT ON TABLE public.sys_machine
    IS '服务器配置信息';