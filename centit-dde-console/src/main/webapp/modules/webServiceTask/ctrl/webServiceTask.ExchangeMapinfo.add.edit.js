define(function(require) {
	var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');

	var webServiceTaskExchangeMapinfoAddEdit = Page.extend(function() {
		var _self = this;
		this.load = function(panel, data) {
			panel.find('table').cdatagrid({
				controller: this,
				url:Config.ContextPath+'service/exchangetask/listExchangeMapInfo/4'
			});
		};
	});
	
	return webServiceTaskExchangeMapinfoAddEdit;
});