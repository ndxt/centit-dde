<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<link href="<s:url value="/scripts/autocomplete/autocomplete.css"/>" type="text/css" rel="stylesheet">
<script language="javascript" src="<s:url value="/scripts/autocomplete/autocomplete.js"/>" type="text/javascript"></script>
<script language="javascript" src="<s:url value="/scripts/selectUser.js"/>" type="text/javascript"></script>

<script type="text/javascript">
    var list = [];
    <c:forEach var="userinfo" varStatus="status" items="${cp:ALLUSER('T')}">
        list[${status.index}] = { username:'<c:out value="${userinfo.username}"/>', 
        		loginname:'<c:out value="${userinfo.loginname}"/>', 
        		usercode:'<c:out value="${userinfo.usercode}"/>',
        		pinyin:'<c:out value="${userinfo.nameFisrtLetter}"/>'  };
    </c:forEach>
    function selectUser(obj) {
           userInfo.choose(obj, {dataList:list,userName:$('#userName')});
    }
</script>

<div class="pageContent">
	<form method="post" action="${pageContext.request.contextPath }/sys/unit!saveUnitUser.do" class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone);">
        <input type="hidden" name="tabid" value="${param['tabid']}"/>
		
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>机构代码</label>
				<label>
					${userunit.unitcode }
					<s:hidden name="userunit.unitcode" value="%{userunit.unitcode}" />
				</label>
			</div>
			
			<div class="unit">
				<label>机构名称</label>
				<label><c:out value="${cp:MAPVALUE('unitcode',userunit.unitcode)}" /></label>
			</div>
			
			<div class="unit">
				<label>用户代码</label>
				<s:textfield name="userunit.usercode" onclick="selectUser(this);" id="userCode" />
			</div>
			
			<div class="unit">
				<label>用户名</label>
				<span id="getName"></span>	
			</div>
			
			<div class="unit">
				<label>用户岗位</label>
				<select name="userunit.userstation" class="combox">
					<c:forEach var="row" items="${cp:LVB('StationType')}">
						<option value="<c:out value='${row.value}'/>" <c:if test="${row.value==userUnit.userstation}">selected="selected"</c:if>>
							<c:out value="${row.label}" />
						</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="unit">
				<label>行政职务</label>
				<select name="userunit.userrank" class="combox">
					<c:forEach var="row" items="${cp:LVB('RankType')}">
						<option value="<c:out value='${row.value}'/>" <c:if test="${row.value==userUnit.userrank}">selected="selected"</c:if>>
							<c:out value="${row.label}" />
						</option>
					</c:forEach>
				</select>
			</div>
			
			<div class="unit">
				<label>授权说明</label>
				<s:textarea name="userunit.rankmemo" rows="3" cols="40" value="%{userUnit.rankmemo}" />
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
