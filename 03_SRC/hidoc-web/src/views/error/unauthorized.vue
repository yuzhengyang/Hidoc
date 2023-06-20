<template>
    <el-result icon="error" title="Error Tip" sub-title="您正在访问未经授权的内容，具体请联系内容发布方">
        <template #extra>
            <el-button @click="home">去首页</el-button>
            <el-button type="primary" @click="back">返回上一页</el-button>
        </template>
    </el-result>
</template>

<script>
// import errGif from '@/assets/401_images/401.gif'

export default {
    name: 'unauthorized',
    data() {
        return {
            //   errGif: errGif + '?' + +new Date()
        };
    },
    methods: {
        home() {
            this.$router.push({ path: '/' });
        },
        back() {
            if (this.$route.query.noGoBack) {
                this.$router.push({ path: '/' });
            } else {
                this.$router.go(-2);
            }
        }
    }
};
</script>

<style></style>
