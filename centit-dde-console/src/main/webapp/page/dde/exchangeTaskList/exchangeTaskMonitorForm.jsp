<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<!-- <style>
 td {
	 height: 32px 
} 
</style> -->

<div class="pageContent" >
	<s:form action="/dde/exchangeTask!editAndsave.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="58">
			<input name="taskId" type="hidden" class="required" size="40" value="${object.taskId }" /> <input type="hidden" name="s_taskType" value="${s_taskType }" />
			<div class="unit">
				<label>任务名称：</label> <input name="taskName" type="text" size="40" value="${object.taskName }" />
			</div>
			<div class="unit">
				<label>任务执行定时器：</label> <input id="txt_task_cron" name="taskCron" type="text" size="37" value="${object.taskCron }" /> <a class="btnLook"
					href="${pageContext.request.contextPath }/dde/exchangeTask!generateCronExpression.do?taskCron=${object.taskCron }&txtCallback=txt_task_cron" target="dialog" rel="generateCronExpression"
					mask="true" suggestFields="taskCron" title="生成表达式向导" width="420" height="334"> <span>生成表达式向导</span>
				</a>
			</div>
			<div class="unit">
				<label>任务描述：</label> <input name="taskDesc" type="text" size="40" value="${object.taskDesc }" />
			</div>
			<div class="unit">
				<label>上次执行时间：</label> <input name="lastRunTime" type="text" class="date" format="yyyy-MM-dd HH:mm:ss" yearstart="-5" yearend="5" size="37"
					value="<fmt:formatDate value="${object.lastRunTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>" /> <a class="inputDateButton" href="javascript:;">选择</a>
			</div>
			<div class="unit">
				<label>下次执行时间：</label> <input name="nextRunTime" type="text" class="date" format="yyyy-MM-dd HH:mm:ss" yearstart="-5" yearend="5" size="37"
					value="<fmt:formatDate value="${object.nextRunTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>" /> <a class="inputDateButton" href="javascript:;">选择</a>
			</div>
			<div class="unit">
				<label>是否启用：</label> 
					<input type="radio" name="isValid" value="1" <c:if test="${1 eq object.isvalid }">checked="checked"</c:if> />启用 
					<input type="radio" name="isValid" value="0" <c:if test="${1 ne object.isvalid }">checked="checked"</c:if> />停用
			</div>
			<div class="unit">
				<c:if test="${!empty object.createTime }">
						<label>创建时间：</label> <input name="createTime" type="text" readonly="readonly" size="40" value="${object.createTime }" />
	
				</c:if>
			</div>
			<div class="unit">
				<c:if test="${!empty object.created }">
						<label>创建人员：</label> <input name="createdName" type="text" readonly="readonly" size="40" value="${object.createdName }" /> <input name="created" type="hidden" readonly="readonly" size="40"
							value="${object.created }" />
	
				</c:if>
			</div>
			<div class="unit">
				<label>监控文件夹：</label> <input name="monitorFolder" type="text" size="60" value="${object.monitorFolder }" required="required" maxlength="200"/>

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
							<button id="btn_export_field_close" type="button" class="close">取消</button>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</s:form>


</div>


