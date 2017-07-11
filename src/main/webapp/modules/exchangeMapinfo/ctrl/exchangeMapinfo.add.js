define(function(require) {

	var Page = require('core/page');
	var Config = require('config');
	var Core = require('core/core');

	var ExchangeMapInfoDetailAdd = require('../ctrl/exchangeMapInfoDetail.add');
	var ExchangeMapInfoDetailRemove = require('../ctrl/exchangeMapInfoDetail.remove');
	var ExchangeMapInfoDetailAdd2 = require('../ctrl/exchangeMapInfoDetail.add2');
	var ExchangeMapInfoDetailRemove2 = require('../ctrl/exchangeMapInfoDetail.remove2');
	var ExchangeMapInfoTriggerAdd = require("../ctrl/exchangeMapInfoTrigger.add");
	var ExchangeMapInfoTriggerEdit = require("../ctrl/exchangeMapInfoTrigger.edit");
	var ExchangeMapInfoTriggerRemove = require("../ctrl/exchangeMapInfoTrigger.remove");

	var ExchangeMapInfoAdd = Page.extend(function() {
		var _self = this;
		
		this.injecte([
			new ExchangeMapInfoDetailAdd('source_detail_add'),
			new ExchangeMapInfoDetailRemove('source_detail_remove'),
			new ExchangeMapInfoDetailAdd2('dest_detail_add'),
			new ExchangeMapInfoDetailRemove2('dest_detail_remove'),
			new ExchangeMapInfoTriggerAdd('exchangeMapInfoTrigger_add'),
			new ExchangeMapInfoTriggerEdit('exchangeMapInfoTrigger_edit'),
			new ExchangeMapInfoTriggerRemove('exchangeMapInfoTrigger_remove')
    	]);
		
		// @override
		// 加载页面时调用
		this.load = function(panel, data) {

			var form = panel.find('form');

			form.form('disableValidation').form('focus');

			var sourceTable = panel.find('table.source');
			sourceTable.cdatagrid({
				controller:_self,
				editable: true,
				dragSelection: true,
				onLoadSuccess:function(){
					$(this).datagrid('enableDnd');
				}
			});
			var destTable = panel.find('table.dest');
			destTable.cdatagrid({
				controller:_self,
				editable: true,
				dragSelection: true,
				onLoadSuccess:function(){
					$(this).datagrid('enableDnd');
				}
			});
			var triggerTable = panel.find('table.trigger');
			triggerTable.cdatagrid({
				controller:_self,
				editable: true,
				dragSelection: true,
				onLoadSuccess:function(){
					$(this).datagrid('enableDnd');
				}
			});
			// loadCombobox();
		};
		
		// @override
		// 提交表单时调用
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			var formData = form.form('value');
			$.extend(data,formData);
			
			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
				form.form('ajax', {
					url: Config.ContextPath + 'service/exchangemapinfo/save',
					method: 'put',
					data: data,
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

	this.dlgAddDb = function(change){
		$('#dlgAddDbLeft').dialog({
			title:'数据库编辑',
			resizable: true,
			modal: true,
		});
		$('#dlgAddDbLeft').dialog("open");

		$("#txt_sourceDatabaseName").combobox({
			valueField: 'databaseCode',
			textField: 'databaseName',
			url:'service/platform/listDb',
			onSelect: function(rec) {
				var url = Config.ContextPath + 'service/platform/listTable/' + rec.databaseCode;
				$('#txt_sourceTableName').combobox('reload', url);
			}
		});

		$("#txt_sourceTableName").combobox({
			valueField: 'tableName',
			textField: 'tableName',
			// url:'service/platform/listTable',
			onSelect: function(rec) {
			    var databaseCode = $("#txt_sourceDatabaseName");
				var url = Config.ContextPath + 'service/platform/generateSQL/' + databaseCode + '/ '+ rec.tableName;
                Core.ajax(url,{
                    method: 'get'
                }).then(function(data) {
                    var d = data;
                })
				// $('#txt_querySql');
			}
		});

	};

	return ExchangeMapInfoAdd;
});