define(function(require) {
	var Page = require('core/page');


	var ExchangeMapInfoTriggerAdd = Page.extend(function() {
		var _self = this;

		this.load = function(panel, data) {
			var form = panel.find('form');
// 			Core.ajax(Config.ContextPath+'service/exchangemapinfo/edit/'+data.mapInfoId, {
// 				type: 'json',
// 				method: 'get'
// //					.then()是Promise规范规定的异步调用方法    得到数据后，调用form的load方法将数据显示在表单中
// 			}).then(function(data) {
			_self.data = data;

			form.form('load', data)
				.form('disableValidation')
				.form('readonly', 'mapInfoId')
				.form('focus');

			// });
		};

		// @override
		this.submit = function(table, data, closeCallback) {

		};
		
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
        };
	});
	
	return ExchangeMapInfoTriggerAdd;
});