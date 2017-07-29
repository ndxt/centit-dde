<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>
<div class="pageHeader">
	<form id="pagerForm" onsubmit="return navTabSearch(this);" action="${pageContext.request.contextPath}/dde/mdTable.do" method="post">
        <div class="pageFormContent" style="height: 36px;">
        <p>
            <label>表代码：</label> <input type="text" size="30" value="${mdTable.tbcode}" readonly="readonly"/>
        </p>

        <p>
            <label>表名称：</label> <input type="text" size="30" value="${mdTable.tbname}" readonly="readonly"/>
        </p>
    </div>
	</form>
</div>
	<div class="tabs tabsExtra" currentIndex="${param['currentIndex']}">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li><a href="${pageContext.request.contextPath }/dde/mdTable!viewcolumn.do?tbcode=${object.tbcode }&amp;ec_p=&amp;ec_crd=" target="ajax" rel="jbsxBox"><span>字段信息</span></a></li>
					<li><a href="${pageContext.request.contextPath }/dde/mdTable!viewrelation.do?tbcode=${object.tbcode }&amp;ec_p=&amp;ec_crd=" target="ajax" rel="jbsxBox"><span>关联信息</span></a></li>
				</ul>
			</div>
		</div>
		<div class="tabsContentAjax">
		   <c:if test="${param['currentIndex'] eq 0 || param['currentIndex'] eq null}">
			<div class="unitBox" id="jbsxBox">
				<%@ include file="/page/dde/mdTableView_tbcolumn.jsp"%>
			</div>
		   </c:if>
		   <c:if test="${param['currentIndex'] eq 1}">
			<div class="unitBox" id="jbsxBox">
				<%@ include file="/page/dde/mdRelationList.jsp"%>
			</div>
		   </c:if>
		</div>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>
