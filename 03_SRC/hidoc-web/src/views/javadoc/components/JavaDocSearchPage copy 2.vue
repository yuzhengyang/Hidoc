<template>
    <el-container style="height: 100%">
        <el-aside width="280px" style="height: 100%; padding-right: 10px; border-right: 1px solid #bbb">
            阿斯蒂芬
            <!-- <el-tree :data="this.collected.docLites" node-key="id" default-expand-all :expand-on-click-node="false" @node-click="goDocPage">
                <template #default="{ data }">
                    <span v-if="length(data.title) < 35" :style="{ padding: '0px', cursor: 'pointer', fontSize: '14px', marginTop: '2px', fontWeight: data.id === this.docId ? '900' : 'normal', width: '260px', color: data.id === this.docId ? '#409eff' : '#3c3d40' }">{{ data.title }}</span>
                    <el-tooltip v-else effect="dark" :content="data.title" placement="right">
                        <span :style="{ padding: '0px', cursor: 'pointer', fontSize: '14px', marginTop: '2px', fontWeight: data.id === this.docId ? '900' : 'normal', width: '260px', color: data.id === this.docId ? '#409eff' : '#3c3d40' }">{{ data.title }}</span>
                    </el-tooltip>
                </template>
            </el-tree> -->
        </el-aside>
        <!-- 内容区域 -->
        <el-container v-if="pageMode == 'search'">
            <el-header style="height: 40px; margin-top: -40px">
                <el-affix :offset="50">
                    <el-row>
                        <el-col :offset="12" :span="12">
                            <el-row style="background-color: #ffffff; border: 1px solid #d1d3d4; border-radius: 10px; padding-left: 20px; padding-right: 20px; line-height: 40px">
                                <el-col :span="4" style="padding-left: 5px">
                                    <el-select v-model="searchMode" placeholder="全部">
                                        <el-option label="全部" value="all"></el-option>
                                        <el-option label="方法" value="method"></el-option>
                                        <el-option label="类" value="class"></el-option>
                                    </el-select>
                                </el-col>
                                <el-col :span="6" style="padding-left: 5px">
                                    <el-input v-model="searchName" placeholder="类名、方法名" class="input-with-select" @keydown="searchEnter" clearable />
                                </el-col>
                                <el-col :span="12" style="padding-left: 5px">
                                    <el-input v-model="searchText" placeholder="文本内容" class="input-with-select" @keydown="searchEnter" clearable />
                                </el-col>
                                <el-col :span="2" style="padding-left: 5px">
                                    <el-button type="success" @click="search()" style="height: 32px">
                                        <el-icon><Search /></el-icon>
                                    </el-button>
                                </el-col>
                            </el-row>
                        </el-col>
                    </el-row>
                </el-affix>
            </el-header>
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
    </el-container>
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
    components: { JavaDocItemCard, Search },
    mounted() {
        this.getProjectList();
        // this.search();
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
                url: '/openapi/javadoc/search',
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
                url: '/openapi/javadoc/projectList',
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
                url: '/openapi/javadoc/packageList',
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
                url: '/openapi/javadoc/classList',
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
                url: '/openapi/javadoc/methodList',
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
