<template>
    <div id="root">
        <el-container id="app-container" class="app-container">
            <router-view />
        </el-container>
        <!-- <el-backtop></el-backtop> -->
    </div>
</template>

<script>
import request from './utils/request.js';
export default {
    name: 'App',
    components: {},
    data() {
        return {
            heartbeatTimer: ''
        };
    },
    mounted() {
        console.log('app mounted');
        let token = this.$store.state.user.token;
        // this.heartbeatTimer = setInterval(this.heartbeat, 120 * 1000);
        // clearInterval(this.heartbeatTimer);
        this.heartbeat();
    },
    methods: {
        handleOpen(key, keyPath) {
            console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        },
        handleChange(val) {
            console.log(val);
        },
        handleClick(tab, event) {
            console.log(tab, event);
        },
        search() {
            // alert(base_server + '搜索内容~' + this.keyword);
        },
        home() {
            this.$router.push({ path: '/', params: {} });
        },
        register() {
            this.$router.push({ path: '/biz/register', params: {} });
        },
        login() {
            this.$router.push({ path: '/biz/login', params: {} });
        },
        async logout() {
            await this.$store.dispatch('user/logout');
            this.$router.push(`/login?redirect=${this.$route.fullPath}`);
        },
        heartbeat() {
            return request({
                url: '/openapi/heartbeat/base',
                method: 'post',
                data: {
                    refresh: 'y'
                }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                }
            });
        }
    }
};
</script>

<style>
html,
body,
#app,
#root {
    height: 100%;
    margin: 0;
    padding: 0;
}
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
    /* text-align: center; */
    /* margin-top: 60px; */
}
.app-container {
    height: 100%;
}
</style>
