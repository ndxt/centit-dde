<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath }/sys/roleDef!list.do" method="post" id="pagerForm">
		<input type="hidden" name="pageNum" value="1" /> 
		<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
		<input type="hidden" name="orderField" value="${param['orderField'] }" /> 
		<input type="hidden" name="orderDirection" value="${param['orderDirection'] }" />
		
		<div class="searchBar">
			<ul class="searchContent">
				<li><label>角色名:</label> <input type="text" name="s_ROLENAME" value='${s_ROLENAME }' /></li>
				<li><s:checkbox name="s_isAll" value="#parameters['s_isAll']" fieldValue="true"></s:checkbox>包含禁用</li>
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
		<a class="add" href="${pageContext.request.contextPath }/sys/roleDef!built.do" 
			target="navTab" rel="roleDefBuild" title="新增角色"><span>新增</span></a>
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
					<th align="center" orderField="rolecode">角色代码</th>
					<th align="center" orderField="rolename">角色名称</th>
					<th align="center" orderField="isvalid">状态</th>
					<th align="center">角色说明</th>
					<th align="center">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${objList}" var="fRoleinfo">

					<tr target="sid_user" rel="${fRoleinfo.rolecode}">
						<td title="${fRoleinfo.rolecode}" align="center">${fRoleinfo.rolecode}</td>
						<td title="${fRoleinfo.rolename}" align="center">${fRoleinfo.rolename}</td>
						<td title="${cp:MAPVALUE('VALID_STATE', fRoleinfo.isvalid)}" align="center">${USE_STATE[fRoleinfo.isvalid]}</td>
						<td align="center" title="${fRoleinfo.roledesc}">${fRoleinfo.roledesc}</td>
						<td align="center">
							<a href="${pageContext.request.contextPath }/sys/userDef!roleUserList.do?s_rolecode=${fRoleinfo.rolecode}&ec_p=${ec_p}&ec_crd=${ec_crd}&tabid=roleUserDefList"
								rel="roleUserDefList"
								target='navTab' title="角色用户信息">
								<span class="icon icon-search"></span>
							</a>
							
							
							<a href="${pageContext.request.contextPath }/sys/roleDef!newEdit.do?rolecode=${fRoleinfo.rolecode}&ec_p=${ec_p}&ec_crd=${ec_crd}" 
								target='navTab' title="编辑角色">
								<span class="icon icon-edit"></span>
							</a>
							
							<c:if test="${fRoleinfo.isvalid == 'T' }">
								<a href="${pageContext.request.contextPath }/sys/roleDef!delete.do?rolecode=${fRoleinfo.rolecode}" 
									target="ajaxTodo" title="确定要禁用该角色吗？">
									<span class="icon icon-locked" title="禁用角色"></span>
								</a>
							</c:if>
							
							<c:if test="${fRoleinfo.isvalid == 'F' }">
								<a href="${pageContext.request.contextPath }/sys/roleDef!renew.do?rolecode=${fRoleinfo.rolecode}" 
									target="ajaxTodo" title="确定要启用该角色吗？">
									<span class="icon icon-unlocked" title="启用角色"></span>
								</a>
							</c:if>
						</td>
					</tr>
				</c:forEach>



			</tbody>
		</table>
</div>

<%@ include file="../common/panelBar.jsp"%>


