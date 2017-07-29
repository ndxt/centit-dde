<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="publicinfolog.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> <input type="hidden" name="orderField"
		value="${s_orderField}" />
</form>



<div class="pageHeader">
	<s:form id="pagerForm" onsubmit="return navTabSearch(this);" action="/app/publicinfolog.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				
					<li><label><c:out value="publicinfolog.usercode" />:</label> <s:textfield name="s_usercode" value="%{#parameters['s_usercode']}" /></li>
				
					<li><label><c:out value="publicinfolog.infocode" />:</label> <s:textfield name="s_infocode" value="%{#parameters['s_infocode']}" /></li>
				
				
					<li><label><c:out value="publicinfolog.operation" />:</label> <s:textfield name="s_operation" value="%{#parameters['s_operation']}" /></li>
				
					<li><label><c:out value="publicinfolog.data1" />:</label> <s:textfield name="s_data1" value="%{#parameters['s_data1']}" /></li>
				
					<li><label><c:out value="publicinfolog.data2" />:</label> <s:textfield name="s_data2" value="%{#parameters['s_data2']}" /></li>
				
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
			<li><a class="add" href="publicinfolog!edit.do" rel="" target='dialog'><span>添加</span></a></li>
			<li><a class="edit" href="publicinfolog!edit.do?usercodeinfocode={pk}" warn="请选择一条记录" rel="" target='dialog'><span>编辑</span></a></li>
			<li><a class="delete" href="publicinfolog!delete.do?usercodeinfocode={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div>

	<div layoutH="116">
		<table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
			<thead>

				<tr>
					
						<c:set var="tusercode"><bean:message bundle='appRes' key='publicinfolog.usercode' /></c:set>	
						<th>${tusercode}</th>
					
						<c:set var="tinfocode"><bean:message bundle='appRes' key='publicinfolog.infocode' /></c:set>	
						<th>${tinfocode}</th>
					
					
						<c:set var="toperation"><bean:message bundle='appRes' key='publicinfolog.operation' /></c:set>	
						<th>${toperation}</th>
					
						<c:set var="tdata1"><bean:message bundle='appRes' key='publicinfolog.data1' /></c:set>	
						<th>${tdata1}</th>
					
						<c:set var="tdata2"><bean:message bundle='appRes' key='publicinfolog.data2' /></c:set>	
						<th>${tdata2}</th>
					
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${objList }" var="publicinfolog">
						<tr target="pk" rel="${publicinfolog.usercode}">
							
								<td>${publicinfolog.usercode}</td>
							
								<td>${publicinfolog.infocode}</td>
							
							
								<td>${publicinfolog.operation}</td>
							
								<td>${publicinfolog.data1}</td>
							
								<td>${publicinfolog.data2}</td>
							
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
		<title><c:out value="publicinfolog.list.title" /></title>
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
			<html:form action="/app/publicinfolog.do" style="margin-top:0;margin-bottom:5">
				<table cellpadding="0" cellspacing="0" align="center">

					<tr height="22">
						<td><c:out value="publicinfolog.usercode" />:</td>
						<td><html:text property="s_usercode" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfolog.infocode" />:</td>
						<td><html:text property="s_infocode" /> </td>
					</tr>	


					<tr height="22">
						<td><c:out value="publicinfolog.operation" />:</td>
						<td><html:text property="s_operation" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfolog.data1" />:</td>
						<td><html:text property="s_data1" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfolog.data2" />:</td>
						<td><html:text property="s_data2" /> </td>
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

			<ec:table action="publicinfolog.do" items="publicinfologs" var="publicinfolog"
			imagePath="${STYLE_PATH}/images/table/*.gif" retrieveRowsCallback="limit">
			<ec:exportXls fileName="publicinfologs.xls" ></ec:exportXls>
			<ec:exportPdf fileName="publicinfologs.pdf" headerColor="blue" headerBackgroundColor="white" ></ec:exportPdf>
			<ec:row>
				
					<c:set var="tusercode"><bean:message bundle='appRes' key='publicinfolog.usercode' /></c:set>	
					<ec:column property="usercode" title="${tusercode}" style="text-align:center" />
				
					<c:set var="tinfocode"><bean:message bundle='appRes' key='publicinfolog.infocode' /></c:set>	
					<ec:column property="infocode" title="${tinfocode}" style="text-align:center" />
				
				
					<c:set var="toperation"><bean:message bundle='appRes' key='publicinfolog.operation' /></c:set>	
					<ec:column property="operation" title="${toperation}" style="text-align:center" />
				
					<c:set var="tdata1"><bean:message bundle='appRes' key='publicinfolog.data1' /></c:set>	
					<ec:column property="data1" title="${tdata1}" style="text-align:center" />
				
					<c:set var="tdata2"><bean:message bundle='appRes' key='publicinfolog.data2' /></c:set>	
					<ec:column property="data2" title="${tdata2}" style="text-align:center" />
						
				<c:set var="optlabel"><bean:message key="opt.btn.collection"/></c:set>	
				<ec:column property="opt" title="${optlabel}" sortable="false"
					style="text-align:center">
					<c:set var="deletecofirm"><bean:message key="label.delete.confirm"/></c:set>
					<a href='publicinfolog.do?usercode=${publicinfolog.usercode}&infocode=${publicinfolog.infocode}&method=view'><bean:message key="opt.btn.view" /></a>
					<a href='publicinfolog.do?usercode=${publicinfolog.usercode}&infocode=${publicinfolog.infocode}&method=edit'><bean:message key="opt.btn.edit" /></a>
					<a href='publicinfolog.do?usercode=${publicinfolog.usercode}&infocode=${publicinfolog.infocode}&method=delete' 
							onclick='return confirm("${deletecofirm}publicinfolog?");'><bean:message key="opt.btn.delete" /></a>
				</ec:column>

			</ec:row>
		</ec:table>

	</body>
</html>
 --%>