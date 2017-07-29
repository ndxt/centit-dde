<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<script src="${pageContext.request.contextPath}/scripts/jQueryCheckExt.js" type="text/javascript"></script>

<script type="text/javascript">
	$(function() {
		var imgpath = '${pageContext.request.contextPath}' + "/scripts/plugin/treetable/images/TreeTable";
		var $roleTree = $("#dept_power_form");
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

<form id="form1" action="${pageContext.request.contextPath}/sys/deptManager!saveDeptPower.do" 
	onsubmit="return validateCallback(this, navTabAjaxDone);">
	<div class="panel panelDrag">
		<h1>机构明细</h1>
		<div class="pageFormContent">
			<div class="unit">
				<label>机构代码：</label>
				<label>${unitcode }</label>
				<s:hidden name="unitcode" />
			</div>
			
			<div class="unit">
				<label>机构名称：</label>
				<label>${unitname }</label>
				<s:hidden name="unitname" />
			</div>
			
			<div class="unit">
				<label>机构描述：</label>
				<label>${unitdesc }</label>
				<s:hidden name="unitdesc" />
			</div>
			
	        <div class="subBar">
	            <ul>
	                <li style="float:right; margin-right:50px;">
                        <c:if test="${empty param['disablededit']}">
                            <div class="buttonActive" style="margin-right:5px;">
                                <div class="buttonContent">
                                    <a href='unit!edit.do?unitcode=${unitcode}&tabid=dept_unit_power&superUnitcode=${empty superUnitcode ? param['superUnitcode'] : superUnitcode}' target="dialog"
                                        title="编辑机构" rel="unitForm" width="500" height="450">
                                        编辑机构
                                    </a>
                                </div>
                            </div>
                        </c:if>
		                
	                	<div class="buttonActive" style="margin-right:5px;">
		                    <div class="buttonContent">
		                        <s:submit method='save' value='保存' />
		                    </div>
		                </div>
		                
	                	<div class="buttonActive">
	                    	<div class="buttonContent">
	                        	<input type="button" value="返回" onclick="javascript:navTab.closeCurrentTab('external_DEPTPOW');" />
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
			<table class="list powerTable" width="100%" layoutH=".panel 60">
				<thead align="center">
					<tr>
						<th width="300">业务名称</th>
						<th>业务操作</th>
					</tr>
				</thead>
	
				<tbody id="dept_power_form">
					<c:forEach var="optpower" items="${fOptPowers }" varStatus="status">
						<tr id="item_${status.count}">
							<td><input type="checkbox" id="${optpower.optid}" class="pc" value="${optpower.optid}" /> ${optpower.optname}</td>
							<td><c:forEach var="row" items="${cp:OPTDEF(optpower.optid)}">
									<input type="checkbox" id="c_${row.optcode}" name="optcodelist" class="cc" value="${row.optcode}" <c:if test="${powerlist[row.optcode] eq '1'}">checked="checked" </c:if> />
									<c:out value="${row.optname}" />
								</c:forEach></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</form>

