<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageHeader">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath}/sys/userDef!roleUserList.do" method="post">
		<input type="hidden" name="pageNum" value="1" /> 
		<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
		<input type="hidden" name="orderField" value="${param['orderField'] }" /> 
		<input type="hidden" name="orderDirection" value="${param['orderDirection'] }" />
		<input type="hidden" name="s_rolecode" value="${param['s_rolecode'] }" />

	
		<div class="searchBar">
			<ul class="searchContent">
				<li>
					<label>用户名222:</label> 
					<s:textfield name="s_USERNAME" value="%{#parameters['s_USERNAME']}" />
				</li>
				
				<li>
					<label>登录名:</label> 
					<s:textfield name="s_LOGINNAME" value="%{#parameters['s_LOGINNAME']}"/>
				</li>
				
				<%--<li>
					<s:checkbox name="s_isAll" value="#parameters['s_isAll']" fieldValue="true" />包含禁用
				</li>--%>
			</ul>

			<div class="subBar">
				<ul>
					<li>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查询</button>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</form>
</div>


<div class="pageContent">

    <c:set var="roleUserAddHref" value=""/>
    <c:choose>
        <c:when test="${not empty param['dept']}">
            <c:set var="roleUserAddHref" value="deptManager!deptRoleUserAdd.do"/>
        </c:when>
        <c:otherwise>
            <c:set var="roleUserAddHref" value="userDef!roleUserAdd.do" />
        </c:otherwise>

    </c:choose>


	<c:set var="panel_buttons">
		<a class="add" href="${pageContext.request.contextPath }/sys/${roleUserAddHref }?s_rolecode=${param['s_rolecode'] }&tabid=${param['tabid'] }"
			target="dialog" rel="roleUserAdd" width="480" height="680"><span>新增/删除角色用户</span></a>
	</c:set>
	
	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		<%@ include file="../common/panelBar.jsp"%>
		<table class="list" width="100%" layoutH=".pageHeader 54">
	</c:if>
	
	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		<table class="list" width="100%" layoutH=".pageHeader 27">
	</c:if>
	
		<thead>
			<tr>
				<th width="100" align="center" orderField="usercode">用户代码</th>
				<th width="100" align="center" orderField="username">用户名</th>
				<th width="100" align="center" orderField="loginname">登录名</th>
<!-- 					<th width="100" align="center" orderField="userorder">用户排序号</th> -->
				<th width="70" align="center" orderField="isvalid">状态</th>
				<th width="150" align="center">用户描述</th>
				<th width="70" align="center">操作</th>
			</tr>
		</thead>
		<tbody align="center">
			<c:forEach items="${objList}" var="fUserinfo">
				<tr target="sid_user" rel="${fUserinfo.usercode}">
					<td title="${fUserinfo.usercode}">${fUserinfo.usercode}</td>
					<td title="${fUserinfo.username}">${fUserinfo.username}</td>
					<td title="${fUserinfo.loginname}">${fUserinfo.loginname}</td>
<%-- 						<td title="${fUserinfo.userorder}">${fUserinfo.userorder}</td> --%>
					<td>${USE_STATE[fUserinfo.isvalid]}</td>
					<td title="${fUserinfo.userdesc}">
						${fUserinfo.userdesc}
					</td>
					<td>
						<a href="${pageContext.request.contextPath }/sys/userDef!view.do?usercode=${fUserinfo.usercode}&ec_p=${ec_p}&ec_crd=${ec_crd}&tabid=${param['tabid']}" 
							target="navTab" rel="userDefView" title="查看用户明细">
							<span class="icon icon-search"></span>
						</a>


                        <a href="${pageContext.request.contextPath }/sys/userDef!delete.do?usercode=${fUserinfo.usercode}&s_rolecode=${param['s_rolecode'] }"
                           target="ajaxTodo" title="确定要删除吗？">
                            <span title="删除" class="icon icon-remove"></span>
                        </a>

					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
<%@ include file="../common/panelBar.jsp"%>
</div>

