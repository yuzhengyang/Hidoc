<template>
    <el-container style="height:100%;">
        <!-- 标题区域 -->
        <el-header class="app-main-header">
            <el-row>
                <el-col :span="4">
                    <div class="grid-content bg-purple" @click="home" style="cursor:pointer;height:55px;"><img alt="Vue logo" src="../assets/logo.png" height="60"></div>
                </el-col>
                <el-col :span="4">
                    <div class="grid-content bg-purple-light"></div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content bg-purple-light">
                        <!-- <el-input placeholder="搜索一下" v-model="keyword">
                            <template #suffix>
                                <i class="el-input__icon el-icon-search" style="cursor:pointer;" @click="search"></i>
                            </template>
                        </el-input> -->
                    </div>
                </el-col>
                <el-col :span="6">
                    <div class="grid-content bg-purple">
                    </div>
                </el-col>
                <el-col :span="4">
                    <div v-if="this.$store.state.user.token == undefined || this.$store.state.user.token == ''" class="grid-content bg-purple-light">
                        <el-dropdown>
                            <el-button type="text">
                                <i class="el-icon-ship"></i> 访客<i class="el-icon-arrow-down el-icon--right"></i>
                            </el-button>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item @click="register"><i class="el-icon-user"></i> 注册 </el-dropdown-item>
                                    <el-dropdown-item @click="login"><i class="el-icon-thumb"></i> 登录 </el-dropdown-item>
                                </el-dropdown-menu>
                            </template>
                        </el-dropdown>
                    </div>
                    <div v-else class="grid-content bg-purple-light">
                        <el-dropdown>
                            <el-button type="text">
                                <i class="el-icon-ship"></i>你好~ {{this.$store.state.user.name}}<i class="el-icon-arrow-down el-icon--right"></i>
                            </el-button>
                            <template #dropdown>
                                <el-dropdown-menu>
                                    <el-dropdown-item @click="workbench"><i class="el-icon-monitor"></i> 进入工作台 </el-dropdown-item>
                                    <el-dropdown-item @click="changePasswordDialogVisible = true"><i class="el-icon-key"></i> 修改密码 </el-dropdown-item>
                                    <el-dropdown-item @click="logout"><i class="el-icon-circle-close"></i> 退出 </el-dropdown-item>
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
        <el-footer height="40px">
            Copyright © 2021-2025 Hidoc. All rights reserved.<br />
            Web site developed by <a href="https://github.com/" target="_blank">@yuzhengyang</a> <a href="mailto:YUZHENGYANG@HISENSE.COM">Contact Us</a>
        </el-footer>
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
    methods: {
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
    line-height: 60px;
}
.el-main {
    /* background-color: #e9eef3; */
    /* color: #333; */
    padding: 10px;
    text-align: left;
}
.el-footer {
    font-size: 10px;
    padding: 5px;
    background-color: #d1d3d4;
    color: #333;
    text-align: center;
}
</style>
