<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>


<div class="pageContent">
	<div class="pageFormContent" style="height: 200px;">
		<p>
			<label>通讯录ID：</label>
			<s:property value="%{usercode}" />
		</p>
		<p>
			<label>通讯主体类别：</label>
			<s:property value="%{bodytype}" />
		</p>
		<p>
			<label>通讯主体编号：</label>
			<s:property value="%{bodycode}" />
		</p>
		<p>
			<label>描述：</label>
			<s:property value="%{representation}" />
		</p>
		<p>
			<label>单位名：</label>
			<s:property value="%{unitname}" />
		</p>
		<p>
			<label>部门名：</label>
			${cp:MAPVALUE("unitcode",unitcode)}
		</p>
		<p>
			<label>头衔名：</label>
			<s:property value="%{rankname}" />
		</p>
		<p>
			<label>电子邮件：</label>
			<s:property value="%{email}" />
		</p>
		<p>
			<label>电子邮件2：</label>
			<s:property value="%{email2}" />
		</p>
		<p>
			<label>电子邮件3：</label>
			<s:property value="%{email3}" />
		</p>
		<p>
			<label>主页：</label>
			<s:property value="%{homepage}" />
		</p>
		<p>
			<label>qq：</label>
			<s:property value="%{qq}" />
		</p>
		<p>
			<label>msn：</label>
			<s:property value="%{msn}" />
		</p>
		<p>
			<label>旺旺：</label>
			<s:property value="%{wangwang}" />
		</p>
		<p>
			<label>商务电话：</label>
			<s:property value="%{buzphone}" />
		</p>
		<p>
			<label>商务电话2：</label>
			<s:property value="%{buzphone2}" />
		</p>
		<p>
			<label>商务传真：</label>
			<s:property value="%{buzfax}" />
		</p>
		<p>
			<label>助理电话：</label>
			<s:property value="%{assiphone}" />
		</p>
		<p>
			<label>回复电话：</label>
			<s:property value="%{callbackphone}" />
		</p>
		<p>
			<label>车载电话：</label>
			<s:property value="%{carphone}" />
		</p>
		<p>
			<label>单位电话：</label>
			<s:property value="%{unitphone}" />
		</p>
		<p>
			<label>家庭电话：</label>
			<s:property value="%{homephone}" />
		</p>
		<p>
			<label>家庭电话2：</label>
			<s:property value="%{homephone2}" />
		</p>
		<p>
			<label>家庭电话3：</label>
			<s:property value="%{homephone3}" />
		</p>
		<p>
			<label>家庭传真：</label>
			<s:property value="%{homefax}" />
		</p>
		<p>
			<label>移动电话：</label>
			<s:property value="%{mobilephone}" />
		</p>
		<p>
			<label>移动电话2：</label>
			<s:property value="%{mobilephone2}" />
		</p>
		<p>
			<label>移动电话3：</label>
			<s:property value="%{mobilephone3}" />
		</p>
		<p>
			<label>单位邮编：</label>
			<s:property value="%{unitzip}" />
		</p>
		<p>
			<label>单位省：</label>
			<s:property value="%{unitprovince}" />
		</p>
		<p>
			<label>单位城市：</label>
			<s:property value="%{unitcity}" />
		</p>
		<p>
			<label>单位区：</label>
			<s:property value="%{unitdistrict}" />
		</p>
		<p>
			<label>单位街道：</label>
			<s:property value="%{unitstreet}" />
		</p>
		<p>
			<label>单位地址：</label>
			<s:property value="%{unitaddress}" />
		</p>
		<p>
			<label>家庭邮编：</label>
			<s:property value="%{homezip}" />
		</p>
		<p>
			<label>家庭省：</label>
			<s:property value="%{homeprovince}" />
		</p>
		<p>
			<label>家庭城市：</label>
			<s:property value="%{homecity}" />
		</p>
		<p>
			<label>家庭区：</label>
			<s:property value="%{homedistrict}" />
		</p>
		<p>
			<label>家庭街道：</label>
			<s:property value="%{homestreet}" />
		</p>
		<p>
			<label>家庭地址：</label>
			<s:property value="%{homeaddress}" />
		</p>
		<p>
			<label>家庭2邮编：</label>
			<s:property value="%{home2zip}" />
		</p>
		<p>
			<label>家庭2省：</label>
			<s:property value="%{home2province}" />
		</p>
		<p>
			<label>家庭2市：</label>
			<s:property value="%{home2city}" />
		</p>
		<p>
			<label>家庭2区：</label>
			<s:property value="%{home2district}" />
		</p>
		<p>
			<label>家庭2街道：</label>
			<s:property value="%{home2street}" />
		</p>
		<p>
			<label>家庭2地址：</label>
			<s:property value="%{home2address}" />
		</p>
		<p>
			<label>常用地址：</label>
			<s:property value="%{inuseaddress}" />
		</p>
		<p>
			<label>备注：</label>
			<s:property value="%{memo}" />
		</p>

	</div>

	<div class="formBar">
		<ul>
			<li>
				<div class="button">
					<div class="buttonContent">
						<button type="button" class="close">取消</button>
					</div>
				</div>
			</li>
		</ul>
	</div>
</div>




