<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div id="div_data_source_page_content_import" class="pageContent">
	<s:form action="#" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDoneBack);">
       	<input name="exportId" type="hidden" size="40" value="${object.importId }" />
       	<input name="tabid" type="hidden" size="40" value="${param.tabid }" />
		<div class="pageFormContent" layoutH="58">		

			<div class="unit">
				<label>源数据库名：</label> 
				<select name="destDatabaseName" id="sel_sourcedb_import">
					<c:forEach var="db" items="${dbList }">
						<option value="${db.databaseName }" <c:if test="${db.databaseName eq object.destDatabaseName }">selected="selected"</c:if> >${db.databaseName }</option>
					</c:forEach>
				</select> 

			</div>


			<div class="unit">
				<label>导入表：</label> <input id="txt_tableName_import" type="text" name="tableName" value="${object.tableName }" />

			</div>
			
			<%-- <div class="unit">
				<label>记录操作：</label> 
				<input type="radio" name="recordOperate" value="1" <c:if test="${('2' ne object.recordOperate) and ('3' ne object.recordOperate) }">checked="checked"</c:if> />插入
				<input type="radio" name="recordOperate" value="2" <c:if test="${'2' eq object.recordOperate }">checked="checked"</c:if> />更新
				<input type="radio" name="recordOperate" value="3" <c:if test="${'3' eq object.recordOperate }">checked="checked"</c:if> />合并
			</div> --%>
			

		</div>
			
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button id="btn_def_datasource_import" type="button">保存</button>
						</div>
					</div></li>
				<li>
					<div class="button">
						<div class="buttonContent">
							<button id="btn_def_datasource_close" type="button" class="close">取消</button>
						</div>
					</div>
				</li>
			</ul>
		</div>

	</s:form>
</div>

<script src="${pageContext.request.contextPath }/scripts/module/dde/importsql/importsql.js"></script>
<script>
	$(function(){
		var port = new ExportSql();
		port.pubfuns.exportFieldInit('${param.columnNo}');
		port.bind("bindDefDatasource");
	});
</script>

