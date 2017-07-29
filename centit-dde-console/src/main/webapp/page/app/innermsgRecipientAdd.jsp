<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/app/innermsg!saveMsg.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">

		<div class="pageFormContent nowrap" layoutH="56">
			<dl>
				<dt>主题：</dt>
				<dd>
					<input name="msgtitle" type="text" maxlength="128" class="required"/>
				</dd>
			</dl>
			<dl>
				<dt>收件人：</dt>
				<dd>
					<textarea rows="3" cols="50" class="required" disabled="disabled">${cp:MAPVALUE('usercode', object.innermsg.sender) }</textarea>
					<input name="receiveUserCode" type="hidden" class="required" value="${object.innermsg.sender }"/>
				</dd>
			</dl>
			<dl>
				<dt>内容：</dt>
				<dd>
					<textarea name="msgcontent" rows="3" cols="50" class="required"></textarea>
				</dd>
			</dl>
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
