define(function(require) {

	var Config = require('config');
	var Page = require('core/page');

  var webServiceTaskExchangeMapinfoAdd = require('../ctrl/webServiceTask.ExchangeMapinfo.add');
  var webServiceTaskexportSqlEdit = require('../ctrl/webServiceTask.exportSql.edit');
  var webServiceTaskExchangeTaskdetailIdRemove = require('../ctrl/webServiceTask.ExchangeTaskdetailId.remove');
  var webServiceTaskRun = require('../ctrl/webServiceTask.run');

	var webServiceTaskAdd = Page.extend(function() {
    var _self = this;

    this.injecte([
      new webServiceTaskExchangeMapinfoAdd('webServiceTask_ExchangeMapinfo_add'),
      new webServiceTaskexportSqlEdit('webServiceTask_exportSql_edit'),
      new webServiceTaskExchangeTaskdetailIdRemove('webServiceTask_ExchangeTaskdetailId_remove'),
      new webServiceTaskRun('webServiceTask_transfer_run'),
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
			data.taskType = "4";
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
	
	return webServiceTaskAdd;
});
