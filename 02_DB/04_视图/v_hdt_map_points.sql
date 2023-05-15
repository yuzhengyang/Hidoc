CREATE VIEW "public"."v_hdt_map_points" AS  SELECT DISTINCT data_coll.sender_id,
    data_coll.sender_name,
    to_char(data_coll.create_time, 'yyyy-mm-dd'::text) AS create_time,
    ((data_coll.data -> 'Component'::text) ->> 'Province'::text) AS province,
    ((data_coll.data -> 'Component'::text) ->> 'City'::text) AS city,
    ((data_coll.data -> 'Component'::text) ->> 'District'::text) AS district,
    ((data_coll.data -> 'Component'::text) ->> 'Street'::text) AS street,
    (data_coll.data -> 'Latitude'::text) AS latitude,
    (data_coll.data -> 'Longitude'::text) AS longitude
   FROM data_coll
  WHERE ((data_coll.plan_id)::text = '119510508821807104'::text)
  ORDER BY data_coll.sender_id, (to_char(data_coll.create_time, 'yyyy-mm-dd'::text));