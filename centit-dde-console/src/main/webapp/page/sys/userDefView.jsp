<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">
	<div class="panel panelDrag">
		<h1>用户明细</h1>
		<div class="pageFormContent">

			<div class="unit">
				<label>用户代码：</label>
				<label>${object.usercode }</label>
				
				<label>主机构：</label>
				<label>${cp:MAPVALUE('unitcode', userUnit.unitcode) }</label>
			</div>
				

			<div class="unit">
				<label>用户名：</label>
				<label>${object.username }</label>
				
				<label>岗位：</label>
				
				<label>${cp:MAPVALUE('StationType',userUnit.userstation)}</label>
			</div>

			<div class="unit">
				<label>登录名：</label>
				<label>${object.loginname }</label>
				
				<label>行政职务：</label>
				
				<label>${cp:MAPVALUE('RankType',userUnit.userrank)}</label>
			</div>

			<div class="unit">
				<label>用户状态：</label>
				 
				<c:if test="${isvalid=='T' }"><label>启用</label></c:if>
				<c:if test="${isvalid!='T' }"><label>禁用</label></c:if>
				
				<label>用户描述：</label>
				<label>${object.userdesc }</label>
			</div>
		</div>
		
		<div class="subBar">
			<ul>
				<li style="float:right; margin-right:50px;">
					<div class="buttonActive">
						<div class="buttonContent">
							<a href="${pageContext.request.contextPath }/sys/userDef!edit.do?usercode=${object.usercode}&tabid=userDefView" 
								target="dialog" rel="userDefForm" width="480" height="380" title="编辑用户信息">
								编辑用户信息
							</a>
						</div>
					</div>
				
					<div class="buttonActive">
						<div class="buttonContent">
							<button onclick="navTab.closeCurrentTab(<c:choose><c:when test="${not empty param['tabid']}">'${param["tabid"]}'</c:when><c:otherwise>'external_USERMAG'</c:otherwise></c:choose>)">返回</button>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</div>
	
	<div class="tabs tabsExtra" currentIndex="${param['currentIndex']}">
		<div class="tabsHeader">
			<div class="tabsHeaderContent">
				<ul>
					<li>
						<a href="${pageContext.request.contextPath }/sys/userDef!viewUserunits.do?usercode=${object.usercode }"
							target="ajax" rel="userBox" id="userunit_btn">
							<span>用户机构</span>
						</a>
					</li>
					<li>
						<a href="${pageContext.request.contextPath }/sys/userDef!viewUserroles.do?usercode=${object.usercode }"	
							target="ajax" rel="userBox" id="userrole_btn">
							<span>用户角色</span>
						</a>
					</li>
				</ul>
			</div>
		</div>
		<div class="tabsContentAjax">
			<div class="unitBox" id="userBox">
			</div>
		</div>
		<div class="tabsFooter">
			<div class="tabsFooterContent"></div>
		</div>
	</div>
</div>