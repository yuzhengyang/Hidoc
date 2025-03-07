import { createI18n } from 'vue-i18n';

// 引入中文语言包
import zh from './locales/zh.json';
import en from './locales/en.json';

// 创建 i18n 实例
const i18n = createI18n({
    legacy: false, // 关闭 Vue 2 兼容模式
    locale: 'zh', // 默认语言
    fallbackLocale: 'zh', // 回退语言
    messages: {
        zh,
        en
    }
});

export default i18n;
