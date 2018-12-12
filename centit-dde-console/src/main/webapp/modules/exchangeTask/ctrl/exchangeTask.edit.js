define(function(require) {
	var Config = require('config');
	var Core = require('core/core');
  var Page = require('core/page');
	
	var ExchangeTaskEditConAdd = require('./exchangeTask.edit.conAdd');
	var ExchangeTaskAdd = require('./exchangeTask.add');
	var ExchangeTaskExchangeMapinfoAdd = require('../ctrl/exchangeTask.ExchangeMapinfo.add');
	var ExchangeTaskexportSqlEdit = require('../ctrl/exchangeTask.exportSql.edit');
	var ExchangeTaskExchangeTaskdetailIdRemove = require('../ctrl/exchangeTask.ExchangeTaskdetailId.remove');
	var ExchangeTaskRun = require('../ctrl/exchangeTask.run');

	var ExchangeTaskEdit = ExchangeTaskAdd.extend(function() {
		var _self = this;
		
		this.injecte([
		            new ExchangeTaskEditConAdd('exchangeTask_ExchangeMapinfo_edit_conAdd'),
		  			new ExchangeTaskExchangeMapinfoAdd('exchangeTask_ExchangeMapinfo_add'),
		  			new ExchangeTaskexportSqlEdit('exchangeTask_exportSql_edit'),
		  			new ExchangeTaskExchangeTaskdetailIdRemove('exchangeTask_ExchangeTaskdetailId_remove'),
		  			new ExchangeTaskRun('exchangeTask_transfer_run'),
		  		]);
		
		
		// @override
		this.load = function(panel, data) {
			var form = panel.find('form');
			
			Core.ajax(Config.ContextPath+'service/exchangetask/edit/'+data.taskId+'/1/'+-1, {
			  method: 'get',
				data: {
                    _method: 'get'
                }
			}).then(function(data) {
				_self.data = data;
				
				form.form('load', data)
					.form('disableValidation')
					.form('readonly', 'taskId')
					.form('focus');
				
				var tab1table = panel.find('table.tab1');
				tab1table.cdatagrid({
					controller:_self,
					editable: true,
					data:data.exchangeMapInfoList,
				});
				//tab1table.datagrid('reload');
			});
		};
		
		// @override
		this.submit = function(panel, data, closeCallback) {
			var form = panel.find('form');
			var formData = form.form('value');
			$.extend(data,formData);
			// 开启校验
//			enableValidation开启表单校验，validate校验表单数据
			form.form('enableValidation');
			var isValid = form.form('validate');
			
			if (isValid) {
        var exchangeTaskDetails = panel.find('table.tab1').datagrid("getData").rows;
        data.exchangeTaskDetails = exchangeTaskDetails;
				Core.ajax(Config.ContextPath + 'service/exchangetask/editAndsave/',{
					data: data,
					method:'put'
				}).then(closeCallback);
				
				
			}
//			return false让窗口等到响应后再关闭
			return false;
		};
		
		// @override
		this.onClose = function(table) {
			table.datagrid('reload');
		};
	});
//	/centit-dde3/src/main/webapp/modules/exportTask/exportTask-edit-conAdd.html
	this.addCon = function(){
		 $('#dlgaddCon').dialog({
	    	title:'定时器添加',
	    	resizable: true,
	        modal: true,
        });
        $('#dlgaddCon').dialog("open");
        var change = $("#taskCron").textbox('getValue');
        $("#txt_cron").textbox('setValue',change);
	};

	
	this.getCron = function(){
		
		var MinTotal = "0"+" ";
		var txtMinutes = $('#txtMinutes').val();//每分钟
		if(txtMinutes != ""){
			MinTotal = "0"+" "+"*/"+txtMinutes;
		}else{
			MinTotal = "0"+" "+"*";
		}
		
		var HourTotal = null;
		var txtHourly = $('#txtHourly').val();//小时
		var txtHour_begin = $('#txtHour_begin').val();//起始小时
		var txtHour_end = $('#txtHour_end').val();//结束小时
		
		if(txtHour_begin == "" && txtHourly!=""){
			HourTotal = "*/"+txtHourly;
		}
		if(txtHour_begin != "" && txtHourly!=""){
			HourTotal= txtHour_begin+"-"+txtHour_end+"/"+txtHourly;
		}
		if(txtHourly=="" && txtHour_begin!=""){
			HourTotal = txtHour_begin+"-"+txtHour_end;
		}
		if(txtHourly=="" && txtHour_begin=="" && txtHour_end==""){
			HourTotal="*";
		}
		
		var dayTotal ="?";
		var dayVal=null;
		var txtDaily_str0 =  $('#txtDaily_str0').val();
		var txtDaily =  $('#txtDaily').val();
		var txtDaily_str =  $('#txtDaily_str').val();
		var txtDaily_end =  $('#txtDaily_end').val();
		var txtDaily_last =  $('#txtDaily_last').val();
		
		$("input[name=rad_day]:checked").each(function(){     
			dayVal=$(this).val(); 
			if(dayVal=="-1"){
				dayTotal = "?";
			}
			if(dayVal=="0"){
				dayTotal = txtDaily_str0+"/"+txtDaily;
			}
			if(dayVal=="1"){
				dayTotal = txtDaily_str+"-"+txtDaily_end;
			}
			if(dayVal=="2"){
				dayTotal = "L";
			}
			if(dayVal=="3"){
				dayTotal = "LW";
			}
			if(dayVal=="4"){
				dayTotal = txtDaily_last+"L"
			}
	    });
		
		var monthVal = null;
		var monthTotal ="*";
		var choiceMonth_str0 =  $('#choiceMonth_str0').val();
		var choiceMonth =  $('#choiceMonth').val();
		var choiceMonth_str =  $('#choiceMonth_str').val();
		var choiceMonth_end =  $('#choiceMonth_end').val();
		
		
		$("input[name=rad_month]:checked").each(function(){     
			monthVal=$(this).val();  
			if(monthVal=="-1"){
				monthTotal ="*";
			}
			if(monthVal=="0"){
				monthTotal = choiceMonth_str0+"/"+choiceMonth;
			}
			if(monthVal=="1"){
				monthTotal = choiceMonth_str+"-"+choiceMonth_end;
			}
	    });
		
		var weeks=$('#weeks').combobox('getValues').join(",");
		var txtWeek_str =  $('#txtWeek_str').val();
		var txtWeek_end =  $('#txtWeek_end').val();
		var txtWeek_how =  $('#txtWeek_how').val();
		var txtWeek_day =  $('#txtWeek_day').val();
		var weekTotal = "*";
		var weekVal =null;
		$("input[name=rad_week]:checked").each(function(){     
			weekVal=$(this).val();  
			if(weekVal=="-1"){
				weekTotal = "*";
			}
			if(weekVal=="0"){
				weekTotal = weeks;
			}
			if(weekVal=="1"){
				weekTotal = txtWeek_str+"-"+txtWeek_end;
			}
			if(weekVal=="2"){
				weekTotal = "6";
			}
			if(weekVal=="3"){
				weekTotal = "6L";
			}
			if(weekVal=="4"){
				weekTotal = txtWeek_how+"/"+txtWeek_day;
			}
	    });
		
		var total = null;
		total = MinTotal+" "+HourTotal+" "+dayTotal+" "+monthTotal+" "+weekTotal;
		$("#txt_cron").textbox('setValue',total);
	};
	
	
	this.saveCron = function(){
		var cron = $("#txt_cron").textbox('getValue');
		$("#taskCron").textbox('setValue',cron);
        $('#dlgaddCon').dialog("close");
	};
	
	
	
	
	return ExchangeTaskEdit;
});
