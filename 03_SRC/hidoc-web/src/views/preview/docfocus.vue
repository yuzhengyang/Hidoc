<template>
    <v-md-editor id="vmdEditor" v-model="this.doc.content" mode="preview" ref="editor" @copy-code-success="handleCopyCodeSuccess" />
    <!-- :style="{height: getHeight()}" -->
</template>

<script>
import request from '../../utils/request.js';
import { ElMessageBox, ElMessage } from 'element-plus';
export default {
    data() {
        return {
            doc: {}
        };
    },
    mounted() {
        document.getElementById('app-container').className = '';

        console.log('docId:' + this.$route.params.docId);
        this.getDoc(this.$route.params.docId);
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
</style>