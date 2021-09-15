<template>
    <el-row :gutter="12">
        <el-col :span="6">
            <el-card shadow="hover">
                文集数量： {{this.collectedCount}}
            </el-card>
        </el-col>
        <el-col :span="6">
            <el-card shadow="hover">
                文档数量： {{this.docCount}}
            </el-card>
        </el-col>
        <el-col :span="6">
            <el-card shadow="hover">
                文件数量： {{this.cursorCount}}
            </el-card>
        </el-col>
        <el-col :span="6">
            <el-card shadow="hover">
                阅读数量： {{this.readCount}}
            </el-card>
        </el-col>

        月度文章发布统计图
        发布动态
    </el-row>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
export default {
    data() {
        return {
            collectedCount: 0,
            docCount: 0,
            cursorCount: 0,
            readCount: 0
        };
    },
    mounted() {
        request({
            url: '/i/user/currentUserBoard',
            method: 'post'
        }).then(res => {
            if (res.code == 0) {
                this.collectedCount = res.meta.collectedCount;
                this.docCount = res.meta.docCount;
                this.cursorCount = res.meta.cursorCount;
                this.readCount = res.meta.readCount;
            }
        });
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