<template>
    <el-container>
        <el-main>
            <el-row class="login-main">
                <el-col :span="9">
                    <div class="grid-content" style="text-align: right;cursor:pointer" @click="home"><img alt="Vue logo" src="../..//assets/logo.png">
                        <div style="height:150px;"></div>
                    </div>
                </el-col>
                <el-col :span="1">
                </el-col>
                <el-col :span="8">
                    <el-form ref="loginForm" :model="form" :rules="rules" status-icon label-width="80px">
                        <el-form-item label="账号" prop="username">
                            <el-input v-model="form.username" maxlength="64"></el-input>
                        </el-form-item>
                        <el-form-item label="密码" prop="password">
                            <el-input type="password" v-model="form.password" maxlength="64" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="onSubmit()">登录</el-button>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="text" @click="back"><i class="el-icon-back"></i> 返回</el-button>
                        </el-form-item>
                    </el-form>
                </el-col>
                <el-col :span="6">
                </el-col>
            </el-row>
        </el-main>
    </el-container>
</template>

<script>
export default {
    data() {
        return {
            form: {
                username: '',
                password: ''
            },
            rules: {
                username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
            }
        };
    },
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
        onSubmit() {
            console.log('submit!');
            this.$refs.loginForm.validate(valid => {
                if (valid) {
                    // alert('submit!');
                    // this.loading = true;
                    this.$store
                        .dispatch('user/login', this.form)
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
}
</style>