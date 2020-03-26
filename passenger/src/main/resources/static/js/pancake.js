//饼图模板
var dom = document.getElementById("first");
var myChart = echarts.init(dom);
var app = {};
option = null;
option = {
    /* title : {
         text: '用户位置记录',
         subtext: '',
         x:'center'
     },*/
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient : 'vertical',
        x : 'left',
        data:[]
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType : {
                show: true,
                type: ['pie', 'funnel'],
                option: {
                    funnel: {
                        x: '25%',
                        width: '50%',
                        funnelAlign: 'left',
                        max: 1548
                    }
                }
            },
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    series : [
        {
            name:'',
            type:'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:[]
        }
    ]
};

if (option && typeof option === "object") {
    myChart.setOption(option, true);
}
//饼图动态赋值
var year = $("#year-search").val();
var mouth = $("#mouth-search").val();