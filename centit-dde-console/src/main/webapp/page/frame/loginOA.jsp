<%@ page contentType="text/html;charset=UTF-8" %>
<HTML>
<HEAD>
<TITLE> 登录 Domino 系统 </TITLE>
<META NAME="Generator" CONTENT="EditPlus"/>
<META NAME="Author" CONTENT=""/>
<META NAME="Keywords" CONTENT=""/>
<META NAME="Description" CONTENT=""/>
</HEAD>

<BODY onload="document.all._DominoForm.submit();">
<form action="http://10.32.1.10/names.nsf?Login" NAME="_DominoForm" method="post">
	<input type="hidden" name="Username" value="${session.SPRING_SECURITY_CONTEXT.authentication.principal.usercode}" />
	<input type="hidden" name="Password" value="${session.SPRING_SECURITY_CONTEXT.authentication.principal.userPwd}" />
	<input type="hidden" name="RedirectTo" value="/jjwoa.nsf" />
</form>
</BODY>
</HTML>