<template>
    <el-container>
        <el-header height="30px">
            <el-row>
                <el-button-group>
                    <el-button round type="success" size="small" @click="openCreateTeam">创建团队</el-button>
                </el-button-group>
            </el-row>
        </el-header>
        <el-main>
            <el-tabs type="card" style="padding: 10px">
                <el-tab-pane label="我加入的团队">
                    <el-row style="padding: 50px">
                        <el-col :span="24">
                            <el-collapse accordion @change="showTeamList">
                                <el-collapse-item v-for="item in currentMachineList" :key="item.id.toString()" :index="item.id" :title="item.name" :name="item.id">
                                    <div>
                                        {{ item.description }}
                                        <el-button type="text" round size="small" @click="openEditMachine(item.id)">[编辑]</el-button>
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
                <el-tab-pane label="我管理的团队">
                    <el-row style="padding: 50px">
                        <el-col :span="16">qweqweqweqwe</el-col>
                    </el-row>
                </el-tab-pane>
                <el-tab-pane label="所有团队">
                    <el-row style="padding: 50px">
                        <el-col :span="16">qweqweqweqwe</el-col>
                    </el-row>
                </el-tab-pane>
            </el-tabs>
        </el-main>
    </el-container>

    <!-- Form -->
    <el-dialog :title="this.teamForm.mode == 'create' ? '创建团队' : '编辑团队'" v-model="this.teamForm.dialog">
        <el-form :model="this.teamForm.prop">
            <el-form-item label="名称" :label-width="formLabelWidth">
                <el-input v-model="this.teamForm.prop.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="描述" :label-width="formLabelWidth">
                <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="this.teamForm.prop.description"></el-input>
            </el-form-item>
            <el-form-item label="自动加入邮箱地址" :label-width="formLabelWidth">
                <el-input v-model="this.teamForm.prop.autoJoinRule.email" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="自动加入邮箱后缀" :label-width="formLabelWidth">
                <el-input v-model="this.teamForm.prop.autoJoinRule.emailSuffix" autocomplete="off"></el-input>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="this.machineteamFormForm.dialog = false">取 消</el-button>
                <el-button type="primary" @click="saveTeam">保 存</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
export default {
    data() {
        return {
            teamForm: {
                mode: 'create',
                dialog: false,
                prop: {
                    name: '',
                    description: '',
                    autoJoinRule: { email: '', emailSuffix: '' }
                }
            },
            currentMachineList: []
        };
    },
    mounted() {},
    components: {},
    methods: {
        openCreateTeam() {
            // 清空表单数据
            this.teamForm.prop.name = '';
            this.teamForm.prop.description = '';
            this.teamForm.prop.autoJoinRule.email = '';
            this.teamForm.prop.autoJoinRule.emailSuffix = '';
            this.teamForm.mode = 'create';
            this.teamForm.dialog = true;
        },
        saveTeam() {},
        showTeamList() {}
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
