<template>
    <el-container>
        <el-header height="30px">
            <el-row>
                <el-alert title="您可以在此查看关于团队设定的相关信息" type="success" :closable="false" />
            </el-row>
        </el-header>
        <el-main>
            <el-tabs type="card" style="padding: 10px">
                <el-tab-pane label="我加入的团队">
                    <el-row style="padding: 10px">
                        <el-col :span="24">
                            <el-table :data="this.teams.join" style="width: 100%">
                                <el-table-column prop="name" label="名称"></el-table-column>
                                <el-table-column prop="description" label="描述"></el-table-column>
                                <el-table-column prop="createTime" label="创建时间"></el-table-column>
                                <el-table-column prop="ownerUser.realName" label="所属人"></el-table-column>
                                <el-table-column fixed="right" label="管理" width="200">
                                    <template #default="scopeOp">
                                        <el-link type="success" style="font-size: 12px; margin-right: 10px" @click="setAdmin(scopeOp.row, false)">邀请成员</el-link>
                                        <el-link type="danger" style="font-size: 12px; margin-right: 10px" @click="setAdmin(scopeOp.row, true)">退出团队</el-link>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </el-col>
                    </el-row>
                </el-tab-pane>
                <el-tab-pane label="我管理的团队" v-if="user.roles.includes('sa')">
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
                                <el-table-column prop="ownerUser.realName" label="所属人" width="180"></el-table-column>
                                <el-table-column prop="memberCount" label="团队人数" width="180"></el-table-column>
                                <el-table-column fixed="right" label="管理">
                                    <template #default="scopeOp">
                                        <el-link type="warning" style="font-size: 12px; margin-right: 10px" @click="setAdmin(scopeOp.row, true)">编辑信息</el-link>
                                        <el-link type="success" style="font-size: 12px; margin-right: 10px" @click="setAdmin(scopeOp.row, false)">管理成员</el-link>
                                        <el-link  style="font-size: 12px; margin-right: 10px" @click="previewDoc(scopeOp.row)">团队转让</el-link>
                                        <el-link type="danger" style="font-size: 12px; margin-right: 10px" @click="previewDoc(scopeOp.row)">删除团队</el-link>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </el-col>
                    </el-row>
                </el-tab-pane>
                <el-tab-pane label="其他团队">
                    <el-row style="padding: 10px">
                        <el-col :span="24">
                            <el-table :data="this.teams.other" style="width: 100%">
                                <el-table-column prop="name" label="名称"></el-table-column>
                                <el-table-column prop="description" label="描述"></el-table-column>
                                <el-table-column prop="createTime" label="创建时间"></el-table-column>
                                <el-table-column prop="ownerUser.realName" label="所属人"></el-table-column>
                                <el-table-column fixed="right" label="管理" width="200">
                                    <template #default="scopeOp">
                                        <el-link type="warning" style="font-size: 12px; margin-right: 10px" @click="setAdmin(scopeOp.row, true)">自动加入</el-link>
                                        <el-link type="success" style="font-size: 12px; margin-right: 10px" @click="setAdmin(scopeOp.row, false)">密码加入</el-link>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </el-col>
                    </el-row>
                </el-tab-pane>
            </el-tabs>
        </el-main>
    </el-container>

    <!-- Form -->
    <el-dialog :title="this.teamForm.mode == 'create' ? '创建团队' : '编辑团队'" v-model="this.teamForm.dialog">
        <el-form :model="this.teamForm.prop">
            <el-form-item label="名称" label-width="100px">
                <el-input v-model="this.teamForm.prop.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="描述" label-width="100px">
                <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="this.teamForm.prop.description"></el-input>
            </el-form-item>
            <el-form-item label="自动加入邮箱后缀" label-width="100px">
                <el-input v-model="this.teamForm.prop.joinRule.autoJoinEmailSuffix" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="自助加入密码" label-width="100px">
                <el-input v-model="this.teamForm.prop.joinRule.userJoinPassword" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="this.teamForm.dialog = false">取 消</el-button>
                <el-button type="primary" @click="saveTeam()">保 存</el-button>
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
            user: { roles: [] },
            teams: {
                join: [],
                owner: [],
                other: []
            },
            teamForm: {
                mode: 'create',
                dialog: false,
                prop: {
                    name: '',
                    description: '',
                    joinRule: { autoJoinEmailSuffix: '', userJoinPassword: '' }
                }
            },
            currentMachineList: []
        };
    },
    mounted() {
        this.user.roles = this.$store.state.user.roles;
        this.showOwnerTeams();
        this.showOtherTeams();
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
        showTeamList() {},
        showJoinTeams() {
            return request({
                url: '/team/getJoinTeams',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.teams.join = res.data;
                }
            });
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
        showOtherTeams() {
            return request({
                url: '/team/getOtherTeams',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.teams.other = res.data;
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
