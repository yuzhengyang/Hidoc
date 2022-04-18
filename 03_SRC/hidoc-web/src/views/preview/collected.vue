<template>
    <el-container style="height:100%">
        <el-aside width="300px" style="height:100%">
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
                <el-col :span="24" style="line-height:60px; text-align:center; border-bottom:1px solid #bbb; font-size:20px; font-weight:bold; cursor:pointer;" @click="indexPage">{{ this.collected.name }}</el-col>
            </el-row>
            <el-tree :data="this.collected.docLites" node-key="id" default-expand-all :expand-on-click-node="false" @node-click="goDocPage">
                <template #default="{ data }">
                    <span v-if="length(data.title) < 30" :style="{ padding: '5px', cursor: 'pointer', fontSize: '16px', marginTop: '2px', fontWeight: data.id === this.docId ? '900' : 'normal', width: '260px', color: data.id === this.docId ? '#409eff' : '#606266' }">{{ data.title }}</span>
                    <el-tooltip v-else effect="dark" :content="data.title" placement="right">
                        <span :style="{ padding: '5px', cursor: 'pointer', fontSize: '16px', marginTop: '2px', fontWeight: data.id === this.docId ? '900' : 'normal', width: '260px', color: data.id === this.docId ? '#409eff' : '#606266' }">{{ data.title }}</span>
                    </el-tooltip>
                </template>
            </el-tree>
        </el-aside>

        <el-container>
            <el-main ref="docContainer" id="docContainer">
                <div v-if="pageMode === 'index'">
                    <el-row>
                        <el-col :span="24" style="line-height:60px; text-align:center;  font-size:30px; font-weight:bold;">{{ this.collected.name }}</el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            &nbsp;
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="20" :offset="2">
                            <el-tabs v-model="activeName" @tab-click="handleClick">
                                <el-tab-pane label="简介" name="first">{{ this.collected.description }}</el-tab-pane>
                                <el-tab-pane label="目录" name="second">
                                    <el-table :data="this.collected.docLites" style="width: 100%">
                                        <el-table-column prop="title" label="" width="500"></el-table-column>
                                        <el-table-column prop="updateTime" label="" align="right"></el-table-column>
                                    </el-table>
                                </el-tab-pane>
                                <el-tab-pane label="最近文档" name="third">最近更新的文档列表</el-tab-pane>
                                <el-tab-pane label="权限及成员" name="fourth">
                                    <el-table :data="this.collected.sysUserLites" style="width: 100%">
                                        <el-table-column prop="realName" label="姓名" width="180"></el-table-column>
                                        <el-table-column prop="email" label="邮箱"></el-table-column>
                                        <el-table-column prop="memberDesc" label="成员角色"></el-table-column>
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
                                {{ this.doc.title }}
                                <span v-if="this.$store.state.user.token != undefined && this.$store.state.user.token != ''" style="cursor:pointer; font-size:15px" @click="docFocus()">🔍</span>
                            </el-col>
                            <el-col :span="1"></el-col>
                            <!-- <el-col :span="2" style="cursor:pointer;" @click="docFocus()">🔍</el-col> -->
                        </el-row>

                        <el-row>
                            <el-col :span="18" style="text-align:center;">
                                <span v-for="ctor in contributors" :key="ctor" style="padding-right:8px">
                                    <el-tag size="mini" effect="dark">{{ ctor.realName }}</el-tag>
                                </span>
                                <span></span>
                            </el-col>
                            <el-col :span="6" style="text-align:center;">
                                <!-- isSupporter -->
                                <span v-if="this.isLogin">
                                    <el-badge :value="thumbCount" :max="999" class="item">
                                        <el-button v-if="this.myThumb.isSupporter" type="primary" icon="el-icon-thumb" circle @click="createThumb"></el-button>
                                        <el-button v-else icon="el-icon-thumb" circle @click="createThumb"></el-button>
                                    </el-badge>
                                </span>
                                <span v-else>
                                    <el-badge :value="thumbCount" :max="999" class="item">
                                        <el-button icon="el-icon-thumb" circle @click="createThumb"></el-button>
                                    </el-badge>
                                </span>
                            </el-col>
                        </el-row>

                        <v-md-editor v-model="this.doc.content" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />
                    </div>
                </div>
            </el-main>
        </el-container>
    </el-container>

    <div v-if="pageMode === 'detail'">
        <!-- ========== ========== ========== 右侧快捷按钮：目录导航 ========== ========== ==========  -->
        <!-- z-index: 9999;  -->
        <div style="position:fixed; top:100px; right:30px;">
            <el-button type="primary" icon="el-icon-menu" circle @click="drawer = true"></el-button>
        </div>
        <!-- size="260" 不带px单位，运行效果为自适应的 -->
        <el-drawer title="目录导航" v-model="drawer" :with-header="true" size="360px">
            <div style="padding:10px;">
                <div v-for="anchor in titles" :key="anchor" :style="{ padding: `10px 0 10px ${anchor.indent * 20}px` }" style="cursor: pointer;" @click="handleAnchorClick(anchor)">
                    <a>{{ anchor.title }}</a>
                </div>
            </div>
        </el-drawer>

        <!-- ========== ========== ========== 右侧快捷按钮：评论信息 ========== ========== ==========  -->
        <div style="position:fixed; top:150px; right:30px;">
            <el-button type="warning" icon="el-icon-s-comment" circle @click="openCommentDrawer()"></el-button>
        </div>
        <el-drawer title="评论" v-model="commentDrawer" :with-header="true" size="360px">
            <div style="margin: 20px 0" />

            <div style="margin: 10px;">
                <el-input v-model="inputComment.text" maxlength="500" placeholder="请发表有价值的评论， 请勿灌水，良好的团队氛围需大家一起维护。" show-word-limit type="textarea" />
                <div style="height:5px;"></div>
                <el-select v-model="inputComment.replyUserId" class="m-2" placeholder="回复..." size="small" clearable>
                    <el-option v-for="item in replyUserList" :key="item.id" :label="item.realName" :value="item.id" />
                </el-select>
                <el-button style="float:right;" type="primary" size="small" round @click="createComment()">评论</el-button>
            </div>

            <div style="margin: 20px 0" />
            <div style="padding:10px; ">
                <!-- 总宽度280px 左右各间隔10px 剩余可用260px -->
                <div v-for="item in commentList" :key="item.id" style="float:left; padding-bottom: 25px;">
                    <div style="width:45px; float:left;">
                        <el-avatar :size="35" :src="currentAvatar(item.createUser.avatar)">{{ item.createUser.realName }}</el-avatar>
                    </div>
                    <div style="width:280px; float:left; font-size: 10px;">
                        <span>{{ item.createUser.realName }}</span>
                        <span style="color: red; cursor: pointer; ">{{ item.replyUser && item.replyUser.realName ? ' 回复 ' + item.replyUser.realName : '' }}</span>
                        <span>&nbsp;&nbsp;{{ item.createTime }}</span>
                        <span style="color: blue; cursor: pointer; " @click="replyComment(item.id, item.createUserId)">&nbsp;&nbsp;回复&nbsp;&nbsp;</span>
                    </div>
                    <div style="width:280px; float:left; font-size: 14px; color: black; padding: 5px;">
                        <a>{{ item.content }}</a>
                    </div>
                </div>
            </div>
        </el-drawer>
    </div>
</template>

<script>
import request from '../../utils/request.js';
import { ElMessageBox, ElMessage } from 'element-plus';
import { mdFormat } from '../../utils/mdtools';
import { length } from '../../utils/strings';
export default {
    data() {
        return {
            isLogin: false,
            activeName: 'first',
            drawer: false,
            commentDrawer: false,
            inputComment: {
                text: '',
                replyCommentId: '',
                replyUserId: ''
            },
            commentList: [],
            replyUserList: [],
            keyword: '',
            pageMode: 'index', // index 或者 detail
            collected: {},
            docId: '',
            doc: {},
            contributors: [],
            titles: [],
            thumbCount: 0,
            myThumb: {}
        };
    },
    mounted() {
        let token = this.$store.state.user.token;
        console.log('token: ' + token);

        if (this.$store.state.user.token == undefined || this.$store.state.user.token == '') {
            this.isLogin = false;
        } else {
            this.isLogin = true;
        }

        console.log('collectedId:' + this.$route.params.collectedId + ' docId:' + this.$route.params.docId);

        this.getCollected();

        this.docId = this.$route.params.docId;
    },
    methods: {
        length(s) {
            return length(s);
        },
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
                            document.title = this.collected.name;
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
                    // 预览前对文本进行处理，然后再渲染预览
                    this.doc.content = mdFormat(this.doc.content);
                    document.title = this.doc.title;
                    this.contributors = [];
                    if (res.meta.contributors && res.meta.contributors.length > 1) {
                        this.contributors = res.meta.contributors;
                    }
                    this.replyUserList = res.meta.contributors;
                    // 点赞数
                    this.thumbCount = res.meta.thumbCount;
                    if (res.meta.myThumb) {
                        this.myThumb = res.meta.myThumb;
                    }
                    this.pageMode = 'detail';

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

                        // 界面处理-2：锚点定位（定位可能失败，增加异常处理）
                        // 支持两种锚点：
                        // 1、段落标题定位，中文名称定位，重复名称会定位错误
                        // 2、常规锚点定位，使用元素ID来定位，需要使用html元素标签设置ID值
                        try {
                            let hash = document.location.hash.replaceAll('#', '');
                            if (hash != '') {
                                if (hash.indexOf('$') >= 0) {
                                    // 文档小标题定位
                                    console.log('存在标题描述，需要定位：' + hash);
                                    let title = decodeURI(hash.replaceAll('$', ''));
                                    for (var i = 0; i < this.titles.length; i++) {
                                        let titleItem = this.titles[i];
                                        if (titleItem.title === title) {
                                            this.handleAnchorClick(titleItem);
                                            console.log('锚点定位完成，已到达指定位置');
                                            break;
                                        }
                                    }
                                } else {
                                    // 常规锚点定位
                                    console.log('存在常规锚点，需要定位：' + hash);
                                    document.getElementById(hash).scrollIntoView(true);
                                    console.log('锚点定位完成，已到达指定位置');
                                }
                            }
                        } catch (err) {
                            // catchCode - 捕获错误的代码块
                            console.log('锚点定位失败，可能是锚点不存在');
                        } finally {
                            // finallyCode - 无论 try / catch 结果如何都会执行的代码块
                        }
                    });
                }
            });
        },
        goDocPage(data) {
            this.docId = data.id;
            this.$router.push({ name: 'collected', params: { collectedId: this.collected.id, docId: this.docId } });
            this.getDoc(this.docId);
            document.getElementById('docContainer').scrollTop = 0;
        },
        docPage(id) {
            this.docId = id;
            this.$router.push({ name: 'collected', params: { collectedId: this.collected.id, docId: id } });
            this.getDoc(id);
            document.getElementById('docContainer').scrollTop = 0;
        },
        createThumb() {
            if (!this.isLogin) {
                ElMessage({
                    message: '点赞请先登录哦~',
                    type: 'warning',
                    duration: 1 * 1000
                });
            } else {
                var thumbValue = true;
                if (this.myThumb != undefined && this.myThumb.isSupporter != undefined) {
                    thumbValue = !this.myThumb.isSupporter;
                }
                // 查询文集详情
                request({
                    url: '/docthumb/create',
                    method: 'post',
                    data: {
                        tableName: 'doc',
                        dataId: this.docId,
                        type: 'supporter',
                        value: thumbValue
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.thumbCount = res.meta.thumbCount;
                        this.myThumb = res.meta.myThumb;
                    }
                });
            }
        },
        openCommentDrawer() {
            this.commentDrawer = true;
            this.queryComment();
        },
        createComment() {
            if (this.inputComment.text && this.inputComment.text.length > 0) {
                request({
                    url: '/doccomment/create',
                    method: 'post',
                    data: {
                        docId: this.docId,
                        content: this.inputComment.text,
                        replyCommentId: this.inputComment.replyCommentId,
                        replyUserId: this.inputComment.replyUserId
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.inputComment = {
                            text: '',
                            replyCommentId: '',
                            replyUserId: ''
                        };
                        this.queryComment();

                        ElMessage({
                            message: '评论完成~',
                            type: 'success',
                            duration: 1 * 1000
                        });
                    }
                });
            } else {
                ElMessage({
                    message: '请输入评论内容~',
                    type: 'warning',
                    duration: 1 * 1000
                });
            }
        },
        queryComment() {
            request({
                url: '/doccomment/list',
                method: 'post',
                data: {
                    docId: this.docId
                }
            }).then(res => {
                if (res.code == 0) {
                    this.commentList = res.data;
                }
            });
        },
        replyComment(commentId, userId) {
            console.log('comment-id: ' + commentId + ', user-id: ' + userId);

            this.inputComment = {
                // text: '',
                replyCommentId: commentId,
                replyUserId: userId
            };
        },
        currentAvatar(createUserAvatar) {
            return require('../../assets/avatar/' + createUserAvatar.replace('$system$', ''));
        },
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

/** 树自定义样式 */
.el-tree-node__content {
    width: 280px;
    height: 50px;
    font-size: 14px;
    line-height: 50px;
    border-bottom: 1px dashed lightgrey;
    overflow: hidden;
    text-overflow: ellipsis;
}
</style>
