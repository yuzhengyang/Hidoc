<template>
    <el-container>
        <el-header>
            <el-row>
                <el-col :span="8">{{ this.collected.name }}</el-col>
                <el-col :span="8">
                    <el-input v-model="title" placeholder="请输入文档标题"></el-input>
                </el-col>
                <el-col :span="8">
                    <el-button-group>
                        <el-button v-if="this.operationStatus" round type="success" @click="save">保存文档</el-button>
                        <el-button round type="danger" @click="close">关闭</el-button>
                    </el-button-group>
                </el-col>
            </el-row>
        </el-header>
        <el-main>
            <v-md-editor ref="editor" v-model="content" height="100%" :include-level="[1, 2, 3, 4]" :disabled-menus="[]" left-toolbar="undo redo clear | h bold italic strikethrough quote | ul ol table hr | link image code" :before-preview-change="beforePreviewChange" @upload-image="handleUploadImage" @change="textChange"></v-md-editor>
        </el-main>
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
import { ElMessage } from 'element-plus';
import request from '../../utils/request.js';
import { config } from '@/utils/config';
import { mdFormat } from '../../utils/mdtools';
export default {
    name: 'editor',
    data() {
        return {
            dynamicTags: ['标签一', '标签二', '标签三'],
            inputVisible: false,
            inputValue: '',
            collected: {},
            activeName: 'public',
            dialogFormVisible: false,
            collectedForm: {
                name: '',
                description: '',
                isOpen: false
            },
            formLabelWidth: '120px',
            content: '',
            contentHtml: '',
            title: '',
            docId: '',
            mode: '',
            operationStatus: true,
            loadStatus: 'create'
        };
    },
    mounted() {
        this.loadStatus = 'mounted';
        // 默认开启目录导航
        this.$refs.editor.toggleToc();

        // ========== ========== 关闭标签页时提示 ========== ==========
        window.isCloseHint = true;
        window.addEventListener('beforeunload', function(e) {
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

        console.log(this.$route.params.collectedId);

        let collectedId = this.$route.params.collectedId;
        let docId = this.$route.params.docId;

        console.log('collectedId:' + collectedId + ' docId:' + docId);
        // 查询文集信息
        request({
            url: '/collected/get',
            method: 'post',
            data: {
                id: this.$route.params.collectedId
            }
        }).then(res => {
            this.collected = res.meta.collected;

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
                            this.loadStatus = 'active';

                            switch (res.status) {
                                case 'ok': {
                                    if (res.meta.doc.isCurrentUserLock) {
                                        console.log('当前用户获取锁，准许编辑');
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
    },
    components: {},
    methods: {
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
        handleClose(tag) {
            this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
        },

        showInput() {
            this.inputVisible = true;
            this.$nextTick(_ => {
                this.$refs.saveTagInput.$refs.input.focus();
            });
        },
        handleInputConfirm() {
            let inputValue = this.inputValue;
            if (inputValue) {
                this.dynamicTags.push(inputValue);
            }
            this.inputVisible = false;
            this.inputValue = '';
        },
        beforePreviewChange(text, next) {
            // 预览前对文本进行处理，然后再渲染预览
            if (this.loadStatus == 'active') {
                next(mdFormat(text));
            }else{
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
                    id: this.docId
                }
            }).then(res => {
                if (res.code == 0) {
                    window.isCloseHint = false;
                    window.close();
                }
            });
        },
        // 解锁并关闭编辑器
        close() {
            if (this.mode == 'create') {
                console.log('创建时关闭，无需解锁');
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
</style>
