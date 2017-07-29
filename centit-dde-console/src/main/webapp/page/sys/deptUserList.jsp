<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>

<div class="pageHeader">
    <form id="pagerForm" onsubmit="return navTabSearch(this);"
          action="${pageContext.request.contextPath}/sys/userDef!listUserInfo.do" method="post">

        <input type="hidden" name="unitcode" value="${param['unitcode'] }"/>
        <input type="hidden" name="pageNum" value="1"/>
        <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}"/>
        <input type="hidden" name="orderField" value="${param['orderField'] }" />
        <input type="hidden" name="orderDirection" value="${param['orderDirection'] }" />



        <div class="searchBar">
            <ul class="searchContent">
                <li><label>用户名:</label> <s:textfield name="s_username" value="%{#parameters['s_username']}" /></li>
                <li><label>登录名:</label> <s:textfield name="s_loginname" value="%{#parameters['s_loginname']}" /></li>
                <li><label>所属机构：</label>
                <%-- <select name="s_byUnderUnit" class="combox">
                    <option value="">选择所属机构</option>
                    <c:forEach var="row" items="${unitList}">
                        <option value="<c:out value='${row.unitcode}'/>"
                                <c:if test="${row.unitcode==param.s_byUnderUnit}">selected="selected"</c:if>>
                            <c:out value="${row.unitname}" /></option>
                    </c:forEach>

                </select> --%>

                <c:set var="underUnit" value="-选择所属机构-"/>

                <c:forEach var="row" items="${unitList}">
                    <c:if test="${row.unitcode eq param.s_byUnderUnit}"> <c:set var="underUnit" value="${row.unitname}" /> </c:if>
                </c:forEach>
                <ui:tree id="tree_unit" inputValue="${param.s_byUnderUnit }" idKey="unitcode" items="${unitListJson}" name="s_byUnderUnit" parentKey="parentunit"
                             showValue='${pageScope.underUnit }' valueKey="unitname" basePath="${pageContext.request.contextPath}"/>
                </li>
                <li><input type="checkbox" name="s_isAll" <c:if test="${param['s_isAll']}">checked="checked" </c:if> value="true" />包含禁用</li>
                <li><input type="checkbox" name="s_queryUnderUnit" <c:if test="${param['s_queryUnderUnit']}">checked="checked" </c:if> value="true">包含下属机构</li>

            
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

<div class="pageContent" style="margin-top:1px;">
    <c:set var="panel_buttons">
        <a class="add" href="${pageContext.request.contextPath }/sys/userDef!builtUnderUnit.do?tabid=external_DEPTUR"
           target="dialog" rel="userDefForm" width="480" height="380"><span>新增部门用户</span></a>
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
	               <th align="center" orderField="usercode">用户代码</th>
	               <th align="center" orderField="username">用户名</th>
	               <th align="center" orderField="loginname">登录名</th>
	               <th align="center" orderField="isvalid">状态</th>
	               <th align="center">用户描述</th>
	               <th align="center">操作</th>
	           </tr>
            </thead>
            <tbody align="center">
	            <c:forEach items="${objList}" var="sysUser">
	
	                <tr>
	                    <td title="${sysUser.usercode}">${sysUser.usercode}</td>
	                    <td title="${sysUser.username}">${sysUser.username}</td>
	                    <td title="${sysUser.loginname}">${sysUser.loginname}</td>
	                    <td >${USE_STATE[sysUser.isvalid]}</td>
	                    <td title="${sysUser.userdesc}">${sysUser.userdesc}</td>
	                    <td align="center">
                            <a href="${pageContext.request.contextPath }/sys/userDef!viewUnderUnit.do?usercode=${sysUser.usercode}&ec_p=${ec_p}&ec_crd=${ec_crd}&tabid=external_DEPTUR"
                               target="navTab" rel="userDefView" title="查看用户明细">
                                <span class="icon icon-search"></span>
                            </a>

                            <%--<a class="icon" href="${pageContext.request.contextPath }/sys/deptManager!viewUserRole.do?userinfo.usercode=${sysUser.usercode}&ec_p=${ec_p}&ec_crd=${ec_crd}"
                               target="navTab" rel="DEPT_USER_ROLE" title="部门用户权限管理">
                                <span class="icon icon-key"></span>
                            </a>--%>

                            <a href="${pageContext.request.contextPath }/sys/userDef!editUnderUnit.do?usercode=${sysUser.usercode}&tabid=external_DEPTUR"
                               target="dialog" rel="userDefForm" width="480" height="380" title="编辑用户信息" resizable="false" maxable="false">
                                <span class="icon icon-edit"></span>
                            </a>

                            <c:if test="${sysUser.isvalid != 'T' }">
                                <a href="${pageContext.request.contextPath }/sys/userDef!renew.do?usercode=${sysUser.usercode}&tabid=external_DEPTUR"
                                   target="ajaxTodo" title="确定要启用吗？">
                                    <span title="启用" class="icon icon-unlocked"></span>
                                </a>
                            </c:if>

                            <c:if test="${sysUser.isvalid == 'T' }">
                                <a href="${pageContext.request.contextPath }/sys/userDef!delete.do?usercode=${sysUser.usercode}&tabid=external_DEPTUR"
                                   target="ajaxTodo" title="确定要禁用吗？">
                                    <span title="禁用" class="icon icon-locked"></span>
                                </a>
                            </c:if>
	                    </td>
	                </tr>
	            </c:forEach>
            </tbody>
        </table>
</div>

<%@ include file="../common/panelBar.jsp"%>




