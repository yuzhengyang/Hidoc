<template>
    <el-container>
        <el-header height="30px">
            <el-row>
                <el-col :span="16"></el-col>
                <el-col :span="8" style="text-align: right">
                    <el-button type="primary" round @click="openUploadDialog">上传zip</el-button>
                    <el-button type="primary" round @click="loadProjectList">刷新</el-button>
                </el-col>
            </el-row>
        </el-header>
        <el-main>
            <el-row :span="24">
                <el-table :data="projectList" stripe>
                    <el-table-column prop="name" label="名称" width="300" />
                    <!-- <el-table-column prop="description" label="描述"  width="200"/> -->
                    <el-table-column prop="token" label="token" width="150" />
                    <el-table-column prop="createTime" label="创建时间" width="160" />
                    <el-table-column prop="updateTime" label="更新时间" width="160" />
                    <el-table-column label="团队（访问权限）" width="160">
                        <template #default="scope_space">
                            <el-tag v-for="item in scope_space.row.teamsReadList" :key="item.id" type="success" size="small">{{ item.name }}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="团队（源码权限）" width="160">
                        <template #default="scope_space">
                            <el-tag v-for="item in scope_space.row.teamsCodeList" :key="item.id" type="success" size="small">{{ item.name }}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作">
                        <template>
                            <el-link type="warning" style="font-size: 12px; margin-right: 10px">设置关联团队</el-link>
                        </template>
                    </el-table-column>
                </el-table>
            </el-row>
        </el-main>
    </el-container>

    <el-dialog title="上传文件" v-model="dialogUploadVisible">
        <java-doc-zip-upload :callback="uploadCallback" ref="javaDocZipUpload"></java-doc-zip-upload>
        <template #footer>
            <span class="dialog-footer">
                <el-button type="primary" @click="dialogUploadVisible = false">完 成</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
import JavaDocZipUpload from '../components/JavaDocZipUpload';
export default {
    data() {
        return {
            projectList: [],
            dialogUploadVisible: false
        };
    },
    mounted() {
        this.loadProjectList();
    },
    components: { JavaDocZipUpload },
    methods: {
        loadProjectList() {
            request({
                url: '/javadoc/projectList',
                method: 'post',
                data: {
                    token: ''
                }
            }).then(res => {
                if (res.code == 0) {
                    this.projectList = res.data;
                }
            });
        },
        // 打开上传文件窗口
        openUploadDialog() {
            // 第一次会报错，找不到refdom
            this.dialogUploadVisible = true;
            this.$refs['javaDocZipUpload'].openPanel();
        },
        uploadCallback(data) {
            console.log('uploadCallback');
            console.log(data);
            if (data.response.code == 0) {
                // debugger;
                this.dialogUploadVisible = false;
                this.$message.success(data.response.msg);
            } else {
                this.$message.error(data.response.msg);
            }
            this.loadProjectList();
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
