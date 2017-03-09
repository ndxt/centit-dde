define(function(require) {

	var Page = require('core/page');
	var Config = require('config');
	var Core = require('core/core');
	
	
	var ExchangeMapinfoNewAddSourceDs = require('./exchangeMapinfoNew.addSourceDs');
	
	var ExchangeMapinfoNewAdd = Page.extend(function() {
		
		this.injecte([
          new ExchangeMapinfoNewAddSourceDs('exchangeMapinfoNew_addSourceDs'),
    	]);
		
		// @override
		// 加载页面时调用
		this.load = function(panel, data) {

			var form = panel.find('form');
			// 
			form.form('disableValidation')
			.form('focus');
			onchange();
		};
		
		// @override
		// 提交表单时调用
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			
			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath + 'service/exchangemapinfonew/add',
					method: 'post'
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
	
	
	
	
	
	
	return ExchangeMapinfoNewAdd;
});