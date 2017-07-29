<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<form id="pagerForm" method="post" action="mdRelation!list.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
</form>
<div class="pageContent">
	<form method="post" action="${pageContext.request.contextPath}/dde/mdRelation!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input type="hidden" name="pelcode" value="${pelcode }" /> 
	
		<div class="pageFormContent" layoutH="60">
			<p>
				<label class="required">关联代码：</label>
				<c:if test="${ not empty relcode}">
					<input name="relcode" size="30" readonly="true" class="required" maxlength="32" value="${relcode }" />
				</c:if>
				<c:if test="${empty relcode }">
					<input name="relcode" maxlength="32" size="30" class="required alphanumeric" 
						value="${relcode }" alt="不超过32位的数字或字母" />
				</c:if>		
			</p>
			
			<p>
				<label>关联名称：</label>
				<input name="relname" maxlength="64" size="30" class="required" value="${relname }" />
			</p>
			<p>
				<label>关联表一：</label>

				<input name="ptabcode" maxlength="32" size="30" class="required alphanumeric" 
				value="${ptabcode }" alt="不超过32位的数字或字母" readonly="true"/>
			</p>
			<p>
				<label>关联表二：</label>
				<s:select name="ctabcode" id= "ctabcode"
						list="tabcodeList"
						listKey="key"
						listValue="value"
					></s:select>
<%-- 				<input name="ctabcode" maxlength="32" size="30" class="required alphanumeric" 
				value="${ctabcode }" alt="不超过32位的数字或字母" /> --%>
			</p>
			
			<p>
				<label>状态：</label>
					<input type="radio" name="relstate" value="T" <c:if test="${relstate=='T' || empty relstate}">checked="checked"</c:if> />可用
					<input type="radio" name="relstate" value="F" <c:if test="${relstate=='F' }">checked="checked"</c:if>/>不可用

			</p>

			<div class="divider"></div>
			<p style="width:100%">
				<label>关联说明：</label>
				<s:textarea rows="5" cols="50" maxlength="256" name="reldesc" ></s:textarea>
				
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
