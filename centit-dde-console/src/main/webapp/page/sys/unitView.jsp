<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<form id="pagerForm" method="post"
	action="${pageContext.request.contextPath}/sys/unit!view.do?unitcode=${object.unitcode}">
	<input type="hidden" name="pageNum" value="1" /> 
	<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> 
	<input type="hidden" name="orderField" value="${param['orderField'] }" />
    <input type="hidden" name="orderDirection" value="${param['orderDirection'] }" />
</form>

<div class="panel panelDrag" id="unitDetails_users">
	<h1>机构明细</h1>
	<div class="pageFormContent">
	
	<div class="unit">
			<label>机构代码：</label> <label>${object.unitcode}</label> 
			<label>机构名称：</label><label>${object.unitname}</label>
			<label>部门简称：</label> <label><c:if
					test="${null == object.unitshortname}">&nbsp;</c:if> <c:if
					test="${null != object.unitshortname}">${object.unitshortname}</c:if></label>
		</div>

		<div class="unit">		
			<label>机构类型：</label> <label> <c:if
					test="${null == object.unittype}">&nbsp;</c:if> <c:if
					test="${null != object.unittype}">${cp:MAPVALUE('UnitType',object.unittype) }</c:if></label>
			<label>机构状态：</label><label>
			    <c:choose>
					<c:when test="${'T' eq object.isvalid }">启用</c:when>
					<c:when test="${'F' eq object.isvalid }">禁用</c:when>
				</c:choose></label> 
			<label>部门等级：</label><label><c:if
					test="${null == object.unitgrade}">&nbsp;</c:if> <c:if
					test="${null != object.unitgrade}">${object.unitgrade}</c:if></label>
		</div>
		
		<div class="unit">
			<label>机构描述：</label><label><c:if
					test="${null == object.unitdesc}">&nbsp;</c:if> <c:if
					test="${null != object.unitdesc}">${object.unitdesc }</c:if></label>
		</div>

		<div class="subBar">
			<ul>
				<li style="float:right; margin-right:50px;">
					<div class="buttonActive" style="margin-right:5px;">
						<div class="buttonContent">
							<a href='unit!edit.do?unitcode=${object.unitcode}&superUnitcode=${empty superUnitcode ? param['superUnitcode'] : superUnitcode}&tabid=unit_members_detail'
								target="dialog" title="编辑机构" rel="unitForm" width="500" height="450">
								编辑机构信息
							</a> 
						</div>
					</div>
					<div class="buttonActive">
						<div class="buttonContent">
							<button onclick="navTab.closeCurrentTab(<c:choose><c:when test='${not empty param["tabid"]}'>'${param["tabid"]}'</c:when><c:otherwise>'external_UNITMAG'</c:otherwise></c:choose>)">返回</button>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</div>
</div>

<div class="pageContent" style="margin-top:5px;">
    <c:set var="panel_buttons">
		<a class="add"
			href="${pageContext.request.contextPath }/sys/unit!builtUnitUser.do?userunit.unitcode=${object.unitcode}&tabid=${param['tabid']}"
			rel="built_unit_user" target='dialog' width="500" height="350"><span>添加机构用户</span></a>
	</c:set>

	<div id="r_list_print">
	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		<%@ include file="../common/panelBar.jsp"%>
		<table class="list" width="100%" layoutH=".pageHeader 200">
	</c:if>
	
	<c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		<table class="list" width="100%" layoutH=".pageHeader 173">
	</c:if>
	
	<%-- <div class="panelBar">
		<ul class="toolBar">
			<li class="new">
				<a class="add" href="${pageContext.request.contextPath }/sys/unit!builtUnitUser.do?userunit.unitcode=${object.unitcode}" 
					rel="built_unit_user" target='dialog' width="480" height="360">
					<span>添加机构用户</span>
				</a>
			</li>
		</ul>
	</div> --%>

	<!-- <table class="list" width="100%" layoutH="#unitDetails_users 61"> -->
		<thead align="center">
			<tr >
				<th orderField="id.usercode">用户代码</th>
				<th>用户名</th>
				<th orderField="id.userstation">用户岗位</th>
				<th orderField="id.userrank">行政职务</th>
				<th orderField="isprimary">是否主单位</th>
				<th >授权说明</th>
				<th >操作</th>

			</tr>
		</thead>
		<tbody align="center">
			<c:forEach items="${unitusers}" var="user">

				<tr>
					<td title="${user.usercode}">${user.usercode}</td>
					<td title="${cp:MAPVALUE('usercode',user.usercode)}"><c:out value="${cp:MAPVALUE('usercode',user.usercode)}" /></td>
					<td title="${cp:MAPVALUE('StationType',user.userstation)}"><c:out value="${cp:MAPVALUE('StationType',user.userstation)}" /></td>
					<td title="${cp:MAPVALUE('RankType',user.userrank)}"><c:out value="${cp:MAPVALUE('RankType',user.userrank)}" /></td>
					<td title="${YES_NO[user.isprimary]}"><c:out value="${YES_NO[user.isprimary]}" /></td>
					<td title="${user.rankmemo}">${user.rankmemo}</td>
					<td>
						<c:if test="${user.isprimary != 'T'}">
							<a href='unit!deleteUnitUser.do?userunit.unitcode=${user.unitcode}&userunit.usercode=${user.usercode}&userunit.userstation=${user.userstation}&userunit.userrank=${user.userrank}&tabid=${param["tabid"]}'
							 	target="ajaxTodo" title='是否删除用户：${cp:MAPVALUE("usercode",user.usercode)}?' >
							 	<span class="icon icon-trash" title="删除成员"></span>
							 </a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
<%@ include file="../common/panelBar.jsp"%>