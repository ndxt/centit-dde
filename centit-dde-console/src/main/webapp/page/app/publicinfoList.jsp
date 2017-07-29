<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="publicinfo.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> <input type="hidden" name="orderField"
		value="${s_orderField}" />
</form>



<div class="pageHeader">
	<s:form id="pagerForm" onsubmit="return navTabSearch(this);" action="/app/publicinfo.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				
					<li><label><c:out value="publicinfo.infocode" />:</label> <s:textfield name="s_infocode" value="%{#parameters['s_infocode']}" /></li>
				
				
					<li><label><c:out value="publicinfo.parentinfocode" />:</label> <s:textfield name="s_parentinfocode" value="%{#parameters['s_parentinfocode']}" /></li>
				
					<li><label><c:out value="publicinfo.filecode" />:</label> <s:textfield name="s_filecode" value="%{#parameters['s_filecode']}" /></li>
				
					<li><label><c:out value="publicinfo.filename" />:</label> <s:textfield name="s_filename" value="%{#parameters['s_filename']}" /></li>
				
					<li><label><c:out value="publicinfo.fileextension" />:</label> <s:textfield name="s_fileextension" value="%{#parameters['s_fileextension']}" /></li>
				
					<li><label><c:out value="publicinfo.ownercode" />:</label> <s:textfield name="s_ownercode" value="%{#parameters['s_ownercode']}" /></li>
				
					<li><label><c:out value="publicinfo.readcount" />:</label> <s:textfield name="s_readcount" value="%{#parameters['s_readcount']}" /></li>
				
					<li><label><c:out value="publicinfo.downloadcount" />:</label> <s:textfield name="s_downloadcount" value="%{#parameters['s_downloadcount']}" /></li>
				
					<li><label><c:out value="publicinfo.md5" />:</label> <s:textfield name="s_md5" value="%{#parameters['s_md5']}" /></li>
				
					<li><label><c:out value="publicinfo.uploadtime" />:</label> <s:textfield name="s_uploadtime" value="%{#parameters['s_uploadtime']}" /></li>
				
					<li><label><c:out value="publicinfo.modifytimes" />:</label> <s:textfield name="s_modifytimes" value="%{#parameters['s_modifytimes']}" /></li>
				
					<li><label><c:out value="publicinfo.status" />:</label> <s:textfield name="s_status" value="%{#parameters['s_status']}" /></li>
				
					<li><label><c:out value="publicinfo.type" />:</label> <s:textfield name="s_type" value="%{#parameters['s_type']}" /></li>
				
					<li><label><c:out value="publicinfo.isfolder" />:</label> <s:textfield name="s_isfolder" value="%{#parameters['s_isfolder']}" /></li>
				
					<li><label><c:out value="publicinfo.filedescription" />:</label> <s:textfield name="s_filedescription" value="%{#parameters['s_filedescription']}" /></li>
				
			</ul>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<s:submit method="list"><bean:message key="opt.btn.query" /></s:submit>
							</div>
						</div>
					</li>
					<li>
						<div class="buttonActive">
							<div class="buttonContent">
								<!-- 参数 navTabId 根据实际情况填写 -->
								<button type="button" onclick="javascript:navTabAjaxDone({'statusCode' : 200, 'callbackType' : 'closeCurrent', 'navTabId' : ''});">返回</button>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</s:form>
</div>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="publicinfo!edit.do" rel="" target='dialog'><span>添加</span></a></li>
			<li><a class="edit" href="publicinfo!edit.do?infocode={pk}" warn="请选择一条记录" rel="" target='dialog'><span>编辑</span></a></li>
			<li><a class="delete" href="publicinfo!delete.do?infocode={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div>

	<div layoutH="116">
		<table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
			<thead>

				<tr>
					
						<c:set var="tinfocode"><bean:message bundle='appRes' key='publicinfo.infocode' /></c:set>	
						<th>${tinfocode}</th>
					
					
						<c:set var="tparentinfocode"><bean:message bundle='appRes' key='publicinfo.parentinfocode' /></c:set>	
						<th>${tparentinfocode}</th>
					
						<c:set var="tfilecode"><bean:message bundle='appRes' key='publicinfo.filecode' /></c:set>	
						<th>${tfilecode}</th>
					
						<c:set var="tfilename"><bean:message bundle='appRes' key='publicinfo.filename' /></c:set>	
						<th>${tfilename}</th>
					
						<c:set var="tfileextension"><bean:message bundle='appRes' key='publicinfo.fileextension' /></c:set>	
						<th>${tfileextension}</th>
					
						<c:set var="townercode"><bean:message bundle='appRes' key='publicinfo.ownercode' /></c:set>	
						<th>${townercode}</th>
					
						<c:set var="treadcount"><bean:message bundle='appRes' key='publicinfo.readcount' /></c:set>	
						<th>${treadcount}</th>
					
						<c:set var="tdownloadcount"><bean:message bundle='appRes' key='publicinfo.downloadcount' /></c:set>	
						<th>${tdownloadcount}</th>
					
						<c:set var="tmd5"><bean:message bundle='appRes' key='publicinfo.md5' /></c:set>	
						<th>${tmd5}</th>
					
						<c:set var="tuploadtime"><bean:message bundle='appRes' key='publicinfo.uploadtime' /></c:set>	
						<th>${tuploadtime}</th>
					
						<c:set var="tmodifytimes"><bean:message bundle='appRes' key='publicinfo.modifytimes' /></c:set>	
						<th>${tmodifytimes}</th>
					
						<c:set var="tstatus"><bean:message bundle='appRes' key='publicinfo.status' /></c:set>	
						<th>${tstatus}</th>
					
						<c:set var="ttype"><bean:message bundle='appRes' key='publicinfo.type' /></c:set>	
						<th>${ttype}</th>
					
						<c:set var="tisfolder"><bean:message bundle='appRes' key='publicinfo.isfolder' /></c:set>	
						<th>${tisfolder}</th>
					
						<c:set var="tfiledescription"><bean:message bundle='appRes' key='publicinfo.filedescription' /></c:set>	
						<th>${tfiledescription}</th>
					
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${objList }" var="publicinfo">
						<tr target="pk" rel="${publicinfo.infocode}">
							
								<td>${publicinfo.infocode}</td>
							
							
								<td>${publicinfo.parentinfocode}</td>
							
								<td>${publicinfo.filecode}</td>
							
								<td>${publicinfo.filename}</td>
							
								<td>${publicinfo.fileextension}</td>
							
								<td>${publicinfo.ownercode}</td>
							
								<td>${publicinfo.readcount}</td>
							
								<td>${publicinfo.downloadcount}</td>
							
								<td>${publicinfo.md5}</td>
							
								<td>${publicinfo.uploadtime}</td>
							
								<td>${publicinfo.modifytimes}</td>
							
								<td>${publicinfo.status}</td>
							
								<td>${publicinfo.type}</td>
							
								<td>${publicinfo.isfolder}</td>
							
								<td>${publicinfo.filedescription}</td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>

<%-- 
<html>
	<head>
		<title><c:out value="publicinfo.list.title" /></title>
		<link href="<c:out value='${STYLE_PATH}'/>/css/am.css" type="text/css"
			rel="stylesheet">
		<link href="<c:out value='${STYLE_PATH}'/>/css/extremecomponents.css"
			type="text/css" rel="stylesheet">
		<link href="<c:out value='${STYLE_PATH}'/>/css/messages.css"
			type="text/css" rel="stylesheet">
	</head>

	<body>
		<%@ include file="/page/common/messages.jsp"%>
		<fieldset
			style="border: hidden 1px #000000; ">
			<legend>
				 <s:text name="label.list.filter" />
			</legend>
			<html:form action="/app/publicinfo.do" style="margin-top:0;margin-bottom:5">
				<table cellpadding="0" cellspacing="0" align="center">

					<tr height="22">
						<td><c:out value="publicinfo.infocode" />:</td>
						<td><html:text property="s_infocode" /> </td>
					</tr>	


					<tr height="22">
						<td><c:out value="publicinfo.parentinfocode" />:</td>
						<td><html:text property="s_parentinfocode" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfo.filecode" />:</td>
						<td><html:text property="s_filecode" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfo.filename" />:</td>
						<td><html:text property="s_filename" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfo.fileextension" />:</td>
						<td><html:text property="s_fileextension" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfo.ownercode" />:</td>
						<td><html:text property="s_ownercode" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfo.readcount" />:</td>
						<td><html:text property="s_readcount" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfo.downloadcount" />:</td>
						<td><html:text property="s_downloadcount" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfo.md5" />:</td>
						<td><html:text property="s_md5" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfo.uploadtime" />:</td>
						<td><html:text property="s_uploadtime" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfo.modifytimes" />:</td>
						<td><html:text property="s_modifytimes" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfo.status" />:</td>
						<td><html:text property="s_status" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfo.type" />:</td>
						<td><html:text property="s_type" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfo.isfolder" />:</td>
						<td><html:text property="s_isfolder" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="publicinfo.filedescription" />:</td>
						<td><html:text property="s_filedescription" /> </td>
					</tr>	

					<tr>
						<td>
							<html:submit property="method_list" styleClass="btn" > <bean:message key="opt.btn.query" /></html:submit>
						</td>
						<td>
							<html:submit property="method_edit" styleClass="btn" > <bean:message key="opt.btn.new" /> </html:submit>
						</td>
					</tr>
				</table>
			</html:form>
		</fieldset>

			<ec:table action="publicinfo.do" items="publicinfos" var="publicinfo"
			imagePath="${STYLE_PATH}/images/table/*.gif" retrieveRowsCallback="limit">
			<ec:exportXls fileName="publicinfos.xls" ></ec:exportXls>
			<ec:exportPdf fileName="publicinfos.pdf" headerColor="blue" headerBackgroundColor="white" ></ec:exportPdf>
			<ec:row>
				
					<c:set var="tinfocode"><bean:message bundle='appRes' key='publicinfo.infocode' /></c:set>	
					<ec:column property="infocode" title="${tinfocode}" style="text-align:center" />
				
				
					<c:set var="tparentinfocode"><bean:message bundle='appRes' key='publicinfo.parentinfocode' /></c:set>	
					<ec:column property="parentinfocode" title="${tparentinfocode}" style="text-align:center" />
				
					<c:set var="tfilecode"><bean:message bundle='appRes' key='publicinfo.filecode' /></c:set>	
					<ec:column property="filecode" title="${tfilecode}" style="text-align:center" />
				
					<c:set var="tfilename"><bean:message bundle='appRes' key='publicinfo.filename' /></c:set>	
					<ec:column property="filename" title="${tfilename}" style="text-align:center" />
				
					<c:set var="tfileextension"><bean:message bundle='appRes' key='publicinfo.fileextension' /></c:set>	
					<ec:column property="fileextension" title="${tfileextension}" style="text-align:center" />
				
					<c:set var="townercode"><bean:message bundle='appRes' key='publicinfo.ownercode' /></c:set>	
					<ec:column property="ownercode" title="${townercode}" style="text-align:center" />
				
					<c:set var="treadcount"><bean:message bundle='appRes' key='publicinfo.readcount' /></c:set>	
					<ec:column property="readcount" title="${treadcount}" style="text-align:center" />
				
					<c:set var="tdownloadcount"><bean:message bundle='appRes' key='publicinfo.downloadcount' /></c:set>	
					<ec:column property="downloadcount" title="${tdownloadcount}" style="text-align:center" />
				
					<c:set var="tmd5"><bean:message bundle='appRes' key='publicinfo.md5' /></c:set>	
					<ec:column property="md5" title="${tmd5}" style="text-align:center" />
				
					<c:set var="tuploadtime"><bean:message bundle='appRes' key='publicinfo.uploadtime' /></c:set>	
					<ec:column property="uploadtime" title="${tuploadtime}" style="text-align:center" />
				
					<c:set var="tmodifytimes"><bean:message bundle='appRes' key='publicinfo.modifytimes' /></c:set>	
					<ec:column property="modifytimes" title="${tmodifytimes}" style="text-align:center" />
				
					<c:set var="tstatus"><bean:message bundle='appRes' key='publicinfo.status' /></c:set>	
					<ec:column property="status" title="${tstatus}" style="text-align:center" />
				
					<c:set var="ttype"><bean:message bundle='appRes' key='publicinfo.type' /></c:set>	
					<ec:column property="type" title="${ttype}" style="text-align:center" />
				
					<c:set var="tisfolder"><bean:message bundle='appRes' key='publicinfo.isfolder' /></c:set>	
					<ec:column property="isfolder" title="${tisfolder}" style="text-align:center" />
				
					<c:set var="tfiledescription"><bean:message bundle='appRes' key='publicinfo.filedescription' /></c:set>	
					<ec:column property="filedescription" title="${tfiledescription}" style="text-align:center" />
						
				<c:set var="optlabel"><bean:message key="opt.btn.collection"/></c:set>	
				<ec:column property="opt" title="${optlabel}" sortable="false"
					style="text-align:center">
					<c:set var="deletecofirm"><bean:message key="label.delete.confirm"/></c:set>
					<a href='publicinfo.do?infocode=${publicinfo.infocode}&method=view'><bean:message key="opt.btn.view" /></a>
					<a href='publicinfo.do?infocode=${publicinfo.infocode}&method=edit'><bean:message key="opt.btn.edit" /></a>
					<a href='publicinfo.do?infocode=${publicinfo.infocode}&method=delete' 
							onclick='return confirm("${deletecofirm}publicinfo?");'><bean:message key="opt.btn.delete" /></a>
				</ec:column>

			</ec:row>
		</ec:table>

	</body>
</html>
 --%>