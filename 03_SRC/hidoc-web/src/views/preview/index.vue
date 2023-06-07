<template>
    <el-container>
        <el-header height="30px">
            <el-row>
                <el-col :span="16" style="text-align: left">
                    <el-button-group v-if="!(this.$store.state.user.token == undefined || this.$store.state.user.token == '')">
                        <el-button type="success" size="small" @click="searchByCollectedRole('default')" round :plain="this.collectedRole != 'default'">ALL</el-button>
                        <el-button type="primary" size="small" @click="searchByCollectedRole('myJoin')" round :plain="this.collectedRole != 'myJoin'">参与</el-button>
                        <el-button type="danger" size="small" @click="searchByCollectedRole('private')" round :plain="this.collectedRole != 'private'">私有</el-button>
                    </el-button-group>
                </el-col>
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
        <!-- <h2>当前求和为：{{ sum }}</h2>
        <button @click="sum++">点击按钮sum +1</button> -->
    </el-container>
</template>

<script>
import { ref, watch } from 'vue';
import { ElMessage } from 'element-plus';
import DocCollectedCard from './components/DocCollectedCard';
import request from '../../utils/request.js';
import { Search, Share, Guide } from '@element-plus/icons';
export default {
    data() {
        return {
            collectedList: [],
            searchText: '',
            searchMode: '',
            collectedRole: 'default'
        };
    },
    setup() {
        // 数据
        let sum = ref(0);

        //监视属性
        watch(sum, (newValue, oldValue) => {
            // 回调函数形式
            console.log('求和的值变了', '变化后的值是' + newValue, '变化前的值是' + oldValue);
        });
        //返回对象
        return {
            sum
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
        searchByCollectedRole(collectedRole) {
            this.collectedRole = collectedRole;
            this.search();
        },
        search() {
            console.log('搜索 ' + this.searchMode + ' ' + this.searchText);
            request({
                url: '/collected/preview',
                method: 'post',
                data: { mode: this.searchMode, keyword: this.searchText, collectedRole: this.collectedRole }
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
