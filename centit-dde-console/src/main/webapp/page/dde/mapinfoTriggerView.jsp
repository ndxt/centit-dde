<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="" method="post" id="pagerForm">
		<div class="searchBar">
			<ul class="searchContent">
				
				<li><label><c:out value="mapInfoTrigger.triggerId" />:</label> <c:out value="${mapInfoTrigger.triggerId}" /></li>
				<li><label><c:out value="mapInfoTrigger.mapInfoId" />:</label> <c:out value="${mapInfoTrigger.mapInfoId}" /></li>
				<li><label><c:out value="mapInfoTrigger.triggerSql" />:</label> <c:out value="${mapInfoTrigger.triggerSql}" /></li>
				<li><label><c:out value="mapInfoTrigger.triggerDesc" />:</label> <c:out value="${mapInfoTrigger.triggerDesc}" /></li>
				<li><label><c:out value="mapInfoTrigger.triggerType" />:</label> <c:out value="${mapInfoTrigger.triggerType}" /></li>
				<li><label><c:out value="mapInfoTrigger.triggerTime" />:</label> <c:out value="${mapInfoTrigger.triggerTime}" /></li>
				<li><label><c:out value="mapInfoTrigger.triggerDatabase" />:</label> <c:out value="${mapInfoTrigger.triggerDatabase}" /></li>
				<li><label><c:out value="mapInfoTrigger.tiggerOrder" />:</label> <c:out value="${mapInfoTrigger.tiggerOrder}" /></li>
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
<title><c:out value="mapInfoTrigger.view.title" /></title>
<link href="<c:out value='${STYLE_PATH}'/>/css/am.css" type="text/css" rel="stylesheet">

<link href="<c:out value='${STYLE_PATH}'/>/css/messages.css" type="text/css" rel="stylesheet">

</head>

<body>
	<p class="ctitle">
		<c:out value="mapInfoTrigger.view.title" />
	</p>

	<%@ include file="/page/common/messages.jsp"%>

	<html:button styleClass="btn" onclick="window.history.back()" property="none">
		<bean:message key="opt.btn.back" />
	</html:button>
	<p>
	<table width="200" border="0" cellpadding="1" cellspacing="1">
		
		<tr>
			<td class="TDTITLE"><c:out value="mapInfoTrigger.triggerId" /></td>
			<td align="left"><c:out value="${mapInfoTrigger.triggerId}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="mapInfoTrigger.mapInfoId" /></td>
			<td align="left"><c:out value="${mapInfoTrigger.mapInfoId}" /></td>
		</tr>
		 
		<tr>
			<td class="TDTITLE"><c:out value="mapInfoTrigger.triggerSql" /></td>
			<td align="left"><c:out value="${mapInfoTrigger.triggerSql}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="mapInfoTrigger.triggerDesc" /></td>
			<td align="left"><c:out value="${mapInfoTrigger.triggerDesc}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="mapInfoTrigger.triggerType" /></td>
			<td align="left"><c:out value="${mapInfoTrigger.triggerType}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="mapInfoTrigger.triggerTime" /></td>
			<td align="left"><c:out value="${mapInfoTrigger.triggerTime}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="mapInfoTrigger.triggerDatabase" /></td>
			<td align="left"><c:out value="${mapInfoTrigger.triggerDatabase}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="mapInfoTrigger.tiggerOrder" /></td>
			<td align="left"><c:out value="${mapInfoTrigger.tiggerOrder}" /></td>
		</tr>
		
	</table>

	

</body>
</html> --%>
