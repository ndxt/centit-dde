<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>


<div class="pageContent">
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

    <div class="pageFormContent" style="height: 200px;">
        <p class="ctitle">消息接收详细信息</p>

        <p>
            <label>标题：</label> ${object.innermsg.msgtitle }
        </p>

        <p>
            <label>发送人：</label> ${cp:MAPVALUE("usercode", object.innermsg.sender)}
        </p>

        <p>
            <label>发送时间：</label>
            <fmt:formatDate value="${object.innermsg.senddate }" pattern="yyyy-MM-dd HH:mm:ss"/>
        </p>

        <div class="divider"></div>

        <dl class="nowrap">
            <dt>内容：</dt>
            <dd>
                <textarea disabled="true" cols="80" rows="2">${object.innermsg.msgcontent}</textarea>
            </dd>
        </dl>

        <c:if test="${not empty object.innermsg.msgannexs }">
            <dl class="nowrap">
                <dt>附件：</dt>
                <dd>
                    <c:forEach var="annex" items="${object.innermsg.msgannexs }">
                        ${annex.fileinfo.filename } <fmt:formatNumber value="${annex.fileinfo.filesize / 1024 / 1024 }"
                                                                      type="number" pattern="#.##"/> MB <a
                            target="download" href="javasricpt:;" filecode="${annex.fileinfo.filecode }">下载</a><br/>
                    </c:forEach>
                </dd>
            </dl>
        </c:if>

    </div>
</div>
