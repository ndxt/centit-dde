define(function(require) {
	var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');
    
    
    var ExchangeTask2ExchangeMapinfoAddChange = require('./exchangeTask2.ExchangeMapinfo.add.change');

	var ExchangeTask2ExchangeMapinfoAdd = Page.extend(function() {
		var _self = this;
		
		this.injecte([
			  			new ExchangeTask2ExchangeMapinfoAddChange('exchangeTask2_ExchangeMapinfo_add_change'),
			  		]);
		
		
		this.load = function(panel, data) {
			data = _self.parent.data;
			var taskId = data.taskId;
			Core.ajax(Config.ContextPath+'service/exchangetask/listExchangeMapInfo/2/'+taskId, {
				method: 'get'
			}).then(function(data2) {
				panel.find('table').cdatagrid({
					controller: this,
					data:data2
				});
			});
		};
		
		
		
		// @override
		this.submit = function(panel, data,closeCallback) {
			data = _self.parent.data;
			var panelParent = _self.parent.panel;
			var taskId = data.taskId;
			var table = panel.find('#dg2');
			var row = table.datagrid('getSelections');
	        var i = 0;  
	        var exportIds = "";  
	        for(i;i<row.length;i++){  
	        	exportIds += row[i].exportId;  
	            if(i < row.length-1){  
	            	exportIds += ',';  
	            }else{  
	                break;  
	            }  
	        }  
			Core.ajax(Config.ContextPath + 'service/exchangetask/importExchangeMapinfo/' + exportIds+'/'+taskId, {
				method: 'put'
			}).then(function() {
				Core.ajax(Config.ContextPath+'service/exchangetask/edit/'+taskId, {
					method: 'get',
					data: {
	                    _method: 'get'
	                }
				}).then(function(data2) {
					var tableparent = panelParent.find('#dlgList2');
					tableparent.datagrid('loadData',data2.exportSqlList);
				});
				closeCallback();
			});
			return false;
		};
		this.onClose = function(table) {
			table.datagrid('reload');
        };
	});
	
	return ExchangeTask2ExchangeMapinfoAdd;
});