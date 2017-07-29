<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="" method="post" id="pagerForm">
		<div class="searchBar">
			<ul class="searchContent">
				
				<li><label><c:out value="groupuser.no" />:</label> <c:out value="${groupuser.no}" /></li>  
				<li><label><c:out value="groupuser.groupid" />:</label> <c:out value="${groupuser.groupid}" /></li> 
				<li><label><c:out value="groupuser.inUsercode" />:</label> <c:out value="${groupuser.inUsercode}" /></li> 
			</ul>

			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">检索</button>
							</div>
						</div></li>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<!-- 参数 navTabId 根据实际情况填写 -->
								<button type="button" onclick="javascript:navTabAjaxDone({'statusCode' : 200, 'callbackType' : 'closeCurrent', 'navTabId' : ''});">返回</button>
							</div>
						</div></li>
				</ul>
			</div>
		</div>
	</form>
</div>

<div class="pageContent">
	<div class="panelBar">
	</div>

	
</div>



<%-- 
<html>
<head>
<title><c:out value="groupuser.view.title" /></title>
<link href="<c:out value='${STYLE_PATH}'/>/css/am.css" type="text/css" rel="stylesheet">

<link href="<c:out value='${STYLE_PATH}'/>/css/messages.css" type="text/css" rel="stylesheet">

</head>

<body>
	<p class="ctitle">
		<c:out value="groupuser.view.title" />
	</p>

	<%@ include file="/page/common/messages.jsp"%>

	<html:button styleClass="btn" onclick="window.history.back()" property="none">
		<bean:message key="opt.btn.back" />
	</html:button>
	<p>
	<table width="200" border="0" cellpadding="1" cellspacing="1">
		
		<tr>
			<td class="TDTITLE"><c:out value="groupuser.no" /></td>
			<td align="left"><c:out value="${groupuser.no}" /></td>
		</tr>
		 
		<tr>
			<td class="TDTITLE"><c:out value="groupuser.groupid" /></td>
			<td align="left"><c:out value="${groupuser.groupid}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="groupuser.inUsercode" /></td>
			<td align="left"><c:out value="${groupuser.inUsercode}" /></td>
		</tr>
		
	</table>

	

</body>
</html> --%>
