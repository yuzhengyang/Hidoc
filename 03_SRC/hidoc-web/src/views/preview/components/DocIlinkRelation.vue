<template>
    <el-row style="margin-top: 20px; margin-bottom: 20px">
        <el-col :span="24">
            <div id="myChart" :style="{ width: '100%', height: '700px' }"></div>
        </el-col>
    </el-row>
</template>

<script>
import _ from 'lodash';
import request from '../../../utils/request.js';

export default {
    props: {
        data: Object
    },
    setup(props, context) {
        console.log(props);
    },
    methods: {
        ilinkRelation(collectedId) {
            request({
                url: '/openapi/collected/ilinkRelation',
                method: 'post',
                data: {
                    id: collectedId
                }
            }).then(res => {
                if (res.code == 0 && res.meta) {

                    var datas = res.meta.datas;
                    var links = res.meta.links;

                    var categories = [];
                    var types = ['未作答', '较好', '一般', '较差'];

                    for (var i = 0; i < types.length; i++) {
                        categories[i] = {
                            name: types[i]
                        };
                    }
                    let myChart = this.$echarts.init(document.getElementById('myChart'));
                    // 绘制图表
                    var option = {
                        animationDurationUpdate: 1000, //数据更新动画的时长。
                        animationEasingUpdate: 'quinticInOut', //数据更新动画的缓动效果。
                        color: ['#008000', '#cc6633', '#c00', '#ccc'],
                        grid: {
                            top: 0,
                            bottom: 0,
                            left: 0,
                            right: 0
                        },
                        series: [
                            {
                                type: 'graph', // 类型:关系图
                                layout: 'force', // 图的布局，类型为力导图
                                focusNodeAdjacency: true, // 当鼠标移动到节点上，突出显示节点以及节点的边和邻接节点
                                draggable: true, // 指示节点是否可以拖动
                                symbolSize: 14, // 调整节点的大小
                                roam: 'scale', // 是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移,可以设置成 'scale' 或者 'move'。设置成 true 为都开启
                                edgeSymbol: ['circle', 'arrow'], // 边两端的标记类型，可以是一个数组分别指定两端，也可以是单个统一指定。默认不显示标记，常见的可以设置为箭头，如下：edgeSymbol: ['circle', 'arrow']
                                edgeSymbolSize: [2, 10], // 边两端的标记大小，可以是一个数组分别指定两端，也可以是单个统一指定。
                                edgeLabel: {
                                    normal: {
                                        show: false,
                                        formatter: function (x) {
                                            return x.data.name;
                                        },
                                        textStyle: {
                                            fontSize: 14,
                                            color: '#666666'
                                        }
                                    }
                                },
                                force: {
                                    //力引导图基本配置
                                    layoutAnimation: true,
                                    // xAxisIndex : 0, //x轴坐标 有多种坐标系轴坐标选项
                                    // yAxisIndex : 0, //y轴坐标
                                    gravity: 0.02, //节点受到的向中心的引力因子。该值越大节点越往中心点靠拢。
                                    edgeLength: 60, //边的两个节点之间的距离，这个距离也会受 repulsion。[10, 50] 。值越小则长度越长
                                    repulsion: 500 //节点之间的斥力因子。支持数组表达斥力范围，值越大斥力越大。
                                    // repulsion: 3000, //节点之间的斥力因子。支持数组表达斥力范围，值越大斥力越大。
                                    // edgeLength: 80 //边的两个节点之间的距离，这个距离也会受 repulsion。[10, 50] 。值越小则长度越长
                                },
                                lineStyle: {
                                    normal: {
                                        width: 2,
                                        // color: '#4b565b',
                                        color: 'target', // 决定边的颜色是与起点相同还是与终点相同
                                        curveness: 0.2,
                                        type: 'solid' // 'dotted'点型虚线 'solid'实线 'dashed'线性虚线
                                    }
                                },
                                label: {
                                    show: true,
                                    position: 'top',
                                    formatter: '{b}',
                                    textStyle: {
                                        fontSize: 14,
                                        fontWeight: 400,
                                        color: '#666'
                                    }
                                },
                                // 数据
                                data: datas,
                                links: links,
                                categories: categories
                            }
                        ]
                    };
                    myChart.setOption(option);
                    window.addEventListener('resize', function () {
                        myChart.resize();
                    });
                    var that = this;
                    myChart.on('click', function (param) {
                        if (param.dataType === 'node') {
                            // 点击节点
                            console.log('点击节点', param);
                            let routeData = that.$router.resolve({
                                name: 'collected',
                                params: { collectedId: param.data.collectedId, docId: param.data.docId }
                            });
                            window.open(routeData.path, '_blank');

                            // that.$router.push({ name: 'collected', params: { collectedId: param.data.collectedId, docId: param.data.docId } });
                        } else {
                            // 点击边
                            console.log('点击了边', param);
                        }
                    });
                }
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
