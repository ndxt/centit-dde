<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>




<div class="pageHeader">
    <div class="pageFormContent" style="height: 140px;">
        <div class="unit">
            <label>任务名称：</label>
            <input name="taskName" type="text" size="40" value="${exchangeTask.taskName }" readonly="readonly"/>
            <label>执行开始时间：</label>
            <input name="runBeginTime" type="text" <c:if test="${!empty object.runBeginTime }">readonly="readonly"</c:if>
                   size="40" value="<fmt:formatDate value="${object.runBeginTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
        </div>

        <div class="unit">
            <label>执行结束时间：</label>
            <input name="runEndTime" type="text" <c:if test="${!empty object.runEndTime }">readonly="readonly"</c:if>
                   size="40" value="<fmt:formatDate value="${object.runEndTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>"/>

            <label>执行方式：</label>
            <c:if test="${!empty object.runType  && 1== object.runType }">
                <input name="runType" type="text" <c:if test="${!empty object.runType }">readonly="readonly"</c:if> size="40" value="手动"/>
            </c:if>
            <c:if test="${!empty object.runType  && 0== object.runType }">
                <input name="runType" type="text" <c:if test="${!empty object.runType }">readonly="readonly"</c:if> size="40" value="自动"/>
            </c:if>
        </div>

        <div class="unit">
            <label>执行人员：</label>
            <input name="runner" type="text" <c:if test="${!empty object.runner }">readonly="readonly"</c:if>
                   size="40" value="${cp:MAPVALUE('usercode', object.runner) }"/>

            <label>任务描述：</label>
            <input id="mapinfoDesc_edit" name="taskDesc" type="text" readonly="readonly" size="40" value="${exchangeTask.taskDesc }"/>
        </div>

        <div class="unit">
            <label>其它提示信息：</label> <textarea readonly="readonly" rows="3" cols="109" name="otherMessage" >${object.otherMessage }</textarea>

        </div>
    </div>
</div>







<div class="pageContent" layoutH="175">
    <s:form action="/dde/exchangeTask!saveSequence.do" class="pageForm required-validate" onsubmit="return dialogSearch(this);">

        <div>
            <table class="list" width="100%" targetType="navTab" asc="asc"
                   desc="desc">
                <thead>

                <tr>
                    <th align="center" width="5%">序号</th>
                    <th align="center" width="15%">交换名称</th>
                    <th align="center" width="20%">执行开始时间</th>
                    <th align="center" width="20%">执行结束时间</th>
                    <th align="center" width="10%">成功条数</th>
                    <th align="center" width="11%">失败条数</th>
                    <th align="center" width="9%">操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${object.taskDetailLogs}" var="taskDetailLogs"
                           varStatus="s">
                    <tr target="pk" rel="${taskDetailLogs.logDetailId}">

                        <td align="center">
                                ${s.index+1 }
                        </td>
                        <td align="center">${taskDetailLogs.mapInfoName}</td>
                        <td align="center"><fmt:formatDate value="${taskDetailLogs.runBeginTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                        <td align="center"><fmt:formatDate value="${taskDetailLogs.runEndTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                        <td align="center">${taskDetailLogs.successPieces}</td>
                        <td align="center">${taskDetailLogs.errorPieces}</td>
                        <td align="center">
                            <a href="${contextPath }/dde/taskDetailLog!edit.do?logDetailId=${taskDetailLogs.logDetailId}"
                               rel="ErrorData" target='dialog' width="950" height="500" title="错误日志明细"
                               resizable="true" drawable="true" maxable="true" minable="true">
                                <span class="icon icon-search"></span></a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

    </s:form>
</div>

