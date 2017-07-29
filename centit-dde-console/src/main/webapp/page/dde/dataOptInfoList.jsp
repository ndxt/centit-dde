<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="${pageContext.request.contextPath }/dde/dataOptInfo!list.do">
	<input type="hidden" name="pageNum" value="1" /> 
	<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> 
	<input type="hidden" name="orderField" value="${s_orderField}" />
</form>



<div class="pageHeader">
	<s:form id="pagerForm" onsubmit="return navTabSearch(this);" action="/dde/dataOptInfo!list.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				
					<li><label>处理名称:</label> <s:textfield name="s_dataOptId" value="%{#parameters['s_dataOptId']}" /></li>
				
			</ul>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<input type="submit" value="查询"/>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</s:form>
</div>

<div class="pageContent">
	<c:set var="dialog_height"> height="600" </c:set>
	<c:set var="addmapinfo">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                  <a class="add" href="${contextPath }/dde/dataOptInfo!edit.do" warn="请选择一条记录" target='navTab' ${dialog_height } rel="export_sql_add"><span>添加</span></a>                   
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
					<th>数据处理操作ID</th>
					<th>处理名称</th>
					<th>创建人员</th>
					<th>创建时间</th>
					<th>最后更新时间</th>
					<th>操作</th>									
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${objList }" var="obj" varStatus="s">
						<tr>
							    <td>${s.index+1}<input type="hidden" name="dataOptId" id="dataOptId" value="${obj.dataOptId}"/></td>
							    
								<%-- <td>${obj.importId}</td> --%>
								
								<td title="${obj.dataOptId }">${obj.dataOptId }</td>
								<td title="${obj.optName}">${obj.optName}</td>
								<td>${cp:MAPVALUE('usercode', obj.created) }</td>
								<td> <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}" /> </td>
								<td> <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.lastUpdateTime}" /> </td>
								
								
								<td>
								    <a href="${contextPath }/dde/dataOptInfo!edit.do?dataOptId=${obj.dataOptId}&tabid=external_DATAOPTINFO" ${dialog_height } target='navTab' rel="dygxmx"  title="编辑数据导出内容"><span class="icon icon-edit"></span></a>
								    <a href="${contextPath }/dde/dataOptInfo!delete.do?dataOptId=${obj.dataOptId}" target="ajaxTodo" title="删除数据导出内容"><span class="icon icon-trash"></span></a>
								    <a href="${contextPath }/dde/dataOptInfo!edit.do?dataOptId=${obj.dataOptId}&type=copy&tabid=external_DATAOPTINFO" rel="list_field" target="navTab" title="复制导出文件配置"><span class="icon icon-copy"></span></a>
								</td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>

