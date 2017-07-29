<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>

<script type="text/javascript">
    $(function(){
        $('#sel_catalogstyle').change(function(){
           $('#btn_dictionary_list').click();
        });
    });
</script>

<div class="pageHeader">
    <form id="pagerForm" onsubmit="return navTabSearch(this);"
          action="${pageContext.request.contextPath}/sys/dictionary!list.do" method="post">
        <input type="hidden" name="pageNum" value="1"/>
        <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}"/>
        <input type="hidden" name="orderField" value="${param['orderField'] }"/>
        <input type="hidden" name="orderDirection" value="${param['orderDirection'] }"/>

        <div class="searchBar">
            <ul class="searchContent">
                <li><label>代码：</label> <input type="text" name="s_CATALOGCODE" value="${s_CATALOGCODE }"/></li>
                <li><label>字典名：</label> <input type="text" name="s_CATALOGNAME" value="${s_CATALOGNAME }"/></li>
                <li style="width: auto;"><label>字典类型：</label> <s:select id="sel_catalogstyle"
                        list="#{'':'全部类型', 'U':'用户数据字典', 'S':'系统数据字典', 'G':'国际数据字典'}" cssClass="combox" listKey="key"
                        listValue="value"
                        value="#request.s_CATALOGSTYLE" name="s_CATALOGSTYLE"></s:select></li>
            </ul>
            <div class="subBar">
                <ul>
                    <li>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit" id="btn_dictionary_list">查询</button>
                            </div>
                        </div>
                    </li>
                    <li><a class="button" target="ajaxTodo"
                           href="${pageContext.request.contextPath}/sys/dictionary!refresh.do"><span>刷新系统代码库</span></a>
                    </li>
                </ul>
            </div>
        </div>
    </form>
</div>

<div class="pageContent">

    <c:set var="panel_buttons">
		<a class="add" href="dictionary!built.do" 
			target="dialog" rel="DICTIONARY_EDIT" title="新增数据字典" width="530" height="420">
			<span>新增</span>
		</a>
	</c:set>

	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		<%@ include file="../common/panelBar.jsp"%>
		<table class="list" width="100%" layoutH=".pageHeader 54">
	</c:if>
	
	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		<table class="list" width="100%" layoutH=".pageHeader 27">
	</c:if>
        <thead align="center" style="">
        <tr>
            <th width="20%" orderField="catalogcode">代码</th>
            <th width="30%" orderField="catalogname">字典名</th>
            <th width="10%" orderField="catalogtype">字典形式</th>
            <th>字典描述</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody align="center">
        <c:forEach items="${objList }" var="fDatadictionary">
            <tr target="sid_dictionary" rel="${fDatadictionary.catalogcode}">
                <td title="${fDatadictionary.catalogcode }" width="20%">${fDatadictionary.catalogcode }</td>
                <td title="${fDatadictionary.catalogname }" width="30%">${fDatadictionary.catalogname }</td>
                <td title="${fDatadictionary.catalogtype=='L'?'列表':'树形'}" width="10%"><c:out value="${fDatadictionary.catalogtype=='L'?'列表':'树形'}"/></td>
                <td title="${fDatadictionary.catalogdesc }">${fDatadictionary.catalogdesc }</td>
                <td align="center">
                    <a class="icon" href="dictionary!view.do?catalogcode=${fDatadictionary.catalogcode}"
                       target="navTab" rel="DICTIONARY_VIEW" title="数据字典明细"><span
                            class="icon icon-search"></span></a>
                    <c:if test="${(not ('S' eq fDatadictionary.catalogstyle)) or ('T' eq cp:MAPVALUE('SYSPARAM', 'EnableSys')) }">
                        <a class="edit" href="dictionary!edit.do?catalogcode=${fDatadictionary.catalogcode}"
                           target="dialog" rel="DICTIONARY_EDIT" title="编辑数据字典" width="530" height="420"
                           resizable="false" maxable="false"><span class="icon icon-compose"></span></a>
                        <a class="delete" href="dictionary!delete.do?catalogcode=${fDatadictionary.catalogcode}"
                           target="ajaxTodo" title="确定要删除吗?"><span class="icon icon-trash"></span></a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@ include file="../common/panelBar.jsp"%>
