define(function(require) {
  var Config = require('config');
	var Page = require('core/page');
	var Core = require('core/core');
	
	var ImportOptRemove = Page.extend(function() {
		
		// TODO 日志删除
		// @override
		this.submit = function(table, data) {
			Core.ajax(Config.ContextPath+'service/exportsql/delete/'+data.exportId, {
           	type: 'json',
               method: 'post',
               data: {
                   _method: 'delete'
               }
			}).then(function() {
           		table.datagrid('reload');
           });
		}
	});
	
	return ImportOptRemove;
});
