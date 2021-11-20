<template>
    <el-upload class="upload-demo" accept=".zip" :action="fileUploadUrl" :headers="headers" :data="data" :on-success="handlerSuccess" :on-preview="handlePreview" :on-remove="handleRemove" :before-upload="beforeUpload" :before-remove="beforeRemove" multiple :limit="10" :on-exceed="handleExceed" :file-list="fileList">
        <el-button size="small" type="primary">点击上传</el-button>
        <template #tip>
            <div class="el-upload__tip">只能上传 .zip 文件，且不超过 10MB（建议使用HiDevTools工具打包）</div>
        </template>
    </el-upload>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
import { getToken } from '@/utils/auth';
import { config } from '@/utils/config';
export default {
    props: {
        callback: {
            type: Function
        }
    },
    data() {
        return {
            fileUploadUrl: '',
            headers: [],
            data: {},
            fileList: []
        };
    },
    mounted() {
        console.log('mount begin');
        this.fileUploadUrl = config().baseServer + '/javadoc/uploadZip';
        this.headers['Access-Token'] = getToken();

        //  debugger;
        let token = this.$store.state.user.token;
        console.log('token-1-1-1: ' + token);

        let name = this.$store.state.user.name;
        console.log('name: ' + name);
        console.log('mount end');
    },
    components: {},
    methods: {
        openPanel() {
            console.log('openPanel');
            this.fileList = [];
        },
        handleRemove(file, fileList) {
            console.log(file, fileList);
        },
        handlePreview(file) {
            console.log('handlePreview');
            console.log(this.bucket);
            console.log(this.bucket.id);
            console.log(file);
        },
        handleExceed(files, fileList) {
            this.$message.warning(`当前限制选择 10 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        },
        beforeUpload(file) {
            // debugger;
            const isZip = file.type === 'application/x-zip-compressed';
            const isLt10M = file.size / 1024 / 1024 < 10;

            if (!isZip) {
                this.$message.error('仅支持 .zip 文件');
            }
            if (!isLt10M) {
                this.$message.error('zip 文件不能超过 10MB');
            }
            return isZip && isLt10M;
        },
        beforeRemove(file, fileList) {
            // return this.$confirm(`确定移除 ${file.name}？`);
        },
        handlerSuccess(response, file, fileList) {
            this.callback({ response: response });
        }
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
