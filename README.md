# Hidoc 在线文档
## 简介

Hidoc 是基于 SpringBoot 和 VUE3 技术开发的在线文档，简单方便，适合团队管理和分享文档。



## 特点

基于 Java SpringBoot 开发，可基于 Spring 全家桶的技术上，做各种伸缩扩展的支持。



## 技术

后端：

SpringBoot

Mybatis & MybatisPlus

前端：

VUE3

[ElementUI Plus](https://element-plus.gitee.io/#/zh-CN)

[v-md-editor](https://ckang1229.gitee.io/vue-markdown-editor/zh/)

数据库：

PostgreSQL



## 功能

文集管理

文集协作

文章管理

文章阅读

文档附件

文件管理

持续扩展中......



## 演示

[演示地址](http://101.132.159.3:8080/)

账号密码：duanyu / duanyu



## 发布部署

### 前端WEB

首先修改前端访问后端的地址，在前端目录下 config.js 文件，需修改ip地址和端口号

配置内容如下：

```javascript
var config = {
    name: 'yuzhengyang',
    baseServer: 'http:///101.132.159.3:24001/',
    imageServer: 'http://101.132.159.3:24001/f/d/u/'
};
```



前端使用nginx部署启动

配置如下：

```nginx
user   root;
worker_processes  1;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;

    server {
        listen          8080;
        server_name     localhost;
        location / {
            root /home/app/hidoc/hidoc-web;
            try_files $uri $uri/ /index.html;
        }
    }
}

```





### 后端JAVA服务

后端使用打包的Jar包直接启动

启动命令：

```bash
nohup java -Dfile.encoding=utf-8 -jar hidoc-app-0.0.1-SNAPSHOT.jar --spring.config.location=application.yml> ./nohup_output &

```





### 数据库

建议安装 PostgreSQL 13

注意：由于使用了PG数据库的JSON类型等特性，目前仅支持PG数据库



安装数据库后，需要配置Java服务的yml配置文件，修改数据库连接信息



## 关于作者

- Email：[yuzhyn@163.com](mailto:yuzhyn@163.com)



## 赞助

如果对您有所帮助，您可以赞赏支持哟

![赞赏码](https://raw.githubusercontent.com/yuzhengyang/Fork/master/Documents/QRCode/WeiXinQRCodeMini.jpg)