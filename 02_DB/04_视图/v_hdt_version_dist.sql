CREATE OR REPLACE VIEW "v_hdt_version_dist" AS  SELECT COALESCE(v_hdt_use_desc.version, '0.0.0.0'::text) AS version,
    count(1) AS client_count
   FROM v_hdt_use_desc
  GROUP BY v_hdt_use_desc.version
  ORDER BY COALESCE(v_hdt_use_desc.version, '0.0.0.0'::text) DESC
	;
