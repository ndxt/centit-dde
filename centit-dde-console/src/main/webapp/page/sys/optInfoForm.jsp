<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/formValidator.jsp"%>


<script type="text/javascript">
	/* $(document).ready(function() {

		$.formValidator.initConfig({
			formid : "form1",
			onerror : function(msg, obj, errorlist) {
				alert(msg);
			}
		});
		$("#optid").formValidator().inputValidator({
			min : 1,
			max : 16,
			onerror : "业务代码请输入1到16个字符"
		}).regexValidator({
			regexp : "username",
			datatype : "enum",
			onerror : "输入字母或者数字"
		});
		$("#optname").formValidator().inputValidator({
			min : 1,
			max : 32,
			onerror : "输入1到16个字符"
		});

	});
	function changetype() {
		var opttype = $("#opttypeW").attr("checked");
		if (opttype == "checked") {
			$("#wfcode").show();
		} else {
			$("#wfcode").hide();
		}
	} */
</script>

<div class="pageContent">
	<form method="post" action="${pageContext.request.contextPath }/sys/optInfo!save.do" class="pageForm required-validate" onsubmit="return validateCallback(this, dialogAjaxDone);">
        <input type="hidden" name="tabid" value="${param['tabid']}"/>
		<s:hidden property="roid" />
		<c:if test="${not empty object.optid }">
			<input type="hidden" name="optid" value="${object.optid }"/>
		</c:if>
        
        <div class="pageFormContent" layoutH="60">
			<p>
				<label class="required">业务代码：</label> 
				<c:if test="${not empty object.optid }">
						<label>${object.optid }</label>
				</c:if> 
				<c:if test="${ empty object.optid }">
						<s:textfield name="optid" id="optid" size="30" cssClass="required"/>
					 <span id="optidTip"></span>
			    </c:if>
			</p>
						
			<%-- <p><c:if test="${not empty optid }">
						<s:textfield name="optid" readonly="true" cssClass="required"/>
					</c:if> <c:if test="${ empty optid }">
						<s:textfield name="optid" id="optid" cssClass="required"/>
					</c:if> <span id="optidTip"></span></p>--%>
			<p> 
				<label>父类业务代码:</label>
				<s:textfield name="preoptid" size="30" cssClass="required"/>
			</p>
			<p>
				<label>业务名称</label>
				<s:textfield name="optname" id="optname" size="30" cssClass="required"/> <span id="optnameTip"></span></td>
			</p>
			<p style="width:100%">
				<label>业务类别</label>
				<input type="radio" id="opttype" name="opttype" onclick="changetype();" value="M" <c:if test="${'M' eq object.opttype || empty object.optid}"> checked="checked" </c:if>>系统管理</input> 
				<input type="radio" id="opttype" name="opttype" onclick="changetype();" value="S" <c:if test="${'S' eq object.opttype}"> checked="checked" </c:if>>系统业务</input> 
				<input type="radio" id="opttype" name="opttype" onclick="changetype();" value="N" <c:if test="${'N' eq object.opttype}"> checked="checked" </c:if>>普通业务</input>
				<input type="radio" id="opttype" name="opttype" onclick="changetype();" value="W" <c:if test="${'W' eq object.opttype}"> checked="checked" </c:if>>流程业务</input> 
				<input type="radio" id="opttype" name="opttype" onclick="changetype();" value="E" <c:if test="${'E' eq object.opttype}"> checked="checked" </c:if>>外部业务</input> 
				<input type="radio" id="opttype" name="opttype" onclick="changetype();" value="H" <c:if test="${'H' eq object.opttype}"> checked="checked" </c:if>>首页面模块</input>
				
				<%-- <s:radio id="opttype" name="opttype" onclick="changetype();" list="#{'M':'系统管理','S':'系统业务','N':'普通业务','W':'流程业务','E':'外部业务','H':'首页面模块'}"></s:radio> --%>
			</p>
			<p>
				<label>业务URL</label>
				<s:textfield name="opturl" id="opturl" size="30" />
			</p>
			<p style="width:100%">
				<label>是否放在菜单栏</label>				
				<input type="radio" name="isintoolbar" value="Y" <c:if test="${'Y' eq object.isintoolbar || empty object.optid}"> checked="checked" </c:if>>是</input>
				<input type="radio" name="isintoolbar" value="N" <c:if test="${'N' eq object.isintoolbar}"> checked="checked" </c:if>>否</input>
				<%-- <s:radio name="isintoolbar" list="#{'Y':'是','N':'否'}"></s:radio> --%>
			</p>
			<p style="width:100%">
				<label>打开方式</label>				
				<input type="radio" name="pageType" value="D" <c:if test="${'D' eq object.pageType || empty object.optid}"> checked="checked" </c:if>>Div</input>
				<input type="radio" name="pageType" value="F" <c:if test="${'F' eq object.pageType}"> checked="checked" </c:if>>iframe</input>
				<%-- <s:radio name="pageType" list="#{'D':'Div','F':'iframe'}"></s:radio> --%>
			</p>

			<p>
				<label>业务排序号</label>
				<s:textfield name="orderind" size="30" />
			</p>
			<c:if test="${opttype=='W'}">
				<p>
					<label>流程代码</label>
					<s:textfield name="wfcode" size="30" />
				</p>
			</c:if>
			<c:if test="${opttype!='W'}">
				<p id="wfcode" style="display: none">
					<label>流程代码</label>
					<s:textfield name="wfcode" size="30" />
				</p>
			</c:if>
</div>

		<div class="formBar">
			<ul>
				<!--<li><a class="buttonActive" href="javascript:;"><span>保存</span></a></li>-->
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
	</form>
</div>
