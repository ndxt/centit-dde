define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	
	var webServiceTaskAdd = require('./webServiceTask.add');
	var webServiceTaskExchangeMapinfoAdd = require('../ctrl/webServiceTask.ExchangeMapinfo.add');
	var webServiceTaskexportSqlEdit = require('../ctrl/webServiceTask.exportSql.edit');
	var webServiceTaskExchangeTaskdetailIdRemove = require('../ctrl/webServiceTask.ExchangeTaskdetailId.remove');
	var webServiceTaskRun = require('../ctrl/webServiceTask.run');

	var webServiceTaskEdit = webServiceTaskAdd.extend(function() {
		var _self = this;
		
		this.injecte([
		  			new webServiceTaskExchangeMapinfoAdd('webServiceTask_ExchangeMapinfo_add'),
		  			new webServiceTaskexportSqlEdit('webServiceTask_exportSql_edit'),
		  			new webServiceTaskExchangeTaskdetailIdRemove('webServiceTask_ExchangeTaskdetailId_remove'),
		  			new webServiceTaskRun('webServiceTask_transfer_run'),
		  		]);
		
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			Core.ajax(Config.ContextPath+'service/exchangetask/edit/'+data.taskId+'/4/'+-1, {
				method: 'get',
				data: {
                    _method: 'get'
                }
			}).then(function(data) {
				_self.data = data;
				
				form.form('load', data)
					.form('disableValidation')
					.form('readonly', 'taskId')
					.form('focus');
				
				var tab2table = panel.find('table.tab2');
				tab2table.cdatagrid({
					controller:_self,
					editable: true,
					data:data.taskLogs
				});
				
				var tab1table = panel.find('table.tab1');
				tab1table.cdatagrid({
					controller:_self,
					editable: true,
					data:data.exportSqlList
				});
			});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			var formData = form.form('value');
			$.extend(data,formData);
			// 开启校验
//			enableValidation开启表单校验，validate校验表单数据
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
        var exchangeTaskDetails = panel.find('table.tab1').datagrid("getData").rows;
        data.exchangeTaskDetails = exchangeTaskDetails;
				Core.ajax(Config.ContextPath + 'service/exchangetask/editAndsave',{
					data: data,
					method:'put'
				}).then(closeCallback);
				
				
			}
//			return false让窗口等到响应后再关闭
			return false;
		};
		
		// @override
		this.onClose = function(table,panel) {
			table.datagrid('reload');
		};
	});

	return webServiceTaskEdit;
});
