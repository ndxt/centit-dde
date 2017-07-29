<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>

<div class="pageContent">
    
    <c:set var="mdRelationList">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                  <a class="add" href="${contextPath }/dde/mdRelation!edit.do?ptabcode=${tbcode}"
                   target="dialog" rel="" title="新增表关联明细" width="500" height="400"><span>新增</span></a>                  
                </li>
            </ul>
        </div>
    </c:set>
    ${mdRelationList }
    
        <div layoutH="139">
            <table class="list" style="min-width: 1000px; width: 98%">
                <thead align="center">
                <tr>
                    <th width="20%">关联名称</th>
					<th width="25%">链接表名</th>
					<th width="45%">关联字段</th>
					<th width="10%">操作</th>
                </tr>
                </thead>
                <c:set var="temprelcode" value="table" />
                <c:set var="nextcode" value="table"/>  
                <c:set var="tmptablecode" value="table"/>
                <tbody align="center">
                <c:forEach items="${ viewrellist}" var="mdrel" >
                  <c:set var="nextcode" value="${mdrel.relcode}"/>
                  <c:if test="${temprelcode != nextcode}">
                    <tr>
                        <td title="${mdrel.relname}">${mdrel.relname}</td>
                        <td title="${mdrel.ctbname}">${mdrel.ctbname}</td>
                        <td>                        
                          <c:forEach items="${ viewrellist}" var="mdrelin" >
                              <c:if test="${mdrelin.relcode eq nextcode }">${mdrelin.ccolcode}&nbsp;&nbsp;&nbsp;</c:if>
                          </c:forEach>
                          <c:set var="temprelcode" value="${mdrel.relcode}"/>
                        </td>
                        <td>
                           <a id="mdRelationedit"  href="${contextPath }/dde/mdRelDetail!list.do?s_relcode=${mdrel.relcode}&s_ptabcode=${tbcode}&s_ctabcode=${mdrel.ctabcode}"
                              target="navTab" rel="ckglxxxx"  height="430" title="查看关联信息详细" ><span class="icon icon-search"></span></a>
                           <a id="mdRelationedit"  href="${contextPath }/dde/mdRelation!edit.do?relcode=${mdrel.relcode}&ptabcode=${tbcode}"
                              target="dialog" rel="" width="530" height="430" title="编辑表关联信息" ><span class="icon icon-edit"></span></a>
                           <a id="mdRelationdel" class="delete" href="${contextPath }/dde/mdRelation!delete.do?relcode=${mdrel.relcode}"
                              target="ajaxTodo" title="删除关联信息" ><span class="icon icon-trash"></span></a>
                        </td>
                    </tr>
                  </c:if>
                </c:forEach>
                </tbody>
            </table>
        </div>
</div>
