<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageHeader">
	<form  onsubmit="return dialogSearch(this);" action="${pageContext.request.contextPath }/sys/addressBook!beforeImportA.do?s_groupid=${GROUPID}" method="post">
	<input type="hidden" name="pageNum" value="${pageDesc.pageNo}" /> 
		<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
		<input type="hidden" name="s_bodytype" value="${param['s_bodytype'] }">
		<div class="searchBar">
		<table class="searchContent">
			<tr>
				<td class="dateRange">
					姓名:
					<input type="text" name="s_bodyname" value="${param['s_bodyname'] }" />
				</td>				
				<td><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></td>
			</tr>
		</table>
	</div>
	</form>
</div>


<div class="pageContent">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add" target="selectedTodo"  targetType="dialog" rel="ids" href="${pageContext.request.contextPath }/sys/addressBook!ImportA.do?groupid=${GROUPID}" onclick="return validateCallback(this, dialogAjaxDone);"><span>导入</span></a></li>
		</ul>
	</div>
	<table class="table" width="1100" layoutH="138">
		<thead>
				<tr>
				    <th width="22"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
					<th align="center">姓名</th>
					<th align="center">通讯主体ID</th>
					<th align="center">表示为</th>
					<th align="center">单位</th>
					<th align="center">部门</th>
					<th align="center">职务</th>
					<th align="center">QQ</th>
					<th align="center">移动电话</th>
					<th align="center">电子邮件</th>
					<th align="center">常用通讯地址</th>
					<th align="center">最后修改时间</th>
				</tr>
			</thead>
		<tbody>
		
		    <c:forEach items="${objList}" var="obj" varStatus="s">

					<tr target="usercode" rel="${obj.usercode}">
					    <td><input name="ids" value="${obj.usercode}" type="checkbox"></td>
						<td align="center">${obj.bodyname }</td>
						<td align="center">${obj.usercode }</td>
						<td align="center">${obj.representation }</td>
						<td align="center">${obj.unitname }</td>
						<td align="center">${cp:MAPVALUE("unitcode",obj.unitcode)}</td>						
						<td align="center">${obj.rankname }</td>
						<td align="center">${obj.qq }</td>
						<td align="center">${obj.mobilephone }</td>
						<td align="center">${obj.email }</td>
						<td align="center">
							<c:if test="${obj.inuseaddress eq 1}">${obj.homeprovince}${obj.homecity}${obj.homedistrict}${obj.homestreet}${obj.homeaddress}</c:if>
							<c:if test="${obj.inuseaddress eq 2}">${obj.home2province}${obj.home2city}${obj.home2district}${obj.home2street}${obj.home2address}</c:if>
							<c:if test="${obj.inuseaddress eq 3}">${obj.unitprovince}${obj.unitcity}${obj.unitdistrict}${obj.unitstreet}${obj.unitaddress}</c:if> 
						</td>
						<td align="center"><fmt:formatDate pattern="yyyy-MM-dd" value="${obj.lastmodifydate}"/></td>
					</tr>
		    </c:forEach>
		    
		</tbody>
	</table>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>
