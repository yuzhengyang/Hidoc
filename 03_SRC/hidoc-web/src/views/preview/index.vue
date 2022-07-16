<template>
    <el-container>
        <el-header height="30px">
            <el-row>
                <el-col :span="16"></el-col>
                <el-col :span="8" style="text-align: right">
                    <el-input v-model="searchText" placeholder="搜索一下" class="input-with-select" @keydown="searchEnter" clearable>
                        <template #prepend>
                            <el-select v-model="searchMode" placeholder="Select" style="width: 110px">
                                <el-option label="全文" value="1"></el-option>
                                <el-option label="关键字" value="2"></el-option>
                            </el-select>
                        </template>
                        <template #append>
                            <el-button @click="search()">
                                <el-icon>
                                    <Search />
                                </el-icon>
                            </el-button>
                        </template>
                    </el-input>
                </el-col>
            </el-row>
        </el-header>
        <!-- 内容区域 -->
        <el-main>
            <el-row>
                <el-col :span="24">
                    <el-row>
                        <el-col :span="6" v-for="item in collectedList" :key="item">
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
import { Search, Share, Guide } from '@element-plus/icons';
export default {
    data() {
        return {
            collectedList: [],
            searchText: '',
            searchMode: ''
        };
    },
    mounted() {
        document.title = 'Hidoc-首页';
        // debugger;
        let token = this.$store.state.user.token;
        console.log('token: ' + token);
        this.search();
    },
    components: { DocCollectedCard, Search },
    methods: {
        searchEnter(e) {
            if (e.keyCode == 13) {
                this.search();
            }
        },
        search() {
            console.log('搜索 ' + this.searchMode + ' ' + this.searchText);
            request({
                url: '/collected/preview',
                method: 'post',
                data: { mode: this.searchMode, keyword: this.searchText }
            }).then(res => {
                if (res.code == 0) {
                    this.collectedList = res.data;
                }
            });
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
</style>
