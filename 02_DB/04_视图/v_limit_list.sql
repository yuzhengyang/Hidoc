CREATE VIEW "public"."v_limit_list" AS  SELECT limit_access_log.address,
    limit_access_log.version,
    limit_access_log.account,
    limit_access_log.machine,
    limit_access_log.message,
    max(limit_access_log.create_time) AS last_time
   FROM limit_access_log
  WHERE (((limit_access_log.account)::text <> ''::text) AND ((limit_access_log.account)::text <> '<>'::text))
  GROUP BY limit_access_log.address, limit_access_log.version, limit_access_log.account, limit_access_log.machine, limit_access_log.message
  ORDER BY (max(limit_access_log.create_time)) DESC;