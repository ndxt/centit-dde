<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/sys/staffcertificate!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
	<input type="hidden" name="usercode" value="${usercode}" />
	<input type="hidden" name="id" value="${id}" />

		<div class="pageFormContent" layoutH="56">
		
			
			<p>
				<label><c:out value="证书编号" />：</label>
				 
				 
				<input name="certificateid" type="text" size="40" value="${object.certificateid }"/>
				
			</p>
			
			<p>
				<label><c:out value="证书名称" />：</label>
				 
				 
				<input name="certificatename" type="text"  size="40" value="${object.certificatename }"/>
				
			</p>
			
			<p>
				<label><c:out value="证书发放机构" />：</label>
				 
				 
				<input name="certificateorgan" type="text" size="40" value="${object.certificateorgan }"/>
				
			</p>
			
			<p>
				<label><c:out value="证书发放时间" />：</label>
				 
				 
				<input name="certificatetime" type="text"  size="40" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${object.certificatetime }"/>" class="date"/>
				<a class="inputDateButton" href="#">选择</a>
				
			</p>
			
			<p>
				<label><c:out value="证书描述" />：</label>
				 
				<textarea name="certificatedesc"  rows="3" cols="30">${object.certificatedesc }</textarea>
				
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
