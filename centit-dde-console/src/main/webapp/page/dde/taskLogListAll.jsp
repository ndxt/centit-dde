<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<script src="${pageContext.request.contextPath}/scripts/print.js"
	type="text/javascript"></script>


<div class="pageHeader">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath }/dde/taskLog!listall.do" method="post">
		<input type="hidden" name="pageNum" value="1" /> 
	<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> 
	<input type="hidden" name="orderField" value="${s_orderField}" />
		<div class="searchBar">
			<ul class="searchContent">
				
				<li style="width: auto;"><label>执行方式：</label> <s:select 
                        list="#{'':'全部方式', '0':'自动', '1':'手动'}" cssClass="combox" listKey="key"
                        listValue="value"
                        value="#request.s_runType" name="s_runType"></s:select></li>
				
				<li style="width: auto;"><label>任务类型：</label> <s:select 
                        list="#{'':'全部类型', '1':'直接交换', '2':'导出离线文件','3':'监控文件夹导入文件','4':'调用接口'}" cssClass="combox" listKey="key"
                        listValue="value"
                        value="#request.s_taskType" name="s_taskType"></s:select></li>
                <li style="width: auto;">
							<label>开始时间：</label> 
							<input type="text" name="s_runBeginTime" class="date" value="${s_runBeginTime}"
							
								 />
							
				</li>
				<li style="width: auto;">	
				<label>至</label>		     
							<input type="text" name="s_runBeginTime2" class="date" value="${s_runBeginTime2}"
								 />
			    </li>
			    <li><label>只查失败:</label>
                    <input type="checkbox" name="s_isError" value="0" <c:if test="${'0' eq param['s_isError'] }">checked="checked" </c:if> />
				</li>
			</ul>
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type="submit">查询</button>
							</div>
						</div></li>
					<li>
			          <div class="buttonActive">
	                    <div class="buttonContent">
	                        <a onclick="tableToExcel('tbody_taskLogListAll', 'excel')">导出</a>
	                    </div>
	                  </div>
	                </li>
				</ul>
			</div>
			
		</div>
	</form>
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
					<th align="center" width="7%">交换任务</th>
					<th align="center" width="14%">执行开始时间</th>
					<th align="center" width="14%">执行结束时间</th>
					<th align="center" width="5%">执行方式</th>
					<th align="center" width="5%">任务类型</th>
					<th align="center" width="35%">其它提示信息</th>
					<th align="center" width="3%">操作</th>
				</tr>
			</thead>
			<tbody id="tbody_taskLogListAll">
			
				<c:forEach items="${objList }" var="taskLog"  varStatus="s">
						<tr>
							
								<td align="center"><c:out value="${s.index+1 }"></c:out></td>								
							
							    <td align="center" >
									<c:forEach var="row" items="${exchangeTasklist}">
                                     <c:if test="${row.taskId eq taskLog.taskId}"> 
                                     ${row.taskName}
                                     </c:if>
                                    </c:forEach>
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
							
								<td align="center" >
								    <c:if test="${!empty taskLog.taskType  && 1== taskLog.taskType }">直接交换</c:if> 
									<c:if test="${!empty taskLog.taskType  && 2== taskLog.taskType }">导出离线文件</c:if>
									<c:if test="${!empty taskLog.taskType  && 3== taskLog.taskType }">监控文件夹导入文件</c:if>
									<c:if test="${!empty taskLog.taskType  && 4== taskLog.taskType }">调用接口</c:if>
									<c:if test="${!empty taskLog.taskType  && 5== taskLog.taskType }">接口事件</c:if>
								</td>
							
								<td align="center" title="${taskLog.otherMessage}">${taskLog.otherMessage}</td>
								<td align="center">
								    <a href="${contextPath }/dde/taskLog!edit.do?logId=${taskLog.logId}" 
								        rel="ErrorData" target='navTab' width="850" height="500" title="任务明细日志"> <span class="icon icon-search"></span></a>
								</td>
							
						</tr>
				</c:forEach>
			</tbody>
		</table>
</div>
<jsp:include page="../common/panelBar.jsp"></jsp:include>

