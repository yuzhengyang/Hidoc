<template>
    <!-- 类卡片和方法卡片 -->
    <el-card v-if="data._class == 'JavaDocClass'" shadow="hover">
        <template #header>
            <div class="card-header" style="cursor:pointer;" @click="viewCollected">
                <span>{{ data.name }}</span>
                <span>[{{ data.docTotal }}篇]</span>
            </div>
        </template>
        <div class="text item" style="padding:0px;">
            {{ data.commentInfo }}
            {{ data.commentScene }}
            {{ data.commentLimit }}
            <v-md-editor v-model="this.dataObj.commentExample" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />
            {{ data.commentLog }}
            {{ data.CommentKeywords }}
        </div>
    </el-card>
    <el-card v-if="data._class == 'JavaDocMethod'" shadow="hover">
        <template #header>
            <div class="card-header" style="cursor:pointer;" @click="viewCollected">
                <span>{{ data.name }}</span>
                <span>[{{ data.docTotal }}篇]</span>
            </div>
        </template>
        <div v-for="doc in data.docLites" :key="doc" class="text item" style="padding:0px;">
            <el-row style="cursor:pointer;padding:2px" @click="viewDoc(doc.id)">
                <el-col :span="19" style="color:#888;font-size:14px">
                    {{ ellipsis(doc.title) }}
                </el-col>
                <el-col :span="5" style="color:#f88;font-size:12px">
                    {{ doc.relativeUpdateTime }}
                </el-col>
            </el-row>
        </div>
    </el-card>
</template>

<script>
export default {
    props: {
        data: Object
    },
    data() {
        return {
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
