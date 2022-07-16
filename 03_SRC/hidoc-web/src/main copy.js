import { createApp } from 'vue';
import ElementPlus from 'element-plus';
import 'element-plus/lib/theme-chalk/index.css';
import App from './App.vue';
import router from './router';
import store from './store';

// markdown编辑器组件
import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
import 'prismjs/components/prism-json';
import 'prismjs/components/prism-java';
import 'prismjs/components/prism-sql';
import 'prismjs/components/prism-plsql';
import 'prismjs/components/prism-bash';
VueMarkdownEditor.use(vuepressTheme);

// markdown预览组件
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
VMdPreview.use(vuepressTheme);
// 扩展代码高亮：没有某种代码高亮时，可使用别的代码高亮扩展
VueMarkdownEditor.use(vuepressTheme, {
    codeHighlightExtensionMap: {
        vue: 'html'
    }
});

// 代码行号
import createLineNumbertPlugin from '@kangc/v-md-editor/lib/plugins/line-number/index';
VueMarkdownEditor.use(createLineNumbertPlugin());

// 代码高亮
import createHighlightLinesPlugin from '@kangc/v-md-editor/lib/plugins/highlight-lines/index';
import '@kangc/v-md-editor/lib/plugins/highlight-lines/highlight-lines.css';
VueMarkdownEditor.use(createHighlightLinesPlugin());

// 支持代码复制
import createCopyCodePlugin from '@kangc/v-md-editor/lib/plugins/copy-code/index';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';
VueMarkdownEditor.use(createCopyCodePlugin());


const app = createApp(App);
app.use(router);
app.use(store);
app.use(ElementPlus);
app.use(VueMarkdownEditor);
app.use(VMdPreview);
app.mount('#app');
