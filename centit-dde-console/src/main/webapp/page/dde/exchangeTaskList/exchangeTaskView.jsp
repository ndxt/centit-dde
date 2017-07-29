<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent" layoutH="20">
	<div>


		<div class="pageFormContent">

			<input name="taskId" type="hidden" class="required" size="40" value="${object.taskId }" />
			<p>
				<label>任务名称：</label> <input name="taskName" type="text" <c:if test="${!empty object.taskName }">readonly="readonly"</c:if> size="40" value="${object.taskName }" />

			</p>
			<c:if test="${s_taskType=='exchange' }">
				<p>
					<label>任务执行定时器：</label> <input name="taskCron" type="text" readonly="readonly" size="40" value="${object.taskCron }" />

				</p>
			</c:if>
			<p>
				<label>任务描述：</label> <input name="taskDesc" type="text" <c:if test="${!empty object.taskDesc }">readonly="readonly"</c:if> size="40" value="${object.taskDesc }" />
			</p>

			<p>
				<label>上次执行时间：</label> <input name="lastRunTime" type="text" readonly="readonly" format="yyyy-MM-dd HH:mm:ss" size="40"
					value="<fmt:formatDate value="${object.lastRunTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>" />
				<!-- <a class="inputDateButton" href="javascript:;">选择</a> -->
			</p>
			<c:if test="${s_taskType=='exchange' }">
				<p>
					<label>下次执行时间：</label> <input name="nextRunTime" type="text" readonly="readonly" format="yyyy-MM-dd HH:mm:ss" yearstart="-5" yearend="5"
						<c:if test="${!empty object.nextRunTime }">readonly="readonly"</c:if> size="40" value="<fmt:formatDate value="${object.nextRunTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>" />
				</p>
				<p>
					<label>是否启用：</label> <input type="radio" disabled="disabled" name="isValid" value="1" <c:if test="${!empty object.isvalid  && 1== object.isvalid }">checked="checked"</c:if> />启用 <input
						type="radio" name="isValid" value="0" disabled="disabled" <c:if test="${!empty object.isvalid  && 0== object.isvalid }">checked="checked"</c:if> />停用
				</p>
			</c:if>
			<c:if test="${!empty object.createTime }">
				<p>
					<label>创建时间：</label> <input name="createTime" type="text" readonly="readonly" size="40" value="${object.createTime }" />
				</p>
			</c:if>
			<c:if test="${!empty object.created }">
				<p>
					<label>创建人员：</label> <input name="created" type="text" readonly="readonly" size="40" value="${object.createdName }" /> <input name="created" type="hidden" readonly="readonly" size="40"
						value="${object.created }" />
				</p>
			</c:if>
		</div>
	</div>

	<s:form action="/dde/exchangeTask!saveSequence.do" class="pageForm required-validate" onsubmit="return dialogSearch(this);">

		<input name="taskId" type="hidden" value="${object.taskId }" />
		<input name="inputPage" type="hidden" value="view" />

		<div>
			<table class="list" width="100%" targetType="navTab" asc="asc" desc="desc">
				<thead>

					<tr>
						<th align="center" width="8%">执行顺序</th>
						<th align="center" width="8%">${taskTypeList[s_taskType] }名称</th>
						<th align="center" width="8%">源数据库名</th>
						<!-- 	<th align="center" width="25%">数据源sql语句</th> -->
						<c:if test="${1 eq s_taskType }">
							<th align="center" width="8%">目标数据库名</th>
							<th align="center" width="8%">目标表名</th>
							<th align="center" width="7%">是否为重复执行</th>
						</c:if>
						<th align="center" width="15%">${taskTypeList[s_taskType] }说明</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${1 eq s_taskType }">
							<c:forEach items="${echangeMapInfoList}" var="echangeMapInfo" varStatus="s">
								<tr target="pk" rel="${exchangeTask.taskId}">
		
									<td align="center">${s.index+1 } <input name="mapInfoId" type="hidden" value="${echangeMapInfo.mapInfoId}" />
		
									</td>
									<td align="center">${echangeMapInfo.mapInfoName}</td>
									<td align="center">${echangeMapInfo.sourceDatabaseName}</td>
									<%-- <td align="center">${echangeMapInfo.querySql}</td> --%>
									<td align="center">${echangeMapInfo.destDatabaseName}</td>
									<td align="center">${echangeMapInfo.destTableName}</td>
									<td align="center">
										<c:if test="${1 eq echangeMapInfo.isRepeat }">是</c:if> 
										<c:if test="${1 ne echangeMapInfo.isRepeat }">否</c:if>
									</td>
									<td align="center">${echangeMapInfo.mapInfoDesc}</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:when test="${2 eq s_taskType }">
							<c:forEach items="${exportSqlList}" var="object" varStatus="s">
								<tr target="pk" rel="${object.exportId}">
									<td align="center">${s.index+1 } <input name="exportId" type="hidden" value="${object.exportId }" /></td>
									<td align="center">${object.exportName}</td>
									<td align="center">${object.sourceDatabaseName}</td>
									<td align="center">${object.exportDesc}</td>
								</tr>
							</c:forEach>
						</c:when>
					</c:choose>
				
				</tbody>
			</table>
		</div>

	</s:form>
</div>

