<template>
    <el-container>
        <!-- 内容区域 -->
        <el-main>
            <el-row>
                <el-col :span="24">
                    <el-row>
                        <el-col :span="8" v-for="item in collectedList" :key="item">
                            <doc-collected-card v-bind:data="item"></doc-collected-card>
                        </el-col>
                    </el-row>

                    <el-backtop></el-backtop>
                </el-col>
            </el-row>
        </el-main>
    </el-container>
</template>

<script>
import { ElMessage } from 'element-plus';
import DocCollectedCard from './components/DocCollectedCard';
import request from '../../utils/request.js';
export default {
    name: 'Home',
    data() {
        return {
            keyword: '',
            collectedList: []
        };
    },
    mounted() {
        // debugger;
        let token = this.$store.state.user.token;
        console.log('token: ' + token);

        return request({
            url: '/collected/preview',
            method: 'post',
            data: {}
        }).then(res => {
            if (res.code == 0) {
                this.collectedList = res.data;
            }
        });
    },
    components: { DocCollectedCard },
    methods: {
        search() {
            // alert(base_server + '搜索内容~' + this.keyword);
        },
        register() {
            this.$router.push({ path: '/register', params: {} });
        },
        login() {
            this.$router.push({ path: '/login', params: {} });
        },
        async logout() {
            await this.$store.dispatch('user/logout');
            this.$router.push(`/login?redirect=${this.$route.fullPath}`);
        }
    }
};
</script>

<style>
.el-main {
    /* background-color: #e9eef3; */
    /* color: #333; */
    text-align: left;
}
.el-card {
    margin: 10px;
}
</style>