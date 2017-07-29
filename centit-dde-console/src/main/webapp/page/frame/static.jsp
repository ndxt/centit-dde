<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>

<link href="${STYLE_PATH}/css/messages.css" type="text/css" rel="stylesheet">
<link href="${STYLE_PATH}/css/imagestyle.css" type=text/css rel=stylesheet>
<link href="${STYLE_PATH}/css/coolMenu.css" rel="stylesheet" type="text/css">

<script language="javascript" src="<s:url value="/scripts/jquery-1.6.min.js"/>" type="text/javascript"></script>
<script language="javascript" src="<s:url value="/scripts/jquery.utils.min.js"/>" type="text/javascript"></script>
<link href="<s:url value="/scripts/colorbox/colorbox.css"/>" type="text/css" rel="stylesheet">
<script language="javascript" type="text/javascript" src="<s:url value="/scripts/colorbox/jquery.colorbox.js"/>"
        charset="utf-8"></script>
<script language="javascript" src="<s:url value="/scripts/centit.js"/>" type="text/javascript"></script>
<script language="javascript" src="<s:url value="/scripts/coolMenu.js"/>" type="text/javascript"></script>
<script language="javascript" src="<s:url value="/scripts/topMenu.js"/>" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function() {
        //初始化ajax全局设置
        centit.ajax.initAjax({urlPrefix:'${STYLE_PATH}'});

    });
</script>