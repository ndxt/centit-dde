<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<script src="${pageContext.request.contextPath}/scripts/jQueryCheckExt.js" type="text/javascript"></script>


<script type="text/javascript">
	$(function() {
		var imgpath = '${pageContext.request.contextPath}' + "/scripts/plugin/treetable/images/TreeTable";
		var $roleTree = $("#table_unit");
		var index = $.parseJSON('${INDEX}').indexes;

		var $objRoleTree = new jQueryCheckExt();
		$objRoleTree.makeCkeckBoxTreeTable($roleTree, index, imgpath);
	});
</script>

<!-- <style>
table.tree-table td {height:14px; line-height: 14px;}
</style> -->

<div class="pageContent">
    <c:set var="createunit">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                    <a class="add" href="${pageContext.request.contextPath }/sys/unit!built.do"
                       target="dialog" width="500" height="450" >
                        <span>创建顶级机构</span>
                    </a>
                </li>
            </ul>
        </div>
    </c:set>
    ${createunit }

	<table class="tablemain tree-table" style="width:100%;" layoutH="54">
		<thead align="center">
			<tr>
				<th>机构名</th>
				<th>机构代码</th>
				<th>机构类型</th>
				<%-- <th>上级机构</th>
				<th>机构排序号</th> --%>
				<th>状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody id="table_unit" align="center">
			<c:forEach var="obj" items="${objList }">
				<tr>
					<td align="left" title="${obj.unitname }">${obj.unitname }</td>
					<td title="${obj.unitcode }">${obj.unitcode }</td>
					<td>
						${cp:MAPVALUE('UnitType', obj.unittype) }
					</td>
					<%-- <td title="${obj.parentunit }">${obj.parentunit }</td>
					<td title="${obj.unitorder }">${obj.unitorder }</td> --%>
					<td >
						${USE_STATE[obj.isvalid] }
					</td>
					<td>
						<a href='unit!edit.do?unitcode=${obj.unitcode}&superUnitcode=${superUnitcode}&tabid=external_UNITMAG' target="dialog" 
							title="编辑机构" rel="unitForm" width="500" height="450">
							<span class="icon icon-edit"></span> 
						</a> 
						
						<a href='unit!editUnitPower.do?unitcode=${obj.unitcode}&superUnitcode=${superUnitcode}' rel="unit_list_main" target="navTab" title="编辑机构权限">
							<span class="icon icon-key"></span>
						</a> 
						
						<a href='unit!view.do?unitcode=${obj.unitcode}&superUnitcode=${superUnitcode}' rel="unit_members_detail" target="navTab" title="查看机构成员">
							<span class="icon icon-user"></span>
						</a> 
						
						<c:if test="${obj.isvalid eq 'F'}">
							<a href='unit!renew.do?unitcode=${obj.unitcode}' title="确定要启用该机构吗？" target="ajaxTodo">
								<span title="启用机构" class="icon icon-unlocked"></span>
							</a>
						</c:if> 
						
						<c:if test="${obj.isvalid eq 'T'}">
							<a href='unit!delete.do?unitcode=${obj.unitcode}' title="是否禁用该机构吗?" target="ajaxTodo">
								<span title="禁用机构" class="icon icon-locked"></span>
							</a>
						</c:if> 
						
						<a href='unit!builtNext.do?parentunit=${obj.unitcode}'
							target="dialog" title="添加下级机构" width="500" height="450" >
							<span class="icon icon-add"></span>
						</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	${createunit }
</div>