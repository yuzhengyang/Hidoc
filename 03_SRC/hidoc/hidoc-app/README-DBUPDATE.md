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
DROP TABLE `sys_access_log`;
CREATE TABLE `sys_access_log` (
  `id` varchar(20) NOT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `begin_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `uri` varchar(1024) DEFAULT NULL,
  `method` varchar(20) DEFAULT NULL,
  `elapsed_time` bigint DEFAULT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  `step` varchar(200) DEFAULT NULL,
  `begin_max_memory` bigint DEFAULT NULL,
  `begin_total_memory` bigint DEFAULT NULL,
  `begin_free_memory` bigint DEFAULT NULL,
  `end_max_memory` bigint DEFAULT NULL,
  `end_total_memory` bigint DEFAULT NULL,
  `end_free_memory` bigint DEFAULT NULL,
  `probably_use_memory` bigint DEFAULT NULL,
  `thread_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
```