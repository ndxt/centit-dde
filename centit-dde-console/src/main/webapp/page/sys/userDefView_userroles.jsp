<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<%
    pageContext.setAttribute("myDate", new java.util.Date());
%>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li class="new">
				<a href="${pageContext.request.contextPath }/sys/userDef!bulitUserRole.do?usercode=${usercode}"
					class="add" target="dialog" rel="userDefView" width="510" height="360">
					<span>添加用户角色</span>
				</a>
			</li>
		</ul>
	</div>
	
	<table class="list" width="100%" layoutH=".panel 99">
		<thead>
			<tr>
				<th align="center">角色代码</th>
				<th align="center">角色名</th>
				<th align="center">获取时间</th>
				<th align="center">失效时间</th>
				<th align="center">授权说明</th>
				<th align="center">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userroles}" var="role">
				<tr align="center">
					<td title="${role.rolecode}">${role.rolecode}</td>
					<td title="${cp:MAPVALUE('rolecode',role.rolecode)}">${cp:MAPVALUE('rolecode',role.rolecode)}</td>
					<td><fmt:formatDate value="${role.obtaindate }" pattern="yyyy-MM-dd"/></td>
					<td><fmt:formatDate value="${role.secededate }" pattern="yyyy-MM-dd"/></td>
					<td title="${userrole.changedesc }">${role.changedesc }</td>
					<td align="center">
						<a href="${pageContext.request.contextPath }/sys/userDef!editUserRole.do?usercode=${role.usercode}&userrole.id.rolecode=${role.rolecode}&obtaindate=${role.obtaindate}" 
							class="edit" target="dialog" title="编辑用户角色" rel="userDefView" width="510" height="360">
							<span title="编辑用户角色" class="icon icon-edit"></span>
						</a>
						<a href="${pageContext.request.contextPath }/sys/userDef!deleteUserRole.do?userrole.id.usercode=${role.usercode}&userrole.id.rolecode=${role.rolecode}&obtaindate=${role.obtaindate}" 
							class="delete" target="ajaxTodo" title="确定要删除用户角色吗？">
							<span title="删除用户角色" class="icon icon-trash"></span>
						</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div class="panelBar">
		<ul class="toolBar">
			<li class="new">
				<a href="${pageContext.request.contextPath }/sys/userDef!bulitUserRole.do?usercode=${usercode}" 
					class="add" target="dialog" rel="userDefView" width="510" height="440">
					<span>添加用户角色</span>
				</a>
			</li>
		</ul>
	</div>
</div>


