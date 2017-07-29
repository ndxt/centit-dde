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
	<form method="post" action="${pageContext.request.contextPath }/dde/userDataOptId!save.do" class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone);">
		<input name="udId" value="${object.udId}" type="hidden"/>
		
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>用户代码</label>
				<input type="text" name="usercode" onclick="selectUser(this);" id="userCode" value="${object.usercode }"/>
			</div>
			
			<div class="unit">
				<label>用户名</label>
				<span id="getName">
					<c:if test="${not empty object.usercode}">
						${cp:MAPVALUE('usercode', object.usercode)}
					</c:if>
				</span>
			</div>
			
			<div class="unit">
				<label>业务操作ID</label>
				<input type="text" name="dataOptId" value="${object.dataOptId}"/>
			</div>

			<div class="unit">
				<label>业务操作类型</label>
				<input type="radio" name="dataoptType" value="I" <c:if test="${not ('E' eq object.dataoptType)}">checked="checked" </c:if> />导入
				<input type="radio" name="dataoptType" value="E" <c:if test="${'E' eq object.dataoptType}">checked="checked" </c:if> />导出
			</div>
			
			<div class="unit">
				<label>描述</label>
				<textarea name="describe" rows="3" cols="40">${object.describe }</textarea>
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
