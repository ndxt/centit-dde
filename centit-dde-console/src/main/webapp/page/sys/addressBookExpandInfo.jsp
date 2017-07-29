<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<script>
	$(function(){
		$("#ul_user_def_view :button").click(function(){
			var $a = $("#a_user_def_view");
			
			$a.prop("href", $(this).attr("h"));
			$a.click();
		});
	});
</script>

<div class="pageContent">
	<div class="panel panelDrag" defH="80">
		<h1>用户明细</h1>
		<div>
		
			通讯录ID：
			<s:property value="%{usercode}" />
			&nbsp;&nbsp;&nbsp;&nbsp;
		
			通讯主体姓名：
			<s:property value="%{bodyname}" />
			&nbsp;&nbsp;&nbsp;&nbsp;
		
			电子邮件：
			<s:property value="%{email}" />
			&nbsp;&nbsp;&nbsp;&nbsp;
			

		
			<ul id="ul_user_def_view" class="rightTools">
				<a id="a_user_def_view" target="dialog" width="600" height="400"></a>
				<c:if test="${BODYTYPE eq 'u' ||BODYTYPE eq 'p'||BODYTYPE eq 'o' }">
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="button" h="${pageContext.request.contextPath }/sys/staffcertificate!addCertificate.do?usercode=${usercode}">添加资质证书</button>
						</div>
					</div></li>
				</c:if>
				<c:if test="${BODYTYPE eq 'u' ||BODYTYPE eq 'p'||BODYTYPE eq 'o' }">
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="button" h="${pageContext.request.contextPath }/sys/staffeducation!addStaffeducation.do?usercode=${usercode}">添加教育经历</button>
						</div>
					</div></li>
				</c:if>
				<c:if test="${BODYTYPE eq 'u' ||BODYTYPE eq 'p'||BODYTYPE eq 'o' }">
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="button" h="${pageContext.request.contextPath }/sys/staffwork!addStaffwork.do?usercode=${usercode}">添加职业经历</button>
						</div>
					</div></li>
				</c:if>
				<c:if test="${BODYTYPE eq 's'}">
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="button" h="${pageContext.request.contextPath }/sys/supplierinfo!addSupplierinfo.do?usercode=${usercode}">添加供应商资质信息</button>
						</div>
					</div></li>
				</c:if>
			</ul>

		</div>
	</div>
	<div class="tabs tabsExtra">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
				<c:if test="${BODYTYPE eq 'u' ||BODYTYPE eq 'p'||BODYTYPE eq 'o' }">
					<li><a href="${pageContext.request.contextPath }/sys/staffcertificate!viewStaffcertificate.do?s_usercode=${object.usercode }&amp;ec_p=&amp;ec_crd=" target="ajax" rel="jbsxBox"><span>资质证书</span></a></li>				
					<li><a href="${pageContext.request.contextPath }/sys/staffeducation!viewStaffcertificate.do?s_usercode=${object.usercode }&amp;ec_p=&amp;ec_crd=" target="ajax" rel="jbsxBox"><span>教育经历</span></a></li>
					<li><a href="${pageContext.request.contextPath }/sys/staffwork!viewStaffwork.do?s_usercode=${object.usercode }&amp;ec_p=&amp;ec_crd=" target="ajax" rel="jbsxBox"><span>职业经历</span></a></li>
				</c:if>
				<c:if test="${BODYTYPE eq 's'}">
					<li><a href="${pageContext.request.contextPath }/sys/supplierinfo!viewSupplierinfo.do?s_usercode=${object.usercode }&amp;ec_p=&amp;ec_crd=" target="ajax" rel="jbsxBox"><span>供应商资质信息</span></a></li>
				</c:if>
				</ul>
			</div>
		</div>
		<div class="tabsContentAjax">
			<div class="unitBox" id="jbsxBox">
				<c:if test="${BODYTYPE eq 'u' ||BODYTYPE eq 'p'||BODYTYPE eq 'o' }">
				   <%@ include file="/page/sys/staffcertificateList.jsp"%>
				</c:if>
				<c:if test="${BODYTYPE eq 's'}">
				   <%@ include file="/page/sys/supplierinfoList.jsp"%>
				</c:if>
			</div>
		</div>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>

</div>