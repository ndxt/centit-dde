<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/css.jsp"%> 
<html>
<head>
<meta name="decorator" content='${LAYOUT}' />
<title>流程业务变量表</title>
<% 
String sname=request.getParameter("s_OPTID");
System.out.print(sname);
%>
</head>

<body>

<fieldset><legend>
&nbsp;&nbsp;查询条件&nbsp;&nbsp; </legend> <s:form action="optVar"
	style="margin-top:0;margin-bottom:5" theme="simple">
	<input  type="hidden" name="s_OPTID" value="<%=sname %>" />
	<table cellpadding="0" cellspacing="0" align="center">
		<tr height="22">
			<td>&nbsp;&nbsp;变量描述: <s:textfield name="s_VARDESC" value="%{#parameters['s_VARDESC']}"/>

			<td><s:checkbox label="包含禁用" name="s_isAll"
				value="#parameters['s_isAll']" fieldValue="true" />包含禁用</td>
			<td><s:submit method="list" cssClass="btn" value="查询"></s:submit>
			<s:submit method="built"  cssClass="btn" value="新增" ></s:submit> <input
				type="button" value="返回" class="btn" onclick="window.history.go(-1)" />
			</td>
		</tr>
	</table>
</s:form></fieldset>
<ec:table action="optVar!list.do" items="objList" var="object"
	imagePath="${sessionScope.STYLE_PATH}/images/table/*.gif"
	rowsDisplayed="15" filterRowsCallback="limit"
	retrieveRowsCallback="limit" sortRowsCallback="limit">
	<ec:row>
		<ec:column property="varname" title="变量名称" style="text-align:center"></ec:column>
		<ec:column property="vardesc" title="变量描述" style="text-align:center"></ec:column>
		<ec:column property="vartype" title="变量类型" style="text-align:center"></ec:column>
		<ec:column property="defaultvalue" title="变量初值"
			style="text-align:center"></ec:column>
		<ec:column property="isvalid" title="状态" style="text-align:center"></ec:column>
		<ec:column property="varOpt" title="操作" sortable="false"
			style="text-align:center">
			<a href='optVar!edit.do?varname=${object.varname}&optid=${object.optid}'>编辑</a>
			<c:if test="${object.isvalid eq 'F' }">
				<a href='optVar!renew.do?varname=${object.varname}&optid=${object.optid}'>启用</a>
			</c:if>
			<c:if test="${object.isvalid eq 'T' }">

				<a href='optVar!disable.do?varname=${object.varname}&optid=${object.optid}' onclick='return confirm("是否禁用该变量?");'>禁用</a>
			</c:if>
		</ec:column>
	</ec:row>
</ec:table>
</body>
</html>