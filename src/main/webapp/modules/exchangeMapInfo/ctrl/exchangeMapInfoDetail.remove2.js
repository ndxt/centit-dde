define(function(require) {
	require('plugins/extend');

	var Page = require('core/page');

	var ExchangeMapInfoDetailRemove2 = Page.extend(function() {
		
		// TODO 日志删除
		// @override
		this.submit = function(table, data) {
			this.removeListItem(table, data);
		};
		// 删除列表明细
		this.removeListItem = function (table, row) {
			var index = table.datagrid('getRowIndex', row);
			table.datagrid('deleteRow', index);
		}
	});
	
	return ExchangeMapInfoDetailRemove2;
});