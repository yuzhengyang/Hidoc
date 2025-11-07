<template>
    <print-toolbar></print-toolbar>
    <div style="line-height: 60px; text-align: center; font-size: 30px; font-weight: bold">
        {{ this.doc.title }}
    </div>
    <div>
        <v-md-editor id="vmdEditor" v-model="this.doc.content" mode="preview" ref="editor"
            @copy-code-success="handleCopyCodeSuccess" />
    </div>
    <!-- :style="{height: getHeight()}" -->
    <!-- <div style="height: 1000px"></div> -->
    <!-- <div style="page-break-after: always"></div> -->
    <!-- <p>&nbsp;</p> -->
</template>

<script>
import request from '../../utils/request.js';
import { ElMessageBox, ElMessage } from 'element-plus';
import { mdFormat } from '../../utils/mdtools';
import watermark from '../../utils/watermark';
import PrintToolbar from './components/PrintToolbar.vue';

export default {
    components: {
        PrintToolbar
    },
    data() {
        return {
            doc: {}
        };
    },
    mounted() {
        document.getElementById('app-container').className = '';

        console.log('docId:' + this.$route.params.docId);
        this.getDoc(this.$route.params.docId);

        var user = this.$store.state.user;
        console.log(user);
        if (user) {
            watermark.set(user.realName, user.email);
        }
    },
    methods: {
        getDoc(id) {
            request({
                url: '/doc/get',
                method: 'post',
                data: {
                    id: id
                }
            }).then(res => {
                if (res.code == 0) {
                    this.doc = res.meta.doc;
                    // 预览前对文本进行处理，然后再渲染预览
                    this.doc.content = mdFormat(this.doc.content)
                        + '\n'
                        + '\n'
                        + '<p>&nbsp;</p><p>&nbsp;</p>'
                        + '<p>&nbsp;</p><p>&nbsp;</p>'
                        + '<div style="height: 1000px"></div>'
                        + '<div style="page-break-after:always"></div>';
                    document.title = this.doc.title;
                }
            });
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

/* @media print {
    body {
        height: auto !important;
    }
} */
/* 打印样式优化：去掉页眉页脚 + 调整边距 */
@media print {

    /* 关键：设置打印页面的边距，消除页眉页脚的默认空间 */
    @page {
        size: auto;
        /* 自动适应纸张大小 */
        margin: 20px;
        /* 边距设为 0，会挤压页眉页脚的显示空间 */
    }

    /* 可选：给 body 添加上下内边距，避免内容紧贴纸张边缘 */
    body {
        padding: 20px;
        /* 相当于页面内容的“内边距”，避免太靠边 */
        background-color: #fff;
        /* 确保打印背景为白色 */
        -webkit-print-color-adjust: exact;
        /* 强制打印背景色（部分浏览器需要） */
        print-color-adjust: exact;
    }
}
</style>
