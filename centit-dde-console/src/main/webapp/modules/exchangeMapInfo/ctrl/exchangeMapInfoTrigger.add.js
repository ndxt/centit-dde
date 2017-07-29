define(function(require) {
	var Page = require('core/page');
	var Config = require('config');


	var ExchangeMapInfoTriggerAdd = Page.extend(function() {
		var _self = this;

		this.load = function(panel, data) {

			var form = panel.find('form');
			//
			form.form('disableValidation')
				.form('focus');

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
					url: Config.ContextPath + 'service/exchangemapinfo/save',
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
	
	return ExchangeMapInfoTriggerAdd;
});