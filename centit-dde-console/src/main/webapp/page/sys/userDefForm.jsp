<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">
	<form id="form1" onsubmit="return validateCallback(this, dialogAjaxDone);" class="pageForm required-validate" action="${contextPath}/sys/userDef!save.do" method="post">
        <input type="hidden" name="tabid" value="${param['tabid'] }"/>
		<div class="pageFormContent" layoutH="58">
			<c:if test="${not empty object.usercode}">
				<div class="unit">
					<label>用户代码：</label>
					<label>${object.usercode }</label>
					<input type="hidden" name="usercode" value="${object.usercode}" />
				</div>
			</c:if>

			<div class="unit">
				<label>用户名：</label>
				<c:if test="${not empty username}">
					<label>${username }</label>
					<input type="hidden" id="username" name="username" value="${object.username}"/>
				</c:if>
				
				<c:if test="${empty username}">
					<input type="text" id="username" name="username" size="12" maxLength="10" class="required" />
				</c:if>
			</div>

			<div class="unit">
				<label>登录名：</label>
				
				<c:if test="${not empty loginname}">
					<label>${loginname }</label>
					<input type="hidden" id="loginname" name="loginname" value="${object.loginname}"/>
				</c:if>
				
				<c:if test="${empty loginname}">
					<input type="text" id="loginname" name="loginname" size="12" maxLength="10" class="required" />
				</c:if>
			</div>

<%-- 			<div class="unit">
				<label>人员排序：</label>
				<s:textfield id="userorder" name="userorder" size="12" maxLength="4" cssClass="required digits" />
			</div> --%>

			<div class="unit">
				<label>主机构：</label>

                <c:if test="${empty unitList}">

                    <select id="unitcode" name="userUnit.unitcode" class="combox">
                        <option value="">-请选择-</option>
                        <c:forEach var="row" items="${cp:LVB('unitcode')}">
                            <option value="${row.value}"
                                    <c:if test="${row.value==userUnit.unitcode}">selected="selected"</c:if> >
                                <c:out value="${row.label}"/>
                            </option>
                        </c:forEach>
                    </select>

                </c:if>
                <c:if test="${not (empty unitList)}">

                    <select id="unitcode" name="userUnit.unitcode" class="combox">
                        <option value="">-请选择-</option>
                        <c:forEach var="row" items="${unitList}">
                            <option value="${row.unitcode}" <c:if test="${row.unitcode==userUnit.unitcode}">selected="selected"</c:if> > <c:out value="${row.unitname}"/>
                            </option>
                        </c:forEach>
                    </select>
                </c:if>

				<%--<c:if test="${not empty userUnit.unitcode}">
					<label>${cp:MAPVALUE('unitcode', userUnit.unitcode) }</label>
					<s:hidden name="userUnit.unitcode" value="%{userUnit.unitcode}"></s:hidden>
				</c:if>--%>
				<%--<c:if test="${ empty userUnit.unitcode}">--%>
					<%--<select name="userUnit.unitcode" class="combox">
						<c:forEach var="row" items="${unitLabelValueBeans }">
							<option value="${row.value}" <c:if test="${row.value==userUnit.unitcode}">selected="selected"</c:if>>
								<c:out value="${row.label}" />
							</option>
						</c:forEach>
					</select>--%>
				<%--</c:if>--%>
			</div>

			<div class="unit">
				<label>岗位：</label>
				
				<%--<c:if test="${not empty userUnit.userstation}">
					<label>${cp:MAPVALUE('StationType',userUnit.userstation)}</label>
					<s:hidden name="userUnit.userstation" value="%{userUnit.userstation}"></s:hidden>
				</c:if>
				<c:if test="${ empty userUnit.userstation}">--%>
					<select name="userUnit.userstation" class="combox">
						<c:forEach var="row" items="${cp:LVB('StationType')}">
							<option value="${row.value}" <c:if test="${row.value==userUnit.userstation}">selected="selected"</c:if>>
								<c:out value="${row.label}" />
							</option>
						</c:forEach>
					</select>
				<%--</c:if>--%>
			</div>

			<div class="unit">
				<label>行政职务：</label>
				
				<%--<c:if test="${not empty userUnit.userrank}">
					<label>${cp:MAPVALUE('RankType',userUnit.userrank)}</label>
					<s:hidden name="userUnit.userrank" value="%{userUnit.userrank}"></s:hidden>
				</c:if>
				<c:if test="${ empty userUnit.userrank}">--%>
					<select name="userUnit.userrank" class="combox">
						<c:forEach var="row" items="${cp:LVB('RankType')}">
							<option value="${row.value}" <c:if test="${row.value==userUnit.userrank}">selected="selected"</c:if>>
								<c:out value="${row.label}" />
							</option>
						</c:forEach>
					</select>
				<%--</c:if>--%>
			</div>

			<div class="unit">
				<label>用户状态：</label>
				 
				<input type="radio" name=isvalid value="T" <c:if test="${isvalid=='T' || empty isvalid}">checked="checked"</c:if> />启用
				<input type="radio" name=isvalid value="F" <c:if test="${isvalid=='F' }">checked="checked"</c:if> />禁用
			</div>

			<div class="unit">
				<label>用户说明：</label>
                <textarea name="userdesc" id="" cols="40" rows="3">${object.userdesc}</textarea>
			</div>

		</div>

		<div class="formBar">
			<ul>
				<li>
					<div class="buttonActive">
						<div class="buttonContent">
							<s:submit value="提交" onclick="return validateCallback(document.all.form1, dialogAjaxDone)" />
						</div>
					</div>
				</li>

				<c:if test="${object.isvalid eq 'T'}">
					<li>
						<a href="${pageContext.request.contextPath}/sys/userDef!resetpwd.do?usercode=${object.usercode}" callback="dialogAjaxDone"
							class="button" target="ajaxTodo" title="是否重置密码？">
							<span>重置密码</span>
						</a>
					</li>
				</c:if>
				
				<c:if test="${ not empty object.addrbookid }">
					<li>
						<a href="${pageContext.request.contextPath}/sys/userDef!editAddressBook.do"
							class="button" target="ajaxTodo">
							<span>编辑通讯录信息</span>
						</a>
					</li>
				</c:if>

				<li>
					<div class="buttonActive">
						<div class="buttonContent">
							<button type="button" class="close">取消</button>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</form>

</div>