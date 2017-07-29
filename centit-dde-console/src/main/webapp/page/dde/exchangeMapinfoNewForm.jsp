<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<s:form action="/dde/exchangeMapInfo!save.do"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone);">

		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>交换编号：</label> <input name="mapInfoId" class="" type="text"
					size="40" value="${object.mapInfoId }" readonly="readonly"/>
			</div>
			<div class="unit">
				<label>源数据库名：</label> 
				<select class="combox" name="sourceDatabaseName">
				  <c:forEach var="DatabaseNames" items="${DatabaseNames}">
				    <option value="${DatabaseNames}" <c:if test="${object.sourceDatabaseName eq DatabaseNames}">selected=selected</c:if> >${DatabaseNames}</option>
				  </c:forEach>
				</select>
			</div>
			<div class="unit">
				<label>交换名称：</label> <input name="mapInfoName" type="text"
					size="40" value="${object.mapInfoName }" />
			</div>
			<div class="unit">
				<label>数据源Sql语句：</label> <input name="querySql" type="text" size="40"
					value="${object.querySql }" />
			</div>
			<div class="unit">
				<label>目标数据库名：</label>
				<select class="combox" name="destDatabaseName">
				  <c:forEach var="DatabaseNames" items="${DatabaseNames}">
				    <option value="${DatabaseNames}" <c:if test="${object.destDatabaseName eq DatabaseNames}">selected=selected</c:if> >${DatabaseNames}</option>
				  </c:forEach>
				</select> 
			</div>
			<div class="unit">
				<label>是否为重复执行：</label> 
				<select class="combox" name="isRepeat">
				    <option value="1" <c:if test="${object.isRepeat eq '1'}">selected=selected</c:if>>是</option>
				    <option value="0" <c:if test="${object.isRepeat eq '0'}">selected=selected</c:if>>否</option>
				</select>
			</div>
			<div class="unit">
				<label>交换说明：</label> <input name="mapInfoDesc" type="text" size="40"
					value="${object.mapInfoDesc }" />
			</div>
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

