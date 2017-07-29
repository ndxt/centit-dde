<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<%@ include file="/page/common/css.jsp"%> 
<html>
<head><meta name="decorator" content='${LAYOUT}'/>

<title>报销流程</title>
</head>
<body>
<div style="width: 100px;height: 500px;">
<fieldset>
<legend>报销操作</legend>
<a href="<%=request.getContextPath()%>/app/reimburse!flowInit.do?wfcode=${wfcode}">申请报销</a><br>
<a href="<%=request.getContextPath()%>/app/reimburse!flowInit.do?wfcode=${wfcode}">申请年假</a>
</fieldset>
</div>

</body>
</html>