<template>
    <el-tabs type="card" @tab-click="handleClick" style="padding:10px">
        <el-tab-pane label="收集计划">
            <el-row>
                <el-button-group>
                    <el-button round type="success" @click="openCreateCollected">创建</el-button>
                </el-button-group>
            </el-row>
            <el-row style="padding:50px">
                <el-col :span="24">
                    <el-table :data="planList" style="width: 100%">
                        <el-table-column prop="name" label="计划名称"></el-table-column>
                        <el-table-column prop="description" label="描述信息"></el-table-column>
                        <el-table-column prop="token" label="token"></el-table-column>
                        <el-table-column label="时间范围" width="200">
                            <template #default="scope">
                                <div class="name-wrapper">
                                    <span>{{scope.row.startTime}} ~ {{scope.row.stopTime}}</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="状态" width="80">
                            <template #default="scope">
                                <div class="name-wrapper">
                                    <div v-if="scope.row.isEnable">
                                        <el-tag size="medium" type="success">启用</el-tag>
                                    </div>
                                    <div v-else>
                                        <el-tag size="medium" type="danger">停用</el-tag>
                                    </div>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column prop="dataCount" label="数据数量" width="100"></el-table-column>
                        <el-table-column fixed="right" label="操作" width="200">
                            <template #default="scope">
                                <el-button @click="enablePlan(scope.row)" type="text" size="small">启用/停用</el-button>

                                <el-popover placement="top-start" :width="200" trigger="click">
                                    <p>清空数据操作不可撤回，确定删除吗？</p>
                                    <div style="text-align: right; margin: 0">
                                        <el-button type="danger" size="mini" @click="deleteData(scope.row)">确定</el-button>
                                    </div>
                                    <template #reference>
                                        <el-button type="text" size="small">清空数据</el-button>
                                    </template>
                                </el-popover>

                                <el-popover placement="top-start" :width="200" trigger="click">
                                    <p>删除操作不可撤回，确定删除吗？</p>
                                    <div style="text-align: right; margin: 0">
                                        <el-button type="danger" size="mini" @click="deletePlan(scope.row)">确定</el-button>
                                    </div>
                                    <template #reference>
                                        <el-button type="text" size="small">删除计划</el-button>
                                    </template>
                                </el-popover>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
        </el-tab-pane>

        <el-tab-pane label="收集数据">
            <el-row style="padding:50px">
                <el-col :span="24">
                    筛选条件：创建时间（必填），计划名称，数据来源，终端类型，IP地址，MAC地址，用户姓名，详细内容
                </el-col>
                <el-col :span="24">
                    <el-table :data="tableData" style="width: 100%">
                        <el-table-column prop="ip" label="创建时间"></el-table-column>
                        <el-table-column prop="ip" label="计划名称"></el-table-column>
                        <el-table-column prop="ip" label="数据来源"></el-table-column>
                        <el-table-column prop="ip" label="终端类型"></el-table-column>
                        <el-table-column prop="ip" label="IP地址"></el-table-column>
                        <el-table-column prop="ip" label="MAC地址"></el-table-column>
                        <el-table-column prop="ip" label="用户姓名"></el-table-column>
                        <el-table-column prop="ip" label="详细内容"></el-table-column>
                    </el-table>
                </el-col>
            </el-row>
        </el-tab-pane>

        <el-tab-pane label="数据可视化">
            <el-row style="padding:50px">
                <el-col :span="24">
                    <el-table :data="tableData" style="width: 100%">
                        <el-table-column prop="ip" label="模型名称"></el-table-column>
                        <el-table-column fixed="right" label="操作" width="200">
                            <template #default="scope">
                                <el-button type="text" size="small">查看图形</el-button>

                                <el-popover placement="top-start" :width="200" trigger="click">
                                    <p>删除操作不可撤回，确定删除吗？</p>
                                    <div style="text-align: right; margin: 0">
                                        <el-button type="danger" size="mini" @click="forceLogout(scope.row)">确定</el-button>
                                    </div>
                                    <template #reference>
                                        <el-button type="text" size="small">删除模型</el-button>
                                    </template>
                                </el-popover>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
        </el-tab-pane>
    </el-tabs>


    <!-- Form -->
    <el-dialog :title="this.dialogFormMode=='create'?'创建数据收集计划':'编辑数据收集计划'" v-model="dialogFormVisible">
        <el-form :model="dataForm">
            <el-form-item label="计划名称" :label-width="formLabelWidth">
                <el-input v-model="dataForm.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="计划描述" :label-width="formLabelWidth">
                <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="dataForm.description">
                </el-input>
            </el-form-item>
            <el-form-item label="时间范围" :label-width="formLabelWidth">
                <el-date-picker v-model="dataForm.time" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="运行状态" :label-width="formLabelWidth">
                <el-switch v-model="dataForm.isEnable" active-color="#13ce66" inactive-color="#ff4949">
                </el-switch>
                <span> （打开时，开始接收数据） </span>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveCollected">保 存</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
import { datetimeFormat } from '../../../utils/datetime.js';
export default {
    data() {
        return {
            dialogFormMode: 'create',
            dialogFormVisible: false,
            dataForm: {
                name: '',
                description: '',
                time: [],
                startTime: '',
                stopTime: '',
                isEnable: false
            },
            planList: []
        };
    },
    mounted() {
        this.loadPlan();
        this.loadData();
    },
    methods: {
        openCreateCollected() {
            this.dialogFormVisible = true;
        },
        loadPlan() {
            return request({
                url: '/datacollplan/list',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    this.planList = res.meta.plans;
                    console.log(this.planList);
                }
            });
        },
        loadData() {
            return request({
                url: '/datacoll/list',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                }
            });
        },
        saveCollected() {
            if (this.dialogFormMode == 'create') {
                return request({
                    url: '/datacollplan/create',
                    method: 'post',
                    data: {
                        name: this.dataForm.name,
                        description: this.dataForm.description,
                        startTime: datetimeFormat(this.dataForm.time[0], 'yyyy-MM-dd HH:mm:ss'),
                        stopTime: datetimeFormat(this.dataForm.time[1], 'yyyy-MM-dd HH:mm:ss'),
                        isEnable: this.dataForm.isEnable
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.dialogFormVisible = false;
                        this.loadPlan();
                    }
                });
            }
            if (this.dialogFormMode == 'edit') {
                return request({
                    url: '/datacollplan/edit',
                    method: 'post',
                    data: {
                        id: this.currentCollected.id,
                        name: this.dataForm.name,
                        description: this.dataForm.description,
                        startTime: datetimeFormat(this.dataForm.time[0], 'yyyy-MM-dd HH:mm:ss'),
                        stopTime: datetimeFormat(this.dataForm.time[1], 'yyyy-MM-dd HH:mm:ss'),
                        isEnable: this.dataForm.isEnable
                    }
                }).then(res => {
                    if (res.code == 0) {
                        this.dialogFormVisible = false;
                        this.loadPlan();
                    }
                });
            }
        },
        enablePlan(row) {
            return request({
                url: '/datacollplan/enable',
                method: 'post',
                data: { token: this.$store.state.user.token, id: row.id, enable: !row.isEnable }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.loadPlan();
                }
            });
        },
        deleteData() {},
        deletePlan(row) {
            return request({
                url: '/datacollplan/delete',
                method: 'post',
                data: { token: this.$store.state.user.token, id: row.id }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.loadPlan();
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
