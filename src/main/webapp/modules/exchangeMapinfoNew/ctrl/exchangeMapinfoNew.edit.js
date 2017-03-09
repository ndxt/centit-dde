define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	var ExchangeMapinfoNewAdd = require('./exchangeMapinfoNew.add');
	var exchangeMapinfoNewDetailAdd = require('../ctrl/exchangeMapinfoNewDetail.add');
	var ExchangeMapinfoNewDetailRemove = require('../ctrl/exchangeMapinfoNewDetail.remove');

	var ExchangeMapinfoNewEdit = ExchangeMapinfoNewAdd.extend(function() {
		var _self = this;
		
		
		this.injecte([
		            new ExchangeMapinfoNewAdd('exchangeMapinfoNew_add'),
		  			new exchangeMapinfoNewDetailAdd('exchangeMapinfoNew_Detail_add'),
		  			new ExchangeMapinfoNewDetailRemove('exchangeMapinfoNew_Detail_remove'),
		  		]);
		
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			Core.ajax(Config.ContextPath+'service/exchangemapinfo/edit/'+data.mapinfoId, {
				type: 'json',
				method: 'get' 
//					.then()是Promise规范规定的异步调用方法    得到数据后，调用form的load方法将数据显示在表单中
			}).then(function(data) {
				_self.data = data;
				
				form.form('load', data)
					.form('disableValidation')
					.form('readonly', 'mapinfoId')
					.form('focus');
				
				var tab1table = panel.find('table.tab1');
				tab1table.cdatagrid({
					controller:_self,
					editable: true,
					data:data.mapinfoDetails
				});
				
				var tab1table1 = panel.find('table.tab2');
				tab1table1.cdatagrid({
					controller:_self,
					editable: true,
					data:data.mapinfoTriggers
				});
			});
			
			onchange();
			
			
		};
		
		
		this.submit = function(panel, data,closeCallback) {
			var form = panel.find('form');
			var formData = form.form('value');
			$.extend(data,formData);
			var isValid = form.form('validate');
			if (isValid) {
				var mapinfoDetails = panel.find('table.tab1').datagrid("getData").rows;
				data.mapinfoDetails = mapinfoDetails;
				Core.ajax(Config.ContextPath + 'service/exchangemapinfo/save/' + data.mapinfoId, {
					data: data,
					method: 'put'
				}).then(function() {
					closeCallback();
				});
			}
			return false;
		};
		
		// @override
		this.onClose = function(table) {
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
		 Core.ajax(Config.ContextPath+'service/exchangemapinfonew/getListDatebaseInfo', {
				method: 'get',
				data: {
                _method: 'get'
            }
			}).then(function(data) {
				var orgValue;
				var orgNameValue;
				var dataList;
				dataList = [];
				$.each(data,function(index,item){
				    orgValue = data[index].databaseCode;
				    orgNameValue = data[index].databaseName;
				    dataList.push({"value": orgValue,"text":orgNameValue});
				});
				$('#txt_sourceDatabaseName').combobox('select','未选');
				$("#txt_sourceDatabaseName").combobox("loadData",dataList);
			});
//		 sql
		 var querySql=$('#querySql').val();
		 $("#txt_querySql").textbox("setValue", querySql);
//		 表
		 var table = $('#sourceTablename').val();
		 $("#txt_sourceTablename").textbox("setValue", table);
//		 select
	};
	
	
	this.onchange = function (){
		if(document.getElementById('txt_sourceDatabaseName')){
			$('#txt_sourceDatabaseName').combobox({
				onChange: function (newValue, oldValue) {
					Core.ajax(Config.ContextPath+'service/exchangemapinfonew/getTables/'+newValue, {
						method: 'get',
						data: {
		                 _method: 'get'
		             }
					}).then(function(data) {
						var orgValue;
						var orgNameValue;
						var TabledataList;
						TabledataList = [];
						$.each(data,function(index,item){
						    orgValue = data[index];
						    orgNameValue = data[index];
						    TabledataList.push({"value": orgValue,"text":orgNameValue});
						});
						$("#txt_sourceTablename").combobox("loadData",TabledataList);
					});
				}
			});
		}
	}
	
	
	this.saveDbLeft = function(){
		
		 var text_Table = $("#txt_sourceTablename").val();
		 var sql="select" ;
		 var table = $("#exchangeContent").datagrid("getRows");
		 for(var i=0;i<table.length;i++){
			 if(i!=table.length-1){
				 sql = sql +" "+ table[i].sourceFieldName+",";
			 }else{
				 sql = sql +" "+ table[i].sourceFieldName;
			 }
		 }
//		 sql
		 sql = sql + " FORM "+text_Table;
		 $("#querySql").val(sql);
//		 select 
		 var txt_sourceDatabaseName =  $('#txt_sourceDatabaseName').combobox('getValue');
		 $("#sourceDatabaseName").textbox("setValue", txt_sourceDatabaseName);
		$('#dlgAddDbLeft').dialog("close");
	};
	this.closeDbLeft = function(){
		$('#dlgAddDbLeft').dialog("close");
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	return ExchangeMapinfoNewEdit;
});