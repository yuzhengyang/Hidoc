<template>
    <div style="line-height: 60px; text-align: center; font-size: 30px; font-weight: bold">
        {{ this.title }}
    </div>
    <div>
        <v-md-editor id="vmdEditor" v-model="this.content" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />
    </div>
    <!-- :style="{height: getHeight()}" -->
    <!-- <div style="height: 1000px"></div> -->
    <!-- <div style="page-break-after: always"></div> -->
    <!-- <p>&nbsp;</p> -->
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <p>&nbsp;</p>
    <div style="height: 1000px"></div>
    <div style="page-break-after: always"></div>
</template>

<script>
import request from '../../utils/request.js';
import { ElMessageBox, ElMessage } from 'element-plus';
import { mdFormat } from '../../utils/mdtools';
import watermark from '../../utils/watermark';
import _ from 'lodash';
import { nextTick } from 'vue';

export default {
    data() {
        return {
            docList: [],
            title: '',
            content: '',
            tempContent: ''
        };
    },
    mounted() {
        document.getElementById('app-container').className = '';

        console.log('collectedId:' + this.$route.params.collectedId);

        var user = this.$store.state.user;
        console.log(user);
        if (user) {
            watermark.set(user.realName, user.email);
        }

        this.getContent();
    },
    methods: {
        async getContent() {
            console.log(`getCollected`);
            await this.getCollected();

            console.log(`getAllDoc`);
            await this.getAllDoc(this.docList);

            console.log(`fmtContent`);
            await this.fmtContent();
        },
        async getCollected() {
            await request({
                url: '/collected/get',
                method: 'post',
                data: {
                    id: this.$route.params.collectedId
                }
            }).then(res => {
                if (res.code == 0 && res.meta.collected && res.meta.collected.docLites) {
                    this.docList = res.meta.collected.docLites;
                    this.title = res.meta.collected.name;
                    document.title = this.title;
                }
            });
        },
        async getAllDoc(list) {
            let that = this;
            for (let item of list) {
                await that.getDoc(item.id);
                if (item.children) {
                    await that.getAllDoc(item.children);
                }
            }
        },
        async getDoc(id) {
            await request({
                url: '/doc/get',
                method: 'post',
                data: {
                    id: id
                }
            }).then(res => {
                if (res.code == 0) {
                    this.tempContent = this.tempContent + '\n\n' + res.meta.doc.content;
                    console.log(`读取文档：${res.meta.doc.title}，长度:${res.meta.doc.content.length}`);
                }
            });
        },
        async fmtContent() {
            let that = this;
            let _t = mdFormat(that.tempContent);
            this.$nextTick(() => {
                that.content = _t;
                // 滚动到顶部
                window.scrollTo(0, 0);
            });
            console.log(`所有文档内容合并，内容总长度：${this.content.length}`);
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
.el-container {
    height: '';
}
.el-aside {
    padding-right: 10px;
    border-right: 1px solid #bbb;
}
.el-menu {
    border-right: 0;
}
.el-drawer {
    overflow: scroll;
    overflow-x: hidden;
    overflow-y: auto;
}

::-webkit-scrollbar {
    width: 0px;
    background-color: #d8d8d8;
}

/* 滚动槽 */
::-webkit-scrollbar-track {
    border-radius: 10px;
}

::-webkit-scrollbar-thumb {
    background-color: #bfc1c4;
}
@media print {
    body {
        height: auto !important;
    }
}
</style>
