<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<link href="${pageContext.request.contextPath}/themes/css/am.css" rel="stylesheet" type="text/css" />
<% String path = request.getContextPath(); %>


<html>
<head>
<script type="text/javascript" src="<s:url value="/scripts/jquery-1.6.min.js"/>"></script>
<script type="text/javascript" src="<s:url value="/scripts/Calendar/calendar.js"/>"></script>
<script type="text/javascript" src="<s:url value="/scripts/easyui/jquery.easyui.min.js"/>"></script>
<link type="text/css" href="<s:url value='/scripts/Calendar/css/calendar.css'/>" rel="stylesheet"/>
<link type="text/css" href="<s:url value='/scripts/easyui/themes/default/easyui.css'/>" rel="stylesheet"/>
<title>日历</title>
</head>
<script type="text/javascript">
var path='<%=path%>';
$(document).ready(function(){	
	drowCalendar();
	
});
function isdesc(itom){
	//alert(itom.value);
	if(itom.value=="C")
		$("#bydaytype").hide();	
	else{
		$("#bydaytype").show();
	}
}

</script>
<body>

<div id="calendar">
<a href="calendar!test.do">测试</a>
<div><select id="year" onchange="getYear(this);"></select>年 <select id="month" onchange="getMonth(this);"></select>月</div>
<table id="calendarTable" >

	<tr>
	 	 <th>日	</th>
		 <th>一</th>
		 <th>二</th>
		 <th>三</th>
		 <th>四</th>
		 <th>五</th>
		 <th>六</th>	
	</tr>
</table>
</div>

<div id="editDate" class="easyui-dialog" title="编辑日期" closed="true" style="width:500px;height:350px;left:225px;top:50px;padding:10px"
			toolbar="#dlg-toolbar" buttons="#dlg-buttons" resizable="true">
<s:form id="form1" action="/app/calendar!savedate.do" >
	<table>
	<tr>
	<td class="TDTITLE">日期:</td>
	<td align="left">
	<s:textfield  id="workday" name="workday"  readonly="true"></s:textfield>	</td>
	</tr>
	<tr>
	<td class="TDTITLE">类型:</td>
	<td id="type" align="left">	
		<input type='radio' name='daytype' value = 'C'>正常
		<input type='radio'	id="typeB" name='daytype' value = 'B'><span id="typeBLable">加班</span>
		<input type='radio' id="typeA" name='daytype' value = 'A'><span id="typeALable">放假</span>
	</td>
	</tr>
	<tr >
	<td class="TDTITLE">说明:</td>
	<td align="left">
	<s:textarea name="workdaydesc"></s:textarea>
	</td>
	</tr>
	
	</table>
<div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" onclick="ajaxSave(document.all.form1)">保存</a>
		<a href="#" class="easyui-linkbutton" onclick="javascript:$('#editDate').dialog('close')">取消</a>
	</div>
</s:form>			
</div>

</body>
</html>

