INSERT INTO "sys_user"("id", "avatar", "name", "real_name", "email", "create_time", "update_time", "password", "is_frozen", "online_time", "vip_level", "login_time", "roles") VALUES ('1', '$system$49.jpg', 'sa', '超级管理员', 'yuzhyn@163.com', '2021-02-04 22:03:00', NULL, '0', 'f', '2022-07-01 14:26:00.42', NULL, '2022-11-29 23:02:24.552', '["sa", "admin"]')
ON CONFLICT ("id") DO NOTHING;







-- INSERT INTO "public"."sys_user"("id", "avatar", "name", "real_name", "email", "create_time", "update_time", "password", "is_frozen", "online_time", "vip_level", "login_time", "roles") VALUES ('1', '$system$49.jpg', 'sa', '超级管理员', 'yuzhyn@163.com', '2021-02-04 22:03:00', NULL, 'd058a10f0cbdf5c80c475307a0d7eb68f144d741c24656a9ab3d4048dc559181', 'f', '2022-07-01 14:26:00.42', NULL, '2022-11-29 23:02:24.552', '["sa", "admin"]');



-- INSERT INTO "public"."sys_user_file_conf"("user_id", "create_time", "expiry_time", "space_limit", "used_space", "url_prefix") VALUES ('1', '2021-02-04 22:03:26', '2099-02-04 22:03:29', 1073741824, 0, 'sa');


-- INSERT INTO "public"."sys_user"("id", "avatar", "name", "real_name", "email", "create_time", "update_time", "password", "is_frozen", "online_time", "vip_level", "login_time", "roles") VALUES ('60678923813388288', '$system$72.jpg', 'paidaxing', '派大星', 'paidaxing@email.com', '2021-06-17 10:36:23', NULL, '22f774fbdc8846dd7288e82250364e27bceab0ddaf0622178ad58984b71d4d5d', 't', NULL, NULL, NULL, NULL);
