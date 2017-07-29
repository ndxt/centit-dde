<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/css.jsp"%> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head><meta name="decorator" content='${LAYOUT}'/>
    <meta name="decorator" content='${LAYOUT}'/>
      
    <title>操作业务定义 </title>
    

  </head>
  
  <body> 

  <br/>
  <a href='optInfo!built.do?preoptid=0'>新建一个父类功能模块</a>
  <br/>
    <ec:tree identifier="optid" parentAttribute="preoptid" items="objList"
		action="optInfo!list.do" 
		view="org.extremecomponents.tree.TreeView" filterable="false"
		sortable="false"  var="optinfo" imagePath="${pageContext.request.contextPath}/themes/css/images/table/*.gif">
			<ec:row>
				<ec:column property="optid" title="业务代码" style="text-align:left" cell="org.extremecomponents.tree.TreeCell" />
				<ec:column property="preoptid" title="父类业务代码" sortable="false"	style="text-align:left" />
				<ec:column property="optname" title="业务名称" sortable="false" style="text-align:left" />
				<ec:column property="opturl" title="业务URL" sortable="false"	style="text-align:left" />
				<ec:column property="isintoolbar" title="是否在菜单栏" sortable="false"	style="text-align:left" >
					<c:out value="${optinfo.isintoolbar=='Y'?'是':'否'}"/> 
				</ec:column>
				<ec:column property="opttype" title="业务类别" sortable="false"	style="text-align:left" >
					${cp:MAPVALUE("OPTTYPE",optinfo.opttype)}
					<%-- <c:out value="${optinfo.opttype=='M'?'系统管理':(optinfo.opttype=='S'?'系统业务':(optinfo.opttype=='N'?'普通业务':'流程业务'))}"/>  --%>
				</ec:column>
				<ec:column property="catalogopt" title="操作" sortable="false" >
					<c:if test="${not(optinfo.opturl eq '...')}">
						<a href='optDef!list.do?optid=${optinfo.optid}'>
							操作定义
						</a>	
					</c:if>					
					<a href='optInfo!edit.do?optid=${optinfo.optid}'>
						编辑
					</a>
					<a href='optInfo!delete.do?optid=${optinfo.optid}'
						onclick='return confirm("是否删除业务：${optinfo.optname}?");'>
						删除
					</a>
					<a href='optInfo!built.do?preoptid=${optinfo.optid}' >
						添加下层业务
					</a>	
					<c:if test="${optinfo.opttype=='W' }">
					
					<a href='optVar!list.do?s_OPTID=${optinfo.optid}'>查看业务变量</a>	
					</c:if>						
				</ec:column>
			</ec:row>
		</ec:tree>	
  </body>
</html>
