$(function() {
	if (!Highcharts) {
		alert('没有引用Highcharts库，请联系系统管理员');//请到http://www.highcharts.com/官网下载
		return false;
	}
	
	$.myChart = {
		_contextPath: null,
			
		_renderTo: 'containerH',
		
		_renderToDialog: 'chartDialogContainer',
		
		chartFix: null,
		
		chartDialog: null,
		
		_title: null,
		
		_config: { event: true,
			table:'.list:last', thead: 'thead>tr', tbody: 'tbody>tr', th: 'th', span: 1
		},
		
		_dialgoOption: {
			width:1000, height:450, max:false, mask:true, maxable:false, minable:false, resizable:false, drawable:true, fresh:true
		},

		_option : {
			chart: {
				type: 'column',
				marginBottom: 60
			},
			
			title: {
				text: null
			},
			
			xAxis : {
				categories : []
			},
			
			yAxis : {
				title : {
					text : null
				},
				stackLabels : {
					enabled : true,
					style : {
						fontWeight : 'bold',
						color : (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
					}
				}
			},
			
			tooltip : {
				formatter : function() {
					return '<b>' + this.x + '</b><br/>' + this.series.name + ': ' + this.y ;
				}
			},
			
			legend: {
				layout: 'vertical',
	            backgroundColor: '#FFFFFF',
	            align: 'right',
	            verticalAlign: 'top',
	            floating: true,
	            x: -10,
	            lineHeight:20,
	            itemStyle: {
	            	'line-height':'20px'
	            }
			},
			
			series : []
		},
		
		init: function(json, op) {
			var data = this._parseData(json);
			
			this._extendOption(data, op);
			
			if (op && op.config) {
				
				$.extend (this._config, op.config);
			}
			
			this._parseTable();
			
			//this.chartFix = this._drawChart();
		},
		
		split: function(json, op, count) {
			var nc = this._splitCategories(json, count);
			var ns = this._splitSeries(json, count);
			
			json.objectsJson.categories = nc[0];
			json.objectsJson.series = ns[0];
			
			var chart = this.init(json, op);
			
			chart.split = {
					index:0,
					count:nc.length
			};
			
			var div = $('#kgqkWin');
			div.append('<div id="prev" class="chartsBtn prev transparent-50">上一页</div><div id="next" class="chartsBtn next transparent-50">下一页</div>');
			
			$('#prev').click(function() {
				var flag = $.myChart._setData(chart, nc, ns, -1);
				if (flag.first) {
					$(this).hide();
				}else {
					$(this).show();
				}
				
				if (flag.last) {
					$('#next').hide();
				}
				else {
					$('#next').show();
				}
			}).hover(function() {
				$(this).animate({'opacity':1}, 500);
			}, function() {
				$(this).css({'opacity':0.5});
			});
			
			$('#next').click(function() {
				var flag = $.myChart._setData(chart, nc, ns, 1);
				if (flag.last) {
					$(this).hide();
				}
				else {
					$(this).show();
				}
				
				if (flag.first) {
					$('#prev').hide();
				}else {
					$('#prev').show();
				}
			}).hover(function() {
				$(this).animate({'opacity':1}, 500);
			}, function() {
				$(this).css({'opacity':0.5});
			});
			
			if (chart.split.index == 0) {
				$('#prev').hide();
			}
			
			if (chart.split.index == chart.split.count - 1) {
				$('#next').hide();
			}
		},
		
		_setData: function(chart, nc, ns, d){
			var index = chart.split.index;
			var count = chart.split.count;
			
			index += d;
			if(index >= count) {
				index = 0;
			}
			else if (index < 0) {
				index = count-1;
			}
			
			chart.xAxis[0].setCategories(nc[index], false);
			for(var i=0; i<chart.series.length; i++) {
				chart.series[i].setData(ns[index][i].data);
			}
			
			chart.split.index = index;
			chart.redraw();
			
			var result = {first: false, last: false};
			if (index == 0) result.first = true;
			if (index == count - 1) result.last = true;
			
			return result;
		},
		
		_split: function(data, count){
			var nc = [];
			var ta = [];
			
			for(var i=0; i<data.length; i++) {
				ta.push(data[i]);
				
				if ((i+1)%count==0) {
					nc.push(ta);
					ta = [];
				}
			}
			if (ta.length) {
				nc.push(ta);
			}
			
			return nc;
		},
		
		_splitCategories: function(json, count) {
			var categories = json.objectsJson.categories;
			
			return this._split(categories, count);
		},
		
		_splitSeries: function(json, count) {
			var series = json.objectsJson.series;
			var nd = [];
			for(var i=0; i<series.length; i++) {
				var data = series[i].data;
				var ta = this._split(data, count);
				
				nd.push(ta);
			}
			
			var ns = [];
			var length = nd[0].length;
			for (var i=0; i<length; i++) {
				var obj = $.extend(true, [], series);
				for (var j=0; j<obj.length; j++) {
					obj[j].data = nd[j][i];
				}
				ns.push(obj);
			}
			return ns;
		},
		
		
		_parseData: function(data) {
			if (!data) return null;
			
			this._title = data.title;
			
			var categories = data.objectsJson.categories;
			
			var series = data.objectsJson.series;
			
			return {
				'categories':categories,
				'series':series
			};
		},
		
		_extendOption: function(data, op) {
			if (data) {
				this._option.title.text = this._title;
				this._option.chart.renderTo = this._renderTo;
				
				this._option.yAxis.title.text = this._title;
				
				this._option.xAxis.categories = data.categories;
				
				this._option.series = data.series;
			}
			
			if (op) {
				$.extend(true, this._option, op);
			}
		},
		
		_extendOptionDialog: function(data) {
			if (data) {
				this._option.title.text = this._title;
				this._option.chart.renderTo = this._renderToDialog;
				
				this._option.yAxis.title.text = null;
				
				this._option.xAxis.categories = data.categories;
				
				this._option.series = data.series;
			}
		},
		
		_drawChart: function() {
//			if ($.toJSON) {
//				alert($.toJSON(this._option));
//			}
			
			return new Highcharts.Chart(this._option);
		},
		
		_drawChartDialog: function(obj) {
//			if ($.toJSON) {
//				alert($.toJSON(this._option));
//			}
			var url = this._contextPath+'/page/zfbzjc/chartDialog.jsp';
			$.pdialog.open(url, 'chartDialog', this._title, this._dialgoOption);
		},
		
		_parseTable: function() {
			var config = this._config;
			var table = this._getTable(config.table);
			
			// 判断表头第一行第一个TH是否包含rowspan属性
			var thead = table.find(config.thead);
			var tbody = table.find(config.tbody+'[chart!=false]');
			
			var parseTableData = $.parseTableHead(table);
			for (var index=0; index<parseTableData.length; index++) {
				
				var data = parseTableData[index];
				var $this = data.obj;
				
				// 跳过不需要统计的列
				if (!$this.hasClass('stat')) continue;
				
				var datas = [];
				var childs = [];
				
//				tbody.children('td:nth-child('+ (index+rowspan+1) +')').css({
//					'background-color':'yellow',
//					'color':'red'
//				}).each(function(){
//					var text = $.trim($(this).text());
//					
//					if ($.isNumeric(text)) {
//						
//						datas.push(parseFloat(text));
//					}
//					else {
//						datas.push(text);
//					}
//				});
				
				tbody.children('td:nth-child('+ (index+1) +')').each(function(){
					var text = $.trim($(this).text());
					
					if ($.isNumeric(text)) {
						
						datas.push(parseFloat(text));
					}
					else {
						datas.push(text);
					}
					
					childs.push($(this));
				});
				
				table.data(data.id, $this);
				
				$this.data('series', {
					name: ($this.attr('title')) ? $.trim($this.attr('title')): $.trim($this.text()),
					data: datas
				}).data({'childs': childs});
			}
			
			var categories = this._getCategories(tbody);
			
			// 设置关联
			thead.find(config.th + '.chart').each(function() {
				var $this = $(this);
				var id = $this.data('childs');
				var series = [];
				var childs = [];
				
				for (var i=0; i<id.length; i++) {
					var th = table.data(id[i]);
					if (!th) continue;
					series.push(th.data('series'));	
					childs = $.merge(childs, th.data('childs'));
				}
				
				$this.css({cursor:'pointer'}).click(function() {
					var data = $.myChart._parseData($(this).data('data'));
					$.myChart._extendOptionDialog(data, {});
					$.myChart._drawChartDialog($(this));
					
				}).hover(function(){
					$.each(childs, function() {
						this.addClass('hover');
					});
					
				}, function() {
					$.each(childs, function() {
						this.removeClass('hover');
					});
					
				}).data('data', {
					title: $.trim($this.text()),
					objectsJson: {
						series: series,
						categories: categories
					}
				}).append('<img src="'+this._contextPath+'../../../themes/css/images/slideshow/chart.png" style="width:16px; height:16px; margin-bottom:-2px; margin-left:2px;" title="统计"></img>');
				
			});
		},
		
		/**
		 * 获取表格接口
		 * 
		 * @param table 表格的CSS选择器
		 * @returns
		 */
		_getTable: function(table) {
			if (navTab) {
				return $(table, navTab.getCurrentPanel());
			}
			else {
				return $(table);
			}
		},
		
		_getCategories: function(tbody) {
			var categories = [];
			
			tbody.each(function() {
				var td = $(this).children('td:first');
				categories.push($.trim(td.text()));
			});
			
			return categories;
		}
	};
});