<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
		
<div class="pageContent">
	<form method="post" action="dictionary!saveDetail.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="60">
			<input type="hidden" name="datadictionary.id.catalogcode" value="${datadictionary.id.catalogcode }"/>
			<c:if test="${not empty datadictionary.id.datacode }">
				<input type="hidden" name="datadictionary.id.datacode"
					value="${datadictionary.id.datacode }" />
			</c:if>
			<p>
				<label class="required"><c:out value="${fdesc[0]}" />：</label>
				<c:if test="${not empty datadictionary.id.datacode }">
				    <label>${datadictionary.id.datacode }</label>
					<%-- <input type="text" name="datadictionary.id.datacode" readonly="true" value="${datadictionary.id.datacode }" class="required" /> --%>
				</c:if>
				<c:if test="${empty datadictionary.id.datacode }">
					<input type="text" name="datadictionary.id.datacode" 
						class="required alphanumeric" maxlength="16" alt="不超过16位字母或数字" />
				</c:if>		
			</p>
			
			<p>
				<label><c:out value="${fdesc[1]}" />：</label>
				<input type="text" name="datadictionary.extracode" value="${datadictionary.extracode}" 
					maxlength="8" class="alphanumeric" alt="不超过8位字母或数字" />	
			</p>
			
			<p>
				<label><c:out value="${fdesc[2]}" />：</label>
				<input type="text" name="datadictionary.extracode2" value="${datadictionary.extracode2}" 
					maxlength="8" class="alphanumeric" alt="不超过8位字母或数字" />	
			</p>
			
			<p>
				<label><c:out value="${fdesc[3]}" />：</label>
				<input type="text" name="datadictionary.datatag" value="${datadictionary.datatag}" 
					maxlength="1" class="lettersonly" alt="1位字母" />	
			</p>
			
			<p>
				<label><c:out value="${fdesc[4]}" />：</label>
				<input type="text" name="datadictionary.datavalue" value="${datadictionary.datavalue}" 
					maxlength="256" alt="长度不超过256位" />	
			</p>
			
			<p>
				<label><c:out value="${fdesc[5]}" />：</label>
				<input type="text" name="datadictionary.datastyle" value='<c:choose><c:when test="${empty datadictionary.datastyle}">U</c:when><c:otherwise>${datadictionary.datastyle }</c:otherwise></c:choose>'
					maxlength="1" class="lettersonly" alt="1位字母" />	
			</p>
			<div class="divider"></div>
			<p style="width:100%">
				<label><c:out value="${fdesc[6]}" />：</label>
				<textarea rows="5" cols="50" maxlength="256" name="datadictionary.datadesc">${datadictionary.datadesc}</textarea>
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