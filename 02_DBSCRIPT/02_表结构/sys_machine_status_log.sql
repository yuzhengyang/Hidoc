-- Table: public.sys_machine_status_log

-- DROP TABLE public.sys_machine_status_log;

CREATE TABLE IF NOT EXISTS public.sys_machine_status_log
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    machine_id character varying(32) COLLATE pg_catalog."default",
    create_time timestamp without time zone,
    cpu integer,
    ram bigint,
    disk bigint,
    app_cpu integer,
    app_ram bigint,
    ss_long text COLLATE pg_catalog."default",
    CONSTRAINT sys_machine_status_log_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_machine_status_log
    OWNER to postgres;

COMMENT ON TABLE public.sys_machine_status_log
    IS '服务器状态信息记录';