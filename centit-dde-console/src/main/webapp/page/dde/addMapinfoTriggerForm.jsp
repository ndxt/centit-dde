<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<form action="${pageContext.request.contextPath}/dde/mapInfoTrigger!saveTrigger.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<div class="pageFormContent" layoutH="56">
		    <input type="hidden" name="mapInfoId" value="${param.mapInfoId }"/>
		    <input type="hidden" name="triggerId" value="${object.triggerId }"/>		
			<%-- <p>
				<label>交换编号：</label>	 
				<input name="mapInfoId" readonly="readonly" type="text" size="40" value="${param.mapInfoId }" />
			</p>
			 --%>
			<%-- <p>
				<label>触发器ID：</label>
				<input name="triggerId"  type="text"  size="40" value="${object.triggerId }"  class="digits"/>				
			</p>	 --%>		
			
			<p>
				<label>执行对象：</label>
				<select class="combox" name="triggerDatabase">
					<option value="S" <c:if test="${object.triggerDatabase eq 'S'}">selected=selected</c:if>>数据源</option>
					<option value="D" <c:if test="${object.triggerDatabase eq 'D'}">selected=selected</c:if>>数据目标</option>
				</select>			
			</p>			
			<p>
				<label>触发类别：</label>
				<select class="combox" name="triggerType">
					<option value="L" <c:if test="${object.triggerType eq 'L'}">selected=selected</c:if>>行触发器</option>
					<option value="T" <c:if test="${object.triggerType eq 'T'}">selected=selected</c:if>>表触发器</option>
				</select>			 			
			</p>
			
			<p>
				<label>触发时机：</label>
				<select class="combox" name="triggerTime">
					<option value="B" <c:if test="${object.triggerTime eq 'B'}">selected=selected</c:if>>交换前</option>
					<option value="A" <c:if test="${object.triggerTime eq 'A'}">selected=selected</c:if>>交换后</option>
					<option value="E" <c:if test="${object.triggerTime eq 'E'}">selected=selected</c:if>>交换失败后</option>
				</select>			 			
			</p>
			
			<p>
				<label>触发器顺序：</label>
				<input name="tiggerOrder" type="text"  size="40" value="${object.tiggerOrder }" class="digits"/>	
			</p>
			<p>
				<label>是否是存储过程：</label>
				<select class="combox" name="isprocedure">
					<option value="1" <c:if test="${object.isprocedure eq '1'}">selected=selected</c:if>>是</option>
					<option value="2" <c:if test="${object.isprocedure eq '2'}">selected=selected</c:if>>否</option>
				</select>			 			
			</p>			
			<p>
				<label>执行语句：</label>
				<textarea name="triggerSql" rows="5" cols="30">${object.triggerSql }</textarea>				 
				<%-- <input name="triggerSql" type="text"  size="40" value="${object.triggerSql }"/>	 --%>		
			</p>
			<br><br><br><br><br><br><br><br>
			<p>
				<label>执行语句说明：</label>	
				<textarea name="triggerDesc" rows="3" cols="30">${object.triggerDesc }</textarea>			 
				<%-- <input name="triggerDesc" type="text" size="40" value="${object.triggerDesc }"/>	 --%>		
			</p>
			<br><br><br><br><br>
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

	</form>
</div>
