<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>

<div class="pageContent">
    <div class="pageFormContent" style="height: 200px;">
        <p class="ctitle">
            <c:choose>
                <c:when test="${MsgType_A eq object.msgtype }">
                    公告详细信息
                </c:when>
                <c:when test="${MsgType_P eq object.msgtype }">
                    消息发送详细信息
                </c:when>
            </c:choose>
        </p>

        <p>
            <label>标题：</label> ${object.msgtitle }
        </p>

        <p>
            <c:choose>
                <c:when test="${MsgType_A eq object.msgtype }">
                    <label>发件人：</label> ${cp:MAPVALUE("usercode", object.sender)}
                </c:when>
                <c:when test="${MsgType_P eq object.msgtype }">
                    <label>接收人：</label> ${object.receivename }
                </c:when>
            </c:choose>
        </p>

        <p>
            <label>发送时间：</label>
            <fmt:formatDate value="${object.senddate }" pattern="yyyy-MM-dd HH:mm:ss"/>
        </p>

        <div class="divider"></div>

        <dl class="nowrap">
            <dt>内容：</dt>
            <dd>
                <textarea disabled="true" cols="80" rows="2">${object.msgcontent}</textarea>
            </dd>
        </dl>

        <c:if test="${not empty object.msgannexs}">
        <dl class="nowrap">
            <dt>附件：</dt>
            <dd>
                <c:forEach var="annex" items="${object.msgannexs }">
                    ${annex.fileinfo.filename }
                    <fmt:formatNumber value="${annex.fileinfo.filesize / 1024 / 1024 }"
                                      type="number" pattern="#.##"/> MB
                <a target="download" href="javasricpt:;" filecode="${annex.fileinfo.filecode }">下载</a><br/>
                    </c:forEach>
                </dd>
            </dl>
        </c:if>
    </div>

    <div class="formBar">
                <ul>
                    <li>
                        <div class="button">
                            <div class="buttonContent">
                                <button type="button" class="close">取消</button>
                            </div>
                        </div>
                    </li>
                </ul>
    </div>
</div>

