(function($){
	/**
	 * 解析表格头THEAD
	 */
	$.parseTableHead = function(table) {
		table = $(table);
		
		var thead = table.children('thead');
		var trs = thead.children('tr');
		var length = trs.length;
		
		// 用来存放返回数据的数组
		var datas = [];
		trs.each(function() {
			datas.push([]);
		});
		
		trs.each(function(line, tr) {
			
			// 列计数器
			var column = 0;
			$(tr).children('th').each(function(index) {
				
				var th = $(this);
				
				var rowspan = (th.attr('rowspan')) ? parseInt(th.attr('rowspan')) : 1;
				var colspan = (th.attr('colspan')) ? parseInt(th.attr('colspan')) : 1;
				
				column = _findFirstTH(datas[line]);
				
				
				
				for (var i=line; i<length; i++) {
					for (var j=0; j<colspan && i-line<rowspan; j++) {
						
						var data = {
								id: 'th' + line + '_' + column,
								obj: th,
								length: colspan,
								height: i-line
						};
						
						th.attr('id', data.id);
						
//						console.log(i+', '+(column+j)+"数据："+$.toJSON(data));
						datas[i][column+j]=data;
						
					}
				}
				
//				console.log('\n');
				
			});
		});
		
		// 表头单行
		for(var i=0; i<length; i++) {
			for (var j=0; j<datas[i].length; j++) {
				
				
				var data = datas[i][j];
				
				//对于含有rowspan属性的th只统计第一行的子列
				if (0 != data.height) continue;
				
				var obj = data.obj;
				var childs = obj.data('childs');
				
//				console.log(i+','+j+': '+data.id+' childs:'+$.toJSON(childs));
				
				if (!childs) {
					obj.data('childs', [datas[length-1][j].id]);
				}
				else {
					childs.push(datas[length-1][j].id);
					obj.data('childs', childs);
				}
				
			}
		}
		
//		trs.find('th').css({
//			cursor:'pointer'
//		}).click(function() {
//			alert($.toJSON($(this).data('childs')));
//		});
		
//		console.log($.toJSON(datas[length-1]));
		
		// 只返回最后一行
		return datas[length-1];
	};
	
	function _findFirstTH(tr) {
//		console.log($.toJSON(tr));
		
		for (var i=0; i<tr.length; i++) {
//			console.log(i+': '+tr[i]+ (tr[i]));
			if (!tr[i]) return i; 
		}
		return i;
	}
})(jQuery);