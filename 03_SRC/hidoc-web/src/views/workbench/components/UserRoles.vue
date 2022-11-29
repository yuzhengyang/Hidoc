<template>
    <el-container>
        <el-header height="30px">
            <el-row>
                <el-alert title="超级管理员（sa）可在此处对用户进行权限设置" type="success" :closable="false" />
            </el-row>
        </el-header>
        <el-main>
            <!-- <el-avatar :size="60" :src="currentAvatar(item.avatar)" /> -->
            <el-row>
                <el-col :offset="0" :span="24">
                    <el-table :data="userList" stripe style="width: 100%">
                        <el-table-column prop="name" label="账号" width="160" />
                        <el-table-column prop="realName" label="姓名" width="140" />
                        <el-table-column prop="email" label="邮箱" width="240" />
                        <el-table-column prop="createTime" label="创建时间" width="180" />
                        <el-table-column label="最近登录" width="120">
                            <template #default="scope_loginTime">
                                <span>{{ setDatetimeDiff(scope_loginTime.row.loginTime) }}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="在线" width="80">
                            <template #default="scope_isOnline">
                                <el-tag v-if="scope_isOnline.row.isOnline" type="success" size="small">在线</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column label="冻结" width="80">
                            <template #default="scope_isFrozen">
                                <el-tag v-if="scope_isFrozen.row.isFrozen" type="danger" size="small">冻结</el-tag>
                            </template>
                        </el-table-column>
                        <el-table-column prop="roles" label="权限" width="120" />
                        <el-table-column label="操作">
                            <template #default="scopeOp">
                                <el-link type="success" style="font-size: 12px; margin-right: 10px" @click="setAdmin(scopeOp.row, true)">设为管理员</el-link>
                                <el-link type="warning" style="font-size: 12px; margin-right: 10px" @click="setAdmin(scopeOp.row, false)">取消管理员</el-link>
                                <el-link type="danger" style="font-size: 12px; margin-right: 10px" @click="previewDoc(scopeOp.row)">冻结</el-link>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
        </el-main>
    </el-container>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
import { avatarImage } from '../../../utils/users.js';
import { datetimeDiff } from '../../../utils/datetime.js';
import * as moment from 'moment';

export default {
    data() {
        return {
            userList: []
        };
    },
    mounted() {
        this.loadUserList();
    },
    components: {},
    methods: {
        setDatetimeDiff(dt) {
            return datetimeDiff(dt);
        },
        currentAvatar(createUserAvatar) {
            return avatarImage(createUserAvatar);
        },
        loadUserList() {
            return request({
                url: '/user/getUsers',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.userList = res.data;
                }
            });
        },
        setAdmin(row, op) {
            return request({
                url: '/user/setAdmin',
                method: 'post',
                data: { token: this.$store.state.user.token, userId: row.id, op: op }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.loadUserList();
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
