<template>
    <el-aside width="350px" style="height:750px;">
        <div style="font-size:14px;">
            <div style="text-align:center;">工程</div>
            <div style="height:120px;overflow:hidden;overflow:scroll;">
                <el-row>
                    <el-col :span="24">
                        <el-row v-for="item in this.projectList" :key="item.id" :index="item.id" @click="getMenuList(item.id, item.version)" :style="{ padding: '5px', cursor: 'pointer', fontSize: '16px', marginTop: '2px', color: item.id === this.viewData.currentProjectId ? '#409eff' : '#606266', fontWeight: item.id === this.viewData.currentProjectId ? '900' : 'normal' }">
                            <el-col style="font-size:16px;" :span="24">{{ item.name }}</el-col>
                        </el-row>
                    </el-col>
                </el-row>
            </div>
            <div style="text-align:center;">目录</div>
            <div style="height:300px;overflow:hidden;overflow:scroll;">
                <el-row>
                    <el-col v-if="this.viewData.menuList.length > 0" :span="24">
                        <el-tree :data="this.viewData.menuList" @node-click="menuNodeClick" :expand-on-click-node="false" default-expand-all>
                            <template #default="{ data }">
                                <span :style="{ padding: '5px', cursor: 'pointer', fontSize: '16px', marginTop: '2px', fontWeight: data.label === this.viewData.currentMenuName ? '900' : 'normal', width: '260px', color: data.label === this.viewData.currentMenuName ? '#409eff' : '#606266' }">{{ data.label }}</span>
                            </template>
                        </el-tree>
                    </el-col>
                    <el-col v-else>
                        <el-empty description=""></el-empty>
                    </el-col>
                </el-row>
            </div>
            <div style="text-align:center;">类</div>
            <div style="height:260px;overflow:hidden;overflow:scroll;">
                <el-row>
                    <el-col v-if="this.viewData.classList.length > 0" :span="24">
                        <el-row v-for="item in this.viewData.classList" :key="item.id" :index="item.id" @click="getClassDetail(item.id)" :style="{ padding: '5px', cursor: 'pointer', fontSize: '16px', marginTop: '2px', color: item.id === this.viewData.currentClassId ? '#409eff' : '#606266', fontWeight: item.id === this.viewData.currentClassId ? '900' : 'normal' }">
                            <el-col style="font-size:16px;" :span="24">{{ item.commentInfo }}</el-col>
                        </el-row>
                    </el-col>
                    <el-col v-else>
                        <el-empty description=""></el-empty>
                    </el-col>
                </el-row>
            </div>
        </div>
    </el-aside>
    <el-container>
        <el-main>
            <el-row>
                <el-col :span="24">
                    <el-row v-for="item in viewData.methodList" :key="item">
                        <el-col :span="1"></el-col>
                        <el-col :span="22">
                            <java-doc-item-card v-bind:data="item"></java-doc-item-card>
                        </el-col>
                    </el-row>
                </el-col>
            </el-row>
        </el-main>
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
                currentMenuName: '',
                currentClassId: '',
                menuList: [],
                classList: [],
                methodList: []
            }
        };
    },
    components: { JavaDocItemCard },
    mounted() {
        document.title = 'Hidoc-JavaDoc-文档';
        this.getProjectList();
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
                data: { p: 'n', existMenu: true }
            }).then(res => {
                if (res.code == 0) {
                    this.projectList = res.data;
                }
            });
        },
        getMenuList(projectId, version) {
            this.viewData.menuList = [];
            this.viewData.classList = [];

            this.viewData.currentProjectId = projectId;
            this.viewData.currentVersion = version;
            request({
                url: '/openapi/javadoc/menuList',
                method: 'post',
                data: {
                    projectId: this.viewData.currentProjectId,
                    version: this.viewData.currentVersion
                }
            }).then(res => {
                if (res.code == 0) {
                    this.viewData.menuList = res.data;
                }
            });
        },
        menuNodeClick(treeNode, prop, event) {
            this.getClassList(treeNode);
        },
        getClassList(currentMenu) {
            this.viewData.classList = [];

            this.viewData.currentMenuName = currentMenu.menu;
            request({
                url: '/openapi/javadoc/classList',
                method: 'post',
                data: {
                    projectId: this.viewData.currentProjectId,
                    version: this.viewData.currentVersion,
                    menuPath: currentMenu.menuPath
                }
            }).then(res => {
                if (res.code == 0) {
                    this.viewData.classList = res.data;
                }
            });
        },
        getClassDetail(classId) {
            this.viewData.currentClassId = classId;
            request({
                url: '/openapi/javadoc/classDetail',
                method: 'post',
                data: {
                    projectId: this.viewData.currentProjectId,
                    classId: this.viewData.currentClassId,
                    version: this.viewData.currentVersion
                }
            }).then(res => {
                if (res.code == 0) {
                    this.viewData.methodList = [];
                    this.viewData.methodList.push(res.meta.classInfo);
                    this.viewData.methodList.push(...res.meta.methodList);
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
    height: 6px;
    background-color: #d8d8d8;
}

/* 滚动槽 */
::-webkit-scrollbar-track {
    border-radius: 10px;
}

::-webkit-scrollbar-thumb {
    background-color: #bfc1c4;
}

/** 树自定义样式 */
.el-tree-node__content {
    width: 280px;
    height: 40px;
    font-size: 14px;
    border-bottom: 1px dashed lightgrey;
    overflow: hidden;
    text-overflow: ellipsis;
}
</style>
