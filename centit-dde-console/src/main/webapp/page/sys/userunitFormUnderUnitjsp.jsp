<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>
<%@ include file="/page/common/css.jsp"%> 


<html>
	<head><meta name="decorator" content='${LAYOUT}'/>
		<title>用户机构编辑--
			<c:out value="${cp:MAPVALUE('usercode',userUnit.id.usercode)}" />:
			<c:out value="${cp:MAPVALUE('unitcode',userUnit.id.unitcode)}"/> 
		</title>
	</head>

	<body>
		<fieldset style="padding:10px;">
			<legend class="ctitle" style="width:auto;margin-bottom:10px;">用户机构编辑--
			<c:out value="${cp:MAPVALUE('usercode',userUnit.id.usercode)}" />:
			<c:out value="${cp:MAPVALUE('unitcode',userUnit.id.unitcode)}"/> </legend>
		
		<s:form action="userDef.do" namespace="/sys"  theme="simple" >
			<s:submit method="saveUserUnitUnderUnit" cssClass="btn" value="保存" />
			<input type="button" value="返回" class="btn" onclick="window.history.back()"/>
			
			<table cellpadding="0" cellspacing="0" class="viewTable">

				<tr>
					<td class="addTd">
						用户代码
					</td>
					<td align="left" >
						<s:textfield name="userUnit.id.usercode" value="%{userUnit.id.usercode}" readonly="true" style="width:140px;" theme="simple" /> 
					</td>
				</tr>
				<tr>
					<td class="addTd">
						用户名
					</td>
					<td align="left" >
						<c:out value="${cp:MAPVALUE('usercode',userUnit.id.usercode)}"/> 
					</td>
				</tr>
				<tr>
					<td class="addTd">
						用户机构
					</td>
					<td align="left"> 
					
					
					<select  name="userUnit.id.unitcode" >
				<c:forEach var="row" items="${unitList}">
               		 <option value="${row.unitcode}" 
               		 <c:if test="${row.unitcode==userUnit.id.unitcode}">selected="selected"</c:if> > 
                  		 <c:out value="${row.unitname}"/>
              		 </option>
           			</c:forEach>
						
					</td>
				</tr>
				<tr>
					<td class="addTd">
						是否为主单位
					</td>
					<td align="left" >
					<c:if test="${not empty userUnit.isprimary}">
     					<s:radio name="userUnit.isprimary" list="#{'T':'是','F':'否'}" listKey="key" listValue="value" value="%{userUnit.isprimary}"></s:radio>
     					</c:if>
     				<c:if test="${empty userUnit.isprimary}">
     					<s:radio name="userUnit.isprimary" list="#{'T':'是','F':'否'}" listKey="key" listValue="value" value="'F'"></s:radio>
     				</c:if>
					</td>				</tr>
				<tr>
					<td class="addTd">
						用户岗位
					</td>
					<td align="left" >
					<c:if test="${not empty userUnit.id.userstation }">
						<input type="hidden" name="userUnit.id.userstation" value="${userUnit.id.userstation }">
						<select name="userUnit.id.userstation" disabled="disabled" style="width:140px;">
							<c:forEach var="row" items="${cp:LVB('StationType')}">
               					 <option value="<c:out value='${row.value}'/>" 
               					 <c:if test="${row.value==userUnit.id.userstation}">selected="selected"</c:if>>
                  			 		 <c:out value="${row.label}"/>
              				  </option>
           					</c:forEach>
						</select>
					</c:if>
						
					<c:if test="${empty userUnit.id.userstation }">
						<select name="userUnit.id.userstation" style="width:140px;">
							<c:forEach var="row" items="${cp:LVB('StationType')}">
               					 <option value="<c:out value='${row.value}'/>" 
               					 <c:if test="${row.value==userUnit.id.userstation}">selected="selected"</c:if>>
                  			 		 <c:out value="${row.label}"/>
              				  </option>
           					</c:forEach>
						</select>
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="addTd">
						行政职务
					</td>
					<td align="left" >
					<c:if test="${not empty userUnit.id.userrank}">						
						<input type="hidden" name="userUnit.id.userrank" value="${userUnit.id.userrank }">
						<select name="userUnit.id.userrank" disabled="disabled" style="width:140px;">
							<c:forEach var="row" items="${cp:LVB('RankType')}">
               					 <option value="<c:out value='${row.value}'/>" 
               					 <c:if test="${row.value==userUnit.id.userrank}">selected="selected"</c:if>>
                  			 		 <c:out value="${row.label}"/>
              				  </option>
           					</c:forEach>
						</select>
					</c:if>
					<c:if test="${empty userUnit.id.userrank}">						
						<input type="hidden" name="userrank" value="${userUnit.id.userrank }">
						<select name="userUnit.id.userrank" style="width:140px;">
							<c:forEach var="row" items="${cp:LVB('RankType')}">
               					 <option value="<c:out value='${row.value}'/>" 
               					 <c:if test="${row.value==userUnit.id.userrank}">selected="selected"</c:if>>
                  			 		 <c:out value="${row.label}"/>
              				  </option>
           					</c:forEach>
						</select>
					</c:if>
					</td>
				</tr>				
				<tr>
					<td class="addTd">
						授权说明
					</td>
					<td align="left">
						<s:textarea name="userUnit.rankmemo" style="width:600px;height:50px;" value="%{userUnit.rankmemo}" />
					</td>
				</tr>

			</table>
		</s:form>
		</fieldset>
	</body>
</html>
