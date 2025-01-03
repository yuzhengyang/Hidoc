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
                    path: '/fileshare',
                    component: () => import('@/views/fileshare'),
                    name: 'fileshare'
                },
                {
                    path: '/javadoc',
                    component: () => import('@/views/javadoc'),
                    name: 'javadoc'
                },
                {
                    path: '/ssh',
                    component: () => import('@/views/ssh'),
                    name: 'ssh'
                },
                {
                    path: '/collected/:collectedId/:docId',
                    name: 'collected',
                    component: () => import('@/views/preview/collected')
                },
                {
                    path: '/workbench',
                    component: () => import('@/views/workbench')
                },
                {
                    name: 'workbench_history',
                    path: '/workbench/history/:collectedId/:docId',
                    component: () => import('@/views/workbench/editors/history')
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
            component: () => import('@/views/workbench/editors/editor')
        },
        {
            name: 'docfocus',
            path: '/docfocus/:docId',
            component: () => import('@/views/preview/docfocus')
        },
        {
            name: 'colfocus',
            path: '/colfocus/:collectedId',
            component: () => import('@/views/preview/colfocus')
        },
        {
            name: 'unauthorized',
            path: '/unauthorized',
            component: () => import('@/views/error/unauthorized')
        },
    ]
});

export default router;
