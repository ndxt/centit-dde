<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageHeader">
	<s:form id="pagerForm" onsubmit="return navTabSearch(this);" action="/app/userMailConfig!list.do" method="post">
		<input type="hidden" name="pageNum" value="1" />
		<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
		<input type="hidden" name="orderField" value="${s_orderField}" />
		<div class="searchBar">
			<table class="searchContent">
				<tr>
					<td>邮箱账户:<input type="text" name="s_mailaccount" value="${param['s_mailaccount'] }" /></td>
				</tr>
			</table>

			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<s:submit method="list" value="查询" />
							</div>
						</div></li>
				</ul>
			</div>
		</div>
	</s:form>
</div>

<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" href="${contextPath }/app/userMailConfig!edit.do" rel="" target='dialog'><span>添加</span></a></li>
			<li><a class="edit" href="${contextPath }/app/userMailConfig!edit.do?emailid={pk}" warn="请选择一条记录" rel="" target='dialog'><span>编辑</span></a></li>
			<li><a class="delete" href="${contextPath }/app/userMailConfig!delete.do?emailid={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div>

	<div layoutH="116">
		<table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
			<thead>

				<tr align="center">

					<th>邮箱账户</th>

					<th>邮箱发送类型</th>
					<th>邮箱接收类型</th>

					<th>SMTP服务器url</th>

					<th>SMTP服务器端口</th>

					<th>POP3服务器url</th>

					<th>POP3服务器端口</th>

					<th>定时拉取邮件间隔时间</th>

					<th>服务器邮件保留天数</th>

				</tr>
			</thead>
			<tbody>
				<c:forEach items="${objList }" var="userMailConfig">
					<tr target="pk" rel="${userMailConfig.emailid}" align="center">

						<td>${userMailConfig.mailaccount}</td>

						<td><c:forEach var="c" items="${cp:DICTIONARY_D('MAIL_SEND_TYPE') }">
								<c:if test="${c.id.datacode eq userMailConfig.mailsendtype }">${c.datadesc }</c:if>
							</c:forEach></td>
							
						<td><c:forEach var="c" items="${cp:DICTIONARY_D('MAIL_RECEIVE_TYP') }">
								<c:if test="${c.id.datacode eq userMailConfig.mailreceivetype }">${c.datadesc }</c:if>
							</c:forEach></td>

						<td>${userMailConfig.smtpurl}</td>

						<td>${userMailConfig.smtpport}</td>

						<td>${userMailConfig.receiveurl}</td>

						<td>${userMailConfig.receiveport}</td>

						<td>${userMailConfig.intervaltime}</td>

						<td>${userMailConfig.retaindays}</td>

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
		<title><c:out value="userMailConfig.list.title" /></title>
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
			<html:form action="/app/userMailConfig.do" style="margin-top:0;margin-bottom:5">
				<table cellpadding="0" cellspacing="0" align="center">

					<tr height="22">
						<td><c:out value="userMailConfig.emailid" />:</td>
						<td><html:text property="s_emailid" /> </td>
					</tr>	


					<tr height="22">
						<td><c:out value="userMailConfig.usercode" />:</td>
						<td><html:text property="s_usercode" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="userMailConfig.mailaccount" />:</td>
						<td><html:text property="s_mailaccount" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="userMailConfig.mailpassword" />:</td>
						<td><html:text property="s_mailpassword" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="userMailConfig.mailtype" />:</td>
						<td><html:text property="s_mailtype" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="userMailConfig.smtpurl" />:</td>
						<td><html:text property="s_smtpurl" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="userMailConfig.smtpport" />:</td>
						<td><html:text property="s_smtpport" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="userMailConfig.pop3url" />:</td>
						<td><html:text property="s_pop3url" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="userMailConfig.pop3port" />:</td>
						<td><html:text property="s_pop3port" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="userMailConfig.intervaltime" />:</td>
						<td><html:text property="s_intervaltime" /> </td>
					</tr>	

					<tr height="22">
						<td><c:out value="userMailConfig.retaindays" />:</td>
						<td><html:text property="s_retaindays" /> </td>
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

			<ec:table action="userMailConfig.do" items="userMailConfigs" var="userMailConfig"
			imagePath="${STYLE_PATH}/images/table/*.gif" retrieveRowsCallback="limit">
			<ec:exportXls fileName="userMailConfigs.xls" ></ec:exportXls>
			<ec:exportPdf fileName="userMailConfigs.pdf" headerColor="blue" headerBackgroundColor="white" ></ec:exportPdf>
			<ec:row>
				
					<c:set var="temailid"><bean:message bundle='appRes' key='userMailConfig.emailid' /></c:set>	
					<ec:column property="emailid" title="${temailid}" style="text-align:center" />
				
				
					<c:set var="tusercode"><bean:message bundle='appRes' key='userMailConfig.usercode' /></c:set>	
					<ec:column property="usercode" title="${tusercode}" style="text-align:center" />
				
					<c:set var="tmailaccount"><bean:message bundle='appRes' key='userMailConfig.mailaccount' /></c:set>	
					<ec:column property="mailaccount" title="${tmailaccount}" style="text-align:center" />
				
					<c:set var="tmailpassword"><bean:message bundle='appRes' key='userMailConfig.mailpassword' /></c:set>	
					<ec:column property="mailpassword" title="${tmailpassword}" style="text-align:center" />
				
					<c:set var="tmailtype"><bean:message bundle='appRes' key='userMailConfig.mailtype' /></c:set>	
					<ec:column property="mailtype" title="${tmailtype}" style="text-align:center" />
				
					<c:set var="tsmtpurl"><bean:message bundle='appRes' key='userMailConfig.smtpurl' /></c:set>	
					<ec:column property="smtpurl" title="${tsmtpurl}" style="text-align:center" />
				
					<c:set var="tsmtpport"><bean:message bundle='appRes' key='userMailConfig.smtpport' /></c:set>	
					<ec:column property="smtpport" title="${tsmtpport}" style="text-align:center" />
				
					<c:set var="tpop3url"><bean:message bundle='appRes' key='userMailConfig.pop3url' /></c:set>	
					<ec:column property="pop3url" title="${tpop3url}" style="text-align:center" />
				
					<c:set var="tpop3port"><bean:message bundle='appRes' key='userMailConfig.pop3port' /></c:set>	
					<ec:column property="pop3port" title="${tpop3port}" style="text-align:center" />
				
					<c:set var="tintervaltime"><bean:message bundle='appRes' key='userMailConfig.intervaltime' /></c:set>	
					<ec:column property="intervaltime" title="${tintervaltime}" style="text-align:center" />
				
					<c:set var="tretaindays"><bean:message bundle='appRes' key='userMailConfig.retaindays' /></c:set>	
					<ec:column property="retaindays" title="${tretaindays}" style="text-align:center" />
						
				<c:set var="optlabel"><bean:message key="opt.btn.collection"/></c:set>	
				<ec:column property="opt" title="${optlabel}" sortable="false"
					style="text-align:center">
					<c:set var="deletecofirm"><bean:message key="label.delete.confirm"/></c:set>
					<a href='userMailConfig.do?emailid=${userMailConfig.emailid}&method=view'><bean:message key="opt.btn.view" /></a>
					<a href='userMailConfig.do?emailid=${userMailConfig.emailid}&method=edit'><bean:message key="opt.btn.edit" /></a>
					<a href='userMailConfig.do?emailid=${userMailConfig.emailid}&method=delete' 
							onclick='return confirm("${deletecofirm}userMailConfig?");'><bean:message key="opt.btn.delete" /></a>
				</ec:column>

			</ec:row>
		</ec:table>

	</body>
</html>
 --%>