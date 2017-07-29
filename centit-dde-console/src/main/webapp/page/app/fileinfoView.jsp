<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="" method="post" id="pagerForm">
		<div class="searchBar">
			<ul class="searchContent">
				
				<li><label><c:out value="fileinfo.filecode" />:</label> <c:out value="${fileinfo.filecode}" /></li>  
				<li><label><c:out value="fileinfo.filename" />:</label> <c:out value="${fileinfo.filename}" /></li> 
				<li><label><c:out value="fileinfo.path" />:</label> <c:out value="${fileinfo.path}" /></li> 
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
					 
						
							<th><c:out value="publicinfo.infocode" /></th> 
						 
					 
					 
						
							<th><c:out value="publicinfo.parentinfocode" /></th> 
						 
					 
						 
					 
						
							<th><c:out value="publicinfo.filename" /></th> 
						 
					 
						
							<th><c:out value="publicinfo.fileextension" /></th> 
						 
					 
						
							<th><c:out value="publicinfo.ownercode" /></th> 
						 
					 
						
							<th><c:out value="publicinfo.readcount" /></th> 
						 
					 
						
							<th><c:out value="publicinfo.downloadcount" /></th> 
						 
					 
						
							<th><c:out value="publicinfo.md5" /></th> 
						 
					 
						
							<th><c:out value="publicinfo.uploadtime" /></th> 
						 
					 
						
							<th><c:out value="publicinfo.modifytimes" /></th> 
						 
					 
						
							<th><c:out value="publicinfo.status" /></th> 
						 
					 
						
							<th><c:out value="publicinfo.type" /></th> 
						 
					 
						
							<th><c:out value="publicinfo.isfolder" /></th> 
						 
					 
						
							<th><c:out value="publicinfo.filedescription" /></th> 
						 
					
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach var="publicinfo" items="${fileinfo.publicinfos}">
					<tr target="sid_user" rel="${user.usercode}">
						 
							
								<td><c:out value="${publicinfo.infocode}" /></td> 
							 
						 
						 
							
								<td><c:out value="${publicinfo.parentinfocode}" /></td> 
							 
						 
							 
						 
							
								<td><c:out value="${publicinfo.filename}" /></td> 
							 
						 
							
								<td><c:out value="${publicinfo.fileextension}" /></td> 
							 
						 
							
								<td><c:out value="${publicinfo.ownercode}" /></td> 
							 
						 
							
								<td><c:out value="${publicinfo.readcount}" /></td> 
							 
						 
							
								<td><c:out value="${publicinfo.downloadcount}" /></td> 
							 
						 
							
								<td><c:out value="${publicinfo.md5}" /></td> 
							 
						 
							
								<td><c:out value="${publicinfo.uploadtime}" /></td> 
							 
						 
							
								<td><c:out value="${publicinfo.modifytimes}" /></td> 
							 
						 
							
								<td><c:out value="${publicinfo.status}" /></td> 
							 
						 
							
								<td><c:out value="${publicinfo.type}" /></td> 
							 
						 
							
								<td><c:out value="${publicinfo.isfolder}" /></td> 
							 
						 
							
								<td><c:out value="${publicinfo.filedescription}" /></td> 
							 
						
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
</div>



<%-- 
<html>
<head>
<title><c:out value="fileinfo.view.title" /></title>
<link href="<c:out value='${STYLE_PATH}'/>/css/am.css" type="text/css" rel="stylesheet">

<link href="<c:out value='${STYLE_PATH}'/>/css/extremecomponents.css" type="text/css" rel="stylesheet">

<link href="<c:out value='${STYLE_PATH}'/>/css/messages.css" type="text/css" rel="stylesheet">

</head>

<body>
	<p class="ctitle">
		<c:out value="fileinfo.view.title" />
	</p>

	<%@ include file="/page/common/messages.jsp"%>

	<html:button styleClass="btn" onclick="window.history.back()" property="none">
		<bean:message key="opt.btn.back" />
	</html:button>
	<p>
	<table width="200" border="0" cellpadding="1" cellspacing="1">
		
		<tr>
			<td class="TDTITLE"><c:out value="fileinfo.filecode" /></td>
			<td align="left"><c:out value="${fileinfo.filecode}" /></td>
		</tr>
		 
		<tr>
			<td class="TDTITLE"><c:out value="fileinfo.filename" /></td>
			<td align="left"><c:out value="${fileinfo.filename}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="fileinfo.path" /></td>
			<td align="left"><c:out value="${fileinfo.path}" /></td>
		</tr>
		
	</table>

	
	<p />
	<div class="eXtremeTable">
		<table id="ec_table" border="0" cellspacing="0" cellpadding="0" class="tableRegion" width="100%">

			<thead>
				<tr>
					
					<td class="tableHeader"><c:out value="publicinfo.infocode" /></td>  
					<td class="tableHeader"><c:out value="publicinfo.parentinfocode" /></td>  
					<td class="tableHeader"><c:out value="publicinfo.filename" /></td> 
					<td class="tableHeader"><c:out value="publicinfo.fileextension" /></td> 
					<td class="tableHeader"><c:out value="publicinfo.ownercode" /></td> 
					<td class="tableHeader"><c:out value="publicinfo.readcount" /></td> 
					<td class="tableHeader"><c:out value="publicinfo.downloadcount" /></td> 
					<td class="tableHeader"><c:out value="publicinfo.md5" /></td> 
					<td class="tableHeader"><c:out value="publicinfo.uploadtime" /></td> 
					<td class="tableHeader"><c:out value="publicinfo.modifytimes" /></td> 
					<td class="tableHeader"><c:out value="publicinfo.status" /></td> 
					<td class="tableHeader"><c:out value="publicinfo.type" /></td> 
					<td class="tableHeader"><c:out value="publicinfo.isfolder" /></td> 
					<td class="tableHeader"><c:out value="publicinfo.filedescription" /></td> 
					<td class="tableHeader"><bean:message key="opt.btn.collection" /></td>
				</tr>
			</thead>

			<tbody class="tableBody">
				<c:set value="odd" var="rownum" />

				<c:forEach var="publicinfo" items="${fileinfo.publicinfos}">
					<tr class="${rownum}" onmouseover="this.className='highlight'" onmouseout="this.className='${rownum}'">
						
						<td><c:out value="${publicinfo.infocode}" /></td>  
						<td><c:out value="${publicinfo.parentinfocode}" /></td> 
						<td><c:out value="${publicinfo.filename}" /></td> 
						<td><c:out value="${publicinfo.fileextension}" /></td> 
						<td><c:out value="${publicinfo.ownercode}" /></td> 
						<td><c:out value="${publicinfo.readcount}" /></td> 
						<td><c:out value="${publicinfo.downloadcount}" /></td> 
						<td><c:out value="${publicinfo.md5}" /></td> 
						<td><c:out value="${publicinfo.uploadtime}" /></td> 
						<td><c:out value="${publicinfo.modifytimes}" /></td> 
						<td><c:out value="${publicinfo.status}" /></td> 
						<td><c:out value="${publicinfo.type}" /></td> 
						<td><c:out value="${publicinfo.isfolder}" /></td> 
						<td><c:out value="${publicinfo.filedescription}" /></td> 
						<td><c:set var="deletecofirm">
								<bean:message key="label.delete.confirm" />
							</c:set> <a href='publicinfo.do?filecode=${fileinfo.filecode}&infocode=${publicinfo.infocode}&method=edit'><bean:message key="opt.btn.edit" /></a> <a href='publicinfo.do?filecode=${fileinfo.filecode}&infocode=${publicinfo.infocode}&method=delete'
							onclick='return confirm("${deletecofirm}publicinfo?");'><bean:message key="opt.btn.delete" /></a></td>
					</tr>
					<c:set value="${rownum eq 'odd'? 'even': 'odd'}" var="rownum" />
				</c:forEach>
			</tbody>
		</table>
	</div>
	

</body>
</html> --%>
