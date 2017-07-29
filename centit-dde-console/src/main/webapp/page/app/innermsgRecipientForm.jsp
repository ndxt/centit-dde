<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/app/innermsgRecipient!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">

		<div class="pageFormContent" layoutH="56">
		
			
			<p>
				<label><c:out value="innermsgRecipient.msgcode" />：</label>
				 
				 
				<input name="msgcode" type="text" class="required" <c:if test="${!empty object.msgcode }">readonly="readonly"</c:if> size="40" value="${object.msgcode }" />
				
			</p>

			
			<p>
				<label><c:out value="innermsgRecipient.replymsgcode" />：</label>
				 
				 
				<input name="replymsgcode" type="text" <c:if test="${!empty innermsgRecipientForm.map.replymsgcode }">readonly="readonly"</c:if> size="40" value="${object.replymsgcode }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsgRecipient.receivetype" />：</label>
				 
				 
				<input name="receivetype" type="text" <c:if test="${!empty innermsgRecipientForm.map.receivetype }">readonly="readonly"</c:if> size="40" value="${object.receivetype }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsgRecipient.mailtype" />：</label>
				 
				 
				<input name="mailtype" type="text" <c:if test="${!empty innermsgRecipientForm.map.mailtype }">readonly="readonly"</c:if> size="40" value="${object.mailtype }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsgRecipient.msgstate" />：</label>
				 
				 
				<input name="msgstate" type="text" <c:if test="${!empty innermsgRecipientForm.map.msgstate }">readonly="readonly"</c:if> size="40" value="${object.msgstate }"/>
				
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

			<p>
				<label><c:out value="innermsgRecipient.receive" />：</label>
				 
				 
				<input name="receive" type="text" class="required" <c:if test="${!empty object.receive }">readonly="readonly"</c:if> size="40" value="${object.receive }" />
				
			</p>

			
			<p>
				<label><c:out value="innermsgRecipient.replymsgcode" />：</label>
				 
				 
				<input name="replymsgcode" type="text" <c:if test="${!empty innermsgRecipientForm.map.replymsgcode }">readonly="readonly"</c:if> size="40" value="${object.replymsgcode }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsgRecipient.receivetype" />：</label>
				 
				 
				<input name="receivetype" type="text" <c:if test="${!empty innermsgRecipientForm.map.receivetype }">readonly="readonly"</c:if> size="40" value="${object.receivetype }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsgRecipient.mailtype" />：</label>
				 
				 
				<input name="mailtype" type="text" <c:if test="${!empty innermsgRecipientForm.map.mailtype }">readonly="readonly"</c:if> size="40" value="${object.mailtype }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsgRecipient.msgstate" />：</label>
				 
				 
				<input name="msgstate" type="text" <c:if test="${!empty innermsgRecipientForm.map.msgstate }">readonly="readonly"</c:if> size="40" value="${object.msgstate }"/>
				
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

	<%-- </s:form> --%>
</div>
