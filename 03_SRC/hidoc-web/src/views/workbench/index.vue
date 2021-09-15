<template>
    <el-container>
        <!-- 内容区域 -->
        <el-main>
            <el-row>
                <el-col :span="3">
                    <el-menu default-active="1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
                        <el-menu-item index="1" @click="changeMenu(1)">
                            <i class="el-icon-data-analysis"></i>
                            <template #title>信息看板</template>
                        </el-menu-item>
                        <el-menu-item index="2" @click="changeMenu(2)">
                            <i class="el-icon-collection"></i>
                            <template #title>文集文档</template>
                        </el-menu-item>
                        <el-menu-item index="3" @click="changeMenu(3)">
                            <i class="el-icon-files"></i>
                            <template #title>文件管理</template>
                        </el-menu-item>
                        <el-menu-item index="4" @click="changeMenu(4)">
                            <i class="el-icon-user"></i>
                            <template #title>个人信息</template>
                        </el-menu-item>
                    </el-menu>
                </el-col>
                <el-col :span="21">
                    <dash-board v-if="this.currentMenuIndex==1"></dash-board>
                    <doc-tab-page v-if="this.currentMenuIndex==2"></doc-tab-page>
                    <file-manager v-if="this.currentMenuIndex==3"></file-manager>
                    <user-info v-if="this.currentMenuIndex==4"></user-info>
                </el-col>
            </el-row>
        </el-main>
    </el-container>
    <el-backtop></el-backtop>

</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../utils/request.js';
import DashBoard from './components/DashBoard';
import DocTabPage from './components/DocTabPage';
import FileManager from './components/FileManager';
import UserInfo from './components/UserInfo';
export default {
    name: 'Home',
    data() {
        return {
            currentMenuIndex: 1,
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
            memberId: []
        };
    },
    mounted() {
        //  debugger;
        // let token = this.$store.state.user.token;
        // console.log('token-1-1-1: ' + token);
        // let name = this.$store.state.user.name;
        // console.log('name: ' + name);
        // this.loadCollected();
    },
    components: { DashBoard, DocTabPage, FileManager, UserInfo },
    methods: {
        handleChange(value, direction, movedKeys) {
            console.log(value, direction, movedKeys);
        },
        // 左侧菜单展开
        handleOpen(key, keyPath) {
            console.log(key, keyPath);
        },
        // 左侧菜单收起
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        },
        changeMenu(index) {
            this.currentMenuIndex = index;
            console.log('currentMenuIndex: ' + this.currentMenuIndex);
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