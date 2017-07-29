define(function(require) {
	var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');

	var importTaskExchangeMapinfoAddEdit = Page.extend(function() {
		var _self = this;
		this.load = function(panel, data) {
			panel.find('table').cdatagrid({
				controller: this,
				url:Config.ContextPath+'service/exchangetask/listExchangeMapInfo/3'
			});
		};
	});
	
	return importTaskExchangeMapinfoAddEdit;
});