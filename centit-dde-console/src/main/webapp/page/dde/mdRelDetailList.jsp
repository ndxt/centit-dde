<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="${pageContext.request.contextPath}/dde/mdRelDetail!list.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
</form>

<div class="pageContent">
	<c:set var="mdreldetailList">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                  <a id="mdReldadd" class="add" href="${contextPath }/dde/mdRelDetail!add.do?relcode=${relcode}&ptabcode=${ptabcode}&ctabcode=${ctabcode}"
                     target="dialog" rel="" title="新增关联字段明细"><span>新增关联字段</span></a>                   
                </li>
            </ul>
        </div>
    </c:set>
    ${mdreldetailList }

	   <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		  <%@ include file="../common/panelBar.jsp"%>
		  <table class="list" width="100%" layoutH=".pageHeader 82">
	    </c:if>
	
	   <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		  <table class="list" width="100%" layoutH=".pageHeader 27">
	   </c:if>			
	      <thead align="center" style="">
				<tr>
					<th width="25%">关联代码</th>
					<th width="25%">p字段代码</th>
					<th width="25%">c字段代码</th>
					<th width="25%">操作</th>
				</tr>
			</thead>
			<tbody align="center">
				<c:forEach items="${objList }" var="mdreldetail">
					<tr>
						<td width="25%">${mdreldetail.cid.relcode }</td>					
						<td width="25%">${mdreldetail.cid.pcolcode }</td>
						<td width="25%">
							${mdreldetail.ccolcode }
						</td>
						<td>
						    <a href="${contextPath }/dde/mdRelDetail!edit.do?relcode=${relcode}&pcolcode=${mdreldetail.cid.pcolcode }&ptabcode=${ptabcode}&ctabcode=${ctabcode}"
                                target="dialog" rel="" width="530" height="430" title="编辑字段明细"><span class="icon icon-edit"></span></a>
						    <a href="${contextPath }/dde/mdRelDetail!delete.do?relcode=${relcode}&pcolcode=${mdreldetail.cid.pcolcode }" 
						       target="ajaxTodo" title="删除编辑字段明细"><span class="icon icon-trash"></span></a>						
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
</div>
<jsp:include page="../common/panelBar.jsp"></jsp:include>
