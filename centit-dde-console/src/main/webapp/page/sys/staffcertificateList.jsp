<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="staffcertificate.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> <input type="hidden" name="orderField"
		value="${s_orderField}" />
</form>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="edit" href="${contextPath }/sys/staffcertificate!edit.do?id={pk}" warn="请选择一条记录" rel="" target='dialog' width="600" height="400"><span>编辑</span></a></li>
			<li><a class="delete" href="${contextPath }/sys/staffcertificate!delete.do?id={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div>

	<div layoutH="217">
		<table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
			<thead align="center">

				<tr>
					<th>序号</th>
					<th>证书编号</th>
					<th>证书名称</th>
					<th>证书发放机构</th>
					<th>证书发放时间</th>
					<th>证书描述</th>
					<th>最后修改时间</th>
					
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${staffcertificate}" var="staffcertificate" varStatus="s">
						<tr target="pk" rel="${staffcertificate.id}">
							
								<td>${s.index+1}</td>
							
								<td>${staffcertificate.certificateid}</td>
							
								<td>${staffcertificate.certificatename}</td>
							
								<td>${staffcertificate.certificateorgan}</td>
							
								<td><fmt:formatDate pattern="yyyy-MM-dd" value="${staffcertificate.certificatetime}"/></td>
							
								<td>${staffcertificate.certificatedesc}</td>
							
								<td><fmt:formatDate pattern="yyyy-MM-dd" value="${staffcertificate.lastmodifydate}"/></td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>
