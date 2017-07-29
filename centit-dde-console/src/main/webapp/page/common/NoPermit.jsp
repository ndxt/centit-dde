<%@ page language="java" isErrorPage="true" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head><meta name="decorator" content='${LAYOUT}'/>
    <title><fmt:message key="errorPage.title"/></title>
</head>

<body id="error" bgcolor="#ebf3fb">
    <div id="page">
        <div id="content" class="clearfix">
            <div id="main">
                <h2>您当前的用户没有权限，如需维护权限请与系统管理员联系。</h2>                
            </div>
        </div>
    </div>
</body>
</html>
