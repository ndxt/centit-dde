<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="" method="post" id="pagerForm">
		<div class="searchBar">
			<ul class="searchContent">
				
				<li><label><c:out value="publicinfo.infocode" />:</label> <c:out value="${publicinfo.infocode}" /></li>  
				<li><label><c:out value="publicinfo.parentinfocode" />:</label> <c:out value="${publicinfo.parentinfocode}" /></li> 
				<li><label><c:out value="publicinfo.filecode" />:</label> <c:out value="${publicinfo.filecode}" /></li> 
				<li><label><c:out value="publicinfo.filename" />:</label> <c:out value="${publicinfo.filename}" /></li> 
				<li><label><c:out value="publicinfo.fileextension" />:</label> <c:out value="${publicinfo.fileextension}" /></li> 
				<li><label><c:out value="publicinfo.ownercode" />:</label> <c:out value="${publicinfo.ownercode}" /></li> 
				<li><label><c:out value="publicinfo.readcount" />:</label> <c:out value="${publicinfo.readcount}" /></li> 
				<li><label><c:out value="publicinfo.downloadcount" />:</label> <c:out value="${publicinfo.downloadcount}" /></li> 
				<li><label><c:out value="publicinfo.md5" />:</label> <c:out value="${publicinfo.md5}" /></li> 
				<li><label><c:out value="publicinfo.uploadtime" />:</label> <c:out value="${publicinfo.uploadtime}" /></li> 
				<li><label><c:out value="publicinfo.modifytimes" />:</label> <c:out value="${publicinfo.modifytimes}" /></li> 
				<li><label><c:out value="publicinfo.status" />:</label> <c:out value="${publicinfo.status}" /></li> 
				<li><label><c:out value="publicinfo.type" />:</label> <c:out value="${publicinfo.type}" /></li> 
				<li><label><c:out value="publicinfo.isfolder" />:</label> <c:out value="${publicinfo.isfolder}" /></li> 
				<li><label><c:out value="publicinfo.filedescription" />:</label> <c:out value="${publicinfo.filedescription}" /></li> 
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
					 
						
							<th><c:out value="publicinfolog.usercode" /></th> 
						 
					 
						 
					 
					 
						
							<th><c:out value="publicinfolog.operation" /></th> 
						 
					 
						
							<th><c:out value="publicinfolog.data1" /></th> 
						 
					 
						
							<th><c:out value="publicinfolog.data2" /></th> 
						 
					
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach var="publicinfolog" items="${publicinfo.publicinfologs}">
					<tr target="sid_user" rel="${user.usercode}">
						 
							
								<td><c:out value="${publicinfolog.usercode}" /></td> 
							 
						 
							 
						 
						 
							
								<td><c:out value="${publicinfolog.operation}" /></td> 
							 
						 
							
								<td><c:out value="${publicinfolog.data1}" /></td> 
							 
						 
							
								<td><c:out value="${publicinfolog.data2}" /></td> 
							 
						
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
	<div layoutH="116">
		<table class="list" width="100%" targetType="navTab" asc="asc" desc="desc">
			<thead align="center">

				<tr>
					 
						
							<th><c:out value="msgannex.msgcode" /></th> 
						 
					 
						
							<th><c:out value="msgannex.filecode" /></th> 
						 
					 
					
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach var="msgannex" items="${publicinfo.msgannexs}">
					<tr target="sid_user" rel="${user.usercode}">
						 
							
								<td><c:out value="${msgannex.msgcode}" /></td> 
							 
						 
							
								<td><c:out value="${msgannex.filecode}" /></td> 
							 
						 
						
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
</div>



<%-- 
<html>
<head>
<title><c:out value="publicinfo.view.title" /></title>
<link href="<c:out value='${STYLE_PATH}'/>/css/am.css" type="text/css" rel="stylesheet">

<link href="<c:out value='${STYLE_PATH}'/>/css/extremecomponents.css" type="text/css" rel="stylesheet">

<link href="<c:out value='${STYLE_PATH}'/>/css/messages.css" type="text/css" rel="stylesheet">

</head>

<body>
	<p class="ctitle">
		<c:out value="publicinfo.view.title" />
	</p>

	<%@ include file="/page/common/messages.jsp"%>

	<html:button styleClass="btn" onclick="window.history.back()" property="none">
		<bean:message key="opt.btn.back" />
	</html:button>
	<p>
	<table width="200" border="0" cellpadding="1" cellspacing="1">
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.infocode" /></td>
			<td align="left"><c:out value="${publicinfo.infocode}" /></td>
		</tr>
		 
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.parentinfocode" /></td>
			<td align="left"><c:out value="${publicinfo.parentinfocode}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.filecode" /></td>
			<td align="left"><c:out value="${publicinfo.filecode}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.filename" /></td>
			<td align="left"><c:out value="${publicinfo.filename}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.fileextension" /></td>
			<td align="left"><c:out value="${publicinfo.fileextension}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.ownercode" /></td>
			<td align="left"><c:out value="${publicinfo.ownercode}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.readcount" /></td>
			<td align="left"><c:out value="${publicinfo.readcount}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.downloadcount" /></td>
			<td align="left"><c:out value="${publicinfo.downloadcount}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.md5" /></td>
			<td align="left"><c:out value="${publicinfo.md5}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.uploadtime" /></td>
			<td align="left"><c:out value="${publicinfo.uploadtime}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.modifytimes" /></td>
			<td align="left"><c:out value="${publicinfo.modifytimes}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.status" /></td>
			<td align="left"><c:out value="${publicinfo.status}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.type" /></td>
			<td align="left"><c:out value="${publicinfo.type}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.isfolder" /></td>
			<td align="left"><c:out value="${publicinfo.isfolder}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="publicinfo.filedescription" /></td>
			<td align="left"><c:out value="${publicinfo.filedescription}" /></td>
		</tr>
		
	</table>

	
	<p />
	<div class="eXtremeTable">
		<table id="ec_table" border="0" cellspacing="0" cellpadding="0" class="tableRegion" width="100%">

			<thead>
				<tr>
					
					<td class="tableHeader"><c:out value="publicinfolog.usercode" /></td>   
					<td class="tableHeader"><c:out value="publicinfolog.operation" /></td> 
					<td class="tableHeader"><c:out value="publicinfolog.data1" /></td> 
					<td class="tableHeader"><c:out value="publicinfolog.data2" /></td> 
					<td class="tableHeader"><bean:message key="opt.btn.collection" /></td>
				</tr>
			</thead>

			<tbody class="tableBody">
				<c:set value="odd" var="rownum" />

				<c:forEach var="publicinfolog" items="${publicinfo.publicinfologs}">
					<tr class="${rownum}" onmouseover="this.className='highlight'" onmouseout="this.className='${rownum}'">
						
						<td><c:out value="${publicinfolog.usercode}" /></td>  
						<td><c:out value="${publicinfolog.operation}" /></td> 
						<td><c:out value="${publicinfolog.data1}" /></td> 
						<td><c:out value="${publicinfolog.data2}" /></td> 
						<td><c:set var="deletecofirm">
								<bean:message key="label.delete.confirm" />
							</c:set> <a href='publicinfolog.do?infocode=${publicinfo.infocode}&usercode=${publicinfolog.usercode}&infocode=${publicinfolog.infocode}&method=edit'><bean:message key="opt.btn.edit" /></a> <a href='publicinfolog.do?infocode=${publicinfo.infocode}&usercode=${publicinfolog.usercode}&infocode=${publicinfolog.infocode}&method=delete'
							onclick='return confirm("${deletecofirm}publicinfolog?");'><bean:message key="opt.btn.delete" /></a></td>
					</tr>
					<c:set value="${rownum eq 'odd'? 'even': 'odd'}" var="rownum" />
				</c:forEach>
			</tbody>
		</table>
	</div>
	
	<p />
	<div class="eXtremeTable">
		<table id="ec_table" border="0" cellspacing="0" cellpadding="0" class="tableRegion" width="100%">

			<thead>
				<tr>
					
					<td class="tableHeader"><c:out value="msgannex.msgcode" /></td> 
					<td class="tableHeader"><c:out value="msgannex.filecode" /></td>  
					<td class="tableHeader"><bean:message key="opt.btn.collection" /></td>
				</tr>
			</thead>

			<tbody class="tableBody">
				<c:set value="odd" var="rownum" />

				<c:forEach var="msgannex" items="${publicinfo.msgannexs}">
					<tr class="${rownum}" onmouseover="this.className='highlight'" onmouseout="this.className='${rownum}'">
						
						<td><c:out value="${msgannex.msgcode}" /></td> 
						<td><c:out value="${msgannex.filecode}" /></td>  
						<td><c:set var="deletecofirm">
								<bean:message key="label.delete.confirm" />
							</c:set> <a href='msgannex.do?infocode=${publicinfo.infocode}&msgcode=${msgannex.msgcode}&filecode=${msgannex.filecode}&method=edit'><bean:message key="opt.btn.edit" /></a> <a href='msgannex.do?infocode=${publicinfo.infocode}&msgcode=${msgannex.msgcode}&filecode=${msgannex.filecode}&method=delete'
							onclick='return confirm("${deletecofirm}msgannex?");'><bean:message key="opt.btn.delete" /></a></td>
					</tr>
					<c:set value="${rownum eq 'odd'? 'even': 'odd'}" var="rownum" />
				</c:forEach>
			</tbody>
		</table>
	</div>
	

</body>
</html> --%>
