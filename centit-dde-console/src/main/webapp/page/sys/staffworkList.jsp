<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="staffwork.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> <input type="hidden" name="orderField"
		value="${s_orderField}" />
</form>


<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="edit" href="${contextPath }/sys/staffwork!edit.do?id={pk}" warn="请选择一条记录" rel="" target='dialog' width="600" height="400"><span>编辑</span></a></li>
			<li><a class="delete" href="${contextPath }/sys/staffwork!delete.do?id={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div>

	<div layoutH="217">
		<table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
			<thead align="center">

				<tr>
					<th>序号</th>
					<th>工作开始时间</th>
					<th>工作结束时间</th>
					<th>公司名称</th>
					<th>所属行业</th>
					<th>公司规模</th>
					<th>公司性质</th>
					<th>部门</th>
					<th>职位</th>
					<th>工作描述</th>
					<th>最后修改时间</th>
					
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${objList }" var="staffwork" varStatus="s">
						<tr target="pk" rel="${staffwork.id}">
							
								<td>${s.index+1}</td>
							
								<td><fmt:formatDate pattern="yyyy-MM-dd" value="${staffwork.workbegin}"/></td>
							
								<td><fmt:formatDate pattern="yyyy-MM-dd" value="${staffwork.workend}"/></td>
							
								<td>${staffwork.company}</td>
							
								<td>${staffwork.industry}</td>
							
								<td>
								    <c:if test="${staffwork.companyscale eq 0}">小于50人</c:if>
								    <c:if test="${staffwork.companyscale eq 1}">50-100人</c:if>
									<c:if test="${staffwork.companyscale eq 2}">100-500人</c:if>
									<c:if test="${staffwork.companyscale eq 3}">500-1000人</c:if>
									<c:if test="${staffwork.companyscale eq 4}">1000-5000人</c:if>
									<c:if test="${staffwork.companyscale eq 5}">大于5000人</c:if>
								</td>
							
								<td><c:if test="${staffwork.companynature eq 1}">国企</c:if>
								    <c:if test="${staffwork.companynature eq 2}">民企</c:if>
									<c:if test="${staffwork.companynature eq 3}">外企</c:if></td>
							
								<td>${staffwork.department}</td>
							
								<td>${staffwork.position}</td>
							
								<td>${staffwork.workdesc}</td>
							
								<td><fmt:formatDate pattern="yyyy-MM-dd" value="${staffwork.lastmodifydate}"/></td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>