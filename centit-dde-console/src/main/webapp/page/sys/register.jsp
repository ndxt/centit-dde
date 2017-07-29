<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/formValidator.jsp"%>
<html>
	<head><meta name="decorator" content='none'/>
		<title>用户注册</title>
		<style type="text/css">
		<!--
		body {
			background-image: url(${STYLE_PATH}/images/bg.gif);
		}
		td{
			font-size:12px;
			color:#333;
		}
		-->
		</style>
	<script type="text/javascript">
	
		
		$(document).ready(function(){
			
			$.formValidator.initConfig({formid:"form1",
				onerror:function(msg,obj,errorlist){
					alert(msg);
				}
			});
			$("#loginname").formValidator().inputValidator({min:1,max:10,onerror:"输入1到10个字符"})
			.regexValidator({regexp:"username",datatype:"enum",onerror:"输入字母或者数字"});
			$("#p1").formValidator().inputValidator({min:1,empty:{leftempty:false,rightempty:false,emptyerror:"密码两边不能有空符号"},onerror:"密码不能为空,请确认"});

			$("#p2").formValidator().inputValidator({min:1,empty:{leftempty:false,rightempty:false,emptyerror:"重复密码两边不能有空符号"},onerror:"重复密码不能为空,请确认"}).compareValidator({desid:"p1",operateor:"=",onerror:"  "});
		});

	</script>
	</head>
	
	<body bgcolor="#FFFFFF" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >	
		<s:form id="form1" action="userDef" namespace="/sys" >
			<input type="hidden" name="userPwd.captchakey" value="${userPwd.captchaKey}"/>
		<table width="1000" height="600" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td colspan="3" >
					<img src="${STYLE_PATH}/images/frame/login_01.jpg"  width="1000" height="175" alt=""></td>
			</tr>
			<tr>
				<td rowspan="3" >
					<img src="${STYLE_PATH}/images/frame/login_02.jpg" width="536" height="425" alt=""></td>
				<td >
					<img src="${STYLE_PATH}/images/frame/login_03.jpg" width="269" height="84" alt=""></td>
				<td rowspan="3" >
					<img src="${STYLE_PATH}/images/frame/login_04.jpg" width="195" height="425" alt=""></td>
			</tr>
			<tr>
				<td width="250" height="178" background="${STYLE_PATH}/images/frame/login_05.jpg">
					<c:if test="${errmsg != null}">
					    <div class="error">
					        <img src="${STYLE_PATH}/images/icon/iconWarning.gif"/>
					        <c:out value="${errmsg}" />
					    </div>
					</c:if>
								
				<table width="104%" height="103"  border="0"  cellpadding="1" cellspacing="0">
			          <tr>
			            <td width="40%" >登录名*</td>
			            <td width="60%" >
			            <input type="text" id="loginname" name="loginname" class="text" style="width:120px; height:18px;"></td>
			          </tr>
			          <tr>
			            <td>显示姓名*</td>
			            <td><input type="text" name="username" class="text" style="width:120px; height:18px;"></td>
			          </tr>	          
			          <tr>
			            <td>确认Email*</td>
			            <td><input type="text" name="regemail" class="text" style="width:120px; height:18px;"></td>
			          </tr>	          
			          <tr>
			            <td>密码*</td>
			            <td><input type="password" id="p1" name="password" class="text" style="width:120px; height:20px;"></td><td> <span id="p1Tip" style="vertical-align:middle; "></span></td>
			            
			          </tr>
			          <tr>
			            <td>确认密码*</td>
			            <td><input type="password" id="p2" name="password2" class="text" style="width:120px; height:20px;vertical-align:middle;"><span id="p2Tip" style="vertical-align:middle;padding-bottom:3px "></span></td>
			          </tr>	  
			                  
			          <tr>
			            <td>验证码*</td>
			   				 <td style="width: 70px"><input type="text" name="captchavalue" class="text" style="width:60px; height:18px;">	</td>
			   				<td><img height="20px" src="<c:url value='/sys/userDef!captchaimage.do?userPwd.captchaKey=${userPwd.captchaKey}'/>"/></td>
			          </tr>
			          <tr>
			            <td>&nbsp;</td>
			            <td valign="bottom">
			            	<s:submit method="register"  cssClass="btn"  value="注册"></s:submit>
			            	<s:reset  cssClass="btn" value="清空"></s:reset>			               
			          </tr>
		          </table>
	          </td>
			</tr>
			<tr>
				<td>
					<img src="${STYLE_PATH}/images/frame/login_06.jpg" width="269" height="163" alt=""></td>
			</tr>
		</table>			
			
		</s:form>

	</body>
</html>
