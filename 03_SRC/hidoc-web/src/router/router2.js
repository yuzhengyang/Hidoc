import Router from 'vue-router';
import { createRouter, createWebHistory } from 'vue-router';
// import Layout from '../views/layout/Layout';
/**
 * 重写路由的push方法，解决重复路由问题
 */
// const routerReplace = Router.prototype.replace;
// Router.prototype.replace = function replace(location) {
//     return routerReplace.call(this, location).catch(error => error);
// };


// export const constantRouterMap = ;
// export default new Router({
//     mode: 'hash',
//     scrollBehavior: () => ({
//         y: 0
//     }),
//     routes: constantRouterMap
// });

const routerHistory = createWebHistory();

const router = createRouter({
    history: routerHistory,
    routes: [
        {
            path: '/',
            component: () => import('@/views/account/login'),
        },
        {
            path: '/login',
            component: () => import('@/views/account/login'),
        },
        {
            path: '/404',
            component: () => import('@/views/features/404'),
            hidden: true
        },
        {
            path: '/401',
            component: () => import('@/views/features/401'),
            hidden: true
        },
        {
            path: '/redirect',
            component: null,
            hidden: true,
            children: [
                {
                    path: '/redirect/:path*',
                    component: () => import('@/views/features/redirect')
                }
            ]
        },
        // {
        //     path: '/',
        //     component: null,
        //     redirect: '/preview',
        //     name: '首页',
        //     children: [
        //         {
        //             path: 'preview',
        //             component: () => import('@/views/preview'),
        //             name: 'preview'
        //         }
        //     ]
        // },
        {
            path: '/workbench',
            component: null,
            children: [
                // {
                //     path: 'wholeSale',
                //     component: () => import('@/views/biz/dis/wholesale/wholesale'),
                //     name: 'wholeSale'
                // },
                // {
                //     path: 'stkInNoPurChk',
                //     component: () => import('@/views/biz/pur/stkin/noordpurchk'),
                //     name: 'stkInNoPurChk'
                // },
                // {
                //     path: 'stkInChkDrc',
                //     component: () => import('@/views/biz/pur/stkin/directPurchaseNotice'),
                //     name: 'stkInChkDrc'
                // },
                // {
                //     path: 'comrtnorder',
                //     component: () => import('@/views/biz/pur/rtnorder/comRtnOrder'),
                //     name: 'comrtnorder'
                // }
            ]
        }
    ]
});

export default router;