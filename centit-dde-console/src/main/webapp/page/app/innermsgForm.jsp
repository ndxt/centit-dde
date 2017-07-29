<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/app/innermsg!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">

		<div class="pageFormContent" layoutH="56">
		
			
			<p>
				<label><c:out value="innermsg.msgcode" />：</label>
				 
				 
				<input name="msgcode" type="text" class="required" <c:if test="${!empty object.msgcode }">readonly="readonly"</c:if> size="40" value="${object.msgcode }" />
				
			</p>

			
			<p>
				<label><c:out value="innermsg.sender" />：</label>
				 
				 
				<input name="sender" type="text" <c:if test="${!empty innermsgForm.map.sender }">readonly="readonly"</c:if> size="40" value="${object.sender }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsg.senddate" />：</label>
				 
				 
				<input name="senddate" type="text" <c:if test="${!empty innermsgForm.map.senddate }">readonly="readonly"</c:if> size="40" value="${object.senddate }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsg.msgtitle" />：</label>
				 
				 
				<input name="msgtitle" type="text" <c:if test="${!empty innermsgForm.map.msgtitle }">readonly="readonly"</c:if> size="40" value="${object.msgtitle }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsg.msgtype" />：</label>
				 
				 
				<input name="msgtype" type="text" <c:if test="${!empty innermsgForm.map.msgtype }">readonly="readonly"</c:if> size="40" value="${object.msgtype }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsg.mailtype" />：</label>
				 
				 
				<input name="mailtype" type="text" <c:if test="${!empty innermsgForm.map.mailtype }">readonly="readonly"</c:if> size="40" value="${object.mailtype }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsg.receivename" />：</label>
				 
				 
				<input name="receivename" type="text" <c:if test="${!empty innermsgForm.map.receivename }">readonly="readonly"</c:if> size="40" value="${object.receivename }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsg.holdusers" />：</label>
				 
				 
				<input name="holdusers" type="text" <c:if test="${!empty innermsgForm.map.holdusers }">readonly="readonly"</c:if> size="40" value="${object.holdusers }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsg.msgstate" />：</label>
				 
				 
				<input name="msgstate" type="text" <c:if test="${!empty innermsgForm.map.msgstate }">readonly="readonly"</c:if> size="40" value="${object.msgstate }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsg.msgcontent" />：</label>
				 
				 
				<input name="msgcontent" type="text" <c:if test="${!empty innermsgForm.map.msgcontent }">readonly="readonly"</c:if> size="40" value="${object.msgcontent }"/>
				
			</p>
			
			<p>
				<label><c:out value="innermsg.emailid" />：</label>
				 
				 
				<input name="emailid" type="text" <c:if test="${!empty innermsgForm.map.emailid }">readonly="readonly"</c:if> size="40" value="${object.emailid }"/>
				
			</p>
			
			
			
			
			<div class="divider"></div>
			<div>
				<table class="list nowrap itemDetail" addButton="新建从表1条目" width="100%">
					<thead>
						<tr>
						
						
							
								
							
								 
									<th type="text" name="msgannex.filecode" fieldClass="required"> <c:out value="msgannex.filecode" /> </th>
								
							
							
						
							
								
							
								 
									<th type="text" name="innermsgRecipient.receive" fieldClass="required"> <c:out value="innermsgRecipient.receive" /> </th>
								
							
							
								 
									<th type="text" name="innermsgRecipient.replymsgcode" fieldClass="required"> <c:out value="innermsgRecipient.replymsgcode" /> </th>
								
							
								 
									<th type="text" name="innermsgRecipient.receivetype" fieldClass="required"> <c:out value="innermsgRecipient.receivetype" /> </th>
								
							
								 
									<th type="text" name="innermsgRecipient.mailtype" fieldClass="required"> <c:out value="innermsgRecipient.mailtype" /> </th>
								
							
								 
									<th type="text" name="innermsgRecipient.msgstate" fieldClass="required"> <c:out value="innermsgRecipient.msgstate" /> </th>
								
							
						
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
