<template>
    <el-container>
        <el-header>
            <el-row>
                <el-col :span="16">
                    <el-button-group>
                        <el-button type="primary" plain @click="gotoPage('preview')">文集</el-button>
                        <el-button type="primary" plain @click="gotoPage('')">JavaDoc</el-button>
                    </el-button-group>
                </el-col>
                <el-col :span="8" style="text-align:right;">
                    <el-input v-model="searchText" placeholder="搜索一下" class="input-with-select">
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
                        <el-col :span="24" style="background-color:#f00"></el-col>
                    </el-row>
                </el-col>
            </el-row>
        </el-main>
    </el-container>
</template>

<script>
import { Search, Share, Guide } from '@element-plus/icons';
import request from '../../utils/request.js';
export default {
    data() {
        return {
            searchText: '',
            searchMode: ''
        };
    },
    components: { Search },
    mounted() {
        document.title = 'Hidoc-JavaDoc';
        this.search();
    },
    methods: {
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
                    console.log(res);
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