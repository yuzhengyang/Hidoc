<template>
    <el-row style="padding: 50px">
        <el-col :span="16">展示系统实时状态和检查状态，了解系统健康情况</el-col>
    </el-row>
    <el-tabs tab-position="left">
        <el-tab-pane label="系统状态">
            <el-row style="margin-top: 20px; margin-bottom: 20px">
                <el-col :span="24">
                    <div id="elapsedTimeChart" :style="{ width: '100%', height: '600px' }"></div>
                </el-col>
            </el-row>
        </el-tab-pane>
        <el-tab-pane label="访问状态">Config</el-tab-pane>
        <el-tab-pane label="资源状态">Role</el-tab-pane>
        <el-tab-pane label="文件检查">Role</el-tab-pane>
    </el-tabs>
</template>

<script>
import _ from 'lodash';
import { ElMessage } from 'element-plus';
import request from '../../../utils/request.js';
export default {
    data() {
        return {
            form: {
                name: '',
                email: ''
            },
            user: {},
            fileConf: {},
            tableData: []
        };
    },
    mounted() {
        this.loadElapsedTime();
    },
    methods: {
        loadElapsedTime() {
            request({
                url: '/sysstatusrpt/elapsedTime',
                method: 'post',
                data: { mode: 'mode', date: 'date' }
            }).then(res => {
                if (res.code == 0) {
                    if (res.data) {
                        let echartData = new Array();
                        _(res.data).forEach(function (value) {
                            echartData.push([value.begin_time, value.elapsed_time]);
                        });
                        // this.$nextTick(() => {
                        this.initChart(echartData);
                        // });
                    }
                }
            });
        },
        initChart(echartData) {
            let myChart = this.$echarts.init(document.getElementById('elapsedTimeChart'));

            let option = {
                xAxis: {
                    type: 'time',
                    name: '时间轴'
                },
                yAxis: {
                    type: 'value',
                    name: '响应时间'
                },
                series: [
                    {
                        name: '',
                        data: echartData,
                        type: 'scatter',
                        symbolSize: 10
                    }
                ]
            };

            // 绘制图表
            myChart.setOption(option);
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
