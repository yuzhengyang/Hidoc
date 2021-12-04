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