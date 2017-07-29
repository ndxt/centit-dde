function getOrdersLeft(){
	$("#sqlOrdersLeftTbody >tr").remove();
	var contents = new Array();
	var sqlContents = $($("#sqlContentTbody")).find("input[name='contentDealMethod']");
	for(var i=0;i<sqlContents.size();i++){
			var content = $(sqlContents[i]).parent().parent();
			contents.push(content);
	}
	for(var i=0;i<sqlContents.length;i++){			
		var sortFieldExpressionCodeLeft = $(contents[i]).find("input[name='contentFieldExpressionCode']").val();
		var sortFieldExpressionDesLeft = $(contents[i]).find("input[name='contentFieldExpressionDes']").val();
		
		var sqlOrderTableLeftLength = $("#sqlOrdersLeftTbody >tr").length;
			
		var sqlOrderElementLeft = DWZ.frag["ADDSQLORDERLEFT"]
		.replaceAll('{sortFieldExpressionCodeLeft}', sortFieldExpressionCodeLeft)
		.replaceAll('{sortFieldExpressionDesLeft}', sortFieldExpressionDesLeft);
			
		if(sqlOrderTableLeftLength==0){
			$("#sqlOrdersLeftTbody").append(sqlOrderElementLeft);
		}else{
			$("#sqlOrdersLeftTbody >tr").eq(sqlOrderTableLeftLength-1).after(sqlOrderElementLeft);
		}
		$("#sqlOrdersLeftTable").cssTable();
		}
}

function sqlOrders(obj){ 
	var sortFieldExpressionCodeLeft = $(obj).find("input[name='sortFieldExpressionCodeLeft']").val();
	var sortFieldExpressionDesLeft = $(obj).find("input[name='sortFieldExpressionDesLeft']").val();
	var sqlOrdersRightTbodyLength = $("#sqlOrdersRightTbody>tr").length;
	
	$("#sortFieldExpressionCodeDiv").attr("value",sortFieldExpressionCodeLeft);
	$("#sortFieldExpressionDesDiv").attr("value",sortFieldExpressionDesLeft);
	/*$("#sortNoDiv").attr("value",sqlOrdersRightTbodyLength+1);*/
	
	/*var sqlOrdersRightElement = DWZ.frag["ADDSQLORDERRIGTH"]
	.replaceAll('{sortNo}', sqlOrdersRightTbodyLength+1)
	.replaceAll('{sortFieldExpressionCodeRight}', sortFieldExpressionCodeLeft)
	.replaceAll('{sortFieldExpressionDesRight}',sortFieldExpressionDesLeft);
	
	if(sqlOrdersRightTbodyLength==0){
		$("#sqlOrdersRightTbody").append(sqlOrdersRightElement);
	}else{
		$("#sqlOrdersRightTbody >tr").find(sqlOrdersRightTbodyLength-1).after(sqlOrdersRightElement);
	}
	
	$("#sqlOrdersRightTable").cssTable();*/
	
}

function addSQLOrders(){
	
	var _parentTable = $("#_parentTable").val();
	var sortFieldExpressionCodeDiv = $("#sortFieldExpressionCodeDiv").val();
	var sortFieldExpressionDesDiv = $("#sortFieldExpressionDesDiv").val();
	/*var sortNoDiv = $("#sortNoDiv").val();*/
	var orderMethodDiv = $("#orderMethodDiv").val();
	var sqlOrdersRightTbodyLength = $("#sqlOrdersRightTbody>tr").length;
	
	var sqlOrdersRightElement = DWZ.frag["ADDSQLORDERRIGTH"]
	.replaceAll('{sortNo}', sqlOrdersRightTbodyLength+1)
	.replaceAll('{sortFieldExpressionCodeRight}', sortFieldExpressionCodeDiv)
	.replaceAll('{sortFieldExpressionDesRight}',sortFieldExpressionDesDiv)
	.replaceAll('{orderMethod}',orderMethodDiv);
	
	if($("#sortNoDiv").val()!=''){
		alertMsg.error("这是更改的结果排序，不是新增的结果排序，请点击“更改”按钮");
		return false;
	}
	
	if(sqlOrdersRightTbodyLength==0){
		$("#sqlOrdersRightTbody").append(sqlOrdersRightElement);
	}else{
		$("#sqlOrdersRightTbody >tr").eq(sqlOrdersRightTbodyLength-1).after(sqlOrdersRightElement);
	}
	
	$("#sqlOrdersRightTable").cssTable();
	getOrderSQL();
	getSQL(_parentTable);
}

function deleteOrder(obj){
	var _parentTable = $("#_parentTable").val();
	$(obj).parent().parent().remove();
	getOrderSQL();
	getSQL(_parentTable);
}

function editOrder(obj){
	var templeHtml = $(obj).parent().parent();
	var sortFieldExpressionCodeRight = templeHtml.find("td > input[name='sortFieldExpressionCodeRight']").val();
	var sortFieldExpressionDesRight = templeHtml.find("td > input[name='sortFieldExpressionDesRight']").val();
	var sortNo = templeHtml.find("td > input[name='sortNo']").val();
	var orderMethod = templeHtml.find("td > input[name='orderMethod']").val();
	
	$("#sortFieldExpressionCodeDiv").attr("value",sortFieldExpressionCodeRight);
	$("#sortFieldExpressionDesDiv").attr("value",sortFieldExpressionDesRight);
	$("#sortNoDiv").attr("value",sortNo);
	$("#orderMethod").attr("value",orderMethod);
	
	deleteOrder(obj);
}

function changeSQLOrders(){
	var _parentTable = $("#_parentTable").val();
	var sortFieldExpressionCodeDiv = $("#sortFieldExpressionCodeDiv").val();
	var sortFieldExpressionDesDiv = $("#sortFieldExpressionDesDiv").val();
	var sortNoDiv = $("#sortNoDiv").val();
	var orderMethodDiv = $("#orderMethodDiv").val();
	var sqlOrdersRightTbodyLength = $("#sqlOrdersRightTbody>tr").length;
	
	var sqlOrdersRightElement = DWZ.frag["ADDSQLORDERRIGTH"]
	.replaceAll('{sortNo}', sortNoDiv)
	.replaceAll('{sortFieldExpressionCodeRight}', sortFieldExpressionCodeDiv)
	.replaceAll('{sortFieldExpressionDesRight}',sortFieldExpressionDesDiv)
	.replaceAll('{orderMethod}',orderMethodDiv);
	
	if($("#sortNoDiv").val()==''){
		alertMsg.error("这是新增的结果排序，不是更改的结果排序，请点击“添加”按钮");
		return false;
	}
	
	if(sqlOrdersRightTbodyLength==0){
		$("#sqlOrdersRightTbody").append(sqlOrdersRightElement);
	}else if(sortNoDiv-1<sqlOrdersRightTbodyLength){
		$("#sqlOrdersRightTbody >tr").eq(sortNoDiv-1).before(sqlOrdersRightElement);
	} else if(sortNoDiv-1==sqlOrdersRightTbodyLength){
		$("#sqlOrdersRightTbody >tr").eq(sortNoDiv-2).after(sqlOrdersRightElement);
	}
	$("#NOHaving").val('');
	
	$("#sqlOrdersRightTable").cssTable();
	getOrderSQL();
	getSQL(_parentTable);
}

function getOrderSQL(){
	var getOrderSQL = '';
	var sortFieldExpressionCodeRights = $("#sqlOrdersRightTbody").find("input[name='sortFieldExpressionCodeRight']");
	var orderMethods = $("#sqlOrdersRightTbody").find("input[name='orderMethod']");
	for(var i=0;i<orderMethods.size();i++){
		if(i<orderMethods.size()-1){			
			getOrderSQL = getOrderSQL + $(sortFieldExpressionCodeRights[i]).val()+" "+$(orderMethods[i]).val()+",";
		}else{
			getOrderSQL = getOrderSQL + $(sortFieldExpressionCodeRights[i]).val()+" "+$(orderMethods[i]).val();
		}
	}
	$("#sqlOrderSQL").val(getOrderSQL);
	return getOrderSQL;
}
