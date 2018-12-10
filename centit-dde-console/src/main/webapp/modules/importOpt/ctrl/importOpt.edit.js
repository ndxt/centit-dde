define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	var ImportOptImportFieldSync = require('../ctrl/importOpt.importField.sync');
	var ImportOptImportFieldAdd = require('../ctrl/importOpt.importField.add');
	var ImportOptImportFieldRemove = require('../ctrl/importOpt.importField.remove');
	var ImportOptImportTriggerAdd = require('../ctrl/importOpt.importTrigger.add');
	var ImportOptImportTriggerRemove = require('../ctrl/importOpt.importTrigger.remove');


	var ImportOptEdit = Page.extend(function() {
		var _self = this;

		this.injecte([
			new ImportOptImportFieldSync('importOpt_importField_sync'),
			new ImportOptImportFieldAdd('importOpt_importField_add'),
			new ImportOptImportFieldRemove('importOpt_importField_remove'),
			new ImportOptImportTriggerAdd('importOpt_importTrigger_add'),
			new ImportOptImportTriggerRemove('importOpt_importTrigger_remove')
		]);
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			Core.ajax(Config.ContextPath+'service/importopt/edit/'+data.importId, {
				method: 'get'
			}).then(function(data) {
				_self.data = data;
				form.form('disableValidation')
					.form('load', data)
					.form('focus');
				var tab1table = panel.find('table.tab1');
				tab1table.cdatagrid({
					controller:_self,
					editable: true,
					data:data.importFields
				});
				var tab2table = panel.find('table.tab2');
				tab2table.cdatagrid({
					controller:_self,
					editable: true,
					data:data.importTriggers
				});
			});
            var databaseCode = "";
			$("#db").combobox({
				valueField: 'databaseCode',
				textField: 'databaseName',
				url:'service/platform/listDb',
				onSelect: function(rec) {
				    databaseCode = rec.databaseCode;
					var url = Config.ContextPath + 'service/platform/listTable/' + databaseCode;
					$('#table').combobox('reload', url);
				}
			});
			$("#table").combobox({
				valueField: 'tableName',
				textField: 'tableName',
				onSelect: function(rec) {
                    // var url = Config.ContextPath + 'service/platform/listField/' + databaseCode +"/" + rec.tableName;
                    // $('#field').combobox('reload', url);
					Core.ajax(Config.ContextPath + 'service/importopt/getFields/'+ databaseCode +"/" + rec.tableName,{
						method:'get',
					}).then(function(data){

						var tab1table = panel.find('table.tab1');
            tab1table.datagrid('loadData',data);
						/*tab1table.cdatagrid({
							controller:_self,
							editable: true,
							data:data
						});*/
					});
				}
			});
			$("#field").combobox({
				valueField: 'fieldType',
				textField: 'fieldName',
                method:'get',
                multiple: true,
				onSelect: function(rec) {
					Core.ajax({
						url: Config.ContextPath + 'service/exportsql/resolveQuerySql/',
						method:get,
					}).then(function(data){

						var tab1table = panel.find('table.tab1');
            tab1table.datagrid('loadData',data);
						/*tab1table.cdatagrid({
							controller:_self,
							editable: true,
							data:data
						});*/
					});
				}
			})
		};
		
		// @override
		this.submit = function(panel, data,closeCallback) {
			var form = panel.find('form');
			form.form('enableValidation');
			var isValid = form.form('validate');
			// if (isValid) {
			// 	var importFields = panel.find('table.tab1').datagrid("getData").rows;
			// 	var importTriggers = panel.find('table.tab2').datagrid("getData").rows;
			// 	data.importOpt.importFields = importFields;
			// 	data.importOpt.importTriggers = importTriggers;
            //
			// 	Core.ajax(Config.ContextPath + 'service/importopt/save/' + data.importOpt.importId, {
			// 		data: data.importOpt,
			// 		method: 'put'
			// 	}).then(function() {
			// 		closeCallback();
			// 	});
			// }
			if (isValid) {
				var importFields = panel.find('table.tab1').datagrid("getData").rows;
				var importTriggers = panel.find('table.tab2').datagrid("getData").rows;
				data.importFields = importFields;
				data.importTriggers = importTriggers;
				form.form('ajax', {
					url: Config.ContextPath + 'service/importopt/save/',
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


	return ImportOptEdit;
});
