define(function(require) {
	var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');
    
    var ExchangeTaskExchangeMapinfoAddEdit = require('../ctrl/exchangeTask.ExchangeMapinfo.add.edit');

	var ExchangeTaskExchangeMapinfoAddChange = Page.extend(function() {
		
		this.injecte([
			  			new ExchangeTaskExchangeMapinfoAddEdit('exchangeTask_ExchangeMapinfo_add_edit'),
			  		]);
		
		var _self = this;
		this.load = function(panel, data) {
			panel.find('table').cdatagrid({
				controller: this,
				url:Config.ContextPath+'service/exchangetask/listExchangeMapInfo/1'
			});
		};
	});
	
	return ExchangeTaskExchangeMapinfoAddChange;
});