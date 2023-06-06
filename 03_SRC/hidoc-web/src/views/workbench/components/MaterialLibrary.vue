<template>
    <el-container>
        <el-header height="30px">
            <el-row>
                <el-alert title="您可以在此查看文档中上传使用的素材，包括图片、视频。" type="success" :closable="false" />
            </el-row>
        </el-header>
        <!-- 内容区域 -->
        <el-main>
            <el-row></el-row>
            <el-row>
                <el-col :span="23">
                    <el-table :data="hidocFileList" style="width: 100%">
                        <el-table-column prop="collectedName" label="文集名称"></el-table-column>
                        <el-table-column prop="collectedOwnerName" label="文集管理员" width="120"></el-table-column>
                        <el-table-column prop="fileName" label="文件名称"></el-table-column>
                        <el-table-column label="文件大小">
                            <template #default="scope">
                                <span>{{ parseInt(scope.row.size / 1024 / 1024) }} MB</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="createTime" label="上传时间" width="180"></el-table-column>
                        <el-table-column prop="downloadTime" label="最后查看时间" width="180"></el-table-column>
                        <el-table-column prop="downloadCount" label="查看次数" width="120"></el-table-column>
                        <!-- <el-table-column fixed="right" label="操作" width="200">
                            <template #default="scope">
                                <el-button @click="previewDoc(scope.row)" type="text" size="small">下载</el-button>

                                <el-popover placement="top-start" :width="200" trigger="click">
                                    <p>删除操作不可撤回，确定删除吗？</p>
                                    <div style="text-align: right; margin: 0">
                                        <el-button type="danger" size="mini" @click="docDelete(scope.row)">确定删除</el-button>
                                    </div>
                                    <template #reference>
                                        <el-button type="text" size="small">删除</el-button>
                                    </template>
                                </el-popover>
                            </template>
                        </el-table-column> -->
                    </el-table>
                </el-col>
            </el-row>
        </el-main>
    </el-container>
    <el-backtop></el-backtop>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
export default {
    data() {
        return {
            hidocFileList: []
        };
    },
    mounted() {
        //  debugger;
        let token = this.$store.state.user.token;
        console.log('token-1-1-1: ' + token);

        let name = this.$store.state.user.name;
        console.log('name: ' + name);

        this.getHidocFileList();
    },
    components: {},
    methods: {
        getHidocFileList() { 
            request({
                url: '/file/hidocList',
                method: 'post',
                data: {
                    token: this.$store.state.user.token
                }
            }).then(res => {
                if (res.code == 0) {
                    this.hidocFileList = res.data;
                }
            });
        },
        // 删除
        docDelete(row) {
            console.log('删除');
            return request({
                url: '/doc/delete',
                method: 'post',
                data: {
                    id: row.id.toString()
                }
            }).then(res => {
                if (res.code == 0) {
                    ElMessage({
                        message: res.msg || '操作成功',
                        type: 'success',
                        duration: 5 * 1000
                    });

                    this.selectCollected(this.currentCollected);
                } else {
                    ElMessage({
                        message: res.msg || 'Error',
                        type: 'error',
                        duration: 5 * 1000
                    });
                }
            });
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
