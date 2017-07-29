define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');


	var dataOptInfoAdd = Page.extend(function() {
		var _self = this;


		// @override
		this.load = function(panel) {
			var form = panel.find('form');
			form.form('disableValidation')
				.form('focus');
			var tab1table = panel.find('table.tab1');
			tab1table.cdatagrid({
				controller:_self,
				editable: true
			});
			var tab2table = panel.find('table.tab2');
			tab2table.cdatagrid({
				controller:_self,
				editable: true
			});
		};
		
		// @override
		this.submit = function(panel, data,closeCallback) {
			var form = panel.find('form');
			form.form('enableValidation');
			var isValid = form.form('validate');
			if (isValid) {

				Core.ajax(Config.ContextPath + 'service/exportsql/save/' + data.exportSql.exportId, {
					data: data.dataOptInfo,
					method: 'put'
				}).then(function() {
					closeCallback();
				});
			}
			return false;
		};
		
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
        };
	});
	
	return dataOptInfoAdd;
});