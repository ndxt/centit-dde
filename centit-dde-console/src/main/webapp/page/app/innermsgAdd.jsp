<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageContent">
	<s:form action="/app/innermsg!saveMsg.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input type = "hidden" name="msgtype" value="${object.msgtype }"/>
        <input id="hid_innermsg_add" type = "hidden" name="filecodes" />
		<div class="pageFormContent nowrap" layoutH="56">
			<dl>
				<dt>主题：</dt>
				<dd>
					<input name="msgtitle" type="text" maxlength="128" size="58" class="required" value="${object.msgtitle }" />
				</dd>
			</dl>
			<c:if test="${(cp:MAPVALUE('MSG_TYPE', 'P') eq object.msgtype) }">
				<dl>
					<dt>收件人：</dt>
					<dd>
						<textarea id="txa_innermsg_receive_name" name="receivename" rows="3" cols="50" class="required" readonly="readonly"></textarea>
						<input id="txt_innermsg_receive_usercode" name="receiveUserCode" type="hidden" class="required" /> <a href="${contextPath }/app/innermsg!innermsgTree.do" rel="innermsgAdd"
							height="500" height="300" target="dialog" mask="true">添加收件人</a>
					</dd>
				</dl>
			</c:if>
			<dl>
				<dt>内容：</dt>
				<dd>
					<textarea name="msgcontent" rows="3" cols="50" class="required">${object.msgcontent }</textarea>
				</dd>
			</dl>
            <dl>
				<dt>附件：</dt>
				<dd>
                    <input type="file" id="upload-fileinfo" optID="MSG" inputID="hid_innermsg_add" />
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

