<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<form id="pagerForm" method="post" action="mdTable!list.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
</form>
<div class="pageContent">
	<form method="post" action="${pageContext.request.contextPath}/dde/mdTable!pdmtable.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);" enctype="multipart/form-data">
		<input id="hid_pdm_id" type="hidden" name="filecodes" />
	
		<div class="pageFormContent" layoutH="60">
			<p>				
			   <input type="file" id="upload-fileinfo" optID="pdmtotable" inputID="hid_pdm_id" fileTypeDesc="请选择pdm文件或者xml文件"
			     fileTypeExts="*.xml;*.pdm" uploadCallback="uploadCallback" 
			      uploader="${pageContext.request.contextPath}/dde/mdpdm!loadpdm.do"
			      />	
			</p>
			
			
		</div>
		

	</form>
</div>

<script type="text/javascript">
function uploadCallback(data) {
	if (data.result != '0') {
		alertMsg.warn(data.description);
		return false;
	};
	
	
}
</script>