<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>





<div class="pageContent">
	<form action="${pageContext.request.contextPath }/sys/deptManager!saveUserRole.do" onsubmit="return validateCallback(this, dialogAjaxDone);">
        <input type="hidden" name="tabid" value="${param['tabid']}"/>
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>用户代码：</label>
				<label>${userrole.usercode }</label>
				<input type="hidden" name="userrole.usercode" value="${userrole.usercode }" />
			</div>
			
			<div class="unit">
				<label>用户名：</label>
				<label><c:out value="${cp:MAPVALUE('usercode',userrole.usercode)}" /></label>
			</div>
			
			<div class="unit">
				<label>用户部门角色：</label>
				<select name="userrole.rolecode" class="combox">
					<c:forEach var="row" items="${cp:ROLEINFO(roleprefix)}">
						<option value="${row.rolecode}" <c:if test="${row.rolecode==userrole.rolecode}">selected="selected"</c:if>>
							<c:out value="${row.rolename}" />
						</option>
					</c:forEach>
                    <c:forEach var="row" items="${cp:ROLEINFO('P-')}">
						<option value="${row.rolecode}" <c:if test="${row.rolecode==userrole.rolecode}">selected="selected"</c:if>>
							<c:out value="${row.rolename}" />
						</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="unit">
				<label>获取时间：</label>
				<input type="text" name="userrole.obtaindate" class="date" format="yyyy-MM-dd" yearstart="-20" yearend="20" readonly="true"
					value='<fmt:formatDate  value="${userrole.obtaindate}" type="date" pattern="yyyy-MM-dd" />' /> 
				<a class="inputDateButton" href="javascript:;">选择</a>
			</div>
			
			<div class="unit">
				<label>到期时间：</label>
				<input type="text" name="userrole.secededate" class="date" format="yyyy-MM-dd" yearstart="-20" yearend="20" readonly="true"
					value='<fmt:formatDate  value="${userrole.secededate}" type="date" pattern="yyyy-MM-dd" />' /> 
				<a class="inputDateButton" href="javascript:;">选择</a>
			</div>
			
			<div class="unit">
				<label>授权说明：</label>
				<textarea name="userrole.changedesc" rows="3" cols="40"></textarea>
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

