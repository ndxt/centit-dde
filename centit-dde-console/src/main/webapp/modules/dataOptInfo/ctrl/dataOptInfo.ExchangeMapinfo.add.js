define(function(require) {
	var Config = require('config');
    var Core = require('core/core');
    var Page = require('core/page');


	var DataOptExchangeMapinfoAdd = Page.extend(function() {
		var _self = this;
		
		this.load = function(panel, data) {
			data = _self.parent.data;
      var panelParent = _self.parent.panel;
			var taskId = data.taskId;
      if (data.taskId==undefined){
        taskId = -1;
      }
      var tableparent = panelParent.find('#dlgList5');
      var row = tableparent.datagrid('getRows');
      var i = 0;
      var importIds = "";
      for(i;i<row.length;i++){
        importIds += row[i].importId;
        if(i < row.length-1){
          importIds += ',';
        }else{
          break;
        }
      }
      if (importIds==""){
        importIds =-1;
      }
			Core.ajax(Config.ContextPath+'service/exchangetask/listExchangeMapInfo/3/'+taskId+'/'+importIds, {
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
      var tableparent = panelParent.find('#dlgList5');
      var row = tableparent.datagrid('getRows');
      var i = 0;
      var oimportIds = "";
      for(i;i<row.length;i++){
        oimportIds += row[i].importId;
        if(i < row.length-1){
          oimportIds += ',';
        }else{
          break;
        }
      }
			var table = panel.find('#dg5');
			var row = table.datagrid('getSelections');
	        var i = 0;  
	        var importIds = "";
	        for(i;i<row.length;i++){
            importIds += row[i].importId;
	            if(i < row.length-1){
                importIds += ',';
	            }else{  
	                break;  
	            }  
	        }
      if (oimportIds!=""){
        if (importIds!="") {
          importIds = oimportIds + "," + importIds;
        }else
          importIds= oimportIds;
      }
			/*Core.ajax(Config.ContextPath + 'service/exchangetask/importExchangeMapinfo/' + exportIds+'/'+taskId, {
				method: 'put'
			}).then(function() {*/
				Core.ajax(Config.ContextPath+'service/exchangetask/edit/'+taskId+'/3/'+importIds, {
					method: 'get',
					data: {
	                    _method: 'get'
	                }
				}).then(function(data2) {
					//var tableparent = panelParent.find('#dlgList2');
					tableparent.datagrid('loadData',data2.importOptList);
				});
				/*closeCallback();
			});
			return false;*/
		};
		this.onClose = function(table) {
			table.datagrid('reload');
        };
	});

	return DataOptExchangeMapinfoAdd;
});
