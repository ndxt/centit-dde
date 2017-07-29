<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div id="div_dataoptinfo_field_form" class="pageContent">
	<s:form action="#" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input id="hid_dataoptinfo_field_form_columnno" type="hidden" name="columnNo" value="${param.columnNo}"/>
		<div class="pageFormContent" layoutH="58">		
			<div id="div_optType" class="unit">
				<label>操作类型：</label> 
				
				<input type="radio" name="optType" value="1" checked="checked" />导入
				<input type="radio" name="optType" value="2" />调用接口

			</div>

			<div id="div_dataoptinfo_importId" class="unit">
				<label>导入操作：</label> 
				<select name="importId" id="sel_dataoptinfoId">
					<c:forEach var="obj" items="${importOpts }">
						<option value="${obj.importId }">${obj.importName }</option>
					</c:forEach>
				</select>

			</div>


			<div id="div_dataoptinfo_osId" class="unit" style="display: none;">
				<label>业务系统：</label> 
				
				<select name="osId" id="sel_dataoptinfo_osId">
					<c:forEach var="obj" items="${osInfos }">
						<option value="${obj.osId }">${obj.osName }</option>
					</c:forEach>
				</select>

			</div>

			
		</div>
			
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button id="btn_dataoptinfo_field" type="button">保存</button>
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

<script src="${pageContext.request.contextPath }/scripts/module/dde/dataoptinfo/dataoptinfo.js"></script>
<script>
	$(function(){
		var port = new ExportSql();
		port.pubfuns.exportFieldInit('${param.columnNo}');
		port.bind("bindExportField");
		port.bind("bindOptType");
	});
</script>

