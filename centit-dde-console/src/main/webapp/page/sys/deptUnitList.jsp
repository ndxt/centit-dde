<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/treetable/Treetable_files/jqtreetable.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/treetable/Treetable_files/jqtreetable.js"></script> --%>
<script src="${pageContext.request.contextPath}/scripts/jQueryCheckExt.js" type="text/javascript"></script>


<script type="text/javascript">
	$(function() {
		var imgpath = '${pageContext.request.contextPath}' + "/scripts/plugin/treetable/images/TreeTable";
		var $roleTree = $("#dept_unit_list");
		var index = $.parseJSON('${INDEX}').indexes;

		var $objRoleTree = new jQueryCheckExt();
		$objRoleTree.makeTreeTable($roleTree, index, imgpath);
	});
</script>


<form id="form1" action="${pageContext.request.contextPath}/sys/roleDef!save.do" 
	onsubmit="return validateCallback(this, navTabAjaxDone);">
	<div class="pageContent">
		<table class="list" width="100%" layoutH="1">
			<thead align="center">
				<tr>
					<th width="200">部门名</th>
					<th width="100">部门代码</th>
					<th width="100">部门类型</th>
					<th width="100">上级部门</th>
					<th width="80">状态</th>
					<th width="80">操作</th>
				</tr>
			</thead>

			<tbody id="dept_unit_list" align="center">
				<c:forEach var="obj" items="${objList }" varStatus="status">
					<tr id="item_${status.count}">
						<td align="left">${obj.unitname }</td>
						<td title="${obj.unitcode }">${obj.unitcode }</td>
						<td title="${cp:MAPVALUE("UnitType",obj.unittype)}">${cp:MAPVALUE("UnitType",obj.unittype)}</td>
						<td title="${cp:MAPVALUE("unitcode",obj.parentunit)}">${cp:MAPVALUE("unitcode",obj.parentunit)}</td>
						<td>${USE_STATE[obj.isvalid]}</td>
						<td>

                            <c:if test="${not (0 eq status.index)}">
                                <a href='unit!edit.do?unitcode=${obj.unitcode}&tabid=external_DEPTPOW&superUnitcode=${superUnitcode}' target="dialog"
                                   title="编辑机构" rel="unitForm" width="500" height="450">
                                    <span class="icon icon-edit"></span>
                                </a>
                            </c:if>

                            <a href='deptManager!editDeptPower.do?unitcode=${obj.unitcode}&superUnitcode=${superUnitcode}<c:if test="${0 eq status.index }">&disablededit=true</c:if>'
								target="navTab" rel="dept_unit_power" title="编辑机构权限"> 
								<span class="icon icon-key"></span>  
							</a>


                            <a href='unit!view.do?unitcode=${obj.unitcode}&superUnitcode=${superUnitcode}&tabid=external_DEPTPOW' rel="unit_members_detail" target="navTab" title="查看机构成员">
                                <span class="icon icon-user"></span>
                            </a>

                            <c:if test="${not (0 eq status.index)}">
                            <c:if test="${obj.isvalid eq 'F'}">
                                <a href='unit!renew.do?unitcode=${obj.unitcode}&tabid=external_DEPTPOW' title="确定要启用该机构吗？" target="ajaxTodo">
                                    <span title="启用机构" class="icon icon-unlocked"></span>
                                </a>
                            </c:if>

                            <c:if test="${obj.isvalid eq 'T'}">
                                <a href='unit!delete.do?unitcode=${obj.unitcode}&tabid=external_DEPTPOW' title="是否禁用该机构吗?" target="ajaxTodo">
                                    <span title="禁用机构" class="icon icon-locked"></span>
                                </a>
                            </c:if>
                            </c:if>

                            <a href='unit!builtNext.do?parentunit=${obj.unitcode}&tabid=external_DEPTPOW'
                               target="dialog" title="添加下级机构" width="500" height="450" >
                                <span class="icon icon-add"></span>
                            </a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
</form>