<template>
    <el-container style="height:100%">
        <el-aside width="300px" style="height:800px;">
            <el-row>
                <el-col :span="24">
                    <el-input placeholder="请输入内容" v-model="keyword" @keydown="searchEnter">
                        <template #suffix>
                            <i class="el-input__icon el-icon-search" style="cursor:pointer;" @click="search"></i>
                        </template>
                    </el-input>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="24" style="line-height:60px; text-align:center; border-bottom:1px solid #bbb; font-size:20px; font-weight:bold; cursor:pointer;" @click="indexPage">{{this.collected.name}}</el-col>
            </el-row>
            <el-row>
                <el-col :span="24">
                    <el-row v-for="item in this.collected.docLites" :key="item.id" :index="item.id" @click="docPage(item.id)" :style="{padding:'5px',cursor:'pointer',fontSize:'16px',marginTop:'2px',backgroundColor:(item.id===this.docId?'#CCC':'#FFF')}">
                        <el-col :span="24">{{item.title}}</el-col>
                    </el-row>
                </el-col>
            </el-row>
        </el-aside>

        <el-container>
            <el-main ref="docContainer" id="docContainer">
                <div v-if="pageMode === 'index'">
                    <el-row>
                        <el-col :span="24" style="line-height:60px; text-align:center;  font-size:30px; font-weight:bold;">{{this.collected.name}}</el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            &nbsp;
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="20" :offset="2">
                            <el-tabs v-model="activeName" @tab-click="handleClick">
                                <el-tab-pane label="简介" name="first">{{this.collected.description}}</el-tab-pane>
                                <el-tab-pane label="目录" name="second">
                                    <el-table :data="this.collected.docLites" style="width: 100%">
                                        <el-table-column prop="title" label="" width="500">
                                        </el-table-column>
                                        <el-table-column prop="updateTime" label="" align="right">
                                        </el-table-column>
                                    </el-table>
                                </el-tab-pane>
                                <el-tab-pane label="最近文档" name="third">最近更新的文档列表</el-tab-pane>
                                <el-tab-pane label="权限及成员" name="fourth">
                                    <el-table :data="this.collected.sysUserLites" style="width: 100%">
                                        <el-table-column prop="realName" label="姓名" width="180">
                                        </el-table-column>
                                        <el-table-column prop="email" label="邮箱">
                                        </el-table-column>
                                        <el-table-column prop="memberDesc" label="成员角色">
                                        </el-table-column>
                                        <!-- <el-table-column prop="allowEdit" label="编辑">
                                        </el-table-column>
                                        <el-table-column prop="date" label="日期" width="180">
                                        </el-table-column> -->
                                    </el-table>
                                </el-tab-pane>
                            </el-tabs>
                        </el-col>
                    </el-row>
                </div>
                <div v-else>

                    <div>
                        <el-row>
                            <el-col :span="23" style="line-height:60px; text-align:center;  font-size:30px; font-weight:bold;">
                                {{this.doc.title}}
                                <span v-if="this.$store.state.user.token != undefined && this.$store.state.user.token != ''" style="cursor:pointer; font-size:15px" @click="docFocus()">🔍</span>
                            </el-col>
                            <el-col :span="1"></el-col>
                            <!-- <el-col :span="2" style="cursor:pointer;" @click="docFocus()">🔍</el-col> -->
                        </el-row>

                        <el-row>
                            <el-col :span="23" style="text-align:center;">
                                <span v-for="ctor in contributors" :key="ctor" style="padding-right:8px">
                                    <el-tag size="mini" effect="dark">{{ctor.realName}}</el-tag>
                                </span>
                                <span>　</span>
                            </el-col>
                            <el-col :span="1"></el-col>
                        </el-row>

                        <v-md-editor v-model="this.doc.content" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />
                    </div>
                </div>
            </el-main>
        </el-container>
    </el-container>

    <div style="position:fixed; z-index: 99999; top:100px; right:30px;">
        <el-button type="primary" icon="el-icon-menu" circle @click="drawer = true"></el-button>
    </div>

    <el-drawer title="目录导航" v-model="drawer" :with-header="true" size="260">
        <div style="padding:10px;">
            <div v-for="anchor in titles" :key="anchor" :style="{ padding: `10px 0 10px ${anchor.indent * 20}px` }" style="cursor: pointer;" @click="handleAnchorClick(anchor)">
                <a>{{ anchor.title }}</a>
            </div>
        </div>
    </el-drawer>
</template>

<script>
import request from '../../utils/request.js';
import { ElMessageBox, ElMessage } from 'element-plus';
export default {
    data() {
        return {
            activeName: 'first',
            drawer: false,
            keyword: '',
            pageMode: 'index', // index 或者 detail
            collected: {},
            docId: '',
            doc: {},
            contributors: [],
            titles: []
        };
    },
    mounted() {
        let token = this.$store.state.user.token;
        console.log('token: ' + token);

        console.log('collectedId:' + this.$route.params.collectedId + ' docId:' + this.$route.params.docId);

        this.getCollected();

        this.docId = this.$route.params.docId;
    },
    methods: {
        search() {
            this.getCollected();
        },
        searchEnter(e) {
            if (e.keyCode == 13) {
                this.getCollected();
            }
        },
        indexPage() {
            this.pageMode = 'index';
            this.docId = '';
        },
        detailPage() {
            this.pageMode = 'detail';
        },
        getCollected() {
            // 查询文集详情
            request({
                url: '/collected/get',
                method: 'post',
                data: {
                    id: this.$route.params.collectedId,
                    keyword: this.keyword
                }
            }).then(res => {
                if (res.code == 0) {
                    this.collected = res.meta.collected;

                    switch (this.$route.params.docId) {
                        case '_intro':
                            break;
                        default:
                            this.$nextTick(() => {
                                this.getDoc(this.$route.params.docId);
                            });

                            break;
                    }
                }
            });
        },
        getDoc(id) {
            request({
                url: '/doc/get',
                method: 'post',
                data: {
                    id: id
                }
            }).then(res => {
                if (res.code == 0) {
                    this.doc = res.meta.doc;
                    this.contributors = [];
                    if (res.meta.contributors && res.meta.contributors.length > 1) {
                        this.contributors = res.meta.contributors;
                    }
                    this.pageMode = 'detail';

                    this.$nextTick(() => {
                        const anchors = this.$refs.editor.$el.querySelectorAll('.v-md-editor-preview h1,h2,h3,h4,h5,h6');
                        const titles = Array.from(anchors).filter(title => !!title.innerText.trim());

                        if (!titles.length) {
                            this.titles = [];
                            return;
                        }

                        const hTags = Array.from(new Set(titles.map(title => title.tagName))).sort();

                        this.titles = titles.map(el => ({
                            title: el.innerText,
                            lineIndex: el.getAttribute('data-v-md-line'),
                            indent: hTags.indexOf(el.tagName)
                        }));
                    });
                }
            });
        },
        docPage(id) {
            this.docId = id;
            this.$router.push({ name: 'collected', params: { collectedId: this.collected.id, docId: id } });
            this.getDoc(id);
            document.getElementById('docContainer').scrollTop = 0;
        },
        handleAnchorClick(anchor) {
            console.log('handleAnchorClick');
            const { editor } = this.$refs;
            const { lineIndex } = anchor;

            const heading = editor.$el.querySelector(`.v-md-editor-preview [data-v-md-line="${lineIndex}"]`);

            if (heading) {
                console.log('handleAnchorClick:' + heading.offsetTop);
                document.getElementById('docContainer').scrollTop = heading.offsetTop;
                // this.$refs.docContainer.scrollTop = heading.offsetTop;

                //     debugger;
                //     editor.previewScrollToTarget({
                //         target: heading,
                //         scrollContainer: editor.target,
                //         top: 60
                //     });
            } else {
                document.getElementById('docContainer').scrollTop = 0;
            }
        },
        handleCopyCodeSuccess(code) {
            // console.log(code);
            ElMessage({
                message: '复制成功',
                type: 'success',
                duration: 1 * 1000
            });
        },
        // 专注模式
        docFocus() {
            let routeData = this.$router.resolve({
                name: 'docfocus',
                params: { docId: this.$route.params.docId }
            });
            window.open(routeData.path, '_blank');
        }
    }
};
</script>

<style>
.el-aside {
    padding-right: 10px;
    border-right: 1px solid #bbb;
}
.el-menu {
    border-right: 0;
}
.el-drawer {
    overflow: scroll;
    overflow-x: hidden;
    overflow-y: auto;
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