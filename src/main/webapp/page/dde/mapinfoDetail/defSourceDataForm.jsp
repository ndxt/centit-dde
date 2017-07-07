<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>

<div class="pageContent">

	<form id="defSourceDataFrom" class="defSourceDataFrom"
		onsubmit="return navTabSearch(this)"
		action="${pageContext.request.contextPath}/dde/mapInfoDetail!showMapinfoDetail.do?&s_type=reinitsource&s_method=updateSourceColumnSentence"
		method="post">
	<input type="hidden" name="s_goalUrl" value="${GOALURL}"/>
	<input type="hidden" name="s_goalTableName" value="${param['s_goalTableName']}"/>
	<input type="hidden" name="s_mapinfoId" value="${param['s_mapinfoId']}"/>
		<div class="pageFormContent" layoutH="58">

			<div class="unit">
				<label>数据库连接：</label>
				<select  name="s_sourcedatabaseName" ref="tableNames"
						refUrl="${pageContext.request.contextPath }/dde/mapInfoDetail!database.do?databaseName={value}">
						<option value="all">请选择数据库</option>
				    <c:forEach var="database" items="${DATABASE}">
      				  <option value="${database}" <c:if test="${sourceDatabaseName eq database }">selected=selected</c:if> >${database}</option>
      			   </c:forEach>
				</select>
			</div>
			
			<div class="unit">
				<label>表名：</label>
				<input name="s_soueceTableName" size="20"  value="${param['s_soueceTableName']}" type="text" Class="required  sourceTableName" />
				<%-- <label style="width:60px">请选择:</label>
				<select class="table" name="s_soueceTableName1" id="tableNames" disabled="disabled">
     				  <option value="all" >请选择表名</option>
     			   <c:forEach var="tables" items="${TABLES}">
      				  <option value="${tables}" <c:if test="${sourceDatabaseName eq database }">selected=selected</c:if>>${tables}</option>
      			   </c:forEach>
				</select> --%>
			</div>
	 
			<div class="unit">
				<label>查询语句：</label>
				<textarea name="s_querySql" cols="44" rows="5" >${SQL}</textarea>
				<input type="hidden" name="s_querySqlsource" value="${SQL}"/>
			</div>
				
		</div>

		<div class="formBar">
			<ul>
				<li>
					<div class="buttonActive">
						<div class="buttonContent">
							<button type="button" onclick="submitMapInfo();">提交</button>
						</div>
					</div>
				</li>

				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="button" class="close">取消</button>
						</div>
					</div></li>
			</ul>
		</div>
	</form>
</div>

<script type="text/javascript">
	  function submitMapInfo(){
		  $("#defSourceDataFrom").submit();
		  $.pdialog.close($.pdialog.getCurrent());
	  }
	  $('.table', $.pdialog.getCurrent()).change(function(event){
		  $(".datasource").val($(this).val());
		  $(".sourceTableName").val($(this).val());
		  $("#defSourceDataFrom").attr("action","${pageContext.request.contextPath}/dde/mapInfoDetail!showMapinfoDetail.do?s_type=reinitsource&s_method=updateSourceColumnSentence");
		  		  
	  });
	  
      function analysisSql(sql){
    	  
      }
	  
</script>