<%@ page contentType="text/html;charset=utf-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/css.jsp"%> 

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>角色权限维护</title>
	<script type="text/javascript" src="<%=path%>/scripts/dtree_chkbox.js"></script>
	<link href="<%=path%>/styles/default/css/dtree.css" rel="stylesheet" type="text/css"></link>

  </head>
  
 <body>
	<fieldset>
		<legend>
			角色权限维护
		</legend>
	<s:form  id="roleForm" name="roleForm" action="roleDef" namespace="/sys" onSubmit="return getMenuIDs();">
	<input type="hidden" name="optIDs">
	<s:submit method="save" cssClass="btn" value="保存" />
	<input type="button" value="返回" class="btn" onclick="window.history.back();"/>
	<table cellpadding="1" cellspacing="1" align="center">
		
		<tr>
			<td class="TDTITLE">角色代码*</td>
			<td align="left" width="300">
				<c:if test="${not empty rolecode}">
				<s:textfield name="rolecode"  value="%{rolecode}" readonly="true" /></c:if>
				<c:if test="${empty rolecode}">
					<s:textfield  id="rolecode" name="rolecode"  value="%{rolecode}"  /></c:if>
				<span id="rolecodeTip"></span>	
			</td>
			<td class="TDTITLE">角色名*</td>
			<td align="left">
			
				<s:textfield id="rolename" name="rolename" value="%{rolename}" />
			<span id="rolenameTip"></span>
			</td>
		</tr>
		<tr>
			<td class="TDTITLE">角色描述</td>

			<td align="left" colspan="3"><s:textfield name="roledesc" value="%{roledesc}"
				rows="1" cols="40" /></td>
		</tr>
	</table>
</s:form>
<div id="systree" style="position:absolute; height:400px;width:250px; overflow:auto"></div>
</fieldset>
</body>
<script type="text/javascript">
	var d = new dTree('d','<%=path%>/styles/images/menu/');
	d.config.folderLinks=false;
	d.config.useCookies=false;
	d.config.check=true;
	d.add('0','-1','系统菜单','');

	function getMenuIDs(){
		var selids= d.getCheckedNodes();
		var str="";
		for(var n=0; n<selids.length; n++){
			str+=selids[n]+";";
		}
		roleForm.optIDs.value = str;
		if(roleForm.optIDs.value == null||roleForm.optIDs.value.length ==0){
			alert("请选择功能菜单");
			return false;
		}
		return true;
		//alert(roleForm.optIDs.value);
	}
	

</script>
<c:forEach var="opt" items="${optTreeList}">
	<script>d.add('${opt.optID}','${opt.preOptID}','${opt.optName}',"javascript:void(0);",'${opt.optName}');</script>
</c:forEach>
<script type="text/javascript">		 
	document.getElementById('systree').innerHTML = d;
</script>
<c:forEach var="map" items="${roleOptList}">
	<script>
	if(d.co('${map.id.optcode}') != null){
		d.co('${map.id.optcode}').checked=true;
	}
	</script>
</c:forEach>
</html>


