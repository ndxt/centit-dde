<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<script src="${pageContext.request.contextPath}/scripts/jQueryCheckExt.js" type="text/javascript"></script>

<script type="text/javascript">
	$(function() {
		var imgpath = '${pageContext.request.contextPath}' + "/scripts/plugin/treetable/images/TreeTable";
		var $roleTree = $("#treet2");
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
table.powerTable tr.even {
	background-color:#fff;
}
</style>

<form id="form1" action="${pageContext.request.contextPath}/sys/roleDef!save.do" method="post" onsubmit="return validateCallback(this, navTabAjaxDone);">
	<div class="panel panelDrag" id="roleDetails">
		<h1>角色明细</h1>
		<div class="pageFormContent">
			<div class="unit">
				<label>角色代码：</label>
				<c:if test="${not empty rolecode}">
					<label>${rolecode }</label>
					<s:hidden name="rolecode" value="%{rolecode}" />
				</c:if> 
				<c:if test="${empty rolecode}">
					<s:textfield id="rolecode" name="rolecode" value="%{rolecode}" cssClass="required"/>
				</c:if>
			</div>
			
			<div class="unit">
				<label>角色名称：</label>
				
				<s:textfield id="rolename" name="rolename" value="%{rolename}" cssClass="required" /> 
			</div>

            <div class="unit">
				<label>公共角色：</label>

                <input type="checkbox" name="isPublic" value="T">
			</div>
			
			<div class="unit">
				<label>角色描述：</label>
				<s:textarea name="roledesc" value="%{roledesc}" rows="3" cols="40" />
			</div>
			
			<div class="subBar">
				<ul>
					<li style="float:right; margin-right:50px;">
						<div class="buttonActive" style="margin-right:10px;">
							<div class="buttonContent">
								<s:submit method='save' value='保存' />
							</div>
						</div>
						<div class="buttonActive">
							<div class="buttonContent">
								<input type="button" onclick="navTab.closeCurrentTab('external_ROLEMAG')" value="返回"/>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>
	
	<div class="panel panelDrag" id="rolePowers" style="margin-top:5px;">
		<h1>角色权限</h1>
		<table class="list powerTable" width="100%" layoutH="#roleDetails 40">
			<thead>
				<tr>
					<th align="center" width="300">业务名称</th>
					<th align="center">业务操作</th>
				</tr>
			</thead>

			<tbody id="treet2">
				<c:forEach var="role" items="${fOptinfos }" varStatus="status">
					<tr id="item_${status.count}">
						<td>
							<input type="checkbox" id="${role.optid}" class="pc" value="${role.optid}" /> ${role.optname}
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
</form>