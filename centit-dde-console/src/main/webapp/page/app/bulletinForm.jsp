<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<%@ include file="/page/common/css.jsp"%> 
<link href="${pageContext.request.contextPath}/themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="${pageContext.request.contextPath}/themes/default/style-custom.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="${pageContext.request.contextPath}/themes/css/core.css" rel="stylesheet" type="text/css" media="screen" />
<link href="${pageContext.request.contextPath}/themes/css/core-custom.css" rel="stylesheet" type="text/css" media="screen" />
<title>公告编辑</title>

	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ckeditor/ckeditor.js"></script>
	<script src="${pageContext.request.contextPath}/scripts/ajaxfileupload.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/centitUntil.js" type="text/javascript"></script>
	<script src="${pageContext.request.contextPath}/scripts/suggest.js" type="text/javascript"></script> 


<script type="text/javascript">
	function checkForm(){
		if(trim(_get('msgtitle').value).length==0){
			alert("主题不能为空");
			_get('msgtitle').focus();
			return false;
		}
		if(trim(_get('selectunit').value).length==0){
			alert("收件人不能为空");
			return false;
		}
		
		
		return true;
	}	

	//字符空格处理
	var trim = function (str) {
		return str.replace(/^\s+|\s+$/g, "");
	};
	
	//删除 
	function deleteRow(node) { 
	    if (!window.confirm("确认删除文件?")) { 
	        return; 
	    } 
	    var tabObj = _get("filetab");//获取表格对象 
	    var rowObj = node.parentNode.parentNode; 
	    tabObj.deleteRow(rowObj.rowIndex); 
	} 


	function addFile(data)
	{
		var newFile = "<input type='hidden' name='fileCodes' value='"+data.id+"' >" + data.name;
		var oper = "&nbsp;&nbsp;<a href='###' onclick=\"deleteRow(this)\">[删除]</a>"; 
		var tblObj = _get("filetab");
		var len =  tblObj.rows.length; 
	    var newRow = tblObj.insertRow(len);//插入行对象 
	    var fileCell = newRow.insertCell(0); 
	    fileCell.innerHTML = newFile + oper;
	};

	var _get = function (id) {
		return document.getElementById(id);
	};
</script>

<br>
<p class="ctitle">新建公告</p>

<%@ include file="/page/common/messages.jsp"%>



<div class="pageHeader" align="left">
	<%-- <form
		action="${pageContext.request.contextPath }/app/innermsg!bulletinEdit.do"
		method="post" onsubmit="${pageContext.request.contextPath }/app/innermsg!saveBulletin.do">
		<input type="hidden" name="receivetype" value="O">
		<input type="hidden" name="msgstate" value="U"> --%>
		
		<s:form action="innermsg" onsubmit="return checkForm();"  method="post" namespace="/app" id="innermsgForm" >
<input type="hidden" name="receivetype" value="O">
<input type="hidden" name="msgstate" value="U" >
		<table width="200" border="0" cellpadding="1" cellspacing="1">		

				<tr>
					<td >
						标题:
					</td>
					<td align="left">
						<s:textfield name="msgtitle" size="40"/><span id="msgtitleTip" style="line-height: "></span>
					</td>
				</tr>
                
                
				<tr>
					<td >
						附件:
					</td>
					<td align="left">
						<table id="filetab">
						</table>
						<input type="file" name="upload" id="upload">
						<input type="button" action="<c:url value='/app/fileinfo!uploadfile.do' ></c:url>"
 							onclick="uploadFile(this,'upload',addFile);" value="上传">
					</td>
				
				</tr>
				<tr>
					<td >
						公告内容:
					</td>
					<td align="left">
						<s:textarea name="msgcontent" id="msgcontent" cols="60" rows="10"/>
					</td>
				</tr>
				<tr>
					<td height="40"></td>
					<td><s:submit name="save"  method="saveBulletin" cssClass="btn" key="保存" />
		
		        </td></tr></table>
		<%-- <dl>
			<dt>标题：</dt>
			<input name="msgtitle" type="text" />
		</dl>
		<dl>
			<dt>附件：</dt>
			<input type="file" name="upload" id="upload">
			<input type="button"
				action="<c:url value='${pageContext.request.contextPath }/app/fileinfo!uploadfile.do' ></c:url>"
				onclick="uploadFile(this,'upload',addFile);" value="上传">
		</dl>
		<br>
		<dl class="nowrap">
			<dt>公告内容：</dt>
			<dd>
				<textarea name="msgcontent" id="msgcontent" cols="70" rows="20"></textarea>
			</dd>
		</dl>

		<div class="buttonActive">
			<div class="buttonContent">
				<button type="submit" >保存</button> --%>
	</s:form>
</div>



<script type="text/javascript">
	    
	    CKEDITOR.replace('msgcontent',
	    		{
	    	skin : 'office2003',
	    	language : 'zh-cn',
	    	width:500,
	    	toolbar:[
	    	['Bold','Italic','Underline','StrikeThrough','Style','FontFormat','FontName','FontSize','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyFull','-','TextColor']
	    	,['OrderedList','UnorderedList','-','Outdent','Indent']
	    	,['-','Smiley','SpecialChar','Unlink','Replace','Preview']
	    	]
	    	}
	    );
   </script>