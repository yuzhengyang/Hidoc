<template>
    <el-container>
        <el-header height="40px">
            <el-affix :offset="70">
                <!--  style="background-color:#FFF;" -->
                <el-row>
                    <el-col :span="16">
                        <el-button-group>
                            <el-button type="primary" plain @click="gotoPage('preview')">文集</el-button>
                            <el-button type="primary" plain @click="gotoPage('')">JavaDoc</el-button>
                        </el-button-group>
                    </el-col>
                    <el-col :span="8" style="text-align:right;">
                        <el-input v-model="searchText" placeholder="搜索一下" class="input-with-select" @keydown="searchEnter">
                            <template #prepend>
                                <el-select v-model="searchMode" placeholder="全部" style="width: 80px">
                                    <el-option label="全部" value="all"></el-option>
                                    <el-option label="方法" value="method"></el-option>
                                    <el-option label="类" value="class"></el-option>
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
            </el-affix>
        </el-header>
        <!-- 内容区域 -->
        <el-main>
            <el-row>
                <el-col :span="24">
                    <el-row v-for="item in javadocItem" :key="item">
                        <el-col :span="1"></el-col>
                        <el-col :span="22">
                            <java-doc-item-card v-bind:data="item"></java-doc-item-card>
                        </el-col>
                    </el-row>
                    <!-- <el-backtop></el-backtop> -->
                </el-col>
            </el-row>
        </el-main>
    </el-container>
</template>

<script>
import { ElMessageBox, ElMessage } from 'element-plus';
import { Search, Share, Guide } from '@element-plus/icons';
import JavaDocItemCard from './components/JavaDocItemCard';
import request from '../../utils/request.js';
export default {
    data() {
        return {
            searchText: '',
            searchMode: 'all',
            javadocItem: []
        };
    },
    components: { JavaDocItemCard, Search },
    mounted() {
        document.title = 'Hidoc-JavaDoc';
        this.search();
    },
    methods: {
        searchEnter(e) {
            if (e.keyCode == 13) {
                this.search();
            }
        },
        search() {
            console.log('搜索 ' + this.searchMode + ' ' + this.searchText);
            request({
                url: '/javadoc/search',
                method: 'post',
                data: {
                    mode: this.searchMode,
                    text: this.searchText
                }
            }).then(res => {
                if (res.code == 0) {
                    this.javadocItem = res.data;
                    ElMessage({
                        message: '搜索完成',
                        type: 'success',
                        duration: 1 * 1000
                    });
                }
            });
        },
        gotoPage(page) {
            if (page == 'preview') {
                let routeData = this.$router.resolve({
                    name: 'preview',
                    params: {}
                });
                window.open(routeData.path, '_blank');
            }
            if (page == 'javadoc') {
                let routeData = this.$router.resolve({
                    name: 'javadoc',
                    params: {}
                });
                window.open(routeData.path, '_blank');
            }
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
