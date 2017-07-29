<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/css.jsp"%> 
<html>
	<head>
		<title>编辑通讯录</title>
		
		<!-- <script type="text/javascript">
			 			
			function onFormSubmit(theForm) {
				document.all.btn_save.value = "正在保存通讯录请稍等...";
				document.all.btn_save.disabled="true";
				document.getElementById('method').value = 'saveindialog';	
				theForm.submit();
			}	
			
			function onBack() {
				parent.window.returnValue = 0;
		      	window.close();
			}	
			
		</script> -->
	</head>
	<base target="_self" />
	<body>
		<p class="ctitle">
			编辑通讯录
		</p>
		<s:form action="addressBook" namespace="/sys">
		
			<s:submit method="saveindialog" value="保存通讯录" cssClass="btn"
				 disabled="flase" />
			<input type="button"  value="返回" Class="btn"  onclick="window.history.back()"/>		
			<table width="200" border="0" cellpadding="1" cellspacing="1">
				<tr>
					<td class="TDTITLE">
						通讯录ID(不用填写)
					</td>
					<td align="left">
						<c:if test="${not empty object.addrbookid   }">
						<s:textfield name="addrbookid" rows="1"
							readonly="true" value="%{addrbookid}"
							cols="40" />
							</c:if>
						<c:if test="${empty object.addrbookid }">
						<s:textfield name="addrbookid" rows="1"
							readonly="false"
							cols="40" />
							</c:if>
					</td>
				</tr>


				<tr>
					<td class="TDTITLE">
						<c:out value="bodytype" />
					</td>
					<td align="left">
						<s:textfield name="bodytype" value="%{bodytype}" size="35"  />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						<c:out value="bodycode" />
					</td>
					<td align="left">
						<s:textfield name="bodycode" value="%{bodycode}" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						描述
					</td>
					<td align="left">
						<s:textfield name="representation" value="%{representation}" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						单位名
					</td>
					<td align="left">
						<s:textfield name="unitname" vale="%{unitname}" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						部门名
					</td>
					<td align="left">
						<s:textfield name="deptname" value="%{deptname}" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						头衔名
					</td>
					<td align="left">
						<s:textfield name="rankname" value="%{rankname}" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						电子邮件
					</td>
					<td align="left">
						<s:textfield name="email" value="%{email}" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						电子邮件2
					</td>
					<td align="left">
						<s:textfield name="email2" value="%{email2}" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						电子邮件3
					</td>
					<td align="left">
						<s:textfield name="email3" value="%{email3}" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						主页
					</td>
					<td align="left">
						<s:textfield name="homepage" value="%{homepage}" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						QQ
					</td>
					<td align="left">
						<s:textfield name="qq" value="%{qq}" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						MSN
					</td>
					<td align="left">
						<s:textfield name="msn"  value="msn" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						旺旺
					</td>
					<td align="left">
						<s:textfield name="wangwang" value="wangwang" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						商务电话
					</td>
					<td align="left">
						<s:textfield name="buzphone" value="buzphone" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						商务电话2
					</td>
					<td align="left">
						<s:textfield name="buzphone2" value="buzphone2" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						商务传真
					</td>
					<td align="left">
						<s:textfield name="buzfax" value="buzfax" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						助理电话
					</td>
					<td align="left">
						<s:textfield name="assiphone" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						回复电话
					</td>
					<td align="left">
						<s:textfield name="callbacphone" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						车载电话
					</td>
					<td align="left">
						<s:textfield name="carphone" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						单位电话
					</td>
					<td align="left">
						<s:textfield name="unitphone" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						家庭电话
					</td>
					<td align="left">
						<s:textfield name="homephone" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						家庭电话2
					</td>
					<td align="left">
						<s:textfield name="homephone2" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						家庭电话3
					</td>
					<td align="left">
						<s:textfield name="homephone3" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						家庭传真
					</td>
					<td align="left">
						<s:textfield name="homefax" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						手机
					</td>
					<td align="left">
						<s:textfield name="mobilephone" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						手机2
					</td>
					<td align="left">
						<s:textfield name="mobilephone2" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						手机3
					</td>
					<td align="left">
						<s:textfield name="mobilephone3" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						单位邮编
					</td>
					<td align="left">
						<s:textfield name="unitzip" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						单位省
					</td>
					<td align="left">
						<s:textfield name="unitprovince" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						单位城市
					</td>
					<td align="left">
						<s:textfield name="unitcity" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						单位区
					</td>
					<td align="left">
						<s:textfield name="unitdistrict" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						单位街道
					</td>
					<td align="left">
						<s:textfield name="unitstreet" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						单位地址
					</td>
					<td align="left">
						<s:textfield name="unitaddress" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						住宅邮编
					</td>
					<td align="left">
						<s:textfield name="homezip" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						住宅省
					</td>
					<td align="left">
						<s:textfield name="homeprovince" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						住宅城市
					</td>
					<td align="left">
						<s:textfield name="homecity" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						住宅区
					</td>
					<td align="left">
						<s:textfield name="homedistrict" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						住宅街道
					</td>
					<td align="left">
						<s:textfield name="homestreet" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						住宅地址
					</td>
					<td align="left">
						<s:textfield name="homeaddress" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						住宅2邮编
					</td>
					<td align="left">
						<s:textfield name="home2zip" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						住宅2省
					</td>
					<td align="left">
						<s:textfield name="home2province" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						住宅2城市
					</td>
					<td align="left">
						<s:textfield name="home2city" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						住宅2区
					</td>
					<td align="left">
						<s:textfield name="home2district" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						住宅2街道
					</td>
					<td align="left">
						<s:textfield name="home2street" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						住宅2地址
					</td>
					<td align="left">
						<s:textfield name="home2address" rows="1" cols="80" size="50px" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						使用中的地址
					</td>
					<td align="left">
						<s:textfield name="inuseaddress" size="35" />
					</td>
				</tr>

				<tr>
					<td class="TDTITLE">
						备注
					</td>
					<td align="left">
						<s:textfield name="memo" size="35" />
					</td>
				</tr>
			</table>
		</s:form>
	</body>
</html>