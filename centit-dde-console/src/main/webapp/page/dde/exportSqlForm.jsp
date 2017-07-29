<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageContent">
	<s:form action="/dde/exportSql!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
       	<input name="exportId" type="hidden" size="40" value="${object.exportId }" />
       	<input name="tabid" type="hidden" size="40" value="${param.tabid }" />
		<div class="pageFormContent" layoutH="58">		
			<div class="unit">
				<label>导出名称：</label> <input name="exportName" type="text" size="40" value="${object.exportName }" class="required" />

			</div>

			<div class="unit">
				<label>业务系统ID：</label> <input name="sourceOsId" type="text" size="40" value="${object.sourceOsId }" class="required" />

			</div>

			<div class="unit">
				<label>源数据库名：</label> 
				<select name="sourceDatabaseName" id="">
					<c:forEach var="db" items="${dbList }">
						<option value="${db.databaseName }" <c:if test="${db.databaseName eq object.sourceDatabaseName }">selected="selected"</c:if> >${db.databaseName }</option>
					</c:forEach>
				</select> 

			</div>


			<div class="unit">
				<label>数据源Sql语句：</label> <textarea name="querySql" rows="6" cols="50" class="required">${object.querySql }</textarea>

			</div>

			<div class="unit">
				<label>导出后处理语句：</label> <textarea name="afterSqlBlock" rows="6" cols="50">${object.afterSqlBlock }</textarea>

			</div>

			<div class="unit">
				<label>导出前预处理语句：</label> <textarea name="beforeSqlBlock" rows="6" cols="50">${object.beforeSqlBlock }</textarea>

			</div>
			<div class="unit">
				<label>导出说明：</label> <textarea name="exportDesc" rows="6" cols="50">${object.exportDesc }</textarea>

			</div>
		</div>
			
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">保存</button>
						</div>
					</div></li>
				<li>
					<div class="button">
						<div class="buttonContent">
							<button type="button" class="close">取消</button>
						</div>
					</div>
				</li>
			</ul>
		</div>

	</s:form>
</div>



