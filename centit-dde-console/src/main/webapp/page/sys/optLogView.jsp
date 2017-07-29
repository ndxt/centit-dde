<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">
	<div class="pageFormContent">

		<p>
			<label>日志序列号：</label> <s:property value="%{logid}" />
		</p>
		<p>
			<label>操作人员：</label> <s:property value="%{usercode}" /> [<c:out value="${cp:MAPVALUE('usercode', object.usercode)}"/>]
		</p>
		<p>
			<label>操作时间：</label> <fmt:formatDate value="${object.opttime }" pattern="yyyy-MM-dd HH:mm:ss" />
		</p>
		<p>
			<label>项目模块：</label> 
				<c:forEach var="opt" items="${optIds }">
					<c:if test="${opt eq object.optid }">${cp:MAPVALUE('optid',opt)}</c:if>
				</c:forEach>
		</p>
		<p>
			<label>操作方法：</label> <s:property value="%{optmethod}" />
		</p>
		<div class="divider"></div>
		
		<dl class="nowrap">
			<dt>操作内容：</dt>
			<c:out value="${optcontent}" />
			<%-- <dd><s:textarea value="%{optcontent}" disabled="true" cols="80" rows="2" /></dd> --%>
		</dl>
		<dl class="nowrap">
			<dt>更改前原值：</dt>
			<c:out value="${oldvalue}" />
			<%-- <dd><s:textarea value="%{oldvalue}" disabled="true" cols="80" rows="2" /></dd> --%>
		</dl>
		
	</div>
	
	<div class="formBar">
		<ul>
			<li>
				<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
			</li>
		</ul>
	</div>
</div>

