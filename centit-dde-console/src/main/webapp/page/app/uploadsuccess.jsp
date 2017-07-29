<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/css.jsp"%> 

<html>
<head><meta name="decorator" content='${LAYOUT}'/>
<title>文件上传成功</title>
<link href="<c:out value='${STYLE_PATH}'/>/css/messages.css" type="text/css"
	rel="stylesheet">
<script type="text/javascript">
	function returnFilecode(filecode)
	{
		parent.window.returnValue = filecode;
      	window.close();
	}
	
</script>
</head>

<body>
<p class="ctitle">文件上传成功</p>

<%@ include file="/page/common/messages.jsp"%>

<html:button value="确定" styleClass="btn" onclick="returnFilecode(${fileinfo.filecode})" property="none"/>
		
<table width="200" border="0" cellpadding="1" cellspacing="1">		
   
				<tr>
					<td class="TDTITLE">
						<c:out value="filecode" />
					</td>
					<td align="left">
						<c:out value="${fileinfo.filecode}" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<c:out value="recorder" />
					</td>
					<td align="left">
						<c:out value="${fileinfo.recorder}" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						<c:out value="fileextname" />
					</td>
					<td align="left">
						<c:out value="${fileinfo.fileextname}" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						<c:out value="optcode" />
					</td>
					<td align="left">
						<c:out value="${fileinfo.optcode}" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						<c:out value="filedesc" />
					</td>
					<td align="left">
						<c:out value="${fileinfo.filedesc}" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						<c:out value="recorddate" />
					</td>
					<td align="left">
						<c:out value="${fileinfo.recorddate}" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						<c:out value="filetype" />
					</td>
					<td align="left">
						<c:out value="${fileinfo.filetype}" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						<c:out value="filename" />
					</td>
					<td align="left">
						<c:out value="${fileinfo.filename}" />
					</td>
				</tr>	
</table>
</body>
</html>
