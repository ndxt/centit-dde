<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageHeader">
	<form onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath }/sys/userDefGroup!list.do" method="post">
		<input type="hidden" name="pageNum" value="1" /> 
		<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
		<div class="searchBar">
		<table class="searchContent">
			<tr>
				<td class="dateRange">
					分组名称:
					<input type="text" name="s_groupname" value="${param['s_groupname'] }" />
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
			<%-- <li><a class="add" href="${contextPath }/sys/userdefgroup!edit.do" rel="userdefgroupAdd" target='dialog'><span>添加</span></a></li> --%>
			<li><a class="edit" href="${contextPath }/sys/userDefGroup!edit.do?groupid={pk}" warn="请选择一条记录" rel="userdefgroupEdit" target='dialog'><span>编辑</span></a></li>
			<li><a class="delete" href="${contextPath }/sys/userDefGroup!delete.do?groupid={pk}" warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
		</ul>
	</div>

<form>
   
	<div layoutH="96">  
		<table class="table" width="100%">
			<thead>

				<tr>
					<th align="center">序号</th>
					<th align="center">分组名称</th>
					<th align="center">分组描述</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${objList}" var="obj" varStatus="s">
					<tr target="pk" rel="${obj.groupid}">
						<td>${s.index}</td>
						<td>${obj.groupname}</td>
                        <td>${obj.groupdesc}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	
	</div>
  </form>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>

