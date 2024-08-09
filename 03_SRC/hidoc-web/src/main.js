import { createApp } from 'vue';

import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';

import App from './App.vue';
import router from './router';
import store from './store';

import VMdEditor from '@kangc/v-md-editor/lib/codemirror-editor';
import '@kangc/v-md-editor/lib/style/codemirror-editor.css';

import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';

import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';

import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';

import Prism from 'prismjs';

// highlightjs
import hljs from 'highlight.js';

// codemirror 编辑器的相关资源
import Codemirror from 'codemirror';
// mode
import 'codemirror/mode/markdown/markdown';
import 'codemirror/mode/javascript/javascript';
import 'codemirror/mode/css/css';
import 'codemirror/mode/htmlmixed/htmlmixed';
import 'codemirror/mode/vue/vue';
// edit
import 'codemirror/addon/edit/closebrackets';
import 'codemirror/addon/edit/closetag';
import 'codemirror/addon/edit/matchbrackets';
// placeholder
import 'codemirror/addon/display/placeholder';
// active-line
import 'codemirror/addon/selection/active-line';
// scrollbar
import 'codemirror/addon/scroll/simplescrollbars';
import 'codemirror/addon/scroll/simplescrollbars.css';
// style
import 'codemirror/lib/codemirror.css';

VMdEditor.Codemirror = Codemirror;
VMdEditor.use(vuepressTheme, {
    Hljs: hljs,
    Prism,
    extend(md) {
        // md为 markdown-it 实例，可以在此处进行修改配置,并使用 plugin 进行语法扩展
        // md.set(option).use(plugin);
    }
});

VMdPreview.use(vuepressTheme, {
    Hljs: hljs
});

// 插件
// 公式
import createKatexPlugin from '@kangc/v-md-editor/lib/plugins/katex/npm';
VMdEditor.use(createKatexPlugin());
// 流程图
// import createMermaidPlugin from '@kangc/v-md-editor/lib/plugins/mermaid/npm';
// import '@kangc/v-md-editor/lib/plugins/mermaid/mermaid.css';
// VMdEditor.use(createMermaidPlugin());
import createMermaidPlugin from '@kangc/v-md-editor/lib/plugins/mermaid/cdn';
import '@kangc/v-md-editor/lib/plugins/mermaid/mermaid.css';
VMdEditor.use(createMermaidPlugin());

// TodoList
import createTodoListPlugin from '@kangc/v-md-editor/lib/plugins/todo-list/index';
import '@kangc/v-md-editor/lib/plugins/todo-list/todo-list.css';
VMdEditor.use(createTodoListPlugin());
// 代码行号
import createLineNumbertPlugin from '@kangc/v-md-editor/lib/plugins/line-number/index';
VMdEditor.use(createLineNumbertPlugin());
// 代码高亮
import createHighlightLinesPlugin from '@kangc/v-md-editor/lib/plugins/highlight-lines/index';
import '@kangc/v-md-editor/lib/plugins/highlight-lines/highlight-lines.css';
VMdEditor.use(createHighlightLinesPlugin());
// 支持代码复制
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';
VMdEditor.use(createCopyCodePlugin());
// 内容定位
import createAlignPlugin from '@kangc/v-md-editor/lib/plugins/align';
VMdEditor.use(createAlignPlugin());
// 自定义解析测试（暂不继续了，从入门到放弃，使用预览前预处理来实现想要的功能了）
// import createMdFilePlugin from './plugin/md-file';
// VMdEditor.use(createMdFilePlugin());

// 百度图表EChart
import * as echarts from 'echarts';

// 导入所有图标并进行全局注册
// 如果您正在使用CDN引入，请删除下面一行
import * as ElementPlusIconsVue from '@element-plus/icons-vue';

// import mermaid from 'mermaid';
// mermaid.initialize({ startOnLoad: true });


const app = createApp(App);
app.use(router);
app.use(store);
app.use(ElementPlus);
app.use(VMdEditor);
app.use(VMdPreview);
app.config.globalProperties.$echarts = echarts;
// app.use(mermaid);
app.mount('#app');
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
}
