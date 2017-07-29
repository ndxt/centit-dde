<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>
<div class="pageHeader">
    <div class="pageFormContent" style="height: 100px;">
        <div class="unit">
            <label>导出名称：</label>
            <label>${object.exportName}</label>

            <label>源数据库名：</label>
            <label>${object.sourceDatabaseName}</label>
        </div>

        <div class="divider"></div>
        <div class="unit">

            <label>导出说明：</label>
            <label style="width: 85%;">${object.exportDesc}</label>
        </div>
    </div>
    <div class="subBar">
        <ul>
            <li style="float:right; margin-right:50px;">
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <a href="${pageContext.request.contextPath }/dde/exportSql!edit.do?exportId=${object.exportId}&tabid=list_field"
                               target="dialog" height="600" title="编辑数据导出内容">
                                		编辑数据导出内容
                            </a>
                        </div>
                    </div>


                <div class="buttonActive">
                    <div class="buttonContent">
                        <button onclick="navTab.closeCurrentTab('external_EXPORTSQL')">返回</button>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
        	<c:set var="dialog_height"> height="400" </c:set>
            <c:set var="pageScoreAdd">
            	<a class="add" href="${pageContext.request.contextPath }/dde/exportSql!formField.do?exportId=${object.exportId }" target="dialog" rel="detailForm" title="新增数据导出内容字段" ${dialog_height } resizable="false" maxable="false"><span>新增</span></a>
            </c:set>
            <li class="new">${pageScoreAdd }</li>
        </ul>
    </div>

    <div layoutH=".pageHeader 54">
        <table class="list" style="min-width: 1000px; width: 100%">
            <thead align="center">
            <tr>
                <th width="13%">源字段名</th>
                <th width="40%">源字段语句</th>
                <th width="10%">源字段类型</th>
                <th width="10%">字段格式</th>
                <th width="10%">存储类型</th>
                <th width="9%">是否为主键</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody align="center">
            <c:forEach items="${object.exportFields }" var="field">
                <tr target="sid_detail" rel="${field.cid.columnNo }">
                    <td width="13%">${field.fieldName }</td>
                    <td width="15%">${field.fieldSentence }</td>
                    <td width="10%">${field.fieldType }</td>
                    <td width="10%">${field.fieldFormat }</td>
                    <td width="10%">
                    	<c:if test="${'E' eq field.fieldStoreType }">embedded</c:if>
                    	<c:if test="${'F' eq field.fieldStoreType }">infile</c:if>
                    </td>
                    <td width="10%">${'T' eq field.isPk ? '是' : '否' }</td>
                    <td align="center">
                            <a class="edit" href="${pageContext.request.contextPath }/dde/exportSql!formField.do?exportId=${field.cid.exportId }&exportField.cid.exportId=${field.cid.exportId }&exportField.cid.columnNo=${field.cid.columnNo }"
                               target="dialog" rel="detailForm" width="530" height="430" resizable="false" maxable="false" title="编辑数据导出内容字段"><span class="icon icon-compose"></span></a>

                            <a class="delete" href="${pageContext.request.contextPath }/dde/exportSql!deleteField.do?exportId=${field.cid.exportId }&exportField.cid.exportId=${field.cid.exportId }&exportField.cid.columnNo=${field.cid.columnNo }"
                               target="ajaxTodo" title="确定要删除这条记录吗？"><span class="icon icon-trash"></span></a>
                        
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>



    <div class="panelBar">
        <ul class="toolBar">
            <li class="new">${pageScoreAdd }</li>
        </ul>
    </div>
</div>
