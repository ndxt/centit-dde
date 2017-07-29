<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">

	<s:form id="form1" action="userDef!modifypwd.do" namespace="/sys"
		cssClass="pageForm required-validate">
		<div class="pageFormContent" layoutH="58">

			
			<div class="unit">
				<label>用户名：</label>
				<authz:authentication property="name" />
			</div>

			<div class="unit">
				<label>旧密码：</label> <input type="password"
					name="userMPwd.oldPassword" minlength="6" maxlength="20"
					alt="字母、数字、下划线 6-20位" class="required alphanumeric">
			</div>

			<div class="unit">
				<label>新密码：</label> <input type="password" id="cp_newPassword"
					name="userMPwd.newPassword" minlength="6" maxlength="20"
					alt="字母、数字、下划线 6-20位" class="required alphanumeric">
			</div>

			<div class="unit">
				<label>确认新密码：</label> <input type="password"
					equalTo="#cp_newPassword" name="userMPwd.confirmPassword"
					minlength="6" maxlength="20" class="required alphanumeric">
			</div>

		</div>
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit"
								onclick="return validateCallback(document.all.form1, dialogAjaxDone)">提交</button>
						</div>
					</div></li>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="button" class="close">取消</button>
						</div>
					</div></li>
			</ul>
		</div>
	</s:form>
</div>
