<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageFormContent" layoutH="548">
	<dl class="nowrap">
		<dt>源数据库：</dt>
		<dd>
			<input name="sourceDatabaseName" type="text" size="30" class="sourceDatabaseName" value="${object.sourceDatabaseName}"/> <a
				class="btnLook sourceDatabaseNamea" 
				href="${pageContext.request.contextPath }/sys/databaseInfo!defSourceAndDestDatabase.do?s_type=source"
				target="dialog" rel="defSourceDatabase" mask="true"
				title="定义数据源"><span>定义数据源</span> </a>
		</dd>
	</dl>

	<dl class="nowrap">
		<dt>目标数据库：</dt>
		<dd>
			<input name="goalDatabaseName" type="text" size="30" class="goalDatabaseName" value="${object.goalDatabaseName}" /> <a
				class="btnLook goalDatabaseNamea"
				href="${pageContext.request.contextPath }/sys/databaseInfo!defSourceAndDestDatabase.do?s_type=dest"
				target="dialog" rel="defGoalDatabase" mask="true"
				title="定义数据目标"><span>定义数据目标</span> </a>
		</dd>
	</dl>
</div>




<%-- <div class="pageContent"
	style="border-left: 1px #B8D0D6 solid; border-right: 1px #B8D0D6 solid">
	<div class="panelBar">
		<ul class="toolBar">
			<li><a class="add"
				href="${pageContext.request.contextPath }/sys/addressBook!edit.do?usercode={usercode}&bodytype=${param['s_bodytype']}"
				warn="请选择一个用户" target='navTab'><span>新增</span></a></li>
			<li><a class="delete"
				href="${pageContext.request.contextPath }/sys/addressBook!view.do?usercode={usercode}"
				warn="请选择一个用户" target='dialog'><span>删除</span></a></li>
			<li><a class="edit"
				href="${pageContext.request.contextPath }/sys/addressBook!edit.do?usercode={usercode}&bodytype=${param['s_bodytype']}"
				warn="请选择一个用户" target='navTab'><span>编辑</span></a></li>
			<li class="line">line</li>
		</ul>
	</div>

	<div layoutH="110" id="r_list_print">
	 <form id="mapinfoDetailForm">
	 	<input type="hidden" name="sourceUrl" value=""/>
	 	<input type="hidden" name="goalUrl" value=""/>
		<table class="table" width="100%"  layoutH="100" rel="jbsxBox">
			<thead>

				<tr>
					<th align="center">序号</th>
					<th align="center">源字段名</th>
					<th align="center">字段类型</th>
					<th align="center">目标字段名</th>
					<th align="center">字段类型</th>
					<th align="center">允许空</th>
					<th align="center">常量（高级优先）</th>
				</tr>
			</thead>
			<tbody>

					<tr target="usercode" rel="">
						<td align="center">暂无</td>
						<td align="center">暂无</td>
						<td align="center">暂无</td>
						<td align="center">暂无</td>
						<td align="center">暂无</td>
						<td align="center">暂无</td>
						<td align="center">暂无</td>
					</tr>
					
			</tbody>
		</table>
		</form>
	</div>
</div> --%>


