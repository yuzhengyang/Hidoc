/*
 Navicat Premium Data Transfer

 Source Server         : 【本地数据库】
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : hidoc

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 13/04/2021 22:44:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for doc
-- ----------------------------
DROP TABLE IF EXISTS `doc`;
CREATE TABLE `doc`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `collected_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文档集id',
  `doc_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文档类型',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `content_length` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容长度',
  `content_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容类型',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `serial_number` int(11) NULL DEFAULT NULL COMMENT '排序序号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `create_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户id',
  `update_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户id',
  `lock_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '锁定用户id',
  `owner_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属用户id',
  `is_delete` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文档信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doc
-- ----------------------------
INSERT INTO `doc` VALUES ('27155370212327424', '24789152289521664', '', '基础配置', '<h1 align=\"center\">Markdown Editor built on Vue</h1>\n\n<p align=\"center\">\n  <a href=\"https://npmcharts.com/compare/@kangc/v-md-editor?minimal=true\"><img src=\"https://img.shields.io/npm/dm/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Downloads\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/v/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Version\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/l/@kangc/v-md-editor.svg?sanitize=true\" alt=\"License\"></a>\n</p>\n\n## Links\n\n- [Demo](https://code-farmer-i.github.io/vue-markdown-editor/examples/base-editor.html)\n- [Documentation](https://code-farmer-i.github.io/vue-markdown-editor/)\n- [Changelog](https://code-farmer-i.github.io/vue-markdown-editor/changelog.html)\n\n## Install\n\n```bash\n# use npm\nnpm i @kangc/v-md-editor -S\n\n# use yarn\nyarn add @kangc/v-md-editor\n```\n\n## Quick Start\n\n```js\nimport Vue from \'vue\';\nimport VueMarkdownEditor from \'@kangc/v-md-editor\';\nimport \'@kangc/v-md-editor/lib/style/base-editor.css\';\nimport vuepressTheme from \'@kangc/v-md-editor/lib/theme/vuepress.js\';\n\nVueMarkdownEditor.use(vuepressTheme);\n\nVue.use(VueMarkdownEditor);\n```\n\n## Usage\n\n```html\n<template>\n  <v-md-editor v-model=\"text\" height=\"400px\"></v-md-editor>\n</template>\n\n<script>\n  export default {\n    data() {\n      return {\n        text: \'\',\n      };\n    },\n  };\n</script>\n```\n\n## Refrence\n\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n', '1620', '', 'tag', 140, '2021-03-26 02:25:47', '2021-03-26 11:09:16', '1', '1', '21580294339428352', '1', 0);
INSERT INTO `doc` VALUES ('27780460402704384', '24789152289521664', '', '附件使用方法', '> 邮箱地址\n\n``` \napp.host = 10.10.10.10\n```', '39', '', 'tag', 150, '2021-03-18 15:49:38', '2021-03-18 15:49:38', '1', NULL, '21580294339428352', '1', 0);
INSERT INTO `doc` VALUES ('27796240007692288', '24789152289521664', '', '邮箱密码定期修改要求', '|序号|规则|描述|\n|-|-|-|\n|1|数字|必须包含数字|', '32', '', 'tag', 130, '2021-03-18 16:52:20', '2021-03-26 11:09:50', '1', '1', '', '1', 0);
INSERT INTO `doc` VALUES ('27803470044069888', '24789152289521664', '', '测试文档001', '001', '3', '', 'tag', 170, '2021-03-18 17:21:04', '2021-03-18 17:21:04', '1', NULL, '21580294339428352', '1', 0);
INSERT INTO `doc` VALUES ('27803539271057408', '24789152289521664', '', '测试文档002', '002', '3', '', 'tag', 180, '2021-03-18 17:21:20', '2021-03-18 17:21:20', '1', NULL, '', '1', 0);
INSERT INTO `doc` VALUES ('27891184378576896', '24789152289521664', '', '测试文档005', '005', '3', '', 'tag', 0, '2021-03-18 23:09:37', '2021-03-18 23:09:37', '1', NULL, '', '1', 1);
INSERT INTO `doc` VALUES ('29503289401802752', '24789152289521664', '', '目录导航测试001', '# 目录 1\n## 目录 1.1\n### 目录 1.1.1\n#### 目录 1.1.1.1\n#### 目录 1.1.1.2\n## 目录 1.2\n### 目录 1.2.1', '84', '', 'tag', 160, '2021-03-23 09:55:32', '2021-03-23 09:55:32', '1', NULL, '', '1', 0);
INSERT INTO `doc` VALUES ('29887416907071488', '24789152289521664', '', '张三的歌', '# 我要带你到处去飞翔\n走遍世界各地去观赏\n没有烦恼没有那悲伤\n自由自在身心多开朗\n忘掉痛苦忘掉那地方\n我们一起启程去流浪\n虽然没有华厦美衣裳\n但是心里充满着希望\n我们要飞到那遥远地方\n看一看这世界并非那么凄凉\n我们要飞到那遥远地方\n望一望 这世界还是一片的光亮\n忘掉痛苦忘掉那地方\n# 我们一起启程去流浪\n虽然没有华厦美衣裳\n但是心里充满着希望\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉', '351', '', 'tag', 110, '2021-03-24 11:21:55', '2021-03-24 22:56:25', '1', '1', '', '1', 0);
INSERT INTO `doc` VALUES ('30817142391701504', '24789152289521664', '', '长文本测试01', '<h1 align=\"center\">Markdown Editor built on Vue</h1>\n\n<p align=\"center\">\n  <a href=\"https://npmcharts.com/compare/@kangc/v-md-editor?minimal=true\"><img src=\"https://img.shields.io/npm/dm/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Downloads\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/v/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Version\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/l/@kangc/v-md-editor.svg?sanitize=true\" alt=\"License\"></a>\n</p>\n\n# markdown\n\n<p align=\"center\">\n  <a href=\"https://npmcharts.com/compare/@kangc/v-md-editor?minimal=true\"><img src=\"https://img.shields.io/npm/dm/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Downloads\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/v/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Version\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/l/@kangc/v-md-editor.svg?sanitize=true\" alt=\"License\"></a>\n</p>\n\n\n<p align=\"center\">\n  <a href=\"https://npmcharts.com/compare/@kangc/v-md-editor?minimal=true\"><img src=\"https://img.shields.io/npm/dm/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Downloads\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/v/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Version\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/l/@kangc/v-md-editor.svg?sanitize=true\" alt=\"License\"></a>\n</p>\n\n\n\n\n## Links\n\n- [Demo](https://code-farmer-i.github.io/vue-markdown-editor/examples/base-editor.html)\n- [Documentation](https://code-farmer-i.github.io/vue-markdown-editor/)\n- [Changelog](https://code-farmer-i.github.io/vue-markdown-editor/changelog.html)\n\n## Install\n\n```bash\n# use npm\nnpm i @kangc/v-md-editor -S\n\n# use yarn\nyarn add @kangc/v-md-editor\n```\n\n## Quick Start\n\n```js\nimport Vue from \'vue\';\nimport VueMarkdownEditor from \'@kangc/v-md-editor\';\nimport \'@kangc/v-md-editor/lib/style/base-editor.css\';\nimport vuepressTheme from \'@kangc/v-md-editor/lib/theme/vuepress.js\';\nimport Vue from \'vue\';\nimport VueMarkdownEditor from \'@kangc/v-md-editor\';\nimport \'@kangc/v-md-editor/lib/style/base-editor.css\';\nimport vuepressTheme from \'@kangc/v-md-editor/lib/theme/vuepress.js\';\nimport Vue from \'vue\';\nimport VueMarkdownEditor from \'@kangc/v-md-editor\';\nimport \'@kangc/v-md-editor/lib/style/base-editor.css\';\nimport vuepressTheme from \'@kangc/v-md-editor/lib/theme/vuepress.js\';\nimport Vue from \'vue\';\nimport VueMarkdownEditor from \'@kangc/v-md-editor\';\nimport \'@kangc/v-md-editor/lib/style/base-editor.css\';\nimport vuepressTheme from \'@kangc/v-md-editor/lib/theme/vuepress.js\';\nimport Vue from \'vue\';\nimport VueMarkdownEditor from \'@kangc/v-md-editor\';\nimport \'@kangc/v-md-editor/lib/style/base-editor.css\';\nimport vuepressTheme from \'@kangc/v-md-editor/lib/theme/vuepress.js\';\nimport Vue from \'vue\';\nimport VueMarkdownEditor from \'@kangc/v-md-editor\';\nimport \'@kangc/v-md-editor/lib/style/base-editor.css\';\nimport vuepressTheme from \'@kangc/v-md-editor/lib/theme/vuepress.js\';\n\nVueMarkdownEditor.use(vuepressTheme);\n\nVue.use(VueMarkdownEditor);\n```\n\n## Usage\n\n```html\n<template>\n  <v-md-editor v-model=\"text\" height=\"400px\"></v-md-editor>\n</template>\n\n<script>\n  export default {\n    data() {\n      return {\n        text: \'\',\n      };\n    },\n  };\n</script>\n<template>\n  <v-md-editor v-model=\"text\" height=\"400px\"></v-md-editor>\n</template>\n\n<script>\n  export default {\n    data() {\n      return {\n        text: \'\',\n      };\n    },\n  };\n</script>\n<template>\n  <v-md-editor v-model=\"text\" height=\"400px\"></v-md-editor>\n</template>\n\n<script>\n  export default {\n    data() {\n      return {\n        text: \'\',\n      };\n    },\n  };\n</script>\n<template>\n  <v-md-editor v-model=\"text\" height=\"400px\"></v-md-editor>\n</template>\n\n<script>\n  export default {\n    data() {\n      return {\n        text: \'\',\n      };\n    },\n  };\n</script>\n<template>\n  <v-md-editor v-model=\"text\" height=\"400px\"></v-md-editor>\n</template>\n\n<script>\n  export default {\n    data() {\n      return {\n        text: \'\',\n      };\n    },\n  };\n</script>\n<template>\n  <v-md-editor v-model=\"text\" height=\"400px\"></v-md-editor>\n</template>\n\n<script>\n  export default {\n    data() {\n      return {\n        text: \'\',\n      };\n    },\n  };\n</script>\n```\n\n## Refrence\n\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n', '7097', '', 'tag', 10, '2021-03-27 00:56:19', '2021-03-27 00:56:19', '21580294339428352', NULL, '', '21580294339428352', 0);
INSERT INTO `doc` VALUES ('30819105955119104', '24789152289521664', '', '文档测试0099', '文档测试0099\n\n\n![七龙珠](https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1269952892,3525182336&fm=26&gp=0.jpg)\n\n![图片](http://192.168.3.13:24001/i/f/d/34412336462692352)', '171', '', 'tag', 0, '2021-03-27 01:04:07', '2021-04-05 23:02:28', '21580294339428352', '1', '', '21580294339428352', 0);
INSERT INTO `doc` VALUES ('30819481957695488', '24789152289521664', '', '文档测试0098', '文档测试0098', '8', '', 'tag', 0, '2021-03-27 01:05:37', '2021-03-27 01:05:37', '21580294339428352', NULL, '', '21580294339428352', 0);
INSERT INTO `doc` VALUES ('36580616010989568', '24789152289521664', '', '001', '<h1 align=\"center\">Markdown Editor built on Vue</h1>\n\n<p align=\"center\">\n  <a href=\"https://npmcharts.com/compare/@kangc/v-md-editor?minimal=true\"><img src=\"https://img.shields.io/npm/dm/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Downloads\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/v/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Version\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/l/@kangc/v-md-editor.svg?sanitize=true\" alt=\"License\"></a>\n</p>\n\n## Links\n\n- [Demo](https://code-farmer-i.github.io/vue-markdown-editor/examples/base-editor.html)\n- [Documentation](https://code-farmer-i.github.io/vue-markdown-editor/)\n- [Changelog](https://code-farmer-i.github.io/vue-markdown-editor/changelog.html)\n\n## Install\n\n```bash\n# use npm\nnpm i @kangc/v-md-editor -S\n\n# use yarn\nyarn add @kangc/v-md-editor\n```\n\n![图片](http://192.168.3.13:24001/i/f/d/u/36580845909180416.jpg)\n\n您的 AdBlock 已更新！ （4.32.0 版）\n去年，我们增加了几百万个新的广告拦截用户。 我们有幸运能拥有这样一个活跃且不断发展的社区，我们一直努力采用更多方式帮助用户体验不受干扰的互联网服务。 我们侧重于以下工作。\n\nAdBlock 是一款免费的、由用户支持的软件。 对我们来说重要的是，让任何人都可以使用我们的软件，无论他们是否能够做出贡献。 但 AdBlock 无法免费运营。 我们要支付薪水和基础设施的成本，以支持全球 7,000 万用户。 值得庆幸的是，许多用户非常支持 AdBlock，通过捐赠他们可以负担的费用来帮助支持 AdBlock 的持续发展。 没有您，我们就不存在。 如果您能够支持我们，我们感激不尽。 每一笔贡献都能帮助我们！\n![图片](http://192.168.3.13:24001/i/f/d/u/36580948233420800.png)\n支付你认为公道的价格（美元）：\n\n $ 3	 $ 5	 $ 7\n $ 8	 $ 10	 $ \n8\n付款方式：\n信用卡\nPayPal\n\n有什么新内容？\n下文简述了我们最近在 AdBlock 推出的内容以及最新产品线 AdBlock VPN 的更新。\n\n新 AdBlock 菜单。 10 年来，我们第一次完全重新设计了 AdBlock 菜单，用户能更轻松地找到最常用的功能。\nAdBlock VPN。 我们已经发布了适用于 Windows、Mac、iOS 和 Android 用户的 VPN 早期版本，为了准备即将开展更广泛的发布。 立即注册抢先体验。\n广告拦截改善。 我们增强了在使用攻击性广告技术来绕过 AdBlock 的网站上移除广告的能力。\n需要帮助吗？ 请访问我们的帮助中心，获取常见问题的修复方法。 如果您想了解更多关于我们的信息，请注册我们的每月电子通讯。\n此致敬礼\nAdBlock', '1775', '', 'tag', 0, '2021-04-11 22:38:18', '2021-04-11 22:39:40', '1', '1', '', '1', 0);
INSERT INTO `doc` VALUES ('36583220858322944', '28176636708913152', '', '部署', '导航故障\n3.4.0中新增\n\n译者注\n\n导航故障，或者叫导航失败，表示一次失败的导航，原文叫 navigation failures，本文统一采用导航故障。\n\n当使用 router-link 组件时，Vue Router 会自动调用 router.push 来触发一次导航。 虽然大多数链接的预期行为是将用户导航到一个新页面，但也有少数情况下用户将留在同一页面上：\n\n用户已经位于他们正在尝试导航到的页面\n一个导航守卫通过调用 next(false) 中断了这次导航\n一个导航守卫抛出了一个错误，或者调用了 next(new Error())\n当使用 router-link 组件时，这些失败都不会打印出错误。然而，如果你使用 router.push 或者 router.replace 的话，可能会在控制台看到一条 \"Uncaught (in promise) Error\" 这样的错误，后面跟着一条更具体的消息。让我们来了解一下如何区分导航故障。\n\n背景故事\n\n在 v3.2.0 中，可以通过使用 router.push 的两个可选的回调函数：onComplete 和 onAbort 来暴露导航故障。从版本 3.1.0 开始，router.push 和 router.replace 在没有提供 onComplete/onAbort 回调的情况下会返回一个 Promise。这个 Promise 的 resolve 和 reject 将分别用来代替 onComplete 和 onAbort 的调用。', '653', '', 'tag', 0, '2021-04-11 22:48:40', '2021-04-11 22:48:40', '1', NULL, '', '1', 0);
INSERT INTO `doc` VALUES ('36591259690729472', '21656049387831296', '', '第一张', '11', '2', '', 'tag', 0, '2021-04-11 23:20:36', '2021-04-11 23:20:36', '21580294339428352', NULL, '', '21580294339428352', 0);

-- ----------------------------
-- Table structure for doc_collected
-- ----------------------------
DROP TABLE IF EXISTS `doc_collected`;
CREATE TABLE `doc_collected`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `is_open` tinyint(1) NULL DEFAULT NULL COMMENT '开放性',
  `create_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户id',
  `owner_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属用户id',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `is_delete` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文档集' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doc_collected
-- ----------------------------
INSERT INTO `doc_collected` VALUES ('21654753893154816', '新建文集', '', 0, '21580294339428352', '21580294339428352', '2021-03-01 18:08:16', 1);
INSERT INTO `doc_collected` VALUES ('21655700618870784', 'UREPORT参数设置方法说明', 'xx', 0, '21580294339428352', '21580294339428352', '2021-03-01 18:12:01', 1);
INSERT INTO `doc_collected` VALUES ('21656049387831296', 'UREPORT数据钻取设置', 'x', 0, '21580294339428352', '21580294339428352', '2021-03-01 18:13:25', 0);
INSERT INTO `doc_collected` VALUES ('22822531811508224', '折纸精选', '收集折纸教程', 1, '21580294339428352', '21580294339428352', '2021-03-04 23:28:36', 1);
INSERT INTO `doc_collected` VALUES ('24787534009597952', 'JAVA开发规范', 'java开发规范', 0, '21580294339428352', '21580294339428352', '2021-03-10 09:36:49', 0);
INSERT INTO `doc_collected` VALUES ('24789035348131840', '手工课精选', '', 0, '1', '1', '2021-03-10 09:42:47', 0);
INSERT INTO `doc_collected` VALUES ('24789152289521664', '邮箱使用手册', '暂无', 1, '1', '1', '2021-03-10 09:43:14', 0);
INSERT INTO `doc_collected` VALUES ('24789262314504192', '办公室管理手册', '', 1, '1', '1', '2021-03-10 09:43:41', 0);
INSERT INTO `doc_collected` VALUES ('28176636708913152', '🍀Hidoc使用说明', '🍀使用方法请看详细内容', 1, '1', '1', '2021-03-19 18:03:54', 0);
INSERT INTO `doc_collected` VALUES ('36586703439265792', 'HTML测试-****', '<el-row>\n  <el-button>默认按钮</el-button>\n  <el-button type=\"primary\">主要按钮</el-button>\n  <el-button type=\"success\">成功按钮</el-button>\n  <el-button type=\"info\">信息按钮</el-button>\n  <el-button type=\"warning\">警告按钮</el-button>\n  <el-button type=\"danger\">危险按钮</el-button>\n</el-row>\n\n<el-row>\n  <el-button plain>朴素按钮</el-button>\n  <el-button type=\"primary\" plain>主要按钮</el-button>\n  <el-button type=\"success\" plain>成功按钮</el-button>\n  <el-button type=\"info\" plain>信息按钮</el-button>\n  <el-button type=\"warning\" plain>警告按钮</el-button>\n  <el-button type=\"danger\" plain>危险按钮</el-button>\n</el-row>\n\n<el-row>\n  <el-button round>圆角按钮</el-button>\n  <el-button type=\"primary\" round>主要按钮</el-button>\n  <el-button type=\"success\" round>成功按钮</el-button>\n  <el-button type=\"info\" round>信息按钮</el-button>\n  <el-button type=\"warning\" round>警告按钮</el-button>\n  <el-button type=\"danger\" round>危险按钮</el-button>\n</el-row>\n\n<el-row>\n  <el-button icon=\"el-icon-search\" circle></el-button>\n  <el-button type=\"primary\" icon=\"el-icon-edit\" circle></el-button>\n  <el-button type=\"success\" icon=\"el-icon-check\" circle></el-button>\n  <el-button type=\"info\" icon=\"el-icon-message\" circle></el-button>\n  <el-button type=\"warning\" icon=\"el-icon-star-off\" circle></el-button>\n  <el-button type=\"danger\" icon=\"el-icon-delete\" circle></el-button>\n</el-row>', 1, '1', '1', '2021-04-11 23:02:30', 0);

-- ----------------------------
-- Table structure for doc_collected_member
-- ----------------------------
DROP TABLE IF EXISTS `doc_collected_member`;
CREATE TABLE `doc_collected_member`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `collected_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文档集id',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `create_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户id',
  `allow_edit` tinyint(1) NULL DEFAULT NULL COMMENT '允许编辑',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文档成员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doc_collected_member
-- ----------------------------
INSERT INTO `doc_collected_member` VALUES ('30815534366851072', '24789152289521664', '1', '1', 1, '2021-03-27 00:49:56');
INSERT INTO `doc_collected_member` VALUES ('30815534371045376', '24789152289521664', '16284317638459392', '1', 1, '2021-03-27 00:49:56');
INSERT INTO `doc_collected_member` VALUES ('30815534375239680', '24789152289521664', '16285020989685760', '1', 1, '2021-03-27 00:49:56');
INSERT INTO `doc_collected_member` VALUES ('30815534379433984', '24789152289521664', '19366074914963456', '1', 1, '2021-03-27 00:49:56');
INSERT INTO `doc_collected_member` VALUES ('30815534383628288', '24789152289521664', '21580294339428352', '1', 1, '2021-03-27 00:49:56');

-- ----------------------------
-- Table structure for doc_history
-- ----------------------------
DROP TABLE IF EXISTS `doc_history`;
CREATE TABLE `doc_history`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `doc_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文档id',
  `collected_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文档集id',
  `doc_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文档类型',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `content_length` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容长度',
  `content_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容类型',
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `serial_number` int(11) NULL DEFAULT NULL COMMENT '排序序号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建用户id',
  `update_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文档修改历史' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doc_history
-- ----------------------------
INSERT INTO `doc_history` VALUES ('30061607950221312', '29887416907071488', '24789152289521664', '', '张三的歌', '我要带你到处去飞翔\n走遍世界各地去观赏\n没有烦恼没有那悲伤\n自由自在身心多开朗\n忘掉痛苦忘掉那地方\n我们一起启程去流浪\n虽然没有华厦美衣裳\n但是心里充满着希望\n我们要飞到那遥远地方\n看一看这世界并非那么凄凉\n我们要飞到那遥远地方\n望一望 这世界还是一片的光亮\n忘掉痛苦忘掉那地方\n我们一起启程去流浪\n虽然没有华厦美衣裳\n但是心里充满着希望\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉', '347', '', 'tag', 0, '2021-03-24 11:21:55', '1', NULL);
INSERT INTO `doc_history` VALUES ('30061751512858624', '29887416907071488', '24789152289521664', '', '张三的歌', '# 我要带你到处去飞翔\n走遍世界各地去观赏\n没有烦恼没有那悲伤\n自由自在身心多开朗\n忘掉痛苦忘掉那地方\n我们一起启程去流浪\n虽然没有华厦美衣裳\n但是心里充满着希望\n我们要飞到那遥远地方\n看一看这世界并非那么凄凉\n我们要飞到那遥远地方\n望一望 这世界还是一片的光亮\n忘掉痛苦忘掉那地方\n# 我们一起启程去流浪\n虽然没有华厦美衣裳\n但是心里充满着希望\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉', '351', '', 'tag', 0, '2021-03-24 11:21:55', '1', '1');
INSERT INTO `doc_history` VALUES ('30061795766960128', '29887416907071488', '24789152289521664', '', '张三的歌', '# 我要带你到处去飞翔\n走遍世界各地去观赏\n没有烦恼没有那悲伤\n自由自在身心多开朗\n忘掉痛苦忘掉那地方\n我们一起启程去流浪\n虽然没有华厦美衣裳\n但是心里充满着希望\n我们要飞到那遥远地方\n看一看这世界并非那么凄凉\n我们要飞到那遥远地方\n望一望 这世界还是一片的光亮\n忘掉痛苦忘掉那地方\n# 我们一起启程去流浪\n虽然没有华厦美衣裳\n但是心里充满着希望\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉', '351', '', 'tag', 0, '2021-03-24 11:21:55', '1', '1');
INSERT INTO `doc_history` VALUES ('30062151729152000', '29887416907071488', '24789152289521664', '', '张三的歌', '# 我要带你到处去飞翔\n走遍世界各地去观赏\n没有烦恼没有那悲伤\n自由自在身心多开朗\n忘掉痛苦忘掉那地方\n我们一起启程去流浪\n虽然没有华厦美衣裳\n但是心里充满着希望\n我们要飞到那遥远地方\n看一看这世界并非那么凄凉\n我们要飞到那遥远地方\n望一望 这世界还是一片的光亮\n忘掉痛苦忘掉那地方\n# 我们一起启程去流浪\n虽然没有华厦美衣裳\n但是心里充满着希望\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉', '351', '', 'tag', 0, '2021-03-24 11:21:55', '1', '1');
INSERT INTO `doc_history` VALUES ('30062190920728576', '29887416907071488', '24789152289521664', '', '张三的歌', '# 我要带你到处去飞翔\n走遍世界各地去观赏\n没有烦恼没有那悲伤\n自由自在身心多开朗\n忘掉痛苦忘掉那地方\n我们一起启程去流浪\n虽然没有华厦美衣裳\n但是心里充满着希望\n我们要飞到那遥远地方\n看一看这世界并非那么凄凉\n我们要飞到那遥远地方\n望一望 这世界还是一片的光亮\n忘掉痛苦忘掉那地方\n# 我们一起启程去流浪\n虽然没有华厦美衣裳\n但是心里充满着希望\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉\n我们要飞到那遥远地方 望一望\n这世界还是一片的光亮\n我们要飞到那遥远地方 看一看\n这世界并非那么凄凉', '351', '', 'tag', 0, '2021-03-24 11:21:55', '1', '1');
INSERT INTO `doc_history` VALUES ('30609006095499264', '27155370212327424', '24789152289521664', '', '基础配置', '<h1 align=\"center\">Markdown Editor built on Vue</h1>\n\n<p align=\"center\">\n  <a href=\"https://npmcharts.com/compare/@kangc/v-md-editor?minimal=true\"><img src=\"https://img.shields.io/npm/dm/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Downloads\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/v/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Version\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/l/@kangc/v-md-editor.svg?sanitize=true\" alt=\"License\"></a>\n</p>\n\n## Links\n\n- [Demo](https://code-farmer-i.github.io/vue-markdown-editor/examples/base-editor.html)\n- [Documentation](https://code-farmer-i.github.io/vue-markdown-editor/)\n- [Changelog](https://code-farmer-i.github.io/vue-markdown-editor/changelog.html)\n\n## Install\n\n```bash\n# use npm\nnpm i @kangc/v-md-editor -S\n\n# use yarn\nyarn add @kangc/v-md-editor\n```\n\n## Quick Start\n\n```js\nimport Vue from \'vue\';\nimport VueMarkdownEditor from \'@kangc/v-md-editor\';\nimport \'@kangc/v-md-editor/lib/style/base-editor.css\';\nimport vuepressTheme from \'@kangc/v-md-editor/lib/theme/vuepress.js\';\n\nVueMarkdownEditor.use(vuepressTheme);\n\nVue.use(VueMarkdownEditor);\n```\n\n## Usage\n\n```html\n<template>\n  <v-md-editor v-model=\"text\" height=\"400px\"></v-md-editor>\n</template>\n\n<script>\n  export default {\n    data() {\n      return {\n        text: \'\',\n      };\n    },\n  };\n</script>\n```\n\n## Refrence\n\n- [ElementUi Scrollbar Component](https://github.com/ElemeFE/element/tree/dev/packages/scrollbar)\n- [vuepress-plugin-container](https://vuepress.github.io/zh/plugins/container/)\n', '1620', '', 'tag', 10, '2021-03-26 02:25:47', '1', NULL);
INSERT INTO `doc_history` VALUES ('30609150559911936', '27796240007692288', '24789152289521664', '', '邮箱密码定期修改要求', '|序号|规则|描述|\n|-|-|-|\n|1|数字|必须包含数字|', '32', '', 'tag', 30, '2021-03-18 16:52:20', '1', NULL);
INSERT INTO `doc_history` VALUES ('34395218841174016', '30819105955119104', '24789152289521664', '', '文档测试0099', '文档测试0099', '8', '', 'tag', 0, '2021-03-27 01:04:07', '21580294339428352', NULL);
INSERT INTO `doc_history` VALUES ('34395248658481152', '30819105955119104', '24789152289521664', '', '文档测试0099', '文档测试0099\n\n\n![七龙珠](https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1269952892,3525182336&fm=26&gp=0.jpg)', '113', '', 'tag', 0, '2021-03-27 01:04:07', '21580294339428352', '1');
INSERT INTO `doc_history` VALUES ('34412369018880000', '30819105955119104', '24789152289521664', '', '文档测试0099', '文档测试0099\n\n\n![七龙珠](https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1269952892,3525182336&fm=26&gp=0.jpg)\n\n', '115', '', 'tag', 0, '2021-03-27 01:04:07', '21580294339428352', '1');
INSERT INTO `doc_history` VALUES ('36580958773706752', '36580616010989568', '24789152289521664', '', '001', '<h1 align=\"center\">Markdown Editor built on Vue</h1>\n\n<p align=\"center\">\n  <a href=\"https://npmcharts.com/compare/@kangc/v-md-editor?minimal=true\"><img src=\"https://img.shields.io/npm/dm/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Downloads\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/v/@kangc/v-md-editor.svg?sanitize=true\" alt=\"Version\"></a>\n  <a href=\"https://www.npmjs.com/package/@kangc/v-md-editor\"><img src=\"https://img.shields.io/npm/l/@kangc/v-md-editor.svg?sanitize=true\" alt=\"License\"></a>\n</p>\n\n## Links\n\n- [Demo](https://code-farmer-i.github.io/vue-markdown-editor/examples/base-editor.html)\n- [Documentation](https://code-farmer-i.github.io/vue-markdown-editor/)\n- [Changelog](https://code-farmer-i.github.io/vue-markdown-editor/changelog.html)\n\n## Install\n\n```bash\n# use npm\nnpm i @kangc/v-md-editor -S\n\n# use yarn\nyarn add @kangc/v-md-editor\n```\n\n您的 AdBlock 已更新！ （4.32.0 版）\n去年，我们增加了几百万个新的广告拦截用户。 我们有幸运能拥有这样一个活跃且不断发展的社区，我们一直努力采用更多方式帮助用户体验不受干扰的互联网服务。 我们侧重于以下工作。\n\nAdBlock 是一款免费的、由用户支持的软件。 对我们来说重要的是，让任何人都可以使用我们的软件，无论他们是否能够做出贡献。 但 AdBlock 无法免费运营。 我们要支付薪水和基础设施的成本，以支持全球 7,000 万用户。 值得庆幸的是，许多用户非常支持 AdBlock，通过捐赠他们可以负担的费用来帮助支持 AdBlock 的持续发展。 没有您，我们就不存在。 如果您能够支持我们，我们感激不尽。 每一笔贡献都能帮助我们！\n\n支付你认为公道的价格（美元）：\n\n $ 3	 $ 5	 $ 7\n $ 8	 $ 10	 $ \n8\n付款方式：\n信用卡\nPayPal\n\n有什么新内容？\n下文简述了我们最近在 AdBlock 推出的内容以及最新产品线 AdBlock VPN 的更新。\n\n新 AdBlock 菜单。 10 年来，我们第一次完全重新设计了 AdBlock 菜单，用户能更轻松地找到最常用的功能。\nAdBlock VPN。 我们已经发布了适用于 Windows、Mac、iOS 和 Android 用户的 VPN 早期版本，为了准备即将开展更广泛的发布。 立即注册抢先体验。\n广告拦截改善。 我们增强了在使用攻击性广告技术来绕过 AdBlock 的网站上移除广告的能力。\n需要帮助吗？ 请访问我们的帮助中心，获取常见问题的修复方法。 如果您想了解更多关于我们的信息，请注册我们的每月电子通讯。\n此致敬礼\nAdBlock', '1649', '', 'tag', 0, '2021-04-11 22:38:18', '1', NULL);

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ext` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `size` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sha1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件信息表（物理文件信息）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file
-- ----------------------------
INSERT INTO `sys_file` VALUES ('12657275986509824', 'HiDevTools-1.0.0.15.udp', 'udp', '22756856', '202102\\12657275986509824', '2021-02-04 14:15:30', '1', '4578BB38591A7C38F77DE4B6A579D736', '5DAAB6594B174E2E0096EC30805F343F81A47418');
INSERT INTO `sys_file` VALUES ('34408187335540736', '微信截图_20200203224616.png', 'png', '5920355', '202104\\34408187335540736', '2021-04-05 22:45:51', '1', '49B400138705E6A21F537EB661F87A29', 'BCBF187A32E5E2DEAD64A4524AAB2BE330882CCD');
INSERT INTO `sys_file` VALUES ('36580845741408256', '微信截图_20200203224616.jpg', 'jpg', '972060', '202104\\36580845741408256', '2021-04-11 22:39:13', '1', '4FE2660D04C8DE88D20C5A7780567FC4', '4D01137F21658133CACB6AB00060D993977CDBD7');
INSERT INTO `sys_file` VALUES ('36580948183089152', '微信截图_20200203224538.png', 'png', '235450', '202104\\36580948183089152', '2021-04-11 22:39:38', '1', 'AFB2C9613580C1FDFC916AE4D2FE2EC9', 'EAB9DA93713F66550EC444ABE0ED7E615FB0B075');

-- ----------------------------
-- Table structure for sys_file_bucket
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_bucket`;
CREATE TABLE `sys_file_bucket`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_open` tinyint(1) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件桶信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file_bucket
-- ----------------------------
INSERT INTO `sys_file_bucket` VALUES ('12657277014114304', '1', 'HiDevTools', 1);
INSERT INTO `sys_file_bucket` VALUES ('12658332401664000', '1', 'Test', 1);
INSERT INTO `sys_file_bucket` VALUES ('34408187671085056', '1', 'fcb5ff8e43e44071bc24ce3d044b9b53', 1);
INSERT INTO `sys_file_bucket` VALUES ('34409381059624960', '1', '5ed0c53d451945b8b8e0d6e6d20529a2', 1);
INSERT INTO `sys_file_bucket` VALUES ('34411090418860032', '1', '2e3c0e8d4a0d4e9da5431a7e42e75edf', 1);
INSERT INTO `sys_file_bucket` VALUES ('34411875743563776', '1', '4b229123f1514863ad3356a9660dc13c', 1);
INSERT INTO `sys_file_bucket` VALUES ('34412135106740224', '1', '4f07491c79c74af3887a35448deaf617', 1);
INSERT INTO `sys_file_bucket` VALUES ('34412336450109440', '1', 'a8f863a05d984ff89995c78524d35b40', 1);
INSERT INTO `sys_file_bucket` VALUES ('36580845896597504', '1', 'a0c3dd5481224984a406c97e616796f0', 1);
INSERT INTO `sys_file_bucket` VALUES ('36580948225032192', '1', 'cadc9fb87c594140a17d4514f347b1dd', 1);
INSERT INTO `sys_file_bucket` VALUES ('36596480194117632', '21580294339428352', '9004be6b028a4adc8c8e5743b85e6681', 1);

-- ----------------------------
-- Table structure for sys_file_cursor
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_cursor`;
CREATE TABLE `sys_file_cursor`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `bucket_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属桶ID',
  `file_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指向文件ID',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上传文件名称',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上传用户ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '上传时间',
  `version` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本号',
  `expiry_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `collected_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属文集ID',
  `doc_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属文档ID',
  `uname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '下载唯一文件名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件指针信息（指向文件信息）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file_cursor
-- ----------------------------
INSERT INTO `sys_file_cursor` VALUES ('12657277022502912', '12657277014114304', '12657275986509824', 'HiDevTools-1.0.0.15.udp', '1', '2021-02-04 14:15:30', '1612448130003', '2021-12-02 04:00:00', NULL, NULL, NULL);
INSERT INTO `sys_file_cursor` VALUES ('12658332418441216', '12658332401664000', '12657275986509824', 'HiDevTools-1.0.0.15.udp', '1', '2021-02-04 14:19:41', '1612448381629', '2021-12-02 04:00:00', NULL, NULL, NULL);
INSERT INTO `sys_file_cursor` VALUES ('18790762690904064', '12657277014114304', '12657275986509824', 'HiDevTools-1.0.0.15.udp', '1', '2021-02-21 12:27:47', '1613910466941', '2099-12-02 04:00:00', NULL, NULL, NULL);
INSERT INTO `sys_file_cursor` VALUES ('18790897177067520', '12657277014114304', '12657275986509824', 'HiDevTools-1.0.0.15.udp', '1', '2021-02-21 12:28:19', '1613910499005', '2099-12-02 04:00:00', NULL, NULL, NULL);
INSERT INTO `sys_file_cursor` VALUES ('18791272307228672', '12657277014114304', '12657275986509824', 'HiDevTools-1.0.0.15.udp', '1', '2021-02-21 20:29:48', '1613910588443', '2099-12-02 12:00:00', NULL, NULL, NULL);
INSERT INTO `sys_file_cursor` VALUES ('34408187679473664', '34408187671085056', '34408187335540736', '微信截图_20200203224616.png', '1', '2021-04-05 22:45:51', '1617633951216', '9999-12-31 12:00:00', NULL, NULL, NULL);
INSERT INTO `sys_file_cursor` VALUES ('34409381063819264', '34409381059624960', '34408187335540736', '微信截图_20200203224616.png', '1', '2021-04-05 22:50:36', '1617634235741', '9999-12-31 12:00:00', NULL, NULL, NULL);
INSERT INTO `sys_file_cursor` VALUES ('34411090435637248', '34411090418860032', '34408187335540736', '微信截图_20200203224616.png', '1', '2021-04-05 22:57:23', '1617634643287', '9999-12-31 12:00:00', NULL, NULL, NULL);
INSERT INTO `sys_file_cursor` VALUES ('34411875764535296', '34411875743563776', '34408187335540736', '微信截图_20200203224616.png', '1', '2021-04-05 23:00:30', '1617634830524', '9999-12-31 12:00:00', NULL, NULL, NULL);
INSERT INTO `sys_file_cursor` VALUES ('34412135119323136', '34412135106740224', '34408187335540736', '微信截图_20200203224616.png', '1', '2021-04-05 23:01:32', '1617634892359', '9999-12-31 12:00:00', NULL, NULL, NULL);
INSERT INTO `sys_file_cursor` VALUES ('34412336462692352', '34412336450109440', '34408187335540736', '微信截图_20200203224616.png', '1', '2021-04-05 23:02:20', '1617634940363', '9999-12-31 12:00:00', NULL, NULL, NULL);
INSERT INTO `sys_file_cursor` VALUES ('36580845909180416', '36580845896597504', '36580845741408256', '微信截图_20200203224616.jpg', '1', '2021-04-11 22:39:13', '1618151953304', '9999-12-31 12:00:00', NULL, NULL, '36580845909180416.jpg');
INSERT INTO `sys_file_cursor` VALUES ('36580948233420800', '36580948225032192', '36580948183089152', '微信截图_20200203224538.png', '1', '2021-04-11 22:39:38', '1618151977700', '9999-12-31 12:00:00', NULL, NULL, '36580948233420800.png');
INSERT INTO `sys_file_cursor` VALUES ('36596480210894848', '36596480194117632', '34408187335540736', '微信截图_20200203224616.png', '21580294339428352', '2021-04-11 23:41:21', '1618155680812', '9999-12-31 12:00:00', NULL, NULL, '36596480210894848.png');

-- ----------------------------
-- Table structure for sys_file_download_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_download_log`;
CREATE TABLE `sys_file_download_log`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `cursor_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件下载记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file_download_log
-- ----------------------------
INSERT INTO `sys_file_download_log` VALUES ('34412145047240704', '192.168.3.13', '2021-04-05 23:01:35', '34412135119323136', '微信截图_20200203224616.png');
INSERT INTO `sys_file_download_log` VALUES ('34412336538189824', '192.168.3.13', '2021-04-05 23:02:20', '34412336462692352', '微信截图_20200203224616.png');
INSERT INTO `sys_file_download_log` VALUES ('34412376778342400', '192.168.3.13', '2021-04-05 23:02:30', '34412336462692352', '微信截图_20200203224616.png');
INSERT INTO `sys_file_download_log` VALUES ('36580846035009536', '192.168.3.13', '2021-04-11 22:39:13', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36580948321501184', '192.168.3.13', '2021-04-11 22:39:38', '36580948233420800', '微信截图_20200203224538.png');
INSERT INTO `sys_file_download_log` VALUES ('36581029254791168', '192.168.3.13', '2021-04-11 22:39:57', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36581029368037376', '192.168.3.13', '2021-04-11 22:39:57', '36580948233420800', '微信截图_20200203224538.png');
INSERT INTO `sys_file_download_log` VALUES ('36581598207934464', '192.168.3.13', '2021-04-11 22:42:13', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36581598207934465', '192.168.3.13', '2021-04-11 22:42:13', '36580948233420800', '微信截图_20200203224538.png');
INSERT INTO `sys_file_download_log` VALUES ('36596480370278400', '192.168.3.13', '2021-04-11 23:41:21', '36596480210894848', '微信截图_20200203224616.png');
INSERT INTO `sys_file_download_log` VALUES ('36936340826226688', '192.168.3.13', '2021-04-12 22:11:50', '36580948233420800', '微信截图_20200203224538.png');
INSERT INTO `sys_file_download_log` VALUES ('36936340826226689', '192.168.3.13', '2021-04-12 22:11:50', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36944693396766720', '192.168.3.13', '2021-04-12 22:45:01', '36580948233420800', '微信截图_20200203224538.png');
INSERT INTO `sys_file_download_log` VALUES ('36944693434515456', '192.168.3.13', '2021-04-12 22:45:01', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36952887749771264', '192.168.3.13', '2021-04-12 23:17:35', '36580948233420800', '微信截图_20200203224538.png');
INSERT INTO `sys_file_download_log` VALUES ('36952887749771265', '192.168.3.13', '2021-04-12 23:17:35', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36958004708376576', '192.168.3.13', '2021-04-12 23:37:55', '36580948233420800', '微信截图_20200203224538.png');
INSERT INTO `sys_file_download_log` VALUES ('36958004708376577', '192.168.3.13', '2021-04-12 23:37:55', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36958645749022720', '192.168.3.13', '2021-04-12 23:40:28', '36580948233420800', '微信截图_20200203224538.png');
INSERT INTO `sys_file_download_log` VALUES ('36958645749022721', '192.168.3.13', '2021-04-12 23:40:28', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36962310035603456', '192.168.3.13', '2021-04-12 23:55:01', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36962310035603457', '192.168.3.13', '2021-04-12 23:55:01', '36580948233420800', '微信截图_20200203224538.png');
INSERT INTO `sys_file_download_log` VALUES ('36963606289448960', '192.168.3.13', '2021-04-13 00:00:10', '36580948233420800', '微信截图_20200203224538.png');
INSERT INTO `sys_file_download_log` VALUES ('36963606331392000', '192.168.3.13', '2021-04-13 00:00:11', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36963818709975040', '192.168.3.13', '2021-04-13 00:01:01', '36580948233420800', '微信截图_20200203224538.png');
INSERT INTO `sys_file_download_log` VALUES ('36963818714169344', '192.168.3.13', '2021-04-13 00:01:01', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36963965091184640', '192.168.3.13', '2021-04-13 00:01:36', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36963965095378944', '192.168.3.13', '2021-04-13 00:01:36', '36580948233420800', '微信截图_20200203224538.png');
INSERT INTO `sys_file_download_log` VALUES ('36964142732541952', '192.168.3.13', '2021-04-13 00:02:18', '36580948233420800', '微信截图_20200203224538.png');
INSERT INTO `sys_file_download_log` VALUES ('36964142732541953', '192.168.3.13', '2021-04-13 00:02:18', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36966698464575488', '192.168.3.13', '2021-04-13 00:12:28', '36580845909180416', '微信截图_20200203224616.jpg');
INSERT INTO `sys_file_download_log` VALUES ('36966698535878656', '192.168.3.13', '2021-04-13 00:12:28', '36580948233420800', '微信截图_20200203224538.png');

-- ----------------------------
-- Table structure for sys_machine
-- ----------------------------
DROP TABLE IF EXISTS `sys_machine`;
CREATE TABLE `sys_machine`  (
  `machine_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mac` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `data_center_id` int(11) NULL DEFAULT NULL,
  `worker_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`machine_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务器配置信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_machine
-- ----------------------------
INSERT INTO `sys_machine` VALUES ('39c756a4a2b44e42a1e7af5bc08713e5', '127.0.0.1', '00:00:00:00:00:00', 0, 0);
INSERT INTO `sys_machine` VALUES ('a14d47e65f5e4a38ad82360c1359880e', '127.0.0.1', '00:00:00:00:00:00', 0, 1);

-- ----------------------------
-- Table structure for sys_machine_status_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_machine_status_log`;
CREATE TABLE `sys_machine_status_log`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `machine_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `cpu` int(11) NULL DEFAULT NULL,
  `ram` bigint(20) NULL DEFAULT NULL,
  `disk` bigint(20) NULL DEFAULT NULL,
  `app_cpu` int(11) NULL DEFAULT NULL,
  `app_ram` bigint(20) NULL DEFAULT NULL,
  `ss_long` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '服务器状态信息记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_machine_status_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu_res
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu_res`;
CREATE TABLE `sys_menu_res`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `menu_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu_res
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `menu_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_urse_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_urse_role`;
CREATE TABLE `sys_urse_role`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `role_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_urse_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '名称',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实名',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `is_frozen` tinyint(1) NULL DEFAULT NULL COMMENT '是否冻结账户',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE COMMENT '用户名唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', NULL, 'hidoc', '系统账号', 'hidoc@mail.com', '2021-02-04 22:03:00', NULL, 0, '0');
INSERT INTO `sys_user` VALUES ('16284317638459392', NULL, 'y', '测试账号y', 'y', '2021-02-14 14:28:06', NULL, 0, 'y');
INSERT INTO `sys_user` VALUES ('16285020989685760', NULL, 'a', '测试账号a', 'a', '2021-02-14 14:30:52', NULL, 0, 'a');
INSERT INTO `sys_user` VALUES ('19366074914963456', NULL, 'zhangsan', '张三', '0', '2021-02-23 10:33:52', NULL, 0, '0');
INSERT INTO `sys_user` VALUES ('21580294339428352', '$system$49.jpg', 'yuzhyn', '于正洋', 'yuzhyn@163.com', '2021-03-01 13:12:23', NULL, 0, '0');

-- ----------------------------
-- Table structure for sys_user_file_conf
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_file_conf`;
CREATE TABLE `sys_user_file_conf`  (
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `expiry_time` datetime(0) NULL DEFAULT NULL,
  `space_limit` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `used_space` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `url_prefix` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户文件上传配置信息（配额）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_file_conf
-- ----------------------------
INSERT INTO `sys_user_file_conf` VALUES ('1', '2021-02-04 22:03:26', '2099-02-04 22:03:29', '1048576000', '150513920', 'hidoc');
INSERT INTO `sys_user_file_conf` VALUES ('16284317638459392', '2021-02-14 14:28:10', '9999-12-31 04:00:00', '1073741824', '0', 'y');
INSERT INTO `sys_user_file_conf` VALUES ('16285020989685760', '2021-02-14 14:30:52', '9999-12-31 04:00:00', '1073741824', '0', 'a');
INSERT INTO `sys_user_file_conf` VALUES ('19366074914963456', '2021-02-23 10:33:52', '9999-12-31 12:00:00', '1073741824', '0', 'zhangsan');
INSERT INTO `sys_user_file_conf` VALUES ('21580294339428352', '2021-03-01 13:12:23', '9999-12-31 12:00:00', '1073741824', '5920355', 'yuzhyn');

SET FOREIGN_KEY_CHECKS = 1;
