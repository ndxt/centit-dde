define(function(require) {
	var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');
    
    var ExchangeTask3ExchangeMapinfoAddEdit = require('../ctrl/exchangeTask3.ExchangeMapinfo.add.edit');

	var ExchangeTask3ExchangeMapinfoAddChange = Page.extend(function() {
		
		this.injecte([
			  			new ExchangeTask3ExchangeMapinfoAddEdit('exchangeTask3_ExchangeMapinfo_add_edit'),
			  		]);
		
		var _self = this;
		this.load = function(panel, data) {
			panel.find('table').cdatagrid({
				controller: this,
				url:Config.ContextPath+'service/exchangetask/listExchangeMapInfo/3'
			});
		};
	});
	
	return ExchangeTask3ExchangeMapinfoAddChange;
});