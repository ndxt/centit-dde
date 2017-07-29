<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>


<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/treetable/Treetable_files/jqtreetable.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/treetable/Treetable_files/jqtreetable.js"></script> --%>

<script src="${pageContext.request.contextPath}/scripts/jQueryCheckExt.js" type="text/javascript"></script>


<script type="text/javascript">
    $(function () {
        var imgpath = '${pageContext.request.contextPath}' + "/scripts/plugin/treetable/images/TreeTable";
        var $roleTree = $("#table_optinfo");
        var index = $.parseJSON('${INDEX}').indexes;

        var $objRoleTree = new jQueryCheckExt();
        $objRoleTree.makeCkeckBoxTreeTable($roleTree, index, imgpath);
    });
</script>


<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <c:set var="optInfoNewListAdd" value='<a class="add" href="${pageContext.request.contextPath }/sys/optInfo!built.do?preoptid=0" target="dialog" width="640" height="400"><span>新建一个父类功能模块</span></a>' />
            <li class="new">${optInfoNewListAdd}</li>
        </ul>
    </div>
    <table class="tablemain tree-table" style="width:100%;">
        <thead align="center">
        <tr>
            <th>业务名称</th>
            <th>业务代码</th>
            <th>父类业务代码</th>
            <th>业务URL</th>
            <th>是否在菜单栏</th>
            <th>业务类别</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody id="table_optinfo">
        <c:forEach var="obj" items="${objList }">
            <tr>
                <td title="${obj.optname }">${obj.optname }</td>
                <td title="${obj.optid }">${obj.optid }</td>
                <td title="${obj.preoptid }">${obj.preoptid }</td>
                <td title="${obj.opturl }">${obj.opturl }</td>
                <td title="${'Y' eq obj.isintoolbar ? '是' : '否' }">${'Y' eq obj.isintoolbar ? '是' : '否' }</td>
                <td title="${cp:MAPVALUE("OPTTYPE", obj.opttype)}">${cp:MAPVALUE("OPTTYPE", obj.opttype)}</td>
                <td><c:if test="${not('...' eq obj.opturl)}">
                    <a href='optDef!list.do?optid=${obj.optid}' target="navTab" rel='opt_EDIT' title="操作定义"><span
                            class="icon icon-search"></span></a>
                </c:if> <a href='optInfo!edit.do?optid=${obj.optid}' target="dialog" title="编辑" width="640" height="400"><span class="icon icon-compose"></span></a> <a
                        href='optInfo!delete.do?optid=${obj.optid}' target="ajaxTodo" title="确定要删除吗?"><span class="icon icon-trash"></span></a> <a
                        href='optInfo!built.do?preoptid=${obj.optid}' target="dialog" title="添加下层业务" width="640" height="400"><span class="icon icon-add"></span></a> <c:if
                        test="${'W' eq obj.opttype }">

                    <a href='optVar!list.do?s_OPTID=${obj.optid}' target="dialog" title="查看业务变量"><span class="icon icon-search"></span></a>
                </c:if></td>

            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div class="panelBar">
        <ul class="toolBar">
            <li class="new">${optInfoNewListAdd}</li>
        </ul>
    </div>
</div>