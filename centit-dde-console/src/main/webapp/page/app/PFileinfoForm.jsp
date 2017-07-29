<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<script type="text/javascript">
	 			
	function onFormSubmit(theForm) {
		if(document.all.file.value=="" && document.all.filecode.value==""){
	        alert("请选择需保存文件！");
	        document.all.file.focus();
	        return;
        }		
		document.all.btn_save.value = "保存中，文件较大请稍等...";
		document.all.btn_save.disabled="true";	
		// save 保存到数据库 upload 保存到文件夹			
		theForm.submit();
		
	}	
	
	
</script>

<body>
<p class="ctitle">上传文件</p>

<%@ include file="/page/common/messages.jsp"%>

<s:form action="fileinfo!upload.do"  namespace="/app" id="PFileinfoForm"  enctype="multipart/form-data">
 	<s:hidden property="method" value="upload" /> 	
 	<s:submit id="btn_save"  method="upload" value="保存" cssClass="btn"
		onclick="onFormSubmit(this.form);" disabled="disabled"/>
	<input type="button"  value="返回" Class="btn" onclick="window.history.back()" />	

<table width="50%" border="0" cellpadding="1" cellspacing="1" class="list" id="upload-table">		
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
						<input type="file" id="upload-fileinfo" optID="FILEINFO" inputID="fileinfo-input" />
						
						<input type="text" id="fileinfo-input" />
					</td>	
				</tr>	
				<tr>
					<td class="TDTITLE">
						文件内容描述
					</td>
					<td class="TDTITLE" align="left">						
						<s:textfield name="fileDesc" rows="1" cols="40" />
					</td>
				</tr>				
				
				<tr>
					<td class="TDTITLE">
						文件名，下载文件时默认的文件名
					</td>
					<td class="TDTITLE" align="left">
						<s:textfield name="fileName" rows="1" />
					</td>
				</tr>	
				<tr>
					<td class="TDTITLE">
						是否索引
					</td>
					<td class="TDTITLE" align="left">						
 						<s:radio name="isindex"	list="#{'1':'是','0':'否'}"></s:radio>
					</td>
				</tr>	
</table>
</s:form>
