<template>
    <el-container style="height: 100%">
        <el-aside width="260px" style="height: 100%; padding-right: 10px; border-right: 1px solid #bbb">
            <el-tree :data="this.projectList" node-key="id" default-expand-all :expand-on-click-node="false" @node-click="selectProject">
                <template #default="{ data }">
                    <span :style="{ padding: '0px', cursor: 'pointer', fontSize: '14px', marginTop: '2px', fontWeight: selectProjectList.indexOf(data.name) > -1 ? '900' : 'normal', color: selectProjectList.indexOf(data.name) > -1 ? '#409eff' : '#3c3d40' }">{{ data.name }}</span>
                </template>
            </el-tree>
        </el-aside>
        <!-- 内容区域 -->
        <el-container v-if="pageMode == 'search'">
            <el-header style="height: 40px; margin-top: -40px">
                <el-affix :offset="50">
                    <el-row style="background-color: #ffffff; border: 1px solid #d1d3d4; border-radius: 10px; padding-left: 20px; padding-right: 20px; line-height: 40px">
                        <el-col :span="2" style="padding-left: 5px">
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
                            <el-switch v-model="searchIsStruct" width="50" inline-prompt active-text="结构" inactive-text="全部" />
                        </el-col>
                        <el-col :span="1" style="padding-left: 5px">
                            <el-button type="success" @click="search()" style="height: 32px">
                                <el-icon><Search /></el-icon>
                            </el-button>
                        </el-col>
                    </el-row>
                </el-affix>
            </el-header>
            <el-main>
                <el-row>
                    <el-col :span="24">
                        <el-row v-for="item in javadocItem" :key="item">
                            <el-col :span="24">
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
import _ from 'lodash';
export default {
    data() {
        return {
            searchText: '',
            searchName: '',
            searchMode: 'all',
            searchIsStruct: true,
            javadocItem: [],
            pageMode: 'search',
            selectProjectList: [],
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
        selectProject(data) {
            if (this.selectProjectList.indexOf(data.name) > -1) {
                this.selectProjectList = this.selectProjectList.filter(item => item !== data.name);
            } else {
                this.selectProjectList.push(data.name);
            }
        },
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
                    text: this.searchText,
                    projects: this.selectProjectList,
                    isStruct: this.searchIsStruct
                }
            }).then(res => {
                if (res.code == 0) {

                    // json字符串格式化成对象
                    _(res.data).forEach(function (item) {
                        // 转换meta的字符串为对象
                        if (item.imports) item.imports = JSON.parse(item.imports);
                        if (item.params) item.params = JSON.parse(item.params);
                        if (item.throwses) item.throwses = JSON.parse(item.throwses);
                        if (item.commentLog) item.commentLog = JSON.parse(item.commentLog);
                        // 转换类信息的字符串为对象
                        if (item.javaDocClassLite && item.javaDocClassLite.commentLog) item.javaDocClassLite.commentLog = JSON.parse(item.javaDocClassLite.commentLog);
                    });

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
