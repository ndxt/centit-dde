<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/app/publicinfo!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">

		<div class="pageFormContent" layoutH="56">
		
			
			<p>
				<label><c:out value="publicinfo.infocode" />：</label>
				 
				 
				<input name="infocode" type="text" class="required" <c:if test="${!empty object.infocode }">readonly="readonly"</c:if> size="40" value="${object.infocode }" />
				
			</p>

			
			<p>
				<label><c:out value="publicinfo.parentinfocode" />：</label>
				 
				 
				<input name="parentinfocode" type="text" <c:if test="${!empty publicinfoForm.map.parentinfocode }">readonly="readonly"</c:if> size="40" value="${object.parentinfocode }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfo.filecode" />：</label>
				 
				 
				<input name="filecode" type="text" <c:if test="${!empty publicinfoForm.map.filecode }">readonly="readonly"</c:if> size="40" value="${object.filecode }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfo.filename" />：</label>
				 
				 
				<input name="filename" type="text" <c:if test="${!empty publicinfoForm.map.filename }">readonly="readonly"</c:if> size="40" value="${object.filename }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfo.fileextension" />：</label>
				 
				 
				<input name="fileextension" type="text" <c:if test="${!empty publicinfoForm.map.fileextension }">readonly="readonly"</c:if> size="40" value="${object.fileextension }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfo.ownercode" />：</label>
				 
				 
				<input name="ownercode" type="text" <c:if test="${!empty publicinfoForm.map.ownercode }">readonly="readonly"</c:if> size="40" value="${object.ownercode }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfo.readcount" />：</label>
				 
				 
				<input name="readcount" type="text" <c:if test="${!empty publicinfoForm.map.readcount }">readonly="readonly"</c:if> size="40" value="${object.readcount }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfo.downloadcount" />：</label>
				 
				 
				<input name="downloadcount" type="text" <c:if test="${!empty publicinfoForm.map.downloadcount }">readonly="readonly"</c:if> size="40" value="${object.downloadcount }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfo.md5" />：</label>
				 
				 
				<input name="md5" type="text" <c:if test="${!empty publicinfoForm.map.md5 }">readonly="readonly"</c:if> size="40" value="${object.md5 }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfo.uploadtime" />：</label>
				 
				 
				<input name="uploadtime" type="text" <c:if test="${!empty publicinfoForm.map.uploadtime }">readonly="readonly"</c:if> size="40" value="${object.uploadtime }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfo.modifytimes" />：</label>
				 
				 
				<input name="modifytimes" type="text" <c:if test="${!empty publicinfoForm.map.modifytimes }">readonly="readonly"</c:if> size="40" value="${object.modifytimes }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfo.status" />：</label>
				 
				 
				<input name="status" type="text" <c:if test="${!empty publicinfoForm.map.status }">readonly="readonly"</c:if> size="40" value="${object.status }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfo.type" />：</label>
				 
				 
				<input name="type" type="text" <c:if test="${!empty publicinfoForm.map.type }">readonly="readonly"</c:if> size="40" value="${object.type }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfo.isfolder" />：</label>
				 
				 
				<input name="isfolder" type="text" <c:if test="${!empty publicinfoForm.map.isfolder }">readonly="readonly"</c:if> size="40" value="${object.isfolder }"/>
				
			</p>
			
			<p>
				<label><c:out value="publicinfo.filedescription" />：</label>
				 
				 
				<input name="filedescription" type="text" <c:if test="${!empty publicinfoForm.map.filedescription }">readonly="readonly"</c:if> size="40" value="${object.filedescription }"/>
				
			</p>
			
			
			
			
			<div class="divider"></div>
			<div>
				<table class="list nowrap itemDetail" addButton="新建从表1条目" width="100%">
					<thead>
						<tr>
						
						
							
								 
									<th type="text" name="publicinfolog.usercode" fieldClass="required"> <c:out value="publicinfolog.usercode" /> </th>
								
							
								
							
							
								 
									<th type="text" name="publicinfolog.operation" fieldClass="required"> <c:out value="publicinfolog.operation" /> </th>
								
							
								 
									<th type="text" name="publicinfolog.data1" fieldClass="required"> <c:out value="publicinfolog.data1" /> </th>
								
							
								 
									<th type="text" name="publicinfolog.data2" fieldClass="required"> <c:out value="publicinfolog.data2" /> </th>
								
							
						
							
								 
									<th type="text" name="msgannex.msgcode" fieldClass="required"> <c:out value="msgannex.msgcode" /> </th>
								
							
								 
									<th type="text" name="msgannex.filecode" fieldClass="required"> <c:out value="msgannex.filecode" /> </th>
								
							
							
						
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
