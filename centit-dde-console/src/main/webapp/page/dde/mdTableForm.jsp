<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<form id="pagerForm" method="post" action="mdTable!list.do">
	<input type="hidden" name="pageNum" value="1" /> <input type="hidden" name="numPerPage" value="${pageDesc.pageSize}" />
</form>
<div class="pageContent">
	<form method="post" action="${pageContext.request.contextPath}/dde/mdTable!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input type="hidden" name="s_tbcode" value="${s_tbcode }" /> 
	
		<div class="pageFormContent" layoutH="60">
			<p>
				<label class="required">表代码：</label>
				<c:if test="${ not empty tbcode}">
					<input name="tbcode" size="30" readonly="true" class="required" maxlength="32" value="${tbcode }" />
				</c:if>
				<c:if test="${empty tbcode }">
					<input name="tbcode" maxlength="32" size="30" class="required alphanumeric" 
						maxlength="32" value="${tbcode }" alt="不超过32位的数字或字母" />
				</c:if>		
			</p>
			
			<p>
				<label>表名称：</label>
				<input name="tbname" maxlength="64" size="30" class="required" value="${tbname }" />
			</p>
			
			<p style="width:100%">
				<label>数据类别：</label>
				<input type="radio" name="tbstate" value="S" <c:if test="${tbstate=='S' || empty tbstate}">checked="checked"</c:if> />系统
				<input type="radio" name="tbstate" value="O" <c:if test="${tbstate=='O' }">checked="checked"</c:if> />业务
			</p>
			
			<p>
				<label>形式类别：</label>
					<input type="radio" name="tbtype" value="T" <c:if test="${tbtype=='T' || empty tbtype}">checked="checked"</c:if> />表
					<input type="radio" name="tbtype" value="V" <c:if test="${tbtype=='V' }">checked="checked"</c:if>/>视图

			</p>
			<div class="unit">
				<label>检验状态：</label>
				<select class="combox" name="checkState">
      				  <option value="0"  <c:if test="${checkState eq '0' || empty checkState}">selected=selected</c:if>>未检验</option>
      				  <option value="1"  <c:if test="${checkState eq '1'}">selected=selected</c:if>>检验一致</option>
      				  <option value="2"  <c:if test="${checkState eq '2'}">selected=selected</c:if>>检验不一致</option>
				</select>
			</div>
			<p>
				<label>是否是流程中的业务表：</label>
					<input type="radio" name="isInWorkflow" value="F" <c:if test="${isInWorkflow=='F' || empty isInWorkflow}">checked="checked"</c:if>/>否
					<input type="radio" name="isInWorkflow" value="T" <c:if test="${isInWorkflow=='T' }">checked="checked"</c:if>/>是
			</p>
			<div class="divider"></div>
			<p style="width:100%">
				<label>表描述：</label>
				<s:textarea rows="5" cols="50" maxlength="256" name="tbdesc" ></s:textarea>
				
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
