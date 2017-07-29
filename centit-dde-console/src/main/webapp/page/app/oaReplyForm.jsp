<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<html>
<head>
<title><s:text name="oaReply.edit.title" /></title>
</head>

<body>
<p class="ctitle"><s:text name="oaReply.edit.title" /></p>

<%@ include file="/page/common/messages.jsp"%>

<s:form action="oaReply"  method="post" namespace="/app" id="oaReplyForm" >
	<s:submit name="save"  method="save" cssClass="btn" key="opt.btn.save" />
	<s:submit type="button" name="back" cssClass="btn" key="opt.btn.back"/>
		
<table width="200" border="0" cellpadding="1" cellspacing="1">		
 
				<tr>
					<td class="TDTITLE">
						<s:text name="oaReply.replyid" />
					</td>
					<td align="left">
	
  
							<s:textfield name="replyid" size="40" />
	
					</td>
				</tr>


				<tr>
					<td class="TDTITLE">
						<s:text name="oaReply.threadid" />
					</td>
					<td align="left">
	
  
						<s:textfield name="threadid"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaReply.reply" />
					</td>
					<td align="left">
	
  
						<s:textfield name="reply"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaReply.replytime" />
					</td>
					<td align="left">
	
  
						<s:textfield name="replytime"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaReply.userid" />
					</td>
					<td align="left">
	
  
						<s:textfield name="userid"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="oaReply.username" />
					</td>
					<td align="left">
  
						<s:textarea name="username" cols="40" rows="2"/>
	
	
					</td>
				</tr>

</table>


</s:form>
