<template>
    <el-container>
        <el-header style="height: 70px">
            <el-row style="line-height: 40px">
                <el-col :span="8" style="text-align: right; padding-right: 20px">{{ this.collected.name + '：' + this.parentDocPath }}</el-col>
                <el-col :span="8">
                    <el-input v-model="title" placeholder="请输入文档标题"></el-input>
                </el-col>
                <el-col :span="2"></el-col>
                <el-col :span="6" style="text-align: right">
                    <el-button-group>
                        <el-button v-if="this.docId" type="success" @click="save" round>　　保存　　</el-button>
                        <el-button v-if="this.operationStatus" type="primary" @click="saveAndClose" round>保存并关闭</el-button>
                        <el-button type="danger" @click="close" round>　　关闭　　</el-button>
                    </el-button-group>
                </el-col>
            </el-row>
            <el-row style="padding: 5px">
                <el-col :span="12">
                    <el-button-group style="padding-left: 5px">
                        <el-button size="small" @click="openTempletDialog()">
                            <el-icon style="padding-right: 3px"><Notebook /></el-icon>
                            使用模板
                        </el-button>
                    </el-button-group>
                    <el-button-group style="padding-left: 5px">
                        <el-button size="small">
                            <el-icon style="padding-right: 3px"><Document /></el-icon>
                            使用文档
                        </el-button>
                    </el-button-group>
                    <el-button-group style="padding-left: 5px">
                        <el-button size="small" @click="openIlinkDialog()">
                            <el-icon style="padding-right: 3px"><Link /></el-icon>
                            引用文档
                        </el-button>
                        <!-- <el-button size="mini">关系图谱</el-button> -->
                    </el-button-group>
                    <el-button-group style="padding-left: 5px">
                        <el-button size="small">
                            <el-icon style="padding-right: 3px"><FolderOpened /></el-icon>
                            插入文件
                        </el-button>
                    </el-button-group>
                    <el-button-group style="padding-left: 5px">
                        <el-button size="small" @click="openUploadDialog()">
                            <el-icon style="padding-right: 3px"><VideoPlay /></el-icon>
                            插入视频
                        </el-button>
                        <!-- <el-button size="mini">关系图谱</el-button> -->
                    </el-button-group>
                </el-col>
                <el-col :span="12" style="text-align: right">
                    <!-- <el-button-group style="padding-left: 5px">
                        <el-button size="small" @click="formatContent('chapterNumber')">
                            <el-icon style="padding-right: 3px"><SetUp /></el-icon>
                            格式化章节编号
                        </el-button>
                    </el-button-group> -->
                </el-col>
                <el-col :span="6" style="text-align: right">
                    <span v-if="this.lockUser && this.lockUser.realName">
                        <el-tag class="ml-2" type="warning" size="small" effect="plain">当前由：{{ this.lockUser.realName }} 编辑锁定</el-tag>
                    </span>
                </el-col>
            </el-row>
        </el-header>
        <el-main>
            <v-md-editor ref="editor" v-model="content" height="100%" :include-level="[1, 2, 3, 4]" :disabled-menus="[]" left-toolbar="undo redo clear | h bold italic strikethrough quote | ul ol table hr | link image code | ilink" :before-preview-change="beforePreviewChange" @upload-image="handleUploadImage" @change="textChange" :toolbar="toolbar"></v-md-editor>
        </el-main>

        <!-- 引用文档弹出框 -->
        <el-dialog v-model="ilinkDialog.show" title="引用文档" width="50%" center>
            <el-container>
                <el-header>
                    <el-input v-model="this.ilinkDialog.searchText" placeholder="搜索一下" class="input-with-select" @keydown="searchIlinkEnter" clearable>
                        <template #prepend>
                            <el-select v-model="this.ilinkDialog.searchMode" placeholder="Select" style="width: 110px">
                                <el-option label="全文" value="1"></el-option>
                                <el-option label="关键字" value="2"></el-option>
                            </el-select>
                        </template>
                        <template #append>
                            <el-button @click="searchIlink()">
                                <el-icon><Search /></el-icon>
                            </el-button>
                        </template>
                    </el-input>
                </el-header>
                <el-main style="margin-left: 30px; margin-right: 30px">
                    <div style="height: 360px">
                        <el-collapse accordion>
                            <el-collapse-item v-for="colItem in this.ilinkDialog.collectedList" :key="colItem" :title="colItem.name" :name="colItem.id">
                                <div v-for="docItem in colItem.docLites" :key="docItem" style="border-bottom: 1px dashed #ccc; padding: 2px">
                                    <div class="import-item" style="cursor: pointer" @click="addIlinkDoc(docItem.title, colItem.id, docItem.id)">
                                        <span>　{{ docItem.title }}　</span>
                                    </div>
                                </div>
                            </el-collapse-item>
                        </el-collapse>
                    </div>
                </el-main>
            </el-container>
            <template #footer>
                <span class="dialog-footer">
                    <el-affix position="bottom" :offset="20">
                        <el-button type="primary" size="mini" @click="ilinkDialog.show = false" style="width: 200px">关闭</el-button>
                    </el-affix>
                </span>
            </template>
        </el-dialog>

        <!-- 模板文档弹出框 -->
        <el-dialog v-model="templetDialog.show" title="模板文档" width="50%" center>
            <el-container>
                <el-header>
                    <el-input v-model="this.templetDialog.searchText" placeholder="搜索一下" class="input-with-select" @keydown="searchTempletEnter" clearable>
                        <template #prepend>
                            <el-select v-model="this.templetDialog.searchMode" placeholder="Select" style="width: 110px">
                                <el-option label="全文" value="1"></el-option>
                                <el-option label="关键字" value="2"></el-option>
                            </el-select>
                        </template>
                        <template #append>
                            <el-button @click="searchTemplet()">
                                <el-icon><Search /></el-icon>
                            </el-button>
                        </template>
                    </el-input>
                </el-header>
                <el-main style="margin-left: 30px; margin-right: 30px">
                    <div style="height: 360px">
                        <el-collapse accordion>
                            <el-collapse-item v-for="colItem in this.templetDialog.collectedList" :key="colItem" :title="colItem.name" :name="colItem.id">
                                <div v-for="docItem in colItem.docLites" :key="docItem" style="border-bottom: 1px dashed #ccc; padding: 2px">
                                    <div class="import-item" style="cursor: pointer" @click="addTempletDoc(docItem.title, colItem.id, docItem.id)">
                                        <span>　{{ docItem.title }}　</span>
                                    </div>
                                </div>
                            </el-collapse-item>
                        </el-collapse>
                    </div>
                </el-main>
            </el-container>
            <template #footer>
                <span class="dialog-footer">
                    <el-affix position="bottom" :offset="20">
                        <el-button type="primary" size="mini" @click="templetDialog.show = false" style="width: 200px">关闭</el-button>
                    </el-affix>
                </span>
            </template>
        </el-dialog>

        <el-dialog :title="上传文件" v-model="dialogUploadVisible">
            <video-file-upload v-bind:uploadDataParams="uploadDataParams" :callback="uploadCallback" ref="videoFileUpload"></video-file-upload>
            <template #footer>
                <span class="dialog-footer">
                    <el-button type="primary" @click="dialogUploadVisible = false">完 成</el-button>
                </span>
            </template>
        </el-dialog>
        <!-- 文档标签 -->
        <!-- <el-footer>
            <el-row>
                <el-tag :key="tag" v-for="tag in dynamicTags" closable :disable-transitions="false" @close="handleClose(tag)">
                    {{tag}}
                </el-tag>
                <el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput" size="small" @keyup.enter="handleInputConfirm" @blur="handleInputConfirm">
                </el-input>
                <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
            </el-row>
        </el-footer> -->
    </el-container>
</template>

<script>
import { ElMessage, ElMessageBox } from 'element-plus';
import VideoFileUpload from './VideoFileUpload';
import request from '../../../utils/request.js';
import { config } from '@/utils/config';
import { mdFormat } from '../../../utils/mdtools';
import { uiRefresh } from '../../../utils/uimsg';
import _, { includes } from 'lodash';

export default {
    name: 'editor',
    data() {
        this.toolbar = {
            ilink: {
                title: 'mermaid示例',
                icon: 'v-md-icon-tip',
                action(editor) {
                    editor.insert(function (selected) {
                        const prefix = '```mermaid\n';
                        const suffix = '```';
                        const placeholder = 'sequenceDiagram\n' + '    participant Alice\n' + '    participant Bob\n' + '    Alice->>John: Hello John, how are you?\n' + '    loop Healthcheck\n' + '        John->>John: Fight against hypochondria\n' + '    end\n' + '    Note right of John: Rational thoughts <br/>prevail!\n' + '    John-->>Alice: Great!\n' + '    John->>Bob: How about you?\n' + '    Bob-->>John: Jolly good!\n';
                        const content = selected || placeholder;

                        return {
                            text: `${prefix}${content}${suffix}`,
                            selected: content
                        };
                    });
                }
            }
        };
        return {
            heartbeatTimer: '',
            ilinkDialog: {
                show: false,
                collectedList: [],
                searchMode: '',
                searchText: ''
            },
            templetDialog: {
                show: false,
                collectedList: [],
                searchMode: '',
                searchText: ''
            },
            dynamicTags: ['标签一', '标签二', '标签三'],
            inputVisible: false,
            inputValue: '',
            collected: {},
            activeName: 'public',
            content: '',
            contentHtml: '',
            title: '',
            docId: '',
            parentDocId: '',
            parentDocPath: '',
            mode: '',
            operationStatus: true,
            loadStatus: 'create',
            createMode: '',
            dialogUploadVisible: false,
            uploadDataParams: {
                bucketName: '.hidoc'
            },
            lockUser: {}
        };
    },
    mounted() {
        document.title = 'Hidoc-文档编辑器';
        // 标记当前页面为加载状态
        this.loadStatus = 'mounted';
        // 默认开启目录导航
        this.$refs.editor.toggleToc();

        // ========== ========== 关闭标签页时提示 ========== ==========
        window.isCloseHint = true;
        window.addEventListener('beforeunload', function (e) {
            if (window.isCloseHint) {
                var confirmationMessage = '您所作的更改可能未保存，确认离开吗？';
                (e || window.event).returnValue = confirmationMessage; // 兼容 Gecko + IE
                return confirmationMessage; // 兼容 Gecko + Webkit, Safari, Chrome
            }
        });
        // ========== ========== ========== ==========

        console.log('编辑器 config: ' + config());

        let token = this.$store.state.user.token;
        console.log('token: ' + token);

        console.log('route params, docId:' + this.$route.params.docId);
        console.log('route params, collectedId:' + this.$route.params.collectedId);
        console.log('route params, parentDocId:' + this.$route.params.parentDocId);
        console.log('route params, copyDocId:' + this.$route.params.copyDocId);

        let docId = this.$route.params.docId;
        let collectedId = this.$route.params.collectedId;
        this.parentDocId = this.$route.params.parentDocId;
        this.copyDocId = this.$route.params.copyDocId;

        // 如果附文档参数为_blank，则视为无父级的文档，直属文集，需要清空_blank
        if (this.parentDocId == '_blank') this.parentDocId = '';
        console.log('this.parentDocId:' + this.parentDocId);

        console.log('collectedId:' + collectedId + ' docId:' + docId);

        // 如果附文档ID存在，则查询文档层级路径
        if (this.parentDocId != '') {
            request({
                url: '/doc/getPath',
                method: 'post',
                data: {
                    parentDocId: this.parentDocId
                }
            }).then(res => {
                if (res.code == 0 && res.meta.path) {
                    console.log('层级路径为：' + res.meta.path);
                    this.parentDocPath = res.meta.path;
                }
            });
        }

        // 查询文集信息
        request({
            url: '/collected/get',
            method: 'post',
            data: {
                id: this.$route.params.collectedId
            }
        }).then(res => {
            // 标记当前页面为活动状态
            this.loadStatus = 'active';
            this.collected = res.meta.collected;
            this.uploadDataParams.collectedId = this.collected.id;

            switch (docId) {
                case '_create':
                    this.mode = 'create';
                    break;
                default:
                    request({
                        url: '/doc/getForEdit',
                        method: 'post',
                        data: {
                            id: docId
                        }
                    }).then(res => {
                        if (res.code == 0 && res.meta.doc) {
                            this.content = res.meta.doc.content;
                            this.title = res.meta.doc.title;
                            this.docId = res.meta.doc.id;

                            switch (res.status) {
                                case 'ok': {
                                    if (res.meta.doc.isCurrentUserLock) {
                                        console.log('当前用户获取锁，准许编辑');
                                        document.title = this.title;
                                        this.heartbeatTimer = setInterval(this.heartbeat, 60 * 1000);
                                    } else {
                                        console.log('文档已被其他成员锁定，不能编辑');

                                        let msg = '文档已被其他成员锁定，不能编辑，您可以联系：' + res.meta.user.realName + ' ' + res.meta.user.email + ' ' + '，解除锁定。';
                                        this.$alert(msg, {
                                            confirmButtonText: '关闭',
                                            callback: action => {
                                                window.close();
                                            }
                                        });
                                    }
                                    break;
                                }
                                case 'lock': {
                                    if (res.meta.doc.isCurrentUserLock) {
                                        console.log('当前用户获取锁，准许编辑');
                                        document.title = this.title;
                                        this.heartbeatTimer = setInterval(this.heartbeat, 60 * 1000);
                                    } else {
                                        console.log('文档已被其他成员锁定，不能编辑');
                                        this.lockUser = res.meta.user;

                                        let msg = '文档已被其他成员锁定，不能编辑，您可以联系：' + res.meta.user.realName + ' ' + res.meta.user.email + ' ' + '，解除锁定。';
                                        this.$alert(msg, {
                                            confirmButtonText: '关闭',
                                            callback: action => {
                                                window.close();
                                            }
                                        });
                                    }
                                    break;
                                }
                                case 'unauth': {
                                    this.operationStatus = false;
                                    this.$alert('没有操作权限', {
                                        confirmButtonText: '关闭',
                                        callback: action => {
                                            window.close();
                                        }
                                    });
                                    break;
                                }
                            }
                        }
                    });
                    break;
            }
        });

        // 复制文档
        if (this.copyDocId != '_none') {
            request({
                url: '/doc/get',
                method: 'post',
                data: {
                    id: this.copyDocId
                }
            }).then(res => {
                if (res.code == 0) {
                    this.content = res.meta.doc.content;
                }
            });
        }
    },
    components: { VideoFileUpload },
    methods: {
        heartbeat() {
            return request({
                url: '/openapi/heartbeat/lockDoc',
                method: 'post',
                data: {
                    id: this.docId
                }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                }
            });
        },
        // 上传图片
        handleUploadImage(event, insertImage, files) {
            // debugger;
            // 拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
            console.log(files);

            let data = new FormData();
            for (let i = 0; i < files.length; i++) {
                data.append('file', files[i], files[i].name);
            }
            data.append('collectedId', this.collected.id);
            data.append('docId', this.docId);
            data.append('bucketName', '.hidoc');
            return request({
                url: '/f/u',
                method: 'post',
                data
            }).then(res => {
                if (res.code == 0) {
                    insertImage({
                        url: config().hdImageFlag + res.data[0].uname,
                        desc: '图片'
                        // width: 'auto',
                        // height: 'auto',
                    });
                }
            });
        },
        uploadCallback(files) {
            console.log(files);
            if (files) {
                for (let i = 0; i < files.length; i++) {
                    this.$refs.editor.insert(selected => {
                        const prefix = '<video width="600" controls="controls" src="#hd.uname://';
                        const suffix = '"></video>';
                        const content = selected || files[i].uname;

                        return {
                            // 要插入的文本
                            text: `${prefix}${content}${suffix}`
                        };
                    });
                }
            }
        },
        // 打开上传文件窗口
        openUploadDialog() {
            this.dialogUploadVisible = true;
            this.$refs['videoFileUpload'].openPanel();
        },
        // handleClose(tag) {
        //     this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
        // },
        // showInput() {
        //     this.inputVisible = true;
        //     this.$nextTick(_ => {
        //         this.$refs.saveTagInput.$refs.input.focus();
        //     });
        // },
        // handleInputConfirm() {
        //     let inputValue = this.inputValue;
        //     if (inputValue) {
        //         this.dynamicTags.push(inputValue);
        //     }
        //     this.inputVisible = false;
        //     this.inputValue = '';
        // },
        beforePreviewChange(text, next) {
            // 预览前对文本进行处理，然后再渲染预览
            if (this.loadStatus == 'active') {
                next(mdFormat(text));
            } else {
                next(text);
            }
        },
        textChange(text, html) {
            console.log(this.loadStatus);
            if (this.loadStatus == 'active') {
                this.$refs.editor;
                this.$refs.editor.codemirrorInstance;
            }
        },
        // 保存文档
        save() {
            return request({
                url: '/doc/save',
                method: 'post',
                data: {
                    collectedId: this.collected.id,
                    title: this.title,
                    content: this.content,
                    tag: 'tag',
                    mode: this.mode,
                    id: this.docId,
                    parentDocId: this.parentDocId,
                    unlock: false
                }
            }).then(res => {
                if (res.code == 0) {
                    ElMessage({
                        message: '保存成功',
                        type: 'success',
                        duration: 5 * 1000
                    });
                    uiRefresh('editor', 'doc', this.docId);
                }
            });
        },
        saveAndClose() {
            return request({
                url: '/doc/save',
                method: 'post',
                data: {
                    collectedId: this.collected.id,
                    title: this.title,
                    content: this.content,
                    tag: 'tag',
                    mode: this.mode,
                    id: this.docId,
                    parentDocId: this.parentDocId,
                    unlock: true
                }
            }).then(res => {
                if (res.code == 0) {
                    uiRefresh('editor', 'doc', this.docId);
                    window.isCloseHint = false;
                    window.close();
                }
            });
        },
        // 解锁并关闭编辑器
        close() {
            ElMessageBox.confirm('关闭会丢失您修改的内容，确认继续吗？', '注意', {
                confirmButtonText: '强制关闭页面',
                cancelButtonText: '继续编辑',
                type: 'warning'
            })
                .then(() => {
                    // 关闭编辑器的浏览器界面
                    if (this.mode == 'create') {
                        console.log('创建时关闭，无需解锁');
                        window.isCloseHint = false;
                        window.close();
                    } else {
                        return request({
                            url: '/doc/unlock',
                            method: 'post',
                            data: {
                                id: this.docId
                            }
                        }).then(res => {
                            if (res.code == 0) {
                                window.isCloseHint = false;
                                window.close();
                            } else {
                                ElMessage({
                                    message: res.msg || 'Error',
                                    type: 'error',
                                    duration: 5 * 1000
                                });
                            }
                        });
                    }
                })
                .catch(() => {
                    console.log('继续编辑');
                    // ElMessage({
                    //     type: 'info',
                    //     message: '继续编辑'
                    // });
                });
        },
        openIlinkDialog() {
            // this.ilinkDialog.searchText = '';
            this.ilinkDialog.show = true;
            this.searchIlink();
        },
        searchIlinkEnter(e) {
            if (e.keyCode == 13) {
                this.searchIlink();
            }
        },
        searchIlink() {
            request({
                url: '/collected/preview',
                method: 'post',
                data: { mode: this.ilinkDialog.searchMode, keyword: this.ilinkDialog.searchText, from: 'editor' }
            }).then(res => {
                if (res.code == 0) {
                    this.ilinkDialog.collectedList = res.data;
                }
            });
        },
        addIlinkDoc(docTitle, collectedId, docId) {
            this.$refs.editor.insert(selected => {
                // [🧰 工具软件-数据库工具：DBeaver](#hd.ilink->121981462344892416/121983064824872960)

                const prefix = '[';
                const suffix = '](' + config().hdIlinkFlag + collectedId + '/' + docId + ')';
                const content = selected || docTitle;

                return {
                    // 要插入的文本
                    text: `${prefix}${content}${suffix}`,
                    // 插入后要选中的文本
                    selected: content
                };
            });
            this.ilinkDialog.show = false;
        },
        openTempletDialog() {
            // this.templetDialog.searchText = '';
            this.templetDialog.show = true;
            this.searchTemplet();
        },
        searchTempletEnter(e) {
            if (e.keyCode == 13) {
                this.searchTemplet();
            }
        },
        searchTemplet() {
            request({
                url: '/collected/preview',
                method: 'post',
                data: { mode: this.templetDialog.searchMode, keyword: this.templetDialog.searchText, from: 'editor', isTemplet: true }
            }).then(res => {
                if (res.code == 0) {
                    this.templetDialog.collectedList = res.data;
                }
            });
        },
        addTempletDoc(docTitle, collectedId, docId) {
            request({
                url: '/doc/get',
                method: 'post',
                data: { id: docId }
            }).then(res => {
                if (res.code == 0) {
                    this.doc = res.meta.doc;
                    this.content = this.doc.content;
                    this.templetDialog.show = false;
                }
            });
        },
        formatContent(type) {
            // switch (type) {
            //     case 'chapterNumber':
            //         {
            //             let contentArray = _.split(this.content, '\n');
            //             console.log(contentArray);
            //             let inCodeBlock = false;
            //             _(contentArray).forEach(function (value) {
            //                 if(value.startsWith('```')) includes = true;


            //                 console.log(value);
            //             });
            //         }
            //         break;
            //     case '':
            //     default:
            //         break;
            // }
        }
    }
};
</script>

<style>
.el-tag + .el-tag {
    margin-left: 10px;
}
.button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
}
.input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
}
/* 引用项目特效 */
.import-item {
    cursor: pointer;
    background: transparent;
    border: 0;
    border-radius: 0;
    position: relative;
    color: #636363;
}
.import-item:before {
    transition: all 0 linear;
    content: '';
    width: 0%;
    height: 100%;
    background: #59a1ff46;
    position: absolute;
    top: 0;
    left: 0;
}
.import-item:hover:before {
    background: #59a1ff46;
    width: 100%;
}
</style>
