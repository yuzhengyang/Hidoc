<template>
    <el-container>
        <!-- 内容区域 -->
        <el-main>
            <el-row>

            </el-row>
            <el-row>
                <el-col :span="5">
                    <el-menu default-active="2" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
                        <el-menu-item v-for="item in bucketList" :key="item.id.toString()" :index="item.id" @click="selectCollected(item)">{{item.name}} </el-menu-item>
                    </el-menu>
                </el-col>
                <el-col :span="19">
                    <el-table :data="tableData" style="width: 100%">
                        <el-table-column prop="title" label="标题">
                        </el-table-column>
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
    <el-dialog :title="this.dialogFormMode=='create'?'创建文件夹':'编辑文件夹'" v-model="dialogFormVisible">
        <el-form :model="collectedForm">
            <el-form-item label="名称" :label-width="formLabelWidth">
                <el-input v-model="collectedForm.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="公开" :label-width="formLabelWidth">
                <el-switch v-model="collectedForm.isOpen" active-color="#13ce66" inactive-color="#ff4949">
                </el-switch>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveCollected">保 存</el-button>
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
            bucketList:[],
            mineList: [],
            coopList: [],
            tableData: [],
            dialogFormMode: 'create',
            dialogFormVisible: false,
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
            memberId: []
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
            console.log('创建文集');
            this.dialogFormMode = 'create';
            this.collectedForm.name = '';
            this.collectedForm.description = '';
            this.collectedForm.isOpen = false;
            this.dialogFormVisible = true;
        },
        openEditCollected() {
            console.log('编辑文集');
            this.dialogFormMode = 'edit';
            this.collectedForm.name = this.currentCollected.name;
            this.collectedForm.description = this.currentCollected.description;
            this.collectedForm.isOpen = this.currentCollected.isOpen;
            this.dialogFormVisible = true;
        },
        // 打开协作成员弹框，并可编辑成员
        openMemberList() {
            this.dialogMemberVisible = true;
            return request({
                url: '/collected/getMember',
                method: 'post',
                data: { id: this.currentCollected.id }
            }).then(res => {
                if (res.code == 0) {
                    this.memberId = res.meta.memberId;
                    this.memberUser = res.meta.memberUser;
                    this.otherUser = res.meta.otherUser;
                    this.allUser = res.meta.allUser;
                }
                // this.$nextTick(_ => {
                // });
            });
        },
        saveMember() {
            return request({
                url: '/collected/saveMember',
                method: 'post',
                data: {
                    id: this.currentCollected.id,
                    memberId: this.memberId
                }
            }).then(res => {
                if (res.code == 0) {
                    this.dialogMemberVisible = false;
                }
            });
        },
        saveCollected() {
            if (this.dialogFormMode == 'create') {
                return request({
                    url: '/collected/create',
                    method: 'post',
                    data: {
                        name: this.collectedForm.name,
                        description: this.collectedForm.description,
                        token: this.$store.state.user.token,
                        isOpen: this.collectedForm.isOpen
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.dialogFormVisible = false;
                        this.loadCollected();
                    }
                });
            }
            if (this.dialogFormMode == 'edit') {
                return request({
                    url: '/collected/edit',
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
                        this.dialogFormVisible = false;
                        this.loadCollected();
                    }
                });
            }
        },
        deleteCollected() {
            return request({
                url: '/collected/delete',
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
                    this.loadCollected();
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
        // 解锁
        docUnlock(row) {
            console.log('解锁:' + row.id);
            return request({
                url: '/doc/unlock',
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
        },
        setOrder(row, vector) {
            console.log('排序:' + row.id);
            return request({
                url: '/collected/setOrder',
                method: 'post',
                data: {
                    collectedId: this.currentCollected.id,
                    docId: row.id,
                    vector: vector
                }
            }).then(res => {
                if (res.code == 0) {
                    ElMessage({
                        message: res.msg || '操作成功',
                        type: 'success',
                        duration: 5 * 1000
                    });

                    this.selectCollected(this.currentCollected);
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