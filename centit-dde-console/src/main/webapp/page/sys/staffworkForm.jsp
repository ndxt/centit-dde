<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/sys/staffwork!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
	<input type="hidden" name="usercode" value="${usercode}" />
	<input type="hidden" name="id" value="${id}" />

		<div class="pageFormContent" layoutH="56">
		
			
			
			<p>
				<label><c:out value="工作开始时间" />：</label>
				 
				 
				<input name="workbegin" type="text"  size="40" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${object.workbegin }"/>"/>
				<a class="inputDateButton" href="#">选择</a>
				
			</p>
			
			<p>
				<label><c:out value="工作结束时间" />：</label>
				 
				 
				<input name="workend" type="text"  size="40" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${object.workend }"/>"/>
				<a class="inputDateButton" href="#">选择</a>
				
			</p>
			
			<p>
				<label><c:out value="公司名称" />：</label>
				 
				 
				<input name="company" type="text"  size="40" value="${object.company }"/>
				
			</p>
			
			<p>
				<label><c:out value="所属行业" />：</label>
				 
				 
				<input name="industry" type="text"  size="40" value="${object.industry }"/>
				
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
				<label><c:out value="公司性质" />：</label>
				 
				<select class="combox" name="companynature">
					<option value="1" <c:if test="${object.companynature eq 1}">selected=selected</c:if>>国企</option>
					<option value="2" <c:if test="${object.companynature eq 2}">selected=selected</c:if>>民企</option>
					<option value="3" <c:if test="${object.companynature eq 3}">selected=selected</c:if>>外企</option>
				</select> 
				
			</p>
			
			<p>
				<label><c:out value="部门" />：</label>
				 
				 
				<input name="department" type="text"  size="40" value="${object.department }"/>
				
			</p>
			
			<p>
				<label><c:out value="职位" />：</label>
				 
				 
				<input name="position" type="text"  size="40" value="${object.position }"/>
				
			</p>
			
			<p>
				<label><c:out value="工作描述" />：</label>
				 
				<textarea name="workdesc"  rows="3" cols="30">${object.workdesc }</textarea>
				
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
