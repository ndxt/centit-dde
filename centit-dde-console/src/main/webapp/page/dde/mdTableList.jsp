<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="${pageContext.request.contextPath}/dde/mdTable!list.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
</form>

<div class="pageHeader">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath}/dde/mdTable!list.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				<li><label>表代码：</label> 
				<input type="text" name="s_tbcode" value="${s_tbcode }" />
				
				</li>
				<li><label>表名称：</label> <input type="text" name="s_tbname" value="${s_tbname }" /></li>
				<li style="width: auto;"><label>数据类别：</label> <s:select list="#{'':'全部类别', 'S':'系统', 'O':'业务'}" cssClass="combox" listKey="key" listValue="value"
						value="#request.s_tbstate" name="s_tbstate"></s:select></li>
				<li style="width: auto;"><label>形式类别：</label> <s:select list="#{'':'全部类别', 'T':'表', 'V':'视图'}" cssClass="combox" listKey="key" listValue="value"
						value="#request.s_tbtype" name="s_tbtype"></s:select></li>
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
	<%-- <div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${contextPath }/dde/mdTable!edit.do" target="dialog" rel="" title="新增表信息"><span>新增</span></a></li>
			<li><a class="edit" href="${contextPath }/dde/mdTable!edit.do?tbcode={pk}" warn="请选择一条记录" target="dialog" rel="" title="编辑表信息"><span>编辑</span></a></li>
			<li><a class="delete" href="${contextPath }/dde/mdTable!delete.do?tbcode={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
			<li><a class="icon" href="${contextPath }/dde/mdTable!view.do?tbcode={pk}" warn="请选择一条记录" target='navTab' rel="dyzdmx"><span>查看表明细</span></a></li>
			<li><a class="icon" href="${contextPath }/dde/mdTable!viewrelation.do?tbcode={pk}" warn="请选择一条记录" target='navTab' rel="dybglmx"><span>对应表关联明细</span></a></li>
			<li><a class="icon" href="${contextPath }/dde/datacompare!importdatabase.do" target='navTab' rel="dybglmx"><span>数据库比对</span></a></li>
			<li><a class="icon" href="${contextPath }/dde/mdpdm!readpdm.do" target='navTab' rel="pdmto"  title="从pdm中导入元数据" ><span>从pdm中导入元数据</span></a></li>
		</ul>
	</div> --%>
	
	<c:set var="mdtablsList">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                  <a class="add" href="${contextPath }/dde/mdTable!edit.do" target="dialog" rel="" title="新增表信息"><span>新增</span></a>
                  <a class="edit" href="${contextPath }/dde/datacompare!importdatabase.do" target='navTab' rel="dybglmx"><span>数据库比对</span></a>
                  <a class="add" href="${contextPath }/dde/mdpdm!readpdm.do" target='navTab' rel="pdmto"  title="从pdm中导入元数据" ><span>从pdm中导入元数据</span></a>                   
                </li>
            </ul>
        </div>
    </c:set>
    ${mdtablsList }

	   <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		  <%@ include file="../common/panelBar.jsp"%>
		  <table class="list" width="100%" layoutH=".pageHeader 82">
	    </c:if>
	
	   <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		  <table class="list" width="100%" layoutH=".pageHeader 27">
	   </c:if>			
	      <thead align="center" style="">
				<tr>
					<th width="15%">表代码</th>
					<th width="20%">表名称</th>
					<th width="5%">数据类别</th>
					<th width="5%">形式类别</th>
					<th width="12%">更改时间</th>
					<th width="8%">更改人员</th>
					<th>表描述</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${objList }" var="mdTable">
					<tr target="pk" rel="${mdTable.tbcode}">
						<td width="15%" title="${mdTable.tbcode }">${mdTable.tbcode }</td>					
						<td width="20%" title="${mdTable.tbname }">${mdTable.tbname }</td>
						<td width="5%">
							<c:if test="${mdTable.tbstate =='S'}">系统</c:if>
							<c:if test="${mdTable.tbstate =='O'}">业务</c:if>
						</td>
						<td width="5%">
							<c:if test="${mdTable.tbtype =='T'}">表</c:if>
							<c:if test="${mdTable.tbtype =='V'}">视图</c:if>
						</td>
						<td width="12%">
							<fmt:formatDate value='${mdTable.recordDate }' pattern='yyyy-MM-dd hh-mm-ss'/>
						</td>
						<td width="8%" title="${cp:MAPVALUE('usercode',mdTable.recorder)}">
							<c:out value="${cp:MAPVALUE('usercode',mdTable.recorder)}"/>
						</td>
						<td title="${mdTable.tbdesc }">${mdTable.tbdesc }</td>
						<td>
						    <a class="icon" href="${contextPath }/dde/mdTable!view.do?tbcode=${mdTable.tbcode}" 
						        target='navTab' rel="dyzdmx" title="查看表明细"><span class="icon icon-search"></span></a>
							<a class="edit" href="${contextPath }/dde/mdTable!edit.do?tbcode=${mdTable.tbcode}" 
							    target="dialog" rel="" title="编辑表信息"><span class="icon icon-edit"></span></a>
						    <a class="delete" href="${contextPath }/dde/mdTable!delete.do?tbcode=${mdTable.tbcode}" 
						        target="ajaxTodo" title="删除表信息"><span class="icon icon-trash"></span></a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
</div>
<jsp:include page="../common/panelBar.jsp"></jsp:include>
