<template>
    <el-tabs type="card" @tab-click="handleClick" style="padding:10px">
        <el-tab-pane label="基础信息">
            <el-row style="padding:50px">
                <el-col :span="16">
                    <el-form ref="form" :model="form" label-width="180px">
                        <el-form-item label="姓名（中文）">
                            <el-input v-model="form.name"></el-input>
                        </el-form-item>

                        <el-form-item label="邮箱">
                            <el-input v-model="form.email"></el-input>
                        </el-form-item>

                        <el-form-item label="文件空间">
                            <el-progress :stroke-width="30" :text-inside="true" :percentage="this.fileConf.spaceUsage" :format="format"></el-progress>
                        </el-form-item>
                    </el-form>
                </el-col>
            </el-row>
        </el-tab-pane>
        <el-tab-pane label="登录设备">
            <el-row style="padding:50px">
                <el-col :span="16">
                    <el-table :data="tableData" style="width: 100%">
                        <el-table-column prop="ip" label="登录IP地址" ></el-table-column>
                        <el-table-column prop="loginTime" label="登录时间"></el-table-column>
                        <el-table-column prop="expiryTime" label="过期时间"></el-table-column>
                        <!-- <el-table-column label="最近更新" width="100">
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
                        </el-table-column> -->
                        <el-table-column fixed="right" label="操作" width="200">
                            <template #default="scope">
                                <el-button @click="previewDoc(scope.row)" type="text" size="small">拉黑IP</el-button>
                                <el-button @click="docEdit(scope.row)" type="text" size="small">下线</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
        </el-tab-pane>
    </el-tabs>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
export default {
    data() {
        return {
            form: {
                name: '',
                email: ''
            },
            user: {},
            fileConf: {},
            tableData: []
        };
    },
    mounted() {
        request({
            url: '/user/currentUserInfo',
            method: 'post'
        }).then(res => {
            if (res.code == 0) {
                this.user = res.meta.user;
                this.fileConf = res.meta.fileConf;

                this.form.name = this.user.realName;
                this.form.email = this.user.email;
            }
        });

        request({
            url: '/user/getLoginUserInfo',
            method: 'post'
        }).then(res => {
            if (res.code == 0) {
                this.tableData = res.data;
            }
        });
    },
    methods: {
        format(percentage) {
            return percentage === 100 ? '满' : `${percentage}%`;
        },
        onSubmit() {
            console.log('submit!');
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
