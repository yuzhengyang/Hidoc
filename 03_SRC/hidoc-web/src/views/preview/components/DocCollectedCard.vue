<template>
    <el-card shadow="hover">
        <template #header>
            <div class="card-header" @click="viewCollected">
                <span>{{ data.name }}</span>
                <span>[{{ data.docTotal }}篇]</span>
                <!-- <el-button class="button" type="text">操作按钮</el-button> -->
            </div>
        </template>
        <div v-for="doc in data.docLites" :key="doc" class="text item" style="padding: 0px">
            <el-row class="doc-item" style="cursor: pointer; padding: 2px" @click="viewDoc(doc.id)">
                <el-col :span="20" style="color: #888; font-size: 14px">
                    {{ ellipsis(doc.title) }}
                </el-col>
                <el-col :span="4" style="color: #f88; font-size: 12px; text-align: right">
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
    methods: {
        ellipsis(value) {
            let textLength = 30;
            if (!value) return '';
            if (value.length > textLength) {
                return value.slice(0, textLength) + '...';
            }
            return value;
        },
        viewCollected() {
            this.$router.push({ name: 'collected', params: { collectedId: this.data.id, docId: '_intro' } });
        },
        viewDoc(id) {
            this.$router.push({ name: 'collected', params: { collectedId: this.data.id, docId: id } });
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
    margin: 8px;
    padding: 0px;
    --el-card-padding: 10px;
}
.card-header {
    font-size: 14px;
    cursor: pointer;
    font-weight: bold;
    padding: 2px;
}
/* 文档条目特效 */
.doc-item {
    cursor: pointer;
    background: transparent;
    border: 0;
    border-radius: 0;
    position: relative;
}
.doc-item:before {
    transition: all 0.3s linear ;
    content: '';
    width: 0%;
    height: 100%;
    position: absolute;
    bottom: 0;
    left: 0;
    border-radius: 6px;
}
.doc-item:hover:before {
    background: linear-gradient(160deg,  #00fff263 0%, #1e5ee979 100%); 
    width: 100%;
    height: 1px;
}
</style>
