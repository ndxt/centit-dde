define(function(require) {
	var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');
    
    var webServiceTaskExchangeMapinfoAddEdit = require('../ctrl/webServiceTask.ExchangeMapinfo.add.edit');

	var webServiceTaskExchangeMapinfoAddChange = Page.extend(function() {
		
		this.injecte([
			  			new webServiceTaskExchangeMapinfoAddEdit('webServiceTask_ExchangeMapinfo_add_edit'),
			  		]);
		
		var _self = this;
		this.load = function(panel, data) {
			panel.find('table').cdatagrid({
				controller: this,
				url:Config.ContextPath+'service/exchangetask/listExchangeMapInfo/4'
			});
		};
	});
	
	return webServiceTaskExchangeMapinfoAddChange;
});