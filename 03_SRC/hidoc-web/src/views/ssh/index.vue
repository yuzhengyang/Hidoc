<template>
    <el-container style="height: 100%">
        <!-- 内容区域 -->
        <el-main style="height: 100%">
            <el-row>
                <el-col :span="24">
                    <div style="width: 100%">
                        <div v-if="this.$store.state.user.token == undefined || this.$store.state.user.token == ''">
                            <el-alert title="登录后可执行共享的SSH命令~" type="success" :closable="false" />
                        </div>
                    </div>
                </el-col>
            </el-row>
            <el-row>
                <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="4" v-for="item in shareList" :key="item">
                    <el-card>
                        <template #header>
                            <div class="card-header">
                                <span>🖥️ {{ item.name }}</span>
                                <span style="color: #aaa; font-size: 14px">（{{ item.address }}）</span>
                            </div>
                        </template>
                        <div v-for="cmd in item.cmdList" :key="cmd" class="cmd-item">
                            <el-container style="margin: 2px">
                                <el-main style="font-size: 14px">{{ cmd.name }}</el-main>
                                <el-aside style="padding: 3px" width="100px" v-if="this.isLogin">
                                    <el-button type="success" circle @click="runCmd(item, cmd)">
                                        <el-icon><VideoPlay /></el-icon>
                                    </el-button>
                                    <el-button type="primary" circle>
                                        <el-icon><Operation /></el-icon>
                                    </el-button>
                                </el-aside>
                            </el-container>
                        </div>
                        <template #footer>
                            <div>
                                <span style="color: #888; font-size: 14px">{{ item.description }}</span>
                            </div>
                            <div v-if="item.teamExecuteList.length > 0" style="padding: 10px">
                                <el-tag style="margin-right: 5px; cursor: pointer" v-for="team in item.teamExecuteList" :key="team.id" type="success" size="small">{{ team.name }}</el-tag>
                            </div>
                        </template>
                    </el-card>
                </el-col>
            </el-row>

            <el-drawer v-model="drawerPanel.visible" direction="btt" :show-close="false" :before-close="handleClose" size="85%" destroy-on-close="true">
                <template #header="{ close, titleId, titleClass }">
                    <div :id="titleId" :class="titleClass">
                        <span style="font-weight: bold; color: #000">🖥️ {{ currentRunning.machine.name }}</span>
                        <span style="font-weight: bold; color: #aaa">（{{ currentRunning.machine.address }}）</span>
                        <span style="font-weight: bold; color: #f00">{{ currentRunning.cmd.name }}</span>
                    </div>
                    <el-button type="success" @click="refreshCmdLogOutput">刷新结果</el-button>
                    <el-button type="danger" @click="close">
                        <el-icon class="el-icon--left"><CircleCloseFilled /></el-icon>
                        Close
                    </el-button>
                </template>
                <div id="outputContainer">
                    <div style="font-size: 14px; font-weight: bold">
                        <span style="color: #f00">执行指令：</span>
                        <span>{{ currentRunning.cmd.contentTa }}</span>
                    </div>
                    <div style="margin: -0px -40px -0px -40px">
                        <v-md-editor v-model="currentRunning.output" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />
                    </div>
                    <div v-if="this.currentRunning.runFinish" style="font-size: 14px; font-weight: bold; color: #f00; margin: 40px">执行指令完成！</div>
                </div>
            </el-drawer>
        </el-main>
    </el-container>
</template>

<script>
import { ElMessageBox, ElMessage } from 'element-plus';
import { Search, Share, Guide, Operation, VideoPlay } from '@element-plus/icons';
import request from '../../utils/request.js';
import { config } from '@/utils/config';
import { getToken } from '@/utils/auth';
import _ from 'lodash';
export default {
    data() {
        return {
            user: {},
            isLogin: false,
            shareList: [],
            drawerPanel: {
                visible: false
            },
            currentRunning: {
                machine: {},
                cmd: {},
                log: {},
                output: '',
                prepareOutput: '',
                refreshTimer: '',
                refreshCount: 0,
                runFinish: false,
                serialNumber: 1
            }
        };
    },
    components: {},
    mounted() {
        document.title = 'Hidoc-SSH';

        this.user = this.$store.state.user;
        if (this.user.token == undefined || this.user.token == '') {
            this.isLogin = false;
            this.user.roles = [];
        } else {
            this.isLogin = true;
        }
        if (this.user.token) {
            this.getCurrentUserInfo();
        }
        this.getShareList();
    },
    methods: {
        getCurrentUserInfo() {
            request({
                url: '/user/currentUserInfo',
                method: 'post'
            }).then(res => {
                if (res.code == 0) {
                    this.user = res.meta.user;
                }
            });
        },
        getShareList() {
            request({
                url: '/openapi/serverMan/shareList',
                method: 'post',
                data: {
                    token: this.$store.state.user.token
                }
            }).then(res => {
                if (res.code == 0) {
                    this.shareList = res.data;
                }
            });
        },
        runCmd(machine, cmd) {
            console.log('runCmd: ' + cmd.id);
            return request({
                url: '/serverManCmd/run',
                method: 'post',
                data: { token: this.$store.state.user.token, id: cmd.id }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.currentRunning.log = res.meta.log;
                    this.openDrawerPanel(machine, cmd);
                    ElMessage({
                        message: res.msg,
                        type: 'success',
                        duration: 5 * 1000
                    });
                }
            });
        },
        refreshCmdLogOutput() {
            let that = this;
            this.currentRunning.refreshCount--;
            if (this.currentRunning.log && this.currentRunning.refreshCount > 0) {
                return request({
                    url: '/serverManExeLog/serverManOutput',
                    method: 'post',
                    data: { token: this.$store.state.user.token, dialogId: this.currentRunning.log.id, serialNumber: this.currentRunning.serialNumber }
                }).then(res => {
                    if (res.code == 0 && res.count > 0) {
                        // 循环组装输出的内容
                        for (let i = 0; i < res.data.length; i++) {
                            // 设置序号为最新值，来防止重复获取数据
                            this.currentRunning.serialNumber = res.data[i].serialNumber + 1;
                            this.currentRunning.prepareOutput = this.currentRunning.prepareOutput + res.data[i].output;

                            // 前端判断是否已经执行成功了，有执行成功的标记则不再刷新了
                            if (res.data[i].output.indexOf('##hidoc->serverman.run::end') > 0) {
                                this.currentRunning.runFinish = true;
                                clearInterval(this.currentRunning.refreshTimer);
                            }
                        }
                        this.currentRunning.output = '```bash' + '\r\n' + this.currentRunning.prepareOutput + '\r\n' + '```';

                        // 应用滚动
                        this.$nextTick(() => {
                            let outputElement = document.getElementById('outputContainer').parentElement;
                            outputElement.scrollTop = outputElement.scrollHeight - outputElement.clientHeight;
                        });
                    }
                });
            }
        },
        // refreshCmdLogOutput() {
        //     this.currentRunning.refreshCount--;
        //     if (this.currentRunning.log && this.currentRunning.refreshCount > 0) {
        //         return request({
        //             url: '/serverManExeLog/fileDetail',
        //             method: 'post',
        //             data: { token: this.$store.state.user.token, id: this.currentRunning.log.id }
        //         }).then(res => {
        //             if (res.code == 0) {
        //                 this.currentRunning.output = '```bash' + '\r\n' + res.meta.fileDetail + '\r\n' + '```';

        //                 // 前端判断是否已经执行成功了，有执行成功的标记则不再刷新了
        //                 if (res.meta.fileDetail.indexOf('##hidoc->serverman.run::end') > 0) {
        //                     this.currentRunning.runFinish = true;
        //                     clearInterval(this.currentRunning.refreshTimer);
        //                 }

        //                 // 应用滚动
        //                 this.$nextTick(() => {
        //                     let outputElement = document.getElementById('outputContainer').parentElement;
        //                     outputElement.scrollTop = outputElement.scrollHeight - outputElement.clientHeight;
        //                 });
        //                 // console.log(`scrollHeight: ${outputElement.scrollHeight}, scrollTop: ${outputElement.scrollTop}, clientHeight: ${outputElement.clientHeight}`);
        //                 // 这里根据高度判断来向下滚动不合适，输出的文本会突然变大，把高度撑的很高，就没有办法满足吸附条件了
        //                 // if (outputElement.scrollHeight - outputElement.scrollTop - outputElement.clientHeight < 1000) {
        //                 //     console.log('滚动条已经滚动到底部，将自动向下滚动');
        //                 //     outputElement.scrollTop = outputElement.scrollHeight - outputElement.clientHeight;
        //                 // }
        //             }
        //         });
        //     }
        // },
        openDrawerPanel(machine, cmd) {
            this.currentRunning.machine = machine;
            this.currentRunning.cmd = cmd;
            this.currentRunning.output = '```bash' + '\r\n' + '... ... 正在获取结果信息 ... ...' + '\r\n' + '```';
            this.drawerPanel.visible = true;
            this.currentRunning.refreshCount = 100;
            this.currentRunning.refreshTimer = setInterval(this.refreshCmdLogOutput, 2 * 1000);
        },
        handleClose() {
            ElMessageBox.confirm('确定要关闭抽屉吗？')
                .then(() => {
                    this.drawerPanel.visible = false;
                    clearInterval(this.currentRunning.refreshTimer);

                    // this.currentRunning.machine = {};
                    // this.currentRunning.cmd = {};
                })
                .catch(() => {
                    // catch error
                });
        },
        handleCopyCodeSuccess(code) {
            // console.log(code);
            ElMessage({
                message: '复制成功',
                type: 'success',
                duration: 1 * 1000
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
    margin: 8px;
    padding: 0px;
    --el-card-padding: 10px;
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

/* 命令悬浮特效 */
.cmd-item {
    cursor: pointer;
    background: transparent;
    border-bottom: 1px solid #d6d6d6;
    border-radius: 0;
    position: relative;
    color: #636363;
    padding: 0px;
    margin: 0px;
}
/* .cmd-item:before {
    transition: all 0.5s linear;
    content: '';
    width: 0%;
    height: 100%;
    background: #80b7ff46;
    position: absolute;
    top: 0;
    left: 0;
} */
/* .cmd-item:hover:before {
    background: #80b7ff46;
    width: 100%;
} */

/* 修复markdown代码带滚动条问题 */
.vuepress-markdown-body {
    overflow: hidden;
}
</style>
