<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div id="div_export_trigger_form" class="pageContent">
	<s:form action="#" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input id="hid_export_trigger_form_triggerId" type="hidden" name="triggerId" value="${param.columnNo}"/>
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>执行语句：</label> 
				<textarea name="triggerSql" rows="6" cols="60" ></textarea>

			</div>
			
			<div class="unit">
				<label>执行语句说明：</label> <textarea name="triggerDesc" rows="6" cols="60"></textarea>

			</div>

			<div class="unit">
				<label>触发类别：</label> 
				
				<d title="行触发器（迁移时对每一条数据执行一遍）"><input name="triggerType" type="radio"  value="L" />行触发器</d>
				<d title="表触发器（数据迁移前或者 完成时执行，一个对应关系只执行一次）"><input name="triggerType" type="radio"  value="T" />表触发器</d>

			</div>

			<div class="unit">
				<label>触发时机：</label> 
				<input name="triggerTime" type="radio"  value="B" />交换前
				<input name="triggerTime" type="radio"  value="A" />交换后
				<input name="triggerTime" type="radio"  value="E" />交换失败后

			</div>

			<!--<div class="unit">
				<label>触发器顺序：</label> 
				<input id="chk_isNull" name="isNull" type="checkbox" size="40"  value="1" />

			</div> -->
			
			<div class="unit">
				<label>无参数存储过程：</label> 
				<input name="isprocedure" type="checkbox" value="T" />

			</div>
		</div>
			
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button id="btn_export_trigger" type="button">保存</button>
						</div>
					</div></li>
				<li>
					<div class="button">
						<div class="buttonContent">
							<button id="btn_export_trigger_close" type="button" class="close">取消</button>
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
		var exportsql = new ExportSql();
		exportsql.pubfuns.exportTriggerInit('${param.columnNo}');
		exportsql.bind('bindTrigger');
	});
</script>

