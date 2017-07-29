<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<title><c:out value="${cp:MAPVALUE('SYSPARAM','SysName')}" /></title>

<!-- import style -->
<link href="${pageContext.request.contextPath}/themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="${pageContext.request.contextPath}/themes/default/style-custom.css" rel="stylesheet" type="text/css" media="screen"/>
<link href="${pageContext.request.contextPath}/themes/css/core.css" rel="stylesheet" type="text/css" media="screen" />
<link href="${pageContext.request.contextPath}/themes/css/core-custom.css" rel="stylesheet" type="text/css" media="screen" />
<link href="${pageContext.request.contextPath}/themes/css/MzTreeView10.css" rel="stylesheet" type="text/css" media="screen" />
<link rel="stylesheet" href="${contextPath }/scripts/plugin/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css"/>
<link href="${pageContext.request.contextPath}/themes/css/smart_wizard/smart_wizard.css" rel="stylesheet" type="text/css" media="screen" />

<link href="${pageContext.request.contextPath}/scripts/plugin/uploadify-v3.1/uploadify.css" rel="stylesheet" type="text/css" media="screen" />

<%-- <link href="${pageContext.request.contextPath}/themes/css/print.css" rel="stylesheet" type="text/css" media="print"/> --%>
<!--[if IE]>
<link href="${pageContext.request.contextPath}/themes/css/ieHack.css" rel="stylesheet" type="text/css" />
<![endif]-->

<!-- import javascript -->
<script src="${pageContext.request.contextPath}/scripts/centitui/speedup.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/jquery-1.7.1.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/jquery.cookie.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/jquery.validate.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/jquery.bgiframe.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/coolMenu.js" type="text/javascript"></script>

<!-- dwz api -->
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.core.js" type="text/javascript"></script>
<script type="text/javascript">DWZ.contextPath = '${pageContext.request.contextPath}';</script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.util.date.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.validate.method.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.regional.zh.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.barDrag.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.drag.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.tree.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.accordion.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.ui.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.theme.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.switchEnv.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.alertMsg.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.contextmenu.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.navTab.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.tab.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.resize.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.dialog.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.dialogDrag.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.sortDrag.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.cssTable.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.stable.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.taskBar.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.ajax.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.pagination.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.database.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.datepicker.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.effects.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.panel.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.checkbox.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.history.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.combox.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.print.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/centitui/dwz.regional.zh.js" type="text/javascript"></script>


<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/engine.js'></script>
<script type='text/javascript' src='${pageContext.request.contextPath}/dwr/util.js'></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/sys/reverse/reverse.js"></script>

<script src="${pageContext.request.contextPath}/scripts/dashboard.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/jquery.json-2.3.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/highcharts.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/sys/ui/centit.util.js" type="text/javascript"></script> 
<script src="${pageContext.request.contextPath}/scripts/sys/ui/centit.chart.js" type="text/javascript"></script> 
<script src="${pageContext.request.contextPath}/scripts/sys/ui/centit.slideshow.js" type="text/javascript"></script> 
<script src="${pageContext.request.contextPath}/scripts/sys/ui/centit.upload.js" type="text/javascript"></script> 

<script src="${pageContext.request.contextPath}/scripts/plugin/jquery.wresize.js" type="text/javascript"></script> 
<script src="${pageContext.request.contextPath}/scripts/plugin/fileupload/ajaxfileupload.js" type="text/javascript"></script> 
<script src="${pageContext.request.contextPath}/scripts/plugin/uploadify-v3.1/jquery.uploadify-3.1.js" type="text/javascript"></script> 


<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/plugin/treetable/Treetable_files/jqtreetable.js"></script>


<script src="${pageContext.request.contextPath}/scripts/plugin/xheditor/xheditor-1.1.12-zh-cn.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${contextPath }/scripts/plugin/zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${contextPath }/scripts/plugin/zTree/js/jquery.ztree.excheck-3.5.js"></script>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/plugin/treetable/Treetable_files/jqtreetable.css" />

<script src="${pageContext.request.contextPath}/scripts/plugin/smart_wizard/jquery.smartWizard-2.0.js" type="text/javascript"></script>

<script type="text/javascript" src="${contextPath }/scripts/pm/projectTasksView.js"></script>
<script type="text/javascript" src="${contextPath }/scripts/pm/projectInfoDashboard.js"></script>



<script type="text/javascript" src="${contextPath }/scripts/Mztreeview1.0/MzTreeView10.js"></script>

<script type="text/javascript" src="${contextPath }/scripts/dde/sqlContents.js"></script>
<script type="text/javascript" src="${contextPath }/scripts/dde/sqlWizard.js"></script>
<script type="text/javascript" src="${contextPath }/scripts/dde/sqlTables.js"></script>
<script type="text/javascript" src="${contextPath }/scripts/dde/sqlFilters.js"></script>
<script type="text/javascript" src="${contextPath }/scripts/dde/sqlGroups.js"></script>
<script type="text/javascript" src="${contextPath }/scripts/dde/sqlHavings.js"></script>
<script type="text/javascript" src="${contextPath }/scripts/dde/sqlOrders.js"></script>
<script type="text/javascript" src="${contextPath }/scripts/dde/sqlParameters.js"></script>

<script type="text/javascript">
$(function(){
	DWZ.init("${pageContext.request.contextPath}/page/frame/centitui.frag.xml", {
		loginUrl:"${pageContext.request.contextPath}/logindialog.jsp", loginTitle:"登录",	// 弹出登录对话框
		statusCode:{ok:200, error:300, timeout:301}, //【可选】
		pageInfo:{pageNum:"pageNum", numPerPage:"numPerPage", orderField:"orderField", orderDirection:"orderDirection"}, //【可选】
		debug:false,	// 调试模式 【true|false】
		callback:function(){
			initEnv();
			setTimeout(function(){
				$("a#firstPage1").click();
			},1000);
			$("#themeList").theme({themeBase:"${pageContext.request.contextPath}/themes"}); // themeBase 相对于index页面的主题base路径
			
			if ($.myChart) {
				$.myChart._contextPath = '${pageContext.request.contextPath}';
			}else {
				alertMsg.warn('没有正确设置图标基础路径！');
			}
			
			DWZ.contextPath = '${pageContext.request.contextPath}';
			
			new main($.parseJSON('${OA_MENUS}'),'${pageContext.request.contextPath}');
		}
	});


    DWZ.userInfo = {
        usercode : '${session.SPRING_SECURITY_CONTEXT.authentication.principal.usercode}'
    };

    //全局缓存，在exchangeTaskConsole中缓存TabId
    DWZ.CACHE = {
        TASK_CONSOLE : {}
    };

	//缓存各类型数据库能匹配的数据类型
	$.post(DWZ.contextPath + '/dde/exportSql!analyissDataType.do', {}, function (respData) {
		DWZ.CACHE.DATA_TYPE = respData;
	}, 'json');


});
</script>
</head>

<body id="mainBody">
	<div id="layout">
		<div id="header">
			<div class="headerNav">
				<!-- <a class="logo" href="#">标志</a> -->
				<ul class="nav"><authz:authentication var="username" property="name" />
					<li class="username"><a><c:if test="${username!='noname'}">${username}</c:if></a></li>
					<c:if test="${username=='noname'}">
							<c:if test="${cp:MAPSTATE('SYSPARAM','CAS') eq 'T'}" >							
								<li><a class="changeps" href="<s:url value='/sys/mainFrame!logincas.do'/>">登录</a></li>
							</c:if>
							<%-- <c:if test="${cp:MAPSTATE('SYSPARAM','CAS') eq 'T'}" >							
								<li><a class="changeps" href="<s:url value='/sys/mainFrame!login.do?inDialog=true'/>" target="dialog">登录</a></li>
							</c:if> --%>
							<c:if test="${not (cp:MAPSTATE('SYSPARAM','CAS') eq 'T')}" >
								<li><a class="changeps" href="<s:url value='/sys/mainFrame!login.do?inDialog=true'/>" >登录</a></li>							
							</c:if>	
							<c:if test="${cp:MAPSTATE('SYSPARAM','EnableWebUsr') eq 'T'}" >
				           		<li ><a class="changeps" href="<s:url value='/sys/userDef!registerpage.do' />" target="dialog" width="550" height="350">注册</a></li>
				           	</c:if>
						</c:if>
						<c:if test="${not(username=='noname')}" >
							<c:if test="${cp:MAPSTATE('SYSPARAM','CAS') eq 'T'}" >
								<li><a class="changeps" href="<s:url value='http://localhost:8080/cas/logout?service=http://localhost:8555/centit-dde'/>">注销</a></li>
							</c:if>
							<c:if test="${not (cp:MAPSTATE('SYSPARAM','CAS') eq 'T')}" >
								<li ><a class="changeps" href="<s:url value='/j_spring_security_logout'/>">注销</a></li>
							</c:if>
							<li ><a class="changeps" href="<s:url value='/sys/userDef!modifyPwdPage.do' />" target="dialog" width="550" height="350">更改密码</a></li>
						</c:if>
				</ul>
				<ul class="themeList" id="themeList">
					<li theme="default"><div class="selected">蓝色</div></li>
					<li theme="green"><div>绿色</div></li>
					<li theme="purple"><div>紫色</div></li>
					<li theme="silver"><div>银色</div></li>
					<li theme="azure"><div>天蓝</div></li>
				</ul>
			</div>
			<!-- navMenu -->
				
		</div>

		<div id="leftside">
			<div class="accordionContent" style="display:none" >
				<ul class="tree treeFolder">
					<li><a target="navTab">123</a>
						<ul id="default-li">						
							<li><a id='firstPage1' href='<c:url value='${firstPage}' />' target="navTab"  rel='main'>我的首页</a></li>
						</ul>
					</li>
				</ul>
			</div>	
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse"><div></div></div>
				</div>
			</div>
			<div id="sidebar">
				<!-- 左边菜单 -->
					<c:if test="${username=='noname'}" >
						<c:set var="funcs" value="${session.USERDETAIL.userFuncs}" />
					</c:if>
					<c:if test="${not(username=='noname')}" >
						<c:set var="funcs" value="${session.SPRING_SECURITY_CONTEXT.authentication.principal.userFuncs}" />
					</c:if>
												
					<script type="text/javascript">
					         /* $(function() {
					        	<c:forEach var="function" items="${funcs}">
					        	
					        	<c:choose>
								<c:when test="${function.opttype=='E'}"> 
								var url='${function.opturl}';
								if(url.indexOf('?')<0){
								 AddMenu('<c:out value="${function.optid}"/>', 	
							                '<c:out value="${function.optname}"/>',
							                '<c:url value="${function.opturl}?usercode=${session.SPRING_SECURITY_CONTEXT.authentication.principal.usercode}"/>',
							                '<c:out value="${function.preoptid}"/>',
							                '<c:out value="${function.pageType}"/>');
								}
								else{
									 AddMenu('<c:out value="${function.optid}"/>', 	
								                '<c:out value="${function.optname}"/>',
								                '<c:url value="${function.opturl}&usercode=${session.SPRING_SECURITY_CONTEXT.authentication.principal.usercode}"/>',
								                '<c:out value="${function.preoptid}"/>',
								                '<c:out value="${function.pageType}"/>');
								}
								</c:when>
								<c:otherwise>
								 AddMenu('<c:out value="${function.optid}"/>', 	
							                '<c:out value="${function.optname}"/>',
							                '<c:url value="${function.opturl}"/>',
							                '<c:out value="${function.preoptid}"/>',
							                '<c:out value="${function.pageType}"/>');
								</c:otherwise>
							</c:choose>
							</c:forEach>	
					        }); */ 
					        
					        
					        /*new main(${OA_MENUS},'${pageContext.request.contextPath}');*/
					</script>
			</div>
		</div>
		
		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent"><!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="javascript:;"><span><span class="home_icon">我的首页</span></span></a></li>
						</ul>
					</div>
					<div class="tabsLeft">left</div><!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
					<div class="tabsRight">right</div><!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
					<div class="tabsMore">more</div>
				</div>
				<ul class="tabsMoreList">
					<li><a href="javascript:;">我的首页</a></li>
				</ul>
				<div class="navTab-panel tabsPageContent layoutBox">
					<div class="page unitBox"></div>
				</div>
			</div>
		</div>
		<div id="footer"><a href="#">技术支持：江苏南大先腾信息产业有限公司 &copy;2014</a>&nbsp;&nbsp;更新日期: 2015-1-15&nbsp;&nbsp;<a href="#">帮助</a></div>
	</div>
</body>
</html>