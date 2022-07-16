INSERT INTO "app_conf" (item, key, value, ps, index) VALUES ('notice-email', 'address', 'hi_notice@163.com', '必填，通知邮箱地址', 1)
ON CONFLICT ("item","key") DO NOTHING;

INSERT INTO "app_conf" (item, key, value, ps, index) VALUES ('notice-email', 'personal', 'Hidoc-通知邮箱', '必填，通知邮箱名称', 2)
ON CONFLICT ("item","key") DO NOTHING;

INSERT INTO "app_conf" (item, key, value, ps, index) VALUES ('notice-email', 'password', '*', '必填，通知邮箱密码', 3)
ON CONFLICT ("item","key") DO NOTHING;

INSERT INTO "app_conf" (item, key, value, ps, index) VALUES ('notice-email', 'port', '994', '必填，端口号', 4)
ON CONFLICT ("item","key") DO NOTHING;

INSERT INTO "app_conf" (item, key, value, ps, index) VALUES ('notice-email', 'is-ssl', 'true', '必填，启用ssl', 5)
ON CONFLICT ("item","key") DO NOTHING;

INSERT INTO "app_conf" (item, key, value, ps, index) VALUES ('notice-email', 'smtp-host', 'smtp.163.com', '必填，smtp地址', 6)
ON CONFLICT ("item","key") DO NOTHING;

INSERT INTO "app_conf" (item, key, value, ps, index) VALUES ('register', 'limit-email', '163.com', '可选，注册限制邮箱后缀', 7)
ON CONFLICT ("item","key") DO NOTHING;


