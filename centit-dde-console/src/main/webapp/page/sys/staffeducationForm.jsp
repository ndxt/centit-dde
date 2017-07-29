<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/sys/staffeducation!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
	<input type="hidden" name="usercode" value="${usercode}" />
	<input type="hidden" name="id" value="${id}" />
		<div class="pageFormContent" layoutH="56">
		
			
			
			<p>
				<label><c:out value="接受教育开始时间" />：</label>
				 
				 
				<input name="educatebegin" type="text"  size="40"  value="<fmt:formatDate pattern="yyyy-MM-dd" value="${object.educatebegin }"/>" class="date"/>
				<a class="inputDateButton" href="#">选择</a>
				
			</p>
			
			<p>
				<label><c:out value="接受教育结束时间" />：</label>
				 
				 
				<input name="educateend" type="text"  size="40" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${object.educateend }"/>" class="date"/>
				<a class="inputDateButton" href="#">选择</a>
				
			</p>
			
			<p>
				<label><c:out value="所在学校名称" />：</label>
				 
				 
				<input name="schoolname" type="text"  size="40" value="${object.schoolname }"/>
				
			</p>
			
			<p>
				<label><c:out value="专业" />：</label>
				 
				 
				<input name="speciality" type="text"  size="40" value="${object.speciality }"/>
				
			</p>
			
			<p>
				<label><c:out value="学历" />：</label>
				 
				<select class="combox" name="edubackground">
					<option value="1" <c:if test="${object.edubackground eq 1}">selected=selected</c:if>>小学</option>
					<option value="2" <c:if test="${object.edubackground eq 2}">selected=selected</c:if>>初中</option>
					<option value="3" <c:if test="${object.edubackground eq 3}">selected=selected</c:if>>高中</option>
					<option value="4" <c:if test="${object.edubackground eq 4}">selected=selected</c:if>>大专</option>
					<option value="5" <c:if test="${object.edubackground eq 5}">selected=selected</c:if>>本科</option>
					<option value="6" <c:if test="${object.edubackground eq 6}">selected=selected</c:if>>硕士</option>
					<option value="7" <c:if test="${object.edubackground eq 7}">selected=selected</c:if>>博士</option>
				</select> 				
			</p>
			
			<p>
				<label><c:out value="证明人" />：</label>
				 
				 
				<input name="certifier" type="text"  size="40" value="${object.certifier }"/>
				
			</p>
			
			<p>
				<label><c:out value="专业描述" />：</label>
			
				<textarea name="specialitydesc"  rows="3" cols="30">${object.specialitydesc }</textarea>
				
			</p>
			<br><br><br><br><br>
						
			<p>
				<label><c:out value="是否是海外学习经历" />：</label>
				
				<input type="radio" name="isabroad" value="2" <c:if test="${object.isabroad eq 2}">checked=true</c:if>/>是
				<input type="radio" name="isabroad" value="1" <c:if test="${object.isabroad eq 1 ||object.isabroad eq null}">checked=true</c:if> />否 
				
				
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
