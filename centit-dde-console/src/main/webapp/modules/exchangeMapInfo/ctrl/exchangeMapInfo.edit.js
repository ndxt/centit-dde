define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
	var Page = require('core/page');
	var ExchangeMapInfoDetailAdd = require('../ctrl/exchangeMapInfoDetail.add');
	var ExchangeMapInfoDetailRemove = require('../ctrl/exchangeMapInfoDetail.remove');
	var ExchangeMapInfoDetailAdd2 = require('../ctrl/exchangeMapInfoDetail.add2');
	var ExchangeMapInfoDetailRemove2 = require('../ctrl/exchangeMapInfoDetail.remove2');
	var ExchangeMapInfoTriggerAdd = require("../ctrl/exchangeMapInfoTrigger.add");
	var ExchangeMapInfoTriggerEdit = require("../ctrl/exchangeMapInfoTrigger.edit");
	var ExchangeMapInfoTriggerRemove = require("../ctrl/exchangeMapInfoTrigger.remove");

	var ExchangeMapInfoEdit = Page.extend(function() {
		var _self = this;

		this.injecte([
		  			new ExchangeMapInfoDetailAdd('source_detail_add'),
		  			new ExchangeMapInfoDetailRemove('source_detail_remove'),
		  			new ExchangeMapInfoDetailAdd2('dest_detail_add'),
		  			new ExchangeMapInfoDetailRemove2('dest_detail_remove'),
					new ExchangeMapInfoTriggerAdd('exchangeMapInfoTrigger_add'),
					new ExchangeMapInfoTriggerEdit('exchangeMapInfoTrigger_edit'),
					new ExchangeMapInfoTriggerRemove('exchangeMapInfoTrigger_remove')
		  		]);

		//@override
		this.load = function(panel, data) {
			var form = panel.find('form');
			Core.ajax(Config.ContextPath+'service/exchangemapinfo/edit/'+data.mapInfoId, {
				type: 'json',
				method: 'get'
//					.then()是Promise规范规定的异步调用方法    得到数据后，调用form的load方法将数据显示在表单中
			}).then(function(data) {
				_self.data = data;

				form.form('load', data)
					.form('disableValidation')
					.form('readonly', 'mapInfoId')
					.form('focus');
        $("#txt_querySql").textbox('setValue', data.querySql);
				var sourceTable = panel.find('table.source');
				sourceTable.cdatagrid({
					controller:_self,
					editable: true,
					data:data.mapInfoDetails,
					dragSelection: true,
					onLoadSuccess:function(){
						$(this).datagrid('enableDnd');
					}
				});
				var destTable = panel.find('table.dest');
				destTable.cdatagrid({
					controller:_self,
					editable: true,
					data:data.mapInfoDetails,
					dragSelection: true,
					onLoadSuccess:function(){
						$(this).datagrid('enableDnd');
					}
				});
				var tab2table = panel.find('table.trigger');
				tab2table.cdatagrid({
					controller:_self,
					editable: true,
					data:data.mapInfoTriggers,
                    dragSelection: true,
                    onLoadSuccess:function(){
                        $(this).datagrid('enableDnd');
                    }
				});
			});
		};

		// 提交表单时调用
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			var formData = form.form('value');
      formData.querySql = $('#txt_querySql').textbox('getValue');
			$.extend(data,formData);

			// 开启校验
			form.form('enableValidation');
			var isValid = form.form('validate');

			if (isValid) {
        var mapInfoDetails = panel.find('table.source').datagrid("getData").rows;
        var mapInfoTriggers = panel.find('table.trigger').datagrid("getData").rows;
        data.mapInfoDetails = mapInfoDetails;
        data.mapInfoTriggers = mapInfoTriggers;
				form.form('ajax', {
					url: Config.ContextPath + 'service/exchangemapinfo/save',
					method: 'put',
					data: data,
				}).then(closeCallback);
			}
			return false;
		};

		// 窗口关闭时调用
		this.onClose = function(table, data) {
			table.datagrid('reload');
		};

	});

	return ExchangeMapInfoEdit;
});
