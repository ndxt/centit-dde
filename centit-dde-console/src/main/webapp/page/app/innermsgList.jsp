<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>


<%-- 标识 --%>
<c:choose>
    <c:when test="${cp:MAPVALUE('MSG_TYPE', 'A') eq param['s_msgtype'] }">
        <c:set var="MSGTYPE_A" value="true"/>
    </c:when>
</c:choose>

<div id="div_innermsg_list" class="pageHeader">
    <s:form id="pagerForm" onsubmit="return navTabSearch(this);" action="/app/innermsg!list.do" method="post">
        <input type="hidden" name="s_msgtype" value="${param['s_msgtype'] }"/>
        <input type="hidden" name="pageNum" value="1"/>
        <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}"/>
        <input type="hidden" name="orderField" value="${s_orderField}"/>

        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>消息标题:<s:textfield name="s_msgtitle" value="%{#parameters['s_msgtitle']}"/></td>
                    <td>起始时间:<input type="text" name="s_begsenddate" readonly="true" class="date" format="yyyy-MM-dd"
                                    yearstart="-20" yearend="5" value="${param['s_begsenddate'] }"/></td>
                    <td>结束时间:<input type="text" name="s_endsenddate" readonly="true" class="date" format="yyyy-MM-dd"
                                    yearstart="-20" yearend="5" value="${param['s_endsenddate'] }"/></td>
                    <c:if test="${empty MSGTYPE_A }">
                        <td>接收人:<s:textfield name="s_receivename" value="%{#parameters['s_receivename']}"/></td>
                    </c:if>
                </tr>
            </table>

            <div class="subBar">
                <ul>
                    <li>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <s:submit method="list" value="查询"/>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </s:form>
</div>

<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <li><a class="icon" href="${contextPath }/app/innermsg!view.do?{param}" rel=""
                   target='dialog'><span>详情</span></a></li>
            <c:if test="${empty MSGTYPE_A }">
                <li><a class="add" href="${contextPath }/app/innermsg!add.do?msgtype=P" rel="" target='dialog'><span>发送消息</span></a>
                </li>
                <li><a class="add" href="${contextPath }/app/innermsg!add.do?msgtype=A" rel="" target='dialog'><span>发送公告</span></a>
                </li>
                <li><a class="delete" href="${contextPath }/app/innermsg!deleteMsg.do?{param}" warn="请选择一条记录"
                       target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
            </c:if>
        </ul>
    </div>

    <div layoutH="116">
        <table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
            <thead>
            <tr align="center">
                <th>标题</th>
                <th>接收人</th>
                <th>内容</th>
                <th>发送时间</th>
                <th>类型</th>
                <th>状态</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${objList }" var="innermsg">
                <tr target="param" rel="msgcode=${innermsg.msgcode}&mailtype=O&msgtype=${innermsg.msgtype }"
                    align="center">

                    <td>${innermsg.msgtitle}
                        <c:if test="${not empty innermsg.msgannexs }"><span
                                class="nui-ico-att">&nbsp;&nbsp;&nbsp;</span></c:if>
                    </td>
                    <td><c:choose>
                        <c:when test="${cp:MAPVALUE('MSG_TYPE', 'A') eq innermsg.msgtype }">
                            所有
                        </c:when>
                        <c:when test="${cp:MAPVALUE('MSG_TYPE', 'P') eq innermsg.msgtype }">
                            ${innermsg.receivename}
                        </c:when>
                    </c:choose></td>
                    <td>${innermsg.msgcontent}</td>
                    <td><fmt:formatDate value="${innermsg.senddate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

                    <td><c:forEach var="msgType" items="${cp:DICTIONARY_D('MSG_TYPE') }">
                        <c:if test="${msgType.id.datacode eq innermsg.msgtype }">${msgType.datadesc }</c:if>
                    </c:forEach></td>
                    <td><c:forEach var="msgStat" items="${cp:DICTIONARY_D('MSG_STAT') }">
                        <c:if test="${msgStat.id.datacode eq innermsg.msgstate }">${msgStat.datadesc }</c:if>
                    </c:forEach></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>

