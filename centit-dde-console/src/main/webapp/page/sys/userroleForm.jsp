<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>




<div class="pageContent">
	<s:form id="frm_userroleform" action="userDef!saveUserRole.do" namespace="/sys" theme="simple" class="pageForm required-validate" 
		onsubmit="return validateCallback(this, dialogAjaxDone);">
        <input type="hidden" name="tabid" value="${param['tabid']}"/>
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>用户代码：</label>
				<s:hidden name="userrole.id.usercode" value="%{userrole.id.usercode}" readonly="true" />
				<label>${userrole.id.usercode }</label>
			</div>
			
			<div class="unit">
				<label>用户名：</label>
				<label>${cp:MAPVALUE('usercode',userrole.id.usercode)}</label>
			</div>
			
			<div class="unit">
				<label>用户角色：</label>
                <c:if test="${not (empty userrole.rolecode) }">
                    <input type="hidden" name="userrole.id.rolecode" value="${userrole.id.rolecode }">
                    <select name="userrole.id.rolecode" disabled="disabled" style="width:140px;">
                        <c:forEach var="row" items="${cp:ROLEINFO('G-')}">
                            <option value="<c:out value='${row.rolecode}'/>"
                                    <c:if test="${row.rolecode==userrole.id.rolecode}">selected="selected"</c:if>>
                                <c:out value="${row.rolename}" /></option>
                        </c:forEach>
                    </select>
                </c:if>
                <c:if test="${empty userrole.id.rolecode }">
                    <select name="userrole.id.rolecode" style="width:140px;" class="combox">
                        <c:forEach var="row" items="${cp:ROLEINFO('G-')}">
                            <c:if test="${(not (row.rolecode eq 'G-anonymous')) and (not (row.rolecode eq 'G-public'))}">
                                <option value="${row.rolecode}"><c:out value="${row.rolename}"/></option>
                            </c:if>
                        </c:forEach>
                        <c:forEach var="row" items="${cp:ROLEINFO('P-')}">
                            <option value="${row.rolecode}"><c:out value="${row.rolename}"/></option>
                        </c:forEach>
                    </select>
                </c:if>
			</div>
			
			<div class="unit">
				<label>获取时间：</label> 
				<input type="text" name="userrole.id.obtaindate" class="date" format="yyyy-MM-dd" readonly="true" 
					value='<fmt:formatDate value="${userrole.id.obtaindate }" pattern="yyyy-MM-dd"/>' /> 
					<a class="inputDateButton" href="javascript:;">选择</a>
			</div>
			
			<div class="unit">
				<label>失效时间：</label> 
				<input id="txt_userroleform_secededate" type="text" name="userrole.secededate" class="date" format="yyyy-MM-dd" readonly="true" 
					value="<fmt:formatDate value='${userrole.secededate}' pattern='yyyy-MM-dd' />" /> 
				<a class="inputDateButton" href="javascript:;">选择</a>
			</div>

			<div class="unit">
				<label>授权说明：</label>
				<s:textarea name="userrole.changedesc" rows="4" cols="40" />
			</div>

		</div>

		<div class="formBar">
			<ul>
				<li>
					<div class="buttonActive">
						<div class="buttonContent">
							<button id="btn_userroleform_submit" type="submit">保存</button>
						</div>
					</div>
				</li>
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
