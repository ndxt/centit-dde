<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<%@ include file="/page/common/css.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>保障性安居工程建设目标任务进展情况</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    
    <!--<link rel="stylesheet" type="text/css" href="./styles.css">-->
<link href="styles/default/css/normal.css" rel="stylesheet" type="text/css">
  </head>
  
  <body>
  <div id="searchArea" style="padding-left:20px;">
  	年份：
  	<select>
  		<option>2012上半年</option>
  	</select>
  	&nbsp;&nbsp;&nbsp;&nbsp;
  	统计区域：
  	<select id="area">
  		<option value="320000">江苏省</option>
  		<option value="320100">&nbsp;&nbsp;&nbsp;&nbsp;南京市</option>
  		<option value="320200">&nbsp;&nbsp;&nbsp;&nbsp;无锡市</option>
  		<option value="320300">&nbsp;&nbsp;&nbsp;&nbsp;徐州市</option>
  		<option value="320400">&nbsp;&nbsp;&nbsp;&nbsp;常州市</option>
  		<option value="320500">&nbsp;&nbsp;&nbsp;&nbsp;苏州市</option>
  		<option value="320600">&nbsp;&nbsp;&nbsp;&nbsp;南通市</option>
  		<option value="320700">&nbsp;&nbsp;&nbsp;&nbsp;连云港市</option>
  		<option value="320800">&nbsp;&nbsp;&nbsp;&nbsp;淮安市</option>
  		<option value="320900">&nbsp;&nbsp;&nbsp;&nbsp;盐城市</option>
  		<option value="321000">&nbsp;&nbsp;&nbsp;&nbsp;扬州市</option>
  		<option value="321100">&nbsp;&nbsp;&nbsp;&nbsp;镇江市</option>
  		<option value="321200">&nbsp;&nbsp;&nbsp;&nbsp;泰州市</option>
  		<option value="321300">&nbsp;&nbsp;&nbsp;&nbsp;宿迁市</option>
  	</select>
  	&nbsp;&nbsp;&nbsp;&nbsp;
  	<!-- 类型： -->
  	<select id="func" style="display:none;">
  		<option value="0">合计</option>
  		<option value="1">公共租赁住房（含廉租住房）</option>
  		<option value="2">经济适用住房</option>
  		<option value="3">限价商品住房</option>
  		<option value="4">棚屋区危旧房改造</option>
  	</select>
  	&nbsp;&nbsp;&nbsp;&nbsp;
  	<input type="radio" name="type" checked="checked" value="B"></input>开工情况
  	<input type="radio" name="type" value="F"></input>竣工情况
  </div>
  	<div id="container"></div>
  </body>
  
  <script type="text/javascript">
  $(function() {
	  var height = $('body').height()-$('#searchArea').height();
	  $('#container').height(height-10);
	  
	  $('#func').bind('change', function() {
		  chart.setOptions(op);
	  });
  });
  
  
  
  var chart=null;
  
  var data = {
		  '320000':{
			  '0':{
				 'B':[[315000, 6905], [315000, 59905], [315000, 89905], [315000, 119905], [315000, 189905], [315000, 229905]],
				 'F':[[130000, 5495], [130000, 10495], [130000, 23495], [130000, 42495], [130000, 62495], [130000, 82495],]
			  },
			  '1':{
					 'B':[[105000, 5439], [105000, 8439], [105000, 19439], [105000, 59439], [105000, 39439], [105000, 59439]],
					 'F':[[3700, 254], [3700, 454], [3700, 654], [3700, 854], [3700, 1054], [3700, 1454]]
			  }
		  }
  };
  
  var op = {
	  		chart: {
	  			renderTo: 'container',
	  			type: 'area'
	  		},
	  		title: {
	  			text: '保障性安居工程建设目标任务进展情况'
	  		},
	  		subtitle: {
	  			text: '2012上半年份江苏省 '
	  		},
	  		xAxis: {
	  			categories: ['一月', '二月', '三月', '四月', '五月', '六月'],
	  			tickmarkPlacement: 'on',
	  			title: {
	  				enabled: false
	  			}
	  		},
	  		yAxis: {
	  			title: {
	  				text: '实际开工数量'
	  			}
	  		},
	  		tooltip: {
				enabled: true,
				formatter: function() {
					return ''+
					this.x + ': ' + this.series.name + Highcharts.numberFormat(this.percentage, 1) +'% ('+
					Highcharts.numberFormat(this.y, 0, ',') +')';
				}
			},
	  		plotOptions: {
	  			area: {
	  				animation:false,
					stacking: 'percent',
					lineColor: '#ffffff',
					lineWidth: 1,
					marker: {
						lineWidth: 1,
						lineColor: '#ffffff'
					}
				}
	  		},
	  		series: [{
	  			name: '公共租赁住房（含廉租住房）',
	  			data: [4000, 4000, 4000, 4000, 4000, 4000]
	  		}, {
	  			name: '经济适用住房',
	  			data: [366, 2598, 3053, 3191, 3365, 5600]
	  		}, {
	  			name: '限价商品住房',
	  			data: [366, 2598, 3053, 3191, 3365, 5600]
	  		}, {
	  			name: '棚屋区危旧房改造',
	  			data: [366, 2598, 3053, 3191, 3365, 5600]
	  		}]
	  	};
	  	
  $(document).ready(function() {
  	chart = new Highcharts.Chart(op);
  });
  </script>
</html>
