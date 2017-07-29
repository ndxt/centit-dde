<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<script type="text/javascript">
<!--/* 替换保存文件夹类型 */
	$(function() {
		$('#btn_save_drafts').click(function() {
			$('#hid_mail_type').val("${cp:MAPVALUE('MAIL_TYPE', 'D') }");

			$('#frm_innermsg_add_mail').submit();
		});

		
		/* 清空空格 */
		var $txa = $('#frm_innermsg_add_mail').find('textarea');
		$.each($txa, function(index, txa) {
			$(txa).text($.trim($(txa).text()));
		});
	});
//-->
</script>

<div class="pageContent">
	<s:form id="frm_innermsg_add_mail" action="/app/innermsg!saveSendMail.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input id="hid_innermsg_mail_add" type="hidden" name="filecodes" />
        <input type="hidden" name="msgcode" value="${object.msgcode }" />
		<input type="hidden" name="emailid" value="${param['s_emailid'] }" />
		<input id="hid_mail_type" type="hidden" name="mailtype" value="${param['s_mailtype'] }" />
		<div class="pageFormContent nowrap" layoutH="56">
			<dl>
				<dt>主题：</dt>
				<dd>
					<input name="msgtitle" type="text" maxlength="128" size="100" class="required" value="${object.msgtitle }" />
				</dd>
			</dl>
			<dl>
				<dt>收件人：</dt>
				<dd>
					<textarea name="to" rows="3" cols="100" class="required">
						<c:forEach var="r" items="${object.innermsgRecipients }">
							<c:if test="${cp:MAPVALUE('RECIPIENT_TYPE', 'T') eq r.mailtype }">
								${r.receive };
							</c:if>
						</c:forEach>
					</textarea>

				</dd>
			</dl>
			<dl>
				<dt>抄送人：</dt>
				<dd>
					<textarea name="cc" rows="3" cols="100">
						<c:forEach var="r" items="${object.innermsgRecipients }">
							<c:if test="${cp:MAPVALUE('RECIPIENT_TYPE', 'C') eq r.mailtype }">
								${r.receive };
							</c:if>
						</c:forEach>
					</textarea>

				</dd>
			</dl>
			<dl>
				<dt>密送人：</dt>
				<dd>
					<textarea name="bcc" rows="3" cols="100">
						<c:forEach var="r" items="${object.innermsgRecipients }">
							<c:if test="${cp:MAPVALUE('RECIPIENT_TYPE', 'B') eq r.mailtype }">
								${r.receive };
							</c:if>
						</c:forEach>
					</textarea>

				</dd>
			</dl>
			<dl>
				<dt>附件：</dt>
				<dd>
                    <input type="file" id="upload-fileinfo" optID="MSG" inputID="hid_innermsg_mail_add" />
				</dd>
			</dl>

            <dl>
				<dt>内容：</dt>
				<dd>
					<textarea name="msgcontent" class="editor" rows="16" cols="100"></textarea>
				</dd>
			</dl>
		</div>


		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button id="btn_save_drafts" type="button">存草稿</button>
						</div>
					</div></li>
				<li>
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

