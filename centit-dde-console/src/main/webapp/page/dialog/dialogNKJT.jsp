<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div id="listTable" style="width: 100%; height: 100%; background-color:white; padding:20px 0 0 0; text-align:center;">

	<div id="preAlarm-xz-kgjg">
		<h1 style="font-size:20px; margin-bottom:10px;"> 江苏省农垦集团市项目工程开工竣工预报警信息  </h1>
		<jsp:include page="./nkjt/preAlarm-xz-kgjg.jsp"></jsp:include>
	</div>
	
	<div id="preAlarm-xz-kgjg1">
		<h1 style="font-size:20px; margin-bottom:10px;"> 江苏省农垦集团市项目工程开工竣工预报警信息  </h1>
		<jsp:include page="./nkjt/preAlarm-xz-kgjg1.jsp"></jsp:include>
	</div>
	
	<div id="preAlarm-xz-kgjg2">
		<h1 style="font-size:20px; margin-bottom:10px;"> 江苏省农垦集团市项目工程开工竣工预报警信息  </h1>
		<jsp:include page="./nkjt/preAlarm-xz-kgjg2.jsp"></jsp:include>
	</div>
	
	<div id="preAlarm-xz-zjcj">
		<h1 style="font-size:20px; margin-bottom:10px;"> 江苏省农垦集团市项目工程资金筹集预报警信息  </h1>
		<jsp:include page="./nkjt/preAlarm-xz-zjcj.jsp"></jsp:include>
	</div>
</div>

<script>
	var datas = [
		'#preAlarm-xz-kgjg', '#preAlarm-xz-kgjg1', '#preAlarm-xz-kgjg2',
		'#preAlarm-xz-zjcj'
	];

	$(function() {
		$.Slideshow.init('#listTable', datas, {type:'dialog'});
	});
</script>













