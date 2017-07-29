<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/dde/dataOptInfo!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input name="dataOptId" type="hidden" value="${object.dataOptId }" />
		<div class="pageFormContent" layoutH="56">



			<div class="unit">
				<label>处理名称：</label> <input name="optName" type="text" size="40" value="${object.optName }" />

			</div>

			<div class="unit">
				<label>处理说明：</label>
				<textarea name="optDesc" id="" cols="50" rows="5">${object.optDesc }</textarea>

			</div>


			<div class="unit subBar">
				<ul>
					<li style="float: right; margin-right: 50px;">
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">保存</button>
							</div>
						</div>


						<div class="buttonActive">
							<div class="buttonContent">
								<button onclick="navTab.closeCurrentTab('external_DATAOPTINFO')">返回</button>
							</div>
						</div>
					</li>
				</ul>
			</div>

			<div class="divider"></div>
			<div>
				<table class="list nowrap itemDetail" width="100%">
					<thead>
						<tr>




							<th type="text" name="dataOptStep.optStepId" fieldClass="required">业务系统</th>




							<th type="text" name="dataOptStep.importId" fieldClass="required">数据导入</th>



							<th type="text" name="dataOptStep.optType" fieldClass="required"><c:out value="dataOptStep.optType" /></th>



							<th type="text" name="dataOptStep.dataOptId" fieldClass="required"><c:out value="dataOptStep.dataOptId" /></th>



							<th type="text" name="dataOptStep.osId" fieldClass="required"><c:out value="dataOptStep.osId" /></th>



							<th type="text" name="dataOptStep.mapInfoOrder" fieldClass="required"><c:out value="dataOptStep.mapInfoOrder" /></th>



							<th type="del" width="60">操作</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>

		</div>



	</s:form>
</div>

