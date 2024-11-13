<template>
    <el-container>
        <el-main>
            <el-row class="login-main">
                <el-col :xs="24" :sm="10" :md="10" :lg="10" :xl="10">
                    <div class="grid-content" style="text-align: center; cursor: pointer;margin-top: 50px;" @click="home">
                        <img alt="logo" src="../..//assets/logo.png" width="96" />
                        <div style="height: 50px">
                            <div style="text-align: center; font-weight: bold">hidoc</div>
                        </div>
                    </div>
                </el-col>
                <el-col :xs="24" :sm="12" :md="8" :lg="8" :xl="8">
                    <div style="text-align: center; margin-bottom: 20px">
                        <el-popover placement="bottom" trigger="manual" :width="400" v-model:visible="selectAvatarPanelVisible">
                            <template #reference>
                                <div v-if="form.avatar == undefined || form.avatar == ''">
                                    <el-avatar :size="100" style="cursor: pointer" @click="openSelectAvatar()">头像</el-avatar>
                                </div>
                                <div v-else>
                                    <el-avatar :size="100" style="cursor: pointer" @click="openSelectAvatar()" :src="currentAvatar()">头像</el-avatar>
                                </div>
                            </template>
                            <el-row>
                                <el-col :span="24">
                                    <el-row>
                                        <el-col :span="4" v-for="item in systemAvatarList" :key="item">
                                            <el-avatar :src="require('../../assets/avatar/' + item.name)" :size="40" style="cursor: pointer" @click="selectAvatar(item.name)"></el-avatar>
                                        </el-col>
                                    </el-row>
                                </el-col>
                            </el-row>
                        </el-popover>
                    </div>
                    <el-form ref="form" :model="form" :rules="rules" status-icon label-width="130px">
                        <el-form-item label="头像" v-show="false">
                            <el-input v-model="form.avatar" maxlength="64"></el-input>
                        </el-form-item>
                        <el-form-item label="登录账号" prop="username">
                            <el-input id="username" v-model="form.username" maxlength="64" placeholder="请输入姓名拼音或工号，至少6位，可使用字母数字下划线"></el-input>
                        </el-form-item>
                        <el-form-item label="姓名" prop="realname">
                            <el-input v-model="form.realname" maxlength="16" placeholder="请输入您的姓名，中文名"></el-input>
                        </el-form-item>
                        <el-form-item label="密码" prop="password">
                            <el-input id="password" type="password" v-model="form.password" maxlength="64" autocomplete="off" placeholder="请输入密码，至少6位，可使用字母数字下划线"></el-input>
                        </el-form-item>
                        <el-form-item label="确认密码" prop="passwordConfirm">
                            <el-input type="password" v-model="form.passwordConfirm" maxlength="64" autocomplete="off" placeholder="请再次输入密码，以确认准确无误"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱" prop="email">
                            <el-input v-model="form.email" maxlength="64" placeholder="请输入公司邮箱"></el-input>
                            <el-button type="text" @click="getAuthCode">获取验证码</el-button>
                        </el-form-item>
                        <el-form-item label="验证码" prop="authCode">
                            <el-input v-model="form.authCode" maxlength="64" placeholder="请填写发送到您邮箱的验证码"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="onSubmit('form')">注册</el-button>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="text" @click="back">
                                <i class="el-icon-back"></i>
                                返回
                            </el-button>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="text" @click="login">
                                已有账号，立即登录
                                <i class="el-icon-right"></i>
                            </el-button>
                        </el-form-item>
                    </el-form>
                </el-col>
            </el-row>
        </el-main>
    </el-container>
</template>

<script>
import request from '../../utils/request.js';
import { ElMessageBox, ElMessage } from 'element-plus';
import { avatarImage } from '../../utils/users.js';
export default {
    data() {
        return {
            form: {
                avatar: '',
                username: '',
                realname: '',
                password: '',
                passwordConfirm: '',
                email: '',
                authCode: '',
                uid: ''
            },
            rules: {
                username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
                realname: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
                passwordConfirm: [{ required: true, message: '请再次输入密码', trigger: 'blur' }],
                email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }]
            },
            systemAvatarList: [],
            selectAvatarPanelVisible: false
        };
    },
    mounted() {
        document.title = '注册';
        for (let i = 1; i <= 96; i++) {
            this.systemAvatarList.push({ type: 'system', name: i + '.jpg' });
        }
    },
    methods: {
        home() {
            this.$router.push({ path: '/', params: {} });
        },
        back() {
            this.$router.go(-1); //返回上一层
            // this.$router.push({ path: '/' });
        },
        login() {
            this.$router.push({ path: '/login', params: {} });
        },
        openSelectAvatar() {
            this.selectAvatarPanelVisible = true;
            console.log('打开头像选择器');
        },
        selectAvatar(name) {
            this.form.avatar = '$system$' + name;
            this.selectAvatarPanelVisible = false;
            console.log('关闭头像选择器');
        },
        currentAvatar() {
            return avatarImage(this.form.avatar);
            // return require('../../assets/avatar/' + this.form.avatar.replace('$system$', ''));
        },
        getAuthCode() {
            request({
                url: '/openapi/authcode/getForRegister',
                method: 'post',
                data: {
                    email: this.form.email,
                    token: ''
                }
            }).then(res => {
                if (res.code == 0) {
                    this.form.uid = res.meta.uid;

                    ElMessage({
                        message: '验证码已发送至邮箱，请查收',
                        type: 'success',
                        duration: 5 * 1000
                    });
                }
            });
        },
        onSubmit(formName) {
            console.log('submit!');
            this.$refs[formName].validate(valid => {
                if (valid) {
                    this.$store
                        .dispatch('user/register', this.form)
                        .then(() => {
                            console.log(1);
                            this.$router.push({ path: this.redirect || '/', query: this.otherQuery });
                            // this.loading = false;
                        })
                        .catch(() => {
                            console.log(2);
                            // this.loading = false;
                        });
                } else {
                    console.log('error submit!!');
                    return false;
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
    text-align: center;
    margin-left: 60px;
    margin-right: 60px;
}
</style>
