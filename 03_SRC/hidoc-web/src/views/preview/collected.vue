<template>
    <el-container style="height: 100%">
        <el-aside width="280px" style="height: 100%; padding-right: 10px; border-right: 1px solid #bbb">
            <el-row>
                <el-col :span="24">
                    <el-input placeholder="请输入内容" v-model="keyword" @keydown="searchEnter">
                        <template #suffix>
                            <i class="el-input__icon el-icon-search" style="cursor: pointer" @click="search"></i>
                        </template>
                    </el-input>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="24" style="line-height: 46px; text-align: center; border-bottom: 1px solid #bbb; font-size: 16px; font-weight: bold; cursor: pointer" @click="indexPage">{{ this.collected.name }}</el-col>
            </el-row>
            <el-tree :data="this.collected.docLites" node-key="id" default-expand-all :expand-on-click-node="false" @node-click="goDocPage">
                <template #default="{ data }">
                    <span v-if="length(data.title) < 35" :style="{ padding: '0px', cursor: 'pointer', fontSize: '14px', marginTop: '2px', fontWeight: data.id === this.docId ? '900' : 'normal', width: '260px', color: data.id === this.docId ? '#409eff' : '#3c3d40' }">{{ data.title }}</span>
                    <el-tooltip v-else effect="dark" :content="data.title" placement="right">
                        <span :style="{ padding: '0px', cursor: 'pointer', fontSize: '14px', marginTop: '2px', fontWeight: data.id === this.docId ? '900' : 'normal', width: '260px', color: data.id === this.docId ? '#409eff' : '#3c3d40' }">{{ data.title }}</span>
                    </el-tooltip>
                </template>
            </el-tree>
        </el-aside>

        <el-container>
            <el-main ref="docContainer" id="docContainer">
                <div v-if="pageMode === 'index'">
                    <el-row>
                        <el-col :span="24" style="line-height: 60px; text-align: center; font-size: 30px; font-weight: bold">{{ this.collected.name }}</el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">&nbsp;</el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="20" :offset="2">
                            <el-tabs v-model="activeName" @tab-click="handleTabClick">
                                <el-tab-pane label="简介" name="first">
                                    <textarea readonly style="border: 0; width: 100%; height: 500px; resize: none; outline: none; font-size: 16px; font-family: Avenir, Helvetica, Arial, sans-serif" v-model="this.collected.description"></textarea>
                                </el-tab-pane>
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
                                <el-tab-pane label="知识网络" name="ilinkRelation">
                                    <doc-ilink-relation v-bind:data="this.collected" ref="docIlinkRef"></doc-ilink-relation>
                                </el-tab-pane>
                            </el-tabs>
                        </el-col>
                    </el-row>
                </div>
                <div v-else>
                    <!-- 文档标题 -->
                    <el-row>
                        <el-col :span="23" style="text-align: center">
                            <span style="line-height: 60px; text-align: center; font-size: 30px; font-weight: bold">{{ this.doc.title }}</span>
                        </el-col>
                        <el-col :span="1"></el-col>
                        <!-- <el-col :span="2" style="cursor:pointer;" @click="docFocus()">🔍</el-col> -->
                    </el-row>
                    <el-row>
                        <el-col :span="23" style="text-align: center">
                            <span v-if="this.lockUser && this.lockUser.realName">
                                <el-tag class="ml-2" type="warning" size="small" effect="plain">{{ this.lockUser.realName }} 编辑锁定</el-tag>
                            </span>
                        </el-col>
                    </el-row>

                    <!-- 文档内容 -->
                    <v-md-editor v-model="this.doc.content" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />

                    <!-- 点赞及点赞人员 -->
                    <el-row>
                        <el-col :span="24" :offset="0" style="height: 100px"></el-col>
                        <el-col :span="24" :offset="0" style="font-size: 14px; text-align: center">
                            <p>
                                <el-button v-if="this.myThumb.isSupporter" type="primary" icon="el-icon-thumb" circle @click="createThumb"></el-button>
                                <el-button v-else icon="el-icon-thumb" circle @click="createThumb"></el-button>
                            </p>
                            <p>{{ this.thumbCount }} 人点赞</p>
                        </el-col>
                        <el-col :span="24" :offset="0" style="text-align: center">
                            <el-popover :width="300" placement="top" trigger="hover" v-for="item in thumbUserList" :key="item">
                                <template #reference>
                                    <el-avatar :size="35" :src="currentAvatar(item.avatar)" style="margin: 10px" />
                                </template>
                                <template #default>
                                    <div class="demo-rich-conent" style="display: flex; gap: 16px; flex-direction: column; padding: 20px">
                                        <el-avatar :size="60" :src="currentAvatar(item.avatar)" style="margin-bottom: 8px" />
                                        <div>
                                            <p class="demo-rich-content__name" style="margin: 0; font-weight: 500">{{ item.realName }}</p>
                                            <p class="demo-rich-content__mention" style="margin: 0; font-size: 14px; color: var(--el-color-info)">{{ item.email }}</p>
                                        </div>

                                        <p class="demo-rich-content__desc" style="margin: 0">暂无描述</p>
                                    </div>
                                </template>
                            </el-popover>
                        </el-col>
                    </el-row>

                    <!-- 补充信息条 -->
                    <el-row>
                        <el-col :span="22" :offset="1" style="font-size: 14px; color: #8a8f8d; text-align: left">
                            <el-tooltip class="item" effect="dark" content="参与编辑" placement="top">
                                <span>
                                    <i class="el-icon-user" style="padding-right: 3px"></i>
                                    <span v-for="ctor in contributors" :key="ctor" style="padding-right: 8px">
                                        {{ ctor.realName }}
                                    </span>
                                </span>
                            </el-tooltip>
                            <el-tooltip class="item" effect="dark" :content="'更新于 ' + this.doc.updateTime + ''" placement="top">
                                <span style="padding-left: 20px">
                                    <i class="el-icon-time" style="padding-right: 3px"></i>
                                    <span>{{ this.doc.updateTime }}</span>
                                </span>
                            </el-tooltip>
                            <el-tooltip class="item" effect="dark" :content="'浏览次数：' + this.readCount" placement="top">
                                <span style="padding-left: 20px">
                                    <i class="el-icon-reading" style="padding-right: 3px"></i>
                                    <span>{{ this.readCount }}</span>
                                </span>
                            </el-tooltip>
                            <el-tooltip class="item" effect="dark" :content="'评论：' + this.commentCount" placement="top">
                                <span style="padding-left: 20px">
                                    <i class="el-icon-chat-dot-square" style="padding-right: 3px"></i>
                                    <span>{{ this.commentCount }}</span>
                                </span>
                            </el-tooltip>
                        </el-col>
                    </el-row>

                    <!-- 已读用户列表 -->
                    <el-row>
                        <el-col :span="24" :offset="0" style="height: 100px"></el-col>
                        <el-col :span="17" :offset="4" style="font-size: 14px; text-align: center">
                            <el-tag v-for="item in readUserList" :key="item" type="" size="small" effect="plain" round style="margin: 2px;border-radius: 30px;">
                                {{ item.realName }}
                            </el-tag>
                            <p>{{ this.readUserList.length }} 人已读~</p>
                        </el-col>
                    </el-row>

                    <!-- 评论 -->
                    <el-row>
                        <el-col :span="22" :offset="1" style="margin-top: 80px">
                            <b>所有评论（{{ this.commentCount }}）</b>
                        </el-col>
                        <el-col :span="22" :offset="1">
                            <div v-for="item in commentList" :key="item.id" style="float: left">
                                <el-divider />
                                <div style="width: 45px; float: left">
                                    <el-avatar :size="35" :src="currentAvatar(item.createUser.avatar)">{{ item.createUser.realName }}</el-avatar>
                                </div>
                                <div style="width: 280px; float: left; font-size: 10px">
                                    <span>{{ item.createUser.realName }}</span>
                                    <span style="color: red; cursor: pointer" @click="replyComment(item.id, item.replyUserId)">{{ item.replyUser && item.replyUser.realName ? ' 回复 ' + item.replyUser.realName : '' }}</span>
                                    <span>&nbsp;&nbsp;{{ item.createTime }}</span>
                                    <span style="color: blue; cursor: pointer" @click="replyComment(item.id, item.createUserId)">&nbsp;&nbsp;回复&nbsp;&nbsp;</span>
                                </div>
                                <div style="width: 280px; float: left; font-size: 14px; color: black; padding: 5px">
                                    <a>{{ item.content }}</a>
                                </div>
                            </div>
                        </el-col>

                        <el-col :span="22" :offset="1">
                            <div style="margin: 10px" v-if="isLogin" id="docCommentBlock">
                                <el-input v-model="inputComment.text" maxlength="500" placeholder="请发表有价值的评论， 请勿灌水，良好的团队氛围需大家一起维护。" show-word-limit type="textarea" />
                                <div style="height: 5px"></div>
                                <el-select v-model="inputComment.replyUserId" class="m-2" placeholder="回复..." size="small" clearable>
                                    <el-option v-for="item in replyUserList" :key="item.id" :label="item.realName" :value="item.id" />
                                </el-select>
                                <el-button style="float: right" type="primary" size="small" round @click="createComment()">评论</el-button>
                            </div>
                        </el-col>
                    </el-row>

                    <!-- 底部预留高度 -->
                    <el-row>
                        <el-col :span="22" :offset="1" style="height: 80px"></el-col>
                    </el-row>
                </div>
            </el-main>
        </el-container>
        <!-- 大纲导航 -->
        <el-aside v-if="pageMode === 'detail' && isShowAnchor" width="250px" style="height: 100%">
            <div style="padding: 45px 10px 100px 10px; font-size: 12px">
                <div v-for="anchor in titles" :key="anchor" class="anchor-item" :style="{ padding: `4px 0 4px ${anchor.indent * 10}px` }" @click="handleAnchorClick(anchor)">
                    <a>{{ anchor.title }}</a>
                </div>
            </div>
        </el-aside>
    </el-container>

    <div v-if="pageMode === 'detail'">
        <!-- ========== ========== ========== 右下角快捷按钮组 ========== ========== ==========  -->
        <div style="position: fixed; bottom: 50px; right: 20px; z-index: 9999">
            <el-badge :value="this.commentCount" class="item" :hidden="this.commentCount == 0">
                <el-button icon="el-icon-chat-dot-square" circle @click="this.scrollToBlock('comment')" style="box-shadow: 0px 0px 5px 5px #ddd"></el-button>
            </el-badge>
        </div>
        <div style="position: fixed; bottom: 100px; right: 20px; z-index: 9999">
            <el-badge :value="this.thumbCount" class="item" :hidden="this.thumbCount == 0">
                <el-button v-if="this.myThumb.isSupporter" type="primary" icon="el-icon-thumb" circle @click="createThumb" style="box-shadow: 0px 0px 5px 5px #ddd"></el-button>
                <el-button v-else icon="el-icon-thumb" circle @click="createThumb" style="box-shadow: 0px 0px 5px 5px #ddd"></el-button>
            </el-badge>
        </div>
        <div style="position: fixed; bottom: 150px; right: 20px; z-index: 9999">
            <el-button icon="el-icon-caret-top" circle @click="this.scrollToBlock('top')" style="box-shadow: 0px 0px 5px 5px #ddd"></el-button>
        </div>
        <!-- ========== ========== ========== 右侧快捷按钮：工具条 ========== ========== ==========  -->
        <!-- z-index: 9999;  -->
        <div style="position: fixed; top: 50px; right: 70px; z-index: 9999" v-if="this.$store.state.user.token != undefined && this.$store.state.user.token != ''">
            <!-- <el-popover placement="left" title="标题" width="200" trigger="hover">
                <div>456456</div>
                <el-button type="primary" icon="el-icon-menu" circle>click 激活</el-button>
            </el-popover> -->
            <el-popover placement="left" :width="360" trigger="click">
                <template #reference>
                    <el-button type="primary" icon="el-icon-menu" circle></el-button>
                </template>
                <div style="font-size: 14px">
                    <el-row>
                        <el-col :span="6" v-if="this.collectedPermission.detail" style="color: red">
                            <i class="el-icon-warning-outline" style="cursor: pointer">-详情</i>
                        </el-col>
                        <el-col :span="6" v-if="this.collectedPermission.focus">
                            <i class="el-icon-full-screen" style="cursor: pointer" @click="docFocus()">-演示</i>
                        </el-col>
                        <el-col :span="6" v-if="this.collectedPermission.his" style="color: red">
                            <i class="el-icon-receiving" style="cursor: pointer">-历史</i>
                        </el-col>
                        <el-col :span="6" v-if="this.collectedPermission.member" style="color: red">
                            <i class="el-icon-user" style="cursor: pointer">-协作</i>
                        </el-col>
                    </el-row>
                    <el-row style="padding-top: 14px">
                        <el-col :span="6" v-if="this.collectedPermission.create">
                            <i class="el-icon-plus" style="cursor: pointer" @click="createDoc()">-新建</i>
                        </el-col>
                        <el-col :span="6" v-if="this.collectedPermission.create">
                            <i class="el-icon-plus" style="cursor: pointer" @click="createChildDoc()">-子文档</i>
                        </el-col>
                        <el-col :span="6" v-if="this.collectedPermission.edit">
                            <i class="el-icon-edit" style="cursor: pointer" @click="editDoc()">-编辑</i>
                        </el-col>
                        <el-col :span="6" v-if="this.collectedPermission.copy">
                            <i class="el-icon-document-copy" style="cursor: pointer" @click="copyDoc()">-复制</i>
                        </el-col>
                    </el-row>
                </div>
            </el-popover>
        </div>

        <!-- ========== ========== ========== 右侧快捷按钮：大纲导航 ========== ========== ==========  -->
        <!-- z-index: 9999;  -->
        <div style="position: fixed; top: 50px; right: 20px; z-index: 9999">
            <el-button type="success" icon="el-icon-tickets" circle @click="isShowAnchor = !isShowAnchor"></el-button>
        </div>
    </div>
</template>

<script>
import _ from 'lodash';
import request from '../../utils/request.js';
import { ElMessageBox, ElMessage } from 'element-plus';
import { mdFormat } from '../../utils/mdtools';
import { length } from '../../utils/strings';
import { avatarImage } from '../../utils/users.js';
import DocIlinkRelation from './components/DocIlinkRelation';

export default {
    data() {
        return {
            isLogin: false,
            activeName: 'first',
            drawer: false,
            isShowAnchor: true,
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
            commentCount: 0,
            readCount: 0,
            myThumb: {},
            thumbUserList: [],
            readUserList: [],
            collectedPermission: {
                detail: false,
                focus: false,
                his: false,
                create: false,
                edit: false,
                copy: false,
                member: false
            },
            lockUser: {}
        };
    },
    components: { DocIlinkRelation },
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
            this.$router.push({ name: 'collected', params: { collectedId: this.collected.id, docId: '_intro' } });

            if (this.activeName == 'ilinkRelation') {
                this.$nextTick(() => {
                    this.$refs.docIlinkRef.ilinkRelation(this.$route.params.collectedId);
                });
            }
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

                    // 查询操作权限清单
                    if (this.isLogin) {
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
                    }

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
        handleTabClick(pane, ev) {
            switch (pane.paneName) {
                case 'ilinkRelation':
                    //this.ilinkRelation(this.$route.params.collectedId);
                    // docIlinkRef.value.ilinkRelation(this.$route.params.collectedId);
                    this.$refs.docIlinkRef.ilinkRelation(this.$route.params.collectedId);
                    break;
                default:
                    break;
            }
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
                    if (res.meta.contributors && res.meta.contributors.length > 0) {
                        this.contributors = res.meta.contributors;
                    }
                    if (res.meta.user) {
                        this.lockUser = res.meta.user;
                        console.log('lockUser:' + this.lockUser);
                    } else {
                        this.lockUser = {};
                    }
                    // 已读用户列表
                    if (res.meta.readUserList) {
                        this.readUserList = res.meta.readUserList;
                    } else {
                        this.readUserList = [];
                    }
                    // 可回复用户列表
                    this.replyUserList = res.meta.replyUserList;
                    // 点赞数
                    this.thumbCount = res.meta.thumbCount;
                    if (res.meta.myThumb) {
                        this.myThumb = res.meta.myThumb;
                    }
                    if (res.meta.readCount) {
                        this.readCount = res.meta.readCount;
                    }
                    if (res.meta.commentCount) {
                        this.commentCount = res.meta.commentCount;
                    }
                    this.pageMode = 'detail';

                    this.getThumbUserList();

                    this.queryComment();

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
        getThumbUserList() {
            request({
                url: '/openapi/docthumb/userList',
                method: 'post',
                data: {
                    tableName: 'doc',
                    dataId: this.docId
                }
            }).then(res => {
                if (res.code == 0) {
                    this.thumbUserList = res.meta.userLiteList;
                }
            });
        },
        createThumb() {
            if (!this.isLogin) {
                ElMessage({
                    message: '点赞请先登录哦~',
                    type: 'warning',
                    duration: 1 * 1000
                });
                return false;
            }

            var thumbValue = true;
            if (this.myThumb != undefined && this.myThumb.isSupporter != undefined) {
                thumbValue = !this.myThumb.isSupporter;
            }

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

                    this.getThumbUserList();
                }
            });
        },
        createComment() {
            if (!this.isLogin) {
                ElMessage({
                    message: '查看评论请先登录哦~',
                    type: 'warning',
                    duration: 1 * 1000
                });
                return false;
            }

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
                url: '/openapi/doccomment/list',
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
            return avatarImage(createUserAvatar);
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
        scrollToBlock(block) {
            switch (block) {
                case 'top':
                    document.getElementById('docContainer').scrollTop = 0;
                    break;
                case 'comment':
                    document.getElementById('docCommentBlock').scrollIntoView(true);
                    break;
            }
        },
        // 专注模式
        docFocus() {
            let routeData = this.$router.resolve({
                name: 'docfocus',
                params: { docId: this.$route.params.docId }
            });
            window.open(routeData.path, '_blank');
        },
        createDoc() {
            console.log('创建文档');

            let parentDocId = '_blank';
            if (this.doc.parentDocId != '') parentDocId = this.doc.parentDocId;

            let routeData = this.$router.resolve({
                name: 'workbench_editor',
                params: { collectedId: this.collected.id, docId: '_create', parentDocId: parentDocId, copyDocId: '_none' }
            });
            window.open(routeData.path, '_blank');
        },
        createChildDoc() {
            console.log('创建子文档');

            let routeData = this.$router.resolve({
                name: 'workbench_editor',
                params: { collectedId: this.collected.id, docId: '_create', parentDocId: this.doc.id, copyDocId: '_none' }
            });
            window.open(routeData.path, '_blank');
        },
        // 编辑
        editDoc() {
            console.log('编辑');
            console.log(this.doc);
            let parentDocId = '_blank';
            if (this.doc.parentDocId != '') parentDocId = this.doc.parentDocId;

            let routeData = this.$router.resolve({
                name: 'workbench_editor',
                params: { collectedId: this.collected.id, docId: this.doc.id, parentDocId: parentDocId, copyDocId: '_none' }
            });
            window.open(routeData.path, '_blank');
        },
        // 复制
        copyDoc() {
            console.log('复制文档');

            let parentDocId = '_blank';
            if (this.doc.parentDocId != '') parentDocId = this.doc.parentDocId;

            let routeData = this.$router.resolve({
                name: 'workbench_editor',
                params: { collectedId: this.collected.id, docId: '_create', parentDocId: parentDocId, copyDocId: this.doc.id }
            });
            window.open(routeData.path, '_blank');
        }
    }
};
</script>

<style>
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

/* 树自定义样式 */
.el-tree-node__content {
    width: 240px;
    height: 40px;
    font-size: 14px;
    line-height: 50px;
    border-bottom: 1px dashed lightgrey;
    overflow: hidden;
    text-overflow: ellipsis;
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
</style>
