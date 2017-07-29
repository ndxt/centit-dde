<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<html>

<head><meta name="decorator" content='${LAYOUT}'/>
<title>选择用户</title>
<link href="<c:out value='${STYLE_PATH}'/>/css/main.css" type="text/css"
	rel="stylesheet">
<link href="<c:out value='${STYLE_PATH}'/>/css/extremecomponents.css"
	type="text/css" rel="stylesheet">
<link href="<c:out value='${STYLE_PATH}'/>/css/messages.css" type="text/css"
	rel="stylesheet">

</head>	

	
<script type="text/javascript">
		function returnUser(usercode,username)
		{
			parent.window.returnValue= {usercode : usercode,
										 username : username};
       		window.close();
		}
</script>
<base target="_self"/>
<body>
<%@ include file="/page/common/messages.jsp"%>

<div class="eXtremeTable" >
	<table id="ec_table"  border="0"  cellspacing="0"  cellpadding="0"  class="tableRegion"  width="100%" >

		<thead>
			<tr>
			<td class="tableHeader">用户代码</td>   
			<td class="tableHeader">登录名</td>  
			<td class="tableHeader">用户名</td>  
			<td class="tableHeader">操作</td>  
			</tr>  
		</thead>
		<tbody class="tableBody" >
		<c:set value="odd" var="rownum" />
		<c:forEach var="userinfo" items="${cp:SEARCHUSER(param.filter)}">    

			<tr class="${rownum}"  onmouseover="this.className='highlight'"  onmouseout="this.className='${rownum}'" >
			
			<td><c:out value="${userinfo.usercode}"/></td>   
			<td><c:out value="${userinfo.loginname}"/> </td>  
			<td><c:out value="${userinfo.username}"/> </td>  
			<td><a href="#" onclick="returnUser('${userinfo.usercode}','${userinfo.username}')"> 选择 </a> </td>  
			</tr>  

		   <c:set value="${rownum eq 'odd'? 'even': 'odd'}" var="rownum" /> 
		  
		</c:forEach> 
		</tbody>        
	</table>
</div>

<form method="get" action="<c:url value='selectUser.jsp'/>" name="getUser">
 	<table cellpadding="1" cellspacing="1" align="center">
		<tr>
			<td width="20%" />
			<td class="TDTITLE" width="20%" >
				筛选条件
			</td>
			<td align="left" width="30%"  colspan="2">
				<input name="filter" maxlength="12" size="12" value="${param.filter}" />
			</td>
			<td class="TDTITLE" width="10%" >
				<input type="submit" value="查询" />
			</td>	
			<td width="20%" />		
		</tr>
	</table>
</form>

</body>
</html>
