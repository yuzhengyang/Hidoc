<template>
    <el-container>
        <el-header height="30px">
            <el-row>
                <el-alert title="您可以在此查看关于团队设定的相关信息" type="success" :closable="false" />
            </el-row>
        </el-header>
        <el-main>
            <el-tabs type="card" style="padding: 10px">
                <el-tab-pane label="团队列表">
                    <el-row style="padding: 10px">
                        <el-col :span="24">
                            <el-table :data="this.teams.all" style="width: 100%">
                                <el-table-column prop="name" label="名称" width="180"></el-table-column>
                                <el-table-column prop="description" label="描述" width="240"></el-table-column>
                                <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
                                <el-table-column prop="ownerUser.realName" label="录入人" width="120"></el-table-column>
                                <el-table-column prop="memberCount" label="团队人数" width="100"></el-table-column>
                                <el-table-column label="加入状态" width="100">
                                    <template #default="scope_myJoinStatus">
                                        <el-tag v-if="scope_myJoinStatus.row.myJoinStatus == 'y'" type="success" size="small">已加入</el-tag>
                                    </template>
                                </el-table-column>
                                <el-table-column label="操作">
                                    <template #default="scopeOp">
                                        <el-link type="primary" style="font-size: 12px; margin-right: 10px" @click="queryMembers(scopeOp.row.id)">查看成员</el-link>
                                        <el-link v-if="scopeOp.row.myJoinStatus == 'n'" type="warning" style="font-size: 12px; margin-right: 10px" @click="joinTeam(scopeOp.row.id, 'autoJoinEmailSuffix', '')">自动加入</el-link>
                                        <el-link v-if="scopeOp.row.myJoinStatus == 'n'" type="success" style="font-size: 12px; margin-right: 10px" @click="openJoinTeam(scopeOp.row.id)">密码加入</el-link>
                                        <el-link v-if="scopeOp.row.myJoinStatus == 'y'" type="success" style="font-size: 12px; margin-right: 10px" @click="openInviteUser(scopeOp.row.id)">邀请成员</el-link>
                                        <el-link v-if="scopeOp.row.myJoinStatus == 'y'" type="danger" style="font-size: 12px; margin-right: 10px" @click="quitTeam(scopeOp.row.id, true)">退出团队</el-link>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </el-col>
                    </el-row>
                </el-tab-pane>
                <el-tab-pane label="我的团队" v-if="user.roles.includes('admin')">
                    <el-row>
                        <el-button-group>
                            <el-button round type="success" size="small" @click="openCreateTeam">创建团队</el-button>
                        </el-button-group>
                    </el-row>
                    <el-row style="padding: 10px">
                        <el-col :span="24">
                            <el-table :data="this.teams.owner" style="width: 100%">
                                <el-table-column prop="name" label="名称" width="180"></el-table-column>
                                <el-table-column prop="description" label="描述" width="180"></el-table-column>
                                <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
                                <el-table-column prop="ownerUser.realName" label="录入人" width="180"></el-table-column>
                                <el-table-column prop="memberCount" label="团队人数" width="180"></el-table-column>
                                <el-table-column fixed="right" label="管理">
                                    <template #default="scopeOp">
                                        <el-link type="primary" style="font-size: 12px; margin-right: 10px" @click="queryMembers(scopeOp.row.id)">查看成员</el-link>
                                        <el-link type="warning" style="font-size: 12px; margin-right: 10px" @click="openEditTeam(scopeOp.row.id, true)">编辑信息</el-link>
                                        <!-- <el-link type="success" style="font-size: 12px; margin-right: 10px" @click="setAdmin(scopeOp.row, false)">xx管理成员</el-link>
                                        <el-link style="font-size: 12px; margin-right: 10px" @click="previewDoc(scopeOp.row)">xx团队转让</el-link>
                                        <el-link type="danger" style="font-size: 12px; margin-right: 10px" @click="previewDoc(scopeOp.row)">xx删除团队</el-link> -->
                                    </template>
                                </el-table-column>
                            </el-table>
                        </el-col>
                    </el-row>
                </el-tab-pane>
            </el-tabs>
        </el-main>
    </el-container>

    <!-- Form 创建团队 -->
    <el-dialog :title="this.teamForm.mode == 'create' ? '创建团队' : '编辑团队'" v-model="this.teamForm.dialog">
        <el-form :model="this.teamForm.prop">
            <el-form-item label="名称" label-width="150px">
                <el-input v-model="this.teamForm.prop.name" autocomplete="off" maxlength="20"></el-input>
            </el-form-item>
            <el-form-item label="描述" label-width="150px">
                <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="this.teamForm.prop.description" maxlength="200"></el-input>
            </el-form-item>
            <el-form-item label="加入邮箱后缀" label-width="150px">
                <el-input v-model="this.teamForm.prop.joinRule.autoJoinEmailSuffix" placeholder="example.com" autocomplete="off" maxlength="40">
                    <template #prepend>@</template>
                </el-input>
            </el-form-item>
            <el-form-item label="加入密码" label-width="150px">
                <el-input v-model="this.teamForm.prop.joinRule.userJoinPassword" placeholder="请输入密码" autocomplete="off" maxlength="40"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="this.teamForm.dialog = false">取 消</el-button>
                <el-button type="primary" @click="saveTeam()">保 存</el-button>
            </span>
        </template>
    </el-dialog>

    <!-- Form 密码加入到团队 -->
    <el-dialog v-model="this.joinTeamForm.dialog" title="使用密码加入团队">
        <el-form :model="this.joinTeamForm.prop">
            <el-form-item label="请输入密码" :label-width="formLabelWidth">
                <el-input v-model="this.joinTeamForm.prop.joinPassword" autocomplete="off" />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="this.joinTeamForm.dialog = false">取 消</el-button>
                <el-button type="primary" @click="joinTeam(this.joinTeamForm.prop.teamId, 'userJoinPassword', '')">加 入</el-button>
            </span>
        </template>
    </el-dialog>

    <!-- Form 邀请到团队 -->
    <el-dialog v-model="this.inviteUserForm.dialog" title="邀请成员加入团队">
        <el-form :model="this.inviteUserForm.prop">
            <el-form-item label="成员邮箱" :label-width="formLabelWidth">
                <el-input v-model="this.inviteUserForm.prop.email" autocomplete="off" type="textarea" :rows="10" />
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="this.inviteUserForm.dialog = false">取 消</el-button>
                <el-button type="primary" @click="inviteUser()">加 入</el-button>
            </span>
        </template>
    </el-dialog>

    <!-- 查看成员列表 -->
    <el-dialog v-model="this.memberListForm.dialog" title="成员列表">
        <el-table :data="this.memberListForm.list" :default-sort="{ prop: 'createTime', order: 'descending' }" height="400" border stripe>
            <el-table-column prop="createTime" label="加入时间" sortable />
            <el-table-column prop="userInfo.realName" label="姓名" width="120" />
            <el-table-column prop="userInfo.email" label="邮箱" />
        </el-table>
    </el-dialog>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
export default {
    data() {
        return {
            user: { roles: [] },
            teams: {
                all: [],
                owner: []
            },
            teamForm: {
                mode: 'create',
                dialog: false,
                prop: {
                    id: '',
                    name: '',
                    description: '',
                    joinRule: { autoJoinEmailSuffix: '', userJoinPassword: '' }
                }
            },
            joinTeamForm: {
                dialog: false,
                prop: { joinPassword: '' }
            },
            inviteUserForm: {
                dialog: false,
                prop: { email: '' }
            },
            memberListForm: {
                dialog: false,
                list: []
            },
            currentMachineList: []
        };
    },
    mounted() {
        this.user.roles = this.$store.state.user.roles;
        this.showAllTeams();
        this.showOwnerTeams();
    },
    components: {},
    methods: {
        openCreateTeam() {
            // 清空表单数据
            this.teamForm.prop.name = '';
            this.teamForm.prop.description = '';
            this.teamForm.prop.joinRule.autoJoinEmailSuffix = '';
            this.teamForm.prop.joinRule.userJoinPassword = '';
            this.teamForm.mode = 'create';
            this.teamForm.dialog = true;
        },
        openEditTeam(teamId) {
            return request({
                url: '/team/get',
                method: 'post',
                data: { token: this.$store.state.user.token, id: teamId }
            }).then(res => {
                if (res.code == 0 && res.meta.team) {
                    this.teamForm.prop = res.meta.team;
                    if (this.teamForm.prop.joinRule == undefined) this.teamForm.prop.joinRule = {};
                    if (this.teamForm.prop.joinRule.autoJoinEmailSuffix == undefined) this.teamForm.prop.joinRule.autoJoinEmailSuffix = '';
                    if (this.teamForm.prop.joinRule.userJoinPassword == undefined) this.teamForm.prop.joinRule.userJoinPassword = '';
                    this.teamForm.mode = 'edit';
                    this.teamForm.dialog = true;
                }
            });
        },
        saveTeam() {
            if (this.teamForm.mode == 'create') {
                return request({
                    url: '/team/create',
                    method: 'post',
                    data: {
                        name: this.teamForm.prop.name,
                        description: this.teamForm.prop.description,
                        joinRule: this.teamForm.prop.joinRule
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.teamForm.dialog = false;
                        this.showOwnerTeams();
                    }
                });
            }
            if (this.teamForm.mode == 'edit') {
                return request({
                    url: '/team/edit',
                    method: 'post',
                    data: {
                        id: this.teamForm.prop.id,
                        name: this.teamForm.prop.name,
                        description: this.teamForm.prop.description,
                        joinRule: this.teamForm.prop.joinRule
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.teamForm.dialog = false;
                        this.showOwnerTeams();
                    }
                });
            }
        },
        showOwnerTeams() {
            return request({
                url: '/team/getOwnerTeams',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.teams.owner = res.data;
                }
            });
        },
        showAllTeams() {
            return request({
                url: '/team/getAllTeams',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.teams.all = res.data;
                }
            });
        },
        openJoinTeam(teamId) {
            this.joinTeamForm.prop.teamId = teamId;
            this.joinTeamForm.prop.joinPassword = '';
            this.joinTeamForm.dialog = true;
        },
        joinTeam(teamId, type, value) {
            switch (type) {
                case 'userJoinPassword':
                    return request({
                        url: '/teamMember/create',
                        method: 'post',
                        data: { teamId: teamId, type: type, value: this.joinTeamForm.prop.joinPassword }
                    }).then(res => {
                        if (res.code == 0) {
                            console.log(res);
                            this.showAllTeams();
                            this.joinTeamForm.dialog = false;
                        }
                    });
                case 'autoJoinEmailSuffix':
                    return request({
                        url: '/teamMember/create',
                        method: 'post',
                        data: { teamId: teamId, type: type, value: '' }
                    }).then(res => {
                        if (res.code == 0) {
                            console.log(res);
                            this.showAllTeams();
                        }
                    });
            }
        },
        quitTeam(teamId) {
            return request({
                url: '/teamMember/delete',
                method: 'post',
                data: { teamId: teamId }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.showAllTeams();
                }
            });
        },
        queryMembers(teamId) {
            this.memberListForm.list = [];
            return request({
                url: '/teamMember/list',
                method: 'post',
                data: { teamId: teamId }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.memberListForm.list = res.data;
                    this.memberListForm.dialog = true;
                }
            });
        },
        openInviteUser(teamId) {
            this.inviteUserForm.prop.teamId = teamId;
            this.inviteUserForm.prop.email = '';
            this.inviteUserForm.dialog = true;
        },
        inviteUser() {
            return request({
                url: '/teamMember/invite',
                method: 'post',
                data: { teamId: this.inviteUserForm.prop.teamId, email: this.inviteUserForm.prop.email }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.showAllTeams();
                    this.inviteUserForm.dialog = false;
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
