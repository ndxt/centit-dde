define(function(require) {
	require('plugins/extend');

	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');

	var importTaskExchangeTaskdetailIdRemove = Page.extend(function() {
		var _self = this;
		// @override
		this.submit = function(table, data,panel) {
			var exportId = data.exportId;
			data = _self.parent.data;
			var taskId = data.taskId;
			Core.ajax(Config.ContextPath+'service/exchangetaskdetail/delete?taskId='+taskId+"&MapinfoId="+exportId, {
            	type: 'json',
                method: 'post',
                data: {
                    _method: 'delete'
                }
			}).then(function() {
				table.datagrid('reload');
            });
		}
	});
	
	return importTaskExchangeTaskdetailIdRemove;
});