<template>
    <el-container style="height: 100%">
        <!-- 内容区域 -->
        <el-main style="height: 100%">
            <el-row>
                <el-col :span="24">
                    <div style="width: 100%">
                        <div v-if="this.$store.state.user.token == undefined || this.$store.state.user.token == ''">
                            <el-alert title="登录后可上传文件" type="success" :closable="false" />
                        </div>
                        <div v-else>
                            <!-- 
                    <el-upload    :show-file-list="false" :on-success="uploadAvatarSuccess" accept="image/*">
                 -->
                            <el-upload :action="fileUploadUrl" :data="{ bucketName: '.share' }" :headers="headers" drag multiple>
                                <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                                <div class="el-upload__text">
                                    拖动文件到此处上传 或
                                    <em>点击上传</em>
                                </div>
                                <template #tip>
                                    <div class="el-upload__tip">您的文件空间剩余：{{ fileConf.spaceLimit ? parseInt((fileConf.spaceLimit - fileConf.usedSpace) / 1024 / 1024) : 0 }} MB</div>
                                </template>
                            </el-upload>
                        </div>
                    </div>
                </el-col>
            </el-row>
            <el-row style="margin-top: 5px">
                <el-col :span="8"><h2>最近</h2></el-col>
                <el-col :span="16" style="text-align: right">
                    <el-input v-model="fileSearchText" placeholder="搜索一下" class="input-with-select" clearable>
                        <template #append>
                            <el-button @click="search()">
                                <el-icon>
                                    <Search />
                                </el-icon>
                            </el-button>
                        </template>
                    </el-input>
                </el-col>
            </el-row>
            <el-row>
                <el-col :xs="24" :sm="12" :md="8" :lg="6" :xl="4" v-for="item in filterShareFileList()" :key="item">
                    <el-card>
                        <div>
                            <el-image style="width: 40px; height: 30px; background-color: rgba(52, 126, 255, 0.786); text-align: center">
                                <template #error>
                                    <div class="image-slot">
                                        <span style="font-size: 20px; color: white; user-select: none">{{ item.ext }}</span>
                                    </div>
                                </template>
                            </el-image>
                            <div>
                                <span style="font-size: 20px; user-select: none">{{ item.fileName }}</span>
                            </div>
                        </div>
                        <div style="font-size: 14px">
                            <el-popover placement="bottom-start" :width="260" trigger="hover">
                                <template #reference>
                                    <div style="height: 30px; margin-top: 15px">
                                        <div style="float: left">
                                            <el-link type="primary" style="font-weight: bold" @click="fileDownload(item)">立即下载</el-link>
                                        </div>
                                        <div style="float: right">
                                            <el-link type="warning" style="font-weight: bold" @click="fileDownloadCopy(item)">复制链接</el-link>
                                        </div>
                                    </div>
                                </template>
                                <template #default>
                                    <div class="demo-rich-conent" style="display: flex; gap: 16px; flex-direction: column">
                                        <div>
                                            <p class="demo-rich-content__name" style="margin: 0; font-weight: 500">{{ item.fileName }}</p>
                                            <p class="demo-rich-content__name" style="margin: 0; font-weight: 500">{{ parseInt(item.size / 1024 / 1024) }} MB</p>
                                            <p class="demo-rich-content__name" style="margin: 0; font-weight: 500">上传：{{ item.createTime }}</p>
                                            <p class="demo-rich-content__mention" style="margin: 0; font-size: 14px; color: var(--el-color-info)">@{{ item.realName }}</p>
                                        </div>

                                        <p class="demo-rich-content__desc" style="margin: 0">查看：{{ item.downloadTime }}</p>
                                        <p class="demo-rich-content__desc" style="margin: 0">查看次数：{{ item.downloadCount }}</p>
                                    </div>
                                </template>
                            </el-popover>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </el-main>
    </el-container>
</template>

<script>
import { ElMessageBox, ElMessage } from 'element-plus';
import { Search, Share, Guide } from '@element-plus/icons';
import request from '../../utils/request.js';
import { config } from '@/utils/config';
import { getToken } from '@/utils/auth';
import _ from 'lodash';
import { copy } from '../../utils/clipboard.js';
export default {
    data() {
        return {
            headers: [],
            fileUploadUrl: '',
            user: {},
            fileConf: {},
            shareFileList: [],
            shareFileTotal: 0,
            fileSearchText: ''
        };
    },
    components: {},
    mounted() {
        document.title = 'Hidoc-共享文件';

        this.headers['Access-Token'] = getToken();
        this.fileUploadUrl = config().baseServer + 'f/u';

        var user = this.$store.state.user;
        console.log(user);
        if (user.token) {
            this.getCurrentUserInfo();
        }

        this.getShareFileList();
    },
    methods: {
        copy(s) {
            copy(s);
            ElMessage({
                message: '复制成功',
                type: 'success',
                duration: 1 * 1000
            });
        },
        getCurrentUserInfo() {
            request({
                url: '/user/currentUserInfo',
                method: 'post'
            }).then(res => {
                if (res.code == 0) {
                    this.user = res.meta.user;
                    this.fileConf = res.meta.fileConf;
                }
            });
        },
        getShareFileList(current) {
            request({
                url: '/openapi/file/shareList',
                method: 'post',
                data: {
                    token: this.$store.state.user.token
                }
            }).then(res => {
                if (res.code == 0) {
                    this.shareFileList = res.data;
                    this.shareFileTotal = res.total;
                }
            });
        },
        filterShareFileList() {
            const t = this.fileSearchText;
            return _.filter(this.shareFileList, function (o) {
                return o.fileName.indexOf(t) >= 0;
            });
        },
        fileDownload(data) {
            window.location.href = config().baseServer + 'f/d/u/' + data.uname;
        },
        fileDownloadCopy(data) {
            this.copy(config().baseServer + 'f/d/u/' + data.uname);
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

::-webkit-scrollbar {
    width: 6px;
    background-color: #d8d8d8;
}

/* 滚动槽 */
::-webkit-scrollbar-track {
    border-radius: 10px;
}

::-webkit-scrollbar-thumb {
    background-color: #bfc1c4;
}
</style>
