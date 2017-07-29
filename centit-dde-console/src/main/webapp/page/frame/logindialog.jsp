<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<div class="pageContent">
<s:form method="post" id="loginForm" action="/j_spring_security_check" namespace="/" cssClass="pageForm required-validate"  >

	<div class="pageFormContent" layoutH="58">

		<div class="unit">
			<label>登录名：</label>
			<input type="text" id="username" name="j_username" value="${SPRING_SECURITY_LAST_USERNAME}"   minlength="1" maxlength="60" class="required" >
		</div>
		<div class="unit">
			<label>密    码：</label>			
			<input type="password" name="j_password" id="password"  class="required" >
		</div>	
	</div>

	<div class="formBar">
		<ul>
			<li><div class="buttonActive"><div class="buttonContent"><button type="submit" onclick="return validateCallback(document.all.loginForm, dialogAjaxDone)">登录</button></div></div></li>
			<li><div class="buttonActive"><div class="buttonContent"><button type="button" class="close">取消</button></div></div></li>
		</ul>
	</div>

</s:form>
</div>
