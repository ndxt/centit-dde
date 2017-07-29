<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="databaseInfo.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden"
		name="numPerPage" value="${pageDesc.pageSize}" /> <input
		type="hidden" name="orderField" value="${s_orderField}" />
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"
				href="${contextPath }/dde/databaseInfo!add.do" rel=""
				target='dialog' width="600" height="400"><span>添加</span></a></li>
			<li><a class="edit"
				href="${contextPath }/dde/databaseInfo!edit.do?databaseName={pk}"
				warn="请选择一条记录" rel="" target='dialog' width="600" height="400"><span>编辑</span></a></li>
			<li><a class="delete"
				href="${contextPath }/dde/databaseInfo!delete.do?databaseName={pk}"
				warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div>

	<div layoutH="116">
		<table class="list" width="100%" targetType="navTab" asc="asc"
			desc="desc">
			<thead align="center">
				<tr>
					<th width="15%">数据库名</th>					
					<th width="10%">数据库类型</th>					
					<th width="35%">数据库连接url</th>					
					<th width="10%">用户名</th>									
					<th >数据内容描述</th>
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${objList}" var="databaseInfo">
					<tr target="pk" rel="${databaseInfo.databaseName}">
						<td>${databaseInfo.databaseName}</td>
						<td>
						   <c:if test="${databaseInfo.databaseType eq 1}">SQLSERVER</c:if>
						   <c:if test="${databaseInfo.databaseType eq 2}">ORACLE</c:if>
						   <c:if test="${databaseInfo.databaseType eq 3}">DB2</c:if>
						   <c:if test="${databaseInfo.databaseType eq 4}">ACCESS</c:if>
						   <c:if test="${databaseInfo.databaseType eq 5}">MYSQL</c:if>
						</td>
						<td>${databaseInfo.databaseUrl}</td>
						<td>${databaseInfo.username}</td>
						<td>${databaseInfo.dataDesc}</td>

					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>