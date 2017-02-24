define(function(require) {
	var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');
    
    
    var ExchangeTaskExchangeMapinfoAddChange = require('./exchangeTask.ExchangeMapinfo.add.change');

	var ExchangeTaskExchangeMapinfoAdd = Page.extend(function() {
		var _self = this;
		
		this.injecte([
			  			new ExchangeTaskExchangeMapinfoAddChange('exchangeTask_ExchangeMapinfo_add_change'),
			  		]);
		
		
		this.load = function(panel, data) {
			data = _self.parent.data;
			var taskId = data.taskId;
			Core.ajax(Config.ContextPath+'service/exchangetask/ExchangeMapInfolist/1/'+taskId, {
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
			var taskId = data.taskId;
			var table = panel.find('#dg1');
			var row = table.datagrid('getSelections');
	        var i = 0;  
	        var mapinfoIds = "";  
	        for(i;i<row.length;i++){  
	        	mapinfoIds += row[i].mapinfoId;  
	            if(i < row.length-1){  
	            	mapinfoIds += ',';  
	            }else{  
	                break;  
	            }  
	        }  
			Core.ajax(Config.ContextPath + 'service/exchangetask/importExchangeMapinfo/' + mapinfoIds+'/'+taskId, {
				method: 'put'
			}).then(function() {
				closeCallback();
			});
			return false;
		};
		this.onClose = function(table) {
			table.datagrid('reload');
        };
	});
	
	return ExchangeTaskExchangeMapinfoAdd;
});