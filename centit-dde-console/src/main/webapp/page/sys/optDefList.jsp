<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath }/sys/optDef!list.do?optid=${optinfo.optid}" method="post" id="pagerForm">
		<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
		<div class="searchBar">
			<ul class="searchContent">
				<li><label>业务代码: ${optinfo.optid}</label> </li>
				<li><label>业务名称: ${optinfo.optname}</label> </li>
                <li><label title="${optinfo.opturl}">业务URL: ${optinfo.opturl} </label> </li>
            </ul>

        </div>
        <div class="subBar">
            <ul>
                <li style="float:right; margin-right:50px;">
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <a href="${pageContext.request.contextPath }/sys/optInfo!edit.do?optid=${object.optid}&tabid=opt_EDIT"
                               target="dialog" width="640" height="400" title="编辑">
                                编辑业务信息
                            </a>
                        </div>
                    </div>

                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="button" onclick="javascript:navTab.closeCurrentTab('external_OPTINFO');">返回</button>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </form>
</div>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
            <c:set var="optDefListAdd" value='<a class="add" href="${pageContext.request.contextPath }/sys/optDef!built.do?optid=${optinfo.optid}" target="dialog"><span>新增</span></a>' />
			<li class="new">${optDefListAdd }</li>

		</ul>
	</div>
	
	<table class="list" width="100%" layoutH="91">
		<thead align="center">

			<tr>
				<th align="center">操作代码</th>
				<th align="center">操作方法</th>
				<th align="center">方法名称</th>
				<th align="center">方法说明</th>
                   <th>操作</th>
			</tr>
		</thead>
		<tbody align="center">
			<c:forEach items="${optdefs}" var="fOptdefs">

				<tr target="sid_user" rel="${fOptdefs.optcode}" align="center">
					<td title="${fOptdefs.optcode}">${fOptdefs.optcode}</td>
					<td title="${fOptdefs.optmethod}">${fOptdefs.optmethod}</td>
					<td title="${fOptdefs.optname}">${fOptdefs.optname}</td>
					<td title="${fOptdefs.optdesc}">${fOptdefs.optdesc}</td>
                       <td>
                           <a href='${pageContext.request.contextPath }/sys/optDef!edit1.do?optcode=${fOptdefs.optcode}&ec_p=${ec_p}&ec_crd=${ec_crd}' target="dialog" title="编辑" width="580" height="300"><span class="icon icon-compose"></span></a> <a
                               href='${pageContext.request.contextPath }/sys/optDef!delete1.do?optcode=${fOptdefs.optcode}' target="ajaxTodo" title="确定要删除吗?"><span class="icon icon-trash"></span></a>

                       </td>
				</tr>
			</c:forEach>



		</tbody>
	</table>
	
    <div class="panelBar">
        <ul class="toolBar">
            <li class="new">${optDefListAdd}</li>
        </ul>
    </div>
</div>



