<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post" action="${pageContext.request.contextPath}/dde/taskLog!list.do?s_taskId=${exchangeTask.taskId}">
	<input type="hidden" name="pageNum" value="1" /> 
	<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> 
	<input type="hidden" name="orderField" value="${s_orderField}" />
</form>

<div class="pageHeader">
        <div class="pageFormContent" style="height: <c:choose><c:when test='${"exchange" eq s_taskType }'>115</c:when><c:otherwise>85</c:otherwise></c:choose>px;">
            <div class="unit">
                <label>任务名称:</label>
                <input type="text" value="${exchangeTask.taskName}" readonly="readonly">
                <label>上次执行时间:</label>
                <input type="text" readonly="readonly" value="<fmt:formatDate value='${exchangeTask.lastRunTime}' pattern='yyyy-MM-dd HH:mm:ss' />" />
            </div>

            <c:if test="${s_taskType=='exchange' }">
                <div class="unit">
                    <label>下次执行时间:</label>
                    <input type="text" readonly="readonly" value="<fmt:formatDate value='${exchangeTask.nextRunTime}' pattern='yyyy-MM-dd HH:mm:ss' />" />

                    <label>状态:</label>
                    <c:if test="${!empty exchangeTask.isvalid  && 1== exchangeTask.isvalid }">
                        <input type="text" value="启用" readonly="readonly">
                    </c:if>
                    <c:if test="${!empty exchangeTask.isvalid  && 0== exchangeTask.isvalid }">
                        <input type="text" value="停用" readonly="readonly">
                    </c:if>
                </div>
            </c:if>

            <div class="unit">
                <label>任务描述：</label> <textarea rows="3" cols="69" name="mapInfoDesc" id="mapinfoDesc_edit">${exchangeTask.taskDesc }</textarea>

            </div>
        </div>
</div>

<div class="pageContent">

       <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		  <%@ include file="../common/panelBar.jsp"%>
		  <table class="list" width="98%" layoutH=".pageHeader <c:choose><c:when test='${"exchange" eq s_taskType }'>75</c:when><c:otherwise>55</c:otherwise></c:choose>">
	    </c:if>
	
	   <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		  <table class="list" width="98%" layoutH=".pageHeader 27">
	   </c:if>
	   
			<thead>
				<tr>
					<th align="center" width="4%">序号</th>
					<th align="center" width="10%">交换任务</th>
					<th align="center" width="10%">执行开始时间</th>
					<th align="center" width="10%">执行结束时间</th>
					<th align="center" width="5%">执行方式</th>
					<th align="center" width="5%">执行人员</th>
					<th align="center" width="40%">其它提示信息</th>
					<th align="center" width="5%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${objList }" var="taskLog"  varStatus="s">
						<tr>
							
								<td align="center"><c:out value="${s.index+1 }"></c:out></td>								
							
							    <td align="center" title="${exchangeTask.taskName}	">
									${exchangeTask.taskName}
								</td>
								
								<td align="center" title="<fmt:formatDate	value='${taskLog.runBeginTime}'	pattern='yyyy-MM-dd HH:mm:ss' />">
									<fmt:formatDate	value='${taskLog.runBeginTime}'	pattern='yyyy-MM-dd HH:mm:ss' />								
								</td>
							
								<td align="center" title="<fmt:formatDate	value='${taskLog.runEndTime}'	pattern='yyyy-MM-dd HH:mm:ss' />">
									<fmt:formatDate	value='${taskLog.runEndTime}'	pattern='yyyy-MM-dd HH:mm:ss' />		
								</td>
							
								<td align="center">
									<c:if test="${!empty taskLog.runType  && 1== taskLog.runType }">手动</c:if> 
									<c:if test="${!empty taskLog.runType  && 0== taskLog.runType }">自动</c:if> 
								</td>
							
								<td align="center" title="${cp:MAPVALUE("usercode",taskLog.runner)}">${cp:MAPVALUE("usercode",taskLog.runner)}</td>
							
								<td align="center" title="${taskLog.otherMessage}">${taskLog.otherMessage}</td>
								<td align="center" id="td_annex">
								    <a href="${contextPath }/dde/taskLog!edit.do?logId=${taskLog.logId}" 
								        rel="ErrorData" target='navTab' width="850" height="500" title="任务明细日志"> <span class="icon icon-search"></span></a>
								   <a href="#" onclick="downFile('${taskLog.logId}','${exchangeTask.taskName}')">下载</a>
								</td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
</div>
<jsp:include page="../common/panelBar.jsp"></jsp:include>
<script>
function downFile(id,name) {
	var url = "${contextPath }/dde/taskLog!downFile.do?logid=" + id+"&logname="+name;
	document.location.href = url;
}	
</script>
	
