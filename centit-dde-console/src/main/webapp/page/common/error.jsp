<%@ page language="java" isErrorPage="true"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
<meta name="decorator" content='${LAYOUT}' />
<title>出错啦！</title>
</head>

<body id="error">
	<center>
		<br /> <br />
		<center>
			<img src="<%=request.getContextPath()%>/styles/default/images/icon_03.jpg" />
				<div>
					<span> 
						<c:if test="${not empty requestScope.error}">
							<font size="2" color="red">错误信息：${requestScope.error}</font>
						</c:if>
					</span>
					<h3>
						<s:property value="exception" />
					</h3>
				</div>
				<div>
					<pre>
						<s:property value="exceptionStack" /> 
					</pre>
				</div> 
				<br />
				<input type="submit" name="sub" value="返回" 
					class="button_jump2" onclick="javascript:window.history.back();" />
				&nbsp;&nbsp;&nbsp; 
		</center>
	</center>
</body>
</html>
