<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>


<%-- 标识 --%>
<c:choose>
    <c:when test="${cp:MAPVALUE('MAIL_TYPE', 'O') eq param['s_mailtype'] }">
        <c:set var="MAILTYPE_O" value="true"/>
    </c:when>
    <c:when test="${cp:MAPVALUE('MAIL_TYPE', 'I') eq param['s_mailtype'] }">
        <c:set var="MAILTYPE_I" value="true"/>
    </c:when>
    <c:when test="${cp:MAPVALUE('MAIL_TYPE', 'T') eq param['s_mailtype'] }">
        <c:set var="MAILTYPE_T" value="true"/>
    </c:when>
    <c:when test="${cp:MAPVALUE('MAIL_TYPE', 'D') eq param['s_mailtype'] }">
        <c:set var="MAILTYPE_D" value="true"/>
    </c:when>
</c:choose>


<div class="pageContent">
    <c:if test="${!MailAccount }">
        <div layoutH="100%"
             style="float: left; display: block; overflow: auto; width: 19%; border: solid 1px #CCC; line-height: 21px; background: #fff">
            <ul class="tree treeFolder" id="ul_innermsg_mail_account">
                <li><a href="javascript">邮箱账户</a>
                    <ul>
                        <c:forEach var="L" items="${LISTMAILACCOUNT }" varStatus="v1">
                            <li><a href="javascript:void(0);">${L.mailaccount }</a>
                                <ul>
                                    <c:forEach var="c" items="${cp:DICTIONARY_D('MAIL_TYPE') }" varStatus="v2">
                                        <li>
                                            <a href="${contextPath }/app/innermsg!listMailBox.do?s_mailtype=${c.id.datacode }&s_emailid=${L.emailid }&account=true"
                                               target="ajax" rel="jbsxBox">${c.datadesc
                                                    }</a></li>
                                    </c:forEach>
                                </ul>
                            </li>
                        </c:forEach>
                    </ul>
                </li>
            </ul>
        </div>
    </c:if>
    <div id="jbsxBox" class="unitBox" <c:if test="${!MailAccount }"> style="margin-left: 246px;" </c:if>>
        <div class="pageHeader">
            <s:form id="pagerForm" onsubmit="return navTabSearch(this);" action="/app/innermsg!listMailBox.do"
                    method="post">
                <input type="hidden" name="s_msgtype" value="${param['s_msgtype'] }"/>
                <input type="hidden" name="s_mailtype" value="${param['s_mailtype'] }"/>
                <input type="hidden" name="s_emailid" value="${param['s_emailid'] }"/>
                <input type="hidden" name="pageNum" value="1"/>
                <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}"/>
                <input type="hidden" name="orderField" value="${s_orderField}"/>

                <div class="searchBar">
                    <table class="searchContent">
                        <tr>
                            <td>消息标题:<s:textfield name="s_msgtitle" value="%{#parameters['s_msgtitle']}"/></td>
                            <td>起始时间:<input type="text" name="s_begsenddate" readonly="true" class="date"
                                            format="yyyy-MM-dd" yearstart="-20" yearend="5"
                                            value="${param['s_begsenddate'] }"/></td>
                            <td>结束时间:<input type="text" name="s_endsenddate" readonly="true" class="date"
                                            format="yyyy-MM-dd" yearstart="-20" yearend="5"
                                            value="${param['s_endsenddate'] }"/></td>
                            <td>接收人:<s:textfield name="s_receivename" value="%{#parameters['s_receivename']}"/></td>
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
                            <c:if test="${MAILTYPE_O }">
                                <li>
                                    <div class="buttonActive">
                                        <div class="buttonContent">
                                            <a href="${contextPath }/app/innermsg!addMail.do?s_mailtype=${param['s_mailtype'] }&s_emailid=${param['s_emailid'] }"
                                               target="dialog">发送邮件</a>
                                        </div>
                                    </div>
                                </li>
                            </c:if>
                            <c:if test="${MAILTYPE_I }">
                                <li>
                                    <div class="buttonActive">
                                        <div class="buttonContent">
                                            <a href="${contextPath }/app/innermsg!saveReceiveMail.do?mailtype=${param['s_mailtype'] }&emailid=${param['s_emailid'] }"
                                               target="ajaxTodo" title="是否收取邮件?">收件</a>
                                        </div>
                                    </div>
                                </li>
                            </c:if>
                        </ul>
                    </div>
                </div>
            </s:form>
        </div>

        <div class="pageContent">
            <div class="panelBar">
                <ul class="toolBar">
                    <li><a class="icon"
                           href="${contextPath }/app/innermsg!view.do?{param}&s_mailtype=<c:choose><c:when test="${MAILTYPE_D }">${cp:MAPVALUE('MAIL_TYPE', 'O') }</c:when><c:otherwise>${param['s_mailtype'] }</c:otherwise></c:choose>&s_emailid=${param['s_emailid'] }"
                           target='<c:choose><c:when test="${MAILTYPE_D }">dialog</c:when><c:otherwise>navTab</c:otherwise></c:choose>'><span>详情</span></a>
                    </li>
                    <c:if test="${MAILTYPE_T }">
                        <li><a class="add"
                               href="${contextPath }/app/innermsg!modifyMailBox.do?{param}&s_p=${param['s_p']}"
                               warn="请选择一条记录" target="ajaxTodo" title="确定要恢复吗?"><span>恢复</span></a></li>
                    </c:if>
                    <li>

                        <a class="delete"
                           href="${contextPath }/app/innermsg!modifyMailBox.do?{param}&mailtype=T&s_p=${param['s_p']}"
                           warn="请选择一条记录" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>

                </ul>
            </div>

            <div layoutH="116">
                <table class="list" width="98%" targetType="navTab" asc="asc" desc="desc">
                    <thead>
                    <tr align="center">
                        <th>标题</th>
                        <c:if test="${!MAILTYPE_D }">
                            <th>发件人</th>
                        </c:if>
                        <th>接收人</th>
                        <th>抄送人</th>
                        <th>密送人</th>
                        <!-- <th>内容</th> -->
                        <th>发送时间</th>
                        <!-- <th>类型</th> -->
                        <th>状态</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${objList }" var="innermsg">
                        <tr target="param"
                            rel="msgcode=<c:out value='${innermsg.msgcode }' escapeXml='true' />&mailUnDelType=${param['s_mailtype'] }&msgtype=${innermsg.msgtype }"
                            align="center">

                            <td><c:choose>
                                <c:when test="${fn:length(innermsg.msgtitle) gt 10 }">${fn:substring(innermsg.msgtitle, 0, 10) }...</c:when>
                                <c:otherwise>${innermsg.msgtitle }</c:otherwise>
                            </c:choose>
                                <c:if test="${not empty innermsg.msgannexs }"><span class="nui-ico-att">&nbsp;&nbsp;&nbsp;</span></c:if>
                            </td>
                            <c:if test="${!MAILTYPE_D }">
                                <td><c:if test="${MAILTYPE_I}">
                                    <c:out value="${innermsg.sender }" escapeXml="true"/>
                                </c:if> <c:if test="${MAILTYPE_O }">
                                    <c:out value="${innermsg.c.mailaccount }" escapeXml="true"/>
                                </c:if> <c:if test="${MAILTYPE_T }">
                                    <c:if test="${cp:MAPVALUE('MAIL_TYPE', 'O') eq innermsg.mailUnDelType }">
                                        <c:out value="${innermsg.c.mailaccount }" escapeXml="true"/>
                                    </c:if>
                                    <c:if test="${cp:MAPVALUE('MAIL_TYPE', 'I') eq innermsg.mailUnDelType }">
                                        <c:out value="${innermsg.sender }" escapeXml="true"/>
                                    </c:if>
                                </c:if></td>
                            </c:if>

                                <%-- 表格中只显示第一个收件人 --%>
                            <c:set var="index_t" value="0"/>
                            <c:set var="index_c" value="0"/>
                            <c:set var="index_b" value="0"/>
                            <td><c:forEach var="ir" items="${innermsg.innermsgRecipients }" varStatus="i">
                                <c:if test="${(cp:MAPVALUE('RECIPIENT_TYPE', 'T') eq ir.mailtype) }">
                                    <c:set var="index_t" value="${index_t + 1 }"/>
                                    <c:if test="${1 eq index_t }">
                                        <c:out value="${ir.receive }" escapeXml="true"/>
                                    </c:if>
                                </c:if>
                            </c:forEach></td>
                            <td><c:forEach var="ir" items="${innermsg.innermsgRecipients }" varStatus="i">
                                <c:if test="${(cp:MAPVALUE('RECIPIENT_TYPE', 'C') eq ir.mailtype) }">
                                    <c:set var="index_c" value="${index_c + 1 }"/>
                                    <c:if test="${1 eq index_c }">
                                        <c:out value="${ir.receive }" escapeXml="true"/>
                                    </c:if>
                                </c:if>
                            </c:forEach></td>
                            <td><c:forEach var="ir" items="${innermsg.innermsgRecipients }" varStatus="i">
                                <c:if test="${(cp:MAPVALUE('RECIPIENT_TYPE', 'B') eq ir.mailtype) }">
                                    <c:set var="index_b" value="${index_b + 1 }"/>
                                    <c:if test="${1 eq index_b }">
                                        <c:out value="${ir.receive }" escapeXml="true"/>
                                    </c:if>
                                </c:if>
                            </c:forEach></td>
                                <%-- <td>${innermsg.msgcontent}</td> --%>
                            <td><fmt:formatDate value="${innermsg.senddate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>

                                <%-- <td><c:forEach var="msgType" items="${msgTypes }">
                                <c:if test="${msgType.value eq innermsg.msgtype }">${msgType.text }</c:if>
                            </c:forEach></td> --%>
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
    </div>
</div>


