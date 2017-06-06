define(function(require) {
	// var Config = require('config');
	// var Core = require('core/core');
	var Page = require('core/page');
	
	var dataOptInfoAdd = require('../ctrl/dataOptInfo.add');
	var  dataOptInfoRemove = require('../ctrl/dataOptInfo.remove');
	var  dataOptInfoEdit = require('../ctrl/dataOptInfo.edit');

	var dataOptInfo = Page.extend(function() {
		
		this.injecte([
		    new dataOptInfoAdd('dataOptInfo_add'),
		    new dataOptInfoRemove('dataOptInfo_remove'),
		    new dataOptInfoEdit('dataOptInfo_edit')
		]);
		
		// @override
		this.load = function(panel) {
			panel.find('table').cdatagrid({
				controller: this
			});
		};
	});
	
	return dataOptInfo;
});