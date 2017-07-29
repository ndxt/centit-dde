<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>

<%-- 标识 --%>
<c:choose>
    <c:when test="${cp:MAPVALUE('MAIL_TYPE', 'O') eq param['s_mailtype'] }">
        <c:set var="sender" value='${object.c.mailaccount }'/>
    </c:when>
    <c:otherwise>
        <c:set var="sender" value='${object.sender }'/>
    </c:otherwise>
</c:choose>


<div class="pageContent">
    <s:form id="pagerForm" cssClass="pageForm required-validate" onsubmit="return navTabSearch(this);"
            action="/app/innermsg!saveSendMail.do" method="post">
        <input type="hidden" name="to" value='<c:out value="${sender }" escapeXml="true" />'/>
        <input type="hidden" name="msgtitle" value="回复  msgtitle"/>
        <input type="hidden" name="emailid" value="${object.c.emailid }"/>

        <div class="pageFormContent" style="height: 100%;">

            <dl>
                <dt>标题：</dt>
                <dd>${object.msgtitle }</dd>
            </dl>
            <dl>
                <dt>发件人：</dt>
                <dd>
                    <c:out value="${sender }" escapeXml="true"/>
                </dd>
            </dl>

            <dl>
                <dt>接收人：</dt>
                <dd>
                    <c:forEach var="ir" items="${object.innermsgRecipients }" varStatus="i">
                        <c:if test="${(cp:MAPVALUE('RECIPIENT_TYPE', 'T') eq ir.mailtype) }">
                            <c:out value="${ir.receive }" escapeXml="true"/>;
                        </c:if>
                    </c:forEach>
                </dd>
            </dl>
            <dl>
                <dt>抄送人：</dt>
                <dd>
                    <c:forEach var="ir" items="${object.innermsgRecipients }" varStatus="i">
                        <c:if test="${(cp:MAPVALUE('RECIPIENT_TYPE', 'C') eq ir.mailtype) }">
                            <c:out value="${ir.receive }" escapeXml="true"/>;

                        </c:if>
                    </c:forEach>
                </dd>
            </dl>
            <c:if test="${cp:MAPVALUE('MAIL_TYPE', 'O') eq param['s_mailtype'] }">
                <dl>
                    <dt>密送人：</dt>
                    <dd>
                        <c:forEach var="ir" items="${object.innermsgRecipients }" varStatus="i">
                            <c:if test="${(cp:MAPVALUE('RECIPIENT_TYPE', 'B') eq ir.mailtype)}">
                                <c:out value="${ir.receive }" escapeXml="true"/>;
                            </c:if>
                        </c:forEach>
                    </dd>
                </dl>
            </c:if>
            <dl>
                <dt>发送时间：</dt>
                <dd>
                    <fmt:formatDate value="${object.senddate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                </dd>
            </dl>
            <div class="divider"></div>

            <c:if test="${not empty object.msgannexs}">
                <dl class="nowrap">
                    <dt>附件：</dt>
                    <dd><c:forEach var="annex" items="${object.msgannexs }">
                        ${annex.fileinfo.filename } <fmt:formatNumber value="${annex.fileinfo.filesize / 1024 / 1024 }"
                                                                      type="number" pattern="#.##"/> MB
                        <a target="download" href="javasricpt:;" filecode="${annex.fileinfo.filecode }">下载</a><br/>
                    </c:forEach></dd>
                </dl>
            </c:if>


            <dl class="nowrap">
                <dt>内容：</dt>
                <dd>${object.msgcontent}</dd>
            </dl>


            <dl class="nowrap">
                <dt>快捷回复：</dt>
                <dd>
                    <textarea name="msgcontent" cols="100" rows="5"></textarea>
                </dd>
            </dl>
        </div>

        <div class="formBar">
            <ul>
                <li>
                    <div class="button">
                        <div class="buttonContent">
                            <s:submit value="快捷回复"/>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
    </s:form>
</div>

