<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<style>
 #table1{border-right:none;}
 #table1 tbody td{border-right:none;}
 #table1 thead th{border-right:none;}
</style>

<div class="pageContent" layoutH="0">
	<s:form action="/dde/exchangeTask!editAndsave.do" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
		<div class="pageFormContent">
			<input name="taskId" type="hidden" class="required"   value="${object.taskId }" /> <input type="hidden" name="s_taskType" value="${s_taskType }" />
			<p>
				<label>任务名称：</label> <input name="taskName" type="text"  value="${object.taskName }" />

			</p>

			<c:if test="${s_taskType=='1' or '2' eq s_taskType }">
				<p>
					<label>任务执行定时器：</label> <input id="txt_task_cron" name="taskCron" type="text" value="${object.taskCron }" /> <a class="btnLook"
						href="${pageContext.request.contextPath }/dde/exchangeTask!generateCronExpression.do?taskCron=${object.taskCron }&txtCallback=txt_task_cron" target="dialog" rel="generateCronExpression"
						mask="true" suggestFields="taskCron" title="生成表达式向导" width="420" height="334"> <span>生成表达式向导</span>
					</a>

				</p>
			</c:if>
			<p>
				<label>任务描述：</label> <input name="taskDesc" type="text"  value="${object.taskDesc }" />

			</p>

			<p>
				<label>上次执行时间：</label> <input name="lastRunTime" type="text" class="date" format="yyyy-MM-dd HH:mm:ss" yearstart="-5" yearend="5" readonly="readonly"
					value="<fmt:formatDate value="${object.lastRunTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>" /> <a class="inputDateButton" href="javascript:;">选择</a>
			</p>
			<c:if test="${s_taskType=='1' or '2' eq s_taskType }">
				<p>
					<label>下次执行时间：</label> <input name="nextRunTime" type="text" class="date" format="yyyy-MM-dd HH:mm:ss" yearstart="-5" yearend="5" readonly="readonly"
						value="<fmt:formatDate value="${object.nextRunTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>" /> <a class="inputDateButton" href="javascript:;">选择</a>
				</p>

				<p>
					<label>是否启用：</label>
						<input type="radio" name="isvalid" value="1" <c:if test="${1 eq object.isvalid }">checked="checked"</c:if> />启用 
						<input type="radio" name="isvalid" value="0" <c:if test="${1 ne object.isvalid }">checked="checked"</c:if> />停用
				</p>
			</c:if>
			<c:if test="${!empty object.createTime }">
				<p>
					<label>创建时间：</label> <input name="createTime" type="text" readonly="readonly"  value="${object.createTime }" />

				</p>
			</c:if>
			<c:if test="${!empty object.created }">
				<p>
					<label>创建人员：</label> <input name="createdName" type="text" readonly="readonly"  value="${object.createdName }" /> <input name="created" type="hidden" readonly="readonly" 
						value="${object.created }" />

				</p>
			</c:if>






			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">保存</button>
							</div>
						</div></li>
					<!-- <li>
						<div class="buttonActive">
							<div class="buttonContent">
								<button type="button" class="close">取消</button>
							</div>
						</div>
					</li> -->
				</ul>
			</div>
		</div>
	</s:form>

	<c:set var="importexchange">
		<div class="panelBar">
			<ul class="toolBar">
                <span style="color: green;">可拖动交换数据列更换交换任务执行顺序</span>

				<li class="new"><a class="add" href="${contextPath }/dde/exchangeTask!listExchangeMapInfo.do?s_taskId=${object.taskId }&s_taskType=${s_taskType }" target='dialog' rel="drjhdygx" width="800"
					height="630" mask="true"> <span> 添加${taskTypeList[s_taskType] }对应关系 </span>
				</a></li>
			</ul>
		</div>
	</c:set>
	${importexchange }

	<form action="${pageContext.request.contextPath }/dde/exchangeTask!saveSequence.do?s_taskType=${s_taskType }" class="pageForm required-validate" onsubmit="return navTabSearch(this);"
		id="saveSequence">

		<input name="taskId" type="hidden" value="${object.taskId }" /> <input name="inputPage" type="hidden" value="edit" />

		<div>
			<div style="float: left; width: 8%">
				<table class="list" width="100%" id="table1">
					<thead align="center">
						<tr>
							<th nowrap>执行顺序</th>
						</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${'1' eq s_taskType }">
							<c:forEach items="${echangeMapInfoList}" var="echangeMapInfo" varStatus="s">
								<tr>
									<td class="notMove" align="center">${s.index+1}</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:when test="${'2' eq s_taskType or '4' eq s_taskType }">
							<c:forEach items="${exportSqlList}" var="object" varStatus="s">
								<tr>
									<td class="notMove" align="center">${s.index+1 }</td>
								</tr>
							</c:forEach>
						</c:when>
					</c:choose>
					</tbody>
				</table>
			</div>
			<div style="float: left; width: 92%">
				<table id="t_exchange_task_form" class="list" width="100%" targetType="dialog" asc="asc" desc="desc">
					<thead>
						<tr>
							<%--<th align="center" width="8%" nowrap>${taskTypeList['4' eq s_taskType ? '' : s_taskType] }编号</th>--%>
						  <c:if test="${s_taskType=='1' }">
							<th align="center" width="8%" nowrap style="border-left: solid 1px #D0D0D0;">${taskTypeList[s_taskType] }名称</th>
							<th align="center" width="8%" nowrap>源数据库名</th>
							<th align="center" width="8%" nowrap>目标数据库名</th>
							<th align="center" width="8%" nowrap>目标表名</th>
							<th align="center" width="7%" nowrap>是否为重复执行</th>							
							<th align="center" width="15%" nowrap>${taskTypeList[s_taskType] }说明</th>
							<th align="center" width="8%" nowrap>操作</th>
						  </c:if>
						  <c:if test="${s_taskType!='1' }">
							<th align="center" width="20%" nowrap style="border-left: solid 1px #D0D0D0;">${taskTypeList[s_taskType] }名称</th>
							<th align="center" width="20%" nowrap>源数据库名</th>							
							<th align="center" width="15%" nowrap>${taskTypeList[s_taskType] }说明</th>
							<th align="center" width="7%" nowrap>操作</th>
						  </c:if>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${'1' eq s_taskType }">
								<c:forEach items="${echangeMapInfoList}" var="echangeMapInfo" varStatus="s">
									<tr target="pk" rel="${echangeMapInfo.mapInfoId}">
										<input name="mapInfoId" type="hidden" value="${echangeMapInfo.mapInfoId}" />
										<td align="center" class="dataClass">${echangeMapInfo.mapInfoName}</td>
										<td align="center">${echangeMapInfo.sourceDatabaseName}</td>
										<%-- <td align="center">${echangeMapInfo.querySql}</td> --%>
										<c:if test="${s_taskType=='1' }">
											<td align="center">${echangeMapInfo.destDatabaseName}</td>
											<td align="center">${echangeMapInfo.destTableName}</td>
											<td align="center"><c:if test="${!empty echangeMapInfo.isRepeat  && 1== echangeMapInfo.isRepeat }">是</c:if> <c:if
													test="${!empty echangeMapInfo.isRepeat  && 0== echangeMapInfo.isRepeat }">否</c:if></td>
										</c:if>
										<td align="center">${echangeMapInfo.mapInfoDesc}</td>
										<td align="center">
												<a href="${contextPath }/dde/exchangeMapinfoNew!showMapinfoDetail.do?mapInfoId=${echangeMapInfo.mapInfoId}&s_type=initfirst" target='navTab' rel="dygxmx" title="编辑${taskTypeList['4' eq s_taskType ? '' : s_taskType] }对应关系"><span
													class="icon icon-edit"></span></a>
												<a href="${contextPath }/dde/exchangeTaskDetail!delete.do?taskId=${object.taskId }&mapInfoId=${echangeMapInfo.mapInfoId}" target="ajaxTodo" title="删除${taskTypeList['4' eq s_taskType ? '' : s_taskType] }对应关系"><span
													class="icon icon-trash"></span></a>
											
										</td>
									</tr>
								</c:forEach>
							</c:when>
							<c:when test="${'2' eq s_taskType or '4' eq s_taskType }">
								<c:forEach items="${exportSqlList}" var="obj" varStatus="s">
									<tr target="pk" rel="${obj.exportId}">
										<input name="mapInfoId" type="hidden" value="${obj.exportId}" id="exportId" />
										<td align="center" class="dataClass">${obj.exportName}</td>
										<td align="center">${obj.sourceDatabaseName}</td>
										<td align="center">${obj.exportDesc}</td>
										<td align="center"> 
											<a href="${contextPath }/dde/exportSql!edit.do?exportId=${obj.exportId}&tabid=external_EXPORTSQL" target='navTab' rel="dygxmx" title="编辑${taskTypeList['2'] }对应关系"><span
												class="icon icon-edit"></span></a>
											<a href="${contextPath }/dde/exchangeTaskDetail!delete.do?cid.taskId=${object.taskId }&cid.mapInfoId=${obj.exportId}" target="ajaxTodo" title="删除${taskTypeList['2'] }对应关系"><span
												class="icon icon-trash"></span></a>
										</td>
									</tr>
								</c:forEach>
							</c:when>
						</c:choose>
					
					
					
					
					</tbody>
				</table>
			</div>
		</div>

	</form>
</div>

<script type="text/javascript">
	function saveSequence() {
		$("#saveSequence").submit();
	}

	$(function() {
		var options = {
			cursor : 'move', // selector 的鼠标手势
			sortBoxs : 'tbody.sortDrag', //拖动排序项父容器
			replace : true, //2个sortBox之间拖动替换
			items : '> tr', //拖动排序项选择器
			selector : 'td:first:not(.notMove)', //拖动排序项用于拖动的子元素的选择器，为空时等于item
			zIndex : 1000,
			formId : 'saveSequence'
		};
		$('tbody', navTab.getCurrentPanel()).sortDrag(options);
		var h = parseInt($("#t_exchange_task_form tbody").find("td:first").height());
		$("#table1 tbody").find("td").each(function(){
			$(this).height(h-1);
		});
	});
</script>

