<template>
    <el-container style="height: 100%">
        <!-- 内容区域 -->
        <el-aside width="180px" style="height: 100%; border-right: 1px solid #ccc">
            <el-menu default-active="1000" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
                <el-menu-item index="1000" @click="changeMenu(1000)">
                    <el-icon><DataAnalysis /></el-icon>
                    <template #title>信息看板</template>
                </el-menu-item>
                <el-menu-item index="2000" @click="changeMenu(2000)">
                    <el-icon><Collection /></el-icon>
                    <template #title>文集文档</template>
                </el-menu-item>
                <el-menu-item index="2010" @click="changeMenu(2010)">
                    <el-icon><Delete /></el-icon>
                    <template #title>回收站</template>
                </el-menu-item>
                <el-menu-item index="2020" @click="changeMenu(2020)">
                    <el-icon><ChatLineSquare /></el-icon>
                    <template #title>互动评论</template>
                </el-menu-item>
                <el-menu-item index="2030" @click="changeMenu(2030)">
                    <el-icon><OfficeBuilding /></el-icon>
                    <template #title>团队信息</template>
                </el-menu-item>
                <el-menu-item index="3000" @click="changeMenu(3000)">
                    <el-icon><Picture /></el-icon>
                    <template #title>素材库</template>
                </el-menu-item>
                <el-menu-item index="3010" @click="changeMenu(3010)">
                    <el-icon><Files /></el-icon>
                    <template #title>我的文件</template>
                </el-menu-item>
                <el-menu-item index="7000" v-if="user.roles.includes('sa')" @click="changeMenu(7000)">
                    <el-icon><Memo /></el-icon>
                    <template #title>代码注释 *</template>
                </el-menu-item>
                <el-menu-item index="8000" v-if="user.roles.includes('sa')" @click="changeMenu(8000)">
                    <el-icon><Box /></el-icon>
                    <template #title>数据收集器 *</template>
                </el-menu-item>
                <el-menu-item index="8020" v-if="user.roles.includes('sa')" @click="changeMenu(8020)">
                    <el-icon><Notification /></el-icon>
                    <template #title>服务管理 *</template>
                </el-menu-item>
                <el-menu-item index="9000" @click="changeMenu(9000)">
                    <el-icon><User /></el-icon>
                    <template #title>个人信息</template>
                </el-menu-item>
                <el-menu-item index="9900" v-if="user.roles.includes('sa')" @click="changeMenu(9900)">
                    <el-icon><Lock /></el-icon>
                    <template #title>权限管理 *</template>
                </el-menu-item>
                <el-menu-item index="9910" v-if="user.roles.includes('sa')" @click="changeMenu(9910)">
                    <el-icon><Lock /></el-icon>
                    <template #title>系统设置 *</template>
                </el-menu-item>
                <el-menu-item index="9920" v-if="user.roles.includes('sa')" @click="changeMenu(9920)">
                    <el-icon><Lock /></el-icon>
                    <template #title>系统状态 *</template>
                </el-menu-item>
                <el-menu-item index="9999" v-if="user.roles.includes('sa')" @click="changeMenu(9999)">
                    <el-icon><Pear /></el-icon>
                    <template #title>实验室 *</template>
                </el-menu-item>
            </el-menu>
        </el-aside>
        <el-main style="height: 100%">
            <dash-board v-if="this.currentMenuIndex == 1000"></dash-board>
            <doc-tab-page v-if="this.currentMenuIndex == 2000"></doc-tab-page>
            <recycle-bin v-if="this.currentMenuIndex == 2010"></recycle-bin>
            <doc-comment v-if="this.currentMenuIndex == 2020"></doc-comment>
            <team-manager v-if="this.currentMenuIndex == 2030"></team-manager>
            <material-library v-if="this.currentMenuIndex == 3000"></material-library>
            <file-manager v-if="this.currentMenuIndex == 3010"></file-manager>
            <java-doc v-if="this.currentMenuIndex == 7000"></java-doc>
            <data-collector v-if="this.currentMenuIndex == 8000"></data-collector>
            <server-manager v-if="this.currentMenuIndex == 8020"></server-manager>
            <user-info v-if="this.currentMenuIndex == 9000"></user-info>
            <user-roles v-if="this.currentMenuIndex == 9900"></user-roles>
            <system-config v-if="this.currentMenuIndex == 9910"></system-config>
            <system-status v-if="this.currentMenuIndex == 9920"></system-status>
            <laboratory v-if="this.currentMenuIndex == 9999"></laboratory>
        </el-main>
    </el-container>
    <el-backtop></el-backtop>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../utils/request.js';
import DashBoard from './components/DashBoard';
import DocTabPage from './components/DocTabPage';
import DocComment from './components/DocComment';
import FileManager from './components/FileManager';
import UserInfo from './components/UserInfo';
import MaterialLibrary from './components/MaterialLibrary';
import Laboratory from './components/Laboratory';
import DataCollector from './components/DataCollector';
import RecycleBin from './components/RecycleBin';
import JavaDoc from './components/JavaDoc';
import ServerManager from './components/ServerManager';
import TeamManager from './components/TeamManager';
import UserRoles from './components/UserRoles';
import SystemConfig from './components/SystemConfig';
import SystemStatus from './components/SystemStatus';
// import { Delete } from '@element-plus/icons';

export default {
    data() {
        return {
            currentMenuIndex: 1000,
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
            memberUser: [],
            otherUser: [],
            allUser: [],
            memberId: [],
            user: { roles: [] }
        };
    },
    mounted() {
        // debugger;
        // let token = this.$store.state.user.token;
        // console.log('token-1-1-1: ' + token);
        // let name = this.$store.state.user.name;
        // console.log('name: ' + name);
        // this.loadCollected();
        this.user.roles = this.$store.state.user.roles;
        document.title = 'Hidoc-工作台';
    },
    components: { DashBoard, DocTabPage, FileManager, UserInfo, MaterialLibrary, Laboratory, DataCollector, RecycleBin, JavaDoc, ServerManager, DocComment, TeamManager, UserRoles, SystemConfig, SystemStatus },
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
            switch (this.currentMenuIndex) {
                case 1000:
                    document.title = 'Hidoc-信息看板';
                    break;
                case 2000:
                    document.title = 'Hidoc-文集文档';
                    break;
                case 2010:
                    document.title = 'Hidoc-回收站';
                    break;
                case 2020:
                    document.title = 'Hidoc-互动评论';
                    break;
                case 2030:
                    document.title = 'Hidoc-团队信息';
                    break;
                case 3000:
                    document.title = 'Hidoc-素材库';
                    break;
                case 3010:
                    document.title = 'Hidoc-我的文件';
                    break;
                case 7000:
                    document.title = 'Hidoc-代码注释';
                    break;
                case 8000:
                    document.title = 'Hidoc-数据收集器';
                    break;
                case 8020:
                    document.title = 'Hidoc-服务管理';
                    break;
                case 9000:
                    document.title = 'Hidoc-个人信息';
                    break;
                case 9999:
                    document.title = 'Hidoc-实验室';
                    break;
                default:
                    document.title = 'Hidoc-工作台';
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

/* 滚动槽 */
::-webkit-scrollbar {
    width: 3px;
    height: 3px;
    background-color: #d8d8d8;
}

::-webkit-scrollbar-track {
    border-radius: 5px;
}

::-webkit-scrollbar-thumb {
    background-color: #bfc1c4;
}
</style>
