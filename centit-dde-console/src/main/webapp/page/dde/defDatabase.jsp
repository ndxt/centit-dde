<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<form id="defSourceDatabase" action="${pageContext.request.contextPath }/dde/databaseInfo?save.do"  onsubmit="return navTabSearch(this)" method="post">
	<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>数据库名：</label>
				<input name="databaseName" class="" type="text" size="40" value="${object.databaseName }"/>
			</div>
			<div class="unit">
				<label>数据库类型：</label>
				<input name="databaseType" type="text" size="40" value="${object.databaseType }"/>
			</div>
			<div class="unit">
				<label>数据库连接url：</label>
				<input name="databaseUrl" type="text" size="40" value="${object.databaseUrl }"/>
			</div>
			<div class="unit">
				<label>用户名：</label>
				<input name="username" type="text"  size="40" value="${object.username }"/>	
			</div>
			<div class="unit">
				<label>密码：</label>
				<input name="password" type="text"  size="40" value="${object.password }"/>
			</div>
			<div class="unit">
				<label>数据内容描述：</label>
				<input name="dataDesc" type="text"  size="40" value="${object.dataDesc }"/>
			</div>
			
		</div>
			
		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="submit" >保存</button>
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
