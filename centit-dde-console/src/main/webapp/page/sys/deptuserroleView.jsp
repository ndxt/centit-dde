<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="panel panelDrag">
	<h1>用户明细</h1>
	<div class="pageFormContent">
		<div class="unit">
			<label>用户工号：</label> 
			<label>${userinfo.usercode}</label>
		</div>
		
		<div class="unit">
			<label>用户名：</label> 
			<label>${userinfo.username}</label>
		</div>
		
		<div class="unit">
			<label>登录名：</label>
			<label>${userinfo.loginname}</label>
		</div>
		
		<div class="subBar">
			<ul>
				<li style="float:right; margin-right:50px;">
					<div class="buttonActive" style="margin-right:5px;">
						<div class="buttonContent">
							<a href="${pageContext.request.contextPath }/sys/userDef!edit.do?usercode=${userinfo.usercode}&tabid=DEPT_USER_ROLE"
                               target="dialog" rel="userDefForm" width="480" height="380" title="编辑用户信息" resizable="false" maxable="false">
                                	编辑用户信息
                            </a>
						</div>
					</div>
					<div class="buttonActive">
						<div class="buttonContent">
							<button type="button" onclick="javascript:navTab.closeCurrentTab('external_DEPTUR')">返回</button>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</div>
</div>

<%
    java.util.Date myDate = new java.util.Date();
    request.setAttribute("myDate", myDate);
%>

<div class="pageContent" style="margin-top:5px;">
	<c:set var="panel_buttons">
		<a class="add" href="${pageContext.request.contextPath }/sys/deptManager!bulitUserRole.do?userrole.usercode=${userinfo.usercode}" 
            target="dialog" rel="userDefView" title="新增用户角色" width="500" height="320">
            <span>新增用户角色</span>
        </a>
	</c:set>

	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		<%@ include file="../common/panelBar.jsp"%>
		<table class="list" width="100%" layoutH=".panel 60">
	</c:if>
	
	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		<table class="list" width="100%" layoutH=".panel 33">
	</c:if>
			<thead>

				<tr>
					<th align="center">用户角色代码</th>
					<th align="center">用户角色名</th>
					<th align="center">获取时间</th>
					<th align="center">失效时间</th>
					<th align="center">授权说明</th>
					<th align="center">操作</th>
				</tr>
			</thead>
			
			<tbody>
				<c:forEach items="${userroles}" var="role">

					<tr>
						<td align="center">${role.rolecode}</td>
						<td align="center">${cp:MAPVALUE('rolecode',role.rolecode)}</td>
						<td align="center">
							<fmt:formatDate value="${role.obtaindate }" pattern="yyyy-MM-dd"/>
						</td>
						<td align="center">
							<fmt:formatDate value="${role.secededate }" pattern="yyyy-MM-dd"/>
						</td>
						<td align="center" title="${role.changedesc }">
							${role.changedesc }
						</td>
						<td align="center">
							<c:if test="${role.obtaindate lt myDate and ( role.secededate == null or role.secededate gt myDate)}">
								<a href='deptManager!editUserRole.do?userrole.rolecode=${role.rolecode}&userrole.usercode=${userinfo.usercode}' 
									target="dialog" width="500" height="320" > 
									<span title="编辑用户角色" class="icon icon-edit"></span> 
								</a>
							</c:if> 
							
							<c:if test="${role.secededate == null or role.secededate lt myDate}">
								<a href='deptManager!deleteUserRole.do?userrole.rolecode=${role.rolecode}&userrole.usercode=${userinfo.usercode}&userrole.obtaindate=${role.obtaindate}'
									target="ajaxTodo" title="是否删除用户部门角色：${role.rolecode}?"> 
									<span title="删除用户角色" class="icon icon-trash"></span> 
								</a>
							</c:if> 
							
							<c:if test="${role.obtaindate lt myDate and ( role.secededate == null or role.secededate gt myDate)}">
								<a href='deptManager!deleteUserRole.do?userrole.rolecode=${role.rolecode}&userrole.usercode=${userinfo.usercode}&userrole.obtaindate=${role.obtaindate}'
									target="ajaxTodo" title="是否回收用户部门角色：${role.rolecode}?"> 
									<span title="回收用户角色" class="icon icon-trash"></span> 
								</a>
							</c:if>
						</td>
					</tr>
				</c:forEach>



			</tbody>
		</table>
</div>

<%@ include file="../common/panelBar.jsp"%>

