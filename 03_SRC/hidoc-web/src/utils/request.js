import axios from 'axios';
import { ElMessageBox, ElMessage } from 'element-plus';
import store from '@/store';
import { getToken } from '@/utils/auth';
import { config } from '@/utils/config';
import router from '@/router';
// import config from '../../public/config.json';

// create an axios instance
const service = axios.create({
    baseURL: config().baseServer, // url = base url + request url
    // withCredentials: true, // send cookies when cross-domain requests
    timeout: 120 * 1000 // request timeout
});

// request interceptor
service.interceptors.request.use(
    config => {
        // do something before request is sent

        if (store.getters.token) {
            // let each request carry token
            // ['Access-Token'] is a custom headers key
            // please modify it according to the actual situation
            config.headers['Access-Token'] = getToken();
        }
        config.headers['Point-Id'] = localStorage.getItem('pointId');
        config.headers['Access-Origin'] = window.location.origin;
        return config;
    },
    error => {
        // do something with request error
        console.log(error); // for debug
        return Promise.reject(error);
    }
);

// response interceptor
service.interceptors.response.use(
    /**
     * If you want to get http information such as headers or status
     * Please return  response => response
     */

    /**
     * Determine the request status by custom code
     * Here is just an example
     * You can also judge the status by HTTP Status Code
     */
    response => {
        const res = response.data;

        // 是否必须登录才能访问数据
        // 目前是为了在外网访问时，前端要求登录，数据仍可访问并返回，但是前端不予展示
        // 后续完善组织或团队的概念后，可由后端进行数据保护
        if (config().mastLogin && (store.getters.token == undefined || store.getters.token == '')) {
            debugger;
            var url = response.config.url;

            // 登录、重置密码、注册，不受登录限制，可以正常提交进行
            switch (url) {
                case '/user/login':
                case '/user/register':
                case '/openapi/authcode/getForRegister':
                    {
                        console.log('正在登录/注册');
                    }
                    break;
                case '/user/resetPassword':
                case '/openapi/authcode/getForResetPassword':
                    {
                        console.log('正在重置密码');
                    }
                    break;
                case '/openapi/heartbeat/base':
                    {
                        console.log('心跳信息');
                    }
                    break;
                default: {
                    // debugger;
                    ElMessageBox.confirm('系统已设置为登录访问，请先登录账号', '请登录', {
                        confirmButtonText: '马上登录',
                        cancelButtonText: '短暂停留',
                        type: 'warning'
                    }).then(() => {
                        // router.push({ path: `/login?redirect=${this.$route.path}`, params: {} });
                        router.push(`/login?redirect=` + location.pathname);
                    });
                    return Promise.reject(new Error(''));
                }
            }
        }

        // if the custom code is not 20000, it is judged as an error.
        if (res.code !== 0) {
            // 10 您的账号已登出或状态过期，请重新登录
            // 11 您访问的内容需要登录才能查看，请先登录系统
            // 12 您正在访问未经授权的内容，具体请联系内容发布方
            switch (res.code) {
                case 10:
                case 11:
                    {
                        // 需要登录才能查看的内容，直接跳转到登录页面
                        store.dispatch('user/resetToken').then(() => {
                            // location.reload();
                            router.push(`/login?redirect=` + location.pathname);
                        });
                    }
                    break;
                case 12:
                    {
                        // http://localhost:8080/collected/262975768051580929/275951135947227136
                        router.push(`/unauthorized`);

                        // 提示信息，可自行选择是否登录
                        // ElMessageBox.confirm(res.msg, '登出确认', {
                        //     confirmButtonText: '重新登录',
                        //     cancelButtonText: '短暂停留',
                        //     type: 'warning'
                        // }).then(() => {
                        //     // router.push({ path: `/login?redirect=${redirect}`, params: {} });
                        //     store.dispatch('user/resetToken').then(() => {
                        //         // location.reload();
                        //         router.push(`/login?redirect=` + location.pathname);
                        //     });
                        // });
                    }
                    break;
                default:
                    {
                        // 仅提示信息
                        ElMessage({
                            message: res.msg || 'Error',
                            type: 'error',
                            duration: 5 * 1000
                        });
                    }
                    break;
            }
            console.log(`请求回复异常：${res.message}`);
            return Promise.reject(new Error(res.message || 'Error'));
        } else {
            return res;
        }
    },
    error => {
        console.log('err' + error); // for debug
        ElMessage({
            message: error.message,
            type: 'error',
            duration: 5 * 1000
        });
        return Promise.reject(error);
    }
);

export default service;
