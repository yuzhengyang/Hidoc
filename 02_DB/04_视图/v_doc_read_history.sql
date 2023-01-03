CREATE VIEW "public"."v_doc_read_history" AS  SELECT dal.ip,
    dal.create_time,
    su.real_name,
    dc.name,
    d.title,
    su2.real_name AS create_user,
    su3.real_name AS owner_user
   FROM (((((doc_access_log dal
     LEFT JOIN sys_user su ON (((dal.user_id)::text = (su.id)::text)))
     LEFT JOIN doc_collected dc ON (((dal.collected_id)::text = (dc.id)::text)))
     LEFT JOIN doc d ON (((dal.doc_id)::text = (d.id)::text)))
     LEFT JOIN sys_user su2 ON (((dal.create_user_id)::text = (su2.id)::text)))
     LEFT JOIN sys_user su3 ON (((dal.owner_user_id)::text = (su3.id)::text)))
  ORDER BY dal.create_time DESC;