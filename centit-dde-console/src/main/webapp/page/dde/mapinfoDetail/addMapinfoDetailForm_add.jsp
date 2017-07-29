<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/page/common/taglibs.jsp"%>



<div class="pageContent">
	<form action="${pageContext.request.contextPath}/dde/mapInfoDetail!addAndsaveMapinfoDatails.do?s_method=save&s_type=save"
		class="pageForm required-validate"
		onsubmit="return validateCallback(this, dialogAjaxDone);">

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
				   <option value="1">是</option>
				   <option value="0">否</option>
				</select>
				<%-- <input name="isPk"  size="40" type="text" class="isPk"
					value="${object.isPk }" /> --%>
			</div>
			<div class="unit">
				<label>是否允许空：</label>
				<select class="combox isNull" name="isNull" >
				   <option value="1">是</option>
				   <option value="0">否</option>
				</select> 
				<%-- <input name="isNull" type="text" size="40" class="isNull"
					value="${object.isNull }" /> --%>
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
							<button type="button" onclick="addMapinfoDetails();">保存</button>
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
   function addMapinfoDetails(){
	   //debugger;
	   
	   var $mapinfoDetailFormAdd = $('#div_mapinfo_detail_form_add');
	   
	   
	   var sourceFieldName = $(".sourceFieldName").val();
	   var sourceFieldType = $(".sourceFieldType").val();
	   var sourceFieldSentence = $(".sourceFieldSentence").val();
	   var destFieldName = $(".destFieldName").val();
	   var destFieldType = $(".destFieldType").val();
	   var isPk = $("select.isPk").val();
	   var isNull = $("select.isNull").val();
	   var destFieldDefault = $(".destFieldDefault").val();
	   var mapInfoName = $("#mapInfoName").val();
	   mapInfoName = newVal(mapInfoName, $mapinfoDetailFormAdd.find('#mapInfoName').val());
	   
	   if('' == mapInfoName) {
		   DWZ.ajaxDone({
				statusCode : DWZ.statusCode.error,
				message : '请填写交换名称'
			});
		   
		   return;
	   }
	   
	   
	   
	   var isRepeat = $("#isRepeat").val();
	   isRepeat = newVal(isRepeat, $mapinfoDetailFormAdd.find(':radio.isRepeat').val());
	   
	   var mapInfoDesc = $("#mapInfoDesc").val();
	   mapInfoDesc = newVal(mapInfoDesc, $mapinfoDetailFormAdd.find('#mapInfoDesc').text());
	   
	   var tableOperate = $("#tableOperate").val();
	   //tableOperate = newVal(tableOperate, $mapinfoDetailFormAdd.find(':radio.isRepeat').val());
	   
	   var recordOperate = $("#recordOperate").val();
	   recordOperate = newVal(recordOperate, $mapinfoDetailFormAdd.find(':radio.recordOperate').val());
		
	   var mapinfoNameElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'mapInfoName')
		.replaceAll('{value}', mapInfoName);
	   var isRepeatElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'isRepeat')
		.replaceAll('{value}', isRepeat);
	   var mapinfoDescElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'mapInfoDesc')
		.replaceAll('{value}', mapInfoDesc);
	   var tableOperateElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'tableOperate')
		.replaceAll('{value}', tableOperate);
	   var recordOperateElement = DWZ.frag["INPUTHIDDEN"]
		.replaceAll('{name}', 'recordOperate')
		.replaceAll('{value}', recordOperate);
		
		$("#addmapinfoDetailForm").append(mapinfoNameElement);
		$("#addmapinfoDetailForm").append(isRepeatElement);
		$("#addmapinfoDetailForm").append(mapinfoDescElement);
		$("#addmapinfoDetailForm").append(tableOperateElement);
		$("#addmapinfoDetailForm").append(recordOperateElement);
	   
	   var SourceElement = DWZ.frag["ADDMAPINFODETAILSOURCE"]
		.replaceAll('{sourceFieldName}', sourceFieldName)
		.replaceAll('{sourceFieldType}', sourceFieldType)
		.replaceAll('{sourceFieldSentence}', sourceFieldSentence);
	   
	   /* alert(SourceElement); */
	     
	   var GoalElement = DWZ.frag["ADDMAPINFODETAILGOAL"]
		.replaceAll('{destFieldName}', destFieldName)
		.replaceAll('{destFieldType}', destFieldType)
		.replaceAll('{isPk}', isPk)
		.replaceAll('{isNull}', isNull)
		.replaceAll('{destFieldDefault}', destFieldDefault);
	   
	   var sourcetablelength = $(".sourcetable >tr").length;
	   var goaltablelength = $(".goaltable >tr").length;
	   
	   var lastSourceIndex = 0;
	   var lastGoalIndex = 0;
	   for(var i=0;i<sourcetablelength;i++){		
	    if($(".sourcetable>tr").eq(i).find("input").eq(0).val()!=''){
	    	lastSourceIndex++;
	    }
	   }
	     
	   for(var i=0;i<goaltablelength;i++){
		    if($(".goaltable>tr").eq(i).find("input").eq(0).val()!=''){
		    	lastGoalIndex++;
		    }		   
		   }
	   
	   if(lastSourceIndex>lastGoalIndex){
		   $(".sourcetable >tr").eq(lastGoalIndex).before(SourceElement);		   
		   $(".goaltable >tr").eq(lastGoalIndex).before(GoalElement);
	   }else if(lastSourceIndex<lastGoalIndex){
		   $(".sourcetable >tr").eq(lastSourceIndex).before(SourceElement);
		   $(".goaltable >tr").eq(lastSourceIndex).before(GoalElement);
	   }else if(lastSourceIndex=lastGoalIndex){
		   $(".sourcetable >tr").eq(lastSourceIndex-1).after(SourceElement);
		   /* alert(lastSourceIndex);
		   alert($(".sourcetable >tr").eq(lastSourceIndex).html()); */
		   $(".goaltable >tr").eq(lastSourceIndex-1).after(GoalElement);
	   }
	   
	   $.pdialog.close($.pdialog.getCurrent());
	   $("#addmapinfoDetailForm").attr("action","${pageContext.request.contextPath}/dde/mapInfoDetail!addAndsaveMapinfoDatails.do?s_method=save&s_type=add").submit();
}
   
   
   var newVal = function (val, nval) {
	   if('undefined' == typeof val || '' == $.trim(val)) {
		   return nval;
	   }
	   
	   return val;
   }
</script>

