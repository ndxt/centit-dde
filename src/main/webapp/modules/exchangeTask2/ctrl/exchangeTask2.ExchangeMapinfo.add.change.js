define(function(require) {
	var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');
    
    var ExchangeTask2ExchangeMapinfoAddEdit = require('../ctrl/exchangeTask2.ExchangeMapinfo.add.edit');

	var ExchangeTask2ExchangeMapinfoAddChange = Page.extend(function() {
		
		this.injecte([
			  			new ExchangeTask2ExchangeMapinfoAddEdit('exchangeTask2_ExchangeMapinfo_add_edit'),
			  		]);
		
		var _self = this;
		this.load = function(panel, data) {
			panel.find('table').cdatagrid({
				controller: this,
				url:Config.ContextPath+'service/exchangetask/listExchangeMapInfo/2'
			});
		};
	});
	
	return ExchangeTask2ExchangeMapinfoAddChange;
});