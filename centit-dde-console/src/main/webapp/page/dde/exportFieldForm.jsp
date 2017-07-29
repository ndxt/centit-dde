<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageContent">
	<s:form action="/dde/exportSql!saveField.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
       	<input name="exportField.cid.exportId" type="hidden" size="40" value="${object.exportField.cid.exportId }" />
       	<input name="exportField.cid.columnNo" type="hidden" size="40" value="${object.exportField.cid.columnNo }" />
       	<input name="exportId" type="hidden" size="40" value="${object.exportId }" />
       	
       	
		<div class="pageFormContent" layoutH="58">		
			<div class="unit">
				<label>源字段名：</label> <input name="exportField.fieldName" type="text" size="40" value="${object.exportField.fieldName }" readonly="readonly" class="required" />

			</div>

			<div class="unit">
				<label>源字段类型：</label> <input name="exportField.fieldType" type="text" size="40" value="${object.exportField.fieldType }" />

			</div>

			<div class="unit">
				<label>字段格式：</label> 
				<input name="exportField.fieldFormat" type="text" size="40" value="${object.exportField.fieldFormat }" />

			</div>


			<div class="unit">
				<label>存储类型：</label> 
				
				
				<input name="exportField.fieldStoreType" type="radio" <c:if test="${'F' ne object.exportField.fieldStoreType }">checked="checked"</c:if> value="F" /> infile
				<input name="exportField.fieldStoreType" type="radio" <c:if test="${'E' eq object.exportField.fieldStoreType }">checked="checked"</c:if> value="E" /> embedded 

			</div>

			<div class="unit">
				<label>是否为主键：</label> <input name="exportField.isPk" type="checkbox" size="40" <c:if test="${'T' eq object.exportField.isPk }">checked="checked"</c:if> value="T" />

			</div>

			<div class="unit">
				<label>源字段语句：</label> <textarea name="exportField.fieldSentence" rows="6" cols="50">${object.exportField.fieldSentence }</textarea>

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



