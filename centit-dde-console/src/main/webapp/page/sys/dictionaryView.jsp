<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>
<div class="pageHeader">
    <div class="pageFormContent" style="height: 136px;">
        <div class="unit">
            <label>字典代码：</label>
            <label>${catalog.catalogcode}</label>

            <label>字典名称：</label>
            <label>${catalog.catalogname}</label>
        </div>

        <div class="divider"></div>
        <div class="unit">

            <label>字典描述：</label>
            <label style="width: 85%;">${catalog.catalogdesc}</label>
        </div>
    </div>
    <div class="subBar">
        <ul>
            <li style="float:right; margin-right:50px;">
                <c:if test="${(not ('S' eq object.catalogstyle)) or ('T' eq cp:MAPVALUE('SYSPARAM', 'EnableSys')) }">
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <a href="${pageContext.request.contextPath }/sys/dictionary!edit.do?catalogcode=${object.catalogcode}&tabid=DICTIONARY_VIEW"
                               target="dialog" width="530" height="420" title="编辑数据字典">
                                编辑数据字典
                            </a>
                        </div>
                    </div>
                </c:if>


                <div class="buttonActive">
                    <div class="buttonContent">
                        <button onclick="navTab.closeCurrentTab('external_DICTSET')">返回</button>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <c:set var="pageScoreAdd" value='<a class="add" href="dictionary!editDetail.do?catalogcode=${catalog.catalogcode}" target="dialog" rel="detailForm" title="新增数据字典明细" width="500" height="420" resizable="false" maxable="false"><span>新增</span></a>' />
            <li class="new">${pageScoreAdd }</li>
        </ul>
    </div>

    <c:if test="${catalog.catalogtype eq 'L'}">
        <div layoutH=".pageHeader 54">
            <table class="list" style="min-width: 1000px; width: 100%">
                <thead align="center">
                <tr>
                    <th width="13%">${fdesc[0]}</th>
                    <th width="10%">${fdesc[1]}</th>
                    <th width="10%">${fdesc[2]}</th>
                    <th width="10%">${fdesc[3]}</th>
                    <th width="15%">${fdesc[4]}</th>
                    <th width="10%">${fdesc[5]}</th>
                    <th>${fdesc[6]}</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody align="center">
                <c:forEach items="${ dictDetails}" var="detail">
                    <tr target="sid_detail" rel="${detail.datacode}">
                        <td title="${detail.datacode }" width="13%">${detail.datacode }</td>
                        <td title="${detail.extracode }" width="10%">${detail.extracode }</td>
                        <td title="${detail.extracode2 }" width="10%">${detail.extracode2}</td>
                        <td title="${detail.datatag }" width="10%">${detail.datatag }</td>
                        <td title="${detail.datavalue }" width="15%">${detail.datavalue }</td>
                        <td title="${detail.datastyle }" width="10%">${detail.datastyle }</td>
                        <td title="${detail.datadesc }">${detail.datadesc }</td>
                        <td align="center">
                            <c:if test="${(not ('S' eq detail.datastyle) ) or ('T' eq cp:MAPVALUE('SYSPARAM', 'EnableSys')) }">
                                <a class="edit" href="dictionary!editDetail.do?catalogcode=${catalog.catalogcode}&datacode=${detail.datacode}"
                                   target="dialog" rel="detailForm" width="530" height="430" resizable="false" maxable="false" title="编辑数据字典明细"><span class="icon icon-compose"></span></a>

                                <a class="delete" href="dictionary!deleteDetail.do?catalogcode=${catalog.catalogcode}&datacode=${detail.datacode}"
                                   target="ajaxTodo" title="确定要删除这条记录吗？"><span class="icon icon-trash"></span></a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>

    <c:if test="${catalog.catalogtype eq 'T'}">
        <div layoutH=".pageHeader 54">
            <table class="tablemain" style="min-width: 1000px; width: 100%">
                <thead align="center">
                <tr>
                    <th width="15%">${fdesc[4]}</th>
                    <th width="13%">${fdesc[0]}</th>
                    <th width="13%">${fdesc[2]}</th>
                    <th width="10%">${fdesc[3]}</th>
                    <th width="10%">${fdesc[5]}</th>
                    <th>${fdesc[6]}</th>
                </tr>
                </thead>
                <tbody align="center" id="table-detailTree">
                <c:forEach items="${dictDetails}" var="detail">
                    <tr target="sid_detail" rel="${detail.datacode}">
                        <td width="15%" align="left">${detail.datavalue }</td>
                        <td width="13%">${detail.datacode }</td>
                        <td width="13%">${detail.extracode2}</td>
                        <td width="10%">${detail.datatag }</td>
                        <td width="10%">${detail.datastyle }</td>
                        <td title="${detail.datadesc }">${detail.datadesc }</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>



        <%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/treetable/Treetable_files/jqtreetable.css" />
        <script type="text/javascript" src="${pageContext.request.contextPath}/scripts/treetable/Treetable_files/jqtreetable.js"></script> --%>

        <script src="${pageContext.request.contextPath}/scripts/jQueryCheckExt.js" type="text/javascript"></script>


        <script type="text/javascript">
            $(function () {
                var imgpath = '${pageContext.request.contextPath}' + "/scripts/plugin/treetable/images/TreeTable";
                var $roleTree = $("#table-detailTree");
                var index = $.parseJSON('${INDEX}').indexes;

                var $objRoleTree = new jQueryCheckExt();
                $objRoleTree.makeCkeckBoxTreeTable($roleTree, index, imgpath);
            });
        </script>
    </c:if>


    <div class="panelBar">
        <ul class="toolBar">
            <li class="new">${pageScoreAdd }</li>
        </ul>
    </div>
</div>
