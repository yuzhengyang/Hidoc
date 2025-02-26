<template>
    <el-upload class="upload-demo" :action="fileUploadUrl" :headers="headers" :data="data" :on-success="handlerSuccess" :on-preview="handlePreview" :on-remove="handleRemove" :before-upload="beforeUpload" :before-remove="beforeRemove" multiple :limit="10" :on-exceed="handleExceed" :file-list="fileList" :accept="accept">
        <el-button size="small" type="primary">点击上传</el-button>
        <template #tip>
            <div class="el-upload__tip">上传 {{ fileTypeDesc }} 文件（服务器磁盘空间有限，请注意个人账户的空间用量）</div>
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
            fileUploadUrl: '', // 上传文件的地址
            headers: [], // 请求头
            data: {}, // 请求参数
            fileList: [], // 文件列表
            fileType: '', // 文件类型
            fileTypeDesc: '', // 文件类型名称
            accept: '*', // 文件类型弹框过滤
            whiteList: [] // 白名单
        };
    },
    mounted() {
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

            this.fileType = this.data.fileType;
            switch (this.fileType) {
                case 'image':
                    this.fileTypeDesc = '图片';
                    this.accept = 'image/*';
                    this.whiteList = ['jpg', 'jpeg', 'png'];
                    break;
                case 'video':
                    this.fileTypeDesc = '视频';
                    this.accept = 'video/*';
                    this.whiteList = ['mp4'];
                    break;
                default:
                    this.fileTypeDesc = '其他';
                    this.accept = '*';
                    this.whiteList = [];
                    break;
            }
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
            if (this.whiteList.length > 0 && this.whiteList.indexOf(fileSuffix) === -1) {
                this.$message.error('上传文件格式不符合要求');
                return false;
            }

            const isLt10G = file.size / 1024 / 1024 < 1024; // updated limit to 10 GB

            if (!isLt10G) {
                this.$message.error('上传文件大小不能超过 1 GB');
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
