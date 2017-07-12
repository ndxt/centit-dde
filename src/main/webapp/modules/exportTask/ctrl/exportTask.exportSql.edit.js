define(function(require) {
	require('plugins/extend');

	var Page = require('core/page');

	var exportTaskexportSqlEdit = Page.extend(function() {

		// @override
		this.submit = function(table) {
			if (!table.cdatagrid('endEdit')) {
				return;
			}
			var tablerows = table.datagrid('getRows');
			var i=0;
			tablerows.forEach(function(obj){
				obj.sourceFieldName = obj.destFieldName;
				table.datagrid("updateRow",{index:i,row:obj});
				i++;
			});

		}
	});
	
	return exportTaskexportSqlEdit;
});