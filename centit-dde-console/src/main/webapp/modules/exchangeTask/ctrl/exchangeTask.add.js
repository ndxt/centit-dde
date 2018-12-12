define(function(require) {

	var Config = require('config');
	var Page = require('core/page');

  var ExchangeTaskEditConAdd = require('../ctrl/exchangeTask.edit.conAdd');
  var ExchangeTaskExchangeMapinfoAdd = require('../ctrl/exchangeTask.ExchangeMapinfo.add');
  var ExchangeTaskexportSqlEdit = require('../ctrl/exchangeTask.exportSql.edit');
  var ExchangeTaskExchangeTaskdetailIdRemove = require('../ctrl/exchangeTask.ExchangeTaskdetailId.remove');
  var ExchangeTaskRun = require('../ctrl/exchangeTask.run');

	var ExchangeTaskAdd = Page.extend(function() {
    var _self = this;

    this.injecte([
      new ExchangeTaskEditConAdd('exchangeTask_ExchangeMapinfo_edit_conAdd'),
      new ExchangeTaskExchangeMapinfoAdd('exchangeTask_ExchangeMapinfo_add'),
      new ExchangeTaskexportSqlEdit('exchangeTask_exportSql_edit'),
      new ExchangeTaskExchangeTaskdetailIdRemove('exchangeTask_ExchangeTaskdetailId_remove'),
      new ExchangeTaskRun('exchangeTask_transfer_run'),
    ]);

		// @override
		// 加载页面时调用
		this.load = function(panel, data) {

			var form = panel.find('form');
			// 
			form.form('disableValidation').form('focus');

      var tab1table = panel.find('table.tab1');
      tab1table.cdatagrid({
        controller:_self,
        editable: true,
        dragSelection: true,
        onLoadSuccess:function(){
          $(this).datagrid('enableDnd');
        }
      });
		};
		
		// @override
		// 提交表单时调用
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
      var formData = form.form('value');
      $.extend(data,formData);
			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate');
      data.taskType = "1";
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
	
	return ExchangeTaskAdd;
});
