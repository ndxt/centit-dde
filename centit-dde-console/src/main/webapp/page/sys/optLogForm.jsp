<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<html>
<head>
<title><s:text name="optLog.edit.title" /></title>
</head>

<body>
<p class="ctitle"><s:text name="optLog.edit.title" /></p>

<%@ include file="/page/common/messages.jsp"%>

<s:form action="optLog"  method="post" namespace="/sys" id="optLogForm" >
	<s:submit name="save"  method="save" cssClass="btn" key="opt.btn.save" />
	<s:submit type="button" name="back" cssClass="btn" key="opt.btn.back"/>
		
<table width="200" border="0" cellpadding="1" cellspacing="1">		
 
				<tr>
					<td class="TDTITLE">
						<s:text name="optLog.logid" />
					</td>
					<td align="left">
  
							<s:textfield name="logid" size="40" />
	
					</td>
				</tr>


				<tr>
					<td class="TDTITLE">
						<s:text name="optLog.loglevel" />
					</td>
					<td align="left">
	
  
						<s:textfield name="loglevel"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="optLog.usercode" />
					</td>
					<td align="left">
	
  
						<s:textfield name="usercode"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="optLog.opttime" />
					</td>
					<td align="left">
	
  
						<s:textfield name="opttime"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="optLog.optid" />
					</td>
					<td align="left">
	
  
						<s:textfield name="optid"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="optLog.optcode" />
					</td>
					<td align="left">
	
  
						<s:textfield name="optcode"  size="40"/>
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="optLog.optcontent" />
					</td>
					<td align="left">
  
						<s:textarea name="optcontent" cols="40" rows="2"/>
	
	
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<s:text name="optLog.oldvalue" />
					</td>
					<td align="left">
  
						<s:textarea name="oldvalue" cols="40" rows="2"/>
	
	
					</td>
				</tr>

</table>


</s:form>
