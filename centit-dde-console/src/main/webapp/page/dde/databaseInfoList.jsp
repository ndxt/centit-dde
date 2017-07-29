<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="${pageContext.request.contextPath }/dde/databaseInfo!list.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden"
		name="numPerPage" value="${pageDesc.pageSize}" /> <input
		type="hidden" name="orderField" value="${s_orderField}" />
</form>



<div class="pageHeader">
	<form id="pagerForm" onsubmit="return navTabSearch(this);"
		action="${pageContext.request.contextPath }/dde/databaseInfo!list.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				<li><label>数据库名称:</label> <s:textfield name="s_databaseName"
						value="%{#parameters['s_databaseName']}" /></li>
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
     <c:set var="adddatabase">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                  <a class="add" href="${contextPath }/dde/databaseInfo!add.do"  target='dialog' width="600" height="400"><span>添加数据库</span></a>                   
                </li>
            </ul>
        </div>
    </c:set>
    ${adddatabase}
    
		<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		  <%@ include file="../common/panelBar.jsp"%>
		  <table class="list" width="100%" layoutH=".pageHeader 82">
	    </c:if>
	
	   <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		  <table class="list" width="100%" layoutH=".pageHeader 27">
	   </c:if>
			<thead align="center">
				<tr>
					<th>数据库标识</th>
					<th>数据库名</th>
					<th>数据库类型</th>
					<th>数据库连接url</th>
					<th>业务系统名称</th>
					<th>用户名</th>
					<!-- <th>密码</th> -->
					<th>创建时间</th>
					<th>创建人员</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${objList}" var="databaseInfo">
					<tr>
						<td title="${databaseInfo.databaseName}">${databaseInfo.databaseName}</td>
						<td title="${databaseInfo.dataDesc}">${databaseInfo.dataDesc}</td>
						<td>
						   <c:if test="${databaseInfo.databaseType eq '1'}">SQLSERVER</c:if>
						   <c:if test="${databaseInfo.databaseType eq '2'}">ORACLE</c:if>
						   <c:if test="${databaseInfo.databaseType eq '3'}">DB2</c:if>
						   <c:if test="${databaseInfo.databaseType eq '4'}">ACCESS</c:if>
						   <c:if test="${databaseInfo.databaseType eq '5'}">MYSQL</c:if>
						</td>
						<td title="${databaseInfo.databaseUrl}">${databaseInfo.databaseUrl}</td>
						<td>
							<%--<c:if test="${not empty databaseInfo.osInfo}">
								${databaseInfo.osInfo.osName }
							</c:if>--%>


						</td>
						<td title="${databaseInfo.username}">${databaseInfo.username}</td>
						<%-- <td>${databaseInfo.password}</td> --%>
						<td title="${databaseInfo.createTime}">${databaseInfo.createTime}</td>
						<td title="${cp:MAPVALUE("usercode",databaseInfo.created)}">${cp:MAPVALUE("usercode",databaseInfo.created)}</td>
						<td>
						   <%-- <a  href="${contextPath }/dde/databaseInfo!add.do"  target='dialog' width="600" height="400"><span class="icon icon-add"></span></a> --%>
						   <a  href="${contextPath }/dde/databaseInfo!edit.do?databaseName=${databaseInfo.databaseName}"  rel="" target='dialog' width="600" height="400"><span class="icon icon-edit"></span></a>
						   <a  href="${contextPath }/dde/databaseInfo!delete.do?databaseName=${databaseInfo.databaseName}"  target="ajaxTodo" title="确定要删除吗?"><span class="icon icon-trash"></span></a>
						</td>

					</tr>
				</c:forEach>
			</tbody>
		</table>
</div>
<%@ include file="../common/panelBar.jsp"%>