<template>
    <el-container style="height:100%">
        <el-header>
            <el-row>
                <el-button-group>
                    <el-button round type="success" @click="openCreateCollected">创建文集</el-button>
                    <el-button round v-if="createDocVisible" type="warning" @click="openEditCollected">编辑文集</el-button>
                    <el-button round v-if="createDocVisible" type="primary" @click="openMemberList">协作成员</el-button>
                    <el-button round v-if="createDocVisible" type="danger" @click="deleteCollected">删除文集</el-button>
                </el-button-group>
                <span style="width:200px"></span>
                <el-button-group>
                    <el-button round v-if="createDocVisible" type="success" @click="createDoc">创建文档</el-button>
                    <el-button round v-if="createDocVisible" type="primary" @click="refreshDocList">刷新</el-button>
                </el-button-group>
            </el-row>
        </el-header>
        <el-main style="height:100%">
            <el-row style="height:100%">
                <el-col :span="5" style="height:100%;overflow:hidden;overflow:auto;">
                    <el-menu default-active="2" @open="handleOpen" @close="handleClose">
                        <el-sub-menu index="1">
                            <template #title>
                                <span style="font-size:14px;font-weight:bold;border-bottom:1px solid black">个人知识库</span>
                            </template>
                            <el-menu-item v-for="item in mineList" :key="item.id.toString()" :index="item.id" @click="selectCollected(item)">
                                {{ item.name }}
                                <el-tag v-show="item.isOpen" size="mini">公开</el-tag>
                                <el-tag v-show="item.isCoop" type="warning" size="mini">协作</el-tag>
                            </el-menu-item>
                        </el-sub-menu>
                        <el-sub-menu index="2">
                            <template #title>
                                <span style="font-size:14px;font-weight:bold;border-bottom:1px solid black">团队协作</span>
                            </template>
                            <el-menu-item v-for="item in coopList" :key="item.id.toString()" :index="item.id" @click="selectCollected(item)">{{ item.name }}</el-menu-item>
                        </el-sub-menu>
                    </el-menu>
                </el-col>
                <el-col :span="19" style="height:100%;overflow:hidden;overflow:auto;">
                    <el-row style="font-weight:bold;color:grey;">
                        <el-col :span="10"><span style="padding-left:20px;">标题</span></el-col>
                        <el-col :span="2"><span style="padding-left:5px;">拥有者</span></el-col>
                        <el-col :span="2"><span style="padding-left:5px;">最近更新</span></el-col>
                        <el-col :span="2"><span style="padding-left:5px;">锁(编辑中)</span></el-col>
                        <el-col :span="4"><span style="padding-left:5px;">排序</span></el-col>
                        <el-col :span="4"><span style="padding-left:5px;">操作</span></el-col>
                    </el-row>
                    <el-row style="width:100%">
                        <el-col :span="24" style="padding:10px;border-bottom:1px solid lightgrey;"></el-col>
                    </el-row>
                    <el-tree :data="tableData" node-key="id" default-expand-all :expand-on-click-node="false">
                        <template #default="{ data }">
                            <el-row style="width:100%;">
                                <el-col :span="10">
                                    <el-button @click="previewDoc(data)" type="text" size="small">{{ data.title }}</el-button>
                                </el-col>
                                <el-col :span="2">{{ data.ownerUser.realName }}</el-col>
                                <el-col :span="2">
                                    <el-popover effect="light" trigger="hover" placement="top">
                                        <template #default>
                                            <p>{{ data.updateTime }}</p>
                                        </template>
                                        <template #reference>
                                            <div class="name-wrapper">
                                                {{ data.relativeUpdateTime }}
                                            </div>
                                        </template>
                                    </el-popover>
                                </el-col>
                                <el-col :span="2">
                                    <div class="name-wrapper">
                                        <div v-if="data.isCurrentUserLock">
                                            <el-tag v-if="data.lockUser" size="medium">我</el-tag>
                                        </div>
                                        <div v-else>
                                            <div v-if="data.lockUserId != ''">
                                                <el-tag v-if="data.lockUser" size="medium" type="warning">{{ data.lockUser.realName }}</el-tag>
                                                <el-tag v-else size="medium" type="danger">未知</el-tag>
                                            </div>
                                        </div>
                                    </div>
                                </el-col>
                                <el-col :span="4">
                                    <el-button @click="setOrder(data, 0)" type="text" size="small">顶部</el-button>
                                    <el-button @click="setOrder(data, 1)" type="text" size="small">上移</el-button>
                                    <el-button @click="setOrder(data, 2)" type="text" size="small">下移</el-button>
                                    <el-button @click="setOrder(data, 3)" type="text" size="small">底部</el-button>
                                </el-col>
                                <el-col :span="4">
                                    <el-button @click="createChildDoc(data.id)" type="text" size="small">创建子文档</el-button>
                                    <el-button @click="docEdit(data)" type="text" size="small">编辑</el-button>
                                    <el-button @click="docUnlock(data)" type="text" size="small">解锁</el-button>

                                    <el-popover placement="top-start" :width="200" trigger="click">
                                        <p>删除操作不可撤回，确定删除吗？</p>
                                        <div style="text-align: right; margin: 0">
                                            <el-button type="danger" size="mini" @click="docDelete(data)">确定删除</el-button>
                                        </div>
                                        <template #reference>
                                            <el-button type="text" size="small">删除</el-button>
                                        </template>
                                    </el-popover>
                                </el-col>
                            </el-row>
                        </template>
                    </el-tree>
                </el-col>
            </el-row>
        </el-main>
    </el-container>
    <el-backtop></el-backtop>

    <!-- Form -->
    <el-dialog :title="this.dialogFormMode == 'create' ? '创建文集' : '编辑文集'" v-model="dialogFormVisible">
        <el-form :model="collectedForm">
            <el-form-item label="名称" :label-width="formLabelWidth">
                <el-input v-model="collectedForm.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="描述" :label-width="formLabelWidth">
                <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="collectedForm.description"></el-input>
            </el-form-item>
            <el-form-item label="公开" :label-width="formLabelWidth">
                <el-switch v-model="collectedForm.isOpen" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
            </el-form-item>
            <el-form-item label="需登录查看" :label-width="formLabelWidth">
                <el-switch v-model="collectedForm.isLoginAccess" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveCollected">保 存</el-button>
            </span>
        </template>
    </el-dialog>

    <el-dialog title="协作成员" v-model="dialogMemberVisible">
        <el-row>
            <el-col :span="24">
                <div style="text-align: center">
                    <el-transfer v-model="memberId" :props="{ key: 'id', label: 'realName' }" style="text-align: left; display: inline-block" filterable :titles="['用户列表', '参与成员']" :button-texts="['移除', '添加']" :format="{ noChecked: '${total}', hasChecked: '${checked}/${total}' }" :data="allUser" @change="handleChange">
                        <template #default="{option}">
                            <span>{{ option.realName }}</span>
                        </template>
                        <!-- <template #left-footer>
                            <el-button class="transfer-footer" size="small">操作</el-button>
                        </template>
                        <template #right-footer>
                            <el-button class="transfer-footer" size="small">操作</el-button>
                        </template> -->
                    </el-transfer>
                </div>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="24" style="text-align:center;margin-top:50px;">
                <el-button type="primary" @click="saveMember">保存参与成员列表</el-button>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="24" style="text-align:center;">
                <!-- {{memberId}} -->
            </el-col>
        </el-row>
    </el-dialog>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
export default {
    data() {
        const generateData = _ => {
            const data = [];
            for (let i = 1; i <= 15; i++) {
                data.push({
                    key: i,
                    label: `备选项 ${i}`,
                    disabled: i % 4 === 0
                });
            }
            return data;
        };
        return {
            data: generateData(),
            rightValue: [],
            currentCollected: {},
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

        this.loadCollected();
    },
    components: {},
    methods: {
        loadCollected() {
            return request({
                url: '/collected/list',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    this.mineList = res.meta.mine;
                    this.coopList = res.meta.coop;
                    console.log(this.mineList);
                    console.log(this.coopList);
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
        refreshDocList() {
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
                        isOpen: this.collectedForm.isOpen,
                        isLoginAccess: this.collectedForm.isLoginAccess
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
                        isOpen: this.collectedForm.isOpen,
                        isLoginAccess: this.collectedForm.isLoginAccess
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
        createDoc() {
            console.log('创建文档');

            // this.$router.push({ name: 'workbench_editor', params: { status: 'create', collectedId: this.currentCollected.id } });

            let routeData = this.$router.resolve({
                name: 'workbench_editor',
                params: { collectedId: this.currentCollected.id, docId: '_create', parentDocId: '_blank' }
            });
            window.open(routeData.path, '_blank');
        },
        createChildDoc(parentDocId) {
            console.log('创建子文档');

            // this.$router.push({ name: 'workbench_editor', params: { status: 'create', collectedId: this.currentCollected.id } });

            let routeData = this.$router.resolve({
                name: 'workbench_editor',
                params: { collectedId: this.currentCollected.id, docId: '_create', parentDocId: parentDocId }
            });
            window.open(routeData.path, '_blank');
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
            let parentDocId = '_blank';
            if (row.parentDocId != '') parentDocId = row.parentDocId;

            let routeData = this.$router.resolve({
                name: 'workbench_editor',
                params: { collectedId: this.currentCollected.id, docId: row.id, parentDocId: parentDocId }
            });
            window.open(routeData.path, '_blank');
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

/** 树自定义样式 */
.el-tree-node__content {
    height: 50px;
    font-size: 12px;
    line-height: 50px;
    border-bottom: 1px dashed lightgrey;
}
</style>
