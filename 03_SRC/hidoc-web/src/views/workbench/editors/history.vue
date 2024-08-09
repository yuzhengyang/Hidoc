<template>
    <el-container style="height: 100%">
        <el-main ref="docContainer" id="docContainer">
            <el-row>
                <el-col :span="24" style="line-height: 60px; text-align: center; font-size: 30px; font-weight: bold">{{ this.collected.name }}</el-col>
            </el-row>
            <el-row>
                <el-col :span="12" style="border-right: 2px dotted #9b9b9b">
                    <el-affix :offset="50">
                        <el-row style="background-color: #fff">
                            <el-col :span="22" :offset="1">
                                <el-button type="warning" size="small">历史版本</el-button>
                                <span>&nbsp;</span>
                                <el-select v-model="historyDoc.selectId" placeholder="Select" size="small" @change="getHistoryDoc">
                                    <el-option v-for="item in historyDoc.list" :key="item.id" :label="item.createTime" :value="item.id">
                                        <span style="float: left">{{ item.createTime }}</span>
                                        <span style="float: right; color: var(--el-text-color-secondary); font-size: 13px">&nbsp;-&nbsp;{{ item.realName }}</span>
                                    </el-option>
                                </el-select>
                                <span>&nbsp;</span>
                                <!-- <el-button type="primary" size="small">回退到该版本</el-button> -->
                            </el-col>
                        </el-row>
                    </el-affix>
                    <div style="line-height: 60px; text-align: center; font-size: 30px; font-weight: bold">
                        <span>{{ this.historyDoc.doc.title }}</span>
                    </div>
                    <!-- 文档内容 -->
                    <v-md-editor v-model="this.historyDoc.doc.content" mode="preview" @copy-code-success="handleCopyCodeSuccess" />
                </el-col>
                <el-col :span="12" style="border-left: 2px solid #9b9b9b">
                    <el-affix :offset="50">
                        <el-row style="background-color: #fff">
                            <el-col :span="22" :offset="1">
                                <el-alert :title="'当前版本：' + this.doc.updateTime" type="success" :closable="false" />
                            </el-col>
                        </el-row>
                    </el-affix>
                    <div style="line-height: 60px; text-align: center; font-size: 30px; font-weight: bold">
                        <span>{{ this.doc.title }}</span>
                    </div>
                    <!-- 文档内容 -->
                    <v-md-editor v-model="this.doc.content" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />
                </el-col>
            </el-row>
        </el-main>
        <!-- 大纲导航 -->
        <el-aside width="250px" style="height: 100%">
            <div style="padding: 45px 10px 100px 10px; font-size: 12px">
                <div v-for="anchor in titles" :key="anchor" class="anchor-item" :style="{ padding: `4px 0 4px ${anchor.indent * 10}px` }" @click="handleAnchorClick(anchor)">
                    <a>{{ anchor.title }}</a>
                </div>
            </div>
        </el-aside>
    </el-container>
</template>

<script>
import _ from 'lodash';
import request from '../../../utils/request.js';
import { ElMessageBox, ElMessage } from 'element-plus';
import { mdFormat } from '../../../utils/mdtools';

export default {
    data() {
        return {
            isLogin: false,
            drawer: false,
            collected: {},
            doc: {},
            titles: [],
            collectedPermission: {
                detail: false,
                focus: false,
                his: false,
                create: false,
                edit: false,
                copy: false,
                member: false
            },
            lockUser: {},
            historyDoc: {
                list: [],
                selectId: '',
                doc: {}
            }
        };
    },
    components: {},
    mounted() {
        let token = this.$store.state.user.token;
        this.isLogin = !(this.$store.state.user.token == undefined || this.$store.state.user.token == '');
        console.log('token: ' + token + ' collectedId:' + this.$route.params.collectedId + ' docId:' + this.$route.params.docId);

        this.getCollected();
        this.getDoc();
        this.getHistoryList();
        if (this.isLogin) this.getPermission();
    },
    methods: {
        getCollected() {
            // 查询文集详情
            request({
                url: '/collected/get',
                method: 'post',
                data: {
                    id: this.$route.params.collectedId
                }
            }).then(res => {
                if (res.code == 0) {
                    this.collected = res.meta.collected;
                }
            });
        },
        getDoc() {
            request({
                url: '/doc/get',
                method: 'post',
                data: {
                    id: this.$route.params.docId
                }
            }).then(res => {
                if (res.code == 0) {
                    this.doc = res.meta.doc;

                    // 预览前对文本进行处理，然后再渲染预览
                    this.doc.content = mdFormat(this.doc.content);
                    document.title = this.doc.title;
                    if (res.meta.user) {
                        this.lockUser = res.meta.user;
                        console.log('lockUser:' + this.lockUser);
                    } else {
                        this.lockUser = {};
                    }
                    this.$nextTick(() => {
                        // 界面处理-1：预览中设置标题层级导航栏
                        const anchors = this.$refs.editor.$el.querySelectorAll('.v-md-editor-preview h1,h2,h3,h4,h5,h6');
                        const titles = Array.from(anchors).filter(title => !!title.innerText.trim());
                        if (!titles.length) {
                            this.titles = [];
                            // return;
                        } else {
                            const hTags = Array.from(new Set(titles.map(title => title.tagName))).sort();
                            this.titles = titles.map(el => ({
                                title: el.innerText,
                                lineIndex: el.getAttribute('data-v-md-line'),
                                indent: hTags.indexOf(el.tagName)
                            }));
                        }
                    });
                }
            });
        },
        getHistoryList() {
            // 查询文集详情
            request({
                url: '/dochistory/list',
                method: 'post',
                data: {
                    id: this.$route.params.docId
                }
            }).then(res => {
                if (res.code == 0) {
                    this.historyDoc.list = res.data;
                }
            });
        },
        // 查询操作权限清单
        getPermission() {
            request({
                url: '/collected/permission',
                method: 'post',
                data: {
                    id: this.$route.params.collectedId
                }
            }).then(res => {
                if (res.code == 0) {
                    this.collectedPermission = res.meta;
                }
            });
        },
        getHistoryDoc(id) {
            // 查询文集详情
            request({
                url: '/dochistory/get',
                method: 'post',
                data: {
                    id: id
                }
            }).then(res => {
                if (res.code == 0) {
                    this.historyDoc.doc = res.meta.docHistory;
                }
            });
        },
        // 导航定位
        handleAnchorClick(anchor) {
            console.log('handleAnchorClick: ' + anchor);
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
        // 复制代码
        handleCopyCodeSuccess(code) {
            // console.log(code);
            ElMessage({
                message: '复制成功',
                type: 'success',
                duration: 1 * 1000
            });
        }
    }
};
</script>

<style>
/* 滚动槽 */
::-webkit-scrollbar {
    width: 4px;
    height: 4px;
    background-color: #e9e9e9;
}

::-webkit-scrollbar-track {
    border-radius: 10px;
}

::-webkit-scrollbar-thumb {
    background-color: #bfc1c4;
}

/* 大纲导航特效 */
.anchor-item {
    cursor: pointer;
    background: transparent;
    border: 0;
    border-radius: 0;
    position: relative;
    color: #636363;
}
.anchor-item:before {
    transition: all 0.2s linear;
    content: '';
    width: 0%;
    height: 100%;
    background: #59a1ff46;
    position: absolute;
    top: 0;
    left: 0;
}
.anchor-item:hover:before {
    background: #59a1ff46;
    width: 100%;
}
.vuepress-markdown-body{
    overflow: hidden;
}
</style>
