define(function(require) {

	var Config = require('config');
	var Page = require('core/page');

  var importTaskExchangeMapinfoAdd = require('../ctrl/importTask.ExchangeMapinfo.add');
  var importTaskexportSqlEdit = require('../ctrl/importTask.exportSql.edit');
  var importTaskExchangeTaskdetailIdRemove = require('../ctrl/importTask.ExchangeTaskdetailId.remove');

	var importTaskAdd = Page.extend(function() {
    var _self = this;

    this.injecte([
            new importTaskExchangeMapinfoAdd('importTask_ExchangeMapinfo_add'),
            new importTaskexportSqlEdit('importTask_exportSql_edit'),
            new importTaskExchangeTaskdetailIdRemove('importTask_ExchangeTaskdetailId_remove'),
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
      //table.datagrid('reload');
		};
		
		// @override
		// 提交表单时调用
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate');
      data.taskType = "3";
			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath + 'service/exchangetask/save',
          data:data,
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
	
	return importTaskAdd;
});
