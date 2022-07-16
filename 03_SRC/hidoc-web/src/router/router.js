// import Router from 'vue-router';
import { createRouter, createWebHistory } from 'vue-router';
import AppMain from '../layout/AppMain';

const routerHistory = createWebHistory();

const router = createRouter({
    history: routerHistory,
    routes: [
        {
            name: 'index',
            path: '/',
            component: AppMain,
            children: [
                {
                    path: '',
                    component: () => import('@/views/preview'),
                    name: 'preview'
                },
                {
                    path: '/javadoc',
                    component: () => import('@/views/javadoc'),
                    name: 'javadoc'
                },
                {
                    path: '/collected/:collectedId/:docId',
                    name: 'collected',
                    component: () => import('@/views/preview/collected')
                },
                {
                    path: '/workbench',
                    component: () => import('@/views/workbench')
                }
            ]
        },
        {
            name: 'login',
            path: '/login',
            component: () => import('@/views/account/login')
        },
        {
            name: 'register',
            path: '/register',
            component: () => import('@/views/account/register')
        },
        {
            name: 'workbench_editor',
            path: '/workbench/editor/:collectedId/:docId/:parentDocId/:copyDocId',
            component: () => import('@/views/workbench/editor')
        },
        {
            name: 'docfocus',
            path: '/docfocus/:docId',
            component: () => import('@/views/preview/docfocus')
        }
    ]
});

export default router;
