var havingDetailDesGobal;
var havingDetailCodeGobal;
function sqlHavings(obj){
	debugger;
	$("#havingDetailCodeDiv").attr("value",$(obj).find("input[name='groupFieldExpressionCode1']").val());
	$("#havingDetailDesDiv").attr("value",$(obj).find("input[name='groupFieldExpressionDes1']").val());
	
	$("#_parentTable").attr("value",$(obj).find("input[name='parentTable_table1']").val());
	
	havingDetailDesGobal = $("#havingDetailDesDiv").val();
	havingDetailCodeGobal = $("#havingDetailCodeDiv").val();
	var CT_RESULT = $(obj).find("input[name='CT_RESULT_HAVING_TABLE']").val();
	$("#HAVING_CT_RESULT").attr("value",CT_RESULT);
	
	var havingDealMethod_CTNONE = DWZ.frag["filterDealMethod_CTNONE"];
	var havingDetailLogic_CTNONE = DWZ.frag["filterDetailLogic_CTNONE"];
	if(CT_RESULT=='CT_NUM'){
		var havingDealMethod_CTNUM = DWZ.frag["filterDealMethod_CTNUM"];
		var havingDetailLogic_CTNUM = DWZ.frag["filterDetailLogic_CTNUM"];
		$("#havingDealMethodDiv").empty();
		$("#havingDealMethodDiv").append(havingDealMethod_CTNUM);
		$("#havingDealMethodDiv").append(havingDealMethod_CTNONE);
		
		$("#havingDetailLogicDiv").empty();
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTNUM);
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTNONE);
	}else if(CT_RESULT=='CT_CHAR'){
		var havingDealMethod_CTCHAR = DWZ.frag["filterDealMethod_CTCHAR"];
		var havingDetailLogic_CTCHAR = DWZ.frag["filterDetailLogic_CTCHAR"];
		$("#havingDealMethodDiv").empty();
		$("#havingDealMethodDiv").append(havingDealMethod_CTCHAR);
		$("#havingDealMethodDiv").append(havingDealMethod_CTNONE);
		
		$("#havingDetailLogicDiv").empty();
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTCHAR);
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTNONE);
	}else if(CT_RESULT=='CT_STRING'){
		var havingDealMethod_CTSTRING = DWZ.frag["filterDealMethod_CTSTRING"];
		var havingDetailLogic_CTSTRING = DWZ.frag["filterDetailLogic_CTSTRING"];
		$("#havingDealMethodDiv").empty();
		$("#havingDealMethodDiv").append(havingDealMethod_CTSTRING);
		$("#havingDealMethodDiv").append(havingDealMethod_CTNONE);
		
		$("#havingDetailLogicDiv").empty();
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTSTRING);
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTNONE);
	}else if(CT_RESULT=='CT_DATE'){
		var havingDealMethod_CTDATE = DWZ.frag["filterDealMethod_CTDATE"];
		var havingDetailLogic_CTDATE = DWZ.frag["filterDetailLogic_CTDATE"];
		$("#havingDealMethodDiv").empty();
		$("#havingDealMethodDiv").append(havingDealMethod_CTDATE);
		$("#havingDealMethodDiv").append(havingDealMethod_CTNONE);
		
		$("#havingDetailLogicDiv").empty();
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTDATE);
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTNONE);
	}else if(CT_RESULT=='CT_NONE'){
		$("#havingDealMethodDiv").empty();
		$("#havingDealMethodDiv").append(havingDealMethod_CTNONE);
		
		$("#havingDetailLogicDiv").empty();
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTNONE);
	}
	
	
		$("#currentTimeCheckHaving").css("display","none");
		$("#currentTimeHaving").css("display","none");
}

function havingDealMethodChange(){
	var value = $("#havingDealMethodDiv option:selected").val();
	if(value=='abs'||value=='ave'
	    ||value=='max'||value=='min'
	    ||value=='upper'||value=='lower'
	    ||value=='ltrim'||value=='rtrim'
	    ||value=='length'||value=='year'
	    ||value=='month'||value=='day'
	    ||value=='trunc'||value=='sum'
	    ||value=='count'){
		$("#F_havingDealMethodPARAMETER1").hide();
		$("#havingDealMethodPARAMETER1").hide();
		$("#F_havingDealMethodPARAMETER2").hide();
		$("#havingDealMethodPARAMETER2").hide();
		$("#havingDetailCodeDiv").attr("value",value+'('+havingDetailCodeGobal+')');
	}else if(value=='+'||value=='-'||value=='*'||value=='/'){
		$("#havingDetailCodeDiv").attr("value",havingDetailCodeGobal+value);
		$("#F_havingDealMethodPARAMETER1").show();
		$("#havingDealMethodPARAMETER1").show();
	}else if(value=='substr'){
		$("#havingDetailCodeDiv").attr("value",value+'('+havingDetailCodeGobal+',,)');
		$("#F_havingDealMethodPARAMETER1").show();
		$("#havingDealMethodPARAMETER1").show();
		$("#F_havingDealMethodPARAMETER2").show();
		$("#havingDealMethodPARAMETER2").show();
	}else if(value=='trim'){
		$("#F_havingDealMethodPARAMETER1").hide();
		$("#havingDealMethodPARAMETER1").hide();
		$("#F_havingDealMethodPARAMETER2").hide();
		$("#havingDealMethodPARAMETER2").hide();
		$("#havingDetailCodeDiv").attr("value",'rtrim(ltrim('+havingDetailCodeGobal+'))');
	}else if(value=='dcount'){
		$("#F_havingDealMethodPARAMETER1").hide();
		$("#havingDealMethodPARAMETER1").hide();
		$("#F_havingDealMethodPARAMETER2").hide();
		$("#havingDealMethodPARAMETER2").hide();
		$("#havingDetailCodeDiv").attr("value",'count(distinct '+havingDetailCodeGobal+')');
	}
	havingDetailDesChangeByDealMethod();
}


function havingDetailDesChangeByDealMethod(){
	var value = $("#havingDealMethodDiv option:selected").val();
	if(value=='+'||value=='-'||value=='*'
		||value=='/'){
		$("#havingDetailDesDiv").attr("value",havingDetailDesGobal+methodMap.get(value));
	}else if(value=='abs'||value=='max'||value=='min'){
		$("#havingDetailDesDiv").attr("value","取"+havingDetailDesGobal+"的"+methodMap.get(value));
	}else if(value=='sum'||value=='ave'
		     ||value=='trunc'){
		$("#havingDetailDesDiv").attr("value","对"+havingDetailDesGobal+methodMap.get(value));
	}else if(value=='upper'||value=='lower'
		     ||value=='ltrim'||value=='rtrim'
		     ||value=='trim'){
		$("#havingDetailDesDiv").attr("value",methodMap.get(value)+havingDetailDesGobal);
	}else if(value=='year'||value=='month'
		     ||value=='day'||value=='count'
		     ||value=='dcount'||value=='length'){
		$("#havingDetailDesDiv").attr("value",havingDetailDesGobal+"的"+methodMap.get(value));
	}
}

function dealHavingParameter1(){
	var value = $("#havingDealMethodDiv option:selected").val();
	var parameter1 = $("#havingDealMethodPARAMETER1").val();
	if(value=='+'||value=='-'||value=='*'||value=='/'){		
		var havingDealMethodPARAMETER1 = $("#havingDealMethodPARAMETER1").val();
		$("#havingDetailCodeDiv").attr("value",havingDetailCodeGobal+value+havingDealMethodPARAMETER1);
		$("#havingDetailDesDiv").attr("value",havingDetailDesGobal+methodMap.get(value)+parameter1);
	}else if(value = 'substr'){
		var havingDealMethodPARAMETER1 = $("#havingDealMethodPARAMETER1").val();
		$("#havingDetailCodeDiv").attr("value",value+'('+havingDetailCodeGobal+','+havingDealMethodPARAMETER1+',)');
		$("#havingDetailDesDiv").attr("value","从"+havingDetailDesGobal+"第"+havingDealMethodPARAMETER1+"字符取   个字符");
		dealHavingParameter2();
	}
}

function dealHavingParameter2(){
	var value = $("#havingDealMethodDiv option:selected").val();
	var havingDealMethodPARAMETER1 = $("#havingDealMethodPARAMETER1").val();
	var havingDealMethodPARAMETER2 = $("#havingDealMethodPARAMETER2").val();
	$("#havingDetailCodeDiv").attr("value",value+'('+havingDetailCodeGobal+','
			                                     +havingDealMethodPARAMETER1+','
			                                     +havingDealMethodPARAMETER2+')');
	$("#havingDetailDesDiv").attr("value","从"+havingDetailDesGobal+"第"+havingDealMethodPARAMETER1+"字符取"+havingDealMethodPARAMETER2+"个字符");
}


//添加HavingFilter到列表前操作
function addSQLHavingsBefore(){
	var havingDetailNumber = $("#havingDetailNumberDiv").val();
	var havingDetailLogic = $("#havingDetailLogicDiv option:selected").val();
	var havingDetailDes = $("#havingDetailDesDiv").val();
	var HAVING_CT_RESULT = $("#HAVING_CT_RESULT").val();
	
	if(HAVING_CT_RESULT=='CT_CHAR'||HAVING_CT_RESULT=='CT_STRING'){
		$("#havingDetailNumberTemp").val("'"+havingDetailNumber+"'");
	}else if(HAVING_CT_RESULT=='CT_NUM'){
		$("#havingDetailNumberTemp").val(havingDetailNumber);
	}else if(HAVING_CT_RESULT=='CT_DATE'||HAVING_CT_RESULT=='CT_TIME'){
		if(havingDetailNumber=='sysdate'){
			$("#havingDetailNumberTemp").val(havingDetailNumber);
		}else{		
			$("#havingDetailNumberTemp").val("to_date('"+havingDetailNumber+"','yyyy-mm-dd')");
		}
	}
	
	$("#havingLogicExpressionDesDiv").val(havingDetailDes+logicMap.get(havingDetailLogic)+havingDetailNumber);
	if(havingDetailLogic=='<;'){
		$("#havingLogicExpressionDiv").val($("#havingDetailCodeDiv").val()+"&lt;"+$("#havingDetailNumberTemp").val());
	}else{		
		$("#havingLogicExpressionDiv").val($("#havingDetailCodeDiv").val()+havingDetailLogic+$("#havingDetailNumberTemp").val());
	}
	
}

function addSQLHavings(){
	
	addSQLHavingsBefore();
	var havingDetailCode = $("#havingDetailCodeDiv").val();
	var havingDetailDes = $("#havingDetailDesDiv").val();
	var havingDetailLogic = $("#havingDetailLogicDiv option:selected").val();
	var havingDealMethod = $("#havingDealMethodDiv option:selected").val();
	var havingDetailNumber = $("#havingDetailNumberDiv").val();
	var havingLogicExpression = $("#havingLogicExpressionDiv").val();
	var havingLogicExpressionDes = $("#havingLogicExpressionDesDiv").val();
	var sqlHavingsTableLength = $("#sqlHavingTbody >tr").length;
	var NO = sqlHavingsTableLength+1;
	var _parentTable = $("#_parentTable").val();
	var HAVING_CT_RESULT = $("#HAVING_CT_RESULT").val();
	
	if($("#NOHaving").val()!=''){
		alertMsg.error("这是更改的分组过滤，不是更改的新增内容，请点击“更改”按钮");
		return false;
	}
	
	if(havingDetailLogic!='is null'&&havingDetailLogic!='is not null'&&havingDetailLogic!='bjz'){
		if(havingDetailNumber==''){			
			alertMsg.error("请填写数值！");
			return false;
		}else{		
			$("#havingLogicExpressionDesDiv").attr("value",havingDetailDes+logicMap.get(havingDetailLogic)+havingDetailNumber);
		}
	}else{
		$("#havingLogicExpressionDesDiv").attr("value",havingDetailDes+logicMap.get(havingDetailLogic));
	}
	
	var sqlHavingsElement = DWZ.frag["ADDSQLHAVING"]
	.replaceAll('{NO}', NO)
	.replaceAll('{havingDetailCode}', havingDetailCode)
	.replaceAll('{havingDetailLogic}', logicMap.get(havingDetailLogic)+'('+havingDetailLogic+')')
	.replaceAll('{havingDetailNumber}', havingDetailNumber)
	.replaceAll('{havingLogicExpression}', havingLogicExpression)
	.replaceAll('{havingLogicExpressionDes}', havingLogicExpressionDes)
	.replaceAll('{CT_RESULT_TABLE_HAVING}',HAVING_CT_RESULT)
	.replaceAll('{havingDetailCodeGobal}',havingDetailCodeGobal)
	.replaceAll('{havingDetailDesGobal}',havingDetailDesGobal)
	.replaceAll('{havingDealMethod}',havingDealMethod)
	.replaceAll('{parentTable_table}',_parentTable);
	
	if(sqlHavingsTableLength==0){
		$("#sqlHavingTbody").append(sqlHavingsElement);
	}else{
		$("#sqlHavingTbody >tr").eq(sqlHavingsTableLength-1).after(sqlHavingsElement);
	}
	$("#sqlHavingTable").cssTable();
	resetNO();
	createHavingLogicExpressionTotal();
	getSQL(_parentTable);
}

function deleteHaving(obj){
	var _parentTable = $("#_parentTable").val();
	$(obj).parent().parent().remove();
	createHavingLogicExpressionTotal();
	getSQL(_parentTable);
}

function deleteHavingByCode(code){
	debugger;
	var _parentTable = $("#_parentTable").val();
	for(var i=0;i<$("#sqlHavingTbody tr").size();i++){
		if(code==$($("#sqlHavingTbody tr")[i]).find("input[name='havingDetailCode']").val()){
			$($("#sqlHavingTbody tr")[0]).remove();
			createHavingLogicExpressionTotal();
			getSQL(_parentTable);
		}		
	}
}

function editHaving(obj){
	var templeHtml = $(obj).parent().parent();
	var havingDetailCode = templeHtml.find("td > input[name='havingDetailCode']").val();
	var havingDetailDes = templeHtml.find("td > input[name='havingDetailDes']").val();
	var havingDetailLogic = templeHtml.find("td > input[name='havingDetailLogic']").val();
	var havingDealMethod = templeHtml.find("td > input[name='havingDealMethod']").val();
	var havingDetailNumber = templeHtml.find("td > input[name='havingDetailNumber']").val();
	var havingLogicExpression = templeHtml.find("td > input[name='havingLogicExpression']").val();
	var havingLogicExpressionDes = templeHtml.find("td > input[name='havingLogicExpressionDes']").val();
	var NO = templeHtml.find("td:first").text();
	var parentTable_table = templeHtml.find("td > input[name='parentTable_table2']").val();
	var HAVING_CT_RESULT = templeHtml.find("td > input[name='CT_RESULT_TABLE_HAVING']").val();
	var havingDetailDesGobalTemp = templeHtml.find("td > input[name='havingDetailDesGobal']").val();
	var havingDetailCodeGobalTemp = templeHtml.find("td > input[name='havingDetailCodeGobal']").val();
	havingDetailDesGobal = havingDetailDesGobalTemp;
	havingDetailCodeGobal = havingDetailCodeGobalTemp;
	
	$("#havingDetailCodeDiv").attr("value",havingDetailCode);
	$("#havingDetailDesDiv").attr("value",havingDetailDes);
	$("#havingDetailLogicDiv").attr("value",havingDetailLogic);
	$("#havingDealMethodDiv").attr("value",havingDealMethod);
	$("#havingDetailNumberDiv").attr("value",havingDetailNumber);
	$("#havingLogicExpressionDiv").attr("value",havingLogicExpression);
	$("#havingLogicExpressionDesDiv").attr("value",havingLogicExpressionDes);
	$("#HAVING_CT_RESULT").attr("value",HAVING_CT_RESULT);
	$("#NOHaving").attr("value",NO);
	$("#_parentTable").attr("value",parentTable_table);
	
	var havingDealMethod_CTNONE = DWZ.frag["filterDealMethod_CTNONE"];
	var havingDetailLogic_CTNONE = DWZ.frag["filterDetailLogic_CTNONE"];

	if(HAVING_CT_RESULT=='CT_NUM'){
		var havingDealMethod_CTNUM = DWZ.frag["filterDealMethod_CTNUM"];
		var havingDetailLogic_CTNUM = DWZ.frag["filterDetailLogic_CTNUM"];
		$("#havingDealMethodDiv").empty();
		$("#havingDealMethodDiv").append(havingDealMethod_CTNUM);
		$("#havingDealMethodDiv").append(havingDealMethod_CTNONE);
		$("#HAVING_CT_RESULT").attr("value",HAVING_CT_RESULT);
		$("#havingDetailLogicDiv").empty();
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTNUM);
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTNONE);
	}else if(HAVING_CT_RESULT=='CT_CHAR'){
		var havingDealMethod_CTCHAR = DWZ.frag["filterDealMethod_CTCHAR"];
		var havingDetailLogic_CTCHAR = DWZ.frag["filterDetailLogic_CTCHAR"];
		$("#havingDealMethodDiv").empty();
		$("#havingDealMethodDiv").append(havingDealMethod_CTCHAR);
		$("#havingDealMethodDiv").append(havingDealMethod_CTNONE);
		$("#HAVING_CT_RESULT").attr("value",HAVING_CT_RESULT);
		$("#havingDetailLogicDiv").empty();
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTCHAR);
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTNONE);
	}else if(HAVING_CT_RESULT=='CT_STRING'){
		var havingDealMethod_CTSTRING = DWZ.frag["filterDealMethod_CTSTRING"];
		var havingDetailLogic_CTSTRING = DWZ.frag["filterDetailLogic_CTSTRING"];
		$("#havingDealMethodDiv").empty();
		$("#havingDealMethodDiv").append(havingDealMethod_CTSTRING);
		$("#havingDealMethodDiv").append(havingDealMethod_CTNONE);
		$("#HAVING_CT_RESULT").attr("value",HAVING_CT_RESULT);
		$("#havingDetailLogicDiv").empty();
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTSTRING);
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTNONE);
	}else if(HAVING_CT_RESULT=='CT_DATE'){
		var havingDealMethod_CTDATE = DWZ.frag["filterDealMethod_CTDATE"];
		var havingDetailLogic_CTDATE = DWZ.frag["filterDetailLogic_CTDATE"];
		$("#havingDealMethodDiv").empty();
		$("#havingDealMethodDiv").append(havingDealMethod_CTDATE);
		$("#havingDealMethodDiv").append(havingDealMethod_CTNONE);
		$("#HAVING_CT_RESULT").attr("value",HAVING_CT_RESULT);
		$("#havingDetailLogicDiv").empty();
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTDATE);
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTNONE);
	}else if(HAVING_CT_RESULT=='CT_NONE'){
		$("#havingDealMethodDiv").empty();
		$("#havingDealMethodDiv").append(havingDealMethod_CTNONE);
		$("#HAVING_CT_RESULT").attr("value",HAVING_CT_RESULT);
		$("#havingDetailLogicDiv").empty();
		$("#havingDetailLogicDiv").append(havingDetailLogic_CTNONE);
	}
	deleteHaving(obj);
	
}

function changeSQLHavings(){
	addSQLHavingsBefore();
	var havingDetailCode = $("#havingDetailCodeDiv").val();
	var havingDetailDes = $("#havingDetailDesDiv").val();
	var havingDetailLogic = $("#havingDetailLogicDiv option:selected").val();
	var havingDealMethod = $("#havingDealMethodDiv option:selected").val();
	var havingDetailNumber = $("#havingDetailNumberDiv").val();
	var havingLogicExpression = $("#havingLogicExpressionDiv").val();
	var havingLogicExpressionDes = $("#havingLogicExpressionDesDiv").val();
	var _parentTable = $("#_parentTable").val();
	var sqlHavingsTableLength = $("#sqlHavingTbody >tr").length;
	
	var NO = $("#NOHaving").val();
	var HAVING_CT_RESULT = $("#HAVING_CT_RESULT").val();
	
	if($("#NOHaving").val()==''){
		alertMsg.error("这是新增的分组过滤，不是更改的分组过滤，请点击“添加”按钮");
		return false;
	}
	
	if(havingDetailLogic!='is null'&&havingDetailLogic!='is not null'&&havingDetailLogic!='bjz'){
		if(havingDetailNumber==''){			
			alertMsg.error("请填写数值！");
			return false;
		}else{		
			$("#havingLogicExpressionDesDiv").attr("value",havingDetailDes+logicMap.get(havingDetailLogic)+havingDetailNumber);
		}
	}else{
		$("#havingLogicExpressionDesDiv").attr("value",havingDetailDes+logicMap.get(havingDetailLogic));
	}
	
	var sqlHavingsElement = DWZ.frag["ADDSQLHAVING"]
	.replaceAll('{NO}', NO)
	.replaceAll('{havingDetailCode}', havingDetailCode)
	.replaceAll('{havingDetailLogic}', logicMap.get(havingDetailLogic)+'('+havingDetailLogic+')')
	.replaceAll('{havingDetailNumber}', havingDetailNumber)
	.replaceAll('{havingLogicExpression}', havingLogicExpression)
	.replaceAll('{havingLogicExpressionDes}', havingLogicExpressionDes)
	.replaceAll('{CT_RESULT_TABLE_HAVING}',HAVING_CT_RESULT)
	.replaceAll('{havingDetailCodeGobal}',havingDetailCodeGobal)
	.replaceAll('{havingDetailDesGobal}',havingDetailDesGobal)
	.replaceAll('{havingDealMethod}',havingDealMethod)
	.replaceAll('{parentTable_table}',_parentTable);
	
	
	if($("#NOHaving").val()==''){
		alertMsg.error("这是新增的分组过滤，不是更改的分组过滤，请点击“添加”按钮");
		return false;
	}
	
	if(sqlHavingsTableLength==0){
		$("#sqlHavingTbody").append(sqlHavingsElement);
	}else if(NO-1<sqlHavingsTableLength){
		$("#sqlHavingTbody >tr").eq(NO-1).before(sqlHavingsElement);
	} else if(NO-1==sqlHavingsTableLength){
		$("#sqlHavingTbody >tr").eq(NO-2).after(sqlHavingsElement);
	}
	$("#NOHaving").val('');
	
	$("#sqlHavingTable").cssTable();
	
	resetNO();
	createHavingLogicExpressionTotal();	
	getSQL(_parentTable);

}


function showDateElementHaving(){
	var HAVING_CT_RESULT = $("#HAVING_CT_RESULT").val();
	if(HAVING_CT_RESULT=='CT_DATE'||HAVING_CT_RESULT=='CT_TIME'){
		$("#currentTimeCheckHaving").css("display","inline");
		$("#currentTimeHaving").css("display","inline");
	}
}

function getCurrentTimeHaving(){
	if($("input[name='currentTimeCheckHaving']").attr("checked")=='checked'){
		$("#havingDetailNumberDiv").val("sysdate");
	}else{
		$("#havingDetailNumberDiv").val("");
	}
}
function getSelectTimeBeforeHaving(){
	if($("input[name='currentTimeCheckHaving']").attr("checked")!='checked'){		
		$("#calendar").attr("onclick","getSelectTimeHaving();");
	}
}
function getSelectTimeHaving(){
	$("#havingDetailNumberDiv").val($("#currentTime").val());
	$("#calendar").removeAttr("onclick");
}

function createHavingLogicExpressionTotal(){
	$("#havingLogicExpressionTotal").val("");
	var size = $("#sqlHavingTbody").find("tr").size();
	var havingLogicExpressionTotal="";
	for(var i=0;i<size;i++){
		var havingLogicExpressionGobal = $($("#sqlHavingTbody").find("tr")[i]).find("td:first").text();
		if($("#havingLogicExpressionTotal").val()==""){		
			havingLogicExpressionTotal = havingLogicExpressionGobal ;
			$("#havingLogicExpressionTotal").val(havingLogicExpressionTotal);
		}else{
			havingLogicExpressionTotal = havingLogicExpressionTotal +"*"+ havingLogicExpressionGobal ;
			$("#havingLogicExpressionTotal").val(havingLogicExpressionTotal);
		}	
	}
	$("#havingLogicExpressionTotal").val(havingLogicExpressionTotal);	
}


function bingHaving(){
	/*$("#filterLogicExpressionTotal").append("+");*/
	var position = positions($("#havingLogicExpressionTotal")[0],undefined);
	var text = $("#havingLogicExpressionTotal").val();
	var textBefore = text.substring(0,position);
	var textAfter = text.substring(position,text.length);
	var textTotal = textBefore +"*"+textAfter;
	$("#havingLogicExpressionTotal").val(textTotal);
}
function huoHaving(){
	/*$("#havingLogicExpressionTotal").append("+");*/
	var position = positions($("#havingLogicExpressionTotal")[0],undefined);
	var text = $("#havingLogicExpressionTotal").val();
	var textBefore = text.substring(0,position);
	var textAfter = text.substring(position,text.length);
	var textTotal = textBefore +"+"+textAfter;
	$("#havingLogicExpressionTotal").val(textTotal);
}
function zuoHaving(){
	/*$("#havingLogicExpressionTotal").append("+");*/
	var position = positions($("#havingLogicExpressionTotal")[0],undefined);
	var text = $("#havingLogicExpressionTotal").val();
	var textBefore = text.substring(0,position);
	var textAfter = text.substring(position,text.length);
	var textTotal = textBefore +"("+textAfter;
	$("#havingLogicExpressionTotal").val(textTotal);
}
function youHaving(){
	/*$("#havingLogicExpressionTotal").append("+");*/
	var position = positions($("#havingLogicExpressionTotal")[0],undefined);
	var text = $("#havingLogicExpressionTotal").val();
	var textBefore = text.substring(0,position);
	var textAfter = text.substring(position,text.length);
	var textTotal = textBefore +")"+textAfter;
	$("#havingLogicExpressionTotal").val(textTotal);
}
function fanHaving(){
	/*$("#havingLogicExpressionTotal").append("+");*/
	var position = positions($("#havingLogicExpressionTotal")[0],undefined);
	var text = $("#havingLogicExpressionTotal").val();
	var textBefore = text.substring(0,position);
	var textAfter = text.substring(position,text.length);
	var textTotal = textBefore +"!"+textAfter;
	$("#havingLogicExpressionTotal").val(textTotal);
}

function getHavingSQL(){
	
	var havingSQL = '';
	var havingLogicExpressionTotal = $("#havingLogicExpressionTotal").val().split('');
	var sqlHavingTbody = $("#sqlHavingTbody").find("tr");
	
	for(var i=0;i<havingLogicExpressionTotal.length;i++){
		if(havingLogicExpressionTotal[i].isNumber()){
			havingSQL = havingSQL +"("+ $($("#sqlHavingTbody").find("tr")[havingLogicExpressionTotal[i]-1]).find("input[name='havingLogicExpression']").val()+") ";
		}else{
			
			havingSQL = havingSQL + logicExpress.get(havingLogicExpressionTotal[i]) +  " ";
		}
	}
	$("#sqlHavingSQL").val(havingSQL);
	return havingSQL;
	
}


function getHavingsLeft(){
	$("#sqlGroupsTbody1 >tr").remove();
	var sqlContents = $($("#sqlContentTbody")).find("input[name='contentDealMethod']");
	if(!isIncludeAggregate(sqlContents)){
		return false;
	}else{
		var contentWithoutAggregates = getContentWithoutAggregate(sqlContents);
		for(var i=0;i<contentWithoutAggregates.length;i++){			
			var contentWithoutAggregatesCode = $(contentWithoutAggregates[i]).find("input[name='contentFieldExpressionCode']").val();
			var contentWithoutAggregatesDes = $(contentWithoutAggregates[i]).find("input[name='contentFieldExpressionDes']").val();
			var parentTable_table1 = $(contentWithoutAggregates[i]).find("input[name='parentTable_table']").val();
			var CT_RESULT_HAVING_TABLE = $(contentWithoutAggregates[i]).find("input[name='CT_RESULT_TABLE']").val();
			var sqlGroupTableLength = $("#sqlGroupsTbody1 >tr").length;
			
			var sqlGroupElement1 = DWZ.frag["ADDSQLGROUP1"]
			.replaceAll('{groupFieldExpressionDes1}', contentWithoutAggregatesDes)
			.replaceAll('{groupFieldExpressionCode1}', contentWithoutAggregatesCode)
			.replaceAll('{parentTable_table1}', parentTable_table1)
			.replaceAll('{CT_RESULT_HAVING_TABLE}', CT_RESULT_HAVING_TABLE);
			
			if(sqlGroupTableLength==0){
				$("#sqlGroupsTbody1").append(sqlGroupElement1);
			}else{
				$("#sqlGroupsTbody1 >tr").eq(sqlGroupTableLength-1).after(sqlGroupElement1);
			}
			$("#sqlGroupsTable1").cssTable();
		}
	}
}