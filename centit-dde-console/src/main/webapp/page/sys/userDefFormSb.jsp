<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/css.jsp"%>
<style>
    .nobr br{display:none}   
</style>
<html>
<head>
<meta name="decorator" content='${LAYOUT}' />
<title>人员信息</title>
<sj:head />
<s:include value="/page/common/formValidator.jsp"></s:include>
<style type="text/css" media="all">
body,div {
	font-size: 12px;
}
div{
	height: 18px;
}
tr{
	height: 30px;
}
.nobr br{
	display:none;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {

		$.formValidator.initConfig({
			formid : "form1",
			onerror : function(msg, obj, errorlist) {
				//$.map(errorlist,function(msg1){alert(msg1)});
				alert(msg);
			}
		});
		$("#usercode").formValidator().inputValidator({
			min : 1,
			max : 8,
			onerror : "输入1到8个字符"
		}).regexValidator({
			regexp : "username",
			datatype : "enum",
			onerror : "输入字母或者数字"
		});
		$("#userType").formValidator().inputValidator({
			min : 1,
			onerror : "请选择用户类型!"
		});
		$("#username").formValidator().inputValidator({
			min : 1,
			max : 32,
			onerror : "请输入1到32个字符用户名"
		}).regexValidator({
			regexp : "chinese",
			datatype : "enum",
			onerror : "输入中文"
		});
		$("#loginname").formValidator().inputValidator({
			min : 1,
			max : 16,
			onerror : "请输入1到16个字符登录名"
		});
		$("#unitName").formValidator().inputValidator({
			min : 1,
			onerror : "请输入单位名称"
		});
		$("#orgCode").formValidator().inputValidator({
			min : 1,
			onerror : "请输入组织机构代码"
		}).regexValidator({
			regexp : "[0-9]{8}-[A-Za-z0-9]{1}",
			onerror : "你输入的组织机构代码格式不正确"
		});
		$("#unitAddress").formValidator().inputValidator({
			min : 1,
			onerror : "请输入单位地址"
		});
		$("#unitZip").formValidator().inputValidator({
			min : 1,
			max : 6,
			onerror : "请输入6位的单位邮编"
		});

		$("#unitType").formValidator().inputValidator({
			min : 1,
			onerror : "请选择单位类型"
		});
		$("#contact").formValidator().inputValidator({
			min : 1,
			onerror : "请输入联系人"
		});
		$("#contactCodeType").formValidator().inputValidator({
			min : 1,
			onerror : "请选择联系人证件类型"
		});
		$("#contactCode").formValidator().inputValidator({
			min : 1,
			onerror : "请输入联系人证件号码"
		});
		$("#contactDep").formValidator().inputValidator({
			min : 1,
			onerror : "请输入联系人部门"
		});
		$("#officePhone").formValidator().inputValidator({
			min : 1,
			onerror : "请输入办公电话"
		}).regexValidator({
			regexp : "^[[0-9]{3}-|\[0-9]{4}-]?([0-9]{8}|[0-9]{7})?$",
			onerror : "你输入的办公电话格式不正确"
		});
		$("#contactPhone").formValidator().inputValidator({
			min : 11,
			max : 11,
			onerror : "手机号码必须是11位的,请确认"
		}).regexValidator({
			regexp : "mobile",
			datatype : "enum",
			onerror : "你输入的手机号码格式不正确"
		});

		$("#contactFax").formValidator().regexValidator({
			regexp : "^[[0-9]{3}-|\[0-9]{4}-]?([0-9]{8}|[0-9]{7})?$",
			onerror : "你输入的传真格式不正确"
		});

		$("#regemail").formValidator().inputValidator({
			min : 1,
			onerror : "请输入联系人邮件"
		}).regexValidator({
			regexp : "email",
			datatype : "enum",
			onerror : "你输入的邮箱格式不正确"
		});

		$("select[name='primaryUnit'] option").each(function() {
			//var primaryUnit = "<s:property value='object.primaryUnit'/>";
			if ($(this).val() == '$(object.primaryUnit)') {
				$(this).attr("selected", true);
			}

		});

		//$("[name='areaCode']")
	});
</script>
</head>

<body>
	<p class="ctitle">用户信息</p>

	<%@ include file="/page/common/messages.jsp"%>

	<s:form action="userDef" id="form1">
		<s:submit method="verifySave" cssClass="btn" value="保存" />
	<input type="button"  value="返回" Class="btn"  onclick="window.history.back()"/>

		<table  border="0" cellpadding="1" cellspacing="1">
			<tr>
				<td class="TDTITLE">用户代码*</td>
				<td><c:if test="${not empty usercode}">
						<s:textfield name="usercode" value="%{object.usercode}"
							maxLength="8" readonly="true" />
					</c:if> <c:if test="${empty usercode}">
						<s:textfield id="usercode" readonly="true"  name="usercode" maxLength="8" />
					</c:if></td>
				<td style="width:250px;"><span id="usercodeTip"></span></td>
			</tr>
			<tr>
				<td class="TDTITLE">用户类型*</td>
				<td align="left"><select disabled="disabled"  name="userType" id="userType">
						<option value="">
							<c:out value="--" />
						</option>
						<c:forEach var="row" items="${cp:LVB('UserType')}">
							<option value="<c:out value='${row.value}'/>"
								<c:if test="${row.value==object.userType}">selected="selected"</c:if>>
								<c:out value="${row.label}" />
							</option>
						</c:forEach>
				</select></td>
				<td><div id="userTypeTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">用户名*</td>
				<td><s:textfield readonly="true"  id="username" name="username" maxlength="6" />
				</td>
				<td><div id="usernameTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">登录名*</td>
				<td><s:textfield readonly="true"  id="loginname" name="loginname" /></td>
				<td><div id="loginnameTip"></div></td>
			</tr>

			<tr>
				<td class="TDTITLE">单位名称*</td>
				<td><s:textfield readonly="true"  id="unitName" name="unitName" /></td>
				<td><div id="unitNameTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">组织机构代码*</td>
				<td><s:textfield readonly="true"  id="orgCode" name="orgCode" /></td>
				<td><div id="orgCodeTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">单位地址*</td>
				<td><s:textfield readonly="true" id="unitAddress" name="unitAddress" /></td>
				<td><div id="unitAddressTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">单位邮编</td>
				<td><s:textfield  readonly="true" id="unitZip" name="unitZip" /></td>
				<td><div id="unitZipTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">所在地区*</td>
				<td align="left" class="nobr" colspan="2">
					<div class="nobr"> 
						<s:doubleselect disabled="true"
							list="unitShi" name="primaryUnit" listKey="value"
							listValue="label" doubleName="areaCode"
							doubleList="unitQu.get(top.value)" doubleListKey="value"
							doubleListValue="label" theme="simple" />
					</div>
				</td>
			</tr>
			<tr>
				<td class="TDTITLE">单位类型*</td>
				<td align="left"><select disabled="disabled"  name="unitType" id="unitType">
						<option value="">
							<c:out value="--" />
						</option>
						<c:forEach var="row" items="${cp:LVB('UnitType')}">
							<option value="<c:out value='${row.value}'/>"
								<c:if test="${row.value==object.unitType}">selected="selected"</c:if>>
								<c:out value="${row.label}" />
							</option>
						</c:forEach>
				</select></td>
				<td><div id="unitTypeTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">联系人</td>
				<td><s:textfield readonly="true"  id="contact" name="contact" /></td>
				<td><div id="contactTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">联系人证件类型*</td>
				<td align="left"><select disabled="disabled" name="contactCodeType"
					id="contactCodeType">
						<option value="">
							<c:out value="--" />
						</option>
						<c:forEach var="row" items="${cp:LVB('PaperType')}">
							<option value="<c:out value='${row.value}'/>"
								<c:if test="${row.value==object.contactCodeType}">selected="selected"</c:if>>
								<c:out value="${row.label}" />
							</option>
						</c:forEach>
				</select></td>
				<td><div id="contactCodeTypeTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">联系人证件号码</td>
				<td><s:textfield  readonly="true" id="contactCode" name="contactCode" /></td>
				<td><div id="contactCodeTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">联系人部门</td>
				<td><s:textfield  readonly="true" id="contactDep" name="contactDep" /></td>
				<td><div id="contactDepTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">办公电话</td>
				<td><s:textfield  readonly="true" id="officePhone" name="officePhone" /></td>
				<td><div id="officePhoneTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">联系人手机</td>
				<td><s:textfield  readonly="true" id="contactPhone" name="contactPhone" /></td>
				<td><div id="contactPhoneTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">联系人传真</td>
				<td><s:textfield  readonly="true" id="contactFax" name="contactFax" /></td>
				<td><div id="contactFaxTip"></div></td>
			</tr>
			<tr>
				<td class="TDTITLE">联系人邮件</td>
				<td><s:textfield  readonly="true" id="regemail" name="regemail" /></td>
				<td><div id="regemailTip"></div></td>
			</tr>

			<tr>
				<td class="TDTITLE">审核状态</td>
				<td colspan="2"><s:radio name="userState"
						list="#{'1':'审核通过','0':'未通过审核' }" listKey="key" listValue="value"></s:radio></td>
			</tr>
			<tr>
				<td class="TDTITLE">用户状态</td>
				<td colspan="2"><s:radio name="isvalid"
						list="#{'T':'启用','F':'禁用' }" listKey="key" listValue="value"></s:radio></td>
			</tr>

			<tr>
				<td class="TDTITLE">用户说明</td>
				<td colspan="2"><s:textarea readonly="true"  name="userdesc" cols="40" rows="3" />
				</td>
			</tr>
		</table>

	</s:form>

</body>
</html>
