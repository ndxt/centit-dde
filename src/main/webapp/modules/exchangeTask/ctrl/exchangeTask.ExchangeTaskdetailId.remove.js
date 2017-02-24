define(function(require) {
	require('plugins/extend');

	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');

	var ExchangeTaskExchangeTaskdetailIdRemove = Page.extend(function() {
		var _self = this;
		// @override
		this.submit = function(table, data,panel) {
			var mapinfoId = data.mapinfoId;
			data = _self.parent.data;
			var taskId = data.taskId;
			Core.ajax(Config.ContextPath+'service/exchangetaskdetail/delete?taskId='+taskId+"&MapinfoId="+mapinfoId, {
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
	
	return ExchangeTaskExchangeTaskdetailIdRemove;
});