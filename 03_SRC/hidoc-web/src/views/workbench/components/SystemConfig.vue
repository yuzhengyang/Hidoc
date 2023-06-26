<template>
    <el-row style="padding: 50px">
        <el-col :span="16">
            系统设置，提供对系统设置参数进行修改
        </el-col>
    </el-row>
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
    mounted() {},
    methods: {}
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
