<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageContent">
	<form id="form1" method="post" action="${pageContext.request.contextPath}/dde/mdColumn!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input type="hidden" name="tbcode" value="${tbcode }" /> 
	<%-- 	<input type="hidden" name="s_colcode" value="${s_colcode }" />  --%>
		<div class="pageFormContent" layoutH="60">
			<p>
				<label class="required">字段代码：</label>
				<c:if test="${ not empty colcode}">
					<input name="colcode" size="30" readonly="true" class="required" maxlength="32" value="${colcode }" />
				</c:if>
				<c:if test="${empty colcode }">
					<input name="colcode" maxlength="32" size="30" class="required alphanumeric" 
						maxlength="32" value="${colcode }" alt="不超过32位的数字或字母" />
				</c:if>		
			</p>
			
			<p>
				<label>字段名称：</label>
				<input name="colname" maxlength="64" size="30" class="required" value="${colname }" />
			</p>
			
			<p>
				<label>字段类型：</label>
				<input name="coltype" maxlength="32" size="30" class="required "  value="${coltype }" />
			</p>
			
			<p>
				<label>字段长度：</label>
				<input name="colLength" maxlength="32" size="30" value="${colLength }" />
			</p>
			
			<p>
				<label>字段精度：</label>
				<input name="colPrecision" maxlength="32" size="30" value="${colPrecision }" />
			</p>
			<p>
				<label>字段类别：</label>
				<c:if test="${not empty accetype}">
					<select name="accetype" class="combox">
						<c:forEach var="row" items="${cp:LVB('accetype')}">
							<option value="${row.value}" <c:if test="${row.value==accetype}">selected="selected"</c:if>>
								<c:out value="${row.label}" />
							</option>
						</c:forEach>
					</select>
					<s:hidden name="maccetype" value="%{mdColumn.accetype}"></s:hidden>
				</c:if>
				<c:if test="${ empty accetype}">
					<select name="accetype" class="combox">
						<c:forEach var="row" items="${cp:LVB('accetype')}">
							<option value="${row.value}" <c:if test="${row.value==accetype}">selected="selected"</c:if>>
								<c:out value="${row.label}" />
							</option>
						</c:forEach>
					</select>
				</c:if>
			</p>
			<%-- <div class="unit">
				<label>字段类别：</label>
				<select class="combox" name="accetype">
      				  <option value="0"  <c:if test="${accetype eq '0'}">selected=selected</c:if>>不可以访问</option>
      				  <option value="1"  <c:if test="${accetype eq '1'|| empty checkState}">selected=selected</c:if>>公开</option>
      				  <option value="2"  <c:if test="${accetype eq '2'}">selected=selected</c:if>>内部访问</option>
				</select>
			</div> --%>
			<p>
				<label>是否是主键：</label>
					<input type="radio" name="primaryKey" value="F" <c:if test="${primaryKey=='F' || empty primaryKey}">checked="checked"</c:if> />否
					<input type="radio" name="primaryKey" value="T" <c:if test="${primaryKey=='T' }">checked="checked"</c:if>/>是

			</p>
			<div class="unit">
				<label>状态：</label>
				<select class="combox" name="checkState">
      				  <option value="0"  <c:if test="${checkState eq '0'|| empty checkState}">selected=selected</c:if>>未检验</option>
      				  <option value="1"  <c:if test="${checkState eq '1'}">selected=selected</c:if>>检验一致</option>
      				  <option value="2"  <c:if test="${checkState eq '2'}">selected=selected</c:if>>检验不一致</option>
				</select>
			</div>
			<p>
				<label>参考字典：</label>
					<input name="dataCatalog" maxlength="16" size="30"  value="${dataCatalog }" />				
			</p>
			
			<p>
				<label>显示次序：</label>				
				<input name="colOrder" maxlength="16" size="30"    value="${colOrder }" />		
			</p>
			
			
			<div class="divider"></div>
			<p style="width:100%">
				<label>约束关系：</label>
				<s:textarea rows="5" cols="50" maxlength="256" name="colStdesc" ></s:textarea>				
			</p>
			<p></p><p></p>
			<div class="divider"></div>
			<p style="width:100%">
				<label>参考sql语句：</label>
				<s:textarea rows="5" cols="50" maxlength="256" name="refDataSql" ></s:textarea>				
			</p>
			<p></p><p></p>
			<div class="divider"></div>
			<p style="width:100%">
				<label>字典关系：</label>
				<s:textarea rows="5" cols="50" maxlength="256" name="coldesc" ></s:textarea>				
			</p>
			
		</div>
		
		<div class="formBar">
			<ul>
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
				<li>
					<div class="button"><div class="buttonContent"><button type="button" class="close">取消</button></div></div>
				</li>
			</ul>
		</div>
	</form>
</div>
