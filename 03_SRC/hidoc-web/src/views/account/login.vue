<template>
    <el-container>
        <el-main>
            <el-row class="login-main">
                <el-col :span="9">
                    <div class="grid-content" style="text-align: right; cursor: pointer" @click="home">
                        <img alt="logo" src="../..//assets/logo.png" width="96" />
                        <div style="height: 150px;">
                            <div style="width: 100px;float: right; text-align: center;font-weight: bold;">hidoc</div>
                        </div>
                    </div>
                </el-col>
                <el-col :span="1"></el-col>
                <el-col :span="8">
                    <el-form ref="loginForm" :model="form" :rules="rules" status-icon label-width="80px">
                        <el-form-item label="登录账号" prop="username">
                            <el-input id="username" v-model="form.username" maxlength="64" placeholder="请输入您的登录账号"></el-input>
                        </el-form-item>
                        <el-form-item label="密码" prop="password">
                            <el-input id="password" type="password" v-model="form.password" maxlength="64" autocomplete="off" placeholder="请输入您的密码"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="onSubmit()" style="width: 200px">登录</el-button>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="text" @click="back">
                                <i class="el-icon-back"></i>
                                返回
                            </el-button>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="text" @click="openResetPasswordDialog">
                                <i class="el-icon-question"></i>
                                忘记密码
                            </el-button>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="text" @click="register">
                                <i class="el-icon-user"></i>
                                注册账号
                            </el-button>
                        </el-form-item>
                    </el-form>
                </el-col>
                <el-col :span="6"></el-col>
            </el-row>
        </el-main>
    </el-container>

    <!-- 修改密码弹框 -->
    <el-dialog title="修改密码" v-model="resetPasswordDialogVisible">
        <el-form :model="changePasswordForm">
            <el-form-item label="邮箱" prop="email">
                <el-input v-model="resetPasswordForm.email" maxlength="64" placeholder="请输入您的注册邮箱"></el-input>
                <el-button type="text" @click="getAuthCodeForResetPassword">获取验证码</el-button>
            </el-form-item>
            <!-- <el-form-item label="账号(选填)" prop="username">
                <el-input id="username" v-model="resetPasswordForm.username" maxlength="64" placeholder="请输入您的登录账号，忘记时可以不填"></el-input>
            </el-form-item> -->
            <el-form-item label="验证码" prop="authCode">
                <el-input id="authCode" v-model="resetPasswordForm.authCode" maxlength="64" placeholder="请填写发送到您邮箱的验证码"></el-input>
            </el-form-item>
            <el-form-item label="新的密码">
                <el-input id="password" v-model="resetPasswordForm.password" placeholder="请输入密码" show-password></el-input>
            </el-form-item>
            <el-form-item label="再输入一遍">
                <el-input v-model="resetPasswordForm.passwordConfirm" placeholder="请再次输入密码" show-password></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="resetPasswordDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="resetPassword">确 定</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
import { ref, watch } from 'vue';
import request from '../../utils/request.js';
import { ElMessageBox, ElMessage } from 'element-plus';
export default {
    data() {
        return {
            resetPasswordDialogVisible: false,
            form: {
                username: '',
                password: ''
            },
            rules: {
                username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
            },
            resetPasswordForm: {
                username: '',
                email: '',
                authCode: '',
                uid: '',
                password: '',
                passwordConfirm: ''
            }
        };
    },
    setup() {},
    mounted() {
        document.title = '登录';
    },
    methods: {
        home() {
            this.$router.push({ path: '/', params: {} });
        },
        back() {
            this.$router.go(-1); //返回上一层
            // this.$router.push({ path: '/' });
        },
        register() {
            this.$router.push({ path: '/register', params: {} });
        },
        onSubmit() {
            console.log('submit!');
            this.$refs.loginForm.validate(valid => {
                if (valid) {
                    // alert('submit!');
                    // this.loading = true;
                    console.log(this.$route);
                    let redirectSign = 'redirect=';
                    let redirect = this.$route.fullPath.substring(this.$route.fullPath.indexOf(redirectSign) + redirectSign.length);

                    this.$store
                        .dispatch('user/login', this.form)
                        .then(() => {
                            console.log('登录成功');
                            this.$router.push({ path: redirect || '/', params: {} });
                            // this.loading = false;
                        })
                        .catch(() => {
                            console.log('登录异常');
                            // this.loading = false;
                        });
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        openResetPasswordDialog() {
            this.resetPasswordDialogVisible = true;
        },
        getAuthCodeForResetPassword() {
            request({
                url: '/openapi/authcode/getForResetPassword',
                method: 'post',
                data: {
                    email: this.resetPasswordForm.email,
                    token: ''
                }
            }).then(res => {
                if (res.code == 0) {
                    this.resetPasswordForm.uid = res.meta.uid;
                    ElMessage({
                        message: '验证码已发送至邮箱，请查收',
                        type: 'success',
                        duration: 5 * 1000
                    });
                }
            });
        },
        resetPassword() {
            request({
                url: '/user/resetPassword',
                method: 'post',
                data: this.resetPasswordForm
            }).then(res => {
                if (res.code == 0) {
                    ElMessage({
                        message: '重置密码成功',
                        type: 'success',
                        duration: 5 * 1000
                    });
                    this.resetPasswordDialogVisible = false;
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
    /* text-align: right; */
}
.login-main {
    display: -webkit-flex; /* Safari */
    display: flex;
    align-items: center; /* 主要是这两行代码 */
    font-size: 16px;
    height: 550px;
}
</style>
