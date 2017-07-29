<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div id="div_data_source_page_content" class="pageContent">
	<s:form action="#" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDoneBack);">
       	<input name="exportId" type="hidden" size="40" value="${object.exportId }" />
       	<input name="tabid" type="hidden" size="40" value="${param.tabid }" />
       	
       	<%-- 取消的字段，使用隐藏域，防止出错 --%>
       	<input name="afterSqlBlock" type="hidden" size="40" value="${object.afterSqlBlock }" />
       	<input name="beforeSqlBlock" type="hidden" size="40" value="${object.beforeSqlBlock }" />
		<div class="pageFormContent" layoutH="58">		

			<div class="unit">
				<label>源数据库名：</label> 
				<select name="sourceDatabaseName" id="sel_sourcedb">
					<c:forEach var="db" items="${dbList }">
						<option value="${db.databaseName }" <c:if test="${db.databaseName eq object.sourceDatabaseName }">selected="selected"</c:if> >${db.databaseName }</option>
					</c:forEach>
				</select> 

			</div>


			<div class="unit">
				<label>数据源Sql语句：</label> <textarea name="querySql" rows="18" cols="85" class="required">${object.querySql }</textarea>

			</div>

		</div>
			
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button id="btn_def_datasource" type="button">保存</button>
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

<script src="${pageContext.request.contextPath }/scripts/module/dde/exportsql/exportsql.js"></script>
<script>
	$(function(){
		var port = new ExportSql();
		port.pubfuns.exportFieldInit('${param.columnNo}');
		port.bind("bindDefDatasource");
	});
</script>

