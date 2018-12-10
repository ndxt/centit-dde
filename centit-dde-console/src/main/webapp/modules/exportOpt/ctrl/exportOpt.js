define(function(require) {
	var Config = require('config');
	// var Core = require('core/core');
	var Page = require('core/page');
	
	var ExportOptAdd = require('../ctrl/exportOpt.add');
	var ExportOptView = require('../ctrl/exportOpt.view');
	var  ExportOptRemove = require('../ctrl/exportOpt.remove');
	var  ExportOptEdit = require('../ctrl/exportOpt.edit');

	var ExportOpt = Page.extend(function() {
		
		this.injecte([
		    new ExportOptAdd('exportOpt_add'),
		    new ExportOptView('exportOpt_view'),
		    new ExportOptRemove('exportOpt_remove'),
		    new ExportOptEdit('exportOpt_edit')
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
	
	return ExportOpt;
});
