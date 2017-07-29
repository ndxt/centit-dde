<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<script src="${pageContext.request.contextPath}/scripts/jQueryCheckExt.js" type="text/javascript"></script>

<script type="text/javascript">
	$(function() {
		var imgpath = '${pageContext.request.contextPath}' + "/scripts/plugin/treetable/images/TreeTable";
		var $roleTree = $("#table_unitpower");
		var index = $.parseJSON('${INDEX}').indexes;

		var $objRoleTree = new jQueryCheckExt();
		$objRoleTree.makeCkeckBoxTreeTable($roleTree, index, imgpath);
	});
</script>

<style>
table.powerTable {
	table-layout:auto;
}
table.powerTable td{
	white-space:inherit; overflow:auto;
}
</style>

<form id="unitPowerForm" action="${pageContext.request.contextPath}/sys/unit!saveUnitPower.do" 
	onsubmit="return validateCallback(this, navTabAjaxDone);">
	<s:hidden name="unitcode" value="%{object.unitcode}"></s:hidden>
	<div class="panel panelDrag" id="unitDetails">
		<h1>机构明细</h1>
		<div class="pageFormContent">
		
			<div class="unit">
				<label>机构代码：</label>
				<label>${object.unitcode}</label>
				
				<label>机构名称：</label>
				<label>${object.unitname}</label>
				
				<label>部门简称：</label>
				<label>${object.unitshortname}</label>
			</div>
			
			<div class="unit">
				<label>机构类型：</label>
				<label>
					${cp:MAPVALUE('UnitType', object.unittype) }
				</label>
				
				<label>机构状态：</label>
				<label>
					${cp:MAPVALUE('VALID_STATE', object.isvalid) }
				</label>
				
				<label>部门等级：</label>
				<label>${object.unitgrade}</label>
			</div>
			
			<div class="unit">
				<label>机构描述：</label>
				<label>${object.unitdesc }</label>
			</div>
			
			<div class="subBar">
				<ul>
					<li style="float:right; margin-right:50px;">
						<div class="buttonActive" style="margin-right:5px;">
							<div class="buttonContent">
								<a href='unit!edit.do?unitcode=${object.unitcode}&superUnitcode=${empty superUnitcode ? param['superUnitcode'] : superUnitcode}&tabid=unit_list_main'
									target="dialog" title="编辑机构" rel="unitForm" width="500" height="450">
									编辑机构信息
								</a> 
							</div>
						</div>
						<div class="buttonActive" style="margin-right:5px;">
							<div class="buttonContent">
								<input type="button" value="保存" onclick="$('#unitPowerForm').submit()" />
							</div>
						</div>
						<div class="buttonActive">
							<div class="buttonContent">
								<input type="button" value="返回" onclick="navTab.closeCurrentTab('external_UNITMAG')" />
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
	
	<div class="panel panelDrag" style="margin-top:5px;">
		<h1>机构权限</h1>
		
		<div class="pageFormContent">
			<table class="list powerTable" width="100%" layoutH="#unitDetails 61">
			<thead>
				<tr>
					<th align="center" width="300">业务名称</th>
					<th align="center">业务操作</th>
				</tr>
			</thead>

			<tbody id="table_unitpower">
				<c:forEach var="role" items="${fOptinfos }" varStatus="status">
					<tr id="item_${status.count}">
						<td>
							<input type="checkbox" id="${role.optid}" class="pc" value="${role.optid}" /> 
							${role.optname}
						</td>
						<td>
							<c:forEach var="row" items="${cp:OPTDEF(role.optid)}">
								<input type="checkbox" id="c_${row.optcode}" name="optcodelist" class="cc" value="${row.optcode}" 
									<c:if test="${powerlist[row.optcode] eq '1'}">checked="checked" </c:if> />
								<c:out value="${row.optname}" />
							</c:forEach>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
	</div>
</form>
