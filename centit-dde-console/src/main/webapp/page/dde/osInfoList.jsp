<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="${pageContext.request.contextPath }/dde/databaseInfo!list.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> <input type="hidden" name="orderField" value="${s_orderField}" />
</form>



<div class="pageHeader">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath }/dde/osInfo!list.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				<li><label>业务系统ID：</label> <s:textfield name="s_osId" value="%{#parameters['s_osId']}" /></li>
				<li><label>业务系统名称：</label> <s:textfield name="s_osName" value="%{#parameters['s_osName']}" /></li>
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
				<li class="new"><a class="add" href="${contextPath }/dde/osInfo!edit.do" target='dialog' width="600" height="400"><span>添加业务系统</span></a></li>
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
							<th>业务系统ID</th>
							<th>业务系统名称</th>
							<th>是否提供接口</th>
							<th>业务系统接口url</th>
							<!-- <th>密码</th> -->
							<th>创建时间</th>
							<th>创建人员</th>
							
							<th>操作</th>
						</tr>
					</thead>
					<tbody align="center">
						<c:forEach items="${objList}" var="obj">
							<tr>
								<td title="${obj.osId}">${obj.osId}</td>
								<td title="${obj.osName}">${obj.osName}</td>
								<td title="${'T' eq obj.hasInterface ? '是' : '否'}">${'T' eq obj.hasInterface ? '是' : '否'}</td>
								<td title="${obj.interfaceUrl}">${obj.interfaceUrl}</td>
								<%-- <td>${obj.password}</td> --%>
								<td title="${obj.createTime}">${obj.createTime}</td>
								<td title="${cp:MAPVALUE("usercode",obj.created)}">${cp:MAPVALUE("usercode",obj.created)}</td>
								
								<td>
									<%-- <a  href="${contextPath }/dde/databaseInfo!add.do"  target='dialog' width="600" height="400"><span class="icon icon-add"></span></a> --%> <a
									href="${contextPath }/dde/osInfo!edit.do?osId=${obj.osId}" rel="" target='dialog' width="600" height="400"><span class="icon icon-edit"></span></a> <a
									href="${contextPath }/dde/osInfo!delete.do?osId=${obj.osId}" target="ajaxTodo" title="确定要删除吗?"><span class="icon icon-trash"></span></a>
								</td>

							</tr>
						</c:forEach>
					</tbody>
				</table>
				</div>
				<%@ include file="../common/panelBar.jsp"%>


				<%-- <%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="osInfo.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> <input type="hidden" name="orderField"
		value="${s_orderField}" />
</form>



<div class="pageHeader">
	<s:form id="pagerForm" onsubmit="return navTabSearch(this);" action="/pm/osInfo.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				
					<li><label><c:out value="osInfo.osId" />:</label> <s:textfield name="s_osId" value="%{#parameters['s_osId']}" /></li>
				
				
					<li><label><c:out value="osInfo.osName" />:</label> <s:textfield name="s_osName" value="%{#parameters['s_osName']}" /></li>
				
					<li><label><c:out value="osInfo.hasInterface" />:</label> <s:textfield name="s_hasInterface" value="%{#parameters['s_hasInterface']}" /></li>
				
					<li><label><c:out value="osInfo.interfaceUrl" />:</label> <s:textfield name="s_interfaceUrl" value="%{#parameters['s_interfaceUrl']}" /></li>
				
					<li><label><c:out value="osInfo.created" />:</label> <s:textfield name="s_created" value="%{#parameters['s_created']}" /></li>
				
					<li><label><c:out value="osInfo.lastUpdateTime" />:</label> <s:textfield name="s_lastUpdateTime" value="%{#parameters['s_lastUpdateTime']}" /></li>
				
					<li><label><c:out value="osInfo.createTime" />:</label> <s:textfield name="s_createTime" value="%{#parameters['s_createTime']}" /></li>
				
			</ul>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<s:submit method="list"><bean:message key="opt.btn.query" /></s:submit>
							</div>
						</div>
					</li>
					<li>
						<div class="buttonActive">
							<div class="buttonContent">
								<!-- 参数 navTabId 根据实际情况填写 -->
								<button type="button" onclick="javascript:navTabAjaxDone({'statusCode' : 200, 'callbackType' : 'closeCurrent', 'navTabId' : ''});">返回</button>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</s:form>
</div>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${contextPath }/pm/osInfo!edit.do" rel="" target='dialog'><span>添加</span></a></li>
			<li><a class="edit" href="${contextPath }/pm/osInfo!edit.do?osId={pk}" warn="请选择一条记录" rel="" target='dialog'><span>编辑</span></a></li>
			<li><a class="delete" href="${contextPath }/pm/osInfo!delete.do?osId={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div>

	<div layoutH="116">
		<table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
			<thead align="center">

				<tr>
					
						<c:set var="tosId"><bean:message bundle='pmRes' key='osInfo.osId' /></c:set>	
						<th>${tosId}</th>
					
					
						<c:set var="tosName"><bean:message bundle='pmRes' key='osInfo.osName' /></c:set>	
						<th>${tosName}</th>
					
						<c:set var="thasInterface"><bean:message bundle='pmRes' key='osInfo.hasInterface' /></c:set>	
						<th>${thasInterface}</th>
					
						<c:set var="tinterfaceUrl"><bean:message bundle='pmRes' key='osInfo.interfaceUrl' /></c:set>	
						<th>${tinterfaceUrl}</th>
					
						<c:set var="tcreated"><bean:message bundle='pmRes' key='osInfo.created' /></c:set>	
						<th>${tcreated}</th>
					
						<c:set var="tlastUpdateTime"><bean:message bundle='pmRes' key='osInfo.lastUpdateTime' /></c:set>	
						<th>${tlastUpdateTime}</th>
					
						<c:set var="tcreateTime"><bean:message bundle='pmRes' key='osInfo.createTime' /></c:set>	
						<th>${tcreateTime}</th>
					
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${objList }" var="osInfo">
						<tr target="pk" rel="${osInfo.osId}">
							
								<td>${osInfo.osId}</td>
							
							
								<td>${osInfo.osName}</td>
							
								<td>${osInfo.hasInterface}</td>
							
								<td>${osInfo.interfaceUrl}</td>
							
								<td>${osInfo.created}</td>
							
								<td>${osInfo.lastUpdateTime}</td>
							
								<td>${osInfo.createTime}</td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>

 --%>