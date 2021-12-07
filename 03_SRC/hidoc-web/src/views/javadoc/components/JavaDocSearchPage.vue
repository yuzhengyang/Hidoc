<template>
    <!-- 内容区域 -->
    <div style="position:fixed; z-index: 99999; top:70px; right:30px;">
        <el-row style="text-align:right;background-color:#FFF;border-radius: 4px;">
            <el-col :span="4">
                <el-select v-model="searchMode" placeholder="全部">
                    <el-option label="全部" value="all"></el-option>
                    <el-option label="方法" value="method"></el-option>
                    <el-option label="类" value="class"></el-option>
                </el-select>
            </el-col>
            <el-col :span="6">
                <el-input v-model="searchName" placeholder="类名、方法名" class="input-with-select" @keydown="searchEnter" clearable />
            </el-col>
            <el-col :span="12">
                <el-input v-model="searchText" placeholder="文本内容" class="input-with-select" @keydown="searchEnter" clearable />
            </el-col>
            <el-col :span="2">
                <el-button type="success" @click="search()" style="height:40px">
                    <el-icon><Search /></el-icon>
                </el-button>
            </el-col>
        </el-row>
    </div>
    <el-main>
        <el-container style="height:100%" v-if="pageMode == 'search'">
            <el-main>
                <el-row>
                    <el-col :span="24">
                        <el-row v-for="item in javadocItem" :key="item">
                            <el-col :span="1"></el-col>
                            <el-col :span="22">
                                <java-doc-item-card v-bind:data="item"></java-doc-item-card>
                            </el-col>
                        </el-row>
                        <el-backtop></el-backtop>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>
    </el-main>
</template>

<script>
import { ElMessageBox, ElMessage } from 'element-plus';
import { Search, Share, Guide } from '@element-plus/icons';
import JavaDocItemCard from './JavaDocItemCard';
import request from '../../../utils/request.js';
export default {
    data() {
        return {
            searchText: '',
            searchName: '',
            searchMode: 'all',
            javadocItem: [],
            pageMode: 'search',
            projectList: [],
            viewData: {
                currentProjectId: '',
                currentVersion: '',
                currentPackageName: '',
                currentClassId: '',
                packageList: [],
                classList: [],
                methodList: []
            }
        };
    },
    components: { JavaDocItemCard ,Search},
    mounted() {
        document.title = 'Hidoc-JavaDoc-搜索模式';
        this.getProjectList();
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
                    name: this.searchName,
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
        },
        getProjectList() {
            request({
                url: '/javadoc/projectList',
                method: 'post',
                data: { p: 'n' }
            }).then(res => {
                if (res.code == 0) {
                    this.projectList = res.data;
                }
            });
        },
        getPackageList(projectId, version) {
            this.viewData.currentProjectId = projectId;
            this.viewData.currentVersion = version;
            request({
                url: '/javadoc/packageList',
                method: 'post',
                data: {
                    projectId: this.viewData.currentProjectId,
                    version: this.viewData.currentVersion
                }
            }).then(res => {
                if (res.code == 0) {
                    this.viewData.packageList = res.data;
                }
            });
        },
        getClassList(packageName) {
            this.viewData.currentPackageName = packageName;
            request({
                url: '/javadoc/classList',
                method: 'post',
                data: {
                    projectId: this.viewData.currentProjectId,
                    version: this.viewData.currentVersion,
                    packageName: this.viewData.currentPackageName
                }
            }).then(res => {
                if (res.code == 0) {
                    this.viewData.classList = res.data;
                }
            });
        },
        getMethodList(classId) {
            this.viewData.currentClassId = classId;
            request({
                url: '/javadoc/methodList',
                method: 'post',
                data: {
                    projectId: this.viewData.currentProjectId,
                    classId: this.viewData.currentClassId,
                    version: this.viewData.currentVersion
                }
            }).then(res => {
                if (res.code == 0) {
                    this.viewData.methodList = res.data;
                }
            });
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
