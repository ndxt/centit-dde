<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="containerD" style="width:100%; height:100%"></div>

<script>
$(function () {
    var chart;
    $(document).ready(function() {
        chart = new Highcharts.Chart({
            chart: {
                renderTo: 'containerD',
                type: 'column'
            },
            title: {
                text: DWZ.title
            },
            xAxis: {
                categories: [
                    '南京市',
                    '无锡市',
                    '徐州市',
                    '常州市',
                    '苏州市',
                    '南通市',
                    '连云港市',
                    '淮安市',
                    '盐城市',
                    '扬州市',
                    '镇江市',
                    '泰州市',
                    '宿迁市',
                    '省教育厅',
                    '省林业局',
                    '省监狱局',
                    '农垦集团'
                ]
            },
            yAxis: {
                min: 0,
                title: {
                    text: null
                }
            },
            legend: {
                layout: 'vertical',
                backgroundColor: '#FFFFFF',
                align: 'left',
                verticalAlign: 'top',
                x: 100,
                y: 70,
                floating: true,
                shadow: true
            },
            tooltip: {
                formatter: function() {
                    return ''+
                        this.x +': '+ this.y ;
                }
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                }
            },
                series: [{
                name: '任务',
                data: [836, 715, 1064, 1292, 1440, 1760, 1356, 1485, 2164, 1941, 956, 544, 836, 715, 1064, 1292]
    
            }, {
                name: '完成',
                data: [836, 788, 985, 934, 1060, 845, 1050, 1043, 912, 835, 1066, 923, 836, 788, 985, 934, 1060]
    
            }]
        });
    });
    
});
</script>