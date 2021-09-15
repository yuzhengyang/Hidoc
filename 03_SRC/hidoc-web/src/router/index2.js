import router from './router2.js';

const whiteList = ['/login']; // 不重定向白名单

router.beforeEach((to, from, next) => {
  // next()
  // NProgress.start();
  if (this.$store.state.user.token == undefined || this.$store.state.user.token == '') {
    if (to.path === '/login') {
      next({ path: '/' });
      // NProgress.done(); // if current page is dashboard will not trigger	afterEach hook, so manually handle it
    } else {
      next();
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next();
    } else {
      next(`/login?redirect=${to.path}`); // 否则全部重定向到登录页
      // NProgress.done();
    }
  }
});

router.afterEach(() => {
  // NProgress.done(); // 结束Progress
});
