<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageHeader">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath}/sys/deptManager!listrole.do" method="post">
        <input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
        <input type="hidden" name="orderField" value="${param['orderField'] }" />
        <input type="hidden" name="orderDirection" value="${param['orderDirection'] }" />

		<div class="searchBar">
			<ul class="searchContent">
				<li><label>部门角色名:</label> <s:textfield name="s_ROLENAME" value="%{#parameters['s_ROLENAME'] }" /></li>
				<li><label>包含禁用:</label> <s:checkbox label="包含禁用" name="s_isAll" value="%{#parameters['s_isAll'] }" fieldValue="true" /></li>
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
	
	<c:set var="panel_buttons">
		<a class="add" href="${pageContext.request.contextPath }/sys/deptManager!builtDeptRole.do" 
			target="navTab" rel="userDefView" title="新增部门角色">
			<span>新增部门角色</span>
		</a>
	</c:set>

	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		<%@ include file="../common/panelBar.jsp"%>
		<table class="list" width="100%" layoutH=".pageHeader 54">
	</c:if>
	
	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		<table class="list" width="100%" layoutH=".pageHeader 27">
	</c:if>
			<thead align="center">
				<tr>
					<th orderField="rolecode">角色代码</th>
					<th orderField="rolename">角色名</th>
					<th orderField="isvalid">状态</th>
					<th>角色描述</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${fRoleinfos}" var="role">

					<tr>
						<td title="${role.rolecode}">${role.rolecode}</td>
						<td title="${role.rolename}">${role.rolename}</td>
						<td><c:out value="${USE_STATE[role.isvalid]}" escapeXml="false" />  </td>
						<td title="${role.roledesc}">${role.roledesc}</td>
						<td>

                            <a href="${pageContext.request.contextPath }/sys/userDef!roleUserList.do?s_rolecode=${role.rolecode}&ec_p=${ec_p}&ec_crd=${ec_crd}&dept=1&tabid=deptUserRoleList"
                               rel="deptUserRoleList"
                               target='navTab' title="角色用户信息">
                                <span class="icon icon-search"></span>
                            </a>


							<a href="${pageContext.request.contextPath }/sys/deptManager!editDeptRole.do?roleinfo.rolecode=${role.rolecode}" 
								target="navTab" rel="userDefView" title="编辑部门角色">
								<span class="icon icon-edit"></span>
							</a>
							
							<c:if test="${role.rolecode != 'SYSADMIN'}">
								<c:if test="${role.isvalid eq 'T'}">
									<s:a action="deptManager!deleteDeptRole.do" styleopt="" target="ajaxTodo" title="确定要禁用该部门角色吗？">
										<s:param name="roleinfo.rolecode">${role.rolecode }</s:param>
                                        <span class="icon icon-locked" title="禁用部门角色"></span>
									</s:a>
								</c:if>
								<c:if test="${role.isvalid eq 'F'}">
									<s:a action="deptManager!renewDeptRole.do" target="ajaxTodo" title="确定要启用该部门角色吗？">
										<s:param name="roleinfo.rolecode">${role.rolecode }</s:param>
										<span class="icon icon-unlocked" title="启用部门角色"></span></s:a>
								</c:if>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
</div>

<%@ include file="../common/panelBar.jsp"%>
