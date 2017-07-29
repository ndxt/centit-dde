define(function(require) {
	var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');
    
    var importTaskExchangeMapinfoAddEdit = require('../ctrl/importTask.ExchangeMapinfo.add.edit');

	var importTaskExchangeMapinfoAddChange = Page.extend(function() {
		
		this.injecte([
			  			new importTaskExchangeMapinfoAddEdit('importTask_ExchangeMapinfo_add_edit'),
			  		]);
		
		var _self = this;
		this.load = function(panel, data) {
			panel.find('table').cdatagrid({
				controller: this,
				url:Config.ContextPath+'service/exchangetask/listExchangeMapInfo/3'
			});
		};
	});
	
	return importTaskExchangeMapinfoAddChange;
});