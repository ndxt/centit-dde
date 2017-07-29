<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="${pageContext.request.contextPath }/dde/exchangeTask!listExchangeMapInfo.do?s_taskId=${taskId}">
	<input type="hidden" name="pageNum" value="1" /> 
	<input type="hidden"
		name="numPerPage" value="${pageDesc.pageSize}" /> 
	<input
		type="hidden" name="orderField" value="${s_orderField}" />
	<input type="hidden" name="s_taskType" value="${s_taskType}" />
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" target="selectedTodo" rel="ids" targetType="dialog" type="dialog"
			     href="${pageContext.request.contextPath }/dde/exchangeTask!importExchangeMapinfo.do?taskId=${taskId}"><span>添加交换对应关系</span></a></li>
		</ul>
	</div>
	<!-- <table class="table" width="800" layoutH="75"> -->
	  <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		  <%@ include file="../common/dialogBar.jsp"%>
		  <table class="list" width="100%" layoutH=".pageHeader 82">
	    </c:if>
	
	   <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		  <table class="list" width="100%" layoutH=".pageHeader 27">
	   </c:if>
		<thead>
				<tr>
				    <th width="22"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
					<th align="center" width="5%">序号</th>
					<th align="center" width="20%">${taskTypeList[taskType] }编号</th>
					<th align="center">源数据库名</th>
					<th align="center">业务系统ID</th>
					<th align="center">${taskTypeList[taskType] }名称</th>
					<th align="center">${taskTypeList[taskType] }说明</th>
				</tr>
			</thead>
		<tbody>
		
		    <c:forEach items="${exportSqls }" var="obj" varStatus="s">
					<tr target="mapInfoId" rel="${obj.exportId}">
					    <td><input name="ids" value="${obj.exportId}" type="checkbox" 
								<c:forEach var="u" items="${used }">
									<c:if test="${u eq obj.exportId }">checked="checked"</c:if>
								</c:forEach>
					    /></td>
					    <td align="center"><c:out value="${s.index+1 }"></c:out></td>
					    <td align="center">${obj.exportId }</td>
						<td align="center">${obj.sourceDatabaseName }</td>
						<td align="center">${obj.sourceOsId }</td>
						<td align="center">${obj.exportName }</td>
						<td align="center">${obj.exportDesc }</td>
					</tr>
		    </c:forEach>		    
		</tbody>
	</table>
</div>

<jsp:include page="../common/dialogBar.jsp"></jsp:include>