-- Drop table

-- DROP TABLE public.doc_comment;

CREATE TABLE IF NOT EXISTS public.doc_comment (
	id varchar(20) NOT NULL,
	doc_id varchar(20) NULL,
	create_time timestamp NULL,
	create_user_id varchar(20) NULL,
	reply_comment_id varchar(20) NULL,
	reply_user_id varchar(20) NULL,
	is_doc_owner_read bool NULL,
	doc_owner_read_time timestamp NULL,
	is_reply_user_read bool NULL,
	reply_user_read_time timestamp NULL,
	CONSTRAINT doc_comment_pkey PRIMARY KEY (id)
);



-- 增加字段


SELECT * FROM sysdb_add_column('doc_comment', 'is_delete', 'bool NULL');
SELECT * FROM sysdb_add_column('doc_comment', 'content', 'text NULL');