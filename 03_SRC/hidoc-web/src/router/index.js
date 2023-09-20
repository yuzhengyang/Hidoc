import router from './router.js';
import store from '../store';

// 不重定向白名单
const whiteList = ['home', 'preview', 'fileshare', 'javadoc', 'collected', 'login', 'loginx', 'register', 'docfocus'];

router.beforeEach((to, from, next) => {
    console.log('store.state.user.token: ' + store.state.user.token);
    console.log('to.name: ' + to.name);

    // 如果用户已登录到系统（token已设置值）
    if (store.state.user.token !== undefined && store.state.user.token !== '') {
        // 已登录用户，不允许再次进入登录界面，将自动转到主页
        if (to.name === 'login') {
            next({ path: '/' });
        } else {
            next();
        }
    } else {
        if (whiteList.indexOf(to.name) !== -1) {
            // 未登录用户，可以访问白名单的页面地址
            next();
        } else {
            // 非白名单页面地址，重定向到登录页
            next(`/login?redirect=${to.path}`);
        }
    }
});

export default router;
