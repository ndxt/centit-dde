<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="scaffoldTest.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> <input type="hidden" name="orderField"
		value="${s_orderField}" />
</form>



<div class="pageHeader">
	<s:form id="pagerForm" onsubmit="return navTabSearch(this);" action="/1/scaffoldTest.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">
				
					<li><label><c:out value="scaffoldTest.logid" />:</label> <s:textfield name="s_logid" value="%{#parameters['s_logid']}" /></li>
				
				
					<li><label><c:out value="scaffoldTest.loglevel" />:</label> <s:textfield name="s_loglevel" value="%{#parameters['s_loglevel']}" /></li>
				
					<li><label><c:out value="scaffoldTest.usercode" />:</label> <s:textfield name="s_usercode" value="%{#parameters['s_usercode']}" /></li>
				
					<li><label><c:out value="scaffoldTest.opttime" />:</label> <s:textfield name="s_opttime" value="%{#parameters['s_opttime']}" /></li>
				
					<li><label><c:out value="scaffoldTest.optid" />:</label> <s:textfield name="s_optid" value="%{#parameters['s_optid']}" /></li>
				
					<li><label><c:out value="scaffoldTest.optcode" />:</label> <s:textfield name="s_optcode" value="%{#parameters['s_optcode']}" /></li>
				
					<li><label><c:out value="scaffoldTest.optcontent" />:</label> <s:textfield name="s_optcontent" value="%{#parameters['s_optcontent']}" /></li>
				
					<li><label><c:out value="scaffoldTest.oldvalue" />:</label> <s:textfield name="s_oldvalue" value="%{#parameters['s_oldvalue']}" /></li>
				
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
			<li><a class="add" href="scaffoldTest!edit.do" rel="" target='dialog'><span>添加</span></a></li>
			<li><a class="edit" href="scaffoldTest!edit.do?logid={pk}" warn="请选择一条记录" rel="" target='dialog'><span><bean:message key="opt.btn.edit" /></span></a></li>
			<li><a class="delete" href="scaffoldTest!delete.do?logid={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span><bean:message key="opt.btn.delete" /></span></a></li>
		</ul>
	</div>

	<div layoutH="116">
		<table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
			<thead>

				<tr>
					
						<c:set var="tlogid"><bean:message bundle='1Res' key='scaffoldTest.logid' /></c:set>	
						<th>${tlogid}</th>
					
					
						<c:set var="tloglevel"><bean:message bundle='1Res' key='scaffoldTest.loglevel' /></c:set>	
						<th>${tloglevel}</th>
					
						<c:set var="tusercode"><bean:message bundle='1Res' key='scaffoldTest.usercode' /></c:set>	
						<th>${tusercode}</th>
					
						<c:set var="topttime"><bean:message bundle='1Res' key='scaffoldTest.opttime' /></c:set>	
						<th>${topttime}</th>
					
						<c:set var="toptid"><bean:message bundle='1Res' key='scaffoldTest.optid' /></c:set>	
						<th>${toptid}</th>
					
						<c:set var="toptcode"><bean:message bundle='1Res' key='scaffoldTest.optcode' /></c:set>	
						<th>${toptcode}</th>
					
						<c:set var="toptcontent"><bean:message bundle='1Res' key='scaffoldTest.optcontent' /></c:set>	
						<th>${toptcontent}</th>
					
						<c:set var="toldvalue"><bean:message bundle='1Res' key='scaffoldTest.oldvalue' /></c:set>	
						<th>${toldvalue}</th>
					
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${objList }" var="scaffoldTest">
						<tr target="pk" rel="${scaffoldTest.logid}">
							
								<td title="${scaffoldTest.logid}">${scaffoldTest.logid}</td>
							
							
								<td title="${scaffoldTest.loglevel}">${scaffoldTest.loglevel}</td>
							
								<td title="${scaffoldTest.usercode}">${scaffoldTest.usercode}</td>
							
								<td title="${scaffoldTest.opttime}">${scaffoldTest.opttime}</td>
							
								<td title="${scaffoldTest.optid}">${scaffoldTest.optid}</td>
							
								<td title="${scaffoldTest.optcode}">${scaffoldTest.optcode}</td>
							
								<td title="${scaffoldTest.optcontent}">${scaffoldTest.optcontent}</td>
							
								<td title="${scaffoldTest.oldvalue}">${scaffoldTest.oldvalue}</td>
							
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
		<title><c:out value="scaffoldTest.list.title" /></title>
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
			<html:form action="/1/scaffoldTest.do" style="margin-top:0;margin-bottom:5">
				<table cellpadding="0" cellspacing="0" align="center">

					<tr height="22">
						<td><c:out value="scaffoldTest.logid" />:</td>
						<td><html:text property="s_logid" /> </td>
					</tr>	


					<tr height="22">
						<td><c:out value="scaffoldTest.loglevel" />:</td>
						<td><html:text property="s_loglevel" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="scaffoldTest.usercode" />:</td>
						<td><html:text property="s_usercode" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="scaffoldTest.opttime" />:</td>
						<td><html:text property="s_opttime" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="scaffoldTest.optid" />:</td>
						<td><html:text property="s_optid" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="scaffoldTest.optcode" />:</td>
						<td><html:text property="s_optcode" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="scaffoldTest.optcontent" />:</td>
						<td><html:text property="s_optcontent" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="scaffoldTest.oldvalue" />:</td>
						<td><html:text property="s_oldvalue" /> </td>
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

			<ec:table action="scaffoldTest.do" items="scaffoldTests" var="scaffoldTest"
			imagePath="${STYLE_PATH}/images/table/*.gif" retrieveRowsCallback="limit">
			<ec:exportXls fileName="scaffoldTests.xls" ></ec:exportXls>
			<ec:exportPdf fileName="scaffoldTests.pdf" headerColor="blue" headerBackgroundColor="white" ></ec:exportPdf>
			<ec:row>
				
					<c:set var="tlogid"><bean:message bundle='1Res' key='scaffoldTest.logid' /></c:set>	
					<ec:column property="logid" title="${tlogid}" style="text-align:center" />
				
				
					<c:set var="tloglevel"><bean:message bundle='1Res' key='scaffoldTest.loglevel' /></c:set>	
					<ec:column property="loglevel" title="${tloglevel}" style="text-align:center" />
				
					<c:set var="tusercode"><bean:message bundle='1Res' key='scaffoldTest.usercode' /></c:set>	
					<ec:column property="usercode" title="${tusercode}" style="text-align:center" />
				
					<c:set var="topttime"><bean:message bundle='1Res' key='scaffoldTest.opttime' /></c:set>	
					<ec:column property="opttime" title="${topttime}" style="text-align:center" />
				
					<c:set var="toptid"><bean:message bundle='1Res' key='scaffoldTest.optid' /></c:set>	
					<ec:column property="optid" title="${toptid}" style="text-align:center" />
				
					<c:set var="toptcode"><bean:message bundle='1Res' key='scaffoldTest.optcode' /></c:set>	
					<ec:column property="optcode" title="${toptcode}" style="text-align:center" />
				
					<c:set var="toptcontent"><bean:message bundle='1Res' key='scaffoldTest.optcontent' /></c:set>	
					<ec:column property="optcontent" title="${toptcontent}" style="text-align:center" />
				
					<c:set var="toldvalue"><bean:message bundle='1Res' key='scaffoldTest.oldvalue' /></c:set>	
					<ec:column property="oldvalue" title="${toldvalue}" style="text-align:center" />
						
				<c:set var="optlabel"><bean:message key="opt.btn.collection"/></c:set>	
				<ec:column property="opt" title="${optlabel}" sortable="false"
					style="text-align:center">
					<c:set var="deletecofirm"><bean:message key="label.delete.confirm"/></c:set>
					<a href='scaffoldTest.do?logid=${scaffoldTest.logid}&method=view'><bean:message key="opt.btn.view" /></a>
					<a href='scaffoldTest.do?logid=${scaffoldTest.logid}&method=edit'><bean:message key="opt.btn.edit" /></a>
					<a href='scaffoldTest.do?logid=${scaffoldTest.logid}&method=delete' 
							onclick='return confirm("${deletecofirm}scaffoldTest?");'><bean:message key="opt.btn.delete" /></a>
				</ec:column>

			</ec:row>
		</ec:table>

	</body>
</html>
 --%>