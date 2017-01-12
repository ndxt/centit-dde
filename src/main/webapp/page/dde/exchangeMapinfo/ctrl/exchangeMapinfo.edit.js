define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var ExchangeMapinfoAdd = require('./exchangemapinfo.add');

	var ExchangeMapinfoEdit = ExchangeMapinfoAdd.extend(function() {
		var _self = this;
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			Core.ajax(Config.ContextPath+'service/flow/optprocinfo/'+data.cid, {
				type: 'json',
				method: 'get' 
//					.then()是Promise规范规定的异步调用方法    得到数据后，调用form的load方法将数据显示在表单中
			}).then(function(data) {
				_self.data = data;
				
				form.form('load', data)
					.form('disableValidation')
					.form('readonly', 'cid')
					.form('focus');
			});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
//			enableValidation开启表单校验，validate校验表单数据
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath + 'service/flow/optprocinfo/' + data.cid,
					method: 'put',
					data: data 
//					提交成功后调用closeCallback关闭对话框
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

	return ExchangeMapinfoEdit;
});