# Hidoc Online Documentation

## Introduction

Hidoc is a lightweight and simple **online knowledge document sharing system**, suitable for teams to share documents.

## Version Description
Since it is an independent project with a high degree of freedom, the versions of components will be updated and upgraded significantly at irregular intervals.

After the key components are upgraded, development will continue on the main branch for further research.

Of course, for the old versions without component upgrades, branches will be created for backup, and the backup information will be continuously recorded.

### Branch Information
| Creation Date | Branch | Status | Technical Specifications |
|------------|----------------------------------|------------|------------|
| - | main | In progress | JDK17 + SPRING 3.0 + PostgreSQL13^ |
| 2023-11-13 | 2.0.0 | Completed | JDK17 + SPRING 3.0 + PostgreSQL13^  |
| 2023-09-25 | 1.0.0 | Completed | JDK8 + SPRING 2.7.2 + PostgreSQL13^ |

## Features

### Quick Writing
1. Create documents using standard `Markdown`.
2. Support `defining templates` and applying `template` content.
3. Support `referencing documents` and inserting `associated document links` to build a `knowledge network diagram`.
4. Support quick document copying.
5. Support automatic uploading of copied and pasted images.
6. Support inserting `code` with `code highlighting` and `line highlighting` for multiple languages.
7. Support `mermaid` diagrams, enabling custom diagrams such as `flowcharts` and `mind maps`.
8. Record all modification records and retain `history`.

### Collaborative Processing
1. Open `collaboration` processing, allowing `inviting members` to jointly maintain.
2. Collaboration provides a `lock` mechanism to avoid editing conflicts.

### Permission Management
1. Rich permission management capabilities to ensure security.
2. Registered users support `restricting corporate email addresses`.
3. The front end supports `host` filtering, allowing certain entry `hosts` to be restricted to `require login for use`.
4. Support setting the `open` and `private` status of document collections.
5. Support setting document collections to `require login to view`.
6. Support inviting `collaborating members` to edit document collections.

### Comfortable Reading
1. Provide a `table of contents` for an overview of all documents.
2. Provide an `outline` for quickly locating specific `chapters`.
3. Support `full-text search and filtering` within document collections for convenient querying.
4. Display in `large font` for comfortable reading.
5. Provide editing information display for intuitive viewing of editors and editing times.
6. Support `reading statistics` to understand document popularity.

### Various Interactions
1. Support `comments` and `replies` for easy Q&A.
2. The workspace provides a `comment summary` to avoid missing information.
3. Support `liking` to enhance the author's `happiness`.

### Online Cloud Storage
1. Provide a `simplified` `cloud storage` service for uploading and downloading files.
2. Download by username as the `url`, which is unique.
3. Support `multi-version management` of files with the same name, and the same link automatically uses the `latest version`.
4. Precise `file quota` control for controllable user space usage.
5. Provide file `deletion` and `recovery from the recycle bin`, supporting file recovery within `180 days`.
6. Files support `fingerprint` extraction, and files with the same fingerprint do not occupy additional space.
7. Fixed download links can support some simple `software update` capabilities.

### Code Documentation
1. Support documenting `code comments` for easy `retrieval`, viewing, and display.
2. Support batch uploading of packaged code and `structured` analysis and storage.
3. Support rich `blocking` of comments, allowing settings for descriptions, scenarios, directories, restrictions, examples, modification records, etc.
4. Support multiple `views` for viewing code, such as search mode, directory mode, and package mode.
5. Visually display code and comment information on the page.

### Data Collection
1. Support custom `data collection plans`.
2. Support `receiving data` information through interfaces.
3. Support setting the `start and end times` for data collection.

### Service Management
1. Support configuring `SSH` connections to `Linux` servers.
2. Support `SSH` remote calls and viewing `execution results`.

### Rule Restrictions
1. Support setting `restrictions` based on rule matching.
2. Support lightweight `authorization restriction` information for client software.

### Multiple Languages
1. Support extended settings for multiple languages.
2. Languages can be dynamically switched during use.

### Flexible Deployment
1. Front-end and back-end separation, allowing `flexible deployment` as needed.
2. The front end can be deployed multiple times to support `different login requirements`.
3. The back end starts with a separate `jar` package, with `one-click startup`, which is extremely convenient.
4. The database uses the powerful `PostgreSQL`, with excellent performance and easy support for `hundreds of millions` of data.
5. Files are stored in a specified `directory` for easy `migration` and `backup`.
6. Support deployment in various network environments to suit different users and protect data security.

### Continuous Improvement
1. The product is updated irregularly and continuously improved.

### Let Yourself Go~
1. Add various interesting features irregularly.
2. Online Cloud Storage: File sharing and downloading (see the above description).
3. Code Documentation: Building code comments (see the above description).
4. Data Collection: Data collection and aggregation (see the above description).
5. Service Management: SSH calls for services (see the above description).
6. Rule Restrictions: Rule matching interfaces (see the above description).

## Scenarios

### Document Organization and Construction
Structured document organization is required for easy management, classification, viewing, and reading.

### Team Knowledge Inheritance
With a large number of team members, it is suitable for knowledge summarization and precipitation, which is beneficial to the long-term development of the team.

### Standardized Document Manuals
Summarize standardized documents and introductory manuals for new employees to reduce training costs and accelerate the integration of members.

### Knowledge Tutorials
After researching some technologies, organize documents for everyone to learn and make progress together.

### File Sharing and Downloading
Manage public files and share some software or documents.

### Code Documentation Construction
There is a need for code documentation construction, requiring structured viewing and retrieval.

### Data Collection and Aggregation
There is a need for collecting random and discrete data.

### Service SSH Calls
Linux servers are used in the team, and some SSH scripts and automatic processing are allowed.

## Demonstration Address

[Demonstration Address](https://www.yuzhyn.top/)
Account and password: duanyu / duanyu

## Technical Features

Developed based on Java SpringBoot, it can support various scalable development based on the Spring family of technologies.

It is very suitable for developers using the current VUE and SpringBoot technology stack for use and expansion, with high playability.

> Note: Heavyweight middleware will not be introduced in the current construction concept.

### Front-end Technologies
- VUE3 front-end framework
- [ElementUI Plus](https://element-plus.gitee.io/#/zh-CN) front-end UI framework
- [v-md-editor](https://ckang1229.gitee.io/vue-markdown-editor/zh/) Markdown editor

### Back-end Technologies
- SpringBoot
- Mybatis & MybatisPlus
- ehcache in-memory cache
- lombok
- fastjson JSON processing
- druid database connection pool
- javaparser Java code parser
- jsoup HTML text parser
- javax.mail email sending
- emoji-java emoji expression processing

### Database Technology
- PostgreSQL

## Release and Deployment

### Database

Install PostgreSQL 13.

Note: Due to the use of JSON types and other features of the PG database, only the PG database is currently supported.

After installing the database, you need to configure the yml configuration file of the Java service and modify the database connection information.

### Front-end WEB

First, modify the address for the front end to access the back end. In the config.js file in the front-end directory, you need to modify the IP address and port number.

The configuration content is as follows:

```javascript
var config = {
    name: 'yuzhengyang',
    baseServer: 'http:///101.132.159.3:24001/',
    imageServer: 'http://101.132.159.3:24001/f/d/u/'
};
```

The front end is deployed and started using nginx.

The configuration is as follows:

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

### Back-end JAVA Service

The back end is started directly using the packaged Jar package.

Startup command:

```bash
nohup java -Dfile.encoding=utf-8 -jar hidoc-app-0.0.1-SNAPSHOT.jar --spring.config.location=application.yml> ./nohup_output &
```

## Agreement

Please comply with the GPL-3.0 license agreement.

Users of the open-source version must retain the copyright identification related to "Hidoc Documentation" and are prohibited from modifying or deleting the copyright identification related to "Hidoc Documentation".

If violated, the developers reserve the right to hold the infringer accountable.

## About the Author

Email：[yuzhyn@163.com](mailto:yuzhyn@163.com)