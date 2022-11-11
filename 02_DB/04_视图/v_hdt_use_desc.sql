CREATE OR REPLACE VIEW "v_hdt_use_desc" AS  SELECT t1.sender_id,
    t1.sender_name,
    t1.first_time,
    t1.last_time,
    t1.total,
    t2.province,
    t2.city,
    t2.district,
    t3.version
   FROM ((( SELECT data_coll.sender_id,
            data_coll.sender_name,
            min(data_coll.create_time) AS first_time,
            max(data_coll.create_time) AS last_time,
            count(1) AS total
           FROM data_coll
          WHERE ((data_coll.plan_id)::text = '101642217487073280'::text)
          GROUP BY data_coll.sender_id, data_coll.sender_name
          ORDER BY (count(1)) DESC) t1
     LEFT JOIN ( SELECT t1_1.sender_id,
            t1_1.sender_name,
            t1_1.create_time,
            ((t2_1.data -> 'Component'::text) ->> 'Province'::text) AS province,
            ((t2_1.data -> 'Component'::text) ->> 'City'::text) AS city,
            ((t2_1.data -> 'Component'::text) ->> 'District'::text) AS district
           FROM (( SELECT data_coll.sender_id,
                    data_coll.sender_name,
                    max(data_coll.create_time) AS create_time
                   FROM data_coll
                  WHERE ((data_coll.plan_id)::text = '119510508821807104'::text)
                  GROUP BY data_coll.sender_id, data_coll.sender_name) t1_1
             LEFT JOIN data_coll t2_1 ON ((((t1_1.sender_id)::text = (t2_1.sender_id)::text) AND ((t1_1.sender_name)::text = (t2_1.sender_name)::text) AND (t1_1.create_time = t2_1.create_time))))
          WHERE ((t2_1.plan_id)::text = '119510508821807104'::text)) t2 ON ((((t1.sender_id)::text = (t2.sender_id)::text) AND ((t1.sender_name)::text = (t2.sender_name)::text))))
     LEFT JOIN ( SELECT t1_1.sender_id,
            t1_1.sender_name,
            t1_1.last_time,
            t2_1.version
           FROM (( SELECT data_coll.sender_id,
                    data_coll.sender_name,
                    max(data_coll.create_time) AS last_time
                   FROM data_coll
                  WHERE (((data_coll.plan_id)::text = '180629271172222977'::text) AND ((data_coll.data ->> 'version'::text) IS NOT NULL))
                  GROUP BY data_coll.sender_id, data_coll.sender_name) t1_1
             LEFT JOIN ( SELECT data_coll.sender_id,
                    data_coll.sender_name,
                    data_coll.create_time,
                    (data_coll.data ->> 'version'::text) AS version
                   FROM data_coll
                  WHERE ((data_coll.plan_id)::text = '180629271172222977'::text)) t2_1 ON ((((t1_1.sender_id)::text = (t2_1.sender_id)::text) AND ((t1_1.sender_name)::text = (t2_1.sender_name)::text) AND (t1_1.last_time = t2_1.create_time))))) t3 ON ((((t1.sender_id)::text = (t3.sender_id)::text) AND ((t1.sender_name)::text = (t3.sender_name)::text))))
  ORDER BY t1.last_time DESC, t1.total DESC
	;
