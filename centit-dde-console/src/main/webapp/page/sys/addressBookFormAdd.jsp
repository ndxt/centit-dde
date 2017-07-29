<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">
	<s:form action="/sys/addressBook!save.do"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, navTabAjaxDone);"
		id="addressBookFromAdd">
		<div class="pageFormContent" layoutH="56">
			<c:if test="${null!=GROUPID}">
				<!-- <input type="hidden" name="isprivate" value="0" /> -->
				<input type="hidden" name="groupid" value="${GROUPID }" />
			</c:if>

			<%-- <c:if test="${null==GROUPID}">
				<input type="hidden" name="isprivate" value="1" />
			</c:if>--%>
			
			<fieldset>
				<legend>基本信息</legend>
				<dl>
					<dt>姓名：</dt>
					<dd>
						<input value="${bodyname}" name="bodyname" type="text" class="required"/>
					</dd>
				</dl>
				<dl>
					<dt>用户代码：</dt>
					<dd>
						<input type="text" name="usercode" value="${usercode}"
							maxLength="7" minLength="7" class="required"/>
					</dd>
				</dl>
				<dl>
					<dt>通讯主体类别：</dt>
					<dd>
						<select size="50" name="bodytype" class="combox">
							<option value="0" selected="selected">请选择类别</option>
							<c:forEach var="BODYTYPE" items="${BODYTYPE }">
								<option value="${BODYTYPE.CODE}"
									<c:if test="${BODYTYPE.CODE eq object.bodytype }"> selected = "selected" </c:if>>${BODYTYPE.VALUE
									}</option>
							</c:forEach>
						</select>
					</dd>
				</dl>
				<dl>
					<dt>通讯主体编号：</dt>
					<dd>
						<input type = "text" name="bodycode" value="${bodycode}" class="required"/>
					</dd>
				</dl>
				<dl>
					<dt>单位名：</dt>
					<dd>
						<input type="text" name="unitname" value="${unitname}" />
					</dd>
				</dl>
				<c:if test="${BODYTYPEJUDGE eq 1 ||BODYTYPEJUDGE eq ''}">
					<dl>
						<dt>部门名：</dt>
						<dd>
							<select size="50" name="unitcode" class="combox">
								<option value="" selected="selected">请选择部门</option>
								<c:forEach var="UNITS" items="${UNITS }">
									<option value="${UNITS.CODE}"
										<c:if test="${UNITS.CODE eq object.unitcode }"> selected = "selected" </c:if>>${UNITS.VALUE
										}</option>
								</c:forEach>
								<option value="0">其他</option>
							</select>
						</dd>
					</dl>
				</c:if>
				<dl>
					<dt>职务：</dt>
					<dd>
						<input type="text" name="rankname" value="${rankname}" />
					</dd>
				</dl>
				<dl>
					<dt>表示为：</dt>
					<dd>
						<input type="text" name="representation" value="${representation}" />
					</dd>
				</dl>
				<dl>
					<dt>是否私有：</dt>
					<dd>
						<select size="50" name="isprivate" class="combox">
							<option value="" selected="selected">请选择</option>
					    	<option value="1" <c:if test="${isprivate eq 1 }">selected=selected</c:if>>公开可见 </option>
					    	<option value="0" <c:if test="${isprivate eq 0 }">selected=selected</c:if>>仅对自己可见</option>
					    </select>
					</dd>
				</dl>
			</fieldset>

			<fieldset>
				<legend>Internet</legend>
				<dl>
					<dt>电子邮件1：</dt>
					<dd>
						<input type="text" name="email" value="${email}" />
					</dd>
				</dl>
				<dl>
					<dt>电子邮件2：</dt>
					<dd>
						<input type="text" name="email2" value="${email2}" />
					</dd>
				</dl>
				<dl>
					<dt>电子邮件3：</dt>
					<dd>
						<input type="text" name="email3" value="${email3}" />
					</dd>
				</dl>
				<dl>
					<dt>网页地址：</dt>
					<dd>
						<input type="text" name="homepage" value="${homepage}" />
					</dd>
				</dl>
				<dl>
					<dt>qq：</dt>
					<dd>
						<input type="text" name="qq" value="${qq}" />
					</dd>
				</dl>
				<dl>
					<dt>msn：</dt>
					<dd>
						<input type="text" name="msn" value="${msn}" />
					</dd>
				</dl>
				<dl>
					<dt>旺旺：</dt>
					<dd>
						<input type="text" name="wangwang" value="${wangwang}" />
					</dd>
				</dl>
			</fieldset>

			<fieldset>
				<legend>电话号码</legend>
				<dl>
					<dt>商务电话：</dt>
					<dd>
						<input type="text" name="buzphone" value="${buzphone}" />
					</dd>
				</dl>
				<dl>
					<dt>商务电话2：</dt>
					<dd>
						<input type="text" name="buzphone2" value="${buzphone2}" />
					</dd>
				</dl>
				<dl>
					<dt>商务传真：</dt>
					<dd>
						<input type="text" name="buzfax" value="${buzfax}" />
					</dd>
				</dl>
				<dl>
					<dt>助理电话：</dt>
					<dd>
						<input type="text" name="assiphone" value="${assiphone}" />
					</dd>
				</dl>
				<dl>
					<dt>回复电话：</dt>
					<dd>
						<input type="text" name="callbackphone" value="${callbackphone}" />
					</dd>
				</dl>
				<dl>
					<dt>车载电话：</dt>
					<dd>
						<input type="text" name="carphone" value="${carphone}" />
					</dd>
				</dl>
				<dl>
					<dt>单位电话：</dt>
					<dd>
						<input type="text" name="unitphone" value="${unitphone}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅电话：</dt>
					<dd>
						<input type="text" name="homephone" value="${homephone}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅电话2：</dt>
					<dd>
						<input type="text" name="homephone2" value="${homephone2}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅电话3：</dt>
					<dd>
						<input type="text" name="homephone3" value="${homephone3}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅传真：</dt>
					<dd>
						<input type="text" name="homefax" value="${homefax}" />
					</dd>
				</dl>
				<dl>
					<dt>移动电话：</dt>
					<dd>
						<input type="text" name="mobilephone" value="${mobilephone}" />
					</dd>
				</dl>
				<dl>
					<dt>移动电话2：</dt>
					<dd>
						<input type="text" name="mobilephone2" value="${mobilephone2}" />
					</dd>
				</dl>
				<dl>
					<dt>移动电话3：</dt>
					<dd>
						<input type="text" name="mobilephone3" value="${mobilephone3}" />
					</dd>
				</dl>

			</fieldset>

			<fieldset>
				<legend>单位地址</legend>
				<dl>
					<dt>单位邮编：</dt>
					<dd>
						<input type="text" name="unitzip" value="${unitzip}" />
					</dd>
				</dl>
				<dl>
					<dt>单位省：</dt>
					<dd>
						<input type="text" name="unitprovince" value="${unitprovince}" />
					</dd>
				</dl>
				<dl>
					<dt>单位市：</dt>
					<dd>
						<input type="text" name="unitcity" value="${unitcity}" />
					</dd>
				</dl>
				<dl>
					<dt>单位区：</dt>
					<dd>
						<input type="text" name="unitdistrict" value="${unitdistrict}" />
					</dd>
				</dl>
				<dl>
					<dt>单位街道：</dt>
					<dd>
						<input type="text" name="unitstreet" value="${unitstreet}" />
					</dd>
				</dl>
				<dl>
					<dt>单位地址：</dt>
					<dd>
						<input type="text" name="unitaddress" value="${unitaddress}" />
					</dd>
				</dl>
			</fieldset>

			<fieldset>
				<legend>住宅地址</legend>
				<dl>
					<dt>住宅邮编：</dt>
					<dd>
						<input type="text" name="homezip" value="${homezip}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅省：</dt>
					<dd>
						<input type="text" name="homeprovince" value="${homeprovince}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅市：</dt>
					<dd>
						<input type="text" name="homecity" value="${homecity}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅区：</dt>
					<dd>
						<input type="text" name="homedistrict" value="${homedistrict}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅街道：</dt>
					<dd>
						<input type="text" name="homestreet" value="${homestreet}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅地址：</dt>
					<dd>
						<input type="text" name="homeaddress" value="${homeaddress}" />
					</dd>
				</dl>

				<dl>
					<dt>住宅2邮编：</dt>
					<dd>
						<input type="text" name="home2zip" value="${home2zip}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅2省：</dt>
					<dd>
						<input type="text" name="home2province" value="${home2province}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅2市：</dt>
					<dd>
						<input type="text" name="home2city" value="${home2city}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅2区：</dt>
					<dd>
						<input type="text" name="home2district" value="${home2district}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅2街道：</dt>
					<dd>
						<input type="text" name="home2street" value="${home2street}" />
					</dd>
				</dl>
				<dl>
					<dt>住宅2地址：</dt>
					<dd>
						<input type="text" name="home2address" value="${home2address}" />
					</dd>
				</dl>

				<dl>
					<dt>常用地址：</dt>
					<dd>
					    <select size="50" name="inuseaddress" class="combox">
					    	<option value="" selected="selected">请选择常用地址</option>
					    	<option value="1" <c:if test="${inuseaddress eq 1 }">selected=selected</c:if>>住宅地址</option>
					    	<option value="2" <c:if test="${inuseaddress eq 2 }">selected=selected</c:if>>住宅2地址</option>
					    	<option value="3" <c:if test="${inuseaddress eq 3 }">selected=selected</c:if>>单位地址</option>
					    </select>
					</dd>
				</dl>
			</fieldset>

			<fieldset>
				<legend>便签</legend>
				<dl class="nowrap">
					<dt>便签：</dt>
					<dd>
						<textarea name="memo" cols="80" rows="3">${memo}</textarea>
					</dd>
				</dl>
			</fieldset>

		</div>


		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit">保存</button>
						</div>
					</div></li>
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

