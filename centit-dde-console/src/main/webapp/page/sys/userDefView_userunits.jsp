<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form action="${pageContext.request.contextPath }/sys/userDef!viewUserunits.do?usercode=${object.usercode }" 
	id="pagerForm" method="post">
	<input type="hidden" name="orderField" value="${param['orderField'] }" /> 
	<input type="hidden" name="orderDirection" value="${param['orderDirection'] }" />
</form>

<div class="pageContent">
    <c:if test="${not ('O' eq AgencyMode.datavalue) }">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                    <a href="${pageContext.request.contextPath }/sys/userDef!addUserUnit.do?usercode=${usercode}"
                       class="add" target="dialog" rel="userDefView" width="510" height="360">
                        <span>添加用户机构</span>
                    </a>
                </li>
            </ul>
        </div>
        
        <table class="list" width="100%" layoutH=".panel 99">
    </c:if>
    
    <c:if test="${('O' eq AgencyMode.datavalue) }">
        <table class="list" width="100%" layoutH=".panel 45">
    </c:if>

		<thead>
			<tr>
				<th align="center">机构代码</th>
				<th align="center">机构名</th>
				<th align="center">用户岗位</th>
				<th align="center">行政职务</th>
				<th align="center">是否主单位</th>
				<th align="center">授权说明</th>
				<th align="center">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${userunits}" var="fUserunit">

				<tr>
					<td title="${fUserunit.unitcode}" align="center">${fUserunit.unitcode}</td>
					<td title="${cp:MAPVALUE('unitcode',fUserunit.unitcode)}" align="center">${cp:MAPVALUE('unitcode',fUserunit.unitcode)}</td>
					<td title="${cp:MAPVALUE('StationType',fUserunit.userstation)}" align="center">${cp:MAPVALUE('StationType',fUserunit.userstation)}</td>
					<td title="${cp:MAPVALUE('RankType',fUserunit.userrank)}" align="center">${cp:MAPVALUE('RankType',fUserunit.userrank)}</td>
					<td title="${YES_NO[fUserunit.isprimary]}" align="center">${YES_NO[fUserunit.isprimary]}</td>
					<td align="center" title="${fUserunit.rankmemo}">${fUserunit.rankmemo}</td>
					<td align="center">
						<a href="userDef!editUserUnit.do?userUnit.usercode=${usercode}&userUnit.unitcode=${fUserunit.unitcode}&userUnit.userstation=${fUserunit.userstation}&userUnit.userrank=${fUserunit.userrank}"
							class="edit" target="dialog" title="编辑用户机构" rel="userDefView" width="510" height="360">
							<span class="icon icon-edit"></span>
						</a>
						
						<c:if test="${fUserunit.isprimary != 'T' }">
							<a href="userDef!deleteUserUnit.do?userUnit.usercode=${usercode}&userUnit.unitcode=${fUserunit.unitcode}&userUnit.userstation=${fUserunit.userstation}&userUnit.userrank=${fUserunit.userrank}" 
								class="delete" target="ajaxTodo" title="确定要删除用户机构吗？">
								<span class="icon icon-trash" title="删除用户机构"></span>
							</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

<c:if test="${not ('O' eq AgencyMode.datavalue) }">
	<div class="panelBar">
		<ul class="toolBar">
			<li class="new">
				<a href="${pageContext.request.contextPath }/sys/userDef!addUserUnit.do?usercode=${usercode}" 
					class="add" target="dialog" rel="userDefView" width="510" height="360">
					<span>添加用户机构</span>
				</a>
			</li>
		</ul>
	</div>
    </c:if>
</div>



