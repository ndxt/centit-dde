<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>

<link href="<s:url value="/scripts/autocomplete/autocomplete.css"/>" type="text/css" rel="stylesheet">
<script language="javascript" src="<s:url value="/scripts/autocomplete/autocomplete.js"/>"
        type="text/javascript"></script>
<script language="javascript" src="<s:url value="/scripts/selectUser.js"/>" type="text/javascript"></script>
<script type="text/javascript">
    var list = [];
    <c:forEach var="userinfo" varStatus="status" items="${cp:ALLUSER('T')}">
    list[${status.index}] = { username: '<c:out value="${userinfo.username}"/>', loginname: '<c:out value="${userinfo.loginname}"/>', usercode: '<c:out value="${userinfo.usercode}"/>', pinyin: '<c:out value="${userinfo.usernamepinyin}"/>'  };
    </c:forEach>
    function selectUser(obj) {
        userInfo.choose(obj, {dataList: list, userName: $('#userName')});
    }
</script>

<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath }/sys/dataSync!syncAll.do"
          method="post">
        <div class="searchBar">
            <ul class="searchContent">
                <label>同步所有数据</label>
                <li><label>同步方式:</label>
                    <select name="s_SyncLocation" class="combox">
                        <%--<option value="">全部</option>--%>
                        <c:forEach var="s" items="${cp:DICTIONARY('SyncLocation') }">
                            <option value="${s.datavalue }">${s.datavalue}</option>
                        </c:forEach>
                    </select>
                </li>
                <li><label>同步策略:</label>
                    <select name="s_DSMode" class="combox">
                        <%--<option value="">全部</option>--%>
                        <c:forEach var="s" items="${cp:DICTIONARY('DSMode') }">
                            <option value="${s.datacode }">${s.datavalue}</option>
                        </c:forEach>
                    </select>
                </li>
                <li>
                    <label>最后更新时间:</label>
                    <input type="text" name="s_lastModDate" readonly="true" class="date" format="yyyy-MM-dd HH:mm:ss"
                           yearstart="-20" yearend="5"/>
                    <a class="inputDateButton" style="float: right;" href="javascript:;">选择</a>
                </li>
            </ul>
            <div class="subBar">
                <ul>
                    <li>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">同步</button>
                            </div>
                        </div>
                    </li>
                </ul>

            </div>
        </div>
    </form>


    <form onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath }/sys/dataSync!syncUser.do"
          method="post">
        <div class="searchBar">
            <ul class="searchContent">
                <label>同步人员数据</label>
                <li><label>同步用户:</label> <s:textfield onclick="selectUser(this);" id="userCode" name="s_usercode"
                                                      value="%{#parameters['s_usercode']}"/></li>
                <li><label>同步方式:</label>
                    <select name="s_SyncLocation" class="combox">
                        <%--<option value="">全部</option>--%>
                        <c:forEach var="s" items="${cp:DICTIONARY('SyncLocation') }">
                            <option value="${s.datavalue }">${s.datavalue}</option>
                        </c:forEach>
                    </select>
                </li>
            </ul>

            <div class="subBar">
                <ul>
                    <li>
                        <div class="buttonActive">
                            <div class="buttonContent">
                                <button type="submit">同步</button>
                            </div>
                        </div>
                    </li>
                </ul>

            </div>
        </div>
    </form>
</div>
    


