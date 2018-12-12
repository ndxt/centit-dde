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
			if (taskId==undefined || taskId==""){
        taskId = -1;
      }
      var panelParent = _self.parent.panel;
      var tableparent = panelParent.find('#dlgList');
      var row = tableparent.datagrid('getRows');
      var i = 0;
      var mapinfoIds = "";
      for(i;i<row.length;i++){
        mapinfoIds += row[i].mapInfoId;
        if(i < row.length-1){
          mapinfoIds += ',';
        }else{
          break;
        }
      }
      if (mapinfoIds==""){
        mapinfoIds =-1;
      }
      Core.ajax(Config.ContextPath+'service/exchangetask/ExchangeMapInfolist/1/'+taskId+'/'+mapinfoIds, {
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
      if (data.taskId==undefined){
        taskId = -1;
      }
      var tableparent = panelParent.find('#dlgList');
      var row = tableparent.datagrid('getRows');
      var i = 0;
      var omapinfoIds = "";
      for(i;i<row.length;i++){
        omapinfoIds += row[i].mapInfoId;
        if(i < row.length-1){
          omapinfoIds += ',';
        }else{
          break;
        }
      }
			var table = panel.find('#dg1');
			var row = table.datagrid('getSelections');
	        var i = 0;  
	        var mapinfoIds = "";  
	        for(i;i<row.length;i++){  
	        	mapinfoIds += row[i].mapInfoId;
	            if(i < row.length-1){  
	            	mapinfoIds += ',';  
	            }else{  
	                break;  
	            }  
	        }
	    if (omapinfoIds!=""){
        if (mapinfoIds!="") {
          mapinfoIds = omapinfoIds + "," + mapinfoIds;
        }else
          mapinfoIds= omapinfoIds;
      }
			// Core.ajax(Config.ContextPath + 'service/exchangetask/importExchangeMapinfo/' + mapinfoIds+'/'+taskId, {
			// 	method: 'put'
			// }).then(function() {
				Core.ajax(Config.ContextPath+'service/exchangetask/edit/'+taskId+'/1/'+ mapinfoIds, {
					method: 'get',
					data: {
	                    _method: 'get'
	                }
				}).then(function(data2) {
					//var tableparent = panelParent.find('#dlgList');
					tableparent.datagrid('loadData',data2.exchangeMapInfoList);
				});
				//closeCallback();
			//});
			//return false;
		};
		this.onClose = function(table) {
			table.datagrid('reload');
        };
	});
	
	return ExchangeTaskExchangeMapinfoAdd;
});
