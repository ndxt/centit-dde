define(function(require) {
	var Config = require('config');
	// var Core = require('core/core');
	var Page = require('core/page');
	
	var ImportOptAdd = require('../ctrl/importOpt.add');
	var ImportOptView = require('../ctrl/importOpt.view');
	var  ImportOptRemove = require('../ctrl/importOpt.remove');
	var  ImportOptEdit = require('../ctrl/importOpt.edit');

	var ImportOpt = Page.extend(function() {
		
		this.injecte([
		    new ImportOptAdd('importOpt_add'),
		    new ImportOptView('importOpt_view'),
		    new ImportOptRemove('importOpt_remove'),
		    new ImportOptEdit('importOpt_edit')
		]);
		
		// @override
		this.load = function(panel) {
			panel.find("#os").combobox({
				url:Config.ContextPath + 'service/platform/listOs',
			});
			panel.find('table').cdatagrid({
				controller: this
			});
		};
	});
	
	return ImportOpt;
});