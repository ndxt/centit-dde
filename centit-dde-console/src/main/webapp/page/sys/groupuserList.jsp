<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="groupuser.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> <input type="hidden" name="orderField"
		value="${s_orderField}" />
</form>



<div class="pageHeader">
	<s:form id="pagerForm" onsubmit="return navTabSearch(this);" action="/sys/groupuser.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				
					<li><label><c:out value="groupuser.no" />:</label> <s:textfield name="s_no" value="%{#parameters['s_no']}" /></li>
				
				
					<li><label><c:out value="groupuser.groupid" />:</label> <s:textfield name="s_groupid" value="%{#parameters['s_groupid']}" /></li>
				
					<li><label><c:out value="groupuser.inUsercode" />:</label> <s:textfield name="s_inUsercode" value="%{#parameters['s_inUsercode']}" /></li>
				
			</ul>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<s:submit method="list"><bean:message key="opt.btn.query" /></s:submit>
							</div>
						</div>
					</li>
					<li>
						<div class="buttonActive">
							<div class="buttonContent">
								<!-- 参数 navTabId 根据实际情况填写 -->
								<button type="button" onclick="javascript:navTabAjaxDone({'statusCode' : 200, 'callbackType' : 'closeCurrent', 'navTabId' : ''});">返回</button>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</s:form>
</div>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${contextPath }/sys/groupuser!edit.do" rel="" target='dialog'><span>添加</span></a></li>
			<li><a class="edit" href="${contextPath }/sys/groupuser!edit.do?no={pk}" warn="请选择一条记录" rel="" target='dialog'><span>编辑</span></a></li>
			<li><a class="delete" href="${contextPath }/sys/groupuser!delete.do?no={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div>

	<div layoutH="116">
		<table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
			<thead align="center">

				<tr>
					
						<c:set var="tno"><bean:message bundle='sysRes' key='groupuser.no' /></c:set>	
						<th>${tno}</th>
					
					
						<c:set var="tgroupid"><bean:message bundle='sysRes' key='groupuser.groupid' /></c:set>	
						<th>${tgroupid}</th>
					
						<c:set var="tinUsercode"><bean:message bundle='sysRes' key='groupuser.inUsercode' /></c:set>	
						<th>${tinUsercode}</th>
					
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${objList }" var="groupuser">
						<tr target="pk" rel="${groupuser.no}">
							
								<td title="${groupuser.no}">${groupuser.no}</td>
							
							
								<td title="${groupuser.groupid}">${groupuser.groupid}</td>
							
								<td title="${groupuser.inUsercode}">${groupuser.inUsercode}</td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>

<%-- 
<html>
	<head>
		<title><c:out value="groupuser.list.title" /></title>
		<link href="<c:out value='${STYLE_PATH}'/>/css/am.css" type="text/css"
			rel="stylesheet">
		<link href="<c:out value='${STYLE_PATH}'/>/css/extremecomponents.css"
			type="text/css" rel="stylesheet">
		<link href="<c:out value='${STYLE_PATH}'/>/css/messages.css"
			type="text/css" rel="stylesheet">
	</head>

	<body>
		<%@ include file="/page/common/messages.jsp"%>
		<fieldset
			style="border: hidden 1px #000000; ">
			<legend>
				 <s:text name="label.list.filter" />
			</legend>
			<html:form action="/sys/groupuser.do" style="margin-top:0;margin-bottom:5">
				<table cellpadding="0" cellspacing="0" align="center">

					<tr height="22">
						<td><c:out value="groupuser.no" />:</td>
						<td><html:text property="s_no" /> </td>
					</tr>	


					<tr height="22">
						<td><c:out value="groupuser.groupid" />:</td>
						<td><html:text property="s_groupid" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="groupuser.inUsercode" />:</td>
						<td><html:text property="s_inUsercode" /> </td>
					</tr>	

					<tr>
						<td>
							<html:submit property="method_list" styleClass="btn" > <bean:message key="opt.btn.query" /></html:submit>
						</td>
						<td>
							<html:submit property="method_edit" styleClass="btn" > <bean:message key="opt.btn.new" /> </html:submit>
						</td>
					</tr>
				</table>
			</html:form>
		</fieldset>

			<ec:table action="groupuser.do" items="groupusers" var="groupuser"
			imagePath="${STYLE_PATH}/images/table/*.gif" retrieveRowsCallback="limit">
			<ec:exportXls fileName="groupusers.xls" ></ec:exportXls>
			<ec:exportPdf fileName="groupusers.pdf" headerColor="blue" headerBackgroundColor="white" ></ec:exportPdf>
			<ec:row>
				
					<c:set var="tno"><bean:message bundle='sysRes' key='groupuser.no' /></c:set>	
					<ec:column property="no" title="${tno}" style="text-align:center" />
				
				
					<c:set var="tgroupid"><bean:message bundle='sysRes' key='groupuser.groupid' /></c:set>	
					<ec:column property="groupid" title="${tgroupid}" style="text-align:center" />
				
					<c:set var="tinUsercode"><bean:message bundle='sysRes' key='groupuser.inUsercode' /></c:set>	
					<ec:column property="inUsercode" title="${tinUsercode}" style="text-align:center" />
						
				<c:set var="optlabel"><bean:message key="opt.btn.collection"/></c:set>	
				<ec:column property="opt" title="${optlabel}" sortable="false"
					style="text-align:center">
					<c:set var="deletecofirm"><bean:message key="label.delete.confirm"/></c:set>
					<a href='groupuser.do?no=${groupuser.no}&method=view'><bean:message key="opt.btn.view" /></a>
					<a href='groupuser.do?no=${groupuser.no}&method=edit'><bean:message key="opt.btn.edit" /></a>
					<a href='groupuser.do?no=${groupuser.no}&method=delete' 
							onclick='return confirm("${deletecofirm}groupuser?");'><bean:message key="opt.btn.delete" /></a>
				</ec:column>

			</ec:row>
		</ec:table>

	</body>
</html>
 --%>