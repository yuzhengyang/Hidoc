<template>
    <el-container>
        <el-header height="30px">
            <div>剂量控制器</div>
        </el-header>
        <el-main>
            <div>张三的1号水泵</div>
            <div>token：86548126985321575258</div>
            <div>周期：每天</div>
            <div>总量：1000 ml</div>
            <div>累计：不累计</div>
            <div>累计清空：月清</div>
            <div>====================</div>
            <div>最近使用时间：2022-03-04 16:31:28</div>
            <div>今日已用：20 ml</div>
            <div>今日剩余：980 ml</div>
            <div>累计剩余：6980 ml</div>
        </el-main>
    </el-container>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
export default {
    data() {
        return {
            projectList: [],
            dialogUploadVisible: false
        };
    },
    mounted() {
    },
    components: {  },
    methods: {
        
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