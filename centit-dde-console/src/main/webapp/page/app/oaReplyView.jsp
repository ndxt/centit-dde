<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<html>
<head>
<title><s:text name="oaReply.view.title" /></title>
</head>

<body>
<p class="ctitle"><s:text name="oaReply.view.title" /></p>

<%@ include file="/page/common/messages.jsp"%>

<a href='app/oaReply!list.do?ec_p=${param.ec_p}&ec_crd=${param.ec_crd}' property="none">
	<s:text name="opt.btn.back" />
</a>
<p>	
	
<table width="200" border="0" cellpadding="1" cellspacing="1">		
  
				<tr>
					<td class="TDTITLE">
						<s:text name="oaReply.replyid" />
					</td>
					<td align="left">
						<s:property value="%{replyid}" />
					</td>
				</tr>


				<tr>
					<td class="TDTITLE">
						<s:text name="oaReply.threadid" />
					</td>
					<td align="left">
						<s:property value="%{threadid}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaReply.reply" />
					</td>
					<td align="left">
						<s:property value="%{reply}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaReply.replytime" />
					</td>
					<td align="left">
						<s:property value="%{replytime}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaReply.userid" />
					</td>
					<td align="left">
						<s:property value="%{userid}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<s:text name="oaReply.username" />
					</td>
					<td align="left">
						<s:property value="%{username}" />
					</td>
				</tr>	

</table>



</body>
</html>
