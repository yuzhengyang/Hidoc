<template>
    <el-tabs type="card" style="padding:10px">
        <!-- @tab-click="handleClick" -->
        <el-tab-pane label="服务器列表">
            <el-row>
                <el-button-group>
                    <el-button round type="success" size="small" @click="openCreateMachine">创建</el-button>
                </el-button-group>
            </el-row>
            <el-row style="padding:50px">
                <el-col :span="24">
                    <el-collapse accordion @change="showCmdList">
                        <el-collapse-item v-for="item in currentMachineList" :key="item.id.toString()" :index="item.id" :title="item.name" :name="item.id">
                            <div>
                                {{ item.description }}
                                <el-button type="text" round size="small" @click="openEditMachine(item.id)">[编辑服务器信息]</el-button>
                            </div>
                            <div>&nbsp;</div>
                            <div>
                                <el-button type="success" round size="small" @click="openCreateCmd(item.id)">创建指令</el-button>
                            </div>
                            <div>
                                <el-table :data="this.currentCmdList" style="width: 100%">
                                    <el-table-column prop="name" label="名称"></el-table-column>
                                    <!-- <el-table-column prop="description" label="描述信息"></el-table-column> -->
                                    <!-- <el-table-column prop="createTime" label="创建时间"></el-table-column> -->
                                    <el-table-column prop="type" label="类型" width="80"></el-table-column>
                                    <el-table-column prop="contentTa" label="contentTa"></el-table-column>
                                    <!-- <el-table-column prop="contentTb" label="contentTb"></el-table-column>
                                    <el-table-column prop="contentTc" label="contentTc"></el-table-column> -->
                                    <el-table-column fixed="right" label="操作" width="100">
                                        <template #default="scope">
                                            <el-button type="success" round size="small" @click="runCmd(scope.row)">执行</el-button>
                                        </template>
                                    </el-table-column>
                                    <el-table-column fixed="right" label="管理" width="200">
                                        <template #default="scope">
                                            <el-button type="warning" round size="small" @click="openEditCmd(scope.row)">编辑</el-button>
                                            <!-- <el-button type="warning" round size="small" @click="openCreateCmd(scope.row)">编辑</el-button> -->
                                            <el-popover placement="top-start" :width="200" trigger="click">
                                                <p>删除操作不可撤回，确定删除吗？</p>
                                                <div style="text-align: right; margin: 0">
                                                    <el-button type="danger" size="mini" @click="deleteCmd(scope.row)">确定</el-button>
                                                </div>
                                                <template #reference>
                                                    <el-button type="danger" round size="small">删除</el-button>
                                                </template>
                                            </el-popover>
                                        </template>
                                    </el-table-column>
                                </el-table>
                            </div>
                        </el-collapse-item>
                    </el-collapse>
                </el-col>
            </el-row>
        </el-tab-pane>

        <el-tab-pane label="执行日志">
            <el-row style="padding:50px">
                <el-col :span="24">
                    <el-row>
                        <el-col :span="3">
                            <el-input v-model="searchDataParams.planName" placeholder="执行服务器" class="input-with-select" @keydown="searchDataEnter" clearable />
                        </el-col>
                        <el-col :span="3">
                            <el-input v-model="searchDataParams.dataSource" placeholder="执行命令" class="input-with-select" @keydown="searchDataEnter" clearable />
                        </el-col>
                        <el-col :span="3">
                            <el-input v-model="searchDataParams.ip" placeholder="执行人" class="input-with-select" @keydown="searchDataEnter" clearable />
                        </el-col>
                        <el-col :span="2">
                            <el-button type="success" @click="searchData()" style="height:40px">
                                <el-icon>
                                    <Search />
                                </el-icon>
                            </el-button>
                        </el-col>
                    </el-row>
                </el-col>
                <el-col :span="24">
                    <el-table :data="exeLogList" style="width: 100%">
                        <!-- <el-table-column prop="id" label="会话ID"></el-table-column> -->
                        <el-table-column prop="beginTime" label="开始时间"></el-table-column>
                        <!-- <el-table-column prop="endTime" label="结束时间"></el-table-column> -->
                        <el-table-column prop="contentTa" label="执行脚本a"></el-table-column>
                        <el-table-column prop="contentTb" label="执行脚本b"></el-table-column>
                        <el-table-column prop="contentTc" label="执行脚本c"></el-table-column>
                        <el-table-column fixed="right" label="操作">
                            <template #default="scope">
                                <el-button type="text" round size="small" @click="fileDetail(scope.row.id)">详情</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
        </el-tab-pane>
    </el-tabs>

    <!-- Form -->
    <el-dialog :title="this.machineForm.mode == 'create' ? '创建服务链接' : '编辑服务链接'" v-model="this.machineForm.dialog">
        <el-form :model="this.machineForm.prop">
            <el-form-item label="名称" :label-width="formLabelWidth">
                <el-input v-model="this.machineForm.prop.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="描述" :label-width="formLabelWidth">
                <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="this.machineForm.prop.description"></el-input>
            </el-form-item>
            <el-form-item label="类型" :label-width="formLabelWidth">
                <el-select v-model="this.machineForm.prop.type" placeholder="请选择">
                    <el-option v-for="item in machineTypeSelect" :key="item.value" :label="item.label" :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="地址" :label-width="formLabelWidth">
                <el-input v-model="this.machineForm.prop.address" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="端口" :label-width="formLabelWidth">
                <el-input v-model="this.machineForm.prop.port" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="账号" :label-width="formLabelWidth">
                <el-input v-model="this.machineForm.prop.username" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="密码" :label-width="formLabelWidth">
                <el-input v-model="this.machineForm.prop.password" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="this.machineForm.dialog = false">取 消</el-button>
                <el-button type="primary" @click="saveMachine">保 存</el-button>
            </span>
        </template>
    </el-dialog>

    <!-- Form -->
    <el-dialog :title="this.cmdForm.mode == 'create' ? '创建服务指令' : '编辑服务指令'" v-model="this.cmdForm.dialog">
        <el-form :model="this.cmdForm.prop">
            <el-form-item label="名称" :label-width="formLabelWidth">
                <el-input v-model="this.cmdForm.prop.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="描述" :label-width="formLabelWidth">
                <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="this.cmdForm.prop.description"></el-input>
            </el-form-item>
            <el-form-item label="类型" :label-width="formLabelWidth">
                <el-select v-model="this.cmdForm.prop.type" placeholder="请选择">
                    <el-option v-for="item in cmdTypeSelect" :key="item.value" :label="item.label" :value="item.value"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="指令a" :label-width="formLabelWidth">
                <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="this.cmdForm.prop.contentTa"></el-input>
            </el-form-item>
            <el-form-item label="指令b" :label-width="formLabelWidth">
                <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="this.cmdForm.prop.contentTb"></el-input>
            </el-form-item>
            <el-form-item label="指令c" :label-width="formLabelWidth">
                <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="this.cmdForm.prop.contentTc"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="this.cmdForm.dialog = false">取 消</el-button>
                <el-button type="primary" @click="saveCmd">保 存</el-button>
            </span>
        </template>
    </el-dialog>

    <!-- fileDetail -->
    <el-dialog v-model="this.exeLogDialog.dialog" title="日志文件详情" width="90%" center fullscreen>
        <el-container>
            <el-main>
                <v-md-editor v-model="this.exeLogDialog.current.fileDetail" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />
            </el-main>
        </el-container>
        <template #footer>
            <span class="dialog-footer">
                <el-affix position="bottom" :offset="20">
                    <el-button type="primary" @click="this.exeLogDialog.dialog = false" style="width:300px">关闭</el-button>
                </el-affix>
            </span>
        </template>
    </el-dialog>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
import { Search, Share, Guide } from '@element-plus/icons';
export default {
    data() {
        return {
            machineForm: {
                mode: 'create',
                dialog: false,
                prop: {
                    name: '',
                    description: '',
                    type: '',
                    address: '',
                    port: 0,
                    username: '',
                    password: ''
                }
            },
            cmdForm: {
                mode: 'create',
                dialog: false,
                prop: {
                    machineId: '',
                    name: '',
                    description: '',
                    type: '',
                    contentTa: '',
                    contentTb: '',
                    contentTc: ''
                }
            },
            currentMachineId: '',
            currentMachineList: [],
            currentCmdList: [],
            exeLogDialog: {
                dialog: false,
                current: {
                    fileDetail: ''
                }
            },
            exeLogList: [],
            searchDataParams: {
                createTime: '',
                planName: '',
                dataSource: '',
                ip: '',
                mac: '',
                senderId: '',
                senderName: '',
                dataString: ''
            },
            formLabelWidth: '120px',
            machineTypeSelect: [
                {
                    value: 'ssh',
                    label: 'ssh'
                }
            ],
            cmdTypeSelect: [
                {
                    value: '通用',
                    label: 'normal'
                }
            ]
        };
    },
    components: { Search },
    mounted() {
        this.loadMachine();
    },
    methods: {
        searchDataEnter(e) {
            if (e.keyCode == 13) {
                this.searchData();
            }
        },
        searchData() {
            console.log('搜索 ' + this.searchMode + ' ' + this.searchText);
            this.searchDataParams.token = this.$store.state.user.token;
            this.searchDataParams.current = 1;
            this.searchDataParams.size = 100;
            return request({
                url: '/serverManExeLog/pageList',
                method: 'post',
                data: this.searchDataParams
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.exeLogList = res.data;
                }
            });
        },
        showCmdList(machineId) {
            console.log('current change is: ' + machineId);
            this.currentMachineId = machineId;
            this.loadCmd();
        },
        openCreateMachine() {
            // 清空表单数据
            this.machineForm.prop.name = '';
            this.machineForm.prop.description = '';
            this.machineForm.prop.type = '';
            this.machineForm.prop.address = '';
            this.machineForm.prop.port = '';
            this.machineForm.prop.username = '';
            this.machineForm.prop.password = '';

            this.machineForm.mode = 'create';
            this.machineForm.dialog = true;
        },
        openEditMachine(machineId) {
            console.log('编辑主机信息');

            return request({
                url: '/serverManMachine/get',
                method: 'post',
                data: { token: this.$store.state.user.token, id: machineId }
            }).then(res => {
                if (res.code == 0 && res.meta.serverManMachine) {
                    this.machineForm.prop = res.meta.serverManMachine;
                    this.machineForm.mode = 'edit';
                    this.machineForm.dialog = true;
                }
            });
        },
        loadMachine() {
            return request({
                url: '/serverManMachine/list',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    this.currentMachineList = res.meta.serverManMachines;
                    console.log(this.currentMachineList);
                }
            });
        },
        saveMachine() {
            if (this.machineForm.mode == 'create') {
                return request({
                    url: '/serverManMachine/create',
                    method: 'post',
                    data: {
                        name: this.machineForm.prop.name,
                        description: this.machineForm.prop.description,
                        type: this.machineForm.prop.type,
                        address: this.machineForm.prop.address,
                        port: this.machineForm.prop.port,
                        username: this.machineForm.prop.username,
                        password: this.machineForm.prop.password
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.machineForm.dialog = false;
                        this.loadMachine();
                    }
                });
            }
            if (this.machineForm.mode == 'edit') {
                return request({
                    url: '/serverManMachine/edit',
                    method: 'post',
                    data: {
                        id: this.machineForm.prop.id,
                        name: this.machineForm.prop.name,
                        description: this.machineForm.prop.description,
                        type: this.machineForm.prop.type,
                        address: this.machineForm.prop.address,
                        port: this.machineForm.prop.port,
                        username: this.machineForm.prop.username,
                        password: this.machineForm.prop.password
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.machineForm.dialog = false;
                        this.loadMachine();
                    }
                });
            }
        },
        deleteMachine(row) {
            return request({
                url: '/serverManMachine/delete',
                method: 'post',
                data: { token: this.$store.state.user.token, id: row.id }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.loadMachine();
                }
            });
        },
        openCreateCmd(machineId) {
            // 清空表单数据
            this.cmdForm.prop.machineId = machineId;
            this.cmdForm.prop.name = '';
            this.cmdForm.prop.description = '';
            this.cmdForm.prop.type = '';
            this.cmdForm.prop.contentTa = '';
            this.cmdForm.prop.contentTb = '';
            this.cmdForm.prop.contentTc = '';

            this.cmdForm.mode = 'create';
            this.cmdForm.dialog = true;
        },
        openEditCmd(row) {
            console.log('编辑指令信息');

            return request({
                url: '/serverManCmd/get',
                method: 'post',
                data: { token: this.$store.state.user.token, id: row.id }
            }).then(res => {
                if (res.code == 0 && res.meta.serverManCmd) {
                    this.cmdForm.prop = res.meta.serverManCmd;
                    this.cmdForm.mode = 'edit';
                    this.cmdForm.dialog = true;
                }
            });
        },
        loadCmd() {
            return request({
                url: '/serverManCmd/list',
                method: 'post',
                data: { token: this.$store.state.user.token, machineId: this.currentMachineId }
            }).then(res => {
                if (res.code == 0) {
                    this.currentCmdList = res.meta.serverManCmds;
                    console.log(this.currentCmdList);
                }
            });
        },
        saveCmd() {
            if (this.cmdForm.mode == 'create') {
                return request({
                    url: '/serverManCmd/create',
                    method: 'post',
                    data: {
                        machineId: this.cmdForm.prop.machineId,
                        name: this.cmdForm.prop.name,
                        description: this.cmdForm.prop.description,
                        type: this.cmdForm.prop.type,
                        contentTa: this.cmdForm.prop.contentTa,
                        contentTb: this.cmdForm.prop.contentTb,
                        contentTc: this.cmdForm.prop.contentTc
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.cmdForm.dialog = false;
                        this.loadCmd();
                    }
                });
            }
            if (this.cmdForm.mode == 'edit') {
                return request({
                    url: '/serverManCmd/edit',
                    method: 'post',
                    data: {
                        id: this.cmdForm.prop.id,
                        machineId: this.cmdForm.prop.machineId,
                        name: this.cmdForm.prop.name,
                        description: this.cmdForm.prop.description,
                        type: this.cmdForm.prop.type,
                        contentTa: this.cmdForm.prop.contentTa,
                        contentTb: this.cmdForm.prop.contentTb,
                        contentTc: this.cmdForm.prop.contentTc
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.cmdForm.dialog = false;
                        this.loadCmd();
                    }
                });
            }
        },
        deleteCmd(row) {
            return request({
                url: '/serverManCmd/delete',
                method: 'post',
                data: { token: this.$store.state.user.token, id: row.id }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.loadCmd();
                }
            });
        },
        runCmd(row) {
            console.log('runCmd: ' + row.id);
            return request({
                url: '/serverManCmd/run',
                method: 'post',
                data: { token: this.$store.state.user.token, id: row.id }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    ElMessage({
                        message: res.msg,
                        type: 'success',
                        duration: 5 * 1000
                    });
                }
            });
        },
        fileDetail(id) {
            return request({
                url: '/serverManExeLog/fileDetail',
                method: 'post',
                data: { token: this.$store.state.user.token, id: id }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.exeLogDialog.current.fileDetail = '```java' + '\r\n' + res.meta.fileDetail + '\r\n' + '```';
                    this.exeLogDialog.dialog = true;
                }
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
    margin: 10px;
}
</style>
