<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="${pageContext.request.contextPath }/dde/exportSql!list.do">
	<input type="hidden" name="pageNum" value="1" /> 
	<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> 
	<input type="hidden" name="orderField" value="${s_orderField}" />
</form>


<div class="pageHeader">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath }/dde/exportSql!list.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">			
					<li><label>导出名称:</label> <input type="text" name="s_exportName" value="${param.s_exportName }" /></li>				
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

<div id="div_export_sql_page_content" class="pageContent">
	<c:set var="dialog_height"> height="600" </c:set>
	<c:set var="addmapinfo">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                  <a class="add" href="${contextPath }/dde/exportSql!listField.do" warn="请选择一条记录" target='navTab' ${dialog_height } rel="export_sql_add"><span>添加</span></a>                   
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
					<th>导出名称</th>
					<th>源数据库名</th>
					<th>业务系统ID</th>
					<th>创建时间</th>
					<th>创建人员</th>
					<th>导出说明</th>
					<th>操作</th>									
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${objList }" var="obj" varStatus="s">
						<tr>
							    <td>${s.index+1}<input type="hidden" name="exportId" id="exportId" value="${obj.exportId}"/></td>
							    
								<%-- <td>${obj.exportId}</td> --%>
								
								<td title="${obj.dataOptId}">${obj.dataOptId}</td>
								<td title="${obj.exportName}">${obj.exportName}</td>
							
								<td title="${obj.sourceDatabaseName}">${obj.sourceDatabaseName}</td>
								
								<td title="${obj.sourceOsId}">${obj.sourceOsId}</td>
							
							
								<%-- <td>${obj.querySql}</td> --%>
							
								<td> <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${obj.createTime}" /> </td>
							
							
								<td>${cp:MAPVALUE('usercode', obj.created) }</td>
								
								<td title="${obj.exportDesc}">${obj.exportDesc}</td>
								
								<td>
								    <a href="${contextPath }/dde/exportSql!edit.do?exportId=${obj.exportId}&tabid=external_EXPORTSQL" ${dialog_height } target='navTab' rel="dygxmx"  title="编辑数据导出内容"><span class="icon icon-edit"></span>
								    </a>
								    <a href="${contextPath }/dde/exportSql!delete.do?exportId=${obj.exportId}" target="ajaxTodo" title="删除数据导出内容"><span class="icon icon-trash"></span>
								    </a>
								    <a href="${contextPath }/dde/exportSql!edit.do?exportId=${obj.exportId}&type=copy&tabid=external_EXPORTSQL" rel="list_field" target="navTab" title="复制导出文件配置"><span class="icon icon-copy"></span>
								    </a>
								</td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>