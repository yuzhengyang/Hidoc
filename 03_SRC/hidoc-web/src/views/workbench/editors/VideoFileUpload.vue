<template>
    <el-upload class="upload-demo" :action="fileUploadUrl" :headers="headers" :data="data" :on-success="handlerSuccess" :on-preview="handlePreview" :on-remove="handleRemove" :before-upload="beforeUpload" :before-remove="beforeRemove" multiple :limit="10" :on-exceed="handleExceed" :file-list="fileList" accept="video/*">
        <el-button size="small" type="primary">点击上传</el-button>
        <template #tip>
            <div class="el-upload__tip">上传 mp4 文件（服务器磁盘空间有限，请注意个人账户的空间用量）</div>
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
        uploadDataParams: Object,
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
        debugger
        console.log('mount begin');
        this.fileUploadUrl = config().baseServer + 'f/u';
        this.headers['Access-Token'] = getToken();
        this.data = this.uploadDataParams;

        console.log(this.data);

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
            const fileSuffix = file.name.substring(file.name.lastIndexOf('.') + 1);

            const whiteList = ['mp4'];

            if (whiteList.indexOf(fileSuffix) === -1) {
                this.$message.error('上传文件只能是 mp4 格式');
                return false;
            }

            const isLt2M = file.size / 1024 / 1024 < 1024;

            if (!isLt2M) {
                this.$message.error('上传文件大小不能超过 1024MB');
                return false;
            }
        },
        handlerSuccess(response, file, fileList) {
            this.callback(response.data);
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
