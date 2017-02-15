define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	
	var ExchangeTask2Add = require('./exchangeTask2.add');
	var ExchangeTask2ExchangeMapinfoAdd = require('../ctrl/exchangeTask2.ExchangeMapinfo.add');
	var ExchangeTask2exportSqlEdit = require('../ctrl/exchangeTask2.exportSql.edit');
	var ExchangeTask2ExchangeTaskdetailIdRemove = require('../ctrl/exchangeTask2.ExchangeTaskdetailId.remove');

	var ExchangeTask2Edit = ExchangeTask2Add.extend(function() {
		var _self = this;
		
		this.injecte([
		  			new ExchangeTask2ExchangeMapinfoAdd('exchangeTask2_ExchangeMapinfo_add'),
		  			new ExchangeTask2exportSqlEdit('exchangeTask2_exportSql_edit'),
		  			new ExchangeTask2ExchangeTaskdetailIdRemove('exchangeTask2_ExchangeTaskdetailId_remove'),
		  		]);
		
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			Core.ajax(Config.ContextPath+'service/exchangetask/edit/'+data.taskId, {
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
				table.datagrid('reload');
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
				Core.ajax(Config.ContextPath + 'service/exchangetask/editAndsave/',{
					data: data,
					method:'put'
				}).then(closeCallback);
				
				
			}
//			return false让窗口等到响应后再关闭
			return false;
		};
		
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
		};
	});

	return ExchangeTask2Edit;
});