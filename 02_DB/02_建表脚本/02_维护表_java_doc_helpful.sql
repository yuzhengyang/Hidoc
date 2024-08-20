CREATE TABLE IF NOT EXISTS "java_doc_helpful" (
  id varchar(20) NOT NULL,
  meta_id varchar(2048),
  class_id varchar(2048),
  project_id varchar(2048),
  user_id varchar(20) ,
  create_time timestamp(6),
  is_helpful bool,
  CONSTRAINT java_doc_helpful_pkey PRIMARY KEY (id)
)
;