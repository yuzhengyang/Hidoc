<template>
    <el-tabs type="card" @tab-click="handleClick" style="padding:10px">
        <el-tab-pane label="收集计划">
            <el-row style="padding:50px">
                <el-col :span="24">
                    <el-table :data="tableData" style="width: 100%">
                        <el-table-column prop="ip" label="计划名称"></el-table-column>
                        <el-table-column prop="ip" label="描述信息"></el-table-column>
                        <el-table-column prop="loginTime" label="开始时间"></el-table-column>
                        <el-table-column prop="expiryTime" label="结束时间"></el-table-column>
                        <el-table-column label="状态" width="100">
                            <template #default="scope">
                                <div class="name-wrapper">
                                    <div v-if="this.$store.state.user.token == scope.row.token">
                                        <el-tag size="medium">是</el-tag>
                                    </div>
                                    <div v-else>
                                        <!-- <el-tag size="medium" type="danger">-</el-tag> -->
                                    </div>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column prop="loginTime" label="数量"></el-table-column>
                        <el-table-column fixed="right" label="操作" width="200">
                            <template #default="scope">
                                <el-popover placement="top-start" :width="200" trigger="click">
                                    <p>确定启用/停用吗？</p>
                                    <div style="text-align: right; margin: 0">
                                        <el-button type="danger" size="mini" @click="blockIp(scope.row)">确定</el-button>
                                    </div>
                                    <template #reference>
                                        <el-button type="text" size="small">启用/停用</el-button>
                                    </template>
                                </el-popover>

                                <el-popover placement="top-start" :width="200" trigger="click">
                                    <p>清空数据操作不可撤回，确定删除吗？</p>
                                    <div style="text-align: right; margin: 0">
                                        <el-button type="danger" size="mini" @click="forceLogout(scope.row)">确定</el-button>
                                    </div>
                                    <template #reference>
                                        <el-button type="text" size="small">清空数据</el-button>
                                    </template>
                                </el-popover>

                                <el-popover placement="top-start" :width="200" trigger="click">
                                    <p>删除操作不可撤回，确定删除吗？</p>
                                    <div style="text-align: right; margin: 0">
                                        <el-button type="danger" size="mini" @click="forceLogout(scope.row)">确定</el-button>
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
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
export default {
    data() {
        return {};
    },
    mounted() {},
    methods: {}
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
