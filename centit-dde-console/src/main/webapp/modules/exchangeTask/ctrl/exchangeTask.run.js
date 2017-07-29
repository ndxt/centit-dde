define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var ExchangeTaskRun = Page.extend(function() {
		
		var _self = this;
		
		// @override
		this.submit = function(table, data) {
			Core.ajax(Config.ContextPath+'service/transfer/doTransfer/'+data.taskId, {
                method: 'post',
			}).then(function() {
				table.datagrid('reload');
            });
		};
	});
	
	return ExchangeTaskRun;
});