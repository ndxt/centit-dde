<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/sys/userDefGroup!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">

		<div class="pageFormContent" layoutH="56">
		
			
			<p>
				<label><c:out value="分组编号" />：</label>
				 
				 
				<input name="groupid" type="text" class="required" <c:if test="${!empty object.groupid }">readonly="readonly"</c:if> size="40" value="${object.groupid }" />
				
			</p>

			
			<p>
				<label><c:out value="用户代码" />：</label>
				 
				 
				<input name="usercode" type="text" readonly="readonly"size="40" value="${object.usercode }"/>
				
			</p>
			
			<p>
				<label><c:out value="分组名称" />：</label>
				 
				 
				<input name="groupname" type="text" <c:if test="${!empty userdefgroupForm.map.groupname }">readonly="readonly"</c:if> size="40" value="${object.groupname }"/>
				
			</p>
			
			<p>
				<label><c:out value="分组描述" />：</label>
				 
				 
				<input name="groupdesc" type="text" <c:if test="${!empty userdefgroupForm.map.groupdesc }">readonly="readonly"</c:if> size="40" value="${object.groupdesc }"/>
				
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
