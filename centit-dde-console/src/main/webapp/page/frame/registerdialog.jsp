<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">
<s:form id="form1" action="userDef!register.do"  cssClass="pageForm required-validate">
<div class="pageFormContent" layoutH="58">
	
		
	<div class="unit">
		<label>登录名：</label> 
		<input type="text"  id="loginname" name="loginname"  minlength="1" 
			maxlength="12" class="required">
	</div>
	<div class="unit">
		<label>显示姓名：</label> 
		<input type="text"  name="username"  minlength="1"
			maxlength="12" class="required">
	</div>
	<div class="unit">
		<label>确认Email：</label> 
		<input type="text"  name="regemail"  minlength="5"
			maxlength="20" class="required email">
	</div>
	<div class="unit">
		<label>手机号码：</label> 
		<input type="text"  name="contactPhone"  minlength="5"
			maxlength="20" class="phone">
	</div>
	<div class="unit">
		<label>密码：</label> 
		<input type="password"  id="cp_newPassword"  name="password"  minlength="6"
			maxlength="8" alt="字母、数字、下划线 6-20位" class="required alphanumeric">
	</div>
	<div class="unit">
		<label>确认密码：</label> 
		<input type="password"  equalTo="#cp_newPassword" name="confirmPassword"  minlength="6"
			maxlength="8" class="required alphanumeric">
	</div>
	<div class="unit">
		<label>验证码：</label> 
		<input type="text"  name="captchaVal"  class="required">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<img height="20px" src="<c:url value='/sys/userDef!captchaimage.do?userPwd.captchaKey=${userPwd.captchaKey}'/>"/>
	</div>
	</div>
	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit" onclick="return validateCallback(document.all.form1, dialogAjaxDone)">确定</button></div></div></li>
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
		</ul>
	</div>
	

</s:form>
</div>