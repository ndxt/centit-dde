<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">
	<form method="post" action="optDef!save.do"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone);">
		
		<c:if test="${not empty  optid}" >
		<input type="hidden" name="optid" value="${optid }"/>
		</c:if>
		<c:if test="${not empty  optcode}" >
		<input type="hidden" name="optcode" value="${optcode }"/>
		</c:if>
		<div class="pageFormContent" layoutH="60">
			<p>
				<label class="required">业务代码：</label>
				<label>${object.optid }</label>
				<%-- <s:textfield name="optid" rows="1" readonly="true" /> --%>
			</p>

			<p>
				<label>操作代码：</label>
				<c:if test="${not empty optcode}">
					<label>${object.optcode }</label>
					<%-- <s:textfield name="optcode" rows="1" cols="40" readonly="true" /> --%>
				</c:if>
				<c:if test="${empty optcode}">
					<s:textfield name="optcode" rows="1" cols="40" />
				</c:if>
			</p>
			
			<p>
				<label>操作方法：</label>
				<s:textfield name="optmethod" rows="1" cols="40" cssClass="required"/>
			</p>
			
			<p>
				<label>方法名称：</label>
				<s:textfield name="optname" rows="1" cols="40" cssClass="required"/>
			</p>
			
			<p style="width:100%">
				<label>方法说明：</label>
				<s:textarea name="optdesc" rows="2" cols="40" />
			</p>
			
			<p>
				<label>是否为流程操作：</label>
				<input type="radio" name=isinworkflow value="F" <c:if test="${isinworkflow=='F' }">checked="checked"</c:if> />否
				<input type="radio" name=isinworkflow value="T" <c:if test="${isinworkflow=='T' }">checked="checked"</c:if> />是
			</p>
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
	</form>
</div>
