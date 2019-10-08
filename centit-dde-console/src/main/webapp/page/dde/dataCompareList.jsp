<%@page import="com.centit.dde.po.TableColumnInfo"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">
	<form id="datacompareFrom" method="post">
		<div class="pageFormContent">
			<label>表名：</label> <select id="dbtableName" class="combox"
				onchange="showTableCol()">
				<option value="">请选择表名：</option>
				<c:forEach var="row" items="${sourcetableNameList}">
					<option value="${row}"
						<c:if test="${row eq editTablecode}">selected=selected</c:if>>
						<c:out value="${row}" />
					</option>
				</c:forEach>
			</select>
			<p />
			<c:forEach items="${TablesInfos}" var="info">
				<div id="${info.tablecode}"
					<c:if test="${info.tablecode eq editTablecode}"> style="display: block"</c:if>
					<c:if test="${not (info.tablecode eq editTablecode) }"> style="display: none"</c:if>>
					<table class="list" style="min-width: 1000px; width: 98%">
						<thead align="center" style="">
							<tr>
								<th width="8%" rowspan=2>列表名称</th>
								<th width="20%" colspan=4>目标数据(数据库类型： <c:if
										test="${'1'==info.aTableType }">
										<c:out value="SQL" />
									</c:if> <c:if test="${'2'==info.aTableType }">
										<c:out value="ORCAL" />
									</c:if> <c:if test="${'3'==info.aTableType }">
										<c:out value="DB2" />
									</c:if> <c:if test="${'4'==info.aTableType }">
										<c:out value="MYSQL" />
									</c:if> <c:if test="${'5'==info.aTableType }">
										<c:out value="ACCESS" />
									</c:if>)
								</th>
								<th width="20%" colspan=4>元数据(数据库类型： <c:if
										test="${'1'==info.bTableType }">
										<c:out value="SQL" />
									</c:if> <c:if test="${'2'==info.bTableType }">
										<c:out value="ORCAL" />
									</c:if> <c:if test="${'3'==info.bTableType }">
										<c:out value="DB2" />
									</c:if> <c:if test="${'4'==info.bTableType }">
										<c:out value="MYSQL" />
									</c:if> <c:if test="${'5'==info.bTableType }">
										<c:out value="ACCESS" />
									</c:if>)
								</th>
								<th width="10%" rowspan=2>操作</th>
							</tr>
							<tr>
								<th width="10%">数据类型</th>
								<th width="10%">长度</th>
								<th width="10%">精度</th>
								<th width="10%">是否是主键</th>
								<th width="10%">数据类型</th>
								<th width="10%">长度</th>
								<th width="10%">精度</th>
								<th width="10%">是否是主键</th>
							</tr>
						</thead>
						<c:if test="${-1==info.bTablesize }">
							<c:forEach items="${info.mdColumns}" var="dataCompare"
								varStatus="status">
								<tr>
									<td width="8%">${dataCompare.aColumn.cid.colcode}</td>
									<td width="10%">${dataCompare.aColumn.coltype}</td>
									<td width="10%">${dataCompare.aColumn.colLength}</td>
									<td width="10%">${dataCompare.aColumn.colPrecision}</td>
									<td width="10%">${dataCompare.aColumn.primaryKey}</td>
									<td width="10%">${dataCompare.bColumn.coltype}</td>
									<td width="10%">${dataCompare.bColumn.colLength}</td>
									<td width="10%">${dataCompare.bColumn.colPrecision}</td>
									<td width="10%">${dataCompare.bColumn.primaryKey}</td>
									<c:if test="${status.first}">
										<td width="10%" align="center" valign="middle"
											rowspan="${info.aTablesize}">&nbsp;&nbsp;<input
											type=button id="${dataCompare.conclusion}"
											onClick="addTable('${info.tablecode}');" value="添加表"></td>
									</c:if>
								</tr>
							</c:forEach>
						</c:if>
						<c:if test="${-1<info.bTablesize }">
							<c:forEach items="${info.mdColumns}" var="dataCompare"
								varStatus="status">
								<%-- <c:if test="${not ('equal' eq dataCompare.conclusion ) }"> 用于控制置显示不同之处--%>
								<tr>
									<td width="8%">${dataCompare.aColumn.cid.colcode}</td>
									<td width="10%">${dataCompare.aColumn.coltype}</td>
									<td width="10%">${dataCompare.aColumn.colLength}</td>
									<td width="10%">${dataCompare.aColumn.colPrecision}</td>
									<td width="10%">${dataCompare.aColumn.primaryKey}</td>
									<td width="10%">${dataCompare.bColumn.coltype}</td>
									<td width="10%">${dataCompare.bColumn.colLength}</td>
									<td width="10%">${dataCompare.bColumn.colPrecision}</td>
									<td width="10%">${dataCompare.bColumn.primaryKey}</td>
									<td width="10%" align="center" valign="middle"><c:if
											test="${'addColumn' eq dataCompare.conclusion }">&nbsp;&nbsp;<input
												type=button
												onClick="addColumn('${info.tablecode}','${dataCompare.aColumn.cid.colcode}');"
												value="添加字段">
										</c:if> <c:if test="${'editColtype' eq dataCompare.conclusion }">&nbsp;&nbsp;<input
												type=button
												onClick="editColtype('${info.tablecode}','${dataCompare.aColumn.cid.colcode}'););"
												value="修改字段类型" id="${dataCompare.conclusion}">
										</c:if> <c:if test="${'editColLength' eq dataCompare.conclusion }">&nbsp;&nbsp;<input
												type=button
												onClick="editColLength('${info.tablecode}','${dataCompare.aColumn.cid.colcode}');"
												value="修改字段大小" id="${dataCompare.conclusion}">
										</c:if> <c:if test="${'editColPrecision' eq dataCompare.conclusion }">&nbsp;&nbsp;<input
												type=button
												onClick="editColPrecision('${info.tablecode}','${dataCompare.aColumn.cid.colcode}');"
												value="修改字段精度" id="${dataCompare.conclusion}">
										</c:if> <c:if test="${'setPrimaryKey' eq dataCompare.conclusion }">&nbsp;&nbsp;<input
												type=button
												onClick="setPrimaryKey('${info.tablecode}','${dataCompare.aColumn.cid.colcode}');"
												value="设置为主键" id="${dataCompare.conclusion}">
										</c:if></td>
								</tr>
								<%-- </c:if> --%>
							</c:forEach>
						</c:if>
					</table>
				</div>
			</c:forEach>

		</div>
	</form>
</div>
<script type="text/javascript">
	function showTableCol() {
		<c:forEach items="${TablesInfos}" var="info">
		var tablenameid = "${info.tablecode}";
		$("#" + tablenameid).hide();
		</c:forEach>
		$("#" + $("#dbtableName").val()).show();
	}
	function addTable(tablecode) {
		if (window.confirm("确实要添加表" + tablecode + "?")) {
			$("#datacompareFrom").loadUrl("${contextPath }/dde/datacompare!addTable.do", {editTablecode: tablecode,databasename: '${databasename}'});
		} else {
			return false;
		}
	}
	function addColumn(tablecode,colcode){
		$("#datacompareFrom").loadUrl("${contextPath }/dde/datacompare!addoreditColumn.do", {dealColType: 'addColumn',editTablecode: tablecode,editColcode: colcode,databasename: '${databasename}'});
	}
	function editColtype(tablecode,colcode){
		if(window.confirm("确实要改变"+colcode+"的字段属性?")){
			$("#datacompareFrom").loadUrl("${contextPath }/dde/datacompare!addoreditColumn.do", {dealColType: 'editColtype',editTablecode: tablecode,editColcode: colcode,databasename: '${databasename}'});
		}else{
			return false;
		}
	}
    function editColLength(tablecode,colcode){
    	$("#datacompareFrom").loadUrl("${contextPath }/dde/datacompare!addoreditColumn.do", {dealColType: 'editColLength',editTablecode: tablecode,editColcode: colcode,databasename: '${databasename}'});
	}
    function editColPrecision(tablecode,colcode){
    	$("#datacompareFrom").loadUrl("${contextPath }/dde/datacompare!addoreditColumn.do", {dealColType: 'editColPrecision',editTablecode: tablecode,editColcode: colcode,databasename: '${databasename}'});
	}
	function setPrimaryKey(tablecode,colcode){
		$("#datacompareFrom").loadUrl("${contextPath }/dde/datacompare!addoreditColumn.do", {dealColType: 'setPrimaryKey',editTablecode: tablecode,editColcode: colcode,databasename: '${databasename}'});
	}
</script>
