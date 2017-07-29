<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="" method="post" id="pagerForm">
		<div class="searchBar">
			<ul class="searchContent">
				
				<li><label><c:out value="staffwork.id" />:</label> <c:out value="${staffwork.id}" /></li>  
				<li><label><c:out value="staffwork.usercode" />:</label> <c:out value="${staffwork.usercode}" /></li> 
				<li><label><c:out value="staffwork.workbegin" />:</label> <c:out value="${staffwork.workbegin}" /></li> 
				<li><label><c:out value="staffwork.workend" />:</label> <c:out value="${staffwork.workend}" /></li> 
				<li><label><c:out value="staffwork.company" />:</label> <c:out value="${staffwork.company}" /></li> 
				<li><label><c:out value="staffwork.industry" />:</label> <c:out value="${staffwork.industry}" /></li> 
				<li><label><c:out value="staffwork.companyscale" />:</label> <c:out value="${staffwork.companyscale}" /></li> 
				<li><label><c:out value="staffwork.companynature" />:</label> <c:out value="${staffwork.companynature}" /></li> 
				<li><label><c:out value="staffwork.department" />:</label> <c:out value="${staffwork.department}" /></li> 
				<li><label><c:out value="staffwork.position" />:</label> <c:out value="${staffwork.position}" /></li> 
				<li><label><c:out value="staffwork.workdesc" />:</label> <c:out value="${staffwork.workdesc}" /></li> 
				<li><label><c:out value="staffwork.lastmodifydate" />:</label> <c:out value="${staffwork.lastmodifydate}" /></li> 
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
<title><c:out value="staffwork.view.title" /></title>
<link href="<c:out value='${STYLE_PATH}'/>/css/am.css" type="text/css" rel="stylesheet">

<link href="<c:out value='${STYLE_PATH}'/>/css/messages.css" type="text/css" rel="stylesheet">

</head>

<body>
	<p class="ctitle">
		<c:out value="staffwork.view.title" />
	</p>

	<%@ include file="/page/common/messages.jsp"%>

	<html:button styleClass="btn" onclick="window.history.back()" property="none">
		<bean:message key="opt.btn.back" />
	</html:button>
	<p>
	<table width="200" border="0" cellpadding="1" cellspacing="1">
		
		<tr>
			<td class="TDTITLE"><c:out value="staffwork.id" /></td>
			<td align="left"><c:out value="${staffwork.id}" /></td>
		</tr>
		 
		<tr>
			<td class="TDTITLE"><c:out value="staffwork.usercode" /></td>
			<td align="left"><c:out value="${staffwork.usercode}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffwork.workbegin" /></td>
			<td align="left"><c:out value="${staffwork.workbegin}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffwork.workend" /></td>
			<td align="left"><c:out value="${staffwork.workend}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffwork.company" /></td>
			<td align="left"><c:out value="${staffwork.company}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffwork.industry" /></td>
			<td align="left"><c:out value="${staffwork.industry}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffwork.companyscale" /></td>
			<td align="left"><c:out value="${staffwork.companyscale}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffwork.companynature" /></td>
			<td align="left"><c:out value="${staffwork.companynature}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffwork.department" /></td>
			<td align="left"><c:out value="${staffwork.department}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffwork.position" /></td>
			<td align="left"><c:out value="${staffwork.position}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffwork.workdesc" /></td>
			<td align="left"><c:out value="${staffwork.workdesc}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffwork.lastmodifydate" /></td>
			<td align="left"><c:out value="${staffwork.lastmodifydate}" /></td>
		</tr>
		
	</table>

	

</body>
</html> --%>
