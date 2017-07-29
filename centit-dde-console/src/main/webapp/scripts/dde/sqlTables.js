var joinType = new Map();
joinType.put('inner join', '内连接');
joinType.put('left join', '左连接');
joinType.put('right join', '右连接');
joinType.put('full join', '完整连接');


function getTables(_parentTable){
	
	var childTableAndAlias = _parentTable.split(' ');
	var parentAlias = getParentAlias(childTableAndAlias[1]);
	var contentFieldParentTable_table = $("input[class='parentTable_table']");
	var topAlias = getTopAlias(contentFieldParentTable_table);
	var relations = new Array();
	if (contentFieldParentTable_table.length==1||isSameTable(contentFieldParentTable_table)){
		//单表的表关联
		var relTable1 = new parentAndChildTable();
		relTable1.setAll(null, null, null, 
				         $(contentFieldParentTable_table).val().split(' ')[0], 
				         topAlias.get(0), 
				         getCColcode($(contentFieldParentTable_table).val().split(' ')[0]));
		relations.push(relTable1);
	}else{
		//多表的表关联
		var relTable2 = new parentAndChildTable();
		var uniqueTables = getUniqueTables(contentFieldParentTable_table);
		//顶端表在查询内容中的情况(列表中包含顶端表)
		
		if(topAlias.get(topAlias.keys()[0])==topAlias.get(topAlias.keys()[0])){				
			relTable2.setAll(null, null, null, getTableNameByAlias(topAlias.get(topAlias.keys()[0])),topAlias.get(topAlias.keys()[0]), 
						getCColcode(getTableNameByAlias(topAlias.get(topAlias.keys()[0]))));
			relations.push(relTable2);
		}
		for(var i=0;i<uniqueTables.size();i++){
			if(uniqueTables.get(i).split(' ')[1]!=topAlias.get(topAlias.keys()[0])){
				getRelationByAlias(topAlias.get(topAlias.keys()[0]),uniqueTables.get(i).split(' ')[1],relations,contentFieldParentTable_table
						           ,uniqueTables.get(i).split(' ')[1],topAlias.get(topAlias.keys()[0]));
			}else{
				continue;
			}
		}
				
	}
	addSqlTables(relations);
	$("#sqlTableSQL").val(getTableSQL());
}


function getTableSQL() {
	
	var tableSQL = '';
	var tableParentTable = new Map();
	var pColcode = new Map();
	var tableLinkType = new Map();
	var tableChildTable = new Map();
	var cColcode = new Map();

	var tableParentTableList = $("input[name=tableParentTable]");
	var pColcodeList = $("input[name=pColcode]");
	var tableLinkTypeList = $("select[name=tableLinkType] option:selected");
	var tableChildTableList = $("input[name=tableChildTable]");
	var cColcodeList = $("input[name=cColcode]");

	if(tableChildTableList.length==1){
		tableSQL = $(tableChildTableList[0]).val();
	}else{		
		for ( var i = 0; i < tableParentTableList.length; i++) {
			tableParentTable.put(i, $(tableParentTableList[i]).val());
			pColcode.put(i, $(pColcodeList[i]).val());
			tableLinkType.put(i, $(tableLinkTypeList[i]).val());
			tableChildTable.put(i, $(tableChildTableList[i]).val());
			cColcode.put(i, $(cColcodeList[i]).val());
		}
		
		tableSQL = tableSQL + ' ' + tableParentTable.get(1) + ' ';
		for ( var j = 1; j < tableParentTableList.length; j++) {
			var parentAlias = tableParentTable.get(j).split(' ')[1];
			var childAlias = tableChildTable.get(j).split(' ')[1];
			tableSQL = tableSQL + ' ' + tableLinkType.get(j) + ' ';
			tableSQL = tableSQL + ' ' + tableChildTable.get(j) + ' on (';
			if (cColcode.get(j).split(' ').length != 1) {
				for ( var k = 0; k < cColcode.get(j).split(' ').length; k++) {
					tableSQL = tableSQL + parentAlias + '.'+ pColcode.get(j).split(' ')[k] + '=';
					if(k==cColcode.get(j).split(' ').length-1){
						tableSQL = tableSQL + childAlias+'.'+cColcode.get(j).split(' ')[k]+')';
					}else{					
						tableSQL = tableSQL + childAlias+'.'+cColcode.get(j).split(' ')[k]+') and (';
					}
				}
			}else{
				tableSQL = tableSQL + parentAlias + '.'+ pColcode.get(j) + '=';
				tableSQL = tableSQL + childAlias+'.'+cColcode.get(j)+')';
			}
		}
	}
	return tableSQL;
}


function addSqlTables(relations){
	
	var sqlTablesOld = $("#sqlTablesTbody >tr");
	var sqltablesOldmaps = new Map();
	for(var t=0;t<sqlTablesOld.length;t++){
		sqltablesOldmaps.put($(sqlTablesOld[t]).find("input[name='tableChildTable']").val(), 
				$(sqlTablesOld[t]).find("select[name='tableLinkType'] option:selected").val());
	}
	$("#sqlTablesTbody >tr").remove();
	for(var i=0;i<relations.length;i++){
		var tableParentTable;
		var pColcode;
		if(relations[i].parentTable==null){
			tableParentTable='';
			pColcode='';
		}else{			
			tableParentTable = relations[i].parentTable+' '+relations[i].parentAlias;
			pColcode = relations[i].pColCode;
		}
		var tableChildTable = relations[i].childTable+' '+relations[i].childAlias;
		var cColcode = relations[i].cColcode;
		var tableLinkType = null;
		var tableLinkTypeValue = null;
		if(sqltablesOldmaps.get(tableChildTable)!=null){		
			tableLinkType = joinType.get(sqltablesOldmaps.get(tableChildTable));
			tableLinkTypeValue = sqltablesOldmaps.get(tableChildTable);
		}else{
			tableLinkType = joinType.get('inner join');
			tableLinkTypeValue = "inner join";
		}
		
		var sqlTableElement = DWZ.frag["ADDSQLTABLE"]
		.replaceAll('{tableParentTable}', tableParentTable)
		.replaceAll('{pColcode}', pColcode)
		.replaceAll('{tableLinkType}', tableLinkType)
		.replaceAll('{tableChildTable}', tableChildTable)
		.replaceAll('{cColcode}', cColcode);
		
		/*if($("#sqlTablesTbody >tr").length==0){
			
		}*/
		var temp = 0;
		if(sqltablesOldmaps.get(tableChildTable)=='inner join'){
			temp = 0;
		}else if(sqltablesOldmaps.get(tableChildTable)=='left join'){
			temp = 1;
		}else if(sqltablesOldmaps.get(tableChildTable)=='right join'){
			temp = 2;
		}else if(sqltablesOldmaps.get(tableChildTable)=='full join'){
			temp = 3;
		}
		
		$("#sqlTablesTbody").append(sqlTableElement);
		//$($("#sqlTablesTbody").find("select")[i]).find("option[value=tableLinkTypeValue]").attr("selected",true);
		$($($("#sqlTablesTbody").find("select")[i]).find("option")[temp]).attr("selected",true);
	}
	$("#sqlTablesTable").cssTable();
	$("select[name='tableLinkType']").first().attr("disabled","disabled");;
}

function isIncludeInRelation(relations,relTable){
	for(var i=0;i<relations.length;i++){
		if(relTable.isEqual(relations[i])){
			return true;
		}else{
			continue;
		}
	}
	return false;
}

var relation = 0;
function getRelationByAlias(topAlias,cAlias,relations,contentFieldParentTable_table,cAliasGobal,topAliasGobal){
	var cAliasGobalTemp = cAliasGobal;
	var topAliasGobalTemp = topAliasGobal;
	if(topAlias==cAliasGobal){
		return relations;
	}
	if(cAlias.split('_').length-topAlias.split('_').length==1){
		relation++;
		var relTable = new parentAndChildTable();
		relTable.setAll(getTableNameByAlias(topAlias), topAlias, 
				        getPColcode(getTableNameByAlias(topAlias),getTableNameByAlias(cAlias)), 
				        getTableNameByAlias(cAlias), cAlias, 
				        getCColcode(getTableNameByAlias(cAlias)));
		if(!isIncludeInRelation(relations,relTable)){		
			relations.push(relTable);
		}
		if(relation!=0){		
			topAlias = cAlias;
		}
		getRelationByAlias(topAlias,cAliasGobalTemp,relations,contentFieldParentTable_table,cAliasGobal,topAliasGobal);
	}else{
		getRelationByAlias(topAlias,cAlias.substring(0,cAlias.lastIndexOf('_')),relations,contentFieldParentTable_table,cAliasGobal,topAliasGobal);
	}
}


function getTableNameByAlias(alias){
	
	var allTableAndAlias = new Map();
	allTableAndAlias = getAllTableAndAlias();
	
	if(allTableAndAlias.containsKey(alias)){
		return allTableAndAlias.get(alias);
	}else{
		return null;
	}
}

/*function getTableNameByAlias(alias){
	for(var i=0;i<tableAndAlias.length;i++){
		if(tableAndAlias[i].tableAlias==alias){
			if(tableAndAlias[i].id.indexOf('字段列表')==-1){
				return tableAndAlias[i].id;
			}else{
				return tableAndAlias[i].id.substring(0,tableAndAlias[i].id.length-4);
			}
		}else{
			continue;
		}
	}
	return null;
}*/

function getPColcode(pTableName,cTableName){
	var pcolcode='';
	for(var i=0;i<tableRelation.length;i++){
		if(tableRelation[i].PTABCODE==pTableName&&tableRelation[i].CTABCODE==cTableName){
			if(pcolcode==''){
				pcolcode = tableRelation[i].PCOLCODE;
			}else{	
				pcolcode = pcolcode +' '+tableRelation[i].PCOLCODE;
			}
		}else{
			continue;
		}
	}
	return pcolcode;
}


function getCColcode(cTableName){
	var ccolcode='';
	for(var i=0;i<tableRelation.length;i++){
		if(tableRelation[i].CTABCODE==cTableName){
			if(ccolcode==''){
				ccolcode = tableRelation[i].CCOLCODE;
			}else{	
				ccolcode = ccolcode +' '+tableRelation[i].CCOLCODE;
			}
		}else{
			continue;
		}
	}
	return ccolcode;
}

function getParentAlias(childTableAlias){
	return childTableAlias.substring(0,childTableAlias.lastIndexOf('_'));
}
//用于判断最后一个元素是否和其他元素相同
function isSameTable(contentFieldParentTable_table){
	var parentTable_tableMap = new Map();
	for(var i=0;i<contentFieldParentTable_table.length;i++){
		parentTable_tableMap.put(i, $(contentFieldParentTable_table.get(i)).val().split(' ')[1]);
	}
	var last = parentTable_tableMap.get(parentTable_tableMap.size()-1);
	for(var j=0;j<parentTable_tableMap.size()-1;j++){
		if(parentTable_tableMap.get(j)==last){
			continue;
		}else{
			return false;
		}
	}
	return true;
}

//用于判断列表中的任意一个元素是否是同其他元素相同,如果有一个相同就返回true
function isSameTables(contentFieldParentTable_table,k){
	var parentTable_tableMap = new Map();
	for(var i=0;i<contentFieldParentTable_table.length;i++){
		parentTable_tableMap.put(i, $(contentFieldParentTable_table.get(i)).val().split(' ')[1]);
	}
	var last = parentTable_tableMap.get(k);
	for(var j=0;j<=k-1;j++){
		if(parentTable_tableMap.get(j)!=last){
			continue;
		}else{
			return true;
		}
	}
	return false;
}


function getTopAlias(contentFieldParentTable_table){
	var maps = new Map();
	var includeMap = new Map();
	var minLengthElement = new Map();
	var position=0;
	var topAlias;
	var positionTemp = 0;
	for(var i=0;i<contentFieldParentTable_table.length;i++){
		maps.put(i, $(contentFieldParentTable_table.get(i)).val().split(' ')[1].split('_'));
	}	
	for(var i=0;i<contentFieldParentTable_table.length;i++){
		includeMap.put(i, $(contentFieldParentTable_table.get(i)).val().split(' ')[1]);
	}	
	for ( var j = 1; j < maps.size(); j++) {
		if (maps.get(position).length > maps.get(j).length) {
			position = j;
		}	
	}
	topAlias = includeMap.get(position);
	while(!isInclude(contentFieldParentTable_table,position,topAlias)){
		topAlias = getParentAlias(topAlias);
		positionTemp--;
	}
	if(positionTemp<0){	
		minLengthElement.put(-1, topAlias);	
	}else{
		minLengthElement.put(position, topAlias);
	}
	return minLengthElement;
	
}

//判断其他别名中是否包含这个最短元素
function isInclude(contentFieldParentTable_table,position,minAlias){
	var isIncludeMap = new Map();
	for(var i=0;i<contentFieldParentTable_table.length;i++){
		isIncludeMap.put(i, $(contentFieldParentTable_table.get(i)).val().split(' ')[1]);
	}
	for(var j=0;j<isIncludeMap.size();j++){
		if(isIncludeMap.get(j).match(minAlias)==null){
			return false;
		}else{
			continue;
		}
	}
	return true;
}


function getUniqueTables(contentFieldParentTable_table){
	var uniqueTables = new Map();
	var first = $(contentFieldParentTable_table.get(0)).val();
	uniqueTables.put(0, first);
	var j=1;
	for(var i=1;i<contentFieldParentTable_table.length;i++){
		if(!isSameTables(contentFieldParentTable_table,i)){
			uniqueTables.put(j, $(contentFieldParentTable_table.get(i)).val());
			j++;
		}
	}
	return uniqueTables;
}

function getAllTableAndAlias(){
	var allTableAndAlias = new Map();
	for(var i=0;i<tableAndAlias.length;i++){
		if(tableAndAlias[i].id.indexOf('字段列表')==-1){
			allTableAndAlias.put(tableAndAlias[i].tableAlias, tableAndAlias[i].id);
		}else{
			allTableAndAlias.put(tableAndAlias[i].tableAlias, tableAndAlias[i].id.substring(0,tableAndAlias[i].id.length-4));
		}
	}
	
	var tableAndAliasInPage = $("input[name='tableChildTable']");
	
	for(var j=0;j<tableAndAliasInPage.length;j++){
		var tableName = $(tableAndAliasInPage[j]).val().split(' ')[0];
		var AliasName = $(tableAndAliasInPage[j]).val().split(' ')[1];
		if(!allTableAndAlias.containsKey(AliasName)){
			allTableAndAlias.put(AliasName, tableName);
		}
	}
	
	return allTableAndAlias;
	
	
}



function parentAndChildTable() {

	this.parentTable = null;
	this.parentAlias = null;
	this.pColCode = null;
	this.childTable = null;
	this.childAlias = null;
	this.cColcode = null;
	
	this.setAll = function(parentTable,parentAlias,pColCode,childTable,childAlias,cColcode){
		this.parentTable = parentTable;
		this.parentAlias = parentAlias;
		this.pColCode = pColCode;
		this.childTable = childTable;
		this.childAlias = childAlias;
		this.cColcode = cColcode;
	};
	
	this.isEqual = function (relTable2){
		if(this.parentTable==relTable2.parentTable
			&&this.parentAlias==relTable2.parentAlias
			&&this.pColCode==relTable2.pColCode
			&&this.childTable==relTable2.childTable
			&&this.childAlias==relTable2.childAlias
			&&this.cColcode==relTable2.cColcode){
			return true;
		}else{
			return false;
		}
	};

	this.setParentTable = function(parentTable) {
		this.parentTable = parentTable;
	};
	this.setParentAlias = function(parentAlias) {
		this.parentAlias = parentAlias;
	};
	this.setPColCode = function(pColCode) {
		this.pColCode = pColCode;
	};
	this.setChildTable = function(childTable) {
		this.childTable = childTable;
	};
	this.setChildAlias = function(childAlias) {
		this.childAlias = childAlias;
	};
	this.setCColcode = function(cColcode) {
		this.cColcode = cColcode;
	};

	this.getParentTable = function() {
		return this.parentTable;
	};
	this.getParentAlias = function() {
		return this.parentAlias;
	};
	this.getPColCode = function() {
		return this.pColCode;
	};
	this.getChildTable = function() {
		return this.childTable;
	};
	this.getChildAlias = function() {
		return this.childAlias;
	};
	this.getCColcode = function() {
		return this.cColcode;
	};
	
	
}  




