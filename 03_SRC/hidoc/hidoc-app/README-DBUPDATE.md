# 数据库升级记录

## 2021年11月20日
```
java_doc_method class_name character varying 1024

java_doc_method project_name character varying 1024

java_doc_method source_code text

java_doc_class project_name character varying 1024

```

## 2021年12月4日
```
-- Table: public.doc_access_log

-- DROP TABLE public.doc_access_log;

CREATE TABLE IF NOT EXISTS public.doc_access_log
(
    id character varying(20) COLLATE pg_catalog."default",
    ip character varying(64) COLLATE pg_catalog."default",
    create_date date,
    create_time timestamp without time zone,
    user_id character varying(20) COLLATE pg_catalog."default",
    collected_id character varying(20) COLLATE pg_catalog."default",
    doc_id character varying(20) COLLATE pg_catalog."default",
    create_user_id character varying(20) COLLATE pg_catalog."default",
    owner_user_id character varying(20) COLLATE pg_catalog."default"
)

TABLESPACE pg_default;

ALTER TABLE public.doc_access_log
    OWNER to postgres;

```

## 2022年2月15日
```
-- Table: public.sys_access_log

-- DROP TABLE public.sys_access_log;

CREATE TABLE IF NOT EXISTS public.sys_access_log
(
    id character varying(20) COLLATE pg_catalog."default" NOT NULL,
    ip character varying(255) COLLATE pg_catalog."default",
    begin_time timestamp without time zone,
    end_time timestamp without time zone,
    uri character varying(1024) COLLATE pg_catalog."default",
    method character varying(255) COLLATE pg_catalog."default",
    elapsed_time bigint,
    user_id character varying(20) COLLATE pg_catalog."default",
    step character varying(255) COLLATE pg_catalog."default",
    begin_max_memory bigint,
    begin_total_memory bigint,
    begin_free_memory bigint,
    end_max_memory bigint,
    end_total_memory bigint,
    end_free_memory bigint,
    probably_use_memory bigint,
    thread_name character(255) COLLATE pg_catalog."default",
    exception text COLLATE pg_catalog."default",
    CONSTRAINT sys_access_log_pkey PRIMARY KEY (id,ip,uri,method)
)

TABLESPACE pg_default;

ALTER TABLE public.sys_access_log
    OWNER to postgres;
```


## 2022-03-01
ALTER TABLE public.java_doc_class ADD is_struct bool NULL;

ALTER TABLE public.java_doc_method ADD is_struct bool NULL;