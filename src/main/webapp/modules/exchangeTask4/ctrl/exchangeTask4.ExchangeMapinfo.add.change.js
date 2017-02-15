define(function(require) {
	var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');
    
    var ExchangeTask4ExchangeMapinfoAddEdit = require('../ctrl/exchangeTask4.ExchangeMapinfo.add.edit');

	var ExchangeTask4ExchangeMapinfoAddChange = Page.extend(function() {
		
		this.injecte([
			  			new ExchangeTask4ExchangeMapinfoAddEdit('exchangeTask4_ExchangeMapinfo_add_edit'),
			  		]);
		
		var _self = this;
		this.load = function(panel, data) {
			panel.find('table').cdatagrid({
				controller: this,
				url:Config.ContextPath+'service/exchangetask/listExchangeMapInfo/4'
			});
		};
	});
	
	return ExchangeTask4ExchangeMapinfoAddChange;
});