<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>
<div class="pageHeader">
    <div class="pageFormContent" >
		<input type="hidden" name="ptabcode" value="${mdRelation.ptabcode}" />
		<input type="hidden" name="ctabcode" value="${mdRelation.ctabcode}" />
    </div>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <li> <a class="add" href="${contextPath }/dde/mdRelDetail!edit.do?relcode=${mdRelation.relcode}&ptabcode=${mdRelation.ptabcode}&ctabcode=${mdRelation.ctabcode}"
                   target="dialog" rel="" title="新增关联字段明细"><span>新增</span></a></li>
            <li><a class="edit" href="${contextPath }/dde/mdRelDetail!edit.do?relcode=${mdRelation.relcode}&pcolcode={sid_pcolcode}&ptabcode=${mdRelation.ptabcode}&ctabcode=${mdRelation.ctabcode}"
                   warn="请选择一条记录" target="dialog" rel="" width="530" height="430" title="编辑字段明细"><span>编辑</span></a></li>
            <li><a class="delete"
                   href="${contextPath }/dde/mdRelDetail!delete.do?relcode=${mdRelation.relcode}&pcolcode={sid_pcolcode}"
                   target="ajaxTodo" title="确定要删除这条记录吗？" warn="请选择一条记录"><span>删除</span></a></li>

        </ul>
    </div>
        <div layoutH="183">
            <table class="list" style="min-width: 1000px; width: 98%">
                <thead align="center">
                <tr>
                    <th width="50%">关联表一字段代码</th>
					<th width="50%">关联表二字段代码</th>
                </tr>
                </thead>
                <tbody align="center">
                <c:forEach items="${ mdreldetaillist}" var="mdRelDetail">
                    <tr target="sid_pcolcode" rel="${mdRelDetail.pcolcode}">
                        <td width="50%">${mdRelDetail.pcolcode }</td>
                        <td width="50%">${mdRelDetail.ccolcode }</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
</div>
