<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div id="div_exchange_field_form" class="pageContent">
	<s:form action="#" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input id="hid_exchange_field_form_columnno" type="hidden" name="columnNo" value="${param.columnNo}"/>
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>源字段名：</label> <input id="txt_field_name" name="sourceFieldName" type="text" size="40" />

			</div>
			
			<div class="unit">
				<label>字段类型：</label> <input id="txt_sourceFieldType" name="sourceFieldType" type="text" size="40" />
			</div>
			
			<div class="unit">
				<label >源字段描述：</label> <input id="txt_sourceFieldSentence" name="sourceFieldSentence" type="text" size="36" />
                                            <span title="如描述与字段名不一致，直接写 变量或函数，保存时会自动拼接 as 源字段名">提示</span>
			</div>

			<div class="unit">
				<label>目标字段名：</label> <input id="txt_destFieldName" name="destFieldName" type="text" size="40" />
			</div>


			<div class="unit">
				<label>目标字段类型：</label> <input name="destFieldType" type="text" size="40"  />
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
				<label>常量（高优先级）：</label> <input id="txt_destFieldDefault" name="destFieldDefault" type="text" size="36" class="destFieldDefault" />
                                        <span title="常量为不经过源字段查询而直接将常量值插入目标字段">提示</span>
			</div>
		</div>
			
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button id="btn_exchange_field" type="button">保存</button>
						</div>
					</div></li>
				<li>
					<div class="button">
						<div class="buttonContent">
							<button id="btn_exchange_field_close" type="button" class="close">取消</button>
						</div>
					</div>
				</li>
			</ul>
		</div>

	</s:form>
</div>

<script src="${pageContext.request.contextPath }/scripts/module/dde/exchange/exchange.js"></script>
<script src="${pageContext.request.contextPath }/scripts/module/dde/public.js"></script>
<script>
	$(function(){
		var port = new ExportSql();
		port.pubfuns.exportFieldInit('${param.columnNo}');
		port.bind("bindToUpperCase");
		port.bind("bindExportField");
	});
</script>

