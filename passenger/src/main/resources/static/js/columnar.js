//柱形图模板
var domLong = document.getElementById("second");
var myChartSecond = echarts.init(domLong);
var app = {};
option = null;
option = {
    tooltip : {
        trigger: 'axis',
        axisPointer : {      // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'    // 默认为直线，可选为：'line' | 'shadow'
        }
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis : [
        {
            type : 'category',
            data : [],
            axisTick: {
                alignWithLabel: true
            }
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
        {
            name:'直接访问',
            type:'bar',
            itemStyle: {
                normal: {
                    //好，这里就是重头戏了，定义一个list，然后根据所以取得不同的值，这样就实现了，
                    color: function(params) {
                        // build a color map as your need.
                        var colorList = [
                            '#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',
                            '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
                            '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'
                        ];
                        return colorList[params.dataIndex]
                    },
                    //以下为是否显示，显示位置和显示格式的设置了
                    label: {
                        show: true,
                        position: 'top',
                        formatter: '{b}\n{c}'
                    }
                }
            },
            barWidth: '30',
            data:[]
        }
    ]
};

if (option && typeof option === "object") {
    myChartSecond.setOption(option, true);
}
//给柱形图赋值
var year = $("#year-search").val();