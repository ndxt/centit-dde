<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<form id="pagerForm" method="post"
	action="${pageContext.request.contextPath}/dde/exchangeTask!list.do">
	<input type="hidden" name="pageNum" value="1" /> 
	<input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" /> 
	<input type="hidden" name="orderField" value="${s_orderField}" />
	<input type="hidden" name="s_taskType" value="${s_taskType }" />
	<input type="hidden" name="s_isvalid" value="${s_isvalid }" />
</form>



<div class="pageHeader">
	<s:form id="pagerForm" onsubmit="return navTabSearch(this);"
		action="/dde/exchangeTask!list.do" method="post">
		<div class="searchBar">
			<ul class="searchContent">


				<li><label>任务名称:</label> 
					<s:textfield name="s_taskName" value="%{#parameters['s_taskName']}" />
					<input type="hidden" name="s_taskType" value="${s_taskType }" />
				</li>
                <li><label>包含禁用:</label>

                    <input type="checkbox" name="s_isvalid" value="0" <c:if test="${'0' eq param['s_isvalid'] }">checked="checked" </c:if>>
				</li>

				

			</ul>
			<!-- <ul class="searchContent">
	

				

			</ul> -->
			<div class="subBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<s:submit method="list" value="查询"></s:submit>
							</div>
						</div></li>
					<li>
						<div class="buttonActive">
							<div class="buttonContent">
								<!-- 参数 navTabId 根据实际情况填写 -->
								<button type="button"
									onclick="javascript:navTabAjaxDone({'statusCode' : 200, 'callbackType' : 'closeCurrent', 'navTabId' : ''});">返回</button>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</s:form>
</div>

<div class="pageContent">
    <c:set var="addexchangetask">
        <div class="panelBar">
            <ul class="toolBar">
                <li class="new">
                  <a class="add" href="${contextPath }/dde/exchangeTask!add.do?s_taskType=${s_taskType}"  target='dialog' width="550" height="330">
	                  <span>
	                  	添加${taskTypeList[s_taskType] }任务
	                  </span>
                  </a>                   
                </li>
            </ul>
        </div>
    </c:set>
    ${addexchangetask }
    
	    <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 2}">
		  <%@ include file="../common/panelBar.jsp"%>
		  <table class="list" width="100%" layoutH=".pageHeader 82">
	    </c:if>
	
	   <c:if test="${cp:MAPVALUE('SYSPARAM', 'PAGINATION_NUM') == 1}">
		  <table class="list" width="100%" layoutH=".pageHeader 27">
	   </c:if>
	   
			<thead>

				<tr>
					<th align="center" width="5%">序号</th>
					<th align="center" width="10%">任务名称</th>
					<c:if test="${s_taskType=='exchange' }">
						<th align="center" width="10%">任务执行定时器</th>
					</c:if>
					<th align="center" width="15%">任务描述</th>
					<th align="center">上次执行时间</th>
					<c:if test="${s_taskType=='1' }">
						<th align="center">下次执行时间</th>
						<th align="center" width="5%">状态</th>
					</c:if>
					<th align="center">创建时间</th>
					<th align="center">创建人员</th>
					<th align="center" width="15%">操作</th>
				</tr>
			</thead>
			<tbody id="tbody_exchange_task_list">
				<c:forEach items="${objList }" var="exchangeTask" varStatus="s">
					<tr>		
						<td align="center"><c:out value="${s.index+1 }"></c:out></td>
						<td align="center" title="${exchangeTask.taskName}">${exchangeTask.taskName}</td>
						<c:if test="${s_taskType=='exchange' }">
							<td align="center" title="${exchangeTask.taskCron}">${exchangeTask.taskCron}</td>
						</c:if>
						<td align="center" title="${exchangeTask.taskDesc}">${exchangeTask.taskDesc}</td>
						<td align="center"><fmt:formatDate
								value='${exchangeTask.lastRunTime}'
								pattern='yyyy-MM-dd HH:mm:ss' /></td>
						<c:if test="${s_taskType=='1' }">
							<td align="center"><fmt:formatDate
									value='${exchangeTask.nextRunTime}'
									pattern='yyyy-MM-dd HH:mm:ss' /></td>
							<td align="center">
							<c:if test="${!empty exchangeTask.isvalid  && 1== exchangeTask.isvalid }">启用</c:if> 
							<c:if test="${!empty exchangeTask.isvalid  && 0== exchangeTask.isvalid }">停用</c:if> 
							</td>
						</c:if>
						<td align="center"><fmt:formatDate
								value='${exchangeTask.createTime}' pattern='yyyy-MM-dd HH:mm:ss' />
						</td>
						<td align="center">${exchangeTask.createdName}</td>
						<td align="center">
							
							
							<%-- <c:set var="viewtext"/>
							<c:set var="edittext"/>
							<c:set var="viewlogtext"/>
							<c:set var="transfertext"/>
							<c:choose>
								<c:when test="${'1' eq s_taskType }">
									<c:set var="viewtext" value="查看交换任务明细"/>
									<c:set var="edittext" value="编辑交换任务"/>
									<c:set var="viewlogtext" value="查看交换任务日志"/>
									<c:set var="transfertext" value="执行交换任务"/>
								</c:when>
								<c:when test="${'2' eq s_taskType }"></c:when>
								<c:when test="${'3' eq s_taskType }"></c:when>
							</c:choose> --%>
							
				            <%-- <a href="${contextPath }/dde/exchangeTask!view.do?taskId=${exchangeTask.taskId}&s_taskType=${s_taskType }"
				                 target="navTab" rel="jhrwmx" title="查看${taskTypeList[s_taskType] }任务明细"><span class="icon icon-search"></span></a> --%>   
				                 
				            
				                 
				                  
						    <a href="${contextPath }/dde/exchangeTask!edit.do?taskId=${exchangeTask.taskId}&s_taskType=${s_taskType }"
                                 <c:choose><c:when test="${'3' eq s_taskType }">target='dialog' resizable="false" maxable="false" width='500' height='380'</c:when><c:otherwise>target='navTab'</c:otherwise></c:choose> 
                                 title="编辑${taskTypeList[s_taskType] }任务" rel="bjjhrw"><span class="icon icon-edit"></span></a>
                                 
                                 <!-- 保持数据完整性，只能禁用 -->
                            <%-- <a href="${contextPath }/dde/exchangeTask!delete.do?taskId=${exchangeTask.taskId}&s_taskType=${s_taskType }"
				                 target="ajaxTodo" title="删除${taskTypeList[s_taskType] }任务"><span class="icon icon-trash"></span></a> --%>
				            <a href="${contextPath }/dde/taskLog!list.do?s_taskId=${exchangeTask.taskId}&s_taskType=${s_taskType }"
				                target="navTab" width="800" rel="dlg_tasklog" title="查看${taskTypeList[s_taskType] }任务日志"><span class="icon icon-document"></span></a>
                            
                                <a href="${contextPath }/dde/exchangeTask!console.do?taskId=${exchangeTask.taskId}&s_taskType=${s_taskType }"
                                    target="navTab" rel="${exchangeTask.taskId}" title="查看${taskTypeList[s_taskType] }即时交换信息"><span class="icon icon-search"></span></a>
                            

				            <a href="${contextPath }/dde/transfer!doTransfer.do?id=${exchangeTask.taskId}&s_taskType=${s_taskType }"
				                target="ajaxTodo" title="执行${taskTypeList[s_taskType] }任务"><span taskid='${exchangeTask.taskId}' class="icon icon-arrowrefresh-n"> </span></a>
						
						
							<%-- <c:if test="${s_taskType=='1' }">
					            <a href="${contextPath }/dde/exchangeTask!view.do?taskId=${exchangeTask.taskId}&s_taskType=${s_taskType }"
					                 target="navTab" rel="jhrwmx" title="查看交换任务明细"><span class="icon icon-search"></span></a>    
							    <a href="${contextPath }/dde/exchangeTask!edit.do?taskId=${exchangeTask.taskId}&s_taskType=${s_taskType }"
	                                 target='navTab' title="编辑交换任务" rel="bjjhrw"><span class="icon icon-edit"></span></a>
	                                 
	                                 <!-- 保持数据完整性，只能禁用 -->
	                            <a href="${contextPath }/dde/exchangeTask!delete.do?taskId=${exchangeTask.taskId}&s_taskType=${s_taskType }"
					                 target="ajaxTodo" title="删除交换任务"><span class="icon icon-trash"></span></a>
					            <a href="${contextPath }/dde/taskLog!list.do?s_taskId=${exchangeTask.taskId}&s_taskType=${s_taskType }"
					                target="navTab" width="800" rel="dlg_tasklog" title="查看交换任务日志"><span class="icon icon-document"></span></a>
					            <a href="${contextPath }/dde/transfer!doTransfer.do?id=${exchangeTask.taskId}&s_taskType=${s_taskType }" 
					                target="ajaxTodo" title="执行交换任务"><span class="icon icon-arrowrefresh-n"> </span></a> 
					         </c:if>
							<c:if test="${s_taskType=='2' }">
					            <a href="${contextPath }/dde/exchangeTask!view.do?taskId=${exchangeTask.taskId}&s_taskType=${s_taskType }"
					                 target="navTab" rel="jhrwmx" title="查看导出任务明细"><span class="icon icon-search"></span></a>    
							    <a href="${contextPath }/dde/exchangeTask!edit.do?taskId=${exchangeTask.taskId}&s_taskType=${s_taskType }"
	                                 target='navTab' title="编辑导出任务" rel="bjjhrw"><span class="icon icon-edit"></span></a>
	                            <a href="${contextPath }/dde/exchangeTask!delete.do?taskId=${exchangeTask.taskId}&s_taskType=${s_taskType }"
					                 target="ajaxTodo" title="删除导出任务"><span class="icon icon-trash"></span></a>
					            <a href="${contextPath }/dde/taskLog!list.do?s_taskId=${exchangeTask.taskId}&s_taskType=${s_taskType }"
					                target="navTab" width="800" rel="dlg_tasklog" title="查看导出任务日志"><span class="icon icon-document"></span></a>
					            <a href="${contextPath }/dde/transfer!doTransfer.do?id=${exchangeTask.taskId}&s_taskType=${s_taskType }" 
					                target="ajaxTodo" title="执行导出任务"><span class="icon icon-arrowrefresh-n"> </span></a> 
					         </c:if> --%>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
</div>

<script type="text/javascript">
    var TASKLOG = {
    		
        $divtaskconsole : $('#div_task_console'),

        start : function(taskId) {
            //将执行按钮禁用
            debugger;
            TASKLOG.getExecuteButton(taskId, true);


            var $spinner = $('#spinner', TASKLOG.getCurrentPanel(taskId));
            if('1' == $spinner.attr('start')) {
                return;
            }


            $spinner.attr('start', 1);
            $spinner.show();
        },

        stop : function(taskId) {
            var $spinner = $('#spinner', TASKLOG.getCurrentPanel(taskId));
            $spinner.attr('start', 0);
            $spinner.hide();

            TASKLOG.getExecuteButton(taskId, false);

            alert('数据交换完毕');
        },

        /**
         *输出交换日志信息
         * @param jsonResult
         */
        console : function(taskId, message) {
            TASKLOG.start(taskId);
            TASKLOG.scrollTop(taskId);
            $(TASKLOG.$divtaskconsole, TASKLOG.getCurrentPanel(taskId)).append('<div class="break console">[INFO]' + message + '</div>');
        },

        /**
         *输出交换日志信息
         * @param jsonResult
         */
        consoleInfo : function(taskId, message) {
            TASKLOG.start(taskId);
            TASKLOG.scrollTop(taskId);
            $(TASKLOG.$divtaskconsole, TASKLOG.getCurrentPanel(taskId)).append('<div class="break console"style="color:  green;" >[INFO]' + message + '</div>');
        },

        /**
         *输出交换错误日志信息
         * @param jsonResult
         */
        consoleError : function(taskId, message) {
            TASKLOG.start(taskId);
            TASKLOG.scrollTop(taskId);
            $(TASKLOG.$divtaskconsole, TASKLOG.getCurrentPanel(taskId)).append('<div class="break console" style="color: red;">[ERROR]' + message + '</div>');
        },


        /**
         * 输出交换条数信息
         * @param jsonResult
         */
        process : function(taskId, all, success, error, exchangeMapinfoName) {
            var $info = $('#div_task_info', TASKLOG.getCurrentPanel(taskId));

            $('#task_id', $info).text(taskId);
            $('#span_all', $info).text(all);
            $('#span_success', $info).text(success);
            $('#span_error', $info).text(error);
			$('#span_exchangeName', $info).text(exchangeMapinfoName);

            TASKLOG.start(taskId);
        },

		/**
         * 输出已执行完的交换对应关系名称
         * @param jsonResult
         */
		alreadyProcess : function(taskId, message) {
            var $alreadyProcess = $('#div_already_process', TASKLOG.getCurrentPanel(taskId));
			var msgs = message.split('_split_');

			message = '';
			for(var i = 0; i < msgs.length; i++) {
				if('' != $.trim(msgs[i])) {
					message += '<p>' + msgs[i] + '</p>';
				}
			}
			$alreadyProcess.html(message);

            TASKLOG.start(taskId);
        },

        scrollTop : function(taskId) {
            // var $panel = TASKLOG.getCurrentPanel(taskId);

            //$panel.scrollTop($panel.height());
            //$(window).scrollTop($panel[0].scrollHeight);
            //$('div.navTab-panel').scrollTop($panel.height() + 1000);
        },

        isCurrentConsole : function(taskId) {
            var $tabObject = TASKLOG.getCurrentPanel(taskId);

            return 'undefined' != typeof $tabObject && 0 != $tabObject.size();
        },

        getCurrentPanel : function(taskId) {
            var $tbodyexchangetasklist = $('#tbody_exchange_task_list');
            var $panel = $tbodyexchangetasklist.data('panel_' + taskId);

            if('undefined' == typeof $panel || null == $panel || 0 == $panel.size()) {
                $panel = navTab._getPanel(taskId);

                $tbodyexchangetasklist.data('panel_' + taskId, $panel);
            }

            return $panel;
        },

        getExecuteButton : function(taskId, disabled) {
            var $tbodyexchangetasklist = $('#tbody_exchange_task_list');

            var $span = $tbodyexchangetasklist.data(taskId);
            if('undefined' == typeof $span || null == $span || 0 == $span.size()) {
                $span = $tbodyexchangetasklist.find('span[taskid="' + taskId + '"]');
                $tbodyexchangetasklist.data(taskId, $span);
            }


            if(disabled) {
                $span.hide();
            } else {
                $span.show();
            }

        }
    };

</script>

<jsp:include page="../common/panelBar.jsp"></jsp:include>
