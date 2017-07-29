<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<html>
	<head>
		<title>已上传的文件</title>
		<link href="<c:out value='${style}'/>/css/am.css" type="text/css"
			rel="stylesheet">
		<link href="<c:out value='${style}'/>/css/extremecomponents.css"
			type="text/css" rel="stylesheet">
		<link href="<c:out value='${style}'/>/css/messages.css"
			type="text/css" rel="stylesheet">
			
		<script type="text/javascript">
			function uploadfile()
			{
				var filecode = window.showModalDialog(
						"<c:url value='/app/PFileinfo.do?method=uploadindialog' />", 
						"上传文件");
				alert(filecode);
				window.location.reload();//.location.reload();
				
			}
		</script>
	</head>

	<body>
		<%@ include file="/page/common/messages.jsp"%>
		<fieldset
			style="border: hidden 1px #000000; ">
			<legend>
				 查询条件
			</legend>
			<html:form action="/app/PFileinfo.do" style="margin-top:0;margin-bottom:5">
				<table cellpadding="0" cellspacing="0" align="center">

					<tr height="22">
						<td>filecode:</td>
						<td><html:text property="filecode_SEARCH_" /> </td>
					</tr>	

					<tr height="22">
						<td>recorder:</td>
						<td><html:text property="recorder_SEARCH_" /> </td>
					</tr>	
					<tr height="22">
						<td>fileextname:</td>
						<td><html:text property="fileextname_SEARCH_" /> </td>
					</tr>	
					<tr height="22">
						<td>optcode:</td>
						<td><html:text property="optcode_SEARCH_" /> </td>
					</tr>	
					<tr height="22">
						<td>filedesc:</td>
						<td><html:text property="filedesc_SEARCH_" /> </td>
					</tr>	
					<tr height="22">
						<td>recorddate:</td>
						<td><html:text property="recorddate_SEARCH_" /> </td>
					</tr>	
					<tr height="22">
						<td>filetype:</td>
						<td><html:text property="filetype_SEARCH_" /> </td>
					</tr>	
					<tr height="22">
						<td>filename:</td>
						<td><html:text property="filename_SEARCH_" /> </td>
					</tr>	
					<tr>
						<td>
							<html:submit property="method_list" styleClass="btn" value="查询" />
						</td>
						<td>
							<html:submit property="method_edit" styleClass="btn" value="新增" />
							<html:button property="none" onclick="uploadfile()"  value="在对话框中上传文件（不会被索引）" />
						</td>
					</tr>
				</table>
			</html:form>
		</fieldset>

			<ec:table action="PFileinfo.do" items="PFileinfos" var="PFileinfo"
			imagePath="${style}/images/table/*.gif" retrieveRowsCallback="limit">
			<ec:row>

				<ec:column property="filecode" title="filecode" style="text-align:center" />

				<ec:column property="recorder" title="recorder" style="text-align:center" />
				<ec:column property="fileextname" title="fileextname" style="text-align:center" />
				<ec:column property="optcode" title="optcode" style="text-align:center" />
				<ec:column property="filedesc" title="filedesc" style="text-align:center" />
				<ec:column property="recorddate" title="recorddate" style="text-align:center" />
				<ec:column property="filetype" title="filetype" style="text-align:center" />
				<ec:column property="filename" title="filename" style="text-align:center" />			
				<ec:column property="opt" title="操作" sortable="false"
					style="text-align:center">
					<a href='PFileinfo.do?filecode=${PFileinfo.filecode}&method=download'>下载文件</a>
					<a href='PFileinfo.do?filecode=${PFileinfo.filecode}&method=edit'>编辑</a>
					<a href='PFileinfo.do?filecode=${PFileinfo.filecode}&method=delete' 
							onclick='return confirm("是否删除 PFileinfo?");'>删除</a>
				</ec:column>

			</ec:row>
		</ec:table>

	</body>
</html>
