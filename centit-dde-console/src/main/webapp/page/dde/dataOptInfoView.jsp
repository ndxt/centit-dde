<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="" method="post" id="pagerForm">
		<div class="searchBar">
			<ul class="searchContent">
				
				<li><label><c:out value="dataOptInfo.dataOptId" />:</label> <c:out value="${dataOptInfo.dataOptId}" /></li>  
				<li><label><c:out value="dataOptInfo.optDesc" />:</label> <c:out value="${dataOptInfo.optDesc}" /></li> 
				<li><label><c:out value="dataOptInfo.created" />:</label> <c:out value="${dataOptInfo.created}" /></li> 
				<li><label><c:out value="dataOptInfo.lastUpdateTime" />:</label> <c:out value="${dataOptInfo.lastUpdateTime}" /></li> 
				<li><label><c:out value="dataOptInfo.createTime" />:</label> <c:out value="${dataOptInfo.createTime}" /></li> 
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
					 
						
							<th><c:out value="dataOptStep.optStepId" /></th> 
						 
					 
					 
						
							<th><c:out value="dataOptStep.importId" /></th> 
						 
					 
						
							<th><c:out value="dataOptStep.optType" /></th> 
						 
					 
						
							<th><c:out value="dataOptStep.dataOptId" /></th> 
						 
					 
						
							<th><c:out value="dataOptStep.osId" /></th> 
						 
					 
						
							<th><c:out value="dataOptStep.mapInfoOrder" /></th>
						 
					
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach var="dataOptStep" items="${dataOptInfo.dataOptSteps}">
					<tr target="sid_user" rel="${user.usercode}">
						 
							
								<td><c:out value="${dataOptStep.optStepId}" /></td> 
							 
						 
						 
							
								<td><c:out value="${dataOptStep.importId}" /></td> 
							 
						 
							
								<td><c:out value="${dataOptStep.optType}" /></td> 
							 
						 
							
								<td><c:out value="${dataOptStep.dataOptId}" /></td> 
							 
						 
							
								<td><c:out value="${dataOptStep.osId}" /></td> 
							 
						 
							
								<td><c:out value="${dataOptStep.mapInfoOrder}" /></td>
							 
						
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
</div>



<%-- 
<html>
<head>
<title><c:out value="dataOptInfo.view.title" /></title>
<link href="<c:out value='${STYLE_PATH}'/>/css/am.css" type="text/css" rel="stylesheet">

<link href="<c:out value='${STYLE_PATH}'/>/css/extremecomponents.css" type="text/css" rel="stylesheet">

<link href="<c:out value='${STYLE_PATH}'/>/css/messages.css" type="text/css" rel="stylesheet">

</head>

<body>
	<p class="ctitle">
		<c:out value="dataOptInfo.view.title" />
	</p>

	<%@ include file="/page/common/messages.jsp"%>

	<html:button styleClass="btn" onclick="window.history.back()" property="none">
		<bean:message key="opt.btn.back" />
	</html:button>
	<p>
	<table width="200" border="0" cellpadding="1" cellspacing="1">
		
		<tr>
			<td class="TDTITLE"><c:out value="dataOptInfo.dataOptId" /></td>
			<td align="left"><c:out value="${dataOptInfo.dataOptId}" /></td>
		</tr>
		 
		<tr>
			<td class="TDTITLE"><c:out value="dataOptInfo.optDesc" /></td>
			<td align="left"><c:out value="${dataOptInfo.optDesc}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="dataOptInfo.created" /></td>
			<td align="left"><c:out value="${dataOptInfo.created}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="dataOptInfo.lastUpdateTime" /></td>
			<td align="left"><c:out value="${dataOptInfo.lastUpdateTime}" /></td>
		</tr>
		
		<tr>
			<td class="TDTITLE"><c:out value="dataOptInfo.createTime" /></td>
			<td align="left"><c:out value="${dataOptInfo.createTime}" /></td>
		</tr>
		
	</table>

	
	<p />
	<div class="eXtremeTable">
		<table id="ec_table" border="0" cellspacing="0" cellpadding="0" class="tableRegion" width="100%">

			<thead>
				<tr>
					
					<td class="tableHeader"><c:out value="dataOptStep.optStepId" /></td>  
					<td class="tableHeader"><c:out value="dataOptStep.importId" /></td> 
					<td class="tableHeader"><c:out value="dataOptStep.optType" /></td> 
					<td class="tableHeader"><c:out value="dataOptStep.dataOptId" /></td> 
					<td class="tableHeader"><c:out value="dataOptStep.osId" /></td> 
					<td class="tableHeader"><c:out value="dataOptStep.mapInfoOrder" /></td>
					<td class="tableHeader"><bean:message key="opt.btn.collection" /></td>
				</tr>
			</thead>

			<tbody class="tableBody">
				<c:set value="odd" var="rownum" />

				<c:forEach var="dataOptStep" items="${dataOptInfo.dataOptSteps}">
					<tr class="${rownum}" onmouseover="this.className='highlight'" onmouseout="this.className='${rownum}'">
						
						<td><c:out value="${dataOptStep.optStepId}" /></td>  
						<td><c:out value="${dataOptStep.importId}" /></td> 
						<td><c:out value="${dataOptStep.optType}" /></td> 
						<td><c:out value="${dataOptStep.dataOptId}" /></td> 
						<td><c:out value="${dataOptStep.osId}" /></td> 
						<td><c:out value="${dataOptStep.mapInfoOrder}" /></td>
						<td><c:set var="deletecofirm">
								<bean:message key="label.delete.confirm" />
							</c:set> <a href='dataOptStep.do?dataOptId=${dataOptInfo.dataOptId}&optStepId=${dataOptStep.optStepId}&method=edit'><bean:message key="opt.btn.edit" /></a> <a href='dataOptStep.do?dataOptId=${dataOptInfo.dataOptId}&optStepId=${dataOptStep.optStepId}&method=delete'
							onclick='return confirm("${deletecofirm}dataOptStep?");'><bean:message key="opt.btn.delete" /></a></td>
					</tr>
					<c:set value="${rownum eq 'odd'? 'even': 'odd'}" var="rownum" />
				</c:forEach>
			</tbody>
		</table>
	</div>
	

</body>
</html> --%>
