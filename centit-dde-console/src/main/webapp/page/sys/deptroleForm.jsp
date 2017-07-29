<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/treetable/Treetable_files/jqtreetable.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/treetable/Treetable_files/jqtreetable.js"></script> --%>
<script src="${pageContext.request.contextPath}/scripts/jQueryCheckExt.js" type="text/javascript"></script>

<script type="text/javascript">
	$(function() {
		var imgpath = '${pageContext.request.contextPath}' + "/scripts/plugin/treetable/images/TreeTable";
		var $roleTree = $("#deptrole_tbody");
		var index = $.parseJSON('${INDEX}').indexes;

		var $objRoleTree = new jQueryCheckExt();
		$objRoleTree.makeCkeckBoxTreeTable($roleTree, index, imgpath);
	});
</script>


<form action="${pageContext.request.contextPath }/sys/deptManager!saveDeptRole.do" method="post" onsubmit="return validateCallback(this, navTabAjaxDone);">
	<div class="panel panelDrag">
		<h1>角色明细</h1>
		<div class="pageFormContent">

			<div class="unit">
				<label>角色代码</label>
				<input name="roleinfo.rolecode" class='textInput required minlength="1" maxlength="10" ' value="${roleinfo.rolecode }" />
			</div>
			
			<div class="unit">
				<label>角色名</label>
				<input name="roleinfo.rolename" class='textInput required minlength="1" maxlength="10" ' value="${roleinfo.rolename }" />
			</div>
			
			<div class="unit">
				<label>角色描述</label>
				<textarea name="roleinfo.roledesc" cols="40" rows="3">${roleinfo.roledesc }</textarea>
			</div>
			
	        <div class="subBar">
	            <ul>
	                <li style="float:right; margin-right:50px;">
		                <div class="buttonActive" style="margin-right:5px;">
		                    <div class="buttonContent">
		                        <s:submit method='save' value='保存' />
		                    </div>
		                </div>
		                <div class="buttonActive">
		                    <div class="buttonContent">
		                        <input type="button" value="返回" onclick="javascript:navTab.closeCurrentTab('external_DEPTROLE')" />
		                    </div>
		                </div>
	                </li>
	            </ul>
	        </div>
		</div>
    </div>
    
	<div class="panel panelDrag" style="margin-top:5px;">
		<h1>角色权限</h1>
		<div class="pageFormContent">
			<table class='list' width='100%' layoutH=".panel 60">
				<thead>
					<tr>
						<td class="tableHeader">业务名称</td>
						<td class="tableHeader">业务操作</td>
					</tr>
				</thead>
				<tbody class="tableBody" id="deptrole_tbody">
					<c:set value="odd" var="rownum" />
					<c:forEach var="optpower" items="${fOptPowers }" varStatus="status">
						<tr id="item_${status.count}">
							<td><input type="checkbox" id="${optpower.optid}" class="pc" value="${optpower.optid}" /> ${optpower.optname}</td>
							<td>
								<c:forEach var="optcode" items="${optpower.powerlist}">
									<input type="checkbox" id="c_${optcode }" name="optlist" class="cc" value="${optcode}" 
										<c:if test="${powerlist[optcode] eq '1'}">checked="checked" </c:if> />
									<c:out value="${cp:MAPVALUE('optcode',optcode)}" />
								</c:forEach>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</form>

