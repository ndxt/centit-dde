<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="" method="post" id="pagerForm">
		<div class="searchBar">
			<ul class="searchContent">
				
				<li><label><c:out value="userdefgroup.groupid" />:</label> <c:out value="${userdefgroup.groupid}" /></li>  
				<li><label><c:out value="userdefgroup.usercode" />:</label> <c:out value="${userdefgroup.usercode}" /></li> 
				<li><label><c:out value="userdefgroup.groupname" />:</label> <c:out value="${userdefgroup.groupname}" /></li> 
				<li><label><c:out value="userdefgroup.groupdesc" />:</label> <c:out value="${userdefgroup.groupdesc}" /></li> 
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

	
	<div layoutH="116">
		<table class="list" width="100%" targetType="navTab" asc="asc" desc="desc">
			<thead align="center">

				<tr>
					 
						
							<th><c:out value="groupuser.no" /></th> 
						 
					 
					 
						 
					 
						
							<th><c:out value="groupuser.inUsercode" /></th> 
						 
					
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach var="groupuser" items="${userdefgroup.groupusers}">
					<tr target="sid_user" rel="${user.usercode}">
						 
							
								<td><c:out value="${groupuser.no}" /></td> 
							 
						 
						 
							 
						 
							
								<td><c:out value="${groupuser.inUsercode}" /></td> 
							 
						
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
</div>



<%-- 
<html>
<head>
<title><c:out value="userdefgroup.view.title" /></title>
<link href="<c:out value='${STYLE_PATH}'/>/css/am.css" type="text/css" rel="stylesheet">

<link href="<c:out value='${STYLE_PATH}'/>/css/extremecomponents.css" type="text/css" rel="stylesheet">

<link href="<c:out value='${STYLE_PATH}'/>/css/messages.css" type="text/css" rel="stylesheet">

</head>

<body>
	<p class="ctitle">
		<c:out value="userdefgroup.view.title" />
	</p>

	<%@ include file="/page/common/messages.jsp"%>

	<html:button styleClass="btn" onclick="window.history.back()" property="none">
		<bean:message key="opt.btn.back" />
	</html:button>
	<p>
	<table width="200" border="0" cellpadding="1" cellspacing="1">
		
		<tr>
			<td class="TDTITLE"><c:out value="userdefgroup.groupid" /></td>
			<td align="left"><c:out value="${userdefgroup.groupid}" /></td>
		</tr>
		 
		<tr>
			<td class="TDTITLE"><c:out value="userdefgroup.usercode" /></td>
			<td align="left"><c:out value="${userdefgroup.usercode}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="userdefgroup.groupname" /></td>
			<td align="left"><c:out value="${userdefgroup.groupname}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="userdefgroup.groupdesc" /></td>
			<td align="left"><c:out value="${userdefgroup.groupdesc}" /></td>
		</tr>
		
	</table>

	
	<p />
	<div class="eXtremeTable">
		<table id="ec_table" border="0" cellspacing="0" cellpadding="0" class="tableRegion" width="100%">

			<thead>
				<tr>
					
					<td class="tableHeader"><c:out value="groupuser.no" /></td>   
					<td class="tableHeader"><c:out value="groupuser.inUsercode" /></td> 
					<td class="tableHeader"><bean:message key="opt.btn.collection" /></td>
				</tr>
			</thead>

			<tbody class="tableBody">
				<c:set value="odd" var="rownum" />

				<c:forEach var="groupuser" items="${userdefgroup.groupusers}">
					<tr class="${rownum}" onmouseover="this.className='highlight'" onmouseout="this.className='${rownum}'">
						
						<td><c:out value="${groupuser.no}" /></td>  
						<td><c:out value="${groupuser.inUsercode}" /></td> 
						<td><c:set var="deletecofirm">
								<bean:message key="label.delete.confirm" />
							</c:set> <a href='groupuser.do?groupid=${userdefgroup.groupid}&no=${groupuser.no}&method=edit'><bean:message key="opt.btn.edit" /></a> <a href='groupuser.do?groupid=${userdefgroup.groupid}&no=${groupuser.no}&method=delete'
							onclick='return confirm("${deletecofirm}groupuser?");'><bean:message key="opt.btn.delete" /></a></td>
					</tr>
					<c:set value="${rownum eq 'odd'? 'even': 'odd'}" var="rownum" />
				</c:forEach>
			</tbody>
		</table>
	</div>
	

</body>
</html> --%>
