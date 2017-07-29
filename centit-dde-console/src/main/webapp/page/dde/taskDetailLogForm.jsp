<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>
<script src="${pageContext.request.contextPath}/scripts/print.js"
	type="text/javascript" ></script>

<div class="pageHeader">
    <div class="pageFormContent" style="height: 135px;">
        <div class="unit">
            <label>交换名称：</label>
            <input name="mapInfoName" type="text" value="${exchangeMapInfo.mapInfoName }" readonly="readonly"/>
            <label>执行开始时间：</label>
            <input name="runBeginTime" type="text"
                   <c:if test="${!empty object.runBeginTime }">readonly="readonly"</c:if>
                   value="<fmt:formatDate value="${object.runBeginTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
        </div>


        <div class="unit">
            <label>执行结束时间：</label>
            <input name="runEndTime" type="text"
                   <c:if test="${!empty object.runEndTime }">readonly="readonly"</c:if>
                   value="<fmt:formatDate value="${object.runEndTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>"/>

            <label>成功条数：</label>
            <input name="successPieces" type="text"
                   <c:if test="${!empty object.successPieces }">readonly="readonly"</c:if>
                   value="${object.successPieces }"/>
        </div>

        <div class="unit">
            <label>失败条数：</label>
            <input name="errorPieces" type="text"
                   <c:if test="${!empty object.errorPieces }">readonly="readonly"</c:if>
                   value="${object.errorPieces }"/>
        </div>

        <div class="unit">
            <label>任务描述：</label>
            <textarea rows="3" cols="69" name="mapInfoDesc">${exchangeMapInfo.mapInfoDesc }</textarea>

        </div>
        <div class="buttonActive">
	                    <div class="buttonContent">
	                        <a onclick="tableToExcel('tbody_taskDetailLog', 'excel')">导出</a>
	                    </div>
	    </div>
    </div>
</div>


<div class="pageContent" layoutH="20">

    <s:form action="/dde/exchangeTask!saveSequence.do" class="pageForm required-validate"
            onsubmit="return dialogSearch(this);">

        <div>
            <table class="table" width="100%" targetType="navTab" asc="asc" desc="desc">
                <thead>

                <tr>
                    <th align="center" width="5%">序号</th>
                    <th align="center" width="30%">错误原因</th>
                    <th align="center" width="65%">错误内容</th>
                </tr>
                </thead>
                <tbody id="tbody_taskDetailLog">
                <c:forEach items="${object.taskErrorDatas}" var="taskErrorDatas" varStatus="s">
                    <tr target="pk" rel="${taskErrorDatas.dataId}">

                        <td align="center">
                                ${s.index+1 }
                        </td>
                        <td align="center" title='<c:out value="${taskErrorDatas.errorMessage}" escapeXml="true" />'>${taskErrorDatas.errorMessage}</td>
                        <td align="center" title='<c:out value="${taskErrorDatas.dataContent}" escapeXml="true" />'>${taskErrorDatas.dataContent}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

    </s:form>
</div>

