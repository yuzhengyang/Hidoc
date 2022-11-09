<template>
    <el-container>
        <!-- 内容区域 -->
        <el-main>
            <el-tabs type="card" @tab-click="handleClick" style="padding: 10px">
                <el-tab-pane label="文档列表">
                    <el-row style="padding: 50px">
                        <el-col :span="24">
                            <el-alert title="！注意：回收站只保留180天的记录" type="error" :closable="false" />
                        </el-col>
                        <el-col :span="24"></el-col>
                        <el-col :span="24">
                            <el-table :data="deletedDocTable" style="width: 100%">
                                <el-table-column prop="collectedName" label="文集"></el-table-column>
                                <el-table-column prop="title" label="文档"></el-table-column>
                                <el-table-column prop="ownerUser.realName" label="拥有者" width="100"></el-table-column>
                                <el-table-column label="修改时间" width="100">
                                    <template #default="scope">
                                        <el-popover effect="light" trigger="hover" placement="top">
                                            <template #default>
                                                <p>{{ scope.row.updateTime }}</p>
                                            </template>
                                            <template #reference>
                                                <div class="name-wrapper">
                                                    {{ scope.row.relativeUpdateTime }}
                                                </div>
                                            </template>
                                        </el-popover>
                                    </template>
                                </el-table-column>
                                <el-table-column label="删除时间" width="100">
                                    <template #default="scope">
                                        <el-popover effect="light" trigger="hover" placement="top">
                                            <template #default>
                                                <p>{{ scope.row.deleteTime }}</p>
                                            </template>
                                            <template #reference>
                                                <div class="name-wrapper">
                                                    {{ scope.row.relativeDeleteTime }}
                                                </div>
                                            </template>
                                        </el-popover>
                                    </template>
                                </el-table-column>
                                <el-table-column prop="deleteUser.realName" label="删除人" width="100"></el-table-column>
                                <el-table-column fixed="right" label="操作" width="200">
                                    <template #default="scope">
                                        <el-popover placement="top-start" :width="200" trigger="click">
                                            <p>确定还原吗？</p>
                                            <div style="text-align: right; margin: 0">
                                                <el-button type="danger" size="mini" @click="docRestore(scope.row)">确定</el-button>
                                            </div>
                                            <template #reference>
                                                <el-button type="text" size="small">还原</el-button>
                                            </template>
                                        </el-popover>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </el-col>
                    </el-row>
                </el-tab-pane>
                <el-tab-pane label="文集列表"></el-tab-pane>
                <el-tab-pane label="文件列表">
                    <el-row style="padding: 50px">
                        <el-col :span="24">
                            <el-alert title="！注意：回收站只保留180天的记录" type="error" :closable="false" />
                        </el-col>
                        <el-col :span="24"></el-col>
                        <el-col :span="24">
                            <el-table :data="deletedFileTable" style="width: 100%">
                                <el-table-column prop="fileName" label="文件名"></el-table-column>
                                <el-table-column prop="createTime" label="创建时间"></el-table-column>
                                <el-table-column prop="deleteTime" label="删除时间"></el-table-column>
                                <el-table-column fixed="right" label="操作" width="200">
                                    <template #default="scope">
                                        <el-popover placement="top-start" :width="200" trigger="click">
                                            <p>确定还原吗？</p>
                                            <div style="text-align: right; margin: 0">
                                                <el-button type="danger" size="mini" @click="fileRestore(scope.row)">确定</el-button>
                                            </div>
                                            <template #reference>
                                                <el-button type="text" size="small">还原</el-button>
                                            </template>
                                        </el-popover>
                                    </template>
                                </el-table-column>
                            </el-table>
                        </el-col>
                    </el-row>
                </el-tab-pane>
            </el-tabs>
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
            rightValue: [],
            currentCollected: {},
            mineList: [],
            coopList: [],
            deletedDocTable: [],
            deletedFileTable: [],
            dialogFormMode: 'create',
            dialogFormVisible: false,
            dialogMemberVisible: false,
            createDocVisible: false,
            collectedForm: {
                name: '',
                description: '',
                isOpen: false
            },
            formLabelWidth: '120px',
            memberUser: [],
            otherUser: [],
            allUser: [],
            memberId: []
        };
    },
    mounted() {
        //  debugger;
        let token = this.$store.state.user.token;
        let name = this.$store.state.user.name;
        this.loadDeletedDoc();
        this.loadDeletedFile();
    },
    components: {},
    methods: {
        loadDeletedDoc() {
            return request({
                url: '/doc/deletedList',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.deletedDocTable = res.data;
                }
            });
        },
        loadDeletedFile() {
            return request({
                url: '/file/deletedList',
                method: 'post',
                data: { token: this.$store.state.user.token }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.deletedFileTable = res.data;
                }
            });
        },
        // 还原
        docRestore(row) {
            console.log('还原');
            return request({
                url: '/doc/restore',
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

                    // this.selectCollected(this.currentCollected);
                    this.loadDeletedDoc();
                } else {
                    ElMessage({
                        message: res.msg || 'Error',
                        type: 'error',
                        duration: 5 * 1000
                    });
                }
            });
        },
        // 还原
        fileRestore(row) {
            console.log('还原');
            return request({
                url: '/file/restore',
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

                    // this.selectCollected(this.currentCollected);
                    this.loadDeletedFile();
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
