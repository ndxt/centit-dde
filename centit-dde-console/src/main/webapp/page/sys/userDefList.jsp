<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageHeader">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath}/sys/userDef!list.do" method="post">
		<input type="hidden" name="pageNum" value="1" /> 
		<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
		<input type="hidden" name="orderField" value="${param['orderField'] }" /> 
		<input type="hidden" name="orderDirection" value="${param['orderDirection'] }" />

	
		<div class="searchBar">
			<ul class="searchContent">
				<li>
					<label>用户名:</label> 
					<s:textfield name="s_USERNAME" value="%{#parameters['s_USERNAME']}" />
				</li>
				
				<li>
					<label>登录名:</label> 
					<s:textfield name="s_LOGINNAME" />
				</li>
				
				<li>
					<label>所属机构：</label> 
									
					<%--<select name="s_queryByUnit" class="combox">
							<option value="" selected="selected">选择所属机构 </option>
							<c:forEach var="row" items="${cp:LVB('unitcode')}">
								<option value="${row.value}" <c:if test="${row.value eq param['s_queryByUnit']}">selected="selected" </c:if> ><c:out value="${row.label}"/></option>
							</c:forEach>
						</select>--%>
                    <c:set var="underUnit" value="-选择所属机构-"/>

                    <c:forEach var="row" items="${cp:LVB('unitcode')}">
                        <c:if test="${row.value eq param.s_queryByUnit}"> <c:set var="underUnit" value="${row.label}" /> </c:if>
                    </c:forEach>
                    <ui:tree id="tree_unit2" inputValue="${param.s_queryByUnit }" idKey="unitcode" items="${unitListJson}" name="s_queryByUnit" parentKey="parentunit"
                             showValue='${pageScope.underUnit }' valueKey="unitname" basePath="${pageContext.request.contextPath}"/>
					
				</li>

				<li>
					<s:checkbox name="s_isAll" value="#parameters['s_isAll']" fieldValue="true"></s:checkbox>包含禁用
				</li>
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
		<a class="add" href="${pageContext.request.contextPath }/sys/userDef!built.do" 
			target="dialog" rel="userDefForm" width="480" height="380"><span>新增</span></a>
	</c:set>
	
	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		<%@ include file="../common/panelBar.jsp"%>
		<table class="list" width="100%" layoutH=".pageHeader 54">
	</c:if>
	
	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		<table class="list" width="100%" layoutH=".pageHeader 27">
	</c:if>
	
		<thead align="center">
			<tr >
				<th width="100" align="center" style="text-align: center" orderField ="usercode" >用户代码</th>
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
						<a href="${pageContext.request.contextPath }/sys/userDef!view.do?usercode=${fUserinfo.usercode}&ec_p=${ec_p}&ec_crd=${ec_crd}" 
							target="navTab" rel="userDefView" title="查看用户明细">
							<span class="icon icon-search"></span>
						</a>
						
						<a href="${pageContext.request.contextPath }/sys/userDef!edit.do?usercode=${fUserinfo.usercode}&tabid=external_USERMAG" 
							target="dialog" rel="userDefForm" width="480" height="380" title="编辑用户信息">
							<span class="icon icon-edit"></span>
						</a>
						
						<c:if test="${fUserinfo.isvalid != 'T' }">
							<a href="${pageContext.request.contextPath }/sys/userDef!renew.do?usercode=${fUserinfo.usercode}" 
								target="ajaxTodo" title="确定要启用吗？">
								<span title="启用" class="icon icon-unlocked"></span>
							</a>
						</c:if>
						
						<c:if test="${fUserinfo.isvalid == 'T' }">
							<a href="${pageContext.request.contextPath }/sys/userDef!delete.do?usercode=${fUserinfo.usercode}" 
								target="ajaxTodo" title="确定要禁用吗？">
								<span title="禁用" class="icon icon-locked"></span>
							</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
<%@ include file="../common/panelBar.jsp"%>
</div>

