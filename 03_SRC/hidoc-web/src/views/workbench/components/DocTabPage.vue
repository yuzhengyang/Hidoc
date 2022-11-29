<template>
    <el-container style="height: 100%">
        <el-header>
            <el-row>
                <el-button-group>
                    <el-button round type="success" size="small" @click="openCreateCollected">创建文集</el-button>
                    <el-button round v-if="createDocVisible" type="warning" size="small" @click="openEditCollected">编辑文集</el-button>
                    <el-button round v-if="createDocVisible" type="primary" size="small" @click="openMemberList">协作成员</el-button>
                    <el-button round v-if="createDocVisible" type="danger" size="small" @click="deleteCollected">删除文集</el-button>
                </el-button-group>
                <span style="width: 200px"></span>
                <el-button-group>
                    <el-button round v-if="createDocVisible" type="success" size="small" @click="createDoc">创建文档</el-button>
                    <el-button round v-if="createDocVisible" type="primary" size="small" @click="refreshDocList">刷新</el-button>
                </el-button-group>
                <span style="width: 20px"></span>
                <el-button-group>
                    <el-button round v-if="createDocVisible" :type="docNodeDragEnable ? 'success' : 'info'" size="small" @click="docNodeDragEnable = !docNodeDragEnable">拖动排序</el-button>
                    <el-button round v-if="createDocVisible" :type="'warning'" size="small" @click="saveDocNodeDropOrder">保存排序</el-button>
                </el-button-group>
            </el-row>
        </el-header>
        <el-container style="height: 90%">
            <el-aside width="250px" style="height: 100%">
                <el-menu default-active="2" @open="handleOpen" @close="handleClose" :unique-opened="true">
                    <el-sub-menu index="1">
                        <template #title>
                            <span style="font-size: 14px; font-weight: bold; border-bottom: 1px solid black">我公开的协作（{{ collectedList.myCoop.length ?? 0 }}）</span>
                        </template>
                        <el-menu-item v-for="item in collectedList.myCoop" :key="item.id.toString()" :index="item.id" @click="selectCollected(item)">
                            {{ item.name }}
                            <el-tag v-show="item.isTemplet" type="warning" size="mini" style="margin-left: 2px; margin-right: 2px">模板</el-tag>
                            <!-- <el-tag v-show="item.isOpen" size="mini" style="margin-left: 2px; margin-right: 2px">公开</el-tag> -->
                            <!-- <el-tag v-show="item.isCoop" type="warning" size="mini">协作</el-tag> -->
                        </el-menu-item>
                    </el-sub-menu>
                    <el-sub-menu index="2">
                        <template #title>
                            <span style="font-size: 14px; font-weight: bold; border-bottom: 1px solid black">我参与的协作（{{ collectedList.joinCoop.length ?? 0 }}）</span>
                        </template>
                        <el-menu-item v-for="item in collectedList.joinCoop" :key="item.id.toString()" :index="item.id" @click="selectCollected(item)">{{ item.name }}</el-menu-item>
                    </el-sub-menu>
                    <el-sub-menu index="3">
                        <template #title>
                            <span style="font-size: 14px; font-weight: bold; border-bottom: 1px solid black">我公开的文集（{{ collectedList.myOpen.length ?? 0 }}）</span>
                        </template>
                        <el-menu-item v-for="item in collectedList.myOpen" :key="item.id.toString()" :index="item.id" @click="selectCollected(item)">
                            {{ item.name }}
                            <el-tag v-show="item.isCoop" type="warning" size="mini" style="margin-left: 2px; margin-right: 2px">协作</el-tag>
                            <el-tag v-show="item.isTemplet" type="warning" size="mini" style="margin-left: 2px; margin-right: 2px">模板</el-tag>
                        </el-menu-item>
                    </el-sub-menu>
                    <el-sub-menu index="4">
                        <template #title>
                            <span style="font-size: 14px; font-weight: bold; border-bottom: 1px solid black">私有文集（{{ collectedList.myPrivate.length ?? 0 }}）</span>
                        </template>
                        <el-menu-item v-for="item in collectedList.myPrivate" :key="item.id.toString()" :index="item.id" @click="selectCollected(item)">
                            {{ item.name }}
                            <el-tag v-show="item.isCoop" type="warning" size="mini" style="margin-left: 2px; margin-right: 2px">协作</el-tag>
                            <el-tag v-show="item.isTemplet" type="warning" size="mini" style="margin-left: 2px; margin-right: 2px">模板</el-tag>
                        </el-menu-item>
                    </el-sub-menu>
                </el-menu>
            </el-aside>
            <el-main>
                <el-row style="font-weight: bold; color: grey">
                    <el-col :span="9"><span style="padding-left: 20px">标题</span></el-col>
                    <el-col :span="2"><span style="padding-left: 5px">拥有者</span></el-col>
                    <el-col :span="2"><span style="padding-left: 5px">最近更新</span></el-col>
                    <el-col :span="2"><span style="padding-left: 5px">锁(编辑中)</span></el-col>
                    <el-col :span="4"><span style="padding-left: 5px">排序</span></el-col>
                    <el-col :span="4"><span style="padding-left: 5px">操作</span></el-col>
                </el-row>
                <el-row style="width: 100%">
                    <el-col :span="23" style="padding: 10px; border-bottom: 1px solid lightgrey"></el-col>
                </el-row>
                <el-tree :data="tableData" node-key="id" default-expand-all :expand-on-click-node="false" :draggable="docNodeDragEnable" @node-drop="docNodeDrop">
                    <template #default="{ data }">
                        <el-row style="width: 100%">
                            <el-col :span="9">
                                <span v-if="docNodeDragEnable"><i class="el-icon-rank" style="padding-left: 4px; padding-right: 4px"></i></span>
                                <span @click="previewDoc(data)">{{ data.title }}</span>
                                <!-- <el-button @click="previewDoc(data)" type="text" size="medium">{{ data.title }}</el-button> -->
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
                                <el-button @click="setOrder(data, 0)" type="text" size="small">顶</el-button>
                                <el-button @click="setOrder(data, 1)" type="text" size="small">上</el-button>
                                <el-button @click="setOrder(data, 2)" type="text" size="small">下</el-button>
                                <el-button @click="setOrder(data, 3)" type="text" size="small">底</el-button>
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
            </el-main>
        </el-container>
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
                <span v-if="collectedForm.isOpen" class="tips">在团队内可见</span>
                <span v-else class="tips">自己及协作成员可见</span>
            </el-form-item>
            <el-form-item label="需登录查看" :label-width="formLabelWidth">
                <el-switch v-model="collectedForm.isLoginAccess" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
                <span class="tips">设置是否登录后才能获取文档内容</span>
            </el-form-item>
            <el-form-item label="作为模板" :label-width="formLabelWidth">
                <el-switch v-model="collectedForm.isTemplet" active-color="#13ce66" inactive-color="#ff4949"></el-switch>
                <span class="tips">为文档编写提供模板，不可被搜索</span>
            </el-form-item>
            <el-form-item label="所属团队" :label-width="formLabelWidth">
                <el-select v-model="this.collectedForm.teamIdList" multiple placeholder="可选择已加入的团队" style="width: 300px">
                    <el-option v-for="item in this.team.myJoin" :key="item.id" :label="item.name" :value="item.id" />
                </el-select>
                <span class="tips">选取后，仅对应团队可见</span>
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
        <el-row style="margin-bottom: 20px">
            <el-alert title="协作成员拥有编辑权限，可编辑 “文集” 或 “文档”， 同时可以邀请其他成员进入协作" type="warning" :closable="false" />
        </el-row>
        <el-row>
            <el-col :span="24">
                <div style="text-align: center">
                    <el-transfer v-model="memberId" :props="{ key: 'id', label: 'realName' }" style="text-align: left; display: inline-block" filterable :titles="['用户列表', '参与成员']" :button-texts="['移除', '添加']" :format="{ noChecked: '${total}', hasChecked: '${checked}/${total}' }" :data="allUser" @change="handleChange">
                        <template #default="{ option }">
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
            <el-col :span="24" style="text-align: center; margin-top: 50px">
                <el-button type="primary" @click="saveMember">保存参与成员列表</el-button>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="24" style="text-align: center">
                <!-- {{memberId}} -->
            </el-col>
        </el-row>
    </el-dialog>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
import _ from 'lodash';
export default {
    data() {
        return {
            docNodeDragEnable: false,
            rightValue: [],
            currentCollected: {},
            tableData: [],
            dialogFormMode: 'create',
            dialogFormVisible: false,
            dialogMemberVisible: false,
            createDocVisible: false,
            collectedForm: {
                name: '',
                description: '',
                isOpen: false,
                isTemplet: false,
                teamIdList: []
            },
            formLabelWidth: '120px',
            memberUser: [],
            otherUser: [],
            allUser: [],
            memberId: [],
            mineList: [],
            coopList: [],
            collectedList: {
                myCoop: [], // 我发起的协作
                joinCoop: [], // 我加入的协作（别人创建的）
                myOpen: [], // 我公开的文集
                myPrivate: [] // 私有文集
                // myAll: [] // 我的全部
            },
            team: {
                myJoin: []
            }
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
        loadMyJoinTeam() {
            return request({
                url: '/team/getMyJoinTeams',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    // debugger;
                    this.team.myJoin = res.data;
                }
            });
        },
        loadCollected() {
            return request({
                url: '/collected/list',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    // debugger;
                    this.mineList = res.meta.mine;
                    this.coopList = res.meta.coop;

                    this.collectedList.myCoop = _.filter(res.meta.mine, { isCoop: true, isOpen: true }); // 我公开的协作
                    this.collectedList.joinCoop = res.meta.coop; // 我参与的协作
                    this.collectedList.myOpen = _.filter(res.meta.mine, { isCoop: false, isOpen: true }); // 我公开的文集
                    this.collectedList.myPrivate = _.filter(res.meta.mine, { isOpen: false }); // 私有文集
                    // this.myAll = res.meta.mine;
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
            this.collectedForm.teamIdList = [];
            this.collectedForm.isOpen = false;
            this.collectedForm.isTemplet = false;
            this.collectedForm.isLoginAccess = false;
            this.dialogFormVisible = true;
            this.loadMyJoinTeam();
        },
        openEditCollected() {
            console.log('编辑文集');
            this.dialogFormMode = 'edit';
            this.collectedForm.name = this.currentCollected.name;
            this.collectedForm.description = this.currentCollected.description;
            this.collectedForm.teamIdList = this.currentCollected.teamIdList;
            this.collectedForm.isOpen = this.currentCollected.isOpen;
            this.collectedForm.isLoginAccess = this.currentCollected.isLoginAccess;
            this.collectedForm.isTemplet = this.currentCollected.isTemplet;
            this.dialogFormVisible = true;
            this.loadMyJoinTeam();
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
                        teamIdList: this.collectedForm.teamIdList,
                        token: this.$store.state.user.token,
                        isOpen: this.collectedForm.isOpen,
                        isLoginAccess: this.collectedForm.isLoginAccess,
                        isTemplet: this.collectedForm.isTemplet
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
                        teamIdList: this.collectedForm.teamIdList,
                        token: this.$store.state.user.token,
                        isOpen: this.collectedForm.isOpen,
                        isLoginAccess: this.collectedForm.isLoginAccess,
                        isTemplet: this.collectedForm.isTemplet
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.dialogFormVisible = false;
                        this.loadCollected();
                        this.selectCollected(res.meta.docCollected);
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
                    token: this.$store.state.user.token
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

            let routeData = this.$router.resolve({
                name: 'workbench_editor',
                params: { collectedId: this.currentCollected.id, docId: '_create', parentDocId: '_blank', copyDocId: '_none' }
            });
            window.open(routeData.path, '_blank');
        },
        createChildDoc(parentDocId) {
            console.log('创建子文档');

            let routeData = this.$router.resolve({
                name: 'workbench_editor',
                params: { collectedId: this.currentCollected.id, docId: '_create', parentDocId: parentDocId, copyDocId: '_none' }
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
        },
        // 编辑
        docEdit(row) {
            console.log('编辑');
            console.log(row);
            let parentDocId = '_blank';
            if (row.parentDocId != '') parentDocId = row.parentDocId;

            let routeData = this.$router.resolve({
                name: 'workbench_editor',
                params: { collectedId: this.currentCollected.id, docId: row.id, parentDocId: parentDocId, copyDocId: '_none' }
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
        },
        docNodeDrop(draggingNode, dropNode, dropType, ev) {
            // console.log('tree drag end:', dropNode && dropNode.label, dropType);
            // console.log(this.tableData);
        },
        /**
         * 保存拖动的排序
         */
        saveDocNodeDropOrder() {
            console.log('整理层级关系');
            if (this.tableData.length > 0) {
                var result = new Array();
                for (var i = 0; i < this.tableData.length; i++) {
                    result.push({ id: this.tableData[i].id, pid: '', level: 0 });
                    result.push(...this.genDocLevelData(this.tableData[i], 1));
                }
                console.log(result);

                return request({
                    url: '/collected/freeOrder',
                    method: 'post',
                    data: {
                        docList: result
                    }
                }).then(res => {
                    if (res.code == 0) {
                        ElMessage({
                            message: '操作成功',
                            type: 'success',
                            duration: 1 * 1000
                        });

                        this.selectCollected(this.currentCollected);
                    }
                });
            }
            console.log('以上');
        },
        genDocLevelData(parentDoc, level) {
            var result = new Array();
            if (parentDoc && parentDoc.children && parentDoc.children.length > 0) {
                for (var i = 0; i < parentDoc.children.length; i++) {
                    result.push({ id: parentDoc.children[i].id, pid: parentDoc.id, level: level });
                    result.push(...this.genDocLevelData(parentDoc.children[i], level + 1));
                }
            }
            return result;
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
    width: 100%;
    height: 46px;
    font-size: 12px;
    line-height: 50px;
    border-bottom: 1px dashed lightgrey;
}

.tips {
    font-size: 12px;
    color: rgb(94, 94, 94);
    padding-left: 10px;
    padding-right: 10px;
}
</style>
