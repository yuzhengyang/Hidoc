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
            var url = response.config.url;

            // 登录、重置密码、注册，不受登录限制，可以正常提交进行
            if (url == '/user/login' || url == '/user/resetPassword' || url == '/openapi/authcode/getForResetPassword' || url == '/user/register' || url == '/openapi/authcode/getForRegister') {
                console.log('登录或注册中...');
            } else {
                ElMessageBox.confirm('请先登录再访问内容', '请登录', {
                    confirmButtonText: '马上登录',
                    cancelButtonText: '短暂停留',
                    type: 'warning'
                }).then(() => {
                    router.push({ path: '/login', params: {} });
                    // 这里把地址暂存住，方便登录之后再跳转回来
                    localStorage.setItem('loginJumpAddr', window.location.href);
                });
                return Promise.reject(new Error(''));
            }
        }

        // if the custom code is not 20000, it is judged as an error.
        if (res.code !== 0) {
            ElMessage({
                message: res.msg || 'Error',
                type: 'error',
                duration: 5 * 1000
            });

            // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
            if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
                // to re-login
                ElMessageBox.confirm('您的账号已登出，你可以选择保持在当前页面，或者选择重新登录系统。', '登出确认', {
                    confirmButtonText: '重新登录',
                    cancelButtonText: '短暂停留',
                    type: 'warning'
                }).then(() => {
                    store.dispatch('user/resetToken').then(() => {
                        location.reload();
                    });
                });
            }
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
