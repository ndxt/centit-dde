<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<html>
<head>
<title>上传文件</title>
<link href="<c:out value='${style}'/>/css/am.css" type="text/css"
	rel="stylesheet">
<link href="<c:out value='${style}'/>/css/messages.css" type="text/css"
	rel="stylesheet">

<script type="text/javascript"
	src="<c:url value="/page/common/validator.jsp"/>"></script>
<html:javascript formName="PFileinfoForm" staticJavascript="false"
	dynamicJavascript="true" cdata="false" />
<script type="text/javascript">
	 			
	function onFormSubmit(theForm) {
		if(document.all.file.value=="" && document.all.filecode.value==""){
	        alert("请选择需保存文件！");
	        document.all.file.focus();
	        return;
        }		
		document.all.btn_save.value = "保存中，文件较大请稍等...";
		document.all.btn_save.disabled="true";
		document.getElementById('method').value = 'upload';		
		// save 保存到数据库 upload 保存到文件夹			
		theForm.submit();
	}	
	
	function onFormDelete(theForm) {
		document.getElementById('method').value = 'delete';
	}	
</script>
</head>

<body>
<p class="ctitle">上传文件</p>

<%@ include file="/page/common/messages.jsp"%>

<html:form action="/app/PFileinfo"  styleId="PFileinfoForm" onsubmit="return validatePFileinfoForm(this);"
 enctype="multipart/form-data">
 	<html:hidden property="method" value="edit" />
	<html:button property="btn_save" value="保存" styleClass="btn"
		onclick="onFormSubmit(this.form);" disabled="flase" />
 
 	<html:button value="返回" styleClass="btn" onclick="window.history.back()" property="none"/>
		
<table width="200" border="0" cellpadding="1" cellspacing="1">		
				<tr>
					<td class="TDTITLE">
						文件编号（自动）
					</td>
					<td align="left">
						<html:textarea property="filecode" rows="1" readonly="${empty PFileinfoForm.map.filecode?'false':'true'}" cols="40" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						上传人（不要填写）
					</td>
					<td align="left">
						<html:textarea property="recorder" rows="1" cols="40" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						文件扩展名，一定要正确填写
					</td>
					<td align="left">
						<html:textarea property="fileextname" rows="1" cols="40" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						相关业务代码（不要填写）
					</td>
					<td align="left">
						<html:textarea property="optcode" rows="1" cols="40" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						选择文件
					</td>
					<td>
						<html:file property="file" />
					</td>
					
				</tr>	
				<tr>
					<td class="TDTITLE">
						文件内容描述
					</td>
					<td align="left">
						<html:textarea property="filedesc" rows="1" cols="40" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						上传时间（不要填写）
					</td>
					<td align="left">
						<html:textarea property="recorddate" rows="1" cols="40" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						文件类别（不要填写）
					</td>
					<td align="left">
						<html:textarea property="filetype" rows="1" cols="40" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						文件名，下载文件时默认的文件名
					</td>
					<td align="left">
						<html:textarea property="filename" rows="1" cols="40" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						是否要索引
					</td>
					<td align="left">
						<html:radio property="needindex" value="1"/>是   
 						<html:radio property="needindex" value="0"/>否   
					</td>
				</tr>	
</table>

</html:form>
</body>
</html>
