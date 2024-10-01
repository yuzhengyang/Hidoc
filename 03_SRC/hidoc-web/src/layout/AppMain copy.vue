<template>
    <el-container style="height: 100%">
        <!-- 标题区域 -->
        <el-header class="app-main-header">
            <el-row>
                <el-col :xs="20" :sm="11" :md="11" :lg="7" :xl="7">
                    <div class="grid-content bg-purple" @click="home" style="cursor: pointer; height: 40px; width: 55px; float: left"><img alt="logo" src="../assets/logo.png" height="40" /></div>
                    <div style="cursor: pointer; height: 40px; width: 55px; float: left; font-weight: bold; font-size: 14px">hidoc</div>
                    <div style="float: left; font: 14px">
                        <el-button type="text" size="small" @click="gotoPage('preview')">文集</el-button>
                        <el-button type="text">|</el-button>
                        <el-button type="text" size="small" @click="gotoPage('fileshare')">共享文件</el-button>
                        <el-button type="text">|</el-button>
                        <el-button type="text" size="small" @click="gotoPage('javadoc')">代码注释</el-button>
                    </div>
                </el-col>
                <el-col :xs="3" :sm="12" :md="12" :lg="16" :xl="16">
                    <div v-if="this.$store.state.user.token == undefined || this.$store.state.user.token == ''" class="grid-content">
                        <el-link :underline="false" @click="register" style="font-size: 12px">
                            <el-icon><User /></el-icon>
                            <span>&nbsp;注册</span>
                        </el-link>
                        <el-link :underline="false" type="text" style="padding-left: 8px; padding-right: 8px">|</el-link>
                        <el-link :underline="false" @click="login" style="font-size: 12px">登录</el-link>

                        <!-- <el-dropdown>
                            <el-button type="text">
                                <i class="el-icon-ship"></i>
                                访客
                                <i class="el-icon-arrow-down el-icon--right"></i>
                            </el-button>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item @click="register">
                                        <i class="el-icon-user"></i>
                                        注册
                                    </el-dropdown-item>
                                    <el-dropdown-item @click="login">
                                        <i class="el-icon-thumb"></i>
                                        登录
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown> -->
                    </div>
                    <div v-else class="grid-content">
                        <el-link :underline="false" @click="workbench" style="font-size: 12px">
                            <el-icon><Monitor /></el-icon>
                            <span>&nbsp;工作台</span>
                        </el-link>
                        <el-link :underline="false" type="text" style="padding-left: 8px; padding-right: 8px">|</el-link>
                        <el-dropdown style="vertical-align: middle">
                            <el-link :underline="false" style="font-size: 12px">
                                <el-avatar :size="22" :src="currentAvatar(this.$store.state.user.avatar)" style="margin: 5px" shape="square" />
                                <!-- {{ this.$store.state.user.name }} -->
                                <el-icon><ArrowDown /></el-icon>
                            </el-link>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item @click="changePasswordDialogVisible = true">
                                        <el-icon><Key /></el-icon>
                                        修改密码
                                    </el-dropdown-item>
                                    <el-dropdown-item @click="logout">
                                        <el-icon><CircleClose /></el-icon>
                                        退出
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                </el-col>
            </el-row>
        </el-header>
        <!-- 内容区域 -->
        <el-main>
            <router-view />

            <el-backtop></el-backtop>
        </el-main>
        <!-- 底部区域 -->
        <!-- <el-footer height="40px">
            Copyright © 2021-2025 Hidoc. All rights reserved.<br />
            Web site developed by <a href="https://github.com/" target="_blank">@yuzhengyang</a> <a href="mailto:YUZHENGYANG@HISENSE.COM">Contact Us</a>
        </el-footer> -->
    </el-container>

    <!-- 修改密码弹框 -->
    <el-dialog title="修改密码" v-model="changePasswordDialogVisible">
        <el-form :model="changePasswordForm">
            <el-form-item label="新的密码">
                <el-input v-model="changePasswordForm.password" placeholder="请输入密码" show-password></el-input>
            </el-form-item>
            <el-form-item label="再输入一遍">
                <el-input v-model="changePasswordForm.password2" placeholder="请再次输入密码" show-password></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="changePasswordDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="changePassword">确 定</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
import request from '../utils/request.js';
import { avatarImage } from '../utils/users.js';
import watermark from '../utils/watermark';

export default {
    name: 'AppMain',
    components: {},
    data() {
        return {
            keyword: '',
            changePasswordDialogVisible: false,
            changePasswordForm: {
                password: '',
                password2: ''
            }
        };
    },
    mounted() {
        var user = this.$store.state.user;
        console.log(user);
        if (user && user.realName && user.email) {
            watermark.set(user.realName, user.email);
        }
    },
    methods: {
        gotoPage(page) {
            if (page == 'preview') {
                // let routeData = this.$router.resolve({
                //     name: 'preview',
                //     params: {}
                // });
                // window.open(routeData.path, '_blank');
                this.$router.push({ path: '/', params: {} });
            }
            if (page == 'javadoc') {
                // let routeData = this.$router.resolve({
                //     name: 'javadoc',
                //     params: {}
                // });
                // window.open(routeData.path, '_blank');
                this.$router.push({ path: '/javadoc', params: {} });
            }
            if (page == 'fileshare') {
                this.$router.push({ path: '/fileshare', params: {} });
            }
        },
        handleOpen(key, keyPath) {
            console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        },
        handleChange(val) {
            console.log(val);
        },
        handleClick(tab, event) {
            console.log(tab, event);
        },
        search() {
            // alert(base_server + '搜索内容~' + this.keyword);
        },
        home() {
            this.$router.push({ path: '/', params: {} });
        },
        workbench() {
            this.$router.push({ path: '/workbench', params: {} });
        },
        register() {
            this.$router.push({ path: '/register', params: {} });
        },
        login() {
            this.$router.push({ path: '/login', params: {} });
        },
        async logout() {
            await this.$store.dispatch('user/logout');
            this.$router.push(`/login?redirect=${this.$route.fullPath}`);
        },
        changePassword() {
            return request({
                url: '/user/changePassword',
                method: 'post',
                data: { password: this.changePasswordForm.password, password2: this.changePasswordForm.password2 }
            }).then(res => {
                if (res.code == 0) {
                    this.changePasswordDialogVisible = false;
                }
            });
        },
        currentAvatar(createUserAvatar) {
            return avatarImage(createUserAvatar);
        }
    }
};
</script>

<style>
html,
body,
#app,
#root {
    height: 100%;
    margin: 0;
    padding: 0;
}
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
    /* text-align: center; */
    /* margin-top: 60px; */
}
.app-main-header {
    background-color: #d1d3d4;
    color: #333;
    text-align: center;
    height: 40px;
    line-height: 40px;
}
.el-main {
    /* background-color: #e9eef3; */
    /* color: #333; */
    padding: 10px;
    text-align: left;
}
/* .el-footer {
    font-size: 10px;
    padding: 5px;
    background-color: #d1d3d4;
    color: #333;
    text-align: center;
} */
</style>
