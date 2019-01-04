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

	var DataOptInfoEdit = Page.extend(function() {
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
		this.load = function(panel, data) {
			var form = panel.find('form');
			Core.ajax(Config.ContextPath+'service/dataoptinfo/edit/'+data.dataOptId, {
				method: 'get'
			}).then(function(data) {
				_self.data = data;
				form.form('load', data)
          .form('disableValidation')
          .form('readonly', 'dataOptId')
					.form('focus');
        var tab1table = panel.find('table.tab1');
        tab1table.cdatagrid({
          controller:_self,
          editable: true,
          data:data.dataOptSteps
        });
			});

		};
		
		// @override
		this.submit = function(panel, data,closeCallback) {
			var form = panel.find('form');
			form.form('enableValidation');
			var isValid = form.form('validate');
			if (isValid) {
				var dataOptSteps = panel.find('table.tab1').datagrid("getData").rows;
				data.dataOptSteps = dataOptSteps;
				form.form('ajax', {
					url: Config.ContextPath + 'service/dataoptinfo/save',
					method: 'put',
					data: data
//					提交成功后调用closeCallback关闭对话框
				}).then(closeCallback);
			}
			return false;
		};
		
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
        };

	});

	this.sync = function() {
		var form = $("form");
		form.form('enableValidation');
		var isValid = form.form('validate');
		if (isValid) {
			form.form('ajax', {
				url: Config.ContextPath + 'service/exportsql/resolveQuerySql',
				method: 'post',
				data: {
					querySql: $("#querySql").val(),
					sourceDatabaseName: $("#db").combobox('getValue')
				}
			}).then(function (data) {
				var tab1table = $('table.tab1');
        tab1table.datagrid('loadData',data);
				/*tab1table.cdatagrid({
					controller: this,
					editable: true,
					data: data
				});*/
			})
		}
	}

	return DataOptInfoEdit;
});
