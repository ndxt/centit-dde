<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<link href="<s:url value="/scripts/autocomplete/autocomplete.css"/>" type="text/css" rel="stylesheet">
<script language="javascript" src="<s:url value="/scripts/autocomplete/autocomplete.js"/>" type="text/javascript"></script>
<script language="javascript" src="<s:url value="/scripts/selectUser.js"/>" type="text/javascript"></script>

<script type="text/javascript">
	var list = [];
	<c:forEach var="userinfo" varStatus="status" items="${cp:ALLUSER('T')}">
	list[${status.index}] = { username:'<c:out value="${userinfo.username}"/>',
		loginname:'<c:out value="${userinfo.loginname}"/>',
		usercode:'<c:out value="${userinfo.usercode}"/>',
		pinyin:'<c:out value="${userinfo.nameFisrtLetter}"/>'  };
	</c:forEach>
	function selectUser(obj) {
		userInfo.choose(obj, {dataList:list,userName:$('#userName')});
	}
</script>


<form id="pagerForm" method="post" action="${pageContext.request.contextPath }/dde/dataOptInfo!list.do">
	<input type="hidden" name="pageNum" value="1" /> 
	<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> 
	<input type="hidden" name="orderField" value="${s_orderField}" />
</form>



<div class="pageHeader">
	<s:form id="pagerForm" onsubmit="return navTabSearch(this);" action="/dde/userDataOptId!list.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				
					<li><label>用户代码:</label> <input type="text" name="s_usercode" onclick="selectUser(this);" id="userCode" value="${param.s_usercode }"/></li>
					<li><label>业务操作ID:</label> <input type="text" name="s_dataOptId" value="${param.s_dataOptId }"/></li>

			</ul>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<input type="submit" value="查询"/>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</s:form>
</div>

<div class="pageContent">
	<c:set var="dialog_height"> height="400" </c:set>
	<c:set var="addmapinfo">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                  <a class="add" href="${contextPath }/dde/userDataOptId!edit.do" warn="请选择一条记录" target='dialog' ${dialog_height } rel="export_sql_add"><span>添加</span></a>
                </li>
            </ul>
        </div>
    </c:set>
    ${addmapinfo}

	   <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		  <%@ include file="../common/panelBar.jsp"%>
		  <table class="list" width="100%" layoutH=".pageHeader 82">
	    </c:if>
	
	   <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		  <table class="list" width="100%" layoutH=".pageHeader 27">
	   </c:if>
			<thead align="center">
				<tr>
					<th>序号</th>
					<th>用户</th>
					<th>业务操作ID</th>
					<th>业务操作类型</th>
					<th>创建时间</th>
					<th>最后更新时间</th>
					<th>描述</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${objList }" var="obj" varStatus="s">
						<tr>
							    <td>${s.index+1}</td>
							    
								<td >${cp:MAPVALUE('usercode', obj.usercode)}</td>
								<td title="${obj.dataOptId }">${obj.dataOptId }</td>
								<td >
									<c:if test="${'I' eq obj.dataoptType}">导入</c:if>
									<c:if test="${'E' eq obj.dataoptType}">导出</c:if>
								</td>
								<td> <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createDate}" /> </td>
								<td> <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.lastModifyDate}" /> </td>
								<td title="${obj.describe }">${obj.describe }</td>

								
								<td>
								    <a href="${contextPath }/dde/userDataOptId!edit.do?udId=${obj.udId}" ${dialog_height } target='dialog' rel="dygxmx"  title="编辑数据导出内容"><span class="icon icon-edit"></span></a>
								    <a href="${contextPath }/dde/userDataOptId!delete.do?udId=${obj.udId}" target="ajaxTodo" title="删除数据导出内容"><span class="icon icon-trash"></span></a>
								</td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>

