<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<script type="text/javascript">
	function onFormSubmit(theForm) {
		if (document.all.query.value == "") {
			alert("请输入检索内容，关键词用空格隔开。");
			document.all.file.focus();
			return;
		}
		document.all.btn_search.value = "搜索中，请稍等...";
		document.all.btn_search.disabled = "true";
		document.getElementById('method').value = 'search';
		theForm.submit();
	}
	function uploadfile() {
		var filecode = window
				.showModalDialog(
						"<c:url value='/app/PFileinfo.do?method=uploadindialog&index=1' />",
						"上传文件");
		//alert(filecode);
		window.location.reload();//.location.reload();
	}
	function ajaxchange() {
		if ($("#bn").attr("value") == "高级") {
			$("#ajaxtable").show();
			$("#bn").attr("value", "简洁");
		} else {
			$("#ajaxtable").hide();
			$("#bn").attr("value", "高级");
		}
	}
</script>

<%@ include file="/page/common/messages.jsp"%>
<div class="pageHeader">

	<form action="${pageContext.request.contextPath }/app/searcher!search.do" method="post"
		enctype="multipart/form-data"  onsubmit="return navTabSearch(this)">

		<table border="0" cellpadding="1" cellspacing="1">
			<tr>
				<td width="200">请输入检索内容，关键词用空格隔开</td>
				<td align="left"><input type="text" name="keyWord" maxlength="80"
						size="60" /></td>
			</tr>
		</table>
		<div id="ajaxtable" style="display: none">
			<table border="0" cellpadding="1" cellspacing="1">

				<tr>
					<td width="100">时间范围</td>
					<td align="left">起始 : <input name="beginTime" class="date"
						size="30" /> 终止: <input name="endTime" class="date" size="30" />
				</tr>
				<tr>
					<td>文件类别</td>
					<td align="left"><s:checkboxlist name="typeList"
							list="{'txt','doc','docx','xls','xlsx','ppt','pplx','vsd','rtf','html'}"></s:checkboxlist>
					</td>
				</tr>
				<tr>
					<td>业务类别</td>
					<td align="left"><s:checkboxlist name="optList"
							list="{'mail','file'}"></s:checkboxlist></td>
				</tr>
			</table>
		</div>

		<div class="subBar">
			<ul>
				<li><input type="checkbox" name="owner" value="yes" />
					检索属于自己的文档
					<div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">检索</button>
						</div>
					</div>
					<div class="buttonActive">
						<div class="buttonContent">
							<input type="button" id="bn" value="高级" class="btn"
								onclick="ajaxchange()" />
						</div>
					</div>
					<div class="buttonActive">
						<div class="buttonContent">
							<input type="button" onclick="uploadfile()" Class="btn"
								value="上传并索引文件" />
							</td>
						</div>
					</div></li>

			</ul>
		</div>
<%-- 
		<table border="0" cellpadding="1" cellspacing="1">
			<tr>
				<td width="200"><input type="checkbox" name="owner" value="yes" />
					进检索属于自己的文档</td>
				<td><s:submit method="search" value="检索" cssClass="btn" /> <input
					type="button" id="bn" value="高级" class="btn" onclick="ajaxchange()" />
					<input type="button" onclick="uploadfile()" Class="btn"
					value="上传并索引文件" /></td>
			</tr>
		</table> --%>

	</form>
</div>

<div class="tableList" style="clear: both"><c:if test="${docsum gt 0}">
	<table id="t_doclist" border="0" cellspacing="0" cellpadding="0"
		class="tableRegion" width="100%">
		<thead>
			<tr>
				<td class="tableHeader">共检索到 <c:out value="${docsum}" /> 条结果</td>
			</tr>
		</thead>
		<tbody class="tableBody">
			<c:set value="odd" var="rownum" />
			<c:forEach var="doc" items="${doclist}">
				<tr class="${rownum}" onmouseover="this.className='highlight'"
					onmouseout="this.className='${rownum}'">
					<td><c:out value='${doc.summary}' /> <a
						href='<c:url value="${doc.url}" />'>查看详情</a></td>
				</tr>
				<c:set value="${rownum eq 'odd'? 'even': 'odd'}" var="rownum" />
			</c:forEach>
		</tbody>
	</table>
</c:if> <c:if test="${docsum == 0}">
	<p class="CTITLE" align="center">根据条件没有检索到文件</p>
</c:if> 
</div>
