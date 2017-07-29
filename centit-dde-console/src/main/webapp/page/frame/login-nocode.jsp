<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp"%>
<html>
<head>
    <title>系统登录</title>
       <%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path +"/" ;

%>

    <c:if test="${empty STYLE_PATH}">
        <c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
        <c:set var="STYLE_PATH" value="${ctx}/styles/default" scope="session"/>
    </c:if>

    <style type="text/css">
        <!--
        .btn{
	border-right: #bbbbbb 1px solid;
	border-top: #bbbbbb 1px solid;
	border-left: #bbbbbb 1px solid;
	border-bottom: #bbbbbb 1px solid;
	padding-right: 2px;
	padding-top: 2px;
	padding-left: 2px;
	padding-bottom: -2px;
	font-size: 12px;
	filter: progid:dximagetransform.microsoft.gradient(gradienttype=0, startcolorstr=#ffffff, endcolorstr=#c3daf5);
	cursor: pointer; color: black;
	height:20px;
	text-align:center
}
        body {
            margin: 0;
            background-image: url(/images/bg.gif);
        }
       label.error {
           float: none;
           color: red;
           padding-left: .5em;
           vertical-align: top;
       }
        td {
            font-size: 12px;
            color: #333;
        }

        -->
    </style>

    <script type="text/javascript" language="javascript" src="<%=basePath%>scripts/jquery-1.6.min.js"></script>
    <script src="<%=basePath%>scripts/jquery.validate.min.js" type="text/javascript" charset="UTF-8"></script>

    <script src="<%=basePath%>scripts/global.js" type="text/javascript" language="javascript"></script>
</head>

<body>


<script type="text/javascript">

    $(document).ready(function() {
        $("#loginForm").validate({
           // errorLabelContainer: "#messageBox",      wrapper: "li",
            rules: {
                j_username: "required",
                j_password: "required"},
            messages: {
                j_username: "请输入用户名",
                j_password: "请输入密码"} ,
            showErrors: function(errorMap, errorList) {
                var errorTips = $('div');
                var info='';
                $.each(errorList,function(i,e){
                       info += e?'<li>'+e.message+'</li>':'';
                });
                errorTips.html(info);
                var screenWidth = $(document).width();
                errorTips.css({'position':'absolute','left':screenWidth/2-200,'top':'0px','width':'400px','background':'#ffffcc','font-size':'10pt'});
               errorTips.appendTo('body');
                (errorList.length==0)?errorTips.hide():errorTips.show();
            },
            success: function(label) {
                //alert("提交成功");
            }
        });
  });


</script>
 <ul id="messageBox"></ul>
<div id="summary"></div>
<s:form method="post" id="loginForm" action="/j_spring_security_check" namespace="/" >
    <table width="1000" height="600" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr>
            <td colspan="3">
                <img src="${pageContext.request.contextPath}/themes/css/images/frame/login_01.jpg" width="1000" height="175" alt=""></td>
        </tr>
        <tr>
            <td rowspan="3">
                <img src="${pageContext.request.contextPath}/themes/css/images/frame/login_02.jpg" width="536" height="425" alt=""></td>
            <td>
                <img src="${pageContext.request.contextPath}/themes/css/images/frame/login_03.jpg" width="269" height="84" alt=""></td>
            <td rowspan="3">
                <img src="${pageContext.request.contextPath}/themes/css/images/frame/login_04.jpg" width="195" height="425" alt=""></td>
        </tr>
        <tr>
            <td  height="178" background="${pageContext.request.contextPath}/themes/css/images/frame/login_05.jpg">
                <c:if test="${param.error != null}">
                    <div class="error">
                        <img src="${pageContext.request.contextPath}/themes/css/images/icon/iconWarning.gif"/>
                        ${SPRING_SECURITY_LAST_EXCEPTION.message}
                        <c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'Bad credentials'}">
                            <fmt:message key="errors.password.mismatch"/>
                        </c:if>
                        <c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'User account is locked'}">
                            <fmt:message key="errors.user.locked"/>
                        </c:if>
                        <c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message eq 'User credentials have expired'}">
                            <fmt:message key="errors.credential.expired"/>
                        </c:if>
                        <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>

                    </div>
                </c:if>
                <table width="87%" height="110" border="0" align="center" cellpadding="1" cellspacing="0">
                    <tr>
                        <td width="24%">登录名</td>
                        <td width="76%"><input type="text" id="username" name="j_username" value="${SPRING_SECURITY_LAST_USERNAME}"
                                                style="width:120px; height:18px;"></td>
                    </tr>
                    <tr>
                        <td>密&nbsp;&nbsp;码</td>
                        <td><input type="password" name="j_password" id="password"  style="width:120px; height:18px;">
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <s:hidden property="_spring_security_remember_me" value="true"/>
                        <td valign="bottom"><input type="submit" class="btn" name="login" value="登录">
                            <input type="button" class="btn" name="Submit" onclick="javascript:location.href='<%=basePath%>index.do'" value="返回"></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <img src="${pageContext.request.contextPath}/themes/css/images/frame/login_06.jpg" width="269" height="163" alt=""></td>
        </tr>
    </table>
</s:form>


</body>
</html>
