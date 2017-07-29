var aggregateFun = new Map();
aggregateFun.put("sum", "sum");
aggregateFun.put("ave", "ave");
aggregateFun.put("max", "max");
aggregateFun.put("min", "min");
aggregateFun.put("count", "count");
aggregateFun.put("dcount", "dcount");

function getGroups(){
	$("#sqlGroupsTbody >tr").remove();
	var sqlContents = $($("#sqlContentTbody")).find("input[name='contentDealMethod']");
	if(!isIncludeAggregate(sqlContents)){
		return false;
	}else{
		var contentWithoutAggregates = getContentWithoutAggregate(sqlContents);
		for(var i=0;i<contentWithoutAggregates.length;i++){			
			var contentWithoutAggregatesCode = $(contentWithoutAggregates[i]).find("input[name='contentFieldExpressionCode']").val();
			var contentWithoutAggregatesDes = $(contentWithoutAggregates[i]).find("input[name='contentFieldExpressionDes']").val();
			var sqlGroupTableLength = $("#sqlGroupsTbody >tr").length;
			var NO = sqlGroupTableLength+1;
			
			var sqlGroupElement = DWZ.frag["ADDSQLGROUP"]
			.replaceAll('{NO}', NO)
			.replaceAll('{groupFieldExpressionDes}', contentWithoutAggregatesDes)
			.replaceAll('{groupFieldExpressionCode}', contentWithoutAggregatesCode);
			
			if(sqlGroupTableLength==0){
				$("#sqlGroupsTbody").append(sqlGroupElement);
			}else{
				$("#sqlGroupsTbody >tr").eq(sqlGroupTableLength-1).after(sqlGroupElement);
			}
			$("#sqlGroupsTable").cssTable();
		}
	}
	getGroupSQL();
	getHavingsLeft();
}


/*判断查询内容中是否存在聚合函数，是返回true*/
function isIncludeAggregate(sqlContents){
	for(var i=0;i<sqlContents.size();i++){
		if(isSingleAggregate(sqlContents[i])){
			return true;
		}else{
			continue;
		}
	}
	return false;
}

/*判断单个查询内容是否是聚合函数，是返回true*/
function isSingleAggregate(sqlContent){
	var method = $(sqlContent).val();
	if(aggregateFun.containsKey(method)){
		return true;
	}else{
		return false;
	}
}

/*获取没使用聚合函数的查询内容*/
function getContentWithoutAggregate(sqlContents){
	var contentWithoutAggregates = new Array();
	for(var i=0;i<sqlContents.size();i++){
		if(!isSingleAggregate(sqlContents[i])){
			var content = $(sqlContents[i]).parent().parent();
			contentWithoutAggregates.push(content);
		}
	}
	return contentWithoutAggregates;
}

/*拼装分组SQL*/
function getGroupSQL(){
	var groupSQL = '';
	var groupCodes = $("#sqlGroupsTbody").find("input[name='groupFieldExpressionCode']");
	for(var i=0;i<groupCodes.size();i++){
		if(i<groupCodes.size()-1){
			groupSQL = groupSQL + $(groupCodes[i]).val()+",";
		}else{
			groupSQL = groupSQL + $(groupCodes[i]).val();
		}
	}
	$("#groupSql").val(groupSQL);
	return groupSQL;
}


