<template>
    <el-tabs type="card" style="padding: 10px">
        <el-tab-pane label="基础信息">
            <!-- <el-row>
                <el-col :span="9">
                    <div style="text-align: center; margin-bottom: 20px">
                        <el-popover placement="bottom" trigger="manual" :width="400" v-model:visible="selectAvatarPanelVisible">
                            <template #reference>
                                
                            </template>
                            <el-row>
                                <el-col :span="24">
                                    <el-row>
                                        <el-col :span="4" v-for="item in systemAvatarList" :key="item">
                                            <el-avatar :src="require('../../../assets/avatar/' + item.name)" :size="40" style="cursor: pointer" @click="selectAvatar(item.name)"></el-avatar>
                                        </el-col>
                                    </el-row>
                                </el-col>
                            </el-row>
                        </el-popover>
                    </div>
                </el-col>
            </el-row>
            <hr /> -->

            <el-row>
                <el-col :span="16" :offset="2" style="text-align: center; padding: 30px">
                    <el-upload :action="fileUploadUrl" :data="{ bucketName: '.avatar' }" :headers="headers" :show-file-list="false" :on-success="uploadAvatarSuccess" accept="image/*">
                        <div v-if="user.avatar == undefined || user.avatar == ''">
                            <el-avatar :size="200" style="cursor: pointer">
                                <el-icon style="font-size: 60px"><UserFilled /></el-icon>
                            </el-avatar>
                        </div>
                        <div v-else>
                            <el-avatar :size="200" style="cursor: pointer" :src="currentAvatar()"></el-avatar>
                        </div>

                        <template #tip>
                            <div class="el-upload__tip">点击头像上传图片</div>
                        </template>
                    </el-upload>
                </el-col>
            </el-row>

            <el-row>
                <el-col :span="16" :offset="2">
                    <el-form label-width="120px">
                        <el-form-item label="姓名">
                            <el-col :span="16">
                                <el-input v-model="user.realName" maxlength="20" show-word-limit />
                            </el-col>
                            <el-col :span="4" :offset="1">
                                <!-- <button>更新姓名</button> -->
                            </el-col>
                        </el-form-item>
                        <el-form-item label="邮箱">
                            <el-input v-model="user.email" disabled />
                        </el-form-item>
                        <el-form-item label="文件空间">
                            <div style="width: 100%; text-align: left"><el-progress :stroke-width="30" :text-inside="true" :percentage="fileConf.spaceUsage ? 100 - fileConf.spaceUsage : 0" :format="format"></el-progress></div>
                        </el-form-item>
                    </el-form>
                </el-col>
            </el-row>

            <canvas ref="qrCodeCanvas"></canvas>
        </el-tab-pane>
        <el-tab-pane label="登录设备">
            <el-row style="padding: 50px">
                <el-col :span="16">
                    <el-table :data="tableData" style="width: 100%">
                        <el-table-column prop="ip" label="登录IP地址"></el-table-column>
                        <el-table-column prop="loginTime" label="登录时间"></el-table-column>
                        <el-table-column prop="expiryTime" label="过期时间"></el-table-column>
                        <el-table-column label="是否过期" width="100">
                            <template #default="scope">
                                <div class="name-wrapper">
                                    <div v-if="scope.row.expired">
                                        <el-tag size="small" type="danger">已过期</el-tag>
                                    </div>
                                    <div v-else>
                                        <el-tag size="small">正常</el-tag>
                                    </div>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="当前登录" width="100">
                            <template #default="scope">
                                <div class="name-wrapper">
                                    <div v-if="this.$store.state.user.token == scope.row.token">
                                        <el-tag size="small">是</el-tag>
                                    </div>
                                    <div v-else>
                                        <!-- <el-tag size="small" type="danger">-</el-tag> -->
                                    </div>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column fixed="right" label="操作" width="200">
                            <template #default="scope">
                                <el-popover placement="top-start" :width="200" trigger="click">
                                    <p>拉黑IP将导致无法通过该IP登录，确认继续吗？</p>
                                    <div style="text-align: right; margin: 0">
                                        <el-button type="danger" size="small" @click="blockIp(scope.row)">确定</el-button>
                                    </div>
                                    <template #reference>
                                        <el-button type="text" size="small">拉黑IP</el-button>
                                    </template>
                                </el-popover>

                                <el-popover placement="top-start" :width="200" trigger="click">
                                    <p>确定下线该设备吗？</p>
                                    <div style="text-align: right; margin: 0">
                                        <el-button type="danger" size="small" @click="forceLogout(scope.row)">确定</el-button>
                                    </div>
                                    <template #reference>
                                        <el-button type="text" size="small">强制下线</el-button>
                                    </template>
                                </el-popover>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
        </el-tab-pane>
        <el-tab-pane label="动态口令">
            <el-row>
                <el-col :span="12">
                    <p>‌TOTP（Time-based One-Time Password）是一种基于时间同步的一次性密码算法‌。</p>
                    <p>它通过客户端和服务器之间的时间比对，每60秒或30秒生成一个新的密码，确保了密码的动态性和不可预测性，从而提高了安全性‌。</p>
                    <p></p>
                    <el-button @click="generateTotpQrCode">生成动态口令绑定二维码</el-button>
                    <p></p>
                    <p><el-text type="danger">注意：</el-text></p>
                    <p><el-text type="danger">生成后，上一次的二维码将自动失效</el-text></p>
                    <p><el-text type="danger">二维码仅显示一次，请扫码完成绑定</el-text></p>
                    <p><el-text type="danger">可使用支持TOTP的应用扫描（如：Microsoft Authenticator）</el-text></p>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="12">
                    <div v-if="totpUri.length > 0">
                        <qrcode-vue :value="totpUri" size="200" level="H" render-as="svg" />
                    </div>
                </el-col>
            </el-row>
        </el-tab-pane>
    </el-tabs>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
import { avatarImage } from '../../../utils/users.js';
import { config } from '@/utils/config';
import { getToken } from '@/utils/auth';
import QrcodeVue from 'qrcode.vue';

export default {
    data() {
        return {
            headers: [],
            fileUploadUrl: '',
            user: {},
            fileConf: {},
            tableData: [],
            totpUri: ''
        };
    },
    components: {
        QrcodeVue
    },
    mounted() {
        this.headers['Access-Token'] = getToken();
        this.fileUploadUrl = config().baseServer + 'f/u';
        this.getCurrentUserInfo();
        this.getLoginUserInfo();
    },
    methods: {
        currentAvatar() {
            return avatarImage(this.user.avatar);
        },
        uploadAvatarSuccess(response, file, fileList) {
            debugger;
            if (response.code == 0) {
                request({
                    url: '/user/updateCurrentUserInfo',
                    method: 'post',
                    data: { type: 'avatar', value: response.data[0].uname }
                }).then(res => {
                    if (res.code == 0) {
                        this.getCurrentUserInfo();
                    }
                });
            }
        },
        getCurrentUserInfo() {
            request({
                url: '/user/currentUserInfo',
                method: 'post'
            }).then(res => {
                if (res.code == 0) {
                    this.user = res.meta.user;
                    this.fileConf = res.meta.fileConf;
                }
            });
        },
        getLoginUserInfo() {
            request({
                url: '/user/getLoginUserInfo',
                method: 'post'
            }).then(res => {
                if (res.code == 0) {
                    this.tableData = res.data;
                }
            });
        },
        format(percentage) {
            var total = parseInt(this.fileConf.spaceLimit / 1024 / 1024);
            var use = parseInt(this.fileConf.usedSpace / 1024 / 1024);
            var have = total - use;
            return percentage === 100 ? '满' : percentage + '% (剩余：' + have + 'MB)';
        },
        onSubmit() {
            console.log('submit!');
        },
        blockIp() {
            ElMessage({
                message: '功能暂未实现，敬请期待',
                type: 'warning',
                duration: 5 * 1000
            });
        },
        forceLogout(data) {
            console.log(data);
            request({
                url: '/user/logout',
                method: 'post',
                data: data
            }).then(res => {
                if (res.code == 0) {
                    ElMessage({
                        message: '下线成功',
                        type: 'success',
                        duration: 5 * 1000
                    });
                } else {
                    ElMessage({
                        message: '下线失败',
                        type: 'warning',
                        duration: 5 * 1000
                    });
                }
                this.getLoginUserInfo();
            });
        },
        generateTotpQrCode() {
            request({
                url: '/user/createTotpUri',
                method: 'post',
                data: { type: 'totp' }
            }).then(res => {
                if (res.code == 0 && res.meta && res.meta.uri) {
                    this.totpUri = res.meta.uri;
                    ElMessage({
                        message: '生成成功',
                        type: 'success',
                        duration: 5 * 1000
                    });
                } else {
                    ElMessage({
                        message: '生成失败',
                        type: 'warning',
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
