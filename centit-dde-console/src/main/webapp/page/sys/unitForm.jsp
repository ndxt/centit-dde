<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<form method="post" action="${pageContext.request.contextPath }/sys/unit!save.do" class="pageForm required-validate" 
		onsubmit="return validateCallback(this, dialogAjaxDone);">
        <input type="hidden" name="tabid" value="${param['tabid']}"/>

		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label>部门编号：</label>
				<label>
					${unitcode }
				</label>
				<s:hidden name="unitcode" value="%{unitcode}" />
			</div>
			
			<div class="unit">
				<label>上级部门：</label>
                <c:choose>
                    <c:when test="${not ('0' eq object.parentunit)}">
                        <c:if test="${empty object.parentunit }">
                            <select name="parentunit" class="combox">
                                <option value="0">
                                    <c:out value="——无——" />
                                </option>

                            </select>
                        </c:if>

                        <c:if test="${'1' eq edit && not empty object.parentunit}">
                            <%--<select name="parentunit" class="combox">
                                <c:forEach var="row" items="${unitNames}">
                                    <option value="<c:out value='${row.code}'/>"
                                            <c:if test="${row.code eq  object.parentunit}"> selected="selected" </c:if>>
                                        <c:out value="${row.name}" />
                                    </option>
                                </c:forEach>
                            </select>--%>

                            <c:set var="underUnit" value="-选择所属机构-"/>

                            <c:forEach var="row" items="${cp:LVB('unitcode')}">
                                <c:if test="${row.value eq object.parentunit}"> <c:set var="underUnit" value="${row.label}" /> </c:if>
                            </c:forEach>
                            <ui:tree id="tree_unitform" inputValue="${object.parentunit }" idKey="unitcode" items="${unitNamesJson}" name="parentunit" parentKey="parentunit" type="dialog"
                                     showValue='${pageScope.underUnit }' valueKey="unitname" basePath="${pageContext.request.contextPath}"/>


                        </c:if>
                        <c:if test="${null eq edit && not empty object.parentunit }">
                            <label>${cp:MAPVALUE('unitcode', object.parentunit) }</label>
                            <input type="hidden" value="${object.parentunit }" name="parentunit" />
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <label>无</label>
                        <input type="hidden" value="${object.parentunit }" name="parentunit" />
                    </c:otherwise>
                </c:choose>


			</div>
			
			<div class="unit">
				<label>部门名称：</label>
				<s:textfield id="unitname" name="unitname" vlaue="%{unitname}" />
			</div>
			
			<div class="unit">
				<label>部门简称：</label>
				<s:textfield id="unitshortname" name="unitshortname" vlaue="%{unitshortname}" />
			</div>
			
			<div class="unit">
				<label>部门代码：</label>
				<s:textfield id="unitword" name="unitword" vlaue="%{unitword}" />
			</div>
			
			<div class="unit">
				<label>部门状态：</label>
				<input type="radio" name="isvalid" value="T" <c:if test="${(empty isvalid) or (isvalid eq 'T') }">checked="checked"</c:if>  />启用
				<input type="radio" name="isvalid" value="F" <c:if test="${isvalid == 'F' }">checked="checked"</c:if> />禁用
			</div>
			
			<div class="unit">
				<label>部门等级：</label>
				<s:textfield id="unitgrade" name="unitgrade" vlaue="%{unitgrade}" />
			</div>

			<div class="unit">
				<label>部门序号：</label>
				<s:textfield id="unitorder" name="unitorder" vlaue="%{unitorder}" />
			</div>
			
			<div class="unit">
				<label>部门类型：</label>
				<select name="unittype" class="combox">
					<c:forEach var="row" items="${cp:LVB('UnitType')}">
						<option value="<c:out value='${row.value}'/>" <c:if test="${row.value==object.unittype}">selected="selected"</c:if>>
							<c:out value="${row.label}" />
						</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="unit">
				<label>部门描述：</label>
				<s:textarea name="unitdesc" rows="3" cols="40" />
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
	</form>
</div>
