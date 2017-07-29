<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<style type="text/css">
<!--
.ccc{
	background-image: url(<%=request.getContextPath()%>/themes/css/images/beijing.jpg);
}

-->
</style>
<div id="dashboard" class="page unitBox ccc"></div>
<script type="text/javascript">
$(function(){
	$("#dashboard").height($("#firstPage").height());
});
</script>