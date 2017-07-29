define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	
	// 删除数据字典
	var webServiceTaskRemove = Page.extend(function() {
		
		// @override
		this.submit = function(table, data) {
			Core.ajax(Config.ContextPath+'service/exchangetask/delete/'+data.taskId, {
                method: 'post',
                data: {
                    _method: 'delete'
                }
			}).then(function() {
				table.datagrid('reload');
            });
		}
	});
	
	return webServiceTaskRemove;
});