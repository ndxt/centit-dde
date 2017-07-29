<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/app/publicinfolog!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">

		<div class="pageFormContent" layoutH="56">
		
			
			<p>
				<label><c:out value="publicinfolog.usercode" />：</label>
				 
				 
				<input name="usercode" type="text" class="required" <c:if test="${!empty object.usercode }">readonly="readonly"</c:if> size="40" value="${object.usercode }" />
				
			</p>

			
			<p>
				<label><c:out value="publicinfolog.operation" />：</label>
				 
				 
				<input name="operation" type="text" <c:if test="${!empty publicinfologForm.map.operation }">readonly="readonly"</c:if> size="40" value="${object.operation }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfolog.data1" />：</label>
				 
				 
				<input name="data1" type="text" <c:if test="${!empty publicinfologForm.map.data1 }">readonly="readonly"</c:if> size="40" value="${object.data1 }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfolog.data2" />：</label>
				 
				 
				<input name="data2" type="text" <c:if test="${!empty publicinfologForm.map.data2 }">readonly="readonly"</c:if> size="40" value="${object.data2 }"/>
				
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
				<label><c:out value="publicinfolog.infocode" />：</label>
				 
				 
				<input name="infocode" type="text" class="required" <c:if test="${!empty object.infocode }">readonly="readonly"</c:if> size="40" value="${object.infocode }" />
				
			</p>

			
			<p>
				<label><c:out value="publicinfolog.operation" />：</label>
				 
				 
				<input name="operation" type="text" <c:if test="${!empty publicinfologForm.map.operation }">readonly="readonly"</c:if> size="40" value="${object.operation }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfolog.data1" />：</label>
				 
				 
				<input name="data1" type="text" <c:if test="${!empty publicinfologForm.map.data1 }">readonly="readonly"</c:if> size="40" value="${object.data1 }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfolog.data2" />：</label>
				 
				 
				<input name="data2" type="text" <c:if test="${!empty publicinfologForm.map.data2 }">readonly="readonly"</c:if> size="40" value="${object.data2 }"/>
				
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

<%-- 	</s:form> --%>
</div>
