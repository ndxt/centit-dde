define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	var ExportOptExportFieldSync = require('../ctrl/exportOpt.exportField.sync');
	var ExportOptExportFieldAdd = require('../ctrl/exportOpt.exportField.add');
	var ExportOptExportFieldRemove = require('../ctrl/exportOpt.exportField.remove');
	var ExportOptExportTriggerAdd = require('../ctrl/exportOpt.exportTrigger.add');
	var ExportOptExportTriggerRemove = require('../ctrl/exportOpt.exportTrigger.remove');


	var ExportOptAdd = Page.extend(function() {
		var _self = this;

		this.injecte([
			new ExportOptExportFieldSync('exportOpt_exportField_sync'),
			new ExportOptExportFieldAdd('exportOpt_exportField_add'),
			new ExportOptExportFieldRemove('exportOpt_exportField_remove'),
			new ExportOptExportTriggerAdd('exportOpt_exportTrigger_add'),
			new ExportOptExportTriggerRemove('exportOpt_exportTrigger_remove')
		]);
		
		// @override
		this.load = function(panel) {
			var form = panel.find('form');
			form.form('disableValidation')
				.form('focus');
			var tab1table = panel.find('table.tab1');
			tab1table.cdatagrid({
				controller:_self,
				editable: true,
			});
			var tab2table = panel.find('table.tab2');
			tab2table.cdatagrid({
				controller:_self,
				editable: true
			});
      var databaseCode = "";
      $("#db").combobox({
        valueField: 'databaseCode',
        textField: 'databaseName',
        url:'service/platform/listDb',
        onSelect: function(rec) {
          databaseCode = rec.databaseCode;
          var url = Config.ContextPath + 'service/platform/listTable/' + databaseCode;
          $('#table').combobox('reload', url);
        }
      });
      $("#table").combobox({
        valueField: 'tableName',
        textField: 'tableName',
        onSelect: function(rec) {
          // var url = Config.ContextPath + 'service/common/listField/' + databaseCode +"/" + rec.tableName;
          // $('#field').combobox('reload', url);
          Core.ajax(Config.ContextPath + 'service/importopt/getFields/'+ databaseCode +"/" + rec.tableName,{
            method:'get',
          }).then(function(data){

            var tab1table = panel.find('table.tab1');
            tab1table.datagrid('loadData',data);
            /*tab1table.cdatagrid({
              controller:_self,
              editable: true,
              data:data
            });*/
          });
        }
      });
      $("#querySql").blur(function(){
        Core.ajax(Config.ContextPath + 'service/importopt/getFields/',{
          method:'get',
        }).then(function(data){

          var tab1table = panel.find('table.tab1');
          tab1table.datagrid('loadData',data);
         /* tab1table.cdatagrid({
            controller:_self,
            editable: true,
            data:data
          });*/
        });
      })
		};
		
		// @override
		this.submit = function(panel, data,closeCallback) {
			var form = panel.find('form');
			form.form('enableValidation');
			var isValid = form.form('validate');
			if (isValid) {
				var exportFields = panel.find('table.tab1').datagrid("getData").rows;
				var exportTriggers = panel.find('table.tab2').datagrid("getData").rows;
        data.exportFields = exportFields;
        data.exportTriggers = exportTriggers;
        form.form('ajax', {
          url: Config.ContextPath + 'service/exportsql/save',
          method: 'put',
          data: data
//					提交成功后调用closeCallback关闭对话框
        }).then(closeCallback);
			}
			return false;
		};
		
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
        };
	});

   this.sync = function() {
     var form = $("form");
     form.form('enableValidation');
     var isValid = form.form('validate');
     if (isValid) {
      form.form('ajax', {
        url: Config.ContextPath + 'service/exportsql/resolveQuerySql',
        method: 'post',
        data: {
          querySql: $("#querySql").val(),
          sourceDatabaseName: $("#db").combobox('getValue')
        }
      }).then(function (data) {
        var tab1table = $('table.tab1');
        tab1table.datagrid('loadData',data);
        /*tab1table.cdatagrid({
          controller: this,
          editable: true,
          data: data
        });*/
      })
     }
   }

	return ExportOptAdd;
});
