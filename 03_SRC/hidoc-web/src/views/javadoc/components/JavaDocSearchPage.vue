<template>
    <el-container style="height: 100%">
        <el-aside ref="projectTree" width="260px" style="height: 100%; padding-right: 10px; border-right: 1px solid #bbb">
            <el-tree :data="this.projectList" node-key="id" default-expand-all :expand-on-click-node="false" @node-click="selectProject">
                <template #default="{ data }">
                    <span :style="{ padding: '0px', cursor: 'pointer', fontSize: '14px', marginTop: '2px', fontWeight: selectProjectList.indexOf(data.name) > -1 ? '900' : 'normal', color: selectProjectList.indexOf(data.name) > -1 ? '#409eff' : '#3c3d40' }">
                        {{ selectProjectList.indexOf(data.name) > -1 ? '😀 ' + data.name : data.name }}
                    </span>
                </template>
            </el-tree>
        </el-aside>
        <!-- 内容区域 -->
        <el-container v-if="pageMode == 'search'">
            <el-header style="height: 40px; margin-top: -40px">
                <el-affix :offset="50">
                    <el-row style="background-color: #ffffff; border: 1px solid #d1d3d4; border-radius: 10px; padding-left: 20px; padding-right: 20px; line-height: 40px">
                        <!-- <el-col :span="2" style="padding-left: 5px">
                            <el-select v-model="searchMode" placeholder="全部">
                                <el-option label="全部" value="all"></el-option>
                                <el-option label="方法" value="method"></el-option>
                                <el-option label="类" value="class"></el-option>
                            </el-select>
                        </el-col> -->
                        <el-col :span="6" style="padding-left: 5px">
                            <el-input ref="searchName" v-model="searchName" placeholder="类名、方法名" class="input-with-select" @keydown="searchEnter" clearable />
                        </el-col>
                        <el-col :span="10" style="padding-left: 5px">
                            <el-input ref="searchText" v-model="searchText" placeholder="文本内容" class="input-with-select" @keydown="searchEnter" clearable />
                        </el-col>
                        <el-col :span="2" style="padding-left: 10px">
                            <el-tooltip placement="bottom" effect="light">
                                <template #content>
                                    <p>结构：#场景、#限制、#关键字 至少包括一种的描述</p>
                                    <p>全部：查询所有数据（包括不带描述的数据）</p>
                                </template>
                                <el-switch ref="searchIsStruct" v-model="searchIsStruct" width="50" inline-prompt active-text="结构" inactive-text="全部" />
                            </el-tooltip>
                        </el-col>
                        <el-col :span="2" style="padding-left: 5px">
                            <el-tooltip placement="bottom" effect="light">
                                <template #content>
                                    <p>正常：查询不包括废弃的数据</p>
                                    <p>全部：查询所有数据（不区分是否废弃）</p>
                                </template>
                                <el-switch ref="searchIsDeprecated" v-model="searchIsDeprecated" width="50" inline-prompt active-text="全部" inactive-text="正常" />
                            </el-tooltip>
                        </el-col>
                        <el-col :span="1"></el-col>
                        <el-col :span="1" style="padding-left: 5px">
                            <el-button ref="searchButton" type="success" @click="search()" style="height: 32px">
                                <el-icon><Search /></el-icon>
                            </el-button>
                        </el-col>
                        <el-col :span="1"></el-col>
                        <el-col :span="1">
                            <el-button type="danger" @click="helpTour = true" link size="small">帮助</el-button>
                        </el-col>
                    </el-row>
                </el-affix>
            </el-header>
            <el-main v-loading="loading" ref="metaBody">
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
    <el-tour v-model="helpTour">
        <el-tour-step :target="$refs.projectTree?.$el" title="🏷️ 工程列表" placement="right">
            <div>您可以在这里点击某些工程，来筛选从哪些工程查询信息</div>
        </el-tour-step>
        <el-tour-step :target="$refs.searchName?.$el" title="📁 搜索类或方法" description="在这里可以输入类名或方法名，可以较为精准的定位到需要的信息" />
        <el-tour-step :target="$refs.searchText?.$el" title="📄 搜索注释内容" description="您可以输入文本，进行注释内容的查询，多个词用空格隔开，结果会以高亮显示" placement="bottom-end" />
        <el-tour-step :target="$refs.searchIsStruct?.$el" title="📰 只搜索结构化后的注释吗？" placement="bottom-end">
            <p>结构化的注释，指的是注释中，包含至少一种描述，如：#场景、#限制、#关键字</p>
            <p>您也可以从全部注释中搜索，这样搜索会比较慢，并且准确度较低</p>
        </el-tour-step>
        <el-tour-step :target="$refs.searchIsDeprecated?.$el" title="⛓️‍💥 要搜索废弃的类或方法？" placement="bottom-end">
            <p>通常我们会使用 @Deprecated 注解，来标识淘汰的类或方法</p>
            <p>这里默认搜索会排除掉淘汰的类或方法，如果需要查询他们，可以切换到搜索全部信息</p>
        </el-tour-step>
        <el-tour-step :target="$refs.searchButton?.$el" title="🔍️ 搜索" description="开始搜索吧，或者在文本框输入信息后，直接按 回车（Enter）看看" placement="bottom-end" />
        <el-tour-step :target="ref3?.$el" title="🎉 完成学习">
            <p>恭喜你，完成了基本操作步骤的学习，现在可以开始使用了~</p>
            <p></p>
            <p>注意~ 注意~</p>
            <p>如果您对注释有更好的见解，可以修改代码注释</p>
            <p>代码注释每小时都会自动更新到这里哟~</p>
        </el-tour-step>
    </el-tour>
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
            helpTour: false,
            loading: false,
            searchText: '',
            searchName: '',
            searchMode: 'all',
            searchIsStruct: true,
            searchIsDeprecated: false,
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
            if (this.loading) return;
            this.loading = true;
            const timer = setTimeout(this.doSearch, 250);
        },
        doSearch() {
            console.log('搜索 ' + this.searchMode + ' ' + this.searchText);
            request({
                url: '/openapi/javadoc/search',
                method: 'post',
                data: {
                    mode: this.searchMode,
                    name: this.searchName,
                    text: this.searchText,
                    projects: this.selectProjectList,
                    isStruct: this.searchIsStruct,
                    isDeprecated: this.searchIsDeprecated
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
                this.loading = false;
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
