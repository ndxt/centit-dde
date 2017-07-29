<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/css.jsp"%> 


<html>
<head><meta name="decorator" content='${LAYOUT}'/>
	<title>变量信息</title>
	<sj:head/>
	<s:include value="/page/common/formValidator.jsp"></s:include>

</head>
<body>
<p class="ctitle">变量信息</p>

<s:form action="optVar" id="from1">
	<s:submit method="save" cssClass="btn" value="保存"></s:submit>
	<input type="button"  value="返回" Class="btn"  onclick="window.history.back()"/>
<table width="200" border="0" cellpadding="1" cellspacing="1">
	<tr>
		<s:hidden name="optid" value="%{optid}"></s:hidden>
		<c:if test="${ empty varname}">
		<td class="TDTITLE">变量名称*</td>
		<td><s:textfield name="varname" /> </td>
		</c:if>
		<c:if test="${not empty varname}">
		<td class="TDTITLE">变量名称×</td>
		<td><s:textfield name="varname" value="%{varname}" readonly="true" /> </td>
		</c:if>
		
	</tr>
	<tr>
		<td class="TDTITLE">变量描述</td>
		<td><s:textfield name="vardesc" value="%{vardesc}"  /> </td>
	</tr>
	
	<tr>
		<td class="TDTITLE">变量类型</td>
		<td>
		
		<s:select name="vartype"  list="#{'N':'数据','S':'字符串','D':'日期' }" value="%{vartype}"></s:select>
		
		</td>
	</tr>
	<tr>
		<td class="TDTITLE">变量初值</td>
		<td><s:textfield name="defaultvalue" value="%{defaultvalue}" /> </td>
	</tr>
	
</table>
</s:form>
</body>
</html>	