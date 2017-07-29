<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/sys/groupuser!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">

		<div class="pageFormContent" layoutH="56">
		
			
			<p>
				<label><c:out value="groupuser.no" />：</label>
				 
				 
				<input name="no" type="text" class="required" <c:if test="${!empty object.no }">readonly="readonly"</c:if> size="40" value="${object.no }" />
				
			</p>

			
			<p>
				<label><c:out value="groupuser.groupid" />：</label>
				 
				 
				<input name="groupid" type="text" <c:if test="${!empty groupuserForm.map.groupid }">readonly="readonly"</c:if> size="40" value="${object.groupid }"/>
				
			</p>
			
			<p>
				<label><c:out value="groupuser.inUsercode" />：</label>
				 
				 
				<input name="inUsercode" type="text" <c:if test="${!empty groupuserForm.map.inUsercode }">readonly="readonly"</c:if> size="40" value="${object.inUsercode }"/>
				
			</p>
			
			
			
			
		</div>


		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">保存</button>
						</div>
					</div></li>
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
