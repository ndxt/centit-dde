<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>


<div class="pageContent">
    <s:form action="/dde/databaseInfo!save.do" class="pageForm required-validate"
            onsubmit="return validateCallback(this, dialogAjaxDone);">
        <input type="hidden" name="createTime" value="${object.createTime }"/>

        <div class="pageFormContent" layoutH="58">
            <div class="unit">
                <label>数据库标识：</label>
                <input name="databaseName" class="" type="text" size="40" value="${object.databaseName }"
                       readonly="readonly"/>
            </div>
            <div class="unit">
                <label>数据库名：</label>
                <input name="dataDesc" type="text" size="40" value="${object.dataDesc }"/>
            </div>
            <div class="unit">
                <label>数据库类型：</label>
                <select class="combox databaseType" name="databaseType" id="databaseType">
                    <option value="0" selected=selected>请选择数据库</option>
                    <option value="1" <c:if test="${object.databaseType eq '1'}">selected=selected</c:if>>SQLSERVER
                    </option>
                    <option value="2" <c:if test="${object.databaseType eq '2'}">selected=selected</c:if>>ORACLE
                    </option>
                    <option value="3" <c:if test="${object.databaseType eq '3'}">selected=selected</c:if>>DB2</option>
                    <option value="4" <c:if test="${object.databaseType eq '4'}">selected=selected</c:if>>ACCESS
                    </option>
                    <option value="5" <c:if test="${object.databaseType eq '5'}">selected=selected</c:if>>MYSQL</option>
                </select>
            </div>
            <div class="unit">
                <label>业务系统：</label>
                <select name="sourceOsId" id="sel_sourceOsId">
                    <c:forEach var="osinfo" items="${osinfoList }">
                        <option value="${osinfo.osId }"
                                <c:if test="${osinfo.osId eq object.sourceOsId }">selected="selected"</c:if> >${osinfo.osId }</option>
                    </c:forEach>
                </select>
            </div>
            <div class="unit">
                <label>主机号：端口号：</label>
                <input name="hostPort" type="text" size="40" value="${object.hostPort }" id="hostPort"
                       class="focusout"/>
            </div>
            <div class="unit">
                <label>数据库名：</label>
                <input name="databaseNames" type="text" size="40" value="${object.databaseNames }" id="databaseNames"
                       class="focusout"/>
            </div>
            <div class="unit">
                <label>用户名：</label>
                <input name="username" type="text" size="40" value="${object.username }"/>
            </div>
            <div class="unit">
                <label>密码：</label>
                <input name="password" type="password" size="40" value="${password }"/>
            </div>
            <div class="unit">
                <label>数据库连接url：</label>
                <input name="databaseUrl" type="text" size="40" value="${object.databaseUrl }" id="databaseUrl" readonly="readonly" />
                <!-- <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="button" id="generate">自动生成</button>
                            </div>
                </div> -->
            </div>
                <%-- <div class="unit">
                    <label>创建时间：</label>

                    <a class="inputDateButton" href="#">选择</a>

                </div> --%>
                <%-- <div class="unit">
                    <label>创建人员：</label>
                    <input name="created" type="text"  size="40" value="${object.created }"/>
                </div> --%>

        </div>

        <div class="formBar">
            <ul>
                <li>
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">保存</button>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="button">
                        <div class="buttonContent">
                            <button type="button" class="close">取消</button>
                        </div>
                    </div>
                </li>
            </ul>
        </div>

    </s:form>
</div>
<script type="text/javascript">
    $(function () {
        $(".focusout").focusout(function () {
            var hostPort = $.trim($("#hostPort").val());
            if(!/:\d{1,5}$/.test(hostPort)) {
                DWZ.ajaxDone({
                    statusCode: DWZ.statusCode.error,
                    message: '主机和端口号规则不正确'
                });

                return;
            }


            var databaseNames = $.trim($("#databaseNames").val());
            var databaseType = $.trim($("#databaseType").val());
            var databesrurl = null;
            if (databaseType == '1') {
                databesrurl = "jdbc:sqlserver://" + hostPort + ";databaseName=" + databaseNames;
            }
            if (databaseType == '2') {
                databesrurl = "jdbc:oracle:thin:@" + hostPort + ":" + databaseNames;
            }
            if (databaseType == '3') {
                databesrurl = "jdbc:db2://" + hostPort + "/" + databaseNames;
            }
            if (databaseType == '5') {
                databesrurl = "jdbc:mysql://" + hostPort + "/" + databaseNames;
            }
            $("#databaseUrl").val(databesrurl);
        });
        $('#databaseUrl').focusout(function () {
            var dburl = $.trim($("#databaseUrl").val());

            var split = dburl.split('@');
            if (2 != split.length) {
                return;
            }

            split = split[1].split(':');
            if (3 != split.length) {
                return;
            }

            $('#hostPort').val(split[0] + ':' + split[1]);
            $('#databaseNames').val(split[2]);
        });
    });
</script>
