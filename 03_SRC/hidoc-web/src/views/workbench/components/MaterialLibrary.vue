<template>
    <el-container style="height: 100%">
        <el-header height="30px">
            <el-row>
                <el-alert title="您可以在此查看文档中上传使用的素材，包括图片、视频。" type="success" :closable="false" />
            </el-row>
        </el-header>
        <el-main style="height: 100%" id="fileContainer">
            <el-row>
                <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4"  v-for="item in hidocFileList" :key="item">
                    <el-card>
                        <div style="font-size: 14px">
                            <p>{{ item.collectedName }} [{{ item.collectedOwnerName }}]</p>
                        </div>
                        <div v-if="this.fileType(item.uname) == 'image'">
                            <el-image :src="formatImageUrl(item.uname)" style="width: 100%; height: 180px" fit="cover" :preview-src-list="[formatImageUrl(item.uname)]" lazy />
                        </div>
                        <div v-else-if="this.fileType(item.uname) == 'video'">
                            <el-image style="width: 100%; height: 180px; background-color: rgba(0, 166, 255, 0.786); text-align: center">
                                <template #error>
                                    <div class="image-slot">
                                        <el-icon style="font-size: 100px; margin-top: 40px; color: antiquewhite"><VideoPlay /></el-icon>
                                    </div>
                                </template>
                            </el-image>
                        </div>
                        <div v-else>
                            <el-image style="width: 100%; height: 180px; background-color: rgba(81, 81, 81, 0.786); text-align: center">
                                <template #error>
                                    <div class="image-slot">
                                        <el-icon style="font-size: 100px; margin-top: 40px; color: rgb(255, 255, 255)"><Document /></el-icon>
                                    </div>
                                </template>
                            </el-image>
                        </div>
                        <div style="font-size: 14px">
                            <div style="padding-top: 2px">{{ item.fileName }}</div>
                            <div style="padding-top: 2px">上传：{{ item.createTime }}</div>
                            <div style="padding-top: 2px">查看：{{ item.downloadTime }}</div>
                            <div style="padding-top: 2px; height: 20px">
                                <div style="float: left">查看次数：{{ item.downloadCount }}</div>
                                <div style="float: right; padding-right: 10px">{{ parseInt(item.size / 1024 / 1024) }} MB</div>
                            </div>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </el-main>
        <el-footer style="height: 30px; width: 100%; text-align: center">
            <el-pagination background layout="total, prev, pager, next, jumper" :currentPage="this.currentPage" :pageSize="this.pageSize" :total="this.dataTotal" @currentChange="getHidocFileList" style="margin-top: 5px" />
        </el-footer>
    </el-container>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
import { config } from '@/utils/config';
import _ from 'lodash';
export default {
    data() {
        return {
            currentPage: 1,
            pageSize: 40,
            dataTotal: 0,
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
        fileType(name) {
            let extName = name.substring(name.lastIndexOf('.') + 1).toLowerCase();
            switch (extName) {
                case 'png':
                case 'jpg':
                case 'gif':
                    return 'image';
                case 'mp4':
                    return 'video';
            }
            return '';
        },
        formatImageUrl(uname) {
            return config().imageServer + uname;
        },
        getHidocFileList(current) {
            this.currentPage = current;
            request({
                url: '/file/hidocList',
                method: 'post',
                data: {
                    token: this.$store.state.user.token,
                    current: this.currentPage,
                    size: this.pageSize
                }
            }).then(res => {
                if (res.code == 0) {
                    this.hidocFileList = res.data;
                    this.dataTotal = res.total;
                    document.getElementById('fileContainer').scrollTop = 0;
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
