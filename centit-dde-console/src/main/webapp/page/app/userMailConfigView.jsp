<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="" method="post" id="pagerForm">
		<div class="searchBar">
			<ul class="searchContent">
				
				<li><label><c:out value="userMailConfig.emailid" />:</label> <c:out value="${userMailConfig.emailid}" /></li>  
				<li><label><c:out value="userMailConfig.usercode" />:</label> <c:out value="${userMailConfig.usercode}" /></li> 
				<li><label><c:out value="userMailConfig.mailaccount" />:</label> <c:out value="${userMailConfig.mailaccount}" /></li> 
				<li><label><c:out value="userMailConfig.mailpassword" />:</label> <c:out value="${userMailConfig.mailpassword}" /></li> 
				<li><label><c:out value="userMailConfig.mailtype" />:</label> <c:out value="${userMailConfig.mailtype}" /></li> 
				<li><label><c:out value="userMailConfig.smtpurl" />:</label> <c:out value="${userMailConfig.smtpurl}" /></li> 
				<li><label><c:out value="userMailConfig.smtpport" />:</label> <c:out value="${userMailConfig.smtpport}" /></li> 
				<li><label><c:out value="userMailConfig.pop3url" />:</label> <c:out value="${userMailConfig.pop3url}" /></li> 
				<li><label><c:out value="userMailConfig.pop3port" />:</label> <c:out value="${userMailConfig.pop3port}" /></li> 
				<li><label><c:out value="userMailConfig.intervaltime" />:</label> <c:out value="${userMailConfig.intervaltime}" /></li> 
				<li><label><c:out value="userMailConfig.retaindays" />:</label> <c:out value="${userMailConfig.retaindays}" /></li> 
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
<title><c:out value="userMailConfig.view.title" /></title>
<link href="<c:out value='${STYLE_PATH}'/>/css/am.css" type="text/css" rel="stylesheet">

<link href="<c:out value='${STYLE_PATH}'/>/css/messages.css" type="text/css" rel="stylesheet">

</head>

<body>
	<p class="ctitle">
		<c:out value="userMailConfig.view.title" />
	</p>

	<%@ include file="/page/common/messages.jsp"%>

	<html:button styleClass="btn" onclick="window.history.back()" property="none">
		<bean:message key="opt.btn.back" />
	</html:button>
	<p>
	<table width="200" border="0" cellpadding="1" cellspacing="1">
		
		<tr>
			<td class="TDTITLE"><c:out value="userMailConfig.emailid" /></td>
			<td align="left"><c:out value="${userMailConfig.emailid}" /></td>
		</tr>
		 
		<tr>
			<td class="TDTITLE"><c:out value="userMailConfig.usercode" /></td>
			<td align="left"><c:out value="${userMailConfig.usercode}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="userMailConfig.mailaccount" /></td>
			<td align="left"><c:out value="${userMailConfig.mailaccount}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="userMailConfig.mailpassword" /></td>
			<td align="left"><c:out value="${userMailConfig.mailpassword}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="userMailConfig.mailtype" /></td>
			<td align="left"><c:out value="${userMailConfig.mailtype}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="userMailConfig.smtpurl" /></td>
			<td align="left"><c:out value="${userMailConfig.smtpurl}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="userMailConfig.smtpport" /></td>
			<td align="left"><c:out value="${userMailConfig.smtpport}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="userMailConfig.pop3url" /></td>
			<td align="left"><c:out value="${userMailConfig.pop3url}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="userMailConfig.pop3port" /></td>
			<td align="left"><c:out value="${userMailConfig.pop3port}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="userMailConfig.intervaltime" /></td>
			<td align="left"><c:out value="${userMailConfig.intervaltime}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="userMailConfig.retaindays" /></td>
			<td align="left"><c:out value="${userMailConfig.retaindays}" /></td>
		</tr>
		
	</table>

	

</body>
</html> --%>
