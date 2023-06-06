<template>
    <el-row style="padding: 50px">
        <el-col :span="16">
            <el-form ref="form" :model="form" label-width="180px">
                <el-form-item label="姓名（中文）">
                    <el-input v-model="form.name"></el-input>
                </el-form-item>

                <el-form-item label="邮箱">
                    <el-input v-model="form.email"></el-input>
                </el-form-item>

                <el-form-item label="文件空间">
                    <el-progress :stroke-width="30" :text-inside="true" :percentage="100 - this.fileConf.spaceUsage" :format="format"></el-progress>
                </el-form-item>
            </el-form>
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
