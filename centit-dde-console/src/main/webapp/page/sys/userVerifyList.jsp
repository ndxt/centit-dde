<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/css.jsp"%> 
<html>
	<head><meta name="decorator" content='${LAYOUT}'/>
		<title>人员列表</title>
		 <script type="text/javascript" src="<s:url value="/scripts/colorbox/jquery.colorbox.js"/>" charset="utf-8"></script>
        <link href="${pageContext.request.contextPath}/scripts/colorbox/colorbox.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="<s:url value="/scripts/addressBook.js"/>" charset="utf-8"></script>
		<script type="text/javascript" src="<s:url value="/scripts/centit.js"/>" charset="utf-8"></script>
	
	<script type="text/javascript">
		var path="${pageContext.request.contextPath}";				
	 $(document).ready(function() {  
		 centit.ajax.initAjax({urlPrefix:path});  
		 }); 
	</script>
	</head>
	<body>
		<fieldset>
			<legend>查询条件</legend>
			<s:form action="userDef" theme="simple">	
			<s:hidden name="s_userState" value="0"></s:hidden>		
				<table cellpadding="0" cellspacing="0" >
					<tr>
						<td width="250">用户名:
							<s:textfield name="s_USERNAME" value="%{#parameters['s_USERNAME']}" />	
						</td>
						<td width="250">
							登录名:
							<s:textfield name="s_LOGINNAME"  ></s:textfield>							
						</td>
						<td width="100">
							<s:checkbox label="包含禁用" name="s_isAll" value="#parameters['s_isAll']" fieldValue="true" />包含禁用	
						</td>
						<td>
							<s:submit method="toVerify" cssClass="btn" value="查询" ></s:submit>
						</td>
					</tr>
				</table>
		
			</s:form>
		</fieldset>
			<ec:table action="userDef!toVerify.do?s_userState=0" items="objList" var="fUserinfo"
			imagePath="${sessionScope.STYLE_PATH}/images/table/*.gif"
			rowsDisplayed="15" 
			filterRowsCallback="limit" 
			retrieveRowsCallback="limit"
			sortRowsCallback="limit"
			>
			<ec:row>
				<ec:column property="usercode" title="用户代码" style="text-align:center" />
			
				<ec:column property="username" title="用户名" style="text-align:center" />

				<ec:column property="loginname" title="登录名" style="text-align:center" />
				<ec:column property="Isvalid" title="状态" sortable="false" style="text-align:center">
					${USE_STATE[fUserinfo.isvalid]}
				</ec:column>
				<ec:column property="userdesc" title="用户描述" style="text-align:center"></ec:column>
				<ec:column property="userOpt" title="操作" sortable="false"
					style="text-align:center">
					<a href='userDef!verifyEdit.do?usercode=${fUserinfo.usercode}'>审核</a>
				</ec:column>

			</ec:row>
		</ec:table>

	</body>
</html>
