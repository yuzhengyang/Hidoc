<template>
    <el-row :gutter="12">
        <el-col :span="6">
            <el-card shadow="hover">文集数量： {{ this.collectedCount }}</el-card>
        </el-col>
        <el-col :span="6">
            <el-card shadow="hover">文档数量： {{ this.docCount }}</el-card>
        </el-col>
        <el-col :span="6">
            <el-card shadow="hover">文件数量： {{ this.cursorCount }}</el-card>
        </el-col>
        <el-col :span="6">
            <el-card shadow="hover">阅读数量： {{ this.readCount }}</el-card>
        </el-col>

        <!-- 月度文章发布统计图
        发布动态 -->
    </el-row>
    <el-row style="margin-top: 20px; margin-bottom: 20px">
        <el-col :span="24">
            <div id="myChart" :style="{ width: '100%', height: '300px' }"></div>
        </el-col>
    </el-row>

    <el-row>
        <el-col :span="2"></el-col>
        <el-col :span="20"><h3>我的文档阅读记录</h3></el-col>
    </el-row>
    <el-row>
        <el-col :span="2"></el-col>
        <el-col :span="20">
            <el-table :data="myDocReadLog" style="width: 100%" height="360" stripe>
                <el-table-column fixed prop="readTime" label="阅读时间" width="180" />
                <el-table-column prop="collectedName" label="文集" width="200" />
                <el-table-column prop="docTitle" label="文档" width="300" />
                <el-table-column prop="createUser" label="创建" width="120" />
                <el-table-column prop="ownerUser" label="管理" width="120" />
                <el-table-column prop="readUser" label="读者" width="120" />
                <el-table-column prop="ip" label="访问IP" width="120" />
            </el-table>
        </el-col>
    </el-row>
</template>

<script>
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
export default {
    data() {
        return {
            collectedCount: 0,
            docCount: 0,
            cursorCount: 0,
            readCount: 0,
            trendChartData: {
                dateList: [],
                collectedCountList: [],
                cursorCountList: [],
                docCountList: [],
                docUpdateCountList: [],
                readCountList: []
            },
            myDocReadLog: []
        };
    },
    mounted() {
        console.log('DashBoard mounted ~');
        this.loadUserBoard();
        this.initChart();
        //this.$root => app
        console.log(this.echarts);

        this.loadMyDocReadLog();
    },
    created(){
        console.log('DashBoard created ~');
    },
    methods: {
        loadUserBoard() {
            request({
                url: '/user/currentUserBoard',
                method: 'post'
            }).then(res => {
                if (res.code == 0) {
                    this.collectedCount = res.meta.collectedCount;
                    this.docCount = res.meta.docCount;
                    this.cursorCount = res.meta.cursorCount;
                    this.readCount = res.meta.readCount;
                    this.trendChartData = res.meta.trendChartData;

                    // this.$nextTick(() => {
                    this.initChart();
                    // });
                }
            });
        },
        loadMyDocReadLog() {
            request({
                url: '/dashboard/myDocReadLog',
                method: 'post',
                data: {
                    docId: ''
                }
            }).then(res => {
                if (res.code == 0 && res.data.length > 0) {
                    this.myDocReadLog = res.data;
                }
            });
        },
        initChart() {
            let myChart = this.$echarts.init(document.getElementById('myChart'));
            // 绘制图表
            myChart.setOption({
                title: { text: '　　趋势　　' },
                tooltip: {},
                legend: {},
                xAxis: {
                    data: this.trendChartData.dateList
                },
                yAxis: {},
                series: [
                    {
                        name: '文集数量',
                        type: 'line',
                        data: this.trendChartData.collectedCountList
                    },
                    {
                        name: '文档数量',
                        type: 'line',
                        data: this.trendChartData.docCountList
                    },
                    {
                        name: '文档编辑',
                        type: 'line',
                        data: this.trendChartData.docUpdateCountList
                    },
                    {
                        name: '文件数量',
                        type: 'line',
                        data: this.trendChartData.cursorCountList
                    },
                    {
                        name: '阅读数量',
                        type: 'line',
                        data: this.trendChartData.readCountList
                    }
                ]
            });
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
