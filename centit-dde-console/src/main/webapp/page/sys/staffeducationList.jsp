<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="staffeducation.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> <input type="hidden" name="orderField"
		value="${s_orderField}" />
</form>


<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="edit" href="${contextPath }/sys/staffeducation!edit.do?id={pk}" warn="请选择一条记录" rel="" target='dialog' width="600" height="400"><span>编辑</span></a></li>
			<li><a class="delete" href="${contextPath }/sys/staffeducation!delete.do?id={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div>

	<div layoutH="217">
		<table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
			<thead align="center">

				<tr>
					<th>序号</th>
					<th>接受教育开始时间</th>
					<th>接受教育结束时间</th>
					<th>学校名称</th>
					<th>专业</th>
					<th>学历</th>
					<th>证明人</th>
					<th>专业描述</th>
					<th>是否是海外学习经历</th>
					<th>最后修改时间</th>					
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${objList }" var="staffeducation" varStatus="s">
						<tr target="pk" rel="${staffeducation.id}">
						
								<td>${s.index+1}</td>
								
								<td><fmt:formatDate pattern="yyyy-MM-dd" value="${staffeducation.educatebegin}"/></td>
							
								<td><fmt:formatDate pattern="yyyy-MM-dd" value="${staffeducation.educateend}"/></td>
							
								<td>${staffeducation.schoolname}</td>
							
								<td>${staffeducation.speciality}</td>
							
								<td>
									<c:if test="${staffeducation.edubackground eq 1}">小学</c:if>
									<c:if test="${staffeducation.edubackground eq 2}">初中</c:if>
									<c:if test="${staffeducation.edubackground eq 3}">高中</c:if>
									<c:if test="${staffeducation.edubackground eq 4}">大专</c:if>
									<c:if test="${staffeducation.edubackground eq 5}">本科</c:if>
									<c:if test="${staffeducation.edubackground eq 6}">硕士</c:if>
									<c:if test="${staffeducation.edubackground eq 7}">博士</c:if>   
								</td>
							
								<td>${staffeducation.certifier}</td>
							
								<td>${staffeducation.specialitydesc}</td>
							
								<td><c:if test="${staffeducation.isabroad eq 1}">否</c:if><c:if test="${staffeducation.isabroad eq 2}">是</c:if></td>
							
								<td><fmt:formatDate pattern="yyyy-MM-dd" value="${staffeducation.lastmodifydate}"/></td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>
