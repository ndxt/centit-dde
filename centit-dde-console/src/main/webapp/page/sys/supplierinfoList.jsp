<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="supplierinfo.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> <input type="hidden" name="orderField"
		value="${s_orderField}" />
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="edit" href="${contextPath }/sys/supplierinfo!edit.do?id={pk}" warn="请选择一条记录" rel="" target='dialog'><span>编辑</span></a></li>
			<li><a class="delete" href="${contextPath }/sys/supplierinfo!delete.do?id={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div>

	<div layoutH="217">
		<table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
			<thead align="center">

				<tr>
					<th>序号</th>
					<th>注册资本</th>
					<th>公司规模</th>
					<th>业务范围</th>
					<th>经验和成功案例</th>
					<th>最后修改时间</th>
					
				</tr>
			</thead>
			<tbody align="center">
			${supplierinfos }
				<c:forEach items="${supplierinfo }" var="supplierinfo" varStatus="s">
						<tr target="pk" rel="${supplierinfo.id}">
							
								<td>${s.index+1}</td>
							
								<td>${supplierinfo.registeredcapital}</td>
							
								<td> 
									<c:if test="${supplierinfo.companyscale eq 0}">小于50人</c:if>
								    <c:if test="${supplierinfo.companyscale eq 1}">50-100人</c:if>
									<c:if test="${supplierinfo.companyscale eq 2}">100-500人</c:if>
									<c:if test="${supplierinfo.companyscale eq 3}">500-1000人</c:if>
									<c:if test="${supplierinfo.companyscale eq 4}">1000-5000人</c:if>
									<c:if test="${supplierinfo.companyscale eq 5}">大于5000人</c:if>
								</td>
							
								<td>${supplierinfo.businessscope}</td>
							
								<td>${supplierinfo.successcase}</td>
							
								<td><fmt:formatDate pattern="yyyy-MM-dd" value="${supplierinfo.lastmodifydate}"/></td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>