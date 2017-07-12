define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	var ExchangeMapInfoAdd = require('./exchangeMapInfo.add');
	var ExchangeMapInfoDetailAdd = require('../ctrl/exchangeMapInfoDetail.add');
	var ExchangeMapInfoDetailRemove = require('../ctrl/exchangeMapInfoDetail.remove');
	var ExchangeMapInfoDetailAdd2 = require('../ctrl/exchangeMapInfoDetail.add2');
	var ExchangeMapInfoDetailRemove2 = require('../ctrl/exchangeMapInfoDetail.remove2');
	var ExchangeMapInfoTriggerAdd = require("../ctrl/exchangeMapInfoTrigger.add");
	var ExchangeMapInfoTriggerEdit = require("../ctrl/exchangeMapInfoTrigger.edit");
	var ExchangeMapInfoTriggerRemove = require("../ctrl/exchangeMapInfoTrigger.remove");

	var ExchangeMapInfoEdit = ExchangeMapInfoAdd.extend(function() {
		var _self = this;

		this.injecte([
		            // new ExchangeMapInfoAdd('exchangeMapInfo_add'),
		  			new ExchangeMapInfoDetailAdd('source_detail_add'),
		  			new ExchangeMapInfoDetailRemove('source_detail_remove'),
		  			new ExchangeMapInfoDetailAdd2('dest_detail_add'),
		  			new ExchangeMapInfoDetailRemove2('dest_detail_remove'),
					new ExchangeMapInfoTriggerAdd('exchangeMapInfoTrigger_add'),
					new ExchangeMapInfoTriggerEdit('exchangeMapInfoTrigger_edit'),
					new ExchangeMapInfoTriggerRemove('exchangeMapInfoTrigger_remove')
		  		]);

		//@override
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

				var sourceTable = panel.find('table.source');
				sourceTable.cdatagrid({
					controller:_self,
					editable: true,
					data:data.mapInfoDetails,
					dragSelection: true,
					onLoadSuccess:function(){
						$(this).datagrid('enableDnd');
					}
				});
				var destTable = panel.find('table.dest');
				destTable.cdatagrid({
					controller:_self,
					editable: true,
					data:data.mapInfoDetails,
					dragSelection: true,
					onLoadSuccess:function(){
						$(this).datagrid('enableDnd');
					}
				});
				var tab2table = panel.find('table.trigger');
				tab2table.cdatagrid({
					controller:_self,
					editable: true,
					data:data.mapInfoTriggers,
                    dragSelection: true,
                    onLoadSuccess:function(){
                        $(this).datagrid('enableDnd');
                    }
				});
			// });
		};
	});

	this.sourceDb = function() {
		// var sourceDatabaseName = $('#sourceDatabaseName').val();
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

		$("#txt_sourceTableName").combobox({
			valueField: 'tableName',
			textField: 'tableName',
			// url:'service/platform/listTable',
			onSelect: function (rec) {
				var url = Config.ContextPath + 'service/platform/listFields/' + rec.tableName;
				// $('#txt_querySql');
			}
		});
	};

	this.destDb = function(){
		// var sourceDatabaseName = $('#sourceDatabaseName').val();
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
			// url:'service/platform/listTable',
			onSelect: function(rec) {
				var url = Config.ContextPath + 'service/platform/listFields/' + rec.tableName;
				// $('#txt_querySql');
			}
		});
	};

	this.saveSourceDb = function(){

		 var txt_querySql = $('#txt_querySql').val();
		 var text_Table = $("#txt_sourceTablename").combobox('getValue');
		 var value_sourceCodename = $("#txt_sourceCodename").combobox('getValue');

//		 var sql="select" ;
//		 var table = $("#exchangeContent1").datagrid("getRows");
//		 for(var i=0;i<table.length;i++){
//			 if(i!=table.length-1){
//				 sql = sql +" "+ table[i].sourceFieldName+",";
//			 }else{
//				 sql = sql +" "+ table[i].sourceFieldName;
//			 }
//		 }
////		 sql
//		 sql = sql + " FORM "+text_Table;
//		 $("#querySql").val(sql);
////		 select
//		 var txt_sourceDatabaseName =  $('#txt_sourceDatabaseName').combobox('getValue');
//		 $("#sourceDatabaseName").textbox("setValue", txt_sourceDatabaseName);
		 $('#sourceDb').dialog("close");
	};

	this.saveDestDb = function(){

	};

	this.closeSourceDb = function(){
		$('#sourceDb').dialog("close");
	};

	this.closeDestDb = function(){
		$('#destDb').dialog("close");
	};



	return ExchangeMapInfoEdit;
});