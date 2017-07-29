<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="" method="post" id="pagerForm">
		<div class="searchBar">
			<ul class="searchContent">
				
				<li><label><c:out value="staffeducation.id" />:</label> <c:out value="${staffeducation.id}" /></li>  
				<li><label><c:out value="staffeducation.usercode" />:</label> <c:out value="${staffeducation.usercode}" /></li> 
				<li><label><c:out value="staffeducation.educatebegin" />:</label> <c:out value="${staffeducation.educatebegin}" /></li> 
				<li><label><c:out value="staffeducation.educateend" />:</label> <c:out value="${staffeducation.educateend}" /></li> 
				<li><label><c:out value="staffeducation.schoolname" />:</label> <c:out value="${staffeducation.schoolname}" /></li> 
				<li><label><c:out value="staffeducation.speciality" />:</label> <c:out value="${staffeducation.speciality}" /></li> 
				<li><label><c:out value="staffeducation.edubackground" />:</label> <c:out value="${staffeducation.edubackground}" /></li> 
				<li><label><c:out value="staffeducation.certifier" />:</label> <c:out value="${staffeducation.certifier}" /></li> 
				<li><label><c:out value="staffeducation.specialitydesc" />:</label> <c:out value="${staffeducation.specialitydesc}" /></li> 
				<li><label><c:out value="staffeducation.isabroad" />:</label> <c:out value="${staffeducation.isabroad}" /></li> 
				<li><label><c:out value="staffeducation.lastmodifydate" />:</label> <c:out value="${staffeducation.lastmodifydate}" /></li> 
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
<title><c:out value="staffeducation.view.title" /></title>
<link href="<c:out value='${STYLE_PATH}'/>/css/am.css" type="text/css" rel="stylesheet">

<link href="<c:out value='${STYLE_PATH}'/>/css/messages.css" type="text/css" rel="stylesheet">

</head>

<body>
	<p class="ctitle">
		<c:out value="staffeducation.view.title" />
	</p>

	<%@ include file="/page/common/messages.jsp"%>

	<html:button styleClass="btn" onclick="window.history.back()" property="none">
		<bean:message key="opt.btn.back" />
	</html:button>
	<p>
	<table width="200" border="0" cellpadding="1" cellspacing="1">
		
		<tr>
			<td class="TDTITLE"><c:out value="staffeducation.id" /></td>
			<td align="left"><c:out value="${staffeducation.id}" /></td>
		</tr>
		 
		<tr>
			<td class="TDTITLE"><c:out value="staffeducation.usercode" /></td>
			<td align="left"><c:out value="${staffeducation.usercode}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffeducation.educatebegin" /></td>
			<td align="left"><c:out value="${staffeducation.educatebegin}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffeducation.educateend" /></td>
			<td align="left"><c:out value="${staffeducation.educateend}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffeducation.schoolname" /></td>
			<td align="left"><c:out value="${staffeducation.schoolname}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffeducation.speciality" /></td>
			<td align="left"><c:out value="${staffeducation.speciality}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffeducation.edubackground" /></td>
			<td align="left"><c:out value="${staffeducation.edubackground}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffeducation.certifier" /></td>
			<td align="left"><c:out value="${staffeducation.certifier}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffeducation.specialitydesc" /></td>
			<td align="left"><c:out value="${staffeducation.specialitydesc}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffeducation.isabroad" /></td>
			<td align="left"><c:out value="${staffeducation.isabroad}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="staffeducation.lastmodifydate" /></td>
			<td align="left"><c:out value="${staffeducation.lastmodifydate}" /></td>
		</tr>
		
	</table>

	

</body>
</html> --%>
