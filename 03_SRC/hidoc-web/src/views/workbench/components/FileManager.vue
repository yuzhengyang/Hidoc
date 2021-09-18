<template>
    <el-container>
        <!-- 内容区域 -->
        <el-main>
            <el-row>
                <el-button-group>
                    <el-button round type="success" @click="openCreateCollected">创建文件夹</el-button>
                    <el-button round v-if="createDocVisible" type="warning" @click="openEditCollected">编辑文件夹</el-button>
                    <el-button round v-if="createDocVisible" type="danger" @click="deleteCollected">删除文件夹</el-button>
                </el-button-group>
                <span style="width:50px"></span>
                <el-button-group>
                    <el-button round v-if="createDocVisible" type="success" @click="openUploadDialog">上传文件</el-button>
                </el-button-group>
            </el-row>
            <el-row>
                <el-col :span="5">
                    <el-menu default-active="2" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
                        <el-menu-item v-for="item in bucketList" :key="item.id.toString()" :index="item.id" @click="selectCollected(item)">{{ item.name }}</el-menu-item>
                    </el-menu>
                </el-col>
                <el-col :span="19">
                    <el-table :data="tableData" style="width: 100%">
                        <el-table-column prop="title" label="标题"></el-table-column>
                        <el-table-column label="最近更新" width="100">
                            <template #default="scope">
                                <el-popover effect="light" trigger="hover" placement="top">
                                    <template #default>
                                        <p>{{ scope.row.updateTime }}</p>
                                    </template>
                                    <template #reference>
                                        <div class="name-wrapper">
                                            {{ scope.row.relativeUpdateTime }}
                                        </div>
                                    </template>
                                </el-popover>
                            </template>
                        </el-table-column>
                        <el-table-column fixed="right" label="操作" width="200">
                            <template #default="scope">
                                <el-button @click="previewDoc(scope.row)" type="text" size="small">下载</el-button>

                                <el-popover placement="top-start" :width="200" trigger="click">
                                    <p>删除操作不可撤回，确定删除吗？</p>
                                    <div style="text-align: right; margin: 0">
                                        <el-button type="danger" size="mini" @click="docDelete(scope.row)">确定删除</el-button>
                                    </div>
                                    <template #reference>
                                        <el-button type="text" size="small">删除</el-button>
                                    </template>
                                </el-popover>
                            </template>
                        </el-table-column>
                    </el-table>
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
                <el-button type="primary" @click="saveCollected">保 存</el-button>
            </span>
        </template>
    </el-dialog>

    <el-dialog :title="上传文件" v-model="dialogUploadVisible">
        <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/" :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple :limit="3" :on-exceed="handleExceed" :file-list="fileList">
            <el-button size="small" type="primary">点击上传</el-button>
            <template #tip>
                <div class="el-upload__tip">只能上传 jpg/png 文件，且不超过 500kb</div>
            </template>
        </el-upload>
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
export default {
    data() {
        return {
            rightValue: [],
            currentCollected: {},
            bucketList: [],
            mineList: [],
            coopList: [],
            tableData: [],
            dialogFormMode: 'create',
            dialogFormVisible: false,
            dialogUploadVisible: false,
            dialogMemberVisible: false,
            createDocVisible: false,
            collectedForm: {
                name: '',
                description: '',
                isOpen: false
            },
            formLabelWidth: '120px',
            memberUser: [],
            otherUser: [],
            allUser: [],
            memberId: [],
            fileList: [
                {
                    name: 'food.jpeg',
                    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
                },
                {
                    name: 'food2.jpeg',
                    url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'
                }
            ]
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
    components: {},
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
        selectCollected(data) {
            this.currentCollected = data;
            this.createDocVisible = true;
            request({
                url: '/doc/list',
                method: 'post',
                data: {
                    collectedId: this.currentCollected.id
                }
            }).then(res => {
                if (res.code == 0) {
                    this.tableData = res.data;
                }
            });
        },
        openCreateCollected() {
            console.log('创建文件夹');
            this.dialogFormMode = 'create';
            this.collectedForm.name = '';
            this.collectedForm.description = '';
            this.collectedForm.isOpen = false;
            this.dialogFormVisible = true;
        },
        openEditCollected() {
            console.log('编辑文件夹');
            this.dialogFormMode = 'edit';
            this.collectedForm.name = this.currentCollected.name;
            this.collectedForm.description = this.currentCollected.description;
            this.collectedForm.isOpen = this.currentCollected.isOpen;
            this.dialogFormVisible = true;
        },
        // 打开上传文件窗口
        openUploadDialog() {
            this.dialogUploadVisible = true;
        },
        handleRemove(file, fileList) {
            console.log(file, fileList);
        },
        handlePreview(file) {
            console.log(file);
        },
        handleExceed(files, fileList) {
            this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        },
        beforeRemove(file, fileList) {
            return this.$confirm(`确定移除 ${file.name}？`);
        },
        saveCollected() {
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
                        id: this.currentCollected.id,
                        name: this.collectedForm.name,
                        description: '',
                        token: this.$store.state.user.token,
                        isOpen: this.collectedForm.isOpen
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.dialogFormVisible = false;
                        this.currentCollected = res.meta.fileBucket;
                        this.loadBucket();
                    }
                });
            }
        },
        deleteCollected() {
            return request({
                url: '/bucket/delete',
                method: 'post',
                data: {
                    id: this.currentCollected.id,
                    name: this.collectedForm.name,
                    description: this.collectedForm.description,
                    token: this.$store.state.user.token,
                    isOpen: this.collectedForm.isOpen
                }
            }).then(res => {
                if (res.code == 0) {
                    this.currentCollected = {};
                    this.createDocVisible = false;
                    this.loadBucket();
                }
            });
        },
        // 查看
        previewDoc(row) {
            console.log('查看');

            let routeData = this.$router.resolve({
                name: 'collected',
                params: { collectedId: this.currentCollected.id, docId: row.id }
            });
            window.open(routeData.path, '_blank');

            // this.$router.push({ name: 'collected', params: { collectedId: this.currentCollected.id, docId: row.id } });
        },
        // 编辑
        docEdit(row) {
            console.log('编辑');
            console.log(row);

            let routeData = this.$router.resolve({
                name: 'workbench_editor',
                params: { collectedId: this.currentCollected.id, docId: row.id }
            });
            window.open(routeData.path, '_blank');
        },
        // 删除
        docDelete(row) {
            console.log('删除');
            return request({
                url: '/doc/delete',
                method: 'post',
                data: {
                    id: row.id.toString()
                }
            }).then(res => {
                if (res.code == 0) {
                    ElMessage({
                        message: res.msg || '操作成功',
                        type: 'success',
                        duration: 5 * 1000
                    });

                    this.selectCollected(this.currentCollected);
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
