<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div id="div_import_field_form" class="pageContent">
	<s:form action="#" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input id="hid_import_field_form_columnno" type="hidden" name="columnNo" value="${param.columnNo}"/>
		<div class="pageFormContent" layoutH="58">		
			<div class="unit">
				<label>源字段名：</label> <input id="txt_field_name_import" name="sourceFieldName" type="text" size="40" value="" class="required" />

			</div>

			<div class="unit">
				<label>目标字段名：</label> 
				<input name="destFieldName" type="text" size="40" value="" />

			</div>


			<div class="unit">
				<label>目标字段类型：</label> 
				
				<input name="destFieldType" type="text"  value="" />

			</div>

			<div class="unit">
				<label>是否为主键：</label> 
				<input id="chk_ispk" name="isPk" type="checkbox" size="40"  value="1" />

			</div>

			<div class="unit">
				<label>目标是否可以为空：</label> 
				<input id="chk_isNull" name="isNull" type="checkbox" size="40"  value="1" />

			</div>
			
			<div class="unit">
				<label>目标字段默认值：</label> <textarea id="txa_field_sentence_import" name="destFieldDefault" rows="6" cols="50"></textarea>

			</div>
		</div>
			
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button id="btn_import_field" type="button">保存</button>
						</div>
					</div></li>
				<li>
					<div class="button">
						<div class="buttonContent">
							<button id="btn_export_field_close" type="button" class="close">取消</button>
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
		port.bind("bindExportField");
	});
</script>

