<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<script type="text/javascript">
<!--
	$(function() {
		//Ajax 验证邮箱有效性
	});
//-->
</script>


<div class="pageContent">
	<s:form action="/app/userMailConfig!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input type="hidden" name="emailid" value="${object.emailid }" />
		<div class="pageFormContent" layoutH="56">
			<p>
				<label>邮箱账号：</label> <input name="mailaccount" class="required email" maxlength="64" type="text" <c:if test="${!empty object.mailaccount }">readonly="readonly"</c:if> size="40" value="${object.mailaccount }" />

			</p>

			<p>
				<label>邮箱密码：</label> <input name="mailpassword" type="password" class="required" maxlength="256" <c:if test="${!empty object.mailpassword }">readonly="readonly"</c:if> size="40" value="${object.mailpassword }" />

			</p>

			<p>
				<label>邮件发送类型：</label> <select name="mailsendtype">
					<c:forEach var="c" items="${cp:DICTIONARY_D('MAIL_SEND_TYPE') }">
						<option value="${c.id.datacode }">${c.datadesc }</option>
					</c:forEach>
				</select>

			</p>
			<p>
				<label>邮件收取类型：</label> <select name="mailreceivetype">
					<c:forEach var="c" items="${cp:DICTIONARY_D('MAIL_RECEIVE_TYP') }">
						<option value="${c.id.datacode }">${c.datadesc }</option>
					</c:forEach>
				</select>

			</p>

			<p>
				<label>SMTP服务器url：</label> <input name="smtpurl" type="text" size="40" value="${object.smtpurl }" />

			</p>

			<p>
				<label>SMTP服务器端口：</label> <input name="smtpport" type="text" size="4" value="${empty object.smtpport ? 25 : object.smtpport }" />

			</p>

			<p>
				<label>接收服务器url：</label> <input name="receiveurl" type="text" size="40" value="${object.receiveurl }" />

			</p>

			<p>
				<label>接收服务器端口：</label> <input name="receiveport" type="text" size="4" value="${empty object.receiveport ? 110 : object.receiveport }" />

			</p>

			<p>
				<label>定时拉取邮件间隔时间：</label> <select name="intervaltime">
					<c:forEach var="i" items="5,10,15,30">
						<option value="${i }">${i } 分钟</option>
					</c:forEach>
				</select>
			</p>

			<p>
				<label>服务器邮件保留天数：</label> <select name="retaindays">
					<c:forEach var="i" items="1,5,10,15,30">
						<option value="${i }">${i } 天</option>
					</c:forEach>
				</select>
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

	</s:form>
</div>

