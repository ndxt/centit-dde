<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">
	<form method="post" action="dictionary!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input type="hidden" name="tabid" value="${param['tabid'] }" />
		<input type="hidden" name="s_CATALOGCODE" value="${s_CATALOGCODE }" />
		<input type="hidden" name="s_CATALOGNAME" value="${s_CATALOGNAME }" />
		<input type="hidden" name="s_CATALOGSTYLE" value="${s_CATALOGSTYLE }" />
		<c:if test="${ not empty catalogcode}">
			<input type="hidden" name="catalogcode" value="${catalogcode }" />
		</c:if>

		<div class="pageFormContent" layoutH="60">
			<p>
				<label class="required">字典代码：</label>
				<c:if test="${ not empty catalogcode}">
				    <label>${catalogcode }</label>
					<%-- <input name="catalogcode" size="30" readonly="true" class="required" maxlength="16" value="${catalogcode }" /> --%>
				</c:if>
				<c:if test="${empty catalogcode }">
					<input name="catalogcode" maxlength="16" size="30" class="required alphanumeric" 
						maxlength="16" value="${catalogcode }" alt="不超过16位的数字或字母" />
				</c:if>		
			</p>
			
			<p>
				<label>字典名称：</label>
				<input name="catalogname" maxlength="64" size="30" class="required" value="${catalogname }" />
			</p>
			
			<p style="width:100%">
				<label>字典类别：</label>
				<input type="radio" name="catalogstyle" value="U" <c:if test="${catalogstyle=='U' || empty catalogstyle}">checked="checked"</c:if> />用户字典
				<input type="radio" name="catalogstyle" value="S" <c:if test="${catalogstyle=='S' }">checked="checked"</c:if> />系统字典
				<input type="radio" name="catalogstyle" value="G" <c:if test="${catalogstyle=='G' }">checked="checked"</c:if> />国标字典
				<%-- <input type="radio" name="catalogstyle" value="L" <c:if test="${catalogstyle=='L' }">checked="checked"</c:if> />同步字典 --%>
			</p>
			
			<p>
				<label>字典形式：</label>
				<%-- 字典形式不能随意修改 --%>
				<c:if test="${empty catalogtype}">
					<input type="radio" name="catalogtype" value="L" checked="checked" />列表字典
					<input type="radio" name="catalogtype" value="T" />树形字典
				</c:if>
				<c:if test="${not empty catalogtype}">
					<c:if test="${catalogtype=='L'}">列表字典</c:if>
					<c:if test="${catalogtype=='T'}">树形字典</c:if>
				</c:if>
			</p>
			<div class="divider"></div>
			<p style="width:100%">
				<label>字段描述：</label>
				<textarea rows="5" cols="50" maxlength="256" name="fielddesc">${fielddesc }</textarea>
			</p>
			<p></p><p></p>
			<p style="width:100%">
				<label>字典描述：</label>
				<textarea rows="5" cols="50" maxlength="256" name="catalogdesc">${catalogdesc }</textarea>
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
