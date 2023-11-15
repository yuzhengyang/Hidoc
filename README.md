# Hidoc 在线文档

## 简介

Hidoc 是一款轻量简单的 **在线知识文档分享系统**，适合团队分享文档。


## 版本说明
由于是独立项目，自由度极高，所以组件的版本会不定期大步更新升级

关键组件版本升级后，仍然会在主干分支进行开发，继续研究~

当然，没有升级组件的老版本，会创建分支来备份，备份信息会持续记录哒~

### 分支信息
| 创建日期 | 分支                            | 状态                        | 使用技术说明 |
|------------|----------------------------------|------------|------------|
| - | main | 进行中 | JDK17 + SPRING 3.0 + PostgreSQL13^ |
| 2023-11-13 | 2.0.0 | 完成 | JDK17 + SPRING 3.0 + PostgreSQL13^  |
| 2023-09-25 | 1.0.0 | 完成 | JDK8 + SPRING 2.7.2 + PostgreSQL13^ |


## 特点

### 快速编写
1. 使用标准 `Markdown` 创建文档
2. 支持 `定义模板` 并应用 `模板` 内容
3. 支持 `引用文档` 插入 `关联文档链接`，来构建 `知识网络图`
4. 支持快速复制文档
5. 支持复制粘贴图片自动上传
6. 支持插入 `代码`，并支持多种语言的 `代码高亮`，`行高亮`
7. 支持 `mermaid` 图，可实现 `流程图`、`脑图` 等自定义图示
8. 记录所有的修改记录，保留 `历史`

### 协作处理
1. 开放的 `协作` 处理，可 `邀请成员` 共同维护
2. 协作提供 `锁` 机制，避免编辑冲突

### 权限管理
1. 丰富的权限管理能力，保障安全
2. 注册用户支持 `限制企业邮箱`
3. 前端支持 `host` 过滤，可限制某些入口 `host` 必须 `登录使用`
4. 支持文集 `开放` 和 `私有` 状态设置
5. 支持文集设置 `需登录查看`
6. 支持文集邀请 `协作成员` 编辑

### 舒适阅读
1. 提供 `目录栏`，对所有文档一览无余
2. 提供 `大纲栏`，可快速定位指定 `章节`
3. 文集内支持 `全文搜索筛选`，方便查询
4. `大字体` 展示，利于舒适的阅读
5. 提供编辑信息展示，直观查看编辑者，编辑时间信息
6. 支持 `阅读统计`，了解文档热度

### 多种互动
1. 支持 `评论` 和 `回复`，方便简单的问答
2. 工作台提供 `评论汇总`，不漏信息
3. 支持 `点赞`，提升作者 `幸福感`

### 在线网盘
1. 提供 `精简` 的 `网盘` 服务，可上传下载文件
2. 按用户名作为 `url` 下载，独一无二
3. 同名文件支持 `多版本管理`，相同链接自动使用 `最新版本`
4. 精确的 `文件配额` 控制，用户使用的 `空间可控`
5. 提供文件 `删除` 和回收站 `恢复`，支持 `180天` 的文件恢复
6. 文件支持 `指纹` 提取，相同指纹的文件 `不重复占用` 空间
7. 固定下载链接，可以支撑一些简单的 `软件更新` 能力

### 代码文档
1. 支持 `代码注释` 文档化，便于 `检索` 查看与展示
2. 支持打包的代码 `批量` 上传并 `结构化` 分析存储
3. 丰富的注释 `分块` 支持，可设置描述，场景，目录，限制，示例，修改记录等
4. 支持多种 `视图` 查看代码，如搜索模式，目录模式，包模式
5. 页面 `直观展示` 代码及注释信息

### 数据收集
1. 支持自定义的 `数据收集计划`
2. 支持 `接口接受数据` 信息
3. 支持数据收集的 `起止时间`

### 服务管理
1. 支持配置 `SSH` 连接 `Linux` 服务器
2. 支持 `SSH` 远程调用并查看 `执行结果`

### 规则限制
1. 支持规则匹配 `限制` 设置
2. 支持轻量的客户端软件的 `授权限制` 信息

### 灵活部署
1. 前后端分离，可根据需要 `灵活部署`
2. 前端可多部署，支持 `不同登录需求` 的要求
3. 后端使用单独 `jar` 包启动，`一键启动`，极为方便
4. 数据库使用超强 `PostgreSQL`，性能极佳，轻松支持 `亿级` 数据
5. 文件指定 `目录存储`，利于 `迁移` 与 `备份`
6. 支持各种网络环境部署，适用不同用户，保护数据安全

### 持续完善
1. 产品不定期更新，持续完善

### 放飞自我~
1. 不定期增加各种有趣儿的功能
2. 在线网盘：文件分享下载（见上文描述）
3. 代码文档：代码注释构建（见上文描述）
4. 数据收集：数据收集汇总（见上文描述）
5. 服务管理：服务SSH调用（见上文描述）
6. 规则限制：限制匹配接口（见上文描述）


## 场景

### 文档整理构建
需要结构化整理文档，便于管理和分类，易于查看和阅读

### 团队知识传承
团队人员多，适合知识总结和沉淀，利于团队长期发展

### 规范文档手册
为新人总结规范文档和入门手册，便于降低培训成本，加快成员融入时间

### 知识教程说明
对一些技术做研究后，整理文档，便于大家学习，共同进步

### 文件分享下载
对一些公共文件进行管理，需要共享一些软件或文档

### 代码文档构建
有代码文档构建需求，需要结构化查看和检索

### 数据收集汇总
有随机离散数据收集需要

### 服务SSH调用
团队中使用Linux服务器，并允许一些SSH脚本和自动处理




## 演示地址

[演示地址](https://www.yuzhyn.top/)
账号密码：duanyu / duanyu




## 技术特点

基于 Java SpringBoot 开发，可基于 Spring 全家桶的技术上，做各种伸缩扩展的开发支持。

非常适合当下 VUE 和 SpringBoot 技术栈的开发者使用和扩展，可玩性极高。

> 注意：当前建设思路中不会引用重量级的中间件


### 前端技术
- VUE3 前端框架
- [ElementUI Plus](https://element-plus.gitee.io/#/zh-CN) 前端UI框架
- [v-md-editor](https://ckang1229.gitee.io/vue-markdown-editor/zh/) Markdown编辑器



### 后端技术
- SpringBoot
- Mybatis & MybatisPlus
- ehcache 内存缓存
- lombok
- fastjson JSON处理
- druid 数据库连接池
- javaparser Java代码解析器
- jsoup Html文本解析器
- javax.mail 邮件发送
- emoji-java emoji表情处理



### 数据库技术
- PostgreSQL






## 发布部署

### 数据库

安装 PostgreSQL 13

注意：由于使用了PG数据库的JSON类型等特性，目前仅支持PG数据库

安装数据库后，需要配置Java服务的yml配置文件，修改数据库连接信息



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


## 协议

请遵守 GPL-3.0 license 协议

开源版的使用者必须保留 `Hidoc文档` 相关版权标识，禁止对 `Hidoc文档` 相关版权标识进行修改和删除。

如果违反，开发者保留对侵权者追究责任的权利。




## 关于作者

Email：[yuzhyn@163.com](mailto:yuzhyn@163.com)
