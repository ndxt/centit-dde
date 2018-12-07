define(function(require) {
	var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');
    
    
    var importTaskExchangeMapinfoAddChange = require('./importTask.ExchangeMapinfo.add.change');

	var importTaskExchangeMapinfoAdd = Page.extend(function() {
		var _self = this;
		
		this.injecte([
			  			new importTaskExchangeMapinfoAddChange('importTask_ExchangeMapinfo_add_change'),
			  		]);
		
		
		this.load = function(panel, data) {
			data = _self.parent.data;
			var taskId = data.taskId;
      if (data.taskId==undefined){
        taskId = -1;
      }
			Core.ajax(Config.ContextPath+'service/exchangetask/listExchangeMapInfo/3/'+taskId, {
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
			var table = panel.find('#dg3');
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
				closeCallback();
			});
			return false;
		};
		this.onClose = function(table) {
			table.datagrid('reload');
        };
	});
	
	return importTaskExchangeMapinfoAdd;
});
