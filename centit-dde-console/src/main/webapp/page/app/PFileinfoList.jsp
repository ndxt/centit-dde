<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div id="fileinfoList">
<fieldset><legend>查询条件 </legend> <s:form action="fileinfo.do" namespace="/app" theme="simple">
	<table cellpadding="0" cellspacing="0" align="center">
		<tr>
			<td width="120">filecode:</td>
			<td><s:textfield name="s:filecode" /></td>
		</tr>
		<tr>
			<td>recorder:</td>
			<td><s:textfield name="s:recorder" /></td>
		</tr>
		<tr>
			<td>fileextname:</td>
			<td><s:textfield name="s:fileextname" /></td>
		</tr>
		<tr>
			<td>optcode:</td>
			<td><s:textfield name="s:optcode" /></td>
		</tr>
		<tr>
			<td>filedesc:</td>
			<td><s:textfield name="s:filedesc" /></td>
		</tr>
		<tr>
			<td>recorddate</td>
			<td><s:textfield name="s:recorddate" /></td>
		</tr>
		<tr>
			<td>filetype:</td>
			<td><s:textfield name="s:filetype" /></td>
		</tr>
		<tr>
			<td>filename:</td>
			<td><s:textfield name="s:filename" /></td>
		</tr>
		<tr height="36">
			<td></td>
			<td><s:submit method="list" cssClass="btn" value="查询">
			</s:submit><s:submit method="built" target="navTab" cssClass="btn" value="新增"></s:submit>
			<a href="<s:url value='/page/app/uploadfiledialog.jsp'/>" target="dialog" width="550" height="350">在对话框中上传文件（不会被索引）</a>
		</tr>
	</table>
</s:form></fieldset>

<ec:table action="fileinfo!list.do" items="objList" var="fileinfo"
	imagePath="${STYLE_PATH}/images/table/*.gif"
	retrieveRowsCallback="limit">
	<ec:row>
		<ec:column property="fileCode" title="filecode"
			style="text-align:center" />
		<ec:column property="recorder" title="recorder"
			style="text-align:center" />
		<ec:column property="fileExtName" title="fileextname"
			style="text-align:center" />
		<ec:column property="optCode" title="optcode"
			style="text-align:center" />
		<ec:column property="fileDesc" title="filedesc"
			style="text-align:center" />
		<ec:column property="recordDate" title="recorddate"
			style="text-align:center" />
		<ec:column property="fileType" title="filetype"
			style="text-align:center" />
		<ec:column property="fileName" title="filename"
			style="text-align:center" />

		<ec:column property="opt" title="操作" sortable="false"
			style="text-align:center">
			<a href='fileinfo!download.do?fileCode=${fileinfo.fileCode}'>下载文件</a>
			<a href='fileinfo!edit.do?fileCode=${fileinfo.fileCode}'>编辑</a>
			<a href='fileinfo!delete.do?fileCode=${fileinfo.fileCode}'
				onclick='return confirm("是否删除 ${fileinfo.fileCode}?");'>删除</a>
		</ec:column>
	</ec:row>
</ec:table>

</div>

