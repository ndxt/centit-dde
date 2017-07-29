<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/app/fileinfo!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">

		<div class="pageFormContent" layoutH="56">
		
			
			<p>
				<label><c:out value="fileinfo.filecode" />：</label>
				 
				 
				<input name="filecode" type="text" class="required" <c:if test="${!empty object.filecode }">readonly="readonly"</c:if> size="40" value="${object.filecode }" />
				
			</p>

			
			<p>
				<label><c:out value="fileinfo.filename" />：</label>
				 
				 
				<input name="filename" type="text" <c:if test="${!empty fileinfoForm.map.filename }">readonly="readonly"</c:if> size="40" value="${object.filename }"/>
				
			</p>
			
			<p>
				<label><c:out value="fileinfo.path" />：</label>
				 
				 
				<input name="path" type="text" <c:if test="${!empty fileinfoForm.map.path }">readonly="readonly"</c:if> size="40" value="${object.path }"/>
				
			</p>
			
			
			
			
			<div class="divider"></div>
			<div>
				<table class="list nowrap itemDetail" addButton="新建从表1条目" width="100%">
					<thead>
						<tr>
						
						
							
								 
									<th type="text" name="publicinfo.infocode" fieldClass="required"> <c:out value="publicinfo.infocode" /> </th>
								
							
							
								 
									<th type="text" name="publicinfo.parentinfocode" fieldClass="required"> <c:out value="publicinfo.parentinfocode" /> </th>
								
							
								
							
								 
									<th type="text" name="publicinfo.filename" fieldClass="required"> <c:out value="publicinfo.filename" /> </th>
								
							
								 
									<th type="text" name="publicinfo.fileextension" fieldClass="required"> <c:out value="publicinfo.fileextension" /> </th>
								
							
								 
									<th type="text" name="publicinfo.ownercode" fieldClass="required"> <c:out value="publicinfo.ownercode" /> </th>
								
							
								 
									<th type="text" name="publicinfo.readcount" fieldClass="required"> <c:out value="publicinfo.readcount" /> </th>
								
							
								 
									<th type="text" name="publicinfo.downloadcount" fieldClass="required"> <c:out value="publicinfo.downloadcount" /> </th>
								
							
								 
									<th type="text" name="publicinfo.md5" fieldClass="required"> <c:out value="publicinfo.md5" /> </th>
								
							
								 
									<th type="text" name="publicinfo.uploadtime" fieldClass="required"> <c:out value="publicinfo.uploadtime" /> </th>
								
							
								 
									<th type="text" name="publicinfo.modifytimes" fieldClass="required"> <c:out value="publicinfo.modifytimes" /> </th>
								
							
								 
									<th type="text" name="publicinfo.status" fieldClass="required"> <c:out value="publicinfo.status" /> </th>
								
							
								 
									<th type="text" name="publicinfo.type" fieldClass="required"> <c:out value="publicinfo.type" /> </th>
								
							
								 
									<th type="text" name="publicinfo.isfolder" fieldClass="required"> <c:out value="publicinfo.isfolder" /> </th>
								
							
								 
									<th type="text" name="publicinfo.filedescription" fieldClass="required"> <c:out value="publicinfo.filedescription" /> </th>
								
							
						
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
