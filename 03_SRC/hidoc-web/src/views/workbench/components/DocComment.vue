<template>
    <el-container>
        <!-- 内容区域 -->
        <el-main>
            <el-row>
                <el-col :span="24">
                    <el-button-group>
                        <el-button type="primary" @click="loadMyCreated">我评论的</el-button>
                        <el-button type="success" @click="loadReplyMe">回复我的</el-button>
                    </el-button-group>
                </el-col>
                <el-col :span="24">
                    <el-table :data="tableData" style="width: 100%">
                        <!-- <el-table-column prop="collectedName" label="文集"></el-table-column> -->
                        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
                        <el-table-column prop="docLite.title" label="文档"></el-table-column>
                        <el-table-column v-if="queryType=='myCreated'" prop="replyUser.realName" label="回复给" width="100"></el-table-column>
                        <el-table-column v-if="queryType=='replyMe'" prop="createUser.realName" label="评论人" width="100"></el-table-column>
                        <el-table-column prop="isReplyUserRead" label="已读"></el-table-column>
                        <el-table-column prop="content" label="内容"></el-table-column>
                        <el-table-column fixed="right" label="操作" width="200">
                            <template #default="scope">
                                <el-button @click="previewDoc(scope.row)" type="text" size="small">查看</el-button>
                            </template>
                        </el-table-column>
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
            rightValue: [],
            currentCollected: {},
            mineList: [],
            coopList: [],
            tableData: [],
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
            memberId: [],
            queryType: 'myCreated', // myCreated replyMe
        };
    },
    mounted() {
        // debugger;
        let token = this.$store.state.user.token;
        let name = this.$store.state.user.name;

        this.queryType = 'myCreated';
        this.loadMyCreated();
    },
    components: {},
    methods: {
        loadMyCreated() {
            this.queryType = 'myCreated';
            this.loadDocComment({});
        },
        loadReplyMe() {
            this.queryType = 'replyMe';
            this.loadDocComment({});
        },
        loadDocComment(params) {
            return request({
                url: '/doccomment/myList',
                method: 'post',
                data: {
                    type: this.queryType
                }
            }).then(res => {
                if (res.code == 0) {
                    console.log(res);
                    this.tableData = res.data;
                }
            });
        },
        // 查看
        previewDoc(row) {
            console.log('查看');

            let routeData = this.$router.resolve({
                name: 'collected',
                params: { collectedId: row.docLite.collectedId, docId: row.docLite.id }
            });
            window.open(routeData.path, '_blank');

            // this.$router.push({ name: 'collected', params: { collectedId: this.currentCollected.id, docId: row.id } });
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
