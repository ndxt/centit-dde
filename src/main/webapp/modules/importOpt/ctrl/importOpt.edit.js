define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	var ImportOptImportFieldSync = require('../ctrl/importOpt.importField.sync');
	var ImportOptImportFieldAdd = require('../ctrl/importOpt.importField.add');
	var ImportOptImportFieldRemove = require('../ctrl/importOpt.importField.remove');
	var ImportOptImportTriggerAdd = require('../ctrl/importOpt.importTrigger.add');
	var ImportOptImportTriggerRemove = require('../ctrl/importOpt.importTrigger.remove');


	var ImportOptEdit = Page.extend(function() {
		var _self = this;

		this.injecte([
			new ImportOptImportFieldSync('importOpt_importField_sync'),
			new ImportOptImportFieldAdd('importOpt_importField_add'),
			new ImportOptImportFieldRemove('importOpt_importField_remove'),
			new ImportOptImportTriggerAdd('importOpt_importTrigger_add'),
			new ImportOptImportTriggerRemove('importOpt_importTrigger_remove')
		]);
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			Core.ajax(Config.ContextPath+'service/importopt/edit/'+data.importId, {
				method: 'get'
			}).then(function(data) {
				_self.data = data;
				form.form('disableValidation')
					.form('load', data.importOpt)
					.form('focus');
				var tab1table = panel.find('table.tab1');
				tab1table.cdatagrid({
					controller:_self,
					editable: true,
					data:data.importOpt.importFields
				});
				var tab2table = panel.find('table.tab2');
				tab2table.cdatagrid({
					controller:_self,
					editable: true,
					data:data.importOpt.importTriggers
				});
			});
		};
		
		// @override
		this.submit = function(panel, data,closeCallback) {
			var form = panel.find('form');
			form.form('enableValidation');
			var isValid = form.form('validate');
			if (isValid) {
				var importFields = panel.find('table.tab1').datagrid("getData").rows;
				var importTriggers = panel.find('table.tab2').datagrid("getData").rows;
				data.importOpt.importFields = importFields;
				data.importOpt.importTriggers = importTriggers;

				Core.ajax(Config.ContextPath + 'service/importopt/save/' + data.importOpt.importId, {
					data: data.importOpt,
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
	
	return ImportOptEdit;
});