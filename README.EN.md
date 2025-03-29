# Hidoc Online Documentation

## Introduction

Hidoc is a lightweight and simple **online knowledge documentation sharing system**, suitable for team document sharing.

Chinese documentation please refer to: [README.md](README.md)
English documentation please refer to: [README.EN.md](README.EN.md)

## Version Information

As an independent project with high flexibility, the component versions will be updated and upgraded periodically.

After upgrading key components, development will continue on the main branch for further research.

Older versions without component upgrades will be backed up in branches, and backup information will be continuously recorded.

### Branch Information
| Creation Date | Branch                         | Status     | Technical Details |
|---------------|--------------------------------|------------|-------------------|
| -             | main                           | In Progress | JDK21 + SPRING 3.0 + PostgreSQL13^ |
| 2023-11-13    | 2.0.0                          | Completed  | JDK17 + SPRING 3.0 + PostgreSQL13^ |
| 2023-09-25    | 1.0.0                          | Completed  | JDK8 + SPRING 2.7.2 + PostgreSQL13^ |

## Features

### Quick Writing
1. Create documents using standard `Markdown`.
2. Support for `template definition` and applying `template` content.
3. Support for `referencing documents` to insert `related document links` and build a `knowledge graph`.
4. Quickly duplicate documents.
5. Copy-paste images for automatic upload.
6. Insert `code` with support for multiple languages, `syntax highlighting`, and `line highlighting`.
7. Support for `mermaid` diagrams, enabling `flowcharts`, `mind maps`, and other custom illustrations.
8. Record all modification history and retain `version history`.

### Collaboration
1. Open `collaboration` features, allowing `team members` to co-maintain documents.
2. Collaboration includes a `locking` mechanism to avoid editing conflicts.

### Permission Management
1. Rich permission management capabilities to ensure security.
2. Registration supports `enterprise email restrictions`.
3. Frontend supports `host` filtering to restrict certain entry `hosts` to `login-only` usage.
4. Document collections can be set to `public` or `private`.
5. Document collections can require `login to view`.
6. Invite `collaborators` to edit document collections.

### Comfortable Reading
1. Provide a `table of contents` for an overview of all documents.
2. Provide an `outline panel` for quick navigation to specific `sections`.
3. Support `full-text search` within document collections for easy querying.
4. `Large font` display for comfortable reading.
5. Display editing information, showing editors and edit timestamps.
6. Support for `reading statistics` to understand document popularity.

### Interactive Features
1. Support for `comments` and `replies` for simple Q&A.
2. A `comment summary` in the workspace ensures no information is missed.
3. Support for `likes` to enhance author `satisfaction`.

### Online Cloud Storage
1. Provide a `streamlined` `cloud storage` service for uploading and downloading files.
2. Unique `URL` for downloads based on username.
3. Support for `multi-version management` of files with the same name, automatically using the `latest version`.
4. Precise `file quota` control to manage user storage usage.
5. Support for file `deletion` and `recovery` from the recycle bin for up to `180 days`.
6. Extract file `fingerprints` to avoid duplicate storage for files with the same fingerprint.
7. Fixed download links to support simple `software updates`.

### Code Documentation
1. Support for `code comments` documentation for easy `searching`, viewing, and display.
2. Support for bulk uploading and `structural analysis` of packaged code.
3. Rich comment `segmentation` support, including descriptions, scenarios, directories, constraints, examples, and modification records.
4. Support for multiple `views` to browse code, such as search mode, directory mode, and package mode.
5. Intuitive display of code and comment information on the page.

### Data Collection
1. Support for custom `data collection plans`.
2. Support for `API-based data` reception.
3. Support for setting `start and end times` for data collection.

### Service Management
1. Support for configuring `SSH` connections to `Linux` servers.
2. Support for `SSH` remote execution and viewing of `execution results`.

### Rule Restrictions
1. Support for rule-based `restriction` settings.
2. Support for lightweight client software `authorization restrictions`.

### Multi-Language Support
1. Support for multi-language extension settings.
2. Dynamic language switching during usage.

### Flexible Deployment
1. Frontend and backend separation for `flexible deployment` as needed.
2. Multiple frontend deployments to support `different login requirements`.
3. Backend runs as a standalone `jar` package with `one-click startup` for convenience.
4. Database uses the powerful `PostgreSQL`, offering excellent performance and supporting `billion-level` data.
5. Files are stored in a specified `directory`, facilitating `migration` and `backup`.
6. Suitable for deployment in various network environments, ensuring data security for different users.

### Continuous Improvement
1. The product is updated periodically for continuous improvement.

### Creative Additions
1. Periodically add interesting features.
2. Online cloud storage: File sharing and downloading (see above description).
3. Code documentation: Code comment construction (see above description).
4. Data collection: Data collection and summary (see above description).
5. Service management: SSH service invocation (see above description).
6. Rule restrictions: Restriction matching interface (see above description).

## Use Cases

### Document Organization
Structure and organize documents for easy management, classification, and readability.

### Team Knowledge Sharing
Ideal for summarizing and preserving team knowledge, fostering long-term team development.

### Standardized Manuals
Create standardized manuals and onboarding guides for new members to reduce training costs and accelerate integration.

### Knowledge Tutorials
Document technical research for team learning and collective progress.

### File Sharing
Manage public files and share software or documents.

### Code Documentation
Build structured and searchable code documentation.

### Data Collection
Collect and summarize random and discrete data.

### SSH Service Invocation
Use Linux servers within the team and allow SSH scripts and automated processing.

## Demo

[Demo Link](https://hidoc.maozi.io/)  
Account/Password: duanyu / duanyu

## Technical Features

Developed with Java SpringBoot, it supports scalable development based on the Spring ecosystem.

Highly suitable for developers using the VUE and SpringBoot technology stack, offering great flexibility and extensibility.

> Note: The current development approach avoids using heavyweight middleware.

### Frontend Technology
- VUE3 frontend framework
- [ElementUI Plus](https://element-plus.gitee.io/#/en-US) frontend UI framework
- [v-md-editor](https://ckang1229.gitee.io/vue-markdown-editor/en/) Markdown editor

### Backend Technology
- SpringBoot
- Mybatis & MybatisPlus
- ehcache in-memory caching
- lombok
- fastjson for JSON processing
- druid database connection pool
- javaparser for Java code parsing
- jsoup for HTML text parsing
- javax.mail for email sending
- emoji-java for emoji processing

### Database Technology
- PostgreSQL

## Deployment

### Database

Install PostgreSQL 13.

Note: Due to the use of JSON types and other features of PostgreSQL, only PostgreSQL is supported.

After installation, configure the database connection information in the Java service's yml configuration file.

### Frontend Web

First, modify the backend address in the frontend directory's `config.js` file by updating the IP address and port.

Configuration example:

```javascript
var config = {
    name: 'yuzhengyang',
    baseServer: 'http://101.132.159.3:24001/',
    imageServer: 'http://101.132.159.3:24001/f/d/u/'
};
```

Deploy the frontend using nginx with the following configuration:

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

### Backend Java Service

Run the backend as a packaged Jar file with the following command:

```bash
nohup java -Dfile.encoding=utf-8 -jar hidoc-app-0.0.1-SNAPSHOT.jar --spring.config.location=application.yml > ./nohup_output &
```

## License

Please comply with the GPL-3.0 license.

Users of the open-source version must retain the `Hidoc Documentation` copyright notice and are prohibited from modifying or removing it.

Violation of this rule may result in legal action by the developer.

## About the Author

Email: [yuzhyn@163.com](mailto:yuzhyn@163.com)
