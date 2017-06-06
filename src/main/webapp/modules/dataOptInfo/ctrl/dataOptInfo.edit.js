define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');


	var DataOptInfoEdit = Page.extend(function() {
		var _self = this;

		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			Core.ajax(Config.ContextPath+'service/exportsql/edit/'+data.exportId, {
				method: 'get'
			}).then(function(data) {
				_self.data = data;
				form.form('disableValidation')
					.form('load', data.exportSql)
					.form('focus');
			});


		};
		
		// @override
		this.submit = function(panel, data,closeCallback) {
			var form = panel.find('form');
			form.form('enableValidation');
			var isValid = form.form('validate');
			if (isValid) {
				var exportFields = panel.find('table.tab1').datagrid("getData").rows;
				var exportTriggers = panel.find('table.tab2').datagrid("getData").rows;
				data.exportFields = exportFields;
				data.exportTriggers = exportTriggers;
				form.form('ajax', {
					url: Config.ContextPath + 'service/exportsql/save',
					method: 'put',
					data: data
//					提交成功后调用closeCallback关闭对话框
				}).then(closeCallback);
			}
			return false;
		};
		
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
        };

	});

	this.sync = function() {
		var form = $("form");
		form.form('enableValidation');
		var isValid = form.form('validate');
		if (isValid) {
			form.form('ajax', {
				url: Config.ContextPath + 'service/exportsql/resolveQuerySql',
				method: 'post',
				data: {
					querySql: $("#querySql").val(),
					sourceDatabaseName: $("#db").combobox('getValue')
				}
			}).then(function (data) {
				var tab1table = $('table.tab1');
				tab1table.cdatagrid({
					controller: this,
					editable: true,
					data: data
				});
			})
		}
	}

	return DataOptInfoEdit;
});