define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');

  var ExportOptExportFieldSync = require('../ctrl/exportOpt.exportField.sync');
  var ExportOptExportFieldAdd = require('../ctrl/exportOpt.exportField.add');
  var ExportOptExportFieldRemove = require('../ctrl/exportOpt.exportField.remove');
  var ExportOptExportTriggerAdd = require('../ctrl/exportOpt.exportTrigger.add');
  var ExportOptExportTriggerRemove = require('../ctrl/exportOpt.exportTrigger.remove');
  var DataOptExchangeMapinfoAdd = require('../ctrl/dataOptInfo.ExchangeMapinfo.add');

	var dataOptInfoAdd = Page.extend(function() {
		var _self = this;

    this.injecte([
      new ExportOptExportFieldSync('exportOpt_exportField_sync'),
      new ExportOptExportFieldAdd('exportOpt_exportField_add'),
      new ExportOptExportFieldRemove('exportOpt_exportField_remove'),
      new ExportOptExportTriggerAdd('exportOpt_exportTrigger_add'),
      new ExportOptExportTriggerRemove('exportOpt_exportTrigger_remove'),
      new DataOptExchangeMapinfoAdd('dataOptInfo_ExchangeMapinfo_add')
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
		};
		
		// @override
		this.submit = function(panel, data,closeCallback) {
			var form = panel.find('form');
      var formData = form.form('value');
      $.extend(data,formData);
			form.form('enableValidation');
			var isValid = form.form('validate');
			if (isValid) {
        var dataOptSteps = panel.find('table.tab1').datagrid("getData").rows;
        data.dataOptSteps = dataOptSteps;
				Core.ajax(Config.ContextPath + 'service/dataoptinfo/save', {
          data: data,
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
