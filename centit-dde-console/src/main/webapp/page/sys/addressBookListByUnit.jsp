<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageHeader">
<c:if test="${null==GROUPID}">
	<form onsubmit="return divSearch(this, 'addressBookShow');" action="${pageContext.request.contextPath }/sys/addressBook!listAddressBookByUnit.do" method="post" id="addressBookListByUnit">
</c:if>
<c:if test="${null!=GROUPID}">
    <form onsubmit="return divSearch(this, 'addressBookShow');" action="${pageContext.request.contextPath }/sys/addressBook!listAddressBookByGroup.do" method="post" id="addressBookListByUnit">
</c:if>   
		<input type="hidden" name="pageNum" value="${pageDesc.pageNo}" /> 
		<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
		<input type="hidden" name="s_bodytype" value="${param['s_bodytype'] }">
		<input type="hidden" name="s_unitcode" value="${param['s_unitcode'] }">
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

<div class="pageContent" style="border-left:1px #B8D0D6 solid;border-right:1px #B8D0D6 solid">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="icon" href="${pageContext.request.contextPath }/sys/addressBook!view.do?usercode={usercode}" warn="请选择一个用户" target='dialog'><span>查看</span></a></li>
			<li><a class="edit" href="${pageContext.request.contextPath }/sys/addressBook!edit.do?usercode={usercode}&bodytype=${param['s_bodytype']}" warn="请选择一个用户" target='navTab'><span>编辑</span></a></li>
			<c:if test="${null==GROUPID}">
			<li><a class="delete" href="${pageContext.request.contextPath }/sys/addressBook!delete.do?usercode={usercode}" target="ajaxTodo" title="确定要删除吗？" warn="请选择一个记录"><span>删除</span></a></li>
			<li><a class="add" href="${pageContext.request.contextPath }/sys/addressBook!add.do?bodytype=${param['s_bodytype']}" target="navTab"><span>新增</span></a></li>
			</c:if>
			<c:if test="${null!=GROUPID}">
			<li><a class="add" href="${pageContext.request.contextPath }/sys/addressBook!add.do?groupid=${GROUPID}&bodytype=5" target="navTab"><span>新增</span></a></li>
			<li><a class="delete" href="${pageContext.request.contextPath }/sys/groupUser!delete.do?s_groupid=${GROUPID}&s_usercode={usercode}" target="ajaxTodo" title="确定要删除吗？" warn="请选择一个记录"><span>删除</span></a></li>
			<li><a class="add" href="${pageContext.request.contextPath }/sys/addressBook!beforeImportA.do?s_groupid=${GROUPID}" target="dialog" rel="addressBookImport"><span>从通讯录中导入</span></a></li>
			</c:if>
			<li><a class="icon" href="${pageContext.request.contextPath }/sys/addressBook!viewExpandInfo.do?usercode={usercode}" warn="请选择一个用户" target='navTab' rel="addressBookExpandInfo"><span>干系人扩展信息</span></a></li>
			<li class="line">line</li>

		</ul>
	</div>


	<div layoutH="230" id="r_list_print">
		<table class="table" width="102%" layoutH="260" rel="jbsxBox">
			<thead>

				<tr>
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
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>

