<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/css.jsp"%> 

<html>
<head><meta name="decorator" content='${LAYOUT}'/>
<meta name="decorator" content="none"/> 


<title>上传文件</title>

</head>
<base target="_self"/>
<body>
<p class="ctitle">上传文件</p>

<%@ include file="/page/common/messages.jsp"%>
<s:form action="fileinfo.do"  namespace="/app" id="PFileinfoForm"  enctype="multipart/form-data">
<table width="200" border="0" cellpadding="1" cellspacing="1">		
				<tr>
					<td class="TDTITLE">
						文件编号（自动）
					</td>
					<td align="left">
						<c:if test="${not empty fileCode }">
							<s:textfield name="fileCode"  rows="1" readonly="true" />
						</c:if>						
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						选择文件
					</td>
					<td>
						<s:file name="upload" />
					</td>	
				</tr>	
				<tr>
					<td class="TDTITLE">
						文件内容描述
					</td>
					<td class="TDTITLE2" align="left" >						
						<s:textfield name="fileDesc" rows="1" cols="40" />
					</td>
				</tr>				
				
				<tr>
					<td class="TDTITLE">
						文件名，下载文件时默认的文件名
					</td>
					<td class="TDTITLE2" align="left">
						<s:textfield name="fileName" rows="1" />
					</td>
				</tr>		
</table>

	<s:submit id="btn_save"  method="upload" value="保存" cssClass="btn"  cssStyle="text-align:left"/>

</s:form>
</body>
</html>
