define(function(require) {

	var Config = require('config');
	var Page = require('core/page');

  var exportTaskEditConAdd = require('./exportTask.edit.conAdd');
  var exportTaskExchangeMapinfoAdd = require('../ctrl/exportTask.ExchangeMapinfo.add');
  var exportTaskexportSqlEdit = require('../ctrl/exportTask.exportSql.edit');
  var exportTaskExchangeTaskdetailIdRemove = require('../ctrl/exportTask.ExchangeTaskdetailId.remove');

	var exportTaskAdd = Page.extend(function() {
    var _self = this;

    this.injecte([
      new exportTaskEditConAdd('exportTask_ExchangeMapinfo_edit_conAdd'),
      new exportTaskExchangeMapinfoAdd('exportTask_ExchangeMapinfo_add'),
      new exportTaskexportSqlEdit('exportTask_exportSql_edit'),
      new exportTaskExchangeTaskdetailIdRemove('exportTask_ExchangeTaskdetailId_remove'),
    ]);
		
		// @override
		// 加载页面时调用
		this.load = function(panel, data) {

			var form = panel.find('form');
			// 
			form.form('disableValidation')
			.form('focus');

      var tab2table = panel.find('table.tab2');
      tab2table.cdatagrid({
        controller:_self,
        editable: true,
      });

      var tab1table = panel.find('table.tab1');
      tab1table.cdatagrid({
        controller:_self,
        editable: true,
      });
		};
		
		// @override
		// 提交表单时调用
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate');
      data.taskType = "2";
			if (isValid) {
        var exchangeTaskDetails = panel.find('table.tab1').datagrid("getData").rows;
        data.exchangeTaskDetails = exchangeTaskDetails;
				form.form('ajax', {
					url: Config.ContextPath + 'service/exchangetask/save',
          data: data,
					method: 'put'
				}).then(closeCallback);
			}
			
			return false;
		};
		
		// @override 
		// 窗口关闭时调用
		this.onClose = function(table, data) {
			table.datagrid('reload');
		};
	});
	
	return exportTaskAdd;
});
