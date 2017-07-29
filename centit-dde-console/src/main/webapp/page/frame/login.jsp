<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<title><c:out value="${cp:MAPVALUE('SYSPARAM','SysName')}" />系统登录</title>
        <c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
		<style type="text/css">
<!--
body,td,th {
	font-size: 12px;
	color: #999;
}

body {
	background-image: url(${ctx}/styles/images/login/loginbg.jpg);
	margin-top: 100px;
}

.box{width:1024px; height:600px; background:url(${ctx}/images/login/bg.jpg); position:relative; margin:0 auto;}
.logintext{position:absolute; top:200px; left:680px;}
.logintext td{color:#fff; font-weight:bold;}
.download{position:absolute; top:440px; left:50px;}
a.a2:link{font-size:13px; color:#1c1c1c; text-decoration:none;}
a.a2:hover{font-size:13px; color:#1c1c1c; text-decoration:underline;}
a.a2:active{font-size:13px; color:#1c1c1c; text-decoration:none;}
a.a2:visited{font-size:13px; color:#1c1c1c; text-decoration:none;}

.style1 {
	color: #FFFFFF
}

a {
	font-size: 12px;
	color: #666;
}

a:visited {
	color: #666;
	text-decoration: none;
}

a:hover {
	color: #666;
	text-decoration: underline;
}

a:active {
	color: #03C;
	text-decoration: none;
}

a:link {
	text-decoration: none;
}
.error { width:200px; padding:15px 20px; border:1px solid #ff0000; text-align:center; color:#ff0000; position:absolute; left:50%; top:10px; margin-left:-100px; background:#fff; }
-->
</style>
	</head>
    
	<body style="overflow-y:hidden">
		<s:form  method="post" id="loginForm" action="/j_spring_security_check" onsubmit="return submitAction();" namespace="/" focus="loginName">
			<s:hidden property="action" />
			<c:if test="${param.error != null}">
                    <div class="error" align="center">		                  
                        <c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'Bad credentials'}">
                           <span>用户名或者密码错误</span>
                        </c:if>
                        <c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'bad checkcode'}">
                           <span>验证码错误</span>
                        </c:if>				
                    </div>
                </c:if> 
			<%-- <table width="923" border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					<td style="width: 923px;height: 115px;background-image: url('${ctx}/styles/images/login/log_03.png')">
						
					</td>
				</tr>
			</table>
			
     
			<table width="923" border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					<td background="${ctx}/styles/images/login/log_05.png" width="602"
							height="241">
					</td>
					<td background="${ctx}/styles/images/login/log_06.png">
						<table width="66%" border="0" align="center" cellpadding="2"
							cellspacing="0">
							<tr>
								<td align="left">
									<span class="style1">登录名1</span>
								</td>
							</tr>
							<tr>
								<td align="left">
									<input type="text" id="username" name="j_username" value="${SPRING_SECURITY_LAST_USERNAME}"
										style="background: url(${ctx}/styles/images/login/inp_09.jpg) no-repeat; width: 177px; height: 30px; border: 0; line-height: 30px; padding-left: 10px;">
								</td>
							</tr>
							<tr>
								<td align="left">
									<span class="style1">密码</span>
								</td>
							</tr>
							<tr>
								<td align="left">
									<input type="password" name="j_password" id="password" 
										autocomplete=off
										style="background: url(${ctx}/styles/images/login/inp_09.jpg) no-repeat; width: 177px; height: 30px; border: 0; line-height: 30px; padding-left: 10px;">
								</td>
							</tr>
							<tr>
								<td align="left">
									<span class="style1">验证码</span>
								</td>
							</tr>
							<tr>
								<td align="left">
									<input type="text" id="check_num" name="j_checkcode"  
										onkeydown="if (event.keyCode==13) {submitAction();return true;}"
										style="width: 80px" maxlength="4" />
								    <img alt="看不清，点击换一张" style="cursor: hand" 
											src="<c:url value='/sys/userDef!captchaimage.do'/>" 
										width="80" height="24" id="safecode" onclick="this.src='<%=request.getContextPath()%>/sys/userDef!captchaimage.do?key='+Math.random();" />
								</td>
							</tr>
							<tr>
								<td align="right">
									<s:submit  value="登录" style="background: url(${ctx}/styles/images/login/inp_20.jpg); border: 0; width: 79px; height: 33px; color: #FFFFFF; font: 14px/ 33px; margin-right: 10px;"
										cssClass="btn" />
								</td>
							</tr>
						</table>
					</td>
					<td background="${ctx}/styles/images/login/log_07.png" width="81"
							height="241">
					</td>
				</tr>
			</table> --%>
			
			<div class="box" style="position:absolute;left:55%;margin-left:-550px;top:50%;margin-top:-300px;width:1000px;height:500px;">
				  <table width="253"  height="200" border="0" cellspacing="0" cellpadding="0" class="logintext">
				  <tr>
				    <td width="78">登录名:</td>
				    <td colspan="2" align="left">
				    	<input type="text" id="username" name="j_username" value="<%--${SPRING_SECURITY_LAST_USERNAME}--%>" style=" width:160px; height:25px; line-height:25px; background-color:#fff; border:1px solid #828282;" />
				    </td>
				    </tr>
				  <tr>
				    <td>密码:</td>
				    <td colspan="2" align="left">
				    	<input type="password" name="j_password" id="password" value=""
							autocomplete=off style=" width:160px; height:25px; line-height:25px; background-color:#fff; border:1px solid #828282;" />
				    </td>
				    </tr>
				  <tr>
				    <td>验证码:</td>
				    <td width="87" align="left">
				    	<input type="text" id="check_num" name="j_checkcode" onkeydown="if (event.keyCode==13) {submitAction();return true;}"
										style=" width:80px; height:25px; line-height:25px; background-color:#fff; border:1px solid #828282;" maxlength="4" />
				    </td>
				    <td width="88" align="left">
				    	<img alt="看不清，点击换一张" style="cursor: hand" src="<c:url value='/sys/userDef!captchaimage.do'/>" 
							width="74" height="25" id="safecode" onclick="this.src='<%=request.getContextPath()%>/sys/userDef!captchaimage.do?key='+Math.random();" />
				    </td>
				  </tr>
				  <tr>
				    <td colspan="3" align="center">
				    	<input type="image" name="imageField" id="imageField" src="${ctx}/images/login/login_07.png" />
				    </td>
				    </tr>
				</table>
				<%-- <table width="250" height="33" border="0" cellspacing="0" cellpadding="0" class="download">
				  <tr>
				    <td  width="38"><img src="${ctx}/images/login/sp.png" width="32" height="33" /></td>
				    <td align="left"><a href="#"  class="a2">控件下载</a></td>
				    <td  width="38"><img src="${ctx}/images/login/ppt.png" width="32" height="33" /></td>
				    <td align="left"><a href="#"  class="a2">系统帮助</a></td>
				  </tr>
				</table> --%>
			</div>
			
		</s:form>
	</body>
<script language="JavaScript">
 var isV=0;
	function submitAction() {
	    if(document.getElementById("username").value==null || document.getElementById("username").value==""){
	      alert("请输入用户名！");
	      return false;
	    }else if(document.getElementById("password").value==null || document.getElementById("password").value==""){
	      alert("请输入密码！");
	      return false;
	    }else if(document.getElementById("check_num").value==null || document.getElementById("check_num").value==""){
	      //alert("请输入验证码!");
	      //return false;
	    }
	};
	/*if(${param.error != null}){
		if(${SPRING_SECURITY_LAST_EXCEPTION.message eq 'Bad credentials'}){
		      //checkCode();
		      alert("用户名或者密码错误");
		}else if(${SPRING_SECURITY_LAST_EXCEPTION.message eq 'bad checkcode'}){
			  alert("验证码错误");
		}
	}*/
</script>
</html>
