<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath }/sys/optRunRec!list.do" method="post" id="pagerForm">
		<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
        <input type="hidden" name="orderField" value="${param['orderField'] }" />
        <input type="hidden" name="orderDirection" value="${param['orderDirection'] }" />
		<div class="searchBar">
			<ul class="searchContent">
				<li><label>Action名:</label> <input type="text" name="s_actionurl" value="${param['s_actionurl'] }" /></li>
				<li><label>方法名:</label> <input type="text" name="s_funcname" value="${param['s_funcname'] }" /></li>
				<li><label>最后请求时间:</label> <input type="text" name="s_lastreqtime" readonly="true" class="date" format="yyyy-MM-dd" yearstart="-20" yearend="5"
					value="${param['s_lastreqtime'] }" /></li>
			</ul>

			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">检索</button>
							</div>
						</div></li>
				</ul>

			</div>
		</div>
	</form>
</div>
<div class="pageContent">
	<%-- <div class="panelBar">
		<ul class="toolBar">
			<li><a class="edit" href="${pageContext.request.contextPath }/sys/roleDef!newEdit.do?rolecode={sid_user}&ec_p=${ec_p}&ec_crd=${ec_crd}" warn="请选择一个用户" target='navTab'><span>编辑</span></a></li>
			<li><a class="delete" href="${pageContext.request.contextPath }/sys/roleDef!delete.do?rolecode={sid_user}" target="ajaxTodo" title="确定要禁用吗？" warn="请选择一个用户"><span>禁用</span></a></li>
			<li><a class="add" href="${pageContext.request.contextPath }/sys/roleDef!built.do" target="navTab"><span>新增</span></a></li>

			<li class="line">line</li>

		</ul>
	</div> --%>
    <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		<%@ include file="../common/panelBar.jsp"%>
		<table class="list" width="100%" layoutH=".pageHeader 54" targetType="navTab" asc="asc" desc="desc">
	</c:if>
	
	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		<table class="list" width="100%" layoutH=".pageHeader 27">
	</c:if>
			<thead>

				<tr>
					<th align="center">序号</th>
					<th align="center" orderField="actionurl">Action名</th>
					<th align="center" orderField="funcname">方法名</th>
					<th align="center" orderField="reqtimes">请求次数</th>
					<th align="center" orderField="lastreqtime">最后请求时间</th>
					<!-- <th align="center">操作</th> -->

				</tr>
			</thead>
			<tbody>
				<c:forEach items="${objList}" var="obj" varStatus="s">

					<tr target="log_id">
						<td align="center">${s.index + 1}</td>
						<td align="center">${obj.actionurl }</td>
						<td align="center">${obj.funcname }</td>
						<td align="center">${obj.reqtimes }</td>
						<td align="center">${obj.lastreqtime}</td>

					</tr>
				</c:forEach>



			</tbody>
		</table>
<!-- 	</div> -->
</div>

<%@ include file="../common/panelBar.jsp"%>



