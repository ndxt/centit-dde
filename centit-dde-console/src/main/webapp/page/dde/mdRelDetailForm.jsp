<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<form id="pagerForm" method="post" action="mdRelDetail!list.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
	<input type="hidden" name="ptabcode" value="${mdRelation.ptabcode}" />
	<input type="hidden" name="ctabcode" value="${mdRelation.ctabcode}" />
</form>
<div class="pageContent">
	<form method="post" action="${pageContext.request.contextPath}/dde/mdRelDetail!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="60">
			<p>
				<label class="required">关联代码：</label>
				<c:if test="${ not empty relcode}">
					<input name="relcode" size="30" readonly="true" class="required" maxlength="32" value="${relcode}" />
				</c:if>
				<c:if test="${empty relcode}">
					<input name="relcode" maxlength="32" size="30" class="required alphanumeric" 
						value="${relcode}" alt="不超过32位的数字或字母" />
				</c:if>		
			</p>
			

			<p>
				<label>关联表一字段代码：</label>
					<s:select name="pcolcode" id= "pcolcode" disabled="true"
						list="pcodeList"
						listKey="key"
						listValue="value"
					></s:select>
			</p>
			<p>
				<label>关联表一字段代码：</label>
				<s:select name="ccolcode" id= "ccolcode"
						list="ccodeList"
						listKey="key"
						listValue="value"
					></s:select>
<%-- 				<input name="ctabcode" maxlength="32" size="30" class="required alphanumeric" 
				value="${ctabcode }" alt="不超过32位的数字或字母" /> --%>
			</p>
			
		</div>
		
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
