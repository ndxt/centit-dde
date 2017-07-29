<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageContent">
	<s:form action="userDef!saveUserUnit.do" namespace="/sys" theme="simple" class="pageForm required-validate" 
		onsubmit="return validateCallback(this, dialogAjaxDone);">
		
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>用户代码：</label>
				<label><c:out value="${userUnit.id.usercode}" /></label>
				<s:hidden name="userUnit.id.usercode" value="%{userUnit.id.usercode}" />
			</div>
			
			<div class="unit">
				<label>用户名：</label>
				<label><c:out value="${cp:MAPVALUE('usercode',userUnit.id.usercode)}" /></label>
			</div>
			
			<div class="unit">
				<label>用户机构：</label>

                <c:choose>
                    <%--机构用户一对多--%>
                    <c:when test="${'O' eq AgencyMode.datavalue}">
                        <select name="userUnit.id.unitcode" class="combox">
                            <c:forEach var="row" items="${unitLabelValueBeans}">
                                <option value="<c:out value='${row.value}'/>" <c:if test="${row.value eq userUnit.id.unitcode }"> selected="selected" </c:if> >
                                    <c:out value="${row.label}" />
                                </option>
                            </c:forEach>
                        </select>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${not empty userUnit.id.unitcode }">
                            <input type="hidden" name="userUnit.id.unitcode" value="${userUnit.id.unitcode }">
                            <label>${cp:MAPVALUE('unitcode', userUnit.id.unitcode) }</label>
                        </c:if>

                        <c:if test="${empty userUnit.id.unitcode }">
                            <select name="userUnit.id.unitcode" class="combox">
                                <c:forEach var="row" items="${unitLabelValueBeans}">
                                    <option value="<c:out value='${row.value}'/>">
                                        <c:out value="${row.label}" />
                                    </option>
                                </c:forEach>
                            </select>
                        </c:if>

                    </c:otherwise>
                </c:choose>

			</div>

			<div class="unit">
				<label>是否为主单位：</label>
                <c:choose>
                    <c:when test="${'O' eq AgencyMode.datavalue}">
                        <input type="hidden" name="userUnit.isprimary" value="T">是
                    </c:when>
                    <c:otherwise>
                        <c:if test="${userUnit.isprimary == 'T'}">
                            <label>是</label>
                        </c:if>

                        <c:if test="${userUnit.isprimary != 'T'}">
                            <input type="radio" name="userUnit.isprimary" value="F" checked="checked">否
                            <input type="radio" name="userUnit.isprimary" value="T">是
                        </c:if>
                    </c:otherwise>

                </c:choose>

			</div>

			<div class="unit">
				<label>用户岗位：</label>
				<c:if test="${not empty userUnit.id.userstation }">
					<input type="hidden" name="userUnit.id.userstation" value="${userUnit.id.userstation }">
					<label>${cp:MAPVALUE('StationType',userUnit.id.userstation)}</label>
				</c:if>
				<c:if test="${empty userUnit.id.userstation }">
					<select name="userUnit.id.userstation" class="combox">
						<c:forEach var="row" items="${cp:LVB('StationType')}">
							<option value="<c:out value='${row.value}'/>" <c:if test="${row.value==userUnit.id.userstation}">selected="selected"</c:if>>
								<c:out value="${row.label}" />
							</option>
						</c:forEach>
					</select>
				</c:if>
			</div>

			<div class="unit">
				<label>行政职务：</label>
				<c:if test="${not empty userUnit.id.userrank}">
					<input type="hidden" name="userUnit.id.userrank" value="${userUnit.id.userrank }">
					<label>${cp:MAPVALUE('RankType',userUnit.id.userrank)}</label>
				</c:if>
				<c:if test="${empty userUnit.id.userrank}">
					<input type="hidden" name="userrank" value="${userUnit.id.userrank }">
					<select name="userUnit.id.userrank" class="combox">
						<c:forEach var="row" items="${cp:LVB('RankType')}">
							<option value="<c:out value='${row.value}'/>" <c:if test="${row.value==userUnit.id.userrank}">selected="selected"</c:if>>
								<c:out value="${row.label}" />
							</option>
						</c:forEach>
					</select>
				</c:if>
			</div>

			<div class="unit">
				<label>授权说明：</label>
				<s:textarea name="userUnit.rankmemo" cols="40" rows="4" />
			</div>

		</div>

		<div class="formBar">
			<ul>
				<li>
					<div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">保存</button>
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
