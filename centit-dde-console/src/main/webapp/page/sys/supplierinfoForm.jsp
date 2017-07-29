<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/sys/supplierinfo!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input type="hidden" name="usercode" value="${usercode}" />
	    <input type="hidden" name="id" value="${id}" />
		<div class="pageFormContent" layoutH="56">
		
			
			<p>
				<label><c:out value="注册资本" />：</label>
				 
				 
				<input name="registeredcapital" type="text"  size="40" value="${object.registeredcapital }"/>
				
			</p>
			
			<p>
				<label><c:out value="公司规模" />：</label>
				 
				<select class="combox" name="companyscale">
					<option value="0" <c:if test="${object.companyscale eq 0}">selected=selected</c:if>>小于50人</option>
					<option value="1" <c:if test="${object.companyscale eq 1}">selected=selected</c:if>>50-100人</option>
					<option value="2" <c:if test="${object.companyscale eq 2}">selected=selected</c:if>>100-500人</option>
					<option value="3" <c:if test="${object.companyscale eq 3}">selected=selected</c:if>>500-1000人</option>
					<option value="4" <c:if test="${object.companyscale eq 4}">selected=selected</c:if>>1000-5000人</option>
					<option value="5" <c:if test="${object.companyscale eq 5}">selected=selected</c:if>>大于5000人</option>
				</select>  
				
			</p>
			
			<p>
				<label><c:out value="业务范围" />：</label>
				 
				 
				<input name="businessscope" type="text"  size="40" value="${object.businessscope }"/>
				
			</p>
			
			<p>
				<label><c:out value="经验和成功案例" />：</label>
				 
				 
				<input name="successcase" type="text"  size="40" value="${object.successcase }"/>
				
			</p>
			
			<p>
			
			
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
