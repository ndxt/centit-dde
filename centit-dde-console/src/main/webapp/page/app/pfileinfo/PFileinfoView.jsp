<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<html>
<head>
<title>请设定这个页面的标题</title>
<link href="<c:out value='${style}'/>/css/am.css" type="text/css"
	rel="stylesheet">

<link href="<c:out value='${style}'/>/css/messages.css" type="text/css"
	rel="stylesheet">

</head>

<body>
<p class="ctitle">请设定这个页面的标题</p>

<%@ include file="/page/common/messages.jsp"%>

<html:button value="返回" styleClass="btn" onclick="window.history.back()" property="none"/>
<p>	
	
<table width="200" border="0" cellpadding="1" cellspacing="1">		
  
				<tr>
					<td class="TDTITLE">
						<c:out value="filecode" />
					</td>
					<td align="left">
						<c:out value="${PFileinfo.filecode}" />
					</td>
				</tr>


				<tr>
					<td class="TDTITLE">
						<c:out value="recorder" />
					</td>
					<td align="left">
						<c:out value="${PFileinfo.recorder}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<c:out value="fileextname" />
					</td>
					<td align="left">
						<c:out value="${PFileinfo.fileextname}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<c:out value="optcode" />
					</td>
					<td align="left">
						<c:out value="${PFileinfo.optcode}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<c:out value="filecontent" />
					</td>
					<td align="left">
						<c:out value="${PFileinfo.filecontent}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<c:out value="filedesc" />
					</td>
					<td align="left">
						<c:out value="${PFileinfo.filedesc}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<c:out value="recorddate" />
					</td>
					<td align="left">
						<c:out value="${PFileinfo.recorddate}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<c:out value="filetype" />
					</td>
					<td align="left">
						<c:out value="${PFileinfo.filetype}" />
					</td>
				</tr>	

				<tr>
					<td class="TDTITLE">
						<c:out value="filename" />
					</td>
					<td align="left">
						<c:out value="${PFileinfo.filename}" />
					</td>
				</tr>	

</table>



</body>
</html>
