<template>
    <!-- 新的卡片设计 -->
    <div style="padding: 20px; border: 2px solid #aaf; border-radius: 20px; margin: 20px">
        <el-row>
            <el-col :span="2">
                <div style="background-color: lightgrey; border-radius: 0px; font-size: 14px; width: 40px; height: 32px; text-align: center; line-height: 32px">
                    <span v-if="this.dataObj._class == 'JavaDocClass'" style="font-weight: bold; color: dodgerblue">类</span>
                    <span v-if="this.dataObj._class == 'JavaDocMethod'" style="font-weight: bold; color: chocolate">方法</span>
                </div>
            </el-col>
            <el-col :span="16" style="line-height: 32px; text-align: left">
                <el-tag :type="this.dataObj._isPublic ? 'primary' : 'info'" style="margin: 5px" effect="dark">{{ this.dataObj.qualifier }}</el-tag>
                <span style="font-size: 14px; color: #333; cursor: pointer; font-weight: bold" @click="copy(this.dataObj.name)">{{ this.dataObj.name }}</span>
            </el-col>
            <el-col :span="6">
                <div style="text-align: right; font-size: 14px; font-weight: bold">
                    <span v-html="this.dataObj.projectName" />
                </div>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="20" style="margin-top: 10px">
                <span style="font-size: 14px" v-html="this.dataObj.commentInfo" />
            </el-col>
            <el-col :span="4">
                <div v-if="this.dataObj._class == 'JavaDocClass'" style="float: right">
                    <el-button type="primary" size="small" round v-if="this.dataObj.commentExample != ''" @click="showDialog('commentExampleDialog')">示例</el-button>
                    <el-button type="warning" size="small" round v-if="this.dataObj.commentLog != ''" @click="showDialog('commentLogDialog')">Log</el-button>
                    <el-button type="danger" size="small" round v-if="user.roles.includes('admin')" @click="showDialog('originDocumentDialog')">Code</el-button>
                </div>
                <div v-if="this.dataObj._class == 'JavaDocMethod'" style="float: right">
                    <el-button type="primary" size="small" round v-if="this.dataObj.commentExample != ''" @click="showDialog('commentExampleDialog')">示例</el-button>
                    <el-button type="success" size="small" round v-if="this.dataObj.javaDocClassLite ? true : false" @click="showDialog('classDetailsDialog')">类</el-button>
                    <el-button type="danger" size="small" round v-if="user.roles.includes('admin')" @click="showDialog('sourceCodeDialog')">Code</el-button>
                </div>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="24" style="margin: 5px; padding: 5px"></el-col>
        </el-row>
        <el-row v-if="this.dataObj.commentScene != ''">
            <el-col :span="24" style="margin-left: 5px; padding-left: 5px; color: blue">
                🎬️ 场景：
                <span v-html="this.dataObj.commentScene" />
            </el-col>
        </el-row>
        <el-row v-if="this.dataObj._class == 'JavaDocMethod'">
            <el-col :span="22" style="margin: 5px; padding: 5px">
                <el-descriptions :column="1" size="medium" border>
                    <el-descriptions-item>
                        <template #label>➡️ 传入</template>
                        <el-table :data="this.dataObj.paramsJson" stripe :show-header="false">
                            <el-table-column prop="type" label="类型" />
                            <el-table-column prop="desc" label="描述" />
                        </el-table>
                    </el-descriptions-item>
                    <el-descriptions-item :min-width="120">
                        <template #label>↩️ 返回</template>
                        <div>{{ this.dataObj.returnType }} {{ this.dataObj.returnDesc }}</div>
                    </el-descriptions-item>
                    <el-descriptions-item v-if="this.dataObj.throwses != ''">
                        <template #label>🐞 异常</template>
                        <el-table :data="this.dataObj.throwsesJson" stripe :show-header="false">
                            <el-table-column prop="type" label="类型" />
                            <el-table-column prop="desc" label="描述" />
                        </el-table>
                    </el-descriptions-item>
                </el-descriptions>
            </el-col>
        </el-row>
        <el-row v-if="this.dataObj.commentLimit != ''">
            <el-col :span="24" style="margin-left: 5px; padding-left: 5px; color: red">
                ⛔️ 限制：
                <span v-html="this.dataObj.commentLimit" />
            </el-col>
        </el-row>
        <!-- 关键字 -->
        <el-row v-if="this.dataObj.commentKeywords != ''">
            <el-col :span="24">
                <el-tag v-for="keyword in dataObj._commentKeywordArray" :key="keyword" type="warning" style="margin: 5px">{{ keyword }}</el-tag>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="24">
                <div style="text-align: right; font-size: 14px; font-weight: bold">
                    <span v-html="this.dataObj.javaDocClassLite ? '类：' + this.dataObj.javaDocClassLite.name : ''" />
                </div>
            </el-col>
        </el-row>
    </div>

    <!-- ============================================================================================================== -->
    <!-- ============================================================================================================== -->
    <!-- ============================================================================================================== -->

    <!-- 详细信息弹框 -->
    <!-- 示例说明 -->
    <el-dialog v-model="commentExampleDialog" title="示例说明" width="90%" center fullscreen>
        <el-container>
            <el-main>
                <v-md-editor v-model="this.dataObj.commentExample" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />
            </el-main>
        </el-container>
        <template #footer>
            <span class="dialog-footer">
                <el-affix position="bottom" :offset="20">
                    <el-button type="primary" @click="commentExampleDialog = false">我知道了</el-button>
                </el-affix>
            </span>
        </template>
    </el-dialog>
    <!-- 修改记录 -->
    <el-dialog v-model="commentLogDialog" title="修改记录" width="90%" center fullscreen>
        <el-container>
            <el-main>
                <el-table :data="this.dataObj.commentLogJson" :default-sort="{ prop: 'version', order: 'descending' }" style="width: 100%" border>
                    <el-table-column prop="version" label="版本" sortable width="180" />
                    <el-table-column prop="time" label="修改时间" sortable width="180" />
                    <el-table-column prop="author" label="修改人" width="180" />
                    <el-table-column prop="content" label="修改内容" />
                </el-table>
            </el-main>
        </el-container>
        <template #footer>
            <span class="dialog-footer">
                <el-affix position="bottom" :offset="20">
                    <el-button type="primary" @click="commentLogDialog = false">我知道了</el-button>
                </el-affix>
            </span>
        </template>
    </el-dialog>
    <!-- 类信息 -->
    <el-dialog v-model="classDetailsDialog" title="类信息" width="90%" center fullscreen>
        <el-container>
            <el-main>
                <div>
                    <h3>概述</h3>
                    <div v-if="this.dataObj.javaDocClassLite.commentInfo ? true : false">
                        <div v-html="this.dataObj.javaDocClassLite.commentInfo"></div>
                    </div>
                    <div v-else>无</div>

                    <h3>场景</h3>
                    <div v-if="this.dataObj.javaDocClassLite.commentScene ? true : false">
                        <div v-html="this.dataObj.javaDocClassLite.commentScene"></div>
                    </div>
                    <div v-else>无</div>

                    <h3>限制</h3>
                    <div v-if="this.dataObj.javaDocClassLite.commentLimit ? true : false">
                        <div v-html="this.dataObj.javaDocClassLite.commentLimit"></div>
                    </div>
                    <div v-else>无</div>

                    <h3>示例</h3>
                    <div v-if="this.dataObj.javaDocClassLite.commentExample ? true : false">
                        <v-md-editor v-model="this.dataObj.javaDocClassLite.commentExample" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />
                    </div>
                    <div v-else>无</div>

                    <h3>版本</h3>
                    <div v-if="this.dataObj.javaDocClassLite.commentLog ? true : false">
                        <el-table :data="this.dataObj.javaDocClassLite.commentLogJson" :default-sort="{ prop: 'version', order: 'descending' }" style="width: 100%" border>
                            <el-table-column prop="version" label="版本" sortable width="180" />
                            <el-table-column prop="time" label="修改时间" sortable width="180" />
                            <el-table-column prop="author" label="修改人" width="180" />
                            <el-table-column prop="content" label="修改内容" />
                        </el-table>
                    </div>
                    <div v-else>无</div>

                    <h3>关键字</h3>
                    <div v-if="this.dataObj.javaDocClassLite.commentKeywords ? true : false">
                        <div v-html="this.dataObj.javaDocClassLite.commentKeywords"></div>
                    </div>
                    <div v-else>无</div>
                </div>
            </el-main>
        </el-container>
        <template #footer>
            <span class="dialog-footer">
                <el-affix position="bottom" :offset="20">
                    <el-button type="primary" @click="classDetailsDialog = false" style="width: 300px">我知道了</el-button>
                </el-affix>
            </span>
        </template>
    </el-dialog>
    <!-- 方法源码 -->
    <el-dialog v-model="sourceCodeDialog" title="方法源码" width="90%" center fullscreen>
        <el-container>
            <el-main>
                <v-md-editor v-model="this.sourceCodeText" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />
            </el-main>
        </el-container>
        <template #footer>
            <span class="dialog-footer">
                <el-affix position="bottom" :offset="20">
                    <el-button type="primary" @click="sourceCodeDialog = false" style="width: 300px">关闭</el-button>
                </el-affix>
            </span>
        </template>
    </el-dialog>
    <!-- 源文件 -->
    <el-dialog v-model="originDocumentDialog" title="源文件" width="90%" center fullscreen>
        <el-container>
            <el-main>
                <v-md-editor v-model="this.originalDocumentText" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />
            </el-main>
        </el-container>
        <template #footer>
            <span class="dialog-footer">
                <el-affix position="bottom" :offset="20">
                    <el-button type="primary" @click="originDocumentDialog = false" style="width: 300px">关闭</el-button>
                </el-affix>
            </span>
        </template>
    </el-dialog>
</template>

<script>
import { ElMessageBox, ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
import { copy } from '../../../utils/clipboard.js';
export default {
    props: {
        data: Object
    },
    data() {
        return {
            isLogin: false,
            commentExampleDialog: false,
            commentLogDialog: false,
            classDetailsDialog: false,
            originDocumentDialog: false,
            sourceCodeDialog: false,
            sourceCodeText: '',
            originalDocumentText: '',
            dataObj: {},
            user: { roles: [] }
        };
    },
    mounted() {
        this.user.roles = this.$store.state.user.roles;
        if (this.$store.state.user.token == undefined || this.$store.state.user.token == '') {
            this.isLogin = false;
            this.user.roles = [];
        } else {
            this.isLogin = true;
        }

        this.dataObj = this.data;

        // 设置是否public
        if (this.dataObj.qualifier.indexOf('public') > -1) {
            this.dataObj._isPublic = true;
        }

        // 结构化关键字信息
        if (this.dataObj.commentKeywords != '') {
            this.dataObj._commentKeywordArray = this.dataObj.commentKeywords.replaceAll('，', ',').split(',');
        }

        // 高亮关键字
        if (this.dataObj._highlightKeys && this.dataObj._highlightKeys.length > 0) {
            for (var i = 0; i < this.dataObj._highlightKeys.length; i++) {
                if (this.dataObj.commentInfo) this.dataObj.commentInfo = this.setHighlightKeys(this.dataObj.commentInfo, this.dataObj._highlightKeys[i]);
                if (this.dataObj.commentScene) this.dataObj.commentScene = this.setHighlightKeys(this.dataObj.commentScene, this.dataObj._highlightKeys[i]);
                if (this.dataObj.commentLimit) this.dataObj.commentLimit = this.setHighlightKeys(this.dataObj.commentLimit, this.dataObj._highlightKeys[i]);
                if (this.dataObj.commentKeywords) this.dataObj.commentKeywords = this.setHighlightKeys(this.dataObj.commentKeywords, this.dataObj._highlightKeys[i]);
            }
        }
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
        setHighlightKeys(text, key, bgColor) {
            var sText = text;
            bgColor = bgColor || 'orange';
            var sKey = "<span style='background-color: " + bgColor + ";'>" + key + '</span>';
            var regStr = new RegExp(key, 'g');
            sText = sText.replace(regStr, sKey); //替换key
            return sText;
        },
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
                case 'sourceCodeDialog':
                    {
                        if (!this.isLogin) {
                            ElMessage({
                                message: '查看方法源码请先登录哦~',
                                type: 'warning',
                                duration: 1 * 1000
                            });
                            return false;
                        }

                        let id = '';
                        let type = '';
                        if (this.dataObj._class == 'JavaDocClass') {
                            type = 'class';
                            id = this.dataObj.id;
                        }
                        if (this.dataObj._class == 'JavaDocMethod') {
                            type = 'method';
                            id = this.dataObj.id;
                        }
                        request({
                            url: '/javadoc/getSourceCode',
                            method: 'post',
                            data: {
                                id: id,
                                type: type
                            }
                        }).then(res => {
                            if (res.code == 0) {
                                this.sourceCodeText = '```java' + '\r\n' + res.meta.sourceCode + '\r\n' + '```';
                            }
                        });
                        this.sourceCodeDialog = true;
                    }
                    break;
                case 'originDocumentDialog':
                    {
                        if (!this.isLogin) {
                            ElMessage({
                                message: '查看源文件请先登录哦~',
                                type: 'warning',
                                duration: 1 * 1000
                            });
                            return false;
                        }

                        let classId = '';
                        if (this.dataObj._class == 'JavaDocClass') classId = this.dataObj.id;
                        if (this.dataObj._class == 'JavaDocMethod') classId = this.dataObj.classId;
                        request({
                            url: '/javadoc/getOriginalDocument',
                            method: 'post',
                            data: {
                                classId: classId
                            }
                        }).then(res => {
                            if (res.code == 0) {
                                this.originalDocumentText = '```java' + '\r\n' + res.meta.originalDocument + '\r\n' + '```';
                                // console.log(this.originalDocumentText);
                                // ElMessage({
                                //     message: '搜索完成',
                                //     type: 'success',
                                //     duration: 1 * 1000
                                // });
                            }
                        });
                        this.originDocumentDialog = true;
                    }
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
