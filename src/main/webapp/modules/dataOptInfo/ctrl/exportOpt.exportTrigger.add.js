define(function(require) {
	var Page = require('core/page');


	var ExportOptExportTriggerAdd = Page.extend(function() {
		var _self = this;
		// @override
		this.submit = function(table, data,closeCallback) {
			this.editDatagrid(table);
		};
		
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
        };
		// 编辑普通表格
		this.editDatagrid = function(table) {
			if (!table.cdatagrid('endEdit')) {
				return;
			}
			var index = table.datagrid('getRows').length;


			// 插入新数据
			table.datagrid('appendRow',{tiggerOrder:index+1});
			table.datagrid('selectRow', index);

			// 开启编辑
			table.cdatagrid('beginEdit', index);
		}
	});
	
	return ExportOptExportTriggerAdd;
});