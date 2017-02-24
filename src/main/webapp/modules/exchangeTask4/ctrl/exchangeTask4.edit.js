define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	
	var ExchangeTask4Add = require('./exchangeTask4.add');
	var ExchangeTask4ExchangeMapinfoAdd = require('../ctrl/exchangeTask4.ExchangeMapinfo.add');
	var ExchangeTask4exportSqlEdit = require('../ctrl/exchangeTask4.exportSql.edit');
	var ExchangeTask4ExchangeTaskdetailIdRemove = require('../ctrl/exchangeTask4.ExchangeTaskdetailId.remove');
	var ExchangeTask4Run = require('../ctrl/exchangeTask4.run');

	var ExchangeTask4Edit = ExchangeTask4Add.extend(function() {
		var _self = this;
		
		this.injecte([
		  			new ExchangeTask4ExchangeMapinfoAdd('exchangeTask4_ExchangeMapinfo_add'),
		  			new ExchangeTask4exportSqlEdit('exchangeTask4_exportSql_edit'),
		  			new ExchangeTask4ExchangeTaskdetailIdRemove('exchangeTask4_ExchangeTaskdetailId_remove'),
		  			new ExchangeTask4Run('exchangeTask4_transfer_run'),
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

	return ExchangeTask4Edit;
});