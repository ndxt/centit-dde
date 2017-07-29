<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>

<div class="pageHeader">
    <s:form id="pagerForm" onsubmit="return navTabSearch(this);" action="/app/innermsgRecipient!list.do" method="post">
        <div class="searchBar">
            <table class="searchContent">
                <tr>
                    <td>消息标题:<s:textfield name="s_msgtitle" value="%{#parameters['s_msgtitle']}"/>
                    </td>
                    <td>起始时间:<input type="text" name="s_begsenddate" readonly="true" class="date" format="yyyy-MM-dd"
                                    yearstart="-20" yearend="5"
                                    value="${param['s_begsenddate'] }"/></td>
                    <td>结束时间:<input type="text" name="s_endsenddate" readonly="true" class="date" format="yyyy-MM-dd"
                                    yearstart="-20" yearend="5"
                                    value="${param['s_endsenddate'] }"/></td>
                    <td>发件人:<s:textfield name="s_sender" value="%{#parameters['s_sender']}"/>
                    </td>
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
            <li><a class="icon" href="${contextPath }/app/innermsgRecipient!view.do?{param}" rel=""
                   target='dialog'><span>详情</span></a></li>
            <li><a class="edit" href="${contextPath }/app/innermsgRecipient!recipient.do?{param}" warn="请选择一条记录" rel=""
                   target='dialog'><span>回复</span></a></li>
            <li><a class="delete" href="${contextPath }/app/innermsg!deleteMsg.do?{param}" warn="请选择一条记录"
                   target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
        </ul>
    </div>

    <div layoutH="116">
        <table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
            <thead>

            <tr>
            <tr align="center">
                <th>标题</th>
                <th>发收人</th>
                <th>内容</th>
                <th>发送时间</th>
                <th>状态</th>
            </tr>

            </tr>
            </thead>
            <tbody>
            <c:forEach items="${objList }" var="innermsgRecipient">
                <tr target="param"
                    rel="msgcode=${innermsgRecipient.innermsg.msgcode}&mailtype=I&id=${innermsgRecipient.id }"
                    align="center">

                    <td>
                            ${innermsgRecipient.innermsg.msgtitle}
                        <c:if test="${not empty innermsgRecipient.innermsg.msgannexs }"><span class="nui-ico-att">&nbsp;&nbsp;&nbsp;</span></c:if>
                    </td>

                    <td>${cp:MAPVALUE("usercode", innermsgRecipient.innermsg.sender)}</td>

                    <td>${innermsgRecipient.innermsg.msgcontent}</td>
                    <td><fmt:formatDate value="${innermsgRecipient.innermsg.senddate}"
                                        pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><c:forEach var="msgStat" items="${cp:DICTIONARY_D('MSG_STAT') }">
                        <c:if test="${msgStat.id.datacode eq innermsgRecipient.msgstate }">${msgStat.datadesc }</c:if>
                    </c:forEach></td>

                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../common/panelBar.jsp"></jsp:include>

