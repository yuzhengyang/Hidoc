<template>
    <el-container style="height:100%">
        <el-header height="40px">
            <el-affix :offset="70">
                <el-row>
                    <el-col :span="8">
                        <el-button-group>
                            <el-button type="primary" plain @click="pageMode = 'search'">搜索模式</el-button>
                            <el-button type="primary" plain @click="pageMode = 'file'">文档模式</el-button>
                            <el-button type="primary" plain @click="pageMode = 'view'">浏览模式</el-button>
                        </el-button-group>
                    </el-col>
                    <el-col :span="4"></el-col>
                    <el-col :span="12"></el-col>
                </el-row>
            </el-affix>
        </el-header>
        <!-- 内容区域 -->
        <el-container style="height:95%">
            <java-doc-search-page v-if="pageMode == 'search'" />
            <java-doc-file-page v-else-if="pageMode == 'file'" />
            <java-doc-view-page v-else />
        </el-container>
    </el-container>
</template>

<script>
import { ElMessageBox, ElMessage } from 'element-plus';
import { Search, Share, Guide } from '@element-plus/icons';
import JavaDocViewPage from './components/JavaDocViewPage';
import JavaDocSearchPage from './components/JavaDocSearchPage';
import JavaDocFilePage from './components/JavaDocFilePage';
import request from '../../utils/request.js';
export default {
    data() {
        return {
            pageMode: 'search'
        };
    },
    components: { JavaDocSearchPage, JavaDocViewPage, JavaDocFilePage },
    mounted() {
        document.title = 'Hidoc-JavaDoc';
    },
    methods: {}
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

::-webkit-scrollbar {
    width: 6px;
    background-color: #d8d8d8;
}

/* 滚动槽 */
::-webkit-scrollbar-track {
    border-radius: 10px;
}

::-webkit-scrollbar-thumb {
    background-color: #bfc1c4;
}
</style>
