<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<form action="${pageContext.request.contextPath}/dde/mapInfoDetail!showMapinfoDetail.do?s_method=save&s_type=save"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone);">
        <input type="hidden" name="columnNo" class="columnNo" value="${object.cid.columnNo}"/>
		<div class="pageFormContent" layoutH="58">
			<div class="unit">
				<label>源字段名：</label> <input name="sourceFieldName" class="sourceFieldName" type="text"
					size="40" value="${object.sourceFieldName }" />
			</div>
			<div class="unit">
				<label>字段类型：</label> <input name="sourceFieldType" type="text" class="sourceFieldType"
					size="40" value="${object.sourceFieldType }" />
			</div>
			<div class="unit">
				<label>源字段描述：</label> <input name="sourceFieldSentence" type="text" class="sourceFieldSentence"
					size="40" value="${object.sourceFieldSentence }" />
			</div>
			<div class="unit">
				<label>目标字段名：</label> <input name="destFieldName" type="text" class="destFieldName"
					size="40" value="${object.destFieldName }" />
			</div>
			<div class="unit">
				<label>字段类型：</label> <input name="destFieldType" type="text" size="40" class="destFieldType"
					value="${object.destFieldType }" />
			</div>
			<div class="unit">
				<label>是否为主键：</label>
				<select class="combox isPk" name="isPk" >
				   <option value="1" <c:if test="${object.isPk eq '1'}">selected=selected</c:if>>是</option>
				   <option value="0" <c:if test="${object.isPk eq '0'}">selected=selected</c:if>>否</option>
				</select>
			</div>
			<div class="unit">
				<label>是否允许空：</label>
				<select class="combox isNull" name="isNull" >
				   <option value="1" <c:if test="${object.isNull eq '1'}">selected=selected</c:if>>是</option>
				   <option value="0" <c:if test="${object.isNull eq '0'}">selected=selected</c:if>>否</option>
				</select> 
			</div>
			<div class="unit">
				<label>常量（搞优先级）：</label> <input name="destFieldDefault" type="text" size="40" class="destFieldDefault"
					value="${object.destFieldDefault }" />
			</div>
		</div>

		<div class="formBar">
			<ul>
				<li><div class="buttonActive">
						<div class="buttonContent">
							<button type="button" onclick="editMapinfoDetails();">保存</button>
						</div>
					</div></li>
				<li>
					<div class="button">
						<div class="buttonContent">
							<button type="button" class="close" >取消</button>
						</div>
					</div>
				</li>
			</ul>
		</div>

	</form>
</div>

<script type="text/javascript">
   function editMapinfoDetails(){
	   var sourceFieldName = $(".sourceFieldName").val();
	   var sourceFieldType = $(".sourceFieldType").val();
	   var sourceFieldSentence = $(".sourceFieldSentence").val();
	   var destFieldName = $(".destFieldName").val();
	   var destFieldType = $(".destFieldType").val();
	   var isPk = $("select.isPk").val();
	   var isNull = $("select.isNull").val();
	   var destFieldDefault = $(".destFieldDefault").val();
	   
	   var columnNo = $("input.columnNo").val()-1;
	   
	   $(".sourcetable>tr").eq(columnNo).find("input.editSourceColumnName").val(sourceFieldName);
	   $(".sourcetable>tr").eq(columnNo).find("input.editSourceColumnType").val(sourceFieldType);
	   $(".sourcetable>tr").eq(columnNo).find("input.editSourceColumnSentence").val(sourceFieldSentence);
	   
	   $(".goaltable>tr").eq(columnNo).find("input.editGoalColumnName").val(destFieldName);
	   $(".goaltable>tr").eq(columnNo).find("input.editGoalColumnType").val(destFieldType);
	   $(".goaltable>tr").eq(columnNo).find("input.editGoalisPk").val(isPk);
	   $(".goaltable>tr").eq(columnNo).find("input.editGoalisNullable").val(isNull);
	   $(".goaltable>tr").eq(columnNo).find("input.editGoalFieldDefault").val(destFieldDefault);
	   
	   var mapInfoName = $("#mapinfoName_edit").val();
		var isRepeat = $(".isRepeat_edit:checked").val();
		var mapInfoDesc = $("#mapinfoDesc_edit").val();
		/* var tableOperate = $("#tableOperate").val(); */
		var recordOperate = $(".recordOperate_edit:checked").val();
		
		var mapinfoNameElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'mapInfoName')
		.replaceAll('{value}', mapInfoName);
		var isRepeatElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'isRepeat')
		.replaceAll('{value}', isRepeat);
		var mapinfoDescElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'mapInfoDesc')
		.replaceAll('{value}', mapInfoDesc);
		var recordOperateElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'recordOperate')
		.replaceAll('{value}', recordOperate);
		
		$("#mapinfoDetailForm").append(mapinfoNameElement);
		$("#mapinfoDetailForm").append(isRepeatElement);
		$("#mapinfoDetailForm").append(mapinfoDescElement);
		$("#mapinfoDetailForm").append(recordOperateElement);
		
	   
	   $.pdialog.close($.pdialog.getCurrent());
	   $("#mapinfoDetailForm").attr("action","${pageContext.request.contextPath}/dde/mapInfoDetail!showMapinfoDetail.do?s_method=save&s_type=save").submit();
}
</script>

