<template>
    <el-container style="height: 100%">
        <el-header style="height: 40px">
            <el-affix :offset="50">
                <el-button-group style="z-index: 999;">
                    <el-button type="primary" size="default" plain @click="pageMode = 'search'">搜索</el-button>
                    <el-button type="primary" size="default" plain @click="pageMode = 'file'">目录</el-button>
                    <el-button type="primary" size="default" plain @click="pageMode = 'view'">浏览</el-button>
                    <el-button type="primary" size="default" plain @click="pageMode = 'tag'">TAG</el-button>
                </el-button-group>
            </el-affix>
        </el-header>
        <!-- 内容区域 -->
        <el-container style="height: calc(100% - 40px)">
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
        document.title = 'Hidoc-代码注释';
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
    width: 4px;
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
