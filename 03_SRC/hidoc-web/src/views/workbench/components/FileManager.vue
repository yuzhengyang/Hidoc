<template>
    <el-container>
        <!-- 内容区域 -->
        <el-main>
            <el-row>
                <el-button-group>
                    <el-button round type="success" @click="openCreateBucket">创建文件夹</el-button>
                    <el-button round v-if="createDocVisible" type="warning" @click="openEditBucket">编辑文件夹</el-button>
                    <el-button round v-if="createDocVisible" type="danger" @click="deleteBucket">删除文件夹</el-button>
                </el-button-group>
                <span style="width: 50px"></span>
                <el-button-group>
                    <el-button round v-if="createDocVisible" type="success" @click="openUploadDialog">上传文件</el-button>
                </el-button-group>
            </el-row>
            <el-row>
                <el-col :span="3">
                    <el-menu default-active="2" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
                        <el-menu-item v-for="item in bucketList" :key="item.id.toString()" :index="item.id" @click="selectBucket(item)">{{ item.name }}</el-menu-item>
                    </el-menu>
                </el-col>
                <el-col :span="21">
                    <el-row>
                        <el-col :span="4" v-for="item in tableData" :key="item">
                            <el-card>
                                <div style="font-size: 14px">
                                    <p>{{ item.fileName }}</p>
                                </div>
                                <div v-if="this.fileType(item.uname) == 'image'">
                                    <el-image :src="formatImageUrl(item.uname)" style="width: 100%; height: 100px" fit="cover" :preview-src-list="[formatImageUrl(item.uname)]" lazy />
                                </div>
                                <div v-else-if="this.fileType(item.uname) == 'video'">
                                    <el-image style="width: 100%; height: 100px; background-color: rgba(0, 166, 255, 0.786); text-align: center">
                                        <template #error>
                                            <div class="image-slot">
                                                <el-icon style="font-size: 50px; margin-top: 20px; color: antiquewhite"><VideoPlay /></el-icon>
                                            </div>
                                        </template>
                                    </el-image>
                                </div>
                                <div v-else>
                                    <el-image style="width: 100%; height: 100px; background-color: rgba(81, 81, 81, 0.786); text-align: center">
                                        <template #error>
                                            <div class="image-slot">
                                                <el-icon style="font-size: 50px; margin-top: 20px; color: rgb(255, 255, 255)"><Document /></el-icon>
                                            </div>
                                        </template>
                                    </el-image>
                                </div>
                                <div style="font-size: 12px">
                                    <div style="padding-top: 2px">{{ item.createTime }}</div>
                                    <div style="padding-top: 2px">{{ item.expiryTime }}</div>
                                    <div style="padding-top: 2px">历史：{{ item.historyCount }}</div>
                                    <div style="padding-top: 2px">大小：{{ parseInt(item.size / 1024 / 1024) }} MB</div>
                                    <div style="padding-top: 2px">下载：{{ item.downloadCount }}</div>
                                    <div style="padding-top: 2px">
                                        <el-button @click="fileHistory(item)" type="text" size="mini">历史</el-button>
                                        <el-button @click="fileDownload(item)" type="text" size="mini">下载</el-button>
                                        <el-popover placement="top-start" :width="200" trigger="click">
                                            <p>删除操作不可撤回，确定删除吗？</p>
                                            <div style="text-align: right; margin: 0">
                                                <el-button type="danger" size="mini" @click="fileDelete(item)">确定删除</el-button>
                                            </div>
                                            <template #reference>
                                                <el-button type="text" size="mini">删除</el-button>
                                            </template>
                                        </el-popover>
                                    </div>
                                </div>
                            </el-card>
                        </el-col>
                    </el-row>
                </el-col>
            </el-row>
        </el-main>
    </el-container>
    <el-backtop></el-backtop>

    <!-- Form -->
    <el-dialog :title="this.dialogFormMode == 'create' ? '创建文件夹' : '编辑文件夹'" v-model="dialogFormVisible">
        <el-form :model="collectedForm">
            <el-form-item label="名称" :label-width="formLabelWidth">
                <el-input v-model="collectedForm.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="公开" :label-width="formLabelWidth">
                <el-switch v-model="collectedForm.isOpen" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveBucket">保 存</el-button>
            </span>
        </template>
    </el-dialog>

    <el-dialog :title="上传文件" v-model="dialogUploadVisible">
        <file-upload v-bind:bucket="currentBucket" :callback="uploadCallback" ref="fileUpload"></file-upload>
        <template #footer>
            <span class="dialog-footer">
                <el-button type="primary" @click="dialogUploadVisible = false">完 成</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
import { config } from '@/utils/config';
import FileUpload from '../components/FileUpload';
export default {
    data() {
        return {
            currentBucket: {},
            bucketList: [],
            tableData: [],
            dialogFormMode: 'create',
            dialogFormVisible: false,
            dialogUploadVisible: false,
            createDocVisible: false,
            collectedForm: {
                name: '',
                description: '',
                isOpen: false
            },
            formLabelWidth: '120px'
        };
    },
    mounted() {
        //  debugger;
        let token = this.$store.state.user.token;
        console.log('token-1-1-1: ' + token);

        let name = this.$store.state.user.name;
        console.log('name: ' + name);

        this.loadBucket();
    },
    components: { FileUpload },
    methods: {
        loadBucket() {
            return request({
                url: '/bucket/list',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    this.bucketList = res.data;
                    console.log(this.bucketList);
                }
            });
        },
        handleChange(value, direction, movedKeys) {
            console.log(value, direction, movedKeys);
        },
        // 左侧菜单展开
        handleOpen(key, keyPath) {
            console.log(key, keyPath);
        },
        // 左侧菜单收起
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        },
        selectBucket(data) {
            this.currentBucket = data;
            this.createDocVisible = true;
            this.getBucketFiles();
        },
        getBucketFiles() {
            request({
                url: '/bucket/files',
                method: 'post',
                data: {
                    bucketId: this.currentBucket.id
                }
            }).then(res => {
                if (res.code == 0) {
                    this.tableData = res.data;
                }
            });
        },
        openCreateBucket() {
            console.log('创建文件夹');
            this.dialogFormMode = 'create';
            this.collectedForm.name = '';
            this.collectedForm.description = '';
            this.collectedForm.isOpen = false;
            this.dialogFormVisible = true;
        },
        openEditBucket() {
            console.log('编辑文件夹');
            this.dialogFormMode = 'edit';
            this.collectedForm.name = this.currentBucket.name;
            this.collectedForm.description = this.currentBucket.description;
            this.collectedForm.isOpen = this.currentBucket.isOpen;
            this.dialogFormVisible = true;
        },
        // 打开上传文件窗口
        openUploadDialog() {
            this.dialogUploadVisible = true;
            this.$refs['fileUpload'].openPanel();
        },
        saveBucket() {
            if (this.dialogFormMode == 'create') {
                return request({
                    url: '/bucket/create',
                    method: 'post',
                    data: {
                        name: this.collectedForm.name,
                        description: '',
                        token: this.$store.state.user.token,
                        isOpen: this.collectedForm.isOpen
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.dialogFormVisible = false;
                        this.loadBucket();
                    }
                });
            }
            if (this.dialogFormMode == 'edit') {
                return request({
                    url: '/bucket/edit',
                    method: 'post',
                    data: {
                        id: this.currentBucket.id,
                        name: this.collectedForm.name,
                        description: '',
                        token: this.$store.state.user.token,
                        isOpen: this.collectedForm.isOpen
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.dialogFormVisible = false;
                        this.currentBucket = res.meta.fileBucket;
                        this.loadBucket();
                    }
                });
            }
        },
        deleteBucket() {
            return request({
                url: '/bucket/delete',
                method: 'post',
                data: {
                    id: this.currentBucket.id,
                    name: this.collectedForm.name,
                    description: this.collectedForm.description,
                    token: this.$store.state.user.token,
                    isOpen: this.collectedForm.isOpen
                }
            }).then(res => {
                if (res.code == 0) {
                    this.currentBucket = {};
                    this.createDocVisible = false;
                    this.loadBucket();
                }
            });
        },
        fileHistory() {},
        fileDownload(data) {
            window.location.href = config().baseServer + 'f/d/' + data.urlPrefix + '/' + data.bucketName + '/' + data.fileName;
        },
        fileDelete(row) {
            console.log('删除');
            return request({
                url: '/file/delete',
                method: 'post',
                data: {
                    cursorId: row.id,
                    fileId: row.fileId
                }
            }).then(res => {
                if (res.code == 0) {
                    ElMessage({
                        message: res.msg || '操作成功',
                        type: 'success',
                        duration: 5 * 1000
                    });

                    this.selectBucket(this.currentBucket);
                } else {
                    ElMessage({
                        message: res.msg || 'Error',
                        type: 'error',
                        duration: 5 * 1000
                    });
                }
            });
        },
        uploadCallback(data) {
            console.log('uploadCallback');
            console.log(data);
            this.getBucketFiles();
        },
        fileType(name) {
            let extName = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
            switch (extName) {
                case 'png':
                case 'jpg':
                case 'gif':
                    return 'image';
                case 'mp4':
                    return 'video';
            }
            return '';
        },
        formatImageUrl(uname) {
            return config().imageServer + uname;
        },
        getHidocFileList(current) {
            this.currentPage = current;
            request({
                url: '/file/hidocList',
                method: 'post',
                data: {
                    token: this.$store.state.user.token,
                    current: this.currentPage,
                    size: this.pageSize
                }
            }).then(res => {
                if (res.code == 0) {
                    this.hidocFileList = res.data;
                    this.dataTotal = res.total;
                    document.getElementById('fileContainer').scrollTop = 0;
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
