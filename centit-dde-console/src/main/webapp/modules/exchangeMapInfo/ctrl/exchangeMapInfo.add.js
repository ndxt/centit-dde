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
			formData.querySql = $('#txt_querySql').textbox('getValue');
      $.extend(data,formData);
			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
        var mapInfoDetails = panel.find('table.source').datagrid("getData").rows;
        var mapInfoTriggers = panel.find('table.trigger').datagrid("getData").rows;
        data.mapInfoDetails = mapInfoDetails;
        data.mapInfoTriggers = mapInfoTriggers;
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
	this.isPK = function(value, row, index) {
		if (row.isPk == 'Y') {
			return "是";
		} else {
			return "否";
		}
	};
	this.isNULL = function(value, row, index) {
		if (row.isNull == 'Y') {
			return "是";
		} else {
			return "否";
		}
	};

	this.sourceDb = function() {

		$('#sourceDb').dialog({
			title: '数据库编辑',
			resizable: true,
			modal: true,
		}).dialog("open");

		$("#txt_sourceDatabaseName").combobox({
			valueField: 'databaseCode',
			textField: 'databaseName',
			url: 'service/platform/listDb',
			onSelect: function (rec) {
				var url = Config.ContextPath + 'service/platform/listTable/' + rec.databaseCode;
				$('#txt_sourceTableName').combobox('reload', url);
			}
		});

		var url = Config.ContextPath + 'service/platform/listTable/0000000082';
		$('#txt_sourceTableName').combobox('reload', url);
		$("#txt_sourceTableName").combobox({
			valueField: 'tableName',
			textField: 'tableName',
			onSelect: function (rec) {
				if (rec.tableName != ($("#sourceTableName").textbox('getValue'))) {
					// var databaseCode = $("#txt_sourceDatabaseName").combobox("getValue");
					var url = Config.ContextPath + 'service/platform/generateSQL/0000000082/' + rec.tableName;
					Core.ajax(url, {
						method: 'get'
					}).then(function (data) {
						$('#txt_querySql').textbox("setValue", data);
					});
				}
			}
		});

		$("#txt_sourceDatabaseName").combobox("setValue", $("#sourceDatabaseCode").val());
		$("#txt_sourceTableName").combobox("setValue", $("#sourceTableName").textbox('getValue'));
		var sourceTable = $("#exchangeContent1").datagrid("getRows");
		if(sourceTable.length == 0){
			return;
		}
		var sql = 'SELECT';
		for(var i=0;i<sourceTable.length;i++){
			if(i == 0){
				sql = sql + " " + sourceTable[i].sourceFieldSentence;
			}else{
				sql = sql +", "+ sourceTable[i].sourceFieldSentence;
			}
		}
		sql = sql + " FROM " + $("#sourceTableName").val();
		$("#txt_querySql").textbox('setValue', sql);
	};

	this.destDb = function(){

		$('#destDb').dialog({
			title:'数据库编辑',
			resizable: true,
			modal: true,
		}).dialog("open");

		$("#txt_destDatabaseName").combobox({
			valueField: 'databaseCode',
			textField: 'databaseName',
			url:'service/platform/listDb',
			onSelect: function(rec) {
				var url = Config.ContextPath + 'service/platform/listTable/' + rec.databaseCode;
				$('#txt_destTableName').combobox('reload', url);
			}
		});

		$("#txt_destTableName").combobox({
			valueField: 'tableName',
			textField: 'tableName',
		});

		$("#txt_destDatabaseName").combobox("setValue", $('#destDatabaseCode').val());
		$("#txt_destTableName").combobox("setValue", $("#destTableName").textbox("getValue"));
	};

	this.saveSourceDb = function(){

		var sourceQuerySql = $('#txt_querySql').textbox('getValue');
		var sourceTableName = $("#txt_sourceTableName").combobox('getValue');
		var sourceDatabaseCode = $("#txt_sourceDatabaseName").combobox('getValue');
		var sourceDatabaseName = $("#txt_sourceDatabaseName").combobox('getText');

		$("#sourceDatabaseName").textbox("setValue",sourceDatabaseName);
		$("#sourceDatabaseCode").val(sourceDatabaseCode);
		$("#sourceTableName").textbox("setValue",sourceTableName);

		var url = Config.ContextPath + 'service/exchangemapinfo/resolveSQL/';
		Core.ajax(url,{
			method:'get',
			data:{
				databaseCode:sourceDatabaseCode,
				querySql:sourceQuerySql
			}
		}).then(function(data){
			$(".source").cdatagrid({
				data:data
			})
		});

		$('#sourceDb').dialog("close");
	};

	this.saveDestDb = function(){
		var databaseCode = $("#txt_destDatabaseName").combobox('getValue');
		var databaseName = $("#txt_destDatabaseName").combobox('getText');
		var tableName = $("#txt_destTableName").combobox('getValue');

		$("#destDatabaseName").textbox("setValue",databaseName);
		$("#destDatabaseCode").val(databaseCode);
		$("#destTableName").textbox("setValue",tableName);
		Core.ajax(Config.ContextPath + 'service/platform/generateSQL/'+ databaseCode + '/' + tableName,{
			method:'get'
		}).then(function(sql){
			Core.ajax(Config.ContextPath + 'service/exchangemapinfo/resolveSQL/',{
				method:'get',
				data:{
					databaseCode:databaseCode,
					querySql:sql
				}
			}).then(function(fields){
				$(".dest").cdatagrid({
					data:fields
				})
			});
		});

		$('#destDb').dialog("close");
	};

	this.closeSourceDb = function(){
		$('#sourceDb').dialog("close");
	};

	this.closeDestDb = function(){
		$('#destDb').dialog("close");
	};

	return ExchangeMapInfoAdd;
});
