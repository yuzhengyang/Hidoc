<template>
    <div class="loginMain">
        <section class="loginSection">
            <div class="color"></div>
            <div class="color"></div>
            <div class="color"></div>
            <div class="box">
                <div class="square" style="--i:0;"></div>
                <div class="square" style="--i:1;"></div>
                <div class="square" style="--i:2;"></div>
                <div class="square" style="--i:3;"></div>
                <div class="square" style="--i:4;"></div>
                <div class="container">
                    <div class="form">
                        <h2>login form</h2>
                        <form autocomplete="off" @submit.prevent="onSubmit">
                            <div class="inputBox">
                                <input type="text" placeholder="Username" v-model="form.username" />
                            </div>
                            <div class="inputBox">
                                <input type="password" placeholder="Password" v-model="form.password" />
                            </div>
                            <div class="inputBox">
                                <input type="submit" value="Login" />
                            </div>
                            <p class="forget">Forgot Password ? <a href="#">Click Here</a></p>
                            <p class="forget">Don't have an account ? <a href="#">Sign up</a></p>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
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
        back() {
            this.$router.go(-1); //返回上一层
        },
        onSubmit() {
            console.log('submit!');
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
            return false;
        }
    }
};
</script>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}
.loginMain {
    overflow: hidden;
    width: 100%
}
.loginSection {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    width: 100%;
    /* background: linear-gradient(to bottom, #f1f4f9, #dff1ff); */
    background: linear-gradient(to bottom, #f1f4f9, #df919f);
}
.loginSection .color {
    position: absolute;
    filter: blur(150px);
}
.loginSection .color:nth-child(1) {
    top: -350px;
    width: 600px;
    height: 600px;
    background: #ff359b;
}
.loginSection .color:nth-child(2) {
    bottom: 50px;
    left: 100px;
    width: 500px;
    height: 500px;
    background: #fffd87;
}
.loginSection .color:nth-child(3) {
    bottom: 50px;
    right: 100px;
    width: 300px;
    height: 300px;
    background: #00d2ff;
}
.box {
    position: relative;
}
.box .square {
    position: absolute;
    backdrop-filter: blur(5px);
    box-shadow: 0 25px 45px rgba(0, 0, 0, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.5);
    border-right: 1px solid rgba(255, 255, 255, 0.2);
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
    background: rgba(255, 255, 255, 0.1);
    border-radius: 10px;
    animation: animate 10s linear infinite;
    animation-delay: calc(-1s * var(--i));
}
@keyframes animate {
    0%,
    100% {
        transform: translateY(-40px);
    }
    50% {
        transform: translateY(40px);
    }
}
.box .square:nth-child(1) {
    top: -50px;
    right: -60px;
    width: 100px;
    height: 100px;
}
.box .square:nth-child(2) {
    top: 150px;
    left: -100px;
    width: 120px;
    height: 120px;
    z-index: 2;
}
.box .square:nth-child(3) {
    bottom: 50px;
    right: -60px;
    width: 80px;
    height: 80px;
    z-index: 2;
}
.box .square:nth-child(4) {
    bottom: -80px;
    left: 100px;
    width: 50px;
    height: 50px;
}
.box .square:nth-child(5) {
    top: -80px;
    left: 140px;
    width: 60px;
    height: 60px;
}
.container {
    position: relative;
    width: 400px;
    min-height: 400px;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    backdrop-filter: blur(5px);
    box-shadow: 0 25px 45px rgba(0, 0, 0, 0.1);
    border: 1px solid rgba(255, 255, 255, 0.5);
    border-right: 1px solid rgba(255, 255, 255, 0.2);
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}
.form {
    position: relative;
    width: 100%;
    height: 100%;
    padding: 40px;
}
.form h2 {
    position: relative;
    color: #fff;
    font-size: 24px;
    font-weight: 600;
    letter-spacing: 1px;
    margin-bottom: 40px;
}
.form h2::before {
    content: '';
    position: absolute;
    left: 0;
    bottom: -10px;
    width: 88px;
    height: 4px;
    background: #fff;
}
.form .inputBox {
    width: 100%;
    margin-top: 20px;
}
.form .inputBox input {
    width: 100%;
    background: rgba(255, 255, 255, 0.2);
    border: none;
    outline: none;
    padding: 10px 20px;
    border-radius: 35px;
    border: 1px solid rgba(255, 255, 255, 0.5);
    border-right: 1px solid rgba(255, 255, 255, 0.2);
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
    font-size: 16px;
    letter-spacing: 1px;
    color: #fff;
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}
.form .inputBox input::placeholder {
    color: #fff;
}
.form .inputBox input[type='submit'] {
    background: #fff;
    color: #666;
    max-width: 100px;
    cursor: pointer;
    margin-bottom: 20px;
    font-weight: 600;
}
.forget {
    margin-top: 5px;
    color: #fff;
}
.forget a {
    color: #fff;
    font-weight: 600;
}
</style>