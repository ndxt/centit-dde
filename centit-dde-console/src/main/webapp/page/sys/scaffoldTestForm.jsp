<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/1/scaffoldTest!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">

		<div class="pageFormContent" layoutH="56">
		
			
			<p>
				<label><c:out value="scaffoldTest.logid" />：</label>
				 
				 
				<input name="logid" type="text" class="required" <c:if test="${!empty object.logid }">readonly="readonly"</c:if> size="40" value="${object.logid }" />
				
			</p>

			
			<p>
				<label><c:out value="scaffoldTest.loglevel" />：</label>
				 
				 
				<input name="loglevel" type="text" <c:if test="${!empty scaffoldTestForm.map.loglevel }">readonly="readonly"</c:if> size="40" value="${object.loglevel }"/>
				
			</p>
			
			<p>
				<label><c:out value="scaffoldTest.usercode" />：</label>
				 
				 
				<input name="usercode" type="text" <c:if test="${!empty scaffoldTestForm.map.usercode }">readonly="readonly"</c:if> size="40" value="${object.usercode }"/>
				
			</p>
			
			<p>
				<label><c:out value="scaffoldTest.opttime" />：</label>
				 
				 
				<input name="opttime" type="text" <c:if test="${!empty scaffoldTestForm.map.opttime }">readonly="readonly"</c:if> size="40" value="${object.opttime }"/>
				
			</p>
			
			<p>
				<label><c:out value="scaffoldTest.optid" />：</label>
				 
				 
				<input name="optid" type="text" <c:if test="${!empty scaffoldTestForm.map.optid }">readonly="readonly"</c:if> size="40" value="${object.optid }"/>
				
			</p>
			
			<p>
				<label><c:out value="scaffoldTest.optcode" />：</label>
				 
				 
				<input name="optcode" type="text" <c:if test="${!empty scaffoldTestForm.map.optcode }">readonly="readonly"</c:if> size="40" value="${object.optcode }"/>
				
			</p>
			
			<p>
				<label><c:out value="scaffoldTest.optcontent" />：</label>
				 
				 
				<input name="optcontent" type="text" <c:if test="${!empty scaffoldTestForm.map.optcontent }">readonly="readonly"</c:if> size="40" value="${object.optcontent }"/>
				
			</p>
			
			<p>
				<label><c:out value="scaffoldTest.oldvalue" />：</label>
				 
				 
				<input name="oldvalue" type="text" <c:if test="${!empty scaffoldTestForm.map.oldvalue }">readonly="readonly"</c:if> size="40" value="${object.oldvalue }"/>
				
			</p>
			
			
			
			
			<div class="divider"></div>
			<div>
				<table class="list nowrap itemDetail" addButton="新建从表1条目" width="100%">
					<thead>
						<tr>
						
						
							
								
							
							
								 
									<th type="text" name="scaffoldTestSub.loglevel" fieldClass="required"> <c:out value="scaffoldTestSub.loglevel" /> </th>
								
							
								 
									<th type="text" name="scaffoldTestSub.usercode" fieldClass="required"> <c:out value="scaffoldTestSub.usercode" /> </th>
								
							
								 
									<th type="text" name="scaffoldTestSub.opttime" fieldClass="required"> <c:out value="scaffoldTestSub.opttime" /> </th>
								
							
								 
									<th type="text" name="scaffoldTestSub.optid" fieldClass="required"> <c:out value="scaffoldTestSub.optid" /> </th>
								
							
								 
									<th type="text" name="scaffoldTestSub.optcode" fieldClass="required"> <c:out value="scaffoldTestSub.optcode" /> </th>
								
							
								 
									<th type="text" name="scaffoldTestSub.optcontent" fieldClass="required"> <c:out value="scaffoldTestSub.optcontent" /> </th>
								
							
								 
									<th type="text" name="scaffoldTestSub.oldvalue" fieldClass="required"> <c:out value="scaffoldTestSub.oldvalue" /> </th>
								
							
						
							<th type="del" width="60">操作</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
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

