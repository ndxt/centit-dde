<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/css.jsp"%>

<s:include value="/page/common/formValidator.jsp"></s:include>
<script type="text/javascript">
	$(document).ready(
			function() {

				var items = $('#ec_table tbody tr');
				for ( var i = 0; i < items.length + 1; i++) {
					$('#item_' + i + ' :checkbox:first').change(
							function() {
								var ls = $(this).parent().parent().children()
										.last().find(':checkbox');
								if ($(this).attr('checked')) {
									ls.attr('checked', 'checked');
								} else {
									ls.removeAttr('checked');
								}
							});
				}
				$.formValidator.initConfig({
					formid : "form1",
					onerror : function(msg, obj, errorlist) {
						alert(msg);
					}
				});

				$("#rolecode").formValidator().inputValidator({
					min : 1,
					max : 10,
					onerror : "角色代码请输入1到10个字符"
				}).regexValidator({
					regexp : "username",
					datatype : "enum",
					onerror : "输入字母或者数字"
				});
				$("#rolename").formValidator().inputValidator({
					min : 1,
					max : 12,
					onerror : "角色名称请输入1到12个字符"
				});
			});
</script>

<s:form action="roleDef" namespace="/sys" id="form1" styleId="roleForm" theme="simple" method="post">
	<div class="panel panelDrag" id="roleDetails">
		<h1>角色明细</h1>
		<div class="pageFormContent">
			<div class="unit">
				<label>角色代码：</label>
				<c:if test="${not empty rolecode}">
					<label>${rolecode }</label>
					<s:hidden name="rolecode" value="%{rolecode}" />
				</c:if> 
				<c:if test="${empty rolecode}">
					<s:textfield id="rolecode" name="rolecode" value="%{rolecode}" />
				</c:if>
				<span id="rolecodeTip"></span>
			</div>
			
			<div class="unit">
				<label>角色名称：</label>
				
				<s:textfield id="rolename" name="rolename" value="%{rolename}" /> 
				<span id="rolenameTip"></span>
			</div>
			
			<div class="unit">
				<label>角色描述：</label>
				<s:textfield name="roledesc" value="%{roledesc}" rows="3" cols="40" />
			</div>
			
			<div class="subBar">
				<ul>
					<li style="float:right; margin-right:50px;">
						<s:submit method="save" cssClass="btn" value="保存" />
						<div class="buttonActive">
							<div class="buttonContent">
								<button onclick="navTab.closeCurrentTab('external_ROLEMAG')">返回</button>
							</div>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</div>

<div class="pageContent">
	<p class="ctitle">角色权限</p>

	<div class="eXtremeTable">
		<table id="ec_table" class="list">

			<thead>
				<tr>
					<td class="tableHeader">业务名称</td>
					<td class="tableHeader">业务操作</td>
				</tr>
			</thead>
			<tbody class="tableBody">
				<c:set value="odd" var="rownum" />

				<c:forEach var="fOptinfo" items="${fOptinfos}" varStatus="status">
					<tr class="${rownum}" id="item_${status.count}"
						onmouseover="this.className='highlight'"
						onmouseout="this.className='${rownum}'">
						<td><input type="checkbox" name="all_${status.count}">
							<c:out value="${fOptinfo.optname}" /></td>
						<td><c:forEach var="row" items="${cp:OPTDEF(fOptinfo.optid)}">
								<input type="checkbox" name="optcodelist" value="${row.optcode}"
									<c:if test="${powerlist[row.optcode] eq '1' }">  checked="checked" </c:if> />
								<c:out value="${row.optname}" />
							</c:forEach></td>
					</tr>
					<c:set value="${rownum eq 'odd'? 'even': 'odd'}" var="rownum" />

				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
</s:form>
