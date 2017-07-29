var filterDetailDesGobal;
var filterDetailCodeGobal;
var logicMap = new Map();
logicMap.put(">","大于");
logicMap.put("<;","小于");
logicMap.put("=","等于");
logicMap.put(">=","大于等于");
logicMap.put("<=","小于等于");
logicMap.put("<>","不等于");
logicMap.put("in","包含于");
logicMap.put("like","像");
logicMap.put("is null","是空值");
logicMap.put("is not null","非空值");
logicMap.put("bjz","标记值");

var logicExpress = new Map();
logicExpress.put("*", "and");
logicExpress.put("+", "or");
logicExpress.put("(", "(");
logicExpress.put(")", ")");
logicExpress.put("!", "not");


function sqlFilters(treeId, treeNode, clickFlag){
	$("#filterDetailCodeDiv").attr("value",treeNode.tableAlias+"."+treeNode.id);
	$("#filterDetailDesDiv").attr("value",treeNode.name);
	
	$("#_parentTable").attr("value",(treeNode.pId.substring(0,treeNode.pId.length-4)+' '+treeNode.tableAlias));
	filterDetailDesGobal = $("#filterDetailDesDiv").val();
	filterDetailCodeGobal = $("#filterDetailCodeDiv").val();
	
	
	var sTypeDesc;
	sTypeDesc = treeNode.coltype;
	var CT_RESULT;
	var nBPos = sTypeDesc.indexOf('(');
	
	var sTypeH;
	if(nBPos>=0){
		sTypeH = sTypeDesc.substring(0,nBPos);
	}else{
		sTypeH = sTypeDesc;
	}
	
	if((sTypeH)=='INTEGER'
		||(sTypeH)=='NUMBER'
		||(sTypeH)=='NUMERIC'
		||(sTypeH)=='DECIMAL'
		||(sTypeH)=='DOUBLE'
		||(sTypeH)=='FLOAT'
		||(sTypeH)=='BINARY_DOUBLE'
		||(sTypeH)=='BINARY_FLOAT'){
		CT_RESULT = 'CT_NUM';
	}else if((sTypeH)=='CHAR'
		      ||(sTypeH)=='NCHAR'
		      ||(sTypeH)=='CHARACTER'
		      ||(sTypeH)=='NVARCHAR'
		      ||(sTypeH)=='VARCHAR'
		      ||(sTypeH)=='VARCHAR2'){
		if(nBPos>=0){
			var sTypeL;
			sTypeL = sTypeDesc.substring(nBPos+1, sTypeDesc.length-1);
			if(sTypeL>1){
				CT_RESULT = 'CT_STRING';
			}else{
				CT_RESULT = 'CT_CHAR';
			}			
		}else{
			CT_RESULT = 'CT_CHAR';
		}
	}else if((sTypeH)=='DATE'||(sTypeH)=='DATETIME'){
		CT_RESULT = 'CT_DATE';
	}else if((sTypeH)=='TIME'){
		CT_RESULT = 'CT_TIME';
	}else{
		CT_RESULT = 'CT_NONE';
	}
	
	$("#FILTER_CT_RESULT").attr("value",CT_RESULT);
	
	var filterDealMethod_CTNONE = DWZ.frag["filterDealMethod_CTNONE"];
	var filterDetailLogic_CTNONE = DWZ.frag["filterDetailLogic_CTNONE"];
	if(CT_RESULT=='CT_NUM'){
		var filterDealMethod_CTNUM = DWZ.frag["filterDealMethod_CTNUM"];
		var filterDetailLogic_CTNUM = DWZ.frag["filterDetailLogic_CTNUM"];
		$("#filterDealMethodDiv").empty();
		$("#filterDealMethodDiv").append(filterDealMethod_CTNUM);
		$("#filterDealMethodDiv").append(filterDealMethod_CTNONE);
		
		$("#filterDetailLogicDiv").empty();
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTNUM);
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTNONE);
	}else if(CT_RESULT=='CT_CHAR'){
		var filterDealMethod_CTCHAR = DWZ.frag["filterDealMethod_CTCHAR"];
		var filterDetailLogic_CTCHAR = DWZ.frag["filterDetailLogic_CTCHAR"];
		$("#filterDealMethodDiv").empty();
		$("#filterDealMethodDiv").append(filterDealMethod_CTCHAR);
		$("#filterDealMethodDiv").append(filterDealMethod_CTNONE);
		
		$("#filterDetailLogicDiv").empty();
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTCHAR);
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTNONE);
	}else if(CT_RESULT=='CT_STRING'){
		var filterDealMethod_CTSTRING = DWZ.frag["filterDealMethod_CTSTRING"];
		var filterDetailLogic_CTSTRING = DWZ.frag["filterDetailLogic_CTSTRING"];
		$("#filterDealMethodDiv").empty();
		$("#filterDealMethodDiv").append(filterDealMethod_CTSTRING);
		$("#filterDealMethodDiv").append(filterDealMethod_CTNONE);
		
		$("#filterDetailLogicDiv").empty();
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTSTRING);
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTNONE);
	}else if(CT_RESULT=='CT_DATE'){
		var filterDealMethod_CTDATE = DWZ.frag["filterDealMethod_CTDATE"];
		var filterDetailLogic_CTDATE = DWZ.frag["filterDetailLogic_CTDATE"];
		$("#filterDealMethodDiv").empty();
		$("#filterDealMethodDiv").append(filterDealMethod_CTDATE);
		$("#filterDealMethodDiv").append(filterDealMethod_CTNONE);
		
		$("#filterDetailLogicDiv").empty();
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTDATE);
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTNONE);
	}else if(CT_RESULT=='CT_NONE'){
		$("#filterDealMethodDiv").empty();
		$("#filterDealMethodDiv").append(filterDealMethod_CTNONE);
		
		$("#filterDetailLogicDiv").empty();
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTNONE);
	}
	
	
		$("#currentTimeCheck").css("display","none");
		$("#currentTime").css("display","none");
	
}

function filterDealMethodChange(){
	var value = $("#filterDealMethodDiv option:selected").val();
	if(value=='abs'||value=='ave'
	    ||value=='max'||value=='min'
	    ||value=='upper'||value=='lower'
	    ||value=='ltrim'||value=='rtrim'
	    ||value=='length'||value=='year'
	    ||value=='month'||value=='day'
	    ||value=='trunc'||value=='sum'
	    ||value=='count'){
		$("#F_filterDealMethodPARAMETER1").hide();
		$("#filterDealMethodPARAMETER1").hide();
		$("#F_filterDealMethodPARAMETER2").hide();
		$("#filterDealMethodPARAMETER2").hide();
		$("#filterDetailCodeDiv").attr("value",value+'('+filterDetailCodeGobal+')');
	}else if(value=='+'||value=='-'||value=='*'||value=='/'){
		$("#filterDetailCodeDiv").attr("value",filterDetailCodeGobal+value);
		$("#F_filterDealMethodPARAMETER1").show();
		$("#filterDealMethodPARAMETER1").show();
	}else if(value=='substr'){
		$("#filterDetailCodeDiv").attr("value",value+'('+filterDetailCodeGobal+',,)');
		$("#F_filterDealMethodPARAMETER1").show();
		$("#filterDealMethodPARAMETER1").show();
		$("#F_filterDealMethodPARAMETER2").show();
		$("#filterDealMethodPARAMETER2").show();
	}else if(value=='trim'){
		$("#F_filterDealMethodPARAMETER1").hide();
		$("#filterDealMethodPARAMETER1").hide();
		$("#F_filterDealMethodPARAMETER2").hide();
		$("#filterDealMethodPARAMETER2").hide();
		$("#filterDetailCodeDiv").attr("value",'rtrim(ltrim('+filterDetailCodeGobal+'))');
	}else if(value=='dcount'){
		$("#F_filterDealMethodPARAMETER1").hide();
		$("#filterDealMethodPARAMETER1").hide();
		$("#F_filterDealMethodPARAMETER2").hide();
		$("#filterDealMethodPARAMETER2").hide();
		$("#filterDetailCodeDiv").attr("value",'count(distinct '+filterDetailCodeGobal+')');
	}
	filterDetailDesChangeByDealMethod();
}


function filterDetailDesChangeByDealMethod(){
	var value = $("#filterDealMethodDiv option:selected").val();
	if(value=='+'||value=='-'||value=='*'
		||value=='/'){
		$("#filterDetailDesDiv").attr("value",filterDetailDesGobal+methodMap.get(value));
	}else if(value=='abs'||value=='max'||value=='min'){
		$("#filterDetailDesDiv").attr("value","取"+filterDetailDesGobal+"的"+methodMap.get(value));
	}else if(value=='sum'||value=='ave'
		     ||value=='trunc'){
		$("#filterDetailDesDiv").attr("value","对"+filterDetailDesGobal+methodMap.get(value));
	}else if(value=='upper'||value=='lower'
		     ||value=='ltrim'||value=='rtrim'
		     ||value=='trim'){
		$("#filterDetailDesDiv").attr("value",methodMap.get(value)+filterDetailDesGobal);
	}else if(value=='year'||value=='month'
		     ||value=='day'||value=='count'
		     ||value=='dcount'||value=='length'){
		$("#filterDetailDesDiv").attr("value",filterDetailDesGobal+"的"+methodMap.get(value));
	}
}

function dealFilterParameter1(){
	var value = $("#filterDealMethodDiv option:selected").val();
	var parameter1 = $("#filterDealMethodPARAMETER1").val();
	if(value=='+'||value=='-'||value=='*'||value=='/'){		
		var filterDealMethodPARAMETER1 = $("#filterDealMethodPARAMETER1").val();
		$("#filterDetailCodeDiv").attr("value",filterDetailCodeGobal+value+filterDealMethodPARAMETER1);
		$("#filterDetailDesDiv").attr("value",filterDetailDesGobal+methodMap.get(value)+parameter1);
	}else if(value = 'substr'){
		var filterDealMethodPARAMETER1 = $("#filterDealMethodPARAMETER1").val();
		$("#filterDetailCodeDiv").attr("value",value+'('+filterDetailCodeGobal+','+filterDealMethodPARAMETER1+',)');
		$("#filterDetailDesDiv").attr("value","从"+filterDetailDesGobal+"第"+filterDealMethodPARAMETER1+"字符取   个字符");
		dealFilterParameter2();
	}
}

function dealFilterParameter2(){
	var value = $("#filterDealMethodDiv option:selected").val();
	var filterDealMethodPARAMETER1 = $("#filterDealMethodPARAMETER1").val();
	var filterDealMethodPARAMETER2 = $("#filterDealMethodPARAMETER2").val();
	$("#filterDetailCodeDiv").attr("value",value+'('+filterDetailCodeGobal+','
			                                     +filterDealMethodPARAMETER1+','
			                                     +filterDealMethodPARAMETER2+')');
	$("#filterDetailDesDiv").attr("value","从"+filterDetailDesGobal+"第"+filterDealMethodPARAMETER1+"字符取"+filterDealMethodPARAMETER2+"个字符");
}


//添加Filter到列表前操作
function addSQLFiltersBefore(){
	var filterDetailNumber = $("#filterDetailNumberDiv").val();
	var filterDetailLogic = $("#filterDetailLogicDiv option:selected").val();
	var filterDetailDes = $("#filterDetailDesDiv").val();
	var FILTER_CT_RESULT = $("#FILTER_CT_RESULT").val();
	
	if(FILTER_CT_RESULT=='CT_CHAR'||FILTER_CT_RESULT=='CT_STRING'){
		$("#filterDetailNumberTemp").val("'"+filterDetailNumber+"'");
	}else if(FILTER_CT_RESULT=='CT_NUM'){
		$("#filterDetailNumberTemp").val(filterDetailNumber);
	}else if(FILTER_CT_RESULT=='CT_DATE'||FILTER_CT_RESULT=='CT_TIME'){
		if(filterDetailNumber=='sysdate'){
			$("#filterDetailNumberTemp").val(filterDetailNumber);
		}else{		
			$("#filterDetailNumberTemp").val("to_date('"+filterDetailNumber+"','yyyy-mm-dd')");
		}
	}
	
	$("#filterLogicExpressionDesDiv").val(filterDetailDes+logicMap.get(filterDetailLogic)+filterDetailNumber);
	if(filterDetailLogic=='<;'){
		$("#filterLogicExpressionDiv").val($("#filterDetailCodeDiv").val()+"&lt;"+$("#filterDetailNumberTemp").val());
	}else{		
		$("#filterLogicExpressionDiv").val($("#filterDetailCodeDiv").val()+filterDetailLogic+$("#filterDetailNumberTemp").val());
	}
	
}

function deleteFilter(obj){
	var _parentTable = $("#_parentTable").val();
	$(obj).parent().parent().remove();
	resetNO();
	createFilterLogicExpressionTotal();
	getTables(_parentTable);
	getSQL(_parentTable);
}

function resetNO(){
	var size = $("#sqlFilterTbody").find("tr").size();
	for(var i=0;i<size;i++){
		$($("#sqlFilterTbody").find("tr")[i]).find("td:first").text(i+1);
	}
}

function editFilter(obj){
	var templeHtml = $(obj).parent().parent();
	var filterDetailCode = templeHtml.find("td > input[name='filterDetailCode']").val();
	var filterDetailDes = templeHtml.find("td > input[name='filterDetailDes']").val();
	var filterDetailLogic = templeHtml.find("td > input[name='filterDetailLogic']").val();
	var filterDealMethod = templeHtml.find("td > input[name='filterDealMethod']").val();
	var filterDetailNumber = templeHtml.find("td > input[name='filterDetailNumber']").val();
	var filterLogicExpression = templeHtml.find("td > input[name='filterLogicExpression']").val();
	var filterLogicExpressionDes = templeHtml.find("td > input[name='filterLogicExpressionDes']").val();
	var NO = templeHtml.find("td:first").text();
	var parentTable_table = templeHtml.find("td > input[name='parentTable_table2']").val();
	var FILTER_CT_RESULT = templeHtml.find("td > input[name='CT_RESULT_TABLE_FILTER']").val();
	var filterDetailDesGobalTemp = templeHtml.find("td > input[name='filterDetailDesGobal']").val();
	var filterDetailCodeGobalTemp = templeHtml.find("td > input[name='filterDetailCodeGobal']").val();
	filterDetailDesGobal = filterDetailDesGobalTemp;
	filterDetailCodeGobal = filterDetailCodeGobalTemp;
	
	$("#filterDetailCodeDiv").attr("value",filterDetailCode);
	$("#filterDetailDesDiv").attr("value",filterDetailDes);
	$("#filterDetailLogicDiv").attr("value",filterDetailLogic);
	$("#filterDealMethodDiv").attr("value",filterDealMethod);
	$("#filterDetailNumberDiv").attr("value",filterDetailNumber);
	$("#filterLogicExpressionDiv").attr("value",filterLogicExpression);
	$("#filterLogicExpressionDesDiv").attr("value",filterLogicExpressionDes);
	$("#FILTER_CT_RESULT").attr("value",FILTER_CT_RESULT);
	$("#NOFilter").attr("value",NO);
	$("#_parentTable").attr("value",parentTable_table);
	
	var filterDealMethod_CTNONE = DWZ.frag["filterDealMethod_CTNONE"];
	var filterDetailLogic_CTNONE = DWZ.frag["filterDetailLogic_CTNONE"];

	if(FILTER_CT_RESULT=='CT_NUM'){
		var filterDealMethod_CTNUM = DWZ.frag["filterDealMethod_CTNUM"];
		var filterDetailLogic_CTNUM = DWZ.frag["filterDetailLogic_CTNUM"];
		$("#filterDealMethodDiv").empty();
		$("#filterDealMethodDiv").append(filterDealMethod_CTNUM);
		$("#filterDealMethodDiv").append(filterDealMethod_CTNONE);
		$("#FILTER_CT_RESULT").attr("value",FILTER_CT_RESULT);
		$("#filterDetailLogicDiv").empty();
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTNUM);
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTNONE);
	}else if(FILTER_CT_RESULT=='CT_CHAR'){
		var filterDealMethod_CTCHAR = DWZ.frag["filterDealMethod_CTCHAR"];
		var filterDetailLogic_CTCHAR = DWZ.frag["filterDetailLogic_CTCHAR"];
		$("#filterDealMethodDiv").empty();
		$("#filterDealMethodDiv").append(filterDealMethod_CTCHAR);
		$("#filterDealMethodDiv").append(filterDealMethod_CTNONE);
		$("#FILTER_CT_RESULT").attr("value",FILTER_CT_RESULT);
		$("#filterDetailLogicDiv").empty();
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTCHAR);
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTNONE);
	}else if(FILTER_CT_RESULT=='CT_STRING'){
		var filterDealMethod_CTSTRING = DWZ.frag["filterDealMethod_CTSTRING"];
		var filterDetailLogic_CTSTRING = DWZ.frag["filterDetailLogic_CTSTRING"];
		$("#filterDealMethodDiv").empty();
		$("#filterDealMethodDiv").append(filterDealMethod_CTSTRING);
		$("#filterDealMethodDiv").append(filterDealMethod_CTNONE);
		$("#FILTER_CT_RESULT").attr("value",FILTER_CT_RESULT);
		$("#filterDetailLogicDiv").empty();
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTSTRING);
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTNONE);
	}else if(FILTER_CT_RESULT=='CT_DATE'){
		var filterDealMethod_CTDATE = DWZ.frag["filterDealMethod_CTDATE"];
		var filterDetailLogic_CTDATE = DWZ.frag["filterDetailLogic_CTDATE"];
		$("#filterDealMethodDiv").empty();
		$("#filterDealMethodDiv").append(filterDealMethod_CTDATE);
		$("#filterDealMethodDiv").append(filterDealMethod_CTNONE);
		$("#FILTER_CT_RESULT").attr("value",FILTER_CT_RESULT);
		$("#filterDetailLogicDiv").empty();
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTDATE);
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTNONE);
	}else if(FILTER_CT_RESULT=='CT_NONE'){
		$("#filterDealMethodDiv").empty();
		$("#filterDealMethodDiv").append(filterDealMethod_CTNONE);
		$("#FILTER_CT_RESULT").attr("value",FILTER_CT_RESULT);
		$("#filterDetailLogicDiv").empty();
		$("#filterDetailLogicDiv").append(filterDetailLogic_CTNONE);
	}
	deleteFilter(obj);
	
}


function changeSQLFilters(){
	addSQLFiltersBefore();
	var filterDetailCode = $("#filterDetailCodeDiv").val();
	var filterDetailDes = $("#filterDetailDesDiv").val();
	var filterDetailLogic = $("#filterDetailLogicDiv option:selected").val();
	var filterDealMethod = $("#filterDealMethodDiv option:selected").val();
	var filterDetailNumber = $("#filterDetailNumberDiv").val();
	var filterLogicExpression = $("#filterLogicExpressionDiv").val();
	var filterLogicExpressionDes = $("#filterLogicExpressionDesDiv").val();
	var _parentTable = $("#_parentTable").val();
	var sqlFiltersTableLength = $("#sqlFilterTbody >tr").length;
	
	var NO = $("#NOFilter").val();
	var FILTER_CT_RESULT = $("#FILTER_CT_RESULT").val();
	
	if($("#NOFilter").val()==''){
		alertMsg.error("这是新增的查询条件，不是更改的查询条件，请点击“添加”按钮");
		return false;
	}
	
	if(filterDetailLogic!='is null'&&filterDetailLogic!='is not null'&&filterDetailLogic!='bjz'){
		if(filterDetailNumber==''){			
			alertMsg.error("请填写数值！");
			return false;
		}else{		
			$("#filterLogicExpressionDesDiv").attr("value",filterDetailDes+logicMap.get(filterDetailLogic)+filterDetailNumber);
		}
	}else{
		$("#filterLogicExpressionDesDiv").attr("value",filterDetailDes+logicMap.get(filterDetailLogic));
	}
	
	var sqlFiltersElement = DWZ.frag["ADDSQLFILTER"]
	.replaceAll('{NO}', NO)
	.replaceAll('{filterDetailCode}', filterDetailCode)
	.replaceAll('{filterDetailLogic}', logicMap.get(filterDetailLogic)+'('+filterDetailLogic+')')
	.replaceAll('{filterDetailNumber}', filterDetailNumber)
	.replaceAll('{filterLogicExpression}', filterLogicExpression)
	.replaceAll('{filterLogicExpressionDes}', filterLogicExpressionDes)
	.replaceAll('{CT_RESULT_TABLE_FILTER}',FILTER_CT_RESULT)
	.replaceAll('{filterDetailCodeGobal}',filterDetailCodeGobal)
	.replaceAll('{filterDetailDesGobal}',filterDetailDesGobal)
	.replaceAll('{filterDealMethod}',filterDealMethod)
	.replaceAll('{parentTable_table}',_parentTable);
	
	
	if($("#NOFilter").val()==''){
		alertMsg.error("这是新增的查询条件，不是更改的查询条件，请点击“添加”按钮");
		return false;
	}
	
	if(sqlFiltersTableLength==0){
		$("#sqlFilterTbody").append(sqlFiltersElement);
	}else if(NO-1<sqlFiltersTableLength){
		$("#sqlFilterTbody >tr").eq(NO-1).before(sqlFiltersElement);
	} else if(NO-1==sqlFiltersTableLength){
		$("#sqlFilterTbody >tr").eq(NO-2).after(sqlFiltersElement);
	}
	$("#NOFilter").val('');
	
	$("#sqlFilterTable").cssTable();
	
	resetNO();
	createFilterLogicExpressionTotal();
	getTables(_parentTable);
	getSQL(_parentTable);

}



function addSQLFilters(){
	
	addSQLFiltersBefore();
	var filterDetailCode = $("#filterDetailCodeDiv").val();
	var filterDetailDes = $("#filterDetailDesDiv").val();
	var filterDetailLogic = $("#filterDetailLogicDiv option:selected").val();
	var filterDealMethod = $("#filterDealMethodDiv option:selected").val();
	var filterDetailNumber = $("#filterDetailNumberDiv").val();
	var filterLogicExpression = $("#filterLogicExpressionDiv").val();
	var filterLogicExpressionDes = $("#filterLogicExpressionDesDiv").val();
	var sqlFiltersTableLength = $("#sqlFilterTbody >tr").length;
	var NO = sqlFiltersTableLength+1;
	var _parentTable = $("#_parentTable").val();
	var FILTER_CT_RESULT = $("#FILTER_CT_RESULT").val();
	
	if($("#NOFilter").val()!=''){
		alertMsg.error("这是更改的查询条件，不是更改的新增条件，请点击“更改”按钮");
		return false;
	}
	
	if(filterDetailLogic!='is null'&&filterDetailLogic!='is not null'&&filterDetailLogic!='bjz'){
		if(filterDetailNumber==''){			
			alertMsg.error("请填写数值！");
			return false;
		}else{		
			$("#filterLogicExpressionDesDiv").attr("value",filterDetailDes+logicMap.get(filterDetailLogic)+filterDetailNumber);
		}
	}else{
		$("#filterLogicExpressionDesDiv").attr("value",filterDetailDes+logicMap.get(filterDetailLogic));
	}
	
	var sqlFiltersElement = DWZ.frag["ADDSQLFILTER"]
	.replaceAll('{NO}', NO)
	.replaceAll('{filterDetailCode}', filterDetailCode)
	.replaceAll('{filterDetailLogic}', logicMap.get(filterDetailLogic)+'('+filterDetailLogic+')')
	.replaceAll('{filterDetailNumber}', filterDetailNumber)
	.replaceAll('{filterLogicExpression}', filterLogicExpression)
	.replaceAll('{filterLogicExpressionDes}', filterLogicExpressionDes)
	.replaceAll('{CT_RESULT_TABLE_FILTER}',FILTER_CT_RESULT)
	.replaceAll('{filterDetailCodeGobal}',filterDetailCodeGobal)
	.replaceAll('{filterDetailDesGobal}',filterDetailDesGobal)
	.replaceAll('{filterDealMethod}',filterDealMethod)
	.replaceAll('{parentTable_table}',_parentTable);
	
	if(sqlFiltersTableLength==0){
		$("#sqlFilterTbody").append(sqlFiltersElement);
	}else{
		$("#sqlFilterTbody >tr").eq(sqlFiltersTableLength-1).after(sqlFiltersElement);
	}
	$("#sqlFilterTable").cssTable();
	resetNO();
	createFilterLogicExpressionTotal();
	debugger;
	getTables(_parentTable);
	getSQL(_parentTable);
}

function showDateElement(){
	var FILTER_CT_RESULT = $("#FILTER_CT_RESULT").val();
	if(FILTER_CT_RESULT=='CT_DATE'||FILTER_CT_RESULT=='CT_TIME'){
		$("#currentTimeCheck").css("display","inline");
		$("#currentTime").css("display","inline");
	}
}

function getCurrentTime(){
	if($("input[name='currentTimeCheck']").attr("checked")=='checked'){
		$("#filterDetailNumberDiv").val("sysdate");
	}else{
		$("#filterDetailNumberDiv").val("");
	}
}
function getSelectTimeBefore(){
	if($("input[name='currentTimeCheck']").attr("checked")!='checked'){		
		$("#calendar").attr("onclick","getSelectTime();");
	}
}
function getSelectTime(){
	$("#filterDetailNumberDiv").val($("#currentTime").val());
	$("#calendar").removeAttr("onclick");
}

function positions(elem,value){
	
	if (elem&&(elem.tagName=="TEXTAREA"||elem.type.toLowerCase()=="text")) {
	   if($.browser.msie){
			   var rng;
			   if(elem.tagName == "TEXTAREA"){ 
				    rng = event.srcElement.createTextRange();
				    rng.moveToPoint(event.x,event.y);
			   }else{ 
			    	rng = document.selection.createRange();
			   }
			   if( value === undefined ){
			   	 rng.moveStart("character",-event.srcElement.value.length);
			     return  rng.text.length;
			   }else if(typeof value === "number" ){
			   	 var index=this.position();
				 index>value?( rng.moveEnd("character",value-index)):(rng.moveStart("character",value-index));
				 rng.select();
			   }
		}else{
			if( value === undefined ){
			   	 return elem.selectionStart;
			   }else if(typeof value === "number" ){
			   	 elem.selectionEnd = value;
			         elem.selectionStart = value;
			   }
		}
	}else{
		if( value === undefined )
		   return undefined;
	}
}

function createFilterLogicExpressionTotal(){
	$("#filterLogicExpressionTotal").val("");
	var size = $("#sqlFilterTbody").find("tr").size();
	var filterLogicExpressionTotal="";
	for(var i=0;i<size;i++){
		var filterLogicExpressionGobal = $($("#sqlFilterTbody").find("tr")[i]).find("td:first").text();
		if($("#filterLogicExpressionTotal").val()==""){		
			filterLogicExpressionTotal = filterLogicExpressionGobal ;
			$("#filterLogicExpressionTotal").val(filterLogicExpressionTotal);
		}else{
			filterLogicExpressionTotal = filterLogicExpressionTotal +"*"+ filterLogicExpressionGobal ;
			$("#filterLogicExpressionTotal").val(filterLogicExpressionTotal);
		}	
	}
	$("#filterLogicExpressionTotal").val(filterLogicExpressionTotal);	
}

function bing(){
	/*$("#filterLogicExpressionTotal").append("+");*/
	var position = positions($("#filterLogicExpressionTotal")[0],undefined);
	var text = $("#filterLogicExpressionTotal").val();
	var textBefore = text.substring(0,position);
	var textAfter = text.substring(position,text.length);
	var textTotal = textBefore +"*"+textAfter;
	$("#filterLogicExpressionTotal").val(textTotal);
}
function huo(){
	/*$("#filterLogicExpressionTotal").append("+");*/
	var position = positions($("#filterLogicExpressionTotal")[0],undefined);
	var text = $("#filterLogicExpressionTotal").val();
	var textBefore = text.substring(0,position);
	var textAfter = text.substring(position,text.length);
	var textTotal = textBefore +"+"+textAfter;
	$("#filterLogicExpressionTotal").val(textTotal);
}
function zuo(){
	/*$("#filterLogicExpressionTotal").append("+");*/
	var position = positions($("#filterLogicExpressionTotal")[0],undefined);
	var text = $("#filterLogicExpressionTotal").val();
	var textBefore = text.substring(0,position);
	var textAfter = text.substring(position,text.length);
	var textTotal = textBefore +"("+textAfter;
	$("#filterLogicExpressionTotal").val(textTotal);
}
function you(){
	/*$("#filterLogicExpressionTotal").append("+");*/
	var position = positions($("#filterLogicExpressionTotal")[0],undefined);
	var text = $("#filterLogicExpressionTotal").val();
	var textBefore = text.substring(0,position);
	var textAfter = text.substring(position,text.length);
	var textTotal = textBefore +")"+textAfter;
	$("#filterLogicExpressionTotal").val(textTotal);
}
function fan(){
	/*$("#filterLogicExpressionTotal").append("+");*/
	var position = positions($("#filterLogicExpressionTotal")[0],undefined);
	var text = $("#filterLogicExpressionTotal").val();
	var textBefore = text.substring(0,position);
	var textAfter = text.substring(position,text.length);
	var textTotal = textBefore +"!"+textAfter;
	$("#filterLogicExpressionTotal").val(textTotal);
}

function getFilterSQL(){
	
	var filterSQL = '';
	var filterLogicExpressionTotal = $("#filterLogicExpressionTotal").val().split('');
	var sqlFilterTbody = $("#sqlFilterTbody").find("tr");
	
	for(var i=0;i<filterLogicExpressionTotal.length;i++){
		if(filterLogicExpressionTotal[i].isNumber()){
			filterSQL = filterSQL +"("+ $($("#sqlFilterTbody").find("tr")[filterLogicExpressionTotal[i]-1]).find("input[name='filterLogicExpression']").val()+") ";
		}else{
			
			filterSQL = filterSQL + logicExpress.get(filterLogicExpressionTotal[i]) +  " ";
		}
	}
	$("#filterSql").val(filterSQL);
	return filterSQL;
	
}

//用于禁用参数的"+"号button
function disableParameterButton(){
	
}