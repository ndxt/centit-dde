<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.centit.com/el/coderepo" prefix="cp"%>
<html lang="zh-cmn-Hans">
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    	<meta name="renderer" content="webkit">
    	<meta name="viewport" content="width=device-width, initial-scale=1.0">
    	<authz:csrfMetaTags/>
    	<title>系统登录<c:if test="${'DEPLOY' eq sessionScope.ENTRANCE_TYPE}">(实施入口)</c:if></title>
		<link rel="shortcut icon" href="<c:url value="/ui/favicon.ico" />" type="image/x-icon" />
    	<link rel="stylesheet" href="${cp:SYS_VALUE('app.staticfile.home')}/modules/login/sys/style.css">
    	<script type="text/javascript" src="${cp:SYS_VALUE('app.staticfile.home')}/ui/js/jquery/jquery-1.11.2.min.js"></script>
    	<script type="text/javascript" src="${cp:SYS_VALUE('app.staticfile.home')}/modules/login/sys/style.js"></script>
	</head>
	<body>
		<div class="header">
			<span class="#title">项目标题信息<c:if test="${'DEPLOY' eq sessionScope.ENTRANCE_TYPE}">(实施入口)</c:if></span>
		</div>
		<div class="cloud1"></div>
		<div class="cloud2"></div>
		<div class="info">
			<div>
				<i></i>
				<div>
					<form action="${pageContext.request.contextPath }/login" method="post">
						<input name="username" value="${SPRING_SECURITY_LAST_USERNAME}" type="text" autocomplete="on" placeholder="用户名" />
						<input name="password" id="password" type="password" autocomplete="on" placeholder="密码" />
						<input type="hidden"  name="LOCAL_LANG" id="LOCAL_LANG"  value="zh_CN"  />
						<authz:csrfInput/>
						<button type="submit">登录</button>
						<span>
							<input id="remember" name="remember-me" type="checkbox" checked="checked" />
							<label for="remember">记住密码</label>					
						</span>
					</form>
				</div>
			</div>
		</div>
		<div class="footer">
			技术支持服务信息
		</div>
	</body>
</html>