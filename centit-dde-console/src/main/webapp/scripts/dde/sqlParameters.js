function addSQLParameters(){
	var parameterDesDiv = $("#parameterDesDiv").val();
	var parameterDefaultValueDiv = $("#parameterDefaultValueDiv").val();
	var sqlParametersTbodyLength = $("#sqlParametersTbody >tr").length;
	
	var parametersElement = DWZ.frag["ADDSQLPARAMETER"]
	.replaceAll('{parameterDes}', parameterDesDiv)
	.replaceAll('{parameterNo}', sqlParametersTbodyLength+1)
	.replaceAll('{parameterDefaultValue}', parameterDefaultValueDiv);
	
	if($("#parameterNoDiv").val()!=''){
		alertMsg.error("这是更改的 参数，不是新增的参数，请点击“更改”按钮");
		return false;
	}
	
	if(sqlParametersTbodyLength==0){
		$("#sqlParametersTbody").append(parametersElement);
	}else{
		$("#sqlParametersTbody >tr").eq(sqlParametersTbodyLength-1).after(parametersElement);
	}
	
	$("#sqlParametersTable").cssTable();
}

function editParameter(obj){
	var templeHtml = $(obj).parent().parent(); 
	var parameterDes = templeHtml.find("td > input[name='parameterDes']").val();
	var parameterNo = templeHtml.find("td > input[name='parameterNo']").val();
	var parameterDefaultValue = templeHtml.find("td > input[name='parameterDefaultValue']").val();
	
	$("#parameterDesDiv").val(parameterDes);
	$("#parameterNoDiv").val(parameterNo);
	$("#parameterDefaultValueDiv").val(parameterDefaultValue);
	
	deleteParameter(obj);
}

function deleteParameter(obj){
	$(obj).parent().parent().remove();
	$("#sqlParametersTable").cssTable();
}

function changeSQLParameters(){
	var parameterDesDiv = $("#parameterDesDiv").val();
	var parameterDefaultValueDiv = $("#parameterDefaultValueDiv").val();
	var sqlParametersTbodyLength = $("#sqlParametersTbody >tr").length;
	var parameterNoDiv = $("#parameterNoDiv").val();
	
	var parametersElement = DWZ.frag["ADDSQLPARAMETER"]
	.replaceAll('{parameterDes}', parameterDesDiv)
	.replaceAll('{parameterNo}', parameterNoDiv)
	.replaceAll('{parameterDefaultValue}', parameterDefaultValueDiv);
	
	if($("#parameterNoDiv").val()==''){
		alertMsg.error("这是新增的参数，不是更改的参数，请点击“新增”按钮");
		return false;
	}
	
	if(sqlParametersTbodyLength==0){
		$("#sqlParametersTbody").append(parametersElement);
	}else if(parameterNoDiv-1<sqlParametersTbodyLength){
		$("#sqlParametersTbody >tr").eq(parameterNoDiv-1).before(parametersElement);
	}else if(parameterNoDiv-1==sqlParametersTbodyLength){
		$("#sqlParametersTbody >tr").eq(parameterNoDiv-2).after(parametersElement);
	}
	$("#parameterNoDiv").val('');
	
	$("#sqlParametersTable").cssTable();
}