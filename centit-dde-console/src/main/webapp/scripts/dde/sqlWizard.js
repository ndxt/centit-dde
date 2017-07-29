var pSelect = '';
var pFrom = '';
var pWhere = '';
var pGroup = '';
var pHaving = '';
var pOrder = '';
var pParam = '';
var SQL = '';
var parentTable = '';
var tableRelation;
/*$('.parentTable').change(function(event) {
	debugger;
	parentTable = $(this).val();
	$.ajax({
		type : "POST",
		url : "${pageContext.request.contextPath }/dde/sqlWizard!getTableRelation.do",
		data : "ctabcode="+parentTable,
		dataType : "json",
		success:function(data){
			var obj = data;
		    tableRelation = $.parseJSON(obj);
		}
	});
});*/

function getSQL(_parentTable){
	var pSelect = '';
	var pFrom = '';
	var pWhere = '';
	var pGroup = '';
	var pHaving = '';
	var pOrder = '';
	var pParam = '';
	var SQL = '';
	
	if(getTableSQL()!=''){		
		pFrom = "from "+getTableSQL();
		$("#sqlTableSQL").val(getTableSQL());
	}
	pSelect = "select "+getContentSQL()+" ";
	if(getFilterSQL()!=''){		
		pWhere = " where "+getFilterSQL()+" ";
	}
	if(getGroupSQL()!=''){	
		pGroup = " group by "+getGroupSQL()+" ";
	}
	if(getHavingSQL()!=''){
		pHaving = " having "+ getHavingSQL()+" ";
	}
	if(getOrderSQL()!=''){
		pOrder = " order by "+getOrderSQL()+" ";
	}
	SQL = pSelect+pFrom+pWhere+pGroup+pHaving+pOrder;
	$(".SQL").val(SQL);
}