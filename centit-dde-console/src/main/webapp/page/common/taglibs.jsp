<%@ page language="java"%>
<%@ taglib uri="http://www.centit.com/el/coderepo" prefix="cp"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="authz"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%> 
<%@ taglib uri="/WEB-INF/tlds/ui.tld" prefix="ui" %>
<% 
//response.setHeader("Pragma","No-cache"); 
//response.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
//response.setDateHeader ("Expires", 0); //prevents caching at the proxy server 
	
	pageContext.setAttribute("contextPath", request.getContextPath());
	pageContext.setAttribute("ctx", request.getContextPath());
%> 