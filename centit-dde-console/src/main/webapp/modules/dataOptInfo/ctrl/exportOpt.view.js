define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	var ImportOptView = Page.extend(function() {
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			var table = panel.find('table');
			
			Core.ajax(Config.ContextPath + 'system/optlog/' + data.logId, {
				method: 'get'
			}).then(function(data) {
				form.form('load', data);
			});
		};
	});
	
	return ImportOptView;
});