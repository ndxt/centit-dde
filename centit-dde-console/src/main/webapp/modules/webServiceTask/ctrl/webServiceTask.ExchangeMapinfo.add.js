define(function(require) {
	var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');
    
    
    var webServiceTaskExchangeMapinfoAddChange = require('./webServiceTask.ExchangeMapinfo.add.change');

	var webServiceTaskExchangeMapinfoAdd = Page.extend(function() {
		var _self = this;
		
		this.injecte([
			  			new webServiceTaskExchangeMapinfoAddChange('webServiceTask_ExchangeMapinfo_add_change'),
			  		]);
		
		
		this.load = function(panel, data) {
			data = _self.parent.data;
      var panelParent = _self.parent.panel;
			var taskId = data.taskId;
      if (data.taskId==undefined){
        taskId = -1;
      }
      var tableparent = panelParent.find('#dlgList4');
      var row = tableparent.datagrid('getRows');
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
      if (exportIds==""){
        exportIds =-1;
      }
			Core.ajax(Config.ContextPath+'service/exchangetask/listExchangeMapInfo/4/'+taskId+'/'+exportIds, {
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
      var tableparent = panelParent.find('#dlgList4');
      var row = tableparent.datagrid('getRows');
      var i = 0;
      var oexportIds = "";
      for(i;i<row.length;i++){
        oexportIds += row[i].exportId;
        if(i < row.length-1){
          oexportIds += ',';
        }else{
          break;
        }
      }
			var table = panel.find('#dg4');
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
      if (oexportIds!=""){
        if (exportIds!="") {
          exportIds = oexportIds + "," + exportIds;
        }else
          exportIds= oexportIds;
      }
			/*Core.ajax(Config.ContextPath + 'service/exchangetask/importExchangeMapinfo/' + exportIds+'/'+taskId, {
				method: 'put'
			}).then(function() {*/
				Core.ajax(Config.ContextPath+'service/exchangetask/edit/'+taskId+'/4/'+exportIds, {
					method: 'get',
					data: {
	                    _method: 'get'
	                }
				}).then(function(data2) {
					//var tableparent = panelParent.find('#dlgList4');
					tableparent.datagrid('loadData',data2.exportSqlList);
				});
				/*closeCallback();
			});
			return false;*/
		};
		this.onClose = function(table) {
			table.datagrid('reload');
        };
	});
	
	return webServiceTaskExchangeMapinfoAdd;
});
