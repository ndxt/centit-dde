define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	var ExportOptExportFieldSync = require('../ctrl/exportOpt.exportField.sync');
	var ExportOptExportFieldAdd = require('../ctrl/exportOpt.exportField.add');
	var ExportOptExportFieldRemove = require('../ctrl/exportOpt.exportField.remove');
	var ExportOptExportTriggerAdd = require('../ctrl/exportOpt.exportTrigger.add');
	var ExportOptExportTriggerRemove = require('../ctrl/exportOpt.exportTrigger.remove');


	var ExportOptAdd = Page.extend(function() {
		var _self = this;

		this.injecte([
			new ExportOptExportFieldSync('exportOpt_exportField_sync'),
			new ExportOptExportFieldAdd('exportOpt_exportField_add'),
			new ExportOptExportFieldRemove('exportOpt_exportField_remove'),
			new ExportOptExportTriggerAdd('exportOpt_exportTrigger_add'),
			new ExportOptExportTriggerRemove('exportOpt_exportTrigger_remove')
		]);
		
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
				var exportFields = panel.find('table.tab1').datagrid("getData").rows;
				var exportTriggers = panel.find('table.tab2').datagrid("getData").rows;
				data.exportOpt.exportFields = exportFields;
				data.exportOpt.exportTriggers = exportTriggers;

				Core.ajax(Config.ContextPath + 'service/exportsql/save/' + data.exportSql.exportId, {
					data: data.exportOpt,
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
	
	return ExportOptAdd;
});