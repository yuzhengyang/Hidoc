<template>
    <el-descriptions class="margin-top" :title="(data.javaDocClassLite ? '方法：' + data.javaDocClassLite.name + '.' : '类：') + data.name" :column="1" size="medium" border style="margin-top:10px">
        <template #extra>
            <el-button type="primary" size="small" round v-if="this.dataObj.commentExample != ''" :disabled="this.dataObj.commentExample == ''" @click="showDialog('commentExampleDialog')">示例说明</el-button>
            <el-button type="warning" size="small" round v-if="data.commentLog != ''" :disabled="data.commentLog == ''" @click="showDialog('commentLogDialog')">修改记录</el-button>
            <el-button type="success" size="small" round v-if="data.javaDocClassLite ? true : false" @click="showDialog('classDetailsDialog')">类信息</el-button>
            <!-- <el-button type="primary" size="small" @click="showDialog('originDocumentDialog')">源文件</el-button> -->
        </template>
        <el-descriptions-item>
            <template #label>
                概述
            </template>
            <div v-html="data.commentInfo"></div>
        </el-descriptions-item>
        <el-descriptions-item v-if="data.commentScene != ''">
            <template #label>
                使用场景
            </template>
            <div v-html="data.commentScene"></div>
        </el-descriptions-item>
        <el-descriptions-item v-if="data.commentLimit != ''">
            <template #label>
                限制
            </template>
            <div v-html="data.commentLimit"></div>
        </el-descriptions-item>
        <el-descriptions-item v-if="data.commentKeywords != ''">
            <template #label>
                搜索关键字
            </template>
            {{ data.commentKeywords }}
        </el-descriptions-item>
    </el-descriptions>

    <hr style="margin:50px;border:1px dashed #CCC" />

    <!-- 详细信息弹框 -->
    <!-- 示例说明 -->
    <el-dialog v-model="commentExampleDialog" title="示例说明" width="90%">
        <div>
            <v-md-editor v-model="this.dataObj.commentExample" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />
        </div>
        <template #footer>
            <span class="dialog-footer">
                <el-button type="primary" @click="commentExampleDialog = false">我知道了</el-button>
            </span>
        </template>
    </el-dialog>
    <!-- 修改记录 -->
    <el-dialog v-model="commentLogDialog" title="修改记录" width="90%">
        <div>
            <el-table :data="data.commentLogJson" :default-sort="{ prop: 'version', order: 'descending' }" style="width: 100%" border>
                <el-table-column prop="version" label="版本" sortable width="180" />
                <el-table-column prop="time" label="修改时间" sortable width="180" />
                <el-table-column prop="author" label="修改人" width="180" />
                <el-table-column prop="content" label="修改内容" />
            </el-table>
        </div>
        <template #footer>
            <span class="dialog-footer">
                <el-button type="primary" @click="commentLogDialog = false">我知道了</el-button>
            </span>
        </template>
    </el-dialog>
    <!-- 类信息 -->
    <el-dialog v-model="classDetailsDialog" title="类信息" width="90%" center="true" fullscreen="true">
        <el-container>
            <el-main>
                <div>
                    <h3>概述</h3>
                    <div v-if="this.dataObj.javaDocClassLite.commentInfo != ''">
                        <div v-html="this.dataObj.javaDocClassLite.commentInfo"></div>
                    </div>
                    <div v-else>无</div>

                    <h3>场景</h3>
                    <div v-if="this.dataObj.javaDocClassLite.commentScene != ''">
                        <div v-html="this.dataObj.javaDocClassLite.commentScene"></div>
                    </div>
                    <div v-else>无</div>

                    <h3>限制</h3>
                    <div v-if="this.dataObj.javaDocClassLite.commentLimit != ''">
                        <div v-html="this.dataObj.javaDocClassLite.commentLimit"></div>
                    </div>
                    <div v-else>无</div>

                    <h3>示例</h3>
                    <v-md-editor v-model="this.dataObj.javaDocClassLite.commentExample" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />

                    <h3>版本</h3>
                    <div>
                        <el-table :data="this.dataObj.javaDocClassLite.commentLogJson" :default-sort="{ prop: 'version', order: 'descending' }" style="width: 100%" border>
                            <el-table-column prop="version" label="版本" sortable width="180" />
                            <el-table-column prop="time" label="修改时间" sortable width="180" />
                            <el-table-column prop="author" label="修改人" width="180" />
                            <el-table-column prop="content" label="修改内容" />
                        </el-table>
                    </div>

                    <h3>关键字</h3>
                    <div v-if="this.dataObj.javaDocClassLite.commentKeywords != ''">
                        <div v-html="this.dataObj.javaDocClassLite.commentKeywords"></div>
                    </div>
                    <div v-else>无</div>
                </div>
            </el-main>
        </el-container>
        <template #footer>
            <span class="dialog-footer">
                <el-affix position="bottom" :offset="20">
                    <el-button type="primary" @click="classDetailsDialog = false" style="width:300px">我知道了</el-button>
                </el-affix>
            </span>
        </template>
    </el-dialog>
</template>

<script>
import { ElMessageBox, ElMessage } from 'element-plus';
export default {
    props: {
        data: Object
    },
    data() {
        return {
            commentExampleDialog: false,
            commentLogDialog: false,
            classDetailsDialog: false,
            originDocumentDialog: false,
            dataObj: {}
        };
    },
    mounted() {
        this.dataObj = this.data;
    },
    methods: {
        ellipsis(value) {
            let textLength = 30;
            if (!value) return '';
            if (value.length > textLength) {
                return value.slice(0, textLength) + '...';
            }
            return value;
        },
        viewDetails() {
            this.$router.push({ name: 'collected', params: { collectedId: this.data.id, docId: '_intro' } });
        },
        handleCopyCodeSuccess(code) {
            // console.log(code);
            ElMessage({
                message: '复制成功',
                type: 'success',
                duration: 1 * 1000
            });
        },
        showDialog(type) {
            switch (type) {
                case 'commentExampleDialog':
                    this.commentExampleDialog = true;
                    break;
                case 'commentLogDialog':
                    this.commentLogDialog = true;
                    break;
                case 'classDetailsDialog':
                    this.classDetailsDialog = true;
                    break;
                case 'originDocumentDialog':
                    this.originDocumentDialog = true;
                    break;
            }
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
.abow_dialog {
    display: flex;
    justify-content: center;
    align-items: Center;
    overflow: hidden;
    .el-dialog {
        margin: 0 auto !important;
        height: 90%;
        overflow: hidden;
        .el-dialog__body {
            position: absolute;
            left: 0;
            top: 54px;
            bottom: 0;
            right: 0;
            padding: 0;
            z-index: 1;
            overflow: hidden;
            overflow-y: auto;
        }
    }
}
</style>
