<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>

<div class="pageContent">
    
    <c:set var="mdTableView_tbcolumn">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                  <a class="add" href="${contextPath }/dde/mdColumn!edit.do?tbcode=${mdTable.tbcode}"
                      target="dialog" rel=""title="新增字段明细"><span>新增</span></a>                   
                </li>
            </ul>
        </div>
    </c:set>
    ${mdTableView_tbcolumn }
    
        <div layoutH="139">
            <table class="list" style="min-width: 1000px; width: 98%">
                <thead align="center">
                <tr>
                    <th width="20%">字段代码</th>
					<th width="20%">字段名称</th>
					<th width="15%">字段类型</th>
					<th width="10%">字段类别</th>
					<th width="10%">是否为主键</th>
					<th width="5%">检验状态</th>
					<th width="10%">字段描述</th>
					<th width="10%">操作</th>
                </tr>
                </thead>
                <tbody align="center">
                <c:forEach items="${ mdcolumnlist}" var="mdCoulmn">
                    <tr target="sid_colcode" rel="${mdCoulmn.colcode}">
                        <td width="20%">${mdCoulmn.colcode }</td>
                        <td width="30%">${mdCoulmn.colname }</td>
                        <td width="20%">${mdCoulmn.coltype}</td>
                        <td width="10%">
                        <c:if test="${mdCoulmn.accetype =='0'}">不可以访问</c:if>
						<c:if test="${mdCoulmn.accetype =='1'}">公开</c:if>
						<c:if test="${mdCoulmn.accetype =='2'}">内部访问</c:if>
                        </td>
                        <td width="10%">
                        <c:if test="${mdCoulmn.primaryKey =='T'}">是</c:if>
						<c:if test="${mdCoulmn.primaryKey =='F'}">否</c:if>
                        </td>
                        <td width="5%">
                         <c:if test="${mdCoulmn.checkState =='0'}">未检验</c:if>
						<c:if test="${mdCoulmn.checkState =='1'}">检验一致</c:if>
						<c:if test="${mdCoulmn.checkState =='2'}">检验不一致</c:if>
                        </td>
                        <td width="10%"> ${mdCoulmn.colDesc}</td>
                        <td><a href="${contextPath }/dde/mdColumn!edit.do?tbcode=${mdTable.tbcode}&colcode=${mdCoulmn.colcode}"
                               target="dialog" rel="mdColumnForm" width="530" height="430" title="编辑字段明细"><span class="icon icon-edit"></span></a>
                            <a href="${contextPath }/dde/mdColumn!delete.do?tbcode=${mdTable.tbcode}&colcode=${mdCoulmn.colcode}"
                               target="ajaxTodo" title="删除字段明细" ><span class="icon icon-trash"></span></a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
</div>
