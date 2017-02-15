define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	
	var ExchangeTask3Add = require('./exchangeTask3.add');
	var ExchangeTask3ExchangeMapinfoAdd = require('../ctrl/exchangeTask3.ExchangeMapinfo.add');
	var ExchangeTask3exportSqlEdit = require('../ctrl/exchangeTask3.exportSql.edit');
	var ExchangeTask3ExchangeTaskdetailIdRemove = require('../ctrl/exchangeTask3.ExchangeTaskdetailId.remove');

	var ExchangeTask3Edit = ExchangeTask3Add.extend(function() {
		var _self = this;
		
		this.injecte([
		  			new ExchangeTask3ExchangeMapinfoAdd('exchangeTask3_ExchangeMapinfo_add'),
		  			new ExchangeTask3exportSqlEdit('exchangeTask3_exportSql_edit'),
		  			new ExchangeTask3ExchangeTaskdetailIdRemove('exchangeTask3_ExchangeTaskdetailId_remove'),
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

	return ExchangeTask3Edit;
});