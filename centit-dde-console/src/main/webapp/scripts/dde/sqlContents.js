var contentFieldExpressionDesGobal;
var contentFieldExpressionCodeGobal;
var methodMap = new Map();
methodMap.put("+", "加上");
methodMap.put("-", "减去");
methodMap.put("×", "乘上");
methodMap.put("/", "除去");
methodMap.put("abs", "绝对值");
methodMap.put("sum", "求和");
methodMap.put("avg", "求平均值");
methodMap.put("max", "最大值");
methodMap.put("min", "最小值");
methodMap.put("upper", "大写");
methodMap.put("lower", "小写");
methodMap.put("ltrim", "左整理");
methodMap.put("rtrim", "右整理");
methodMap.put("trim", "整理");
methodMap.put("length", "长度");
methodMap.put("substr", "取子串");
methodMap.put("year", "年份");
methodMap.put("month", "月份");
methodMap.put("day", "日");
methodMap.put("trunc", "取整");
methodMap.put("count", "计数");
methodMap.put("dcount", "精确计数");


function sort(){
	var options = {
			cursor: 'move', // selector 的鼠标手势
			sortBoxs: 'tbody.sortDrags', //拖动排序项父容器
			replace: true, //2个sortBox之间拖动替换
			items: '> tr', //拖动排序项选择器
			selector: 'td:first', //拖动排序项用于拖动的子元素的选择器，为空时等于item
			zIndex: 1000
		};
	$("#sqlContentTbody").sortDrag(options);
	var SORTFINISH = DWZ.frag["SORTFINISH"];
	$("#_contentChange").after(SORTFINISH);
	$("#sort").remove();
}

function sortFinish(){
	var _parentTable = $("#_parentTable").val();
	$("#sortFinish").remove();
	getContentSQL();
	getTables(_parentTable);
	getSQL(_parentTable);
}

function sqlContents(treeId, treeNode, clickFlag){
	debugger;
	$("#contentFieldExpressionCode").attr("value",treeNode.tableAlias+"."+treeNode.id);
	$("#contentFieldAlias").attr("value",treeNode.id);
	$("#contentFieldExpressionDes").attr("value",treeNode.name);
	$("#_parentTable").attr("value",(treeNode.pId.substring(0,treeNode.pId.length-4)+' '+treeNode.tableAlias));
	contentFieldExpressionCodeGobal = $("#contentFieldExpressionCode").val();
	contentFieldExpressionDesGobal = $("#contentFieldExpressionDes").val();
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
	
	var contentDealMethod_CTNONE = DWZ.frag["contentDealMethod_CTNONE"];
	if(CT_RESULT=='CT_NUM'){
		var contentDealMethod_CTNUM = DWZ.frag["contentDealMethod_CTNUM"];
		$("#contentDealMethod").empty();
		$("#contentDealMethod").append(contentDealMethod_CTNUM);
		$("#contentDealMethod").append(contentDealMethod_CTNONE);
		$("#CT_RESULT").attr("value","CT_NUM");
	}else if(CT_RESULT=='CT_CHAR'){
		var contentDealMethod_CTCHAR = DWZ.frag["contentDealMethod_CTCHAR"];
		$("#contentDealMethod").empty();
		$("#contentDealMethod").append(contentDealMethod_CTCHAR);
		$("#contentDealMethod").append(contentDealMethod_CTNONE);
		$("#CT_RESULT").attr("value","CT_CHAR");
	}else if(CT_RESULT=='CT_STRING'){
		var contentDealMethod_CTSTRING = DWZ.frag["contentDealMethod_CTSTRING"];
		$("#contentDealMethod").empty();
		$("#contentDealMethod").append(contentDealMethod_CTSTRING);
		$("#contentDealMethod").append(contentDealMethod_CTNONE);
		$("#CT_RESULT").attr("value","CT_STRING");
	}else if(CT_RESULT=='CT_DATE'){
		var contentDealMethod_CTDATE = DWZ.frag["contentDealMethod_CTDATE"];
		$("#contentDealMethod").empty();
		$("#contentDealMethod").append(contentDealMethod_CTDATE);
		$("#contentDealMethod").append(contentDealMethod_CTNONE);
		$("#CT_RESULT").attr("value","CT_DATE");
	}else if(CT_RESULT=='CT_NONE'){
		$("#contentDealMethod").empty();
		$("#contentDealMethod").append(contentDealMethod_CTNONE);
		$("#CT_RESULT").attr("value","CT_NONE");
	}
}

function contentDealMethodChange(){
	debugger;
	var value = $("#contentDealMethod option:selected").val();
	if(value=='abs'||value=='ave'
	    ||value=='max'||value=='min'
	    ||value=='upper'||value=='lower'
	    ||value=='ltrim'||value=='rtrim'
	    ||value=='length'
	    ||value=='trunc'||value=='sum'
	    ||value=='count'){
		$("#L_contentDealMethodPARAMETER1").hide();
		$("#contentDealMethodPARAMETER1").hide();
		$("#L_contentDealMethodPARAMETER2").hide();
		$("#contentDealMethodPARAMETER2").hide();
		$("#contentFieldExpressionCode").attr("value",value+'('+contentFieldExpressionCodeGobal+')');
	}else if(value=='+'||value=='-'||value=='*'||value=='/'){
		$("#contentFieldExpressionCode").attr("value",contentFieldExpressionCodeGobal+value);
		$("#L_contentDealMethodPARAMETER1").show();
		$("#contentDealMethodPARAMETER1").show();
	}else if(value=='substr'){
		$("#contentFieldExpressionCode").attr("value",value+'('+contentFieldExpressionCodeGobal+',,)');
		$("#L_contentDealMethodPARAMETER1").show();
		$("#contentDealMethodPARAMETER1").show();
		$("#L_contentDealMethodPARAMETER2").show();
		$("#contentDealMethodPARAMETER2").show();
	}else if(value=='trim'){
		$("#L_contentDealMethodPARAMETER1").hide();
		$("#contentDealMethodPARAMETER1").hide();
		$("#L_contentDealMethodPARAMETER2").hide();
		$("#contentDealMethodPARAMETER2").hide();
		$("#contentFieldExpressionCode").attr("value",'rtrim(ltrim('+contentFieldExpressionCodeGobal+'))');
	}else if(value=='dcount'){
		$("#L_contentDealMethodPARAMETER1").hide();
		$("#contentDealMethodPARAMETER1").hide();
		$("#L_contentDealMethodPARAMETER2").hide();
		$("#contentDealMethodPARAMETER2").hide();
		$("#contentFieldExpressionCode").attr("value",'count(distinct '+contentFieldExpressionCodeGobal+')');
	}else if(value=='year'
	    ||value=='month'||value=='day'){
		$("#L_contentDealMethodPARAMETER1").hide();
		$("#contentDealMethodPARAMETER1").hide();
		$("#L_contentDealMethodPARAMETER2").hide();
		$("#contentDealMethodPARAMETER2").hide();
		var to_char = null;
		if(value=='year'){
			to_char = 'yyyy';
		}else if(value=='month'){
			to_char = 'mm';
		}else if(value == 'day'){
			to_char = 'dd';
		}
		$("#contentFieldExpressionCode").attr("value",'to_char('+contentFieldExpressionCodeGobal+','+'\''+to_char+'\')');
	}
	contentExpressionDesChangeByDealMethod();
}

function dealParameter1(){
	var value = $("#contentDealMethod option:selected").val();
	var parameter1 = $("#contentDealMethodPARAMETER1").val();
	if(value=='+'||value=='-'||value=='*'||value=='/'){		
		var contentDealMethodPARAMETER1 = $("#contentDealMethodPARAMETER1").val();
		$("#contentFieldExpressionCode").attr("value",contentFieldExpressionCodeGobal+value+contentDealMethodPARAMETER1);
		$("#contentFieldExpressionDes").attr("value",contentFieldExpressionDesGobal+methodMap.get(value)+parameter1);
	}else if(value = 'substr'){
		var contentDealMethodPARAMETER1 = $("#contentDealMethodPARAMETER1").val();
		$("#contentFieldExpressionCode").attr("value",value+'('+contentFieldExpressionCodeGobal+','+contentDealMethodPARAMETER1+',)');
		$("#contentFieldExpressionDes").attr("value","从"+contentFieldExpressionDesGobal+"第"+contentDealMethodPARAMETER1+"字符取   个字符");
		dealParameter2();
	}
}

function dealParameter2(){
	var value = $("#contentDealMethod option:selected").val();
	var contentDealMethodPARAMETER1 = $("#contentDealMethodPARAMETER1").val();
	var contentDealMethodPARAMETER2 = $("#contentDealMethodPARAMETER2").val();
	$("#contentFieldExpressionCode").attr("value",value+'('+contentFieldExpressionCodeGobal+','
			                                     +contentDealMethodPARAMETER1+','
			                                     +contentDealMethodPARAMETER2+')');
	$("#contentFieldExpressionDes").attr("value","从"+contentFieldExpressionDesGobal+"第"+contentDealMethodPARAMETER1+"字符取"+contentDealMethodPARAMETER2+"个字符");
}

function contentExpressionDesChangeByDealMethod(){
	debugger;
	var value = $("#contentDealMethod option:selected").val();
	if(value=='+'||value=='-'||value=='*'
		||value=='/'){
		$("#contentFieldExpressionDes").attr("value",contentFieldExpressionDesGobal+methodMap.get(value));
	}else if(value=='abs'||value=='max'||value=='min'){
		$("#contentFieldExpressionDes").attr("value","取"+contentFieldExpressionDesGobal+"的"+methodMap.get(value));
	}else if(value=='sum'||value=='ave'
		     ||value=='trunc'){
		$("#contentFieldExpressionDes").attr("value","对"+contentFieldExpressionDesGobal+methodMap.get(value));
	}else if(value=='upper'||value=='lower'
		     ||value=='ltrim'||value=='rtrim'
		     ||value=='trim'){
		$("#contentFieldExpressionDes").attr("value",methodMap.get(value)+contentFieldExpressionDesGobal);
	}else if(value=='year'||value=='month'
		     ||value=='day'||value=='count'
		     ||value=='dcount'||value=='length'){
		$("#contentFieldExpressionDes").attr("value",contentFieldExpressionDesGobal+"的"+methodMap.get(value));
	}
}

function addSqlContent(){
	var contentFieldExpressionCode = $("#contentFieldExpressionCode").val();
	var contentFieldExpressionDes = $("#contentFieldExpressionDes").val();
	var contentDealMethod = $("#contentDealMethod").val();
	var contentFieldAlias = $("#contentFieldAlias").val();
	var sqlContentTableLength = $("#sqlContentTbody >tr").length;
	var _parentTable = $("#_parentTable").val();
	var CT_RESULT = $("#CT_RESULT").val();
	var NO = sqlContentTableLength+1;

	var sqlContentElement = DWZ.frag["ADDSQLCONTENT"]
	.replaceAll('{contentFieldExpressionCode}', contentFieldExpressionCode)
	.replaceAll('{contentFieldExpressionCodeGobal}', contentFieldExpressionCodeGobal)
	.replaceAll('{contentFieldExpressionDesGobal}', contentFieldExpressionDesGobal)
	.replaceAll('{contentDealMethod}', contentDealMethod)
	.replaceAll('{contentFieldAlias}', contentFieldAlias)
	.replaceAll('{contentFieldExpressionDes}', contentFieldExpressionDes)
	.replaceAll('{NO}',NO)
	.replaceAll('{parentTable_table}',_parentTable)
	.replaceAll('{CT_RESULT_TABLE}',CT_RESULT);
	
	if(isAliasRepeat()){
		alertMsg.error("别名重复，请修改别名");
		return false;
	}
	
	if($("#NO").val()!=''){
		alertMsg.error("这是更改的查询内容，不是新增内容，请点击“更改”按钮");
		return false;
	}

	if(sqlContentTableLength==0){
		$("#sqlContentTbody").append(sqlContentElement);
	}else{
		$("#sqlContentTbody >tr").eq(sqlContentTableLength-1).after(sqlContentElement);
	}
	$("#sqlContentTable").cssTable();
	getContentSQL();
	getTables(_parentTable);
	debugger;
	getGroups();
	getOrdersLeft();
	getSQL(_parentTable);
	
}
function deleteContent(obj){
	debugger;
	var _parentTable = $("#_parentTable").val();
	var code = $($(obj).parent().parent()[0]).find("input[name='contentFieldExpressionCode']").val();
	$(obj).parent().parent().remove();
	getContentSQL();
	getTables(_parentTable);
	getGroups();
	deleteHavingByCode(code);
	getOrdersLeft();
	getSQL(_parentTable);
}

function editContent(obj){
	var templeHtml = $(obj).parent().parent(); 
	var contentFieldExpressionCode = templeHtml.find("td > input[name='contentFieldExpressionCode']").val();
	var contentDealMethod = templeHtml.find("td > input[name='contentDealMethod']").val();
	var contentFieldAlias = templeHtml.find("td > input[name='contentFieldAlias']").val();
	var contentFieldExpressionDes = templeHtml.find("td > input[name='contentFieldExpressionDes']").val();
	var contentFieldExpressionDesGobaltemp = templeHtml.find("td > input[name='contentFieldExpressionDesGobal']").val();
	var contentFieldExpressionCodeGobaltemp = templeHtml.find("td > input[name='contentFieldExpressionCodeGobal']").val();
	var parentTable_table = templeHtml.find("td > input[name='parentTable_table']").val();
	var ct_result_table = templeHtml.find("td > input[name='CT_RESULT_TABLE']").val();	
	var NO = templeHtml.find("td:first").text();
	contentFieldExpressionDesGobal = contentFieldExpressionDesGobaltemp;
	contentFieldExpressionCodeGobal = contentFieldExpressionCodeGobaltemp;
	$("#contentFieldExpressionCode").attr("value",contentFieldExpressionCode);
	$("#contentFieldAlias").attr("value",contentFieldAlias);
	$("#contentFieldExpressionDes").attr("value",contentFieldExpressionDes);
	$("#contentDealMethod").attr("value",contentDealMethod);
	$("#NO").attr("value",NO);
	$("#_parentTable").attr("value",parentTable_table);
	
	var contentDealMethod_CTNONE = DWZ.frag["contentDealMethod_CTNONE"];
	if(ct_result_table=='CT_NUM'){
		var contentDealMethod_CTNUM = DWZ.frag["contentDealMethod_CTNUM"];
		$("#contentDealMethod").empty();
		$("#contentDealMethod").append(contentDealMethod_CTNUM);
		$("#contentDealMethod").append(contentDealMethod_CTNONE);
		$("#CT_RESULT").attr("value","CT_NUM");
	}else if(ct_result_table=='CT_CHAR'){
		var contentDealMethod_CTCHAR = DWZ.frag["contentDealMethod_CTCHAR"];
		$("#contentDealMethod").empty();
		$("#contentDealMethod").append(contentDealMethod_CTCHAR);
		$("#contentDealMethod").append(contentDealMethod_CTNONE);
		$("#CT_RESULT").attr("value","CT_CHAR");
	}else if(ct_result_table=='CT_STRING'){
		var contentDealMethod_CTSTRING = DWZ.frag["contentDealMethod_CTSTRING"];
		$("#contentDealMethod").empty();
		$("#contentDealMethod").append(contentDealMethod_CTSTRING);
		$("#contentDealMethod").append(contentDealMethod_CTNONE);
		$("#CT_RESULT").attr("value","CT_STRING");
	}else if(ct_result_table=='CT_DATE'){
		var contentDealMethod_CTDATE = DWZ.frag["contentDealMethod_CTDATE"];
		$("#contentDealMethod").empty();
		$("#contentDealMethod").append(contentDealMethod_CTDATE);
		$("#contentDealMethod").append(contentDealMethod_CTNONE);
		$("#CT_RESULT").attr("value","CT_DATE");
	}else if(ct_result_table=='CT_NONE'){
		$("#contentDealMethod").empty();
		$("#contentDealMethod").append(contentDealMethod_CTNONE);
		$("#CT_RESULT").attr("value","CT_NONE");
	}
	
	
	deleteContent(obj);
}

function changeContent(){
	var NO = $("#NO").val();
	var contentFieldExpressionCode = $("#contentFieldExpressionCode").val();
	var contentFieldExpressionDes = $("#contentFieldExpressionDes").val();
	var contentDealMethod = $("#contentDealMethod").val();
	var contentFieldAlias = $("#contentFieldAlias").val();
	var sqlContentTableLength = $("#sqlContentTbody >tr").length;
	var _parentTable = $("#_parentTable").val();
	var sqlContentElement = DWZ.frag["ADDSQLCONTENT"]
	.replaceAll('{contentFieldExpressionCode}', contentFieldExpressionCode)
	.replaceAll('{contentFieldExpressionCodeGobal}', contentFieldExpressionCodeGobal)
	.replaceAll('{contentFieldExpressionDesGobal}', contentFieldExpressionDesGobal)
	.replaceAll('{contentDealMethod}', contentDealMethod)
	.replaceAll('{contentFieldAlias}', contentFieldAlias)
	.replaceAll('{contentFieldExpressionDes}', contentFieldExpressionDes)
	.replaceAll('{NO}',NO)
	.replaceAll('{parentTable_table}',_parentTable);
	
	if(isAliasRepeat()){
		alertMsg.error("别名重复，请修改别名");
		return false;
	}
	
	if($("#NO").val()==''){
		alertMsg.error("这是新增的查询内容，不是更改的查询内容，请点击“添加”按钮");
		return false;
	}
	
	if(sqlContentTableLength==0){
		$("#sqlContentTbody").append(sqlContentElement);
	}else if(NO-1<sqlContentTableLength){
		$("#sqlContentTbody >tr").eq(NO-1).before(sqlContentElement);
	} else if(NO-1==sqlContentTableLength){
		$("#sqlContentTbody >tr").eq(NO-2).after(sqlContentElement);
	}
	$("#NO").val('');
	
	$("#sqlContentTable").cssTable();
	getContentSQL();
	getTables(_parentTable);
	getGroups();
	getOrdersLeft();
	getSQL(_parentTable);

}

function getContentSQL(){
	var contentFieldExpressionCode = $("input[name='contentFieldExpressionCode']");
	var contentFieldAlias = $("input[name='contentFieldAlias']");
	var codeAndAlias = new Map();
	var contentSQL = '';
	for(var i=0;i<contentFieldExpressionCode.length;i++){
		codeAndAlias.put($(contentFieldExpressionCode.get(i)).val(), $(contentFieldAlias.get(i)).val());
		if(i<contentFieldExpressionCode.length-1){
			contentSQL = contentSQL+$(contentFieldExpressionCode.get(i)).val()+' as '+$(contentFieldAlias.get(i)).val()+",";
		}else{
			contentSQL = contentSQL+$(contentFieldExpressionCode.get(i)).val()+' as '+$(contentFieldAlias.get(i)).val();
		}
	}
	$("#sqlContentSQL").val(contentSQL);
	return contentSQL;
}

//判断别名是否重复
function isAliasRepeat(){
	var contentTableAliasElement = $("td > input[name='contentFieldAlias']");
	var contentFieldAlias = $("#contentFieldAlias").val();
	for(var i=0;i<contentTableAliasElement.length;i++){
		if(contentFieldAlias==$(contentTableAliasElement.get(i)).val()){
			return true;
		}else{
			continue;
		}
	}
	return false;
}









