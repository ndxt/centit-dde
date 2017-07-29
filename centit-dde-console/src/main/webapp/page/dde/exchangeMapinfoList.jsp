<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="${pageContext.request.contextPath }/dde/exchangeMapInfo!list.do">
	<input type="hidden" name="pageNum" value="1" /> 
	<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> 
	<input type="hidden" name="orderField" value="${s_orderField}" />
</form>


<div class="pageHeader">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath }/dde/exchangeMapInfo!list.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">			
					<li><label>交换名称:</label> <s:textfield name="s_mapinfoName" value="%{#parameters['s_mapinfoName']}" /></li>				
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

	<c:set var="addmapinfo">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                  <a class="add" href="${contextPath }/dde/mapInfoDetail!addAndsaveMapinfoDatails.do?s_type=initfirst"
                     warn="请选择一条记录" target='navTab' rel="dygxmx_add"><span>添加</span></a>                   
                </li>
            </ul>
        </div>
    </c:set>
    ${addmapinfo}

	   <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		  <%@ include file="../common/panelBar.jsp"%>
		  <table class="list" width="100%" layoutH=".pageHeader 82">
	    </c:if>
	
	   <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		  <table class="list" width="100%" layoutH=".pageHeader 27">
	   </c:if>
			<thead align="center">
				<tr>
					<th>序号</th>
					<th>交换名称</th>
					<th>源数据库名</th>
					<th>源表名</th>
					<th>目标数据库名</th>
					<th>目标表名</th>
					<th>是否为重复执行</th>
					<th>交换说明</th>
					<th>操作</th>									
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${objList}" var="exchangeMapInfo" varStatus="s">
						<tr>
							    <td>${s.index+1}<input type="hidden" name="mapInfoId" id="mapInfoId" value="${exchangeMapInfo.mapInfoId}"/></td>
							    
								<%-- <td>${exchangeMapInfo.mapInfoId}</td> --%>
								
								<td title="${exchangeMapInfo.mapInfoName}">${exchangeMapInfo.mapInfoName}</td>
							
								<td title="${exchangeMapInfo.sourceDatabaseName}">${exchangeMapInfo.sourceDatabaseName}</td>
								
								<td title="${exchangeMapInfo.sourceTableName}">${exchangeMapInfo.sourceTableName}</td>
							
							
								<%-- <td>${exchangeMapInfo.querySql}</td> --%>
							
								<td title="${exchangeMapInfo.destDatabaseName}">${exchangeMapInfo.destDatabaseName}</td>
							
								<td title="${exchangeMapInfo.destTableName}">${exchangeMapInfo.destTableName}</td>
							
								<td><c:if test="${exchangeMapInfo.isRepeat eq '1'}">是</c:if>
									<c:if test="${exchangeMapInfo.isRepeat eq '0'}">否</c:if>
								</td>
							
								<td title="${exchangeMapInfo.mapInfoDesc}">${exchangeMapInfo.mapInfoDesc}</td>
								
								<td>
								    <a href="${contextPath }/dde/mapInfoDetail!showMapinfoDetail.do?s_mapinfoId=${exchangeMapInfo.mapInfoId}&s_type=initfirst"
								       target='navTab' rel="dygxmx"  title="编辑交换对应关系"><span class="icon icon-edit"></span></a>
								    <a href="${contextPath }/dde/exchangeMapInfo!delete.do?mapInfoId=${exchangeMapInfo.mapInfoId}"
								       target="ajaxTodo" title="删除交换对应关系"><span class="icon icon-trash"></span></a>
								    <a href="${contextPath }/dde/mapInfoDetail!showMapinfoDetail.do?s_mapinfoId=${exchangeMapInfo.mapInfoId}&s_type=initcopy"
								       target="navTab" title="复制交换对应关系"><span class="icon icon-copy"></span></a>
								</td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>