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
    <el-row>
        <el-col :span="24">
            <div id="myChart" :style="{ width: '300px', height: '300px' }"></div>
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
            readCount: 0
        };
    },
    mounted() {
        this.loadUserBoard();
        this.initChart();
        //this.$root => app
        console.log(this.echarts);
        let myChart = this.$echarts.init(document.getElementById('myChart'));
        // 绘制图表
        myChart.setOption({
            title: { text: '总用户量' },
            tooltip: {},
            xAxis: {
                data: ['12-3', '12-4', '12-5', '12-6', '12-7', '12-8']
            },
            yAxis: {},
            series: [
                {
                    name: '用户量',
                    type: 'line',
                    data: [5, 20, 36, 10, 10, 20]
                }
            ]
        });
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
                }
            });
        },
        initChart() {}
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
