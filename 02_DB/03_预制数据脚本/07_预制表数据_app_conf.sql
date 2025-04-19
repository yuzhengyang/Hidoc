
INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('app', 'name', 'HIDOC', '必填，应用名称', 1) ON CONFLICT ("item","key") DO NOTHING;
INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('app', 'name-cn', 'HIDOC文档', '必填，应用中文名称', 2) ON CONFLICT ("item","key") DO NOTHING;



INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('elastic', 'enable', '1', '必填，是否启用elastic，启用数据收集和日志收集能力', 1) ON CONFLICT ("item","key") DO NOTHING;
INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('elastic', 'hosts', 'https://www.youaddress.com:9999', '可选，服务地址', 2) ON CONFLICT ("item","key") DO NOTHING;
INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('elastic', 'username', 'elastic', '可选，账号', 3) ON CONFLICT ("item","key") DO NOTHING;
INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('elastic', 'password', 'your-password', '可选，密码', 4) ON CONFLICT ("item","key") DO NOTHING;
INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('elastic', 'index-prefix', 'hidoc.', '可选，索引前缀，默认为：hidoc.', 5) ON CONFLICT ("item","key") DO NOTHING;


INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('function', 'display', 'doc,file,code,ssh', '必填，WEB页面左上角显示哪些功能，默认为：doc,file,code,ssh', 1) ON CONFLICT ("item","key") DO NOTHING;


INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('notice-email', 'address', 'hi_notice@163.com', '必填，通知邮箱地址', 1) ON CONFLICT ("item","key") DO NOTHING;
INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('notice-email', 'personal', 'Hidoc-通知邮箱', '必填，通知邮箱名称', 2) ON CONFLICT ("item","key") DO NOTHING;
INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('notice-email', 'password', 'your-password', '必填，通知邮箱密码', 3) ON CONFLICT ("item","key") DO NOTHING;
INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('notice-email', 'port', '465', '必填，端口号', 4) ON CONFLICT ("item","key") DO NOTHING;
INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('notice-email', 'is-ssl', 'true', '必填，启用ssl', 5) ON CONFLICT ("item","key") DO NOTHING;
INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('notice-email', 'smtp-host', 'smtp.163.com', '必填，smtp地址', 6) ON CONFLICT ("item","key") DO NOTHING;


INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('register', 'limit-email', '163.com', '可选，注册限制邮箱后缀', 1) ON CONFLICT ("item","key") DO NOTHING;

-- INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('access-anonymous', 'enable', '1', '可选，启用匿名访问', 1) ON CONFLICT ("item","key") DO NOTHING;
-- INSERT INTO "app_conf" ("item", "key", "value", "ps", "index") VALUES ('access-anonymous', 'ip-port', '[192.168.14.155]', '可选，允许匿名访问的入口，如[IP:PORT][IP:PORT]', 2) ON CONFLICT ("item","key") DO NOTHING;
