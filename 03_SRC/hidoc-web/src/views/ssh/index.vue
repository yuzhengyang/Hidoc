<template>
    <el-container style="height: 100%">
        <!-- 内容区域 -->
        <el-main style="height: 100%">
            <!-- 登录使用功能的提示 -->
            <el-row class="no-select">
                <el-col :span="24">
                    <div style="width: 100%">
                        <div v-if="this.$store.state.user.token == undefined || this.$store.state.user.token == ''">
                            <el-alert title="登录后可执行共享的SSH命令~" type="success" :closable="false" />
                        </div>
                    </div>
                </el-col>
            </el-row>
            <!-- 搜索过滤 -->
            <el-row class="no-select" style="margin-bottom: 10px">
                <el-col :span="24" style="text-align: right">
                    <el-input
                        v-model="searchKeyword"
                        placeholder="搜索服务器或命令"
                        clearable
                        style="width: 300px"
                        :prefix-icon="Search"
                    />
                </el-col>
            </el-row>
            <!-- 服务器及操作列表：开始 -->
            <div class="waterfall-container no-select">
                <div class="waterfall-item" v-for="item in filteredShareList" :key="item">
                    <el-card>
                        <template #header>
                            <div class="card-header">
                                <el-image style="height: 18px;width: 28px;"
                                    :src="require(item.portOpen ? '../../assets/switch/computer_open.svg' : '../../assets/switch/computer_close.svg')"
                                    fit="scale-down" />
                                <span>{{ item.name }}</span>
                                <span style="color: #aaa; font-size: 14px">（{{ item.address }}）</span>
                            </div>
                        </template>
                        <div v-for="cmd in item.cmdList" :key="cmd" class="cmd-item">
                            <el-container style="margin: 2px">
                                <el-main style="font-size: 14px">{{ cmd.name }}</el-main>
                                <el-aside style="padding: 3px" width="80px" v-if="this.isLogin">
                                    <el-progress class="cust-progress" type="circle" :percentage="cmd._waitProgress"
                                        :status="cmd._waitProgress == 100 ? 'success' : 'warning'" stroke-width="5"
                                        width="30" @click="runCmd(item, cmd, $event)"
                                        :style="{ cursor: cmd._waitProgress == 100 ? 'pointer' : 'not-allowed' }">
                                        <template #default="{ }">
                                            <span v-if="cmd._waitProgress">
                                                <el-icon v-if="cmd._waitProgress == 100" :size="25" color="#13ce66">
                                                    <VideoPlay />
                                                </el-icon>
                                                <span v-else style="font-size: 12px;">{{ cmd._waitSeconds }}</span>
                                            </span>
                                            <span v-else style="font-size: 12px;">
                                                <el-icon :size="20">
                                                    <More />
                                                </el-icon>
                                            </span>
                                        </template>
                                    </el-progress>
                                </el-aside>
                            </el-container>
                        </div>
                        <template #footer>
                            <div>
                                <span style="color: #888; font-size: 14px">{{ item.description }}</span>
                            </div>
                            <div v-if="item.teamList.length > 0" style="padding: 10px">
                                <el-tag style="margin-right: 5px; cursor: pointer" v-for="team in item.teamList"
                                    :key="team.id" type="success" size="small">{{ team.name }}</el-tag>
                            </div>
                        </template>
                    </el-card>
                </div>
            </div>
            <!-- 服务器及操作列表：结束 -->

            <el-drawer v-model="drawerPanel.visible" direction="btt" :show-close="false" :before-close="handleClose"
                size="85%" destroy-on-close="true">
                <template #header="{ close, titleId, titleClass }">
                    <div :id="titleId" :class="titleClass">
                        <span style="font-weight: bold; color: #000">🖥️ {{ currentRunning.machine.name }}</span>
                        <span style="font-weight: bold; color: #aaa">（{{ currentRunning.machine.address }}）</span>
                        <span style="font-weight: bold; color: #f00">{{ currentRunning.cmd.name }}</span>
                    </div>
                    <!-- <el-button type="success" @click="refreshCmdLogOutput">刷新结果</el-button> -->
                    <el-button type="danger" @click="close">
                        <el-icon class="el-icon--left">
                            <CircleCloseFilled />
                        </el-icon>
                        Close
                    </el-button>
                </template>
                <div id="outputContainer">
                    <div style="font-size: 14px; font-weight: bold">
                        <span style="color: #f00">执行指令：</span>
                        <span>{{ currentRunning.cmd.contentTa }}</span>
                    </div>
                    <div style="margin: -0px -40px -0px -40px">
                        <v-md-editor v-model="currentRunning.output" mode="preview" ref="editor"
                            @copy-code-success="handleCopyCodeSuccess" />
                    </div>
                    <div v-if="this.currentRunning.runFinish"
                        style="font-size: 14px; font-weight: bold; color: #f00; margin: 40px">
                        执行指令完成！</div>
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
import * as moment from 'moment';
export default {
    data() {
        return {
            user: {},
            isLogin: false,
            searchKeyword: '',
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
            },
            progressRefresh: {
                debug: false,
                refreshTimer: null,
                refreshCount: 0,
            }
        };
    },
    components: {},
    computed: {
        filteredShareList() {
            if (!this.searchKeyword) {
                return this.shareList;
            }
            const keywords = this.searchKeyword.trim().split(/\s+/).filter(k => k.length > 0);
            if (keywords.length === 0) {
                return this.shareList;
            }
            return this.shareList.filter(item => {
                return keywords.every(keyword => {
                    const lowerKeyword = keyword.toLowerCase();
                    const matchServerName = item.name.toLowerCase().includes(lowerKeyword);
                    const matchCmdName = item.cmdList.some(cmd => cmd.name.toLowerCase().includes(lowerKeyword));
                    return matchServerName || matchCmdName;
                });
            });
        }
    },
    created() {
        console.log('created 钩子函数被调用');
    },
    mounted() {
        console.log('mounted 钩子函数被调用');
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

        const urlParams = new URLSearchParams(window.location.search);
        const keyParam = urlParams.get('key');
        if (keyParam) {
            this.searchKeyword = keyParam;
        }
    },
    unmounted() {
        console.log('unmounted 钩子函数被调用');
        clearInterval(this.progressRefresh.refreshTimer);
        clearInterval(this.currentRunning.refreshTimer);
        console.log('定时器已清除');
    },
    methods: {
        progressRefreshFunction(shareCmdList) {
            this.progressRefresh.refreshCount++;
            if (this.progressRefresh.debug) console.log(`刷新命令执行限制时间，第 ${this.progressRefresh.refreshCount} 次`);

            let nextDo = false;
            if (shareCmdList && shareCmdList.length > 0) {
                let now = moment();
                if (this.progressRefresh.debug) console.log(`刷新命令执行限制时间，共计 ${shareCmdList.length} 个服务器开放指令，当前时间为：${now.format('YYYY-MM-DD HH:mm:ss')}`);
                for (let i = 0; i < shareCmdList.length; i++) {
                    for (let j = 0; j < shareCmdList[i].cmdList.length; j++) {
                        let pgs = 100; // 进度
                        let sec = 0; // 等待秒数
                        let shareItem = shareCmdList[i];
                        let cmdItem = shareCmdList[i].cmdList[j];
                        if (cmdItem.interval && cmdItem.interval > 1 && cmdItem.executeTime) {
                            let executeTime = moment(cmdItem.executeTime);
                            let nextExecuteTime = moment(cmdItem.executeTime).add(cmdItem.interval, 'seconds');
                            sec = nextExecuteTime.diff(now, 'seconds');
                            pgs = Math.round((1 - (sec / cmdItem.interval)) * 100);

                            if (this.progressRefresh.debug) console.log(`刷新命令执行限制时间，第 ${i + 1} 个服务器，第 ${j + 1} 个指令，执行时间：${executeTime.format('YYYY-MM-DD HH:mm:ss')}，执行间隔：${cmdItem.interval}， 下次执行时间：${nextExecuteTime.format('YYYY-MM-DD HH:mm:ss')}，应该等待：${sec}秒`);
                            if (sec > 0) {
                                nextDo = true;
                                console.log(`${this.progressRefresh.refreshTimer}，服务器：${shareItem.name}，指令：${cmdItem.name}，【冷却进度：${pgs}%】`);
                            }
                        }
                        cmdItem._waitProgress = pgs > 100 ? 100 : pgs;
                        if (sec > 0) cmdItem._waitSeconds = sec;
                    }
                }
            }
            if (nextDo == false) {
                clearInterval(this.progressRefresh.refreshTimer);
                console.log('所有的进度都已经就绪，定时器已清除');
            }
        },
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
                clearInterval(this.progressRefresh.refreshTimer);
                if (res.code == 0) {
                    this.progressRefreshFunction(res.data);
                    this.shareList = res.data;
                    this.progressRefresh.refreshTimer = setInterval(() => this.progressRefreshFunction(this.shareList), 1000);
                }
            });
        },
        runCmd(machine, cmd, event) {
            ElMessage({ message: `正在执行：${machine.name} ${cmd.name} 指令`, type: 'success', duration: 3 * 1000 });

            console.log('runCmd: ' + cmd.id);
            if (event.shiftKey) {
                console.log(`You clicked with the Shift key pressed`);
            } else {
                console.log(`You clicked without the Shift key pressed`);
            }
            // 重置当前正在运行的命令
            this.currentRunning.serialNumber = 1;
            this.currentRunning.prepareOutput = '';

            return request({
                url: '/serverManCmd/run',
                method: 'post',
                data: { token: this.$store.state.user.token, id: cmd.id }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.currentRunning.log = res.meta.log;
                    if (!event.shiftKey) {
                        this.openDrawerPanel(machine, cmd);
                    }
                    ElMessage({
                        message: res.msg,
                        type: 'success',
                        duration: 3 * 1000
                    });
                }
                this.getShareList();
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

.waterfall-item .el-card {
    margin: 8px;
    padding: 0px;
    --el-card-padding: 10px;
    border: 1px solid #bebebe;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    transition: box-shadow 0.3s;
}

.waterfall-item .el-card:hover {
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

/* 瀑布流容器样式 */
.waterfall-container {
    column-count: 4;
    column-gap: 10px;
}

@media (max-width: 1600px) {
    .waterfall-container {
        column-count: 3;
    }
}

@media (max-width: 1200px) {
    .waterfall-container {
        column-count: 2;
    }
}

@media (max-width: 768px) {
    .waterfall-container {
        column-count: 1;
    }
}

/* 瀑布流项目样式 */
.waterfall-item {
    break-inside: avoid;
    margin-bottom: 10px;
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
    /* cursor: pointer; */
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

.cust-progress {
    .el-progress__text {
        min-width: 0px;
    }
}
</style>
