<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<link href="<s:url value="/scripts/autocomplete/autocomplete.css"/>" type="text/css" rel="stylesheet">
<script language="javascript" src="<s:url value="/scripts/autocomplete/autocomplete.js"/>" type="text/javascript"></script>
<script language="javascript" src="<s:url value="/scripts/selectUser.js"/>" type="text/javascript"></script>
<script type="text/javascript" >
    var list = [];
    <c:forEach var="userinfo" varStatus="status" items="${cp:ALLUSER('T')}">
        list[${status.index}] = { username:'<c:out value="${userinfo.username}"/>', loginname:'<c:out value="${userinfo.loginname}"/>', usercode:'<c:out value="${userinfo.usercode}"/>',pinyin:'<c:out value="${userinfo.usernamepinyin}"/>'  };
    </c:forEach>
    function selectUser(obj) {
           userInfo.choose(obj, {dataList:list,userName:$('#userName')});
    }
</script>

<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath }/sys/optLog!list.do" method="post" id="pagerForm">
		<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
        <input type="hidden" name="orderField" value="${param['orderField'] }" />
        <input type="hidden" name="orderDirection" value="${param['orderDirection'] }" />

		<div class="searchBar">
			<ul class="searchContent">
				<li><label>操作人员:</label> <s:textfield onclick="selectUser(this);" id="userCode" name="s_usercode" value="%{#parameters['s_usercode']}" /></li>
				<li><label>起始时间:</label> 
					<input type="text" name="s_begopttime" readonly="true" class="date" format="yyyy-MM-dd" yearstart="-20" yearend="5" 
						value="${param['s_begopttime'] }" />
					<!-- <a class="inputDateButton" href="javascript:;">选择</a> -->
				</li>
				<li><label>结束时间:</label>
					<input type="text" name="s_endopttime" readonly="true" class="date" format="yyyy-MM-dd" yearstart="-20" yearend="5" 
						value="${param['s_endopttime'] }" />
					<!-- <a class="inputDateButton" href="javascript:;">选择</a> -->
				</li>
				<li><label>项目模块:</label> 
					<select name="s_optid" class="combox">
							<option value="">全部</option>
							<c:forEach var="opt" items="${optIds }">
								<option value="${opt }" <c:if test="${opt eq param['s_optid'] }">selected="selected"</c:if>>${cp:MAPVALUE('optid',opt)}</option>
							</c:forEach>
					</select>
				</li>
			</ul>

			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">检索</button>
							</div>
						</div></li>
				</ul>

			</div>
		</div>
	</form>
</div>
    
<div class="pageContent">

    <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		<%@ include file="../common/panelBar.jsp"%>
		<table class="list" width="100%" layoutH=".pageHeader 54" targetType="navTab" asc="asc" desc="desc">
	</c:if>
	
	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		<table class="list" width="100%" layoutH=".pageHeader 27">
	</c:if>
			<thead>

				<tr>
					<th align="center">序号</th>
					<th align="center" orderField="usercode">操作人员</th>
					<th align="center" orderField="opttime">操作时间</th>
					<th align="center" orderField="optid">项目模块</th>
					<th align="center" orderField="optmethod">操作方法</th>
					<th align="center">操作内容</th>
					<th align="center">更改前原值</th>
					<th align="center">操作</th>

				</tr>
			</thead>
			<tbody>
				<c:forEach items="${objList}" var="optLog" varStatus="s">

					<tr target="log_id" rel="${optLog.logid}">
						<td align="center">${s.index + 1}</td>
						<td align="center">${optLog.usercode }[<c:out value="${cp:MAPVALUE('usercode',optLog.usercode)}"/>]</td>
						<td align="center"><fmt:formatDate value="${optLog.opttime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td align="center"><c:out value="${cp:MAPVALUE('optid',optLog.optid)}"/></td>
						<td align="center">${optLog.optmethod}</td>

						<td align="center" <c:if test="${fn:length(optLog.optcontent) gt 10 }">title="${optLog.optcontent }" </c:if>>
							<c:choose>
								<c:when test="${fn:length(optLog.optcontent) gt 10 }">${fn:substring(optLog.optcontent, 0, 10) }...</c:when>
								<c:otherwise>${optLog.optcontent }</c:otherwise>
							</c:choose>
						</td>
						<td align="center" title="${optLog.oldvalue }">
							<c:choose>
								<c:when test="${fn:length(optLog.oldvalue) gt 10 }">${fn:substring(optLog.oldvalue, 0, 10) }...</c:when>
								<c:otherwise>${optLog.oldvalue }</c:otherwise>
							</c:choose>
						</td>
						<td align="center">
							<a href='${pageContext.request.contextPath }/sys/optLog!view.do?logid=${optLog.logid}&ec_p=${ec_p}&ec_crd=${ec_crd}' target="dialog" title="系统日志详细信息" width="680" height="345"><span class="icon icon-search"></span></a>
						</td>
					</tr>
				</c:forEach>



			</tbody>
		</table>
</div>

<%@ include file="../common/panelBar.jsp"%>

