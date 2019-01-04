define(function(require) {
	require('plugins/extend');

	var Page = require('core/page');
	var Core = require('core/core');
	var Config = require('config')
	
	var DataOptInfoRemove = Page.extend(function() {
		
		// TODO 日志删除
		// @override
		this.submit = function(table, data) {
			Core.ajax(Config.ContextPath+'service/dataoptinfo/delete/'+data.dataOptId, {
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
	
	return DataOptInfoRemove;
});
