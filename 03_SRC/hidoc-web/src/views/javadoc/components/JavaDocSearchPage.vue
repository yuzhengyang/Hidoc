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
                            <el-button type="danger" @click="showHelpTour()" link size="small">帮助</el-button>
                        </el-col>
                    </el-row>
                </el-affix>
            </el-header>
            <el-main v-loading="loading" ref="metaBody">
                <el-row>
                    <el-col v-if="helpTour" :span="24">
                        <!-- =================================================================================================================== -->
                        <!-- 这里是展示帮助信息的演示卡片 -->
                        <!-- 新的卡片设计 -->
                        <div style="padding: 20px; border: 2px solid #aaf; border-radius: 20px; margin: 20px 21px 20px 21px">
                            <el-row>
                                <el-col :span="2">
                                    <div style="background-color: lightgrey; border-radius: 0px; font-size: 14px; width: 60px; height: 32px; text-align: center; line-height: 32px">
                                        <span id="metaType" style="font-weight: bold; color: dodgerblue">类</span>
                                    </div>
                                </el-col>
                                <el-col :span="16" style="line-height: 32px; text-align: left">
                                    <el-tag id="metaIsDeprecated" type="error" style="margin: 5px" effect="dark">废弃</el-tag>
                                    <el-tag id="metaQualifier" type="success" style="margin: 5px" effect="dark">public static</el-tag>
                                    <span id="metaClassName" style="font-size: 14px; color: #333; cursor: pointer; font-weight: bold; color: blue">className</span>
                                    <span style="font-size: 14px; color: #333; font-weight: bold; color: blue; padding-left: 10px; padding-right: 10px">.</span>
                                    <span id="metaMethodName" style="font-size: 14px; color: #333; font-weight: bold">methodName</span>
                                    <CopyDocument id="metaNameCopy" style="width: 1em; height: 1em; margin-left: 10px; margin-right: 10px; cursor: pointer" />
                                    <el-rate id="metaHelpfulRate" v-model="CONST_FIVE" />
                                </el-col>
                                <el-col :span="6">
                                    <div id="metaProjectName" style="text-align: right; font-size: 14px; font-weight: bold">projectName</div>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="19" id="metaCommentInfo" style="margin-top: 10px; font-size: 14px">概述基本信息</el-col>
                                <el-col :span="5">
                                    <div style="float: right">
                                        <el-button id="metaExp" type="primary" size="small" round>示例</el-button>
                                        <el-button id="metaLog" type="warning" size="small" round>日志</el-button>
                                        <el-button id="metaCode" type="danger" size="small" round>Code</el-button>
                                    </div>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="24" style="margin: 5px; padding: 5px"></el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="24" id="metaCommentScene" style="margin-left: 5px; padding-left: 5px; color: blue">🎬️ 场景：描述什么时候可以使用</el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="10" id="metaParams" style="border: 2px dotted #aaa; border-radius: 10px; margin: 5px; padding: 10px; font-size: 14px">
                                    <p>
                                        ➡️
                                        <el-text type="primary">String</el-text>
                                        :
                                        <el-text type="warning">单据号</el-text>
                                    </p>
                                    <p>
                                        ➡️
                                        <el-text type="primary">Date</el-text>
                                        :
                                        <el-text type="warning">记账时间</el-text>
                                    </p>
                                </el-col>
                                <el-col :span="1" style="margin: 5px; padding: 5px"></el-col>
                                <el-col :span="11" id="metaReturn" style="border: 2px dotted #aaa; border-radius: 10px; margin: 5px; padding: 10px; font-size: 14px">
                                    <p>
                                        ↩️
                                        <el-text type="primary">bool</el-text>
                                        :
                                        <el-text type="warning">操作成功标志</el-text>
                                    </p>
                                    <p>
                                        🐞
                                        <el-text type="primary">IOException</el-text>
                                        :
                                        <el-text type="warning">文件流处理要注意IO异常</el-text>
                                    </p>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="24" id="metaCommentLimit" style="margin-left: 5px; padding-left: 5px; color: red">⛔️ 限制：描述不能使用的情况或需注意的事项</el-col>
                            </el-row>
                            <!-- 关键字 -->
                            <el-row>
                                <el-col :span="24">
                                    <el-tag id="metaKeyword" type="warning" style="margin: 5px">单据记账</el-tag>
                                    <el-tag id="metaKeyword" type="warning" style="margin: 5px">数据查询</el-tag>
                                </el-col>
                            </el-row>
                            <el-row style="margin-top: 5px">
                                <el-col :span="12" style="text-align: left">
                                    <span id="metaCreateTime" style="color: grey; font-size: 12px">2024-01-01 09:00:00 刷新</span>
                                </el-col>
                            </el-row>
                        </div>
                        <!-- =================================================================================================================== -->
                    </el-col>
                    <el-col v-else :span="24">
                        <!-- =================================================================================================================== -->
                        <!-- 这里展示的是正常查询出来的数据卡片 -->
                        <el-row v-for="item in javadocItem" :key="item">
                            <el-col :span="24">
                                <java-doc-item-card v-bind:data="item"></java-doc-item-card>
                            </el-col>
                        </el-row>
                        <el-backtop></el-backtop>
                        <!-- =================================================================================================================== -->
                    </el-col>
                </el-row>
            </el-main>
        </el-container>
    </el-container>
    <el-tour v-model="helpTour">
        <el-tour-step :target="$refs.projectTree?.$el" title="🏢 工程列表" placement="right">
            <div>您可以在这里点击某些工程，来筛选从哪些工程查询信息</div>
            <div>默认不选择，会查询有权限的所有工程</div>
        </el-tour-step>
        <el-tour-step :target="$refs.searchName?.$el" title="📁 搜索类或方法" description="在这里可以输入类名或方法名，可以较为精准的定位到需要的信息" />
        <el-tour-step :target="$refs.searchText?.$el" title="📄 搜索注释内容" description="您可以输入文本，进行注释内容的查询，多个词用空格隔开，结果会以高亮显示" placement="bottom-end" />
        <el-tour-step :target="$refs.searchIsStruct?.$el" title="📰 只搜索结构化后的注释吗？" placement="bottom-end">
            <p>结构化的注释，指的是注释中，包含至少一种描述，如：#场景、#限制、#关键字</p>
            <p>您也可以切换到从全部注释中搜索，这样搜索范围较大</p>
        </el-tour-step>
        <el-tour-step :target="$refs.searchIsDeprecated?.$el" title="⛓️‍💥 要搜索废弃的类或方法？" placement="bottom-end">
            <p>通常我们会使用 @Deprecated 注解，来标识淘汰的类或方法</p>
            <p>这里默认搜索会排除掉淘汰的类或方法，如果需要查询他们，可以切换到搜索全部信息</p>
        </el-tour-step>
        <el-tour-step :target="$refs.searchButton?.$el" title="🔍️ 搜索" description="开始搜索吧，或者在文本框输入信息后，直接按 回车（Enter）看看" placement="bottom-end" />
        <el-tour-step target="#metaType" title="🔨 类还是方法" description="这里标识内容是类还是代码，会根据不同类型展示不同内容" placement="bottom" />
        <el-tour-step target="#metaIsDeprecated" title="🚫 淘汰的类或方法" description="通常不建议使用淘汰的代码，可以根据注释提示，换用新的方法" placement="bottom" />
        <el-tour-step target="#metaQualifier" title="🌻 修饰符" description="代码修饰符，常见的有：public、private、static 等" placement="bottom" />
        <el-tour-step target="#metaClassName" title="🔖 类名称" description="这里展示类的名称，可以通过类名称到代码中找到准确位置" placement="bottom" />
        <el-tour-step target="#metaMethodName" title="🏷️ 方法名称" description="这里是方法名称，可以通过他查找代码里更详细的内容" placement="bottom" />
        <el-tour-step target="#metaNameCopy" title="📋️ 复制" description="点击这里可以复制类名和方法名，格式如：className.methodName" placement="bottom" />
        <el-tour-step target="#metaHelpfulRate" title="⭐️ 推荐" description="这里可以查看大家对代码的推荐程度，您也可以登录进行推荐打分" placement="bottom" />
        <el-tour-step target="#metaProjectName" title="🏗️ 工程名称" description="指代码来源于哪个工程，追溯时可有依据" placement="bottom-end" />
        <el-tour-step target="#metaCommentInfo" title="🐵 基本描述" description="注释的基本描述，通常是第一行注释信息" placement="bottom" />
        <el-tour-step target="#metaExp" title="👌 说明示例" description="这里可以查看在注释中写的示例，示例可以支持 html、markdown 格式展示" placement="bottom-end" />
        <el-tour-step target="#metaLog" title="📅 修改日志" description="按照开发规范，我们需要在代码类上方增加日志信息，这里可以展示结构化的日志信息" placement="bottom-end" />
        <el-tour-step target="#metaCode" title="👾 查看源码" description="如果需要立即查看源码，可以点击这里，注意此功能需要登录使用" placement="bottom-end" />
        <el-tour-step target="#metaCommentScene" title="🎬 适用场景" description="每段代码都有其适用的场景，可以参考这里判断能不能在你的代码中复用" placement="bottom" />
        <el-tour-step target="#metaParams" title="➡️ 传入参数" description="调用方法时，可能需要传入的参数，这里包括类型和说明信息" placement="bottom" />
        <el-tour-step target="#metaReturn" title="↩️ 返回值 及 🐞 异常" placement="bottom">
            <p>↩️ 返回值：类型和数据是不是你需要的</p>
            <p>🐞 异常：您需要注意识别和应对，避免突发状况</p>
        </el-tour-step>
        <el-tour-step target="#metaCommentLimit" title="⛔️ 使用限制" description="有些方法可能存在一些限定条件，为避免影响，请关注使用限制" placement="bottom" />
        <el-tour-step target="#metaKeyword" title="🗝️ 关键字" description="同类型的代码，可以使用关键字来区分，也能更好的帮助大家来搜索" placement="bottom" />
        <el-tour-step target="#metaCreateTime" title="🕙 刷新时间" placement="bottom" style="width: 500px">
            <p>代码注释每小时都会自动更新到这里</p>
        </el-tour-step>
        <el-tour-step :target="ref3?.$el" title="🎉 完成学习" content-style="width:800">
            <p>恭喜你，完成了基本操作步骤的学习，现在可以开始使用了~</p>
            <p>另外：</p>
            <p>如果您有更好的见解，可以在代码注释那里做修改，示例如下：</p>
            <v-md-editor v-model="this.helpTourCommentExp" mode="preview" />
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
            CONST_FIVE: 5,
            helpTourCommentExp: `
\`\`\` java
/**
 * 根据主键列表查询数据
 * #场景 批量记账、批量审核等场景，可以根据前端传入id批量查询
 * #限制 不能用于联合主键的表
 * #关键字 批量查询
 *
 * @param iDataAdapter 数据连接
 * @param idList 主键列表
 * @return 平台表对象
 */
public DataTable selectByIds(IDataAdapter iDataAdapter, List idList)
\`\`\`
            `,
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
        },
        showHelpTour() {
            this.helpTour = true;
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
