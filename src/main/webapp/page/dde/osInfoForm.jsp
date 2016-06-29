<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/dde/osInfo!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">

		<div class="pageFormContent" layoutH="56">
		
			
			<div class="unit">
				<label>业务系统ID：</label>
				 
				 
				<input name="osId" type="text" class="required" size="40" value="${object.osId }" />
				
			</div>

			
			<div class="unit">
				<label>业务系统名称：</label>
				 
				 
				<input name="osName" type="text" class="required" size="40" value="${object.osName }" maxlength="200"/>
				
			</div>
			
			<div class="unit">
				<label>是否提供接口：</label>
				 
				 
				<input name="hasInterface" type="checkbox" <c:if test="${'T' eq object.hasInterface }">checked="checked"</c:if> value="T"/>
				
			</div>
			
			
			<div class="unit">
				<label>接口用户名：</label>
				 
				 
				<input name="osUser" type="text" size="40" value="${object.osUser }" maxlength="20"/>
				
			</div>
			
			<div class="unit">
				<label>接口密码加密：</label>
				 
				 
				<input name="osPassword" type="text" size="40" value="${object.osPassword }" maxlength="100"/>
				
			</div>
			
			
			<div class="unit">
				<label>业务系统接口url：</label>
				 
				 
				<textarea name="interfaceUrl" type="text" cols="50" rows="5" maxlength="200">${object.interfaceUrl }</textarea>
				
			</div>
			
			
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
