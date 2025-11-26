<template>
    <el-upload class="upload-demo" :action="fileUploadUrl" :headers="headers" :data="data" :on-success="handlerSuccess"
        :on-preview="handlePreview" :on-remove="handleRemove" :before-remove="beforeRemove" multiple :limit="10"
        :on-exceed="handleExceed" :file-list="fileList">
        <el-button size="small" type="primary">点击上传</el-button>
        <template #tip>
            <div class="el-upload__tip">只能上传 jpg/png 文件，且不超过 500kb</div>
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
        bucket: Object,
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
        this.loadData();
    },
    components: {},
    methods: {
        loadData() {
            console.log(`methods.loadData 加载当前选中的桶信息`);
            this.fileUploadUrl = config().baseServer + 'f/u';
            this.headers['Access-Token'] = getToken();
            this.data.bucketName = this.bucket.name;
            let name = this.$store.state.user.name;
            let token = this.$store.state.user.token;
            console.log(`methods.loadData 加载完成，当前选中的桶为：${this.bucket.name}，用户为：${name}，token为：${token}`);
        },
        openPanel() {
            console.log('openPanel');
            this.loadData();
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
            this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        },
        beforeRemove(file, fileList) {
            return this.$confirm(`确定移除 ${file.name}？`);
        },
        handlerSuccess(response, file, fileList) {
            this.callback({ name: 'name', desc: "操作完成" });
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
