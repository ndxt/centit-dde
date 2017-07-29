function ExportSql() {
	var $defDatasource = $('#div_data_source_page_content_dataoptinfo');
	var $exportSqlForm = $('#div_export_field_dataoptinfo');
	var $exportFieldForm = $('#div_dataoptinfo_field_form');

	// 添加数据源按钮
	var $btnDefDatasource = $('#btn_def_datasource_dataoptinfo');

	// 添加字段按钮
	var $btnExportField = $('#btn_dataoptinfo_field');

	// 保存query sql 隐藏域
	var $querysql = $('#hid_querysql_dataoptinfo');

	// ExportField 表格
	var $tbodyField = $('#tbody_dataoptinfo_field');

	// Field 隐藏Form
	var $fieldFormList = $('#hid_field_form_list_dataoptinfo');

	var defDsKey = 'DEF_DS_KEY';
	var columnNo = 'columnNo';
	
	
	
	var $txtFieldName = $('#txt_field_name_dataoptinfo');
	var $txaFieldSentence = $('#txa_field_sentence_dataoptinfo');

	this.init = function() {
		for ( var e in binds) {
			binds[e]();
		}
	};
   
	this.bind = function(method){
    	binds[method]();
    };
	
	var binds = {
		bindDefDatasource : function() {
			// $defDatasource.unbind('click');
			$btnDefDatasource.click(function() {
				
				funs.exportSqlComplete();

				funs.updateExportSql(funs.close);
			});
		},

		bindExportField : function() {
			$btnExportField.click(function() {
				
				var index = $exportFieldForm.find('#hid_dataoptinfo_field_form_columnno').val();
				if(funs.isNull(index)) {
					index = funs.getFormFieldMaxIndex();
				}
				
				var objArray = $exportFieldForm.find(':input, select').serializeArray();
				
				//获取表单数据
				var object = {};
				
				for(var i = 0; i < objArray.length; i++) {
					object[objArray[i].name] = objArray[i].value;
				}
				object[columnNo] = index;
				
				object['importName'] = $('#sel_dataoptinfoId option:selected').text();
				object['osName'] = $('#sel_dataoptinfo_osId option:selected').text();
				
				//更新表格
				funs.update(object, index);
				
				//更新隐藏域中form值
				
				funs.setField(object, index);
				
				funs.close();
				
				//更新ExportSql 中 querysql字段值
				//var querysql = $querysql.val();

				//funs.updateExportSqlByField(querysql, funs.close);
				
				
			});
		},
		
		bindFieldName : function() {
			$txtFieldName.bind('keyup', function() {
				var $this = $(this);
				var sentence = $.trim($txaFieldSentence.val());

				if(funs.isNull(sentence)) {
					$txaFieldSentence.val($this.val());
				}

				var fs = sentence.split(/\s+/);
				
				if(1 == fs.length) {
					$txaFieldSentence.val(fs[0] + ' ' + $this.val());
				} else {
					fs[fs.length -1] = $this.val();
					
					$txaFieldSentence.val(fs.join(' '));
				}
			});
		},
		bindFieldSentence : function() {
			$txaFieldSentence.bind('keyup', function(){
				var $this = $(this);
				
				var sentence = $.trim($this.val());
				if(funs.isNull(sentence)) {
					$txtFieldName.val($this.val());
					
					return;
				}

				var fs = sentence.split(/\s+/);
				if(1 == fs.length) {
					$txtFieldName.val(fs[0]);
				} else {
					$txtFieldName.val(fs[fs.length - 1]);
				}
				
				
				
			});
		},
		
		/**
		 * 绑定删除功能
		 */
		bindDelete : function() {
			$tbodyField.find('a.delete').die('click').live('click', function() {
				var $tr = $(this).parent().parent();
				
				var index = funs.get($tr, columnNo);
				//删除文本
				$tr.remove();
				
				funs.deleteField(index);
				
				
				//D(funs.getFieldFormList());
			});
		},
		
		bindOptType : function() {
			$('#div_optType :radio').change(function() {
				var optType = $(this).val();
				
				var $importId = $('#div_dataoptinfo_importId');
				var $osId = $('#div_dataoptinfo_osId');
				
				if('1' == optType) {
					$importId.show();
					$osId.hide();
				} else if('2' == optType) {
					$importId.hide();
					$osId.show();
				}
				
			});
		},
		
		initSortDrag : function() {
			var options = {
				cursor : 'move', // selector 的鼠标手势
				sortBoxs : 'tbody.sortDrag', // 拖动排序项父容器
				replace : true, // 2个sortBox之间拖动替换
				items : '> tr', // 拖动排序项选择器
				selector : 'td:first:not(.notMove)', // 拖动排序项用于拖动的子元素的选择器，为空时等于item
				zIndex : 1000,
				callback : funs.sortDragCallback
			};
			$tbodyField.sortDrag(options);
		}
	};

	var funs = {
		exportSqlComplete : function() {
			// 保存数据源时将数据存在页面的hidden中
			var $objList = $defDatasource.find('textarea,select');

			$.each($objList, function(index, object) {
				var $object = $(object);

				var name = $object.attr('name');
				$exportSqlForm.find(':hidden[name="' + name + '"]').val($object.val());
			});
			
			//datasource
			$('#txt_dataoptinfo_sdn').val($('#hid_dataoptinfo_sdn').val());
		},

		/**
		 * 表格拖动列排序回调方法
		 * @param $srcBox
		 * @param $destBox
		 */
		sortDragCallback : function($srcBox, $destBox) {
			//D($srcBox);
			//D($destBox);

			// funs.alert('234');
			
			funs.updateFieldIndex();
		},

		/**
		 * 将Field表单中的值保存或更新进表格
		 * 
		 * @param $objList
		 */
		save : function(objList) {
			// 获取原先表格中的字段
			

			// 根据query sql解析的字段进行更新
			for ( var i = 0; i < objList.length; i++) {
				var object = objList[i];

				var $tr = $(DWZ.frag['TrDataOptInfoFieldTemplate']);
				
				funs.setTableText($tr, object);
				funs.set($tr, columnNo, object.columnNo);
				
				$tr.show();
				if(parseInt(object.columnNo)%2==1){
					$tr.addClass('trbg');
				}				
				$tr.hover(function(){
					$(this).addClass('hover');
				},function(){
					$(this).removeClass('hover');
				});
				$tbodyField.append($tr);
			}
			$('.sortDrag tr').each(function(){
				$(this).bind('click',function(){
					$('.sortDrag tr.selected').removeClass('selected');
					$(this).addClass('selected');
				});
			});
			$tbodyField.find('tr td').each(function(){
				$(this).unbind();
			});
			// 重新绑定
			binds.initSortDrag();
			$tbodyField.initUI();
		},
		
		update : function(object, index) {
			var $tr = $tbodyField.find('tr[columnNo='+index+']');
			if(0 == $tr.size()) {
				object['mapinfoOrder'] = index + 1;
				funs.save([object], index);
				
				return;
			}
			
			funs.setTableText($tr, object);
			
			funs.set($tr, columnNo, object.columnNo);
			
		},

		/**
		 * 更新querysql字段
		 */
		updateExportSql : function(callbackfn) {
			// resolve sql
			var querysql = $.trim($querysql.val());
			// 前一次val值
			var beforeval = $querysql.attr('beforeval');

			// 未更新query sql 字段值
			if (querysql == beforeval) {
				return;
			}

			funs.resolveQuerySql({
				querysql : querysql,
				sourcedb : $('#sel_dataoptinfo_sourcedb').val()
			}, function(jsonResult) {
				if (undefined != jsonResult.message) {
					funs.alert(jsonResult.message);
					return;
				}
				
				//获取原数据及最新数据
				var $fields = funs.getFieldFormList();
				var efList = jsonResult.efList;
				//将对应位置数据中的字段复制到后台返回的数据
				for(var i = 0; i < efList.length; i++) {
					var ef = efList[i];
					var field = $fields[i];
					
					if('undefined' == typeof field) {
						continue;
					}
					
					ef.fieldType = field.fieldType;
					ef.fieldFormat = field.fieldFormat;
					ef.fieldStoreType = field.fieldStoreType;
					ef.isPk = field.isPk;
					
				}
				
				//清空本地页面
				funs.clean();

				// 更新Field字段
				funs.save(efList);
				
				
				//向隐藏Form中添加数据
				funs.updateFieldFormList(efList, true);
				
				if (undefined != callbackfn && $.isFunction(callbackfn)) {
					callbackfn();
				}
			});

			// update field
		},
		
		
		/**
		 * 更新Field或删除Field字段后，根据最新的数据更新QuerySql的值
		 * @param callbackfn
		 */
		updateExportSqlByField : function(querysql, callbackfn) {
			var objList = funs.getQuerySqlByField();
			
			funs.splitQuerySql(querysql, function(jsonResult) {
				
				var splitsql = jsonResult.splitsql;
				
				var sqlField = [];
				for(var obj in objList) {
					
					sqlField.push('' == $.trim(objList[obj].fieldSentence) ? objList[obj].fieldName : objList[obj].fieldSentence);
				}
				
				//重新拼装querysql并替换
				querysql = splitsql[0] + ' ' + sqlField.join(', ') + ' from ' + splitsql[splitsql.length - 1];

				$querysql.val(querysql);

				
				callbackfn();
			});
		},
		
		
		
		

		updateFieldText : function(object, index) {
		},
		
		/**
		 * 设置页面中Table中的页面值
		 * @param $tr
		 * @param object
		 */
		setTableText : function($tr, object) {
			var $tdArray = $tr.find('td');

			//funs.setText($tdArray.eq(0), object.optStepId);
			
			var optType = object.optType;
			if('1' == optType) {
				optType = '导入';
				funs.setText($tdArray.eq(0), object.importName);
			} else if('2' == optType) {
				optType = '调用接口';
				funs.setText($tdArray.eq(1), object.osName);
			}
			
			funs.setText($tdArray.eq(2), optType);
			//funs.setText($tdArray.eq(3), object.dataOptId);
			funs.setText($tdArray.eq(3), object.mapinfoOrder);
		},
		
		
		updateFieldFormList : function(objList, remove) {
			if(remove) {
				$fieldFormList.children().remove();
			}
			
			//替换排序
			for(var i = 0; i < objList.length; i++) {
				var object = objList[i];
				var exportFieldText = DWZ.frag["DataOptInfoField"];
				
				exportFieldText = exportFieldText.replaceAll('{columnNo}', object.columnNo);
				
				var $ef = $(exportFieldText);
				
				
				for(var name in object) {
					$ef.find(':hidden[name$='+name+']').val(object[name]);
				}
				
				$fieldFormList.append($ef);
			}
			
		},
		
		
		/**
		 * 删除Field操作
		 * @param index
		 */
		deleteField : function(index) {
			//删除隐藏Form中数据
			var $field = funs.getFieldObject(index);
			$field.remove();
			
			//如果删除的非最后一行数据，更新 columnNo值
			funs.updateFieldIndex();
			
			//更新ExportSql 中 querysql字段值
			//var querysql = $querysql.val();

			//funs.updateExportSqlByField(querysql, function(){funs.close();});
		},
		
		/**
		 * 设置隐藏Field Form中的值
		 * @param object
		 * @param index
		 */
		setField : function(object, index) {
			var $field = $fieldFormList.find('field[columnNo=' + index + ']');
			
			if(0 == $field.size()) {
				funs.updateFieldFormList([object]);
				
				return;
			}
			
			var $input = $field.find(':hidden');
			
			$.each($input, function(i, obj){
				var $object = $(obj);
				
				var names = funs.get($object, 'name').split(/\./);
				var name = names[names.length - 1];
				
				$object.val(object[name]);
			});
		},
		
		
		/**
		 * 获取隐藏Field Form中的值
		 * @param index
		 * @returns {___anonymous5974_5975}
		 */
		getField : function(index) {
			var $field = funs.getFieldObject(index);
			
			var $input = $field.find(':hidden');
			
			var object = {};
			object[columnNo] = index;
			$.each($input, function(i, obj){
				var $object = $(obj);
				var names = funs.get($object, 'name').split(/\./);
				
				var name = names[names.length - 1];
				
				object[name] = $object.val();
			});
			
			
			return object;
		},
		
		/**
		 * 获取隐藏Field中的Jquery对象
		 * @param index
		 * @returns
		 */
		getFieldObject : function(index) {
			return $fieldFormList.find('field[columnNo="' + index + '"]');
		},

		/**
		 * 所有FieldForm 数据
		 */
		getFieldFormList : function() {
			var fields = [];
			var $fields = $fieldFormList.find('field');
			
			$.each($fields, function(index, o) {
				fields.push(funs.getField(index));
			});
			
			return fields;
		},

		/**
		 * 获取隐藏域中有多少个Field
		 * @returns
		 */
		getFormFieldMaxIndex : function() {
			var index = $fieldFormList.find('field').size();
			//return 0 == index ? -1 : index;
			return index;
		},
		
		/**
		 * 更新索引，在有移动，删除Table中列的时候重建Index
		 */
		updateFieldIndex : function() {
			 //重建Table展示功能 区的索引
			 var $trs = $tbodyField.find('tr');
			 $.each($trs, function(i, tr) {
				 var $this = $(tr);
				 var index = funs.get($this, columnNo);

				 var $td = $this.find('td:eq(3)');
				 if(i != index) {
					 funs.set($this, columnNo, i);
					 funs.setText($td, i + 1);
					 
					 var $sourceField = funs.getFieldObject(index);
					 funs.set($sourceField, "newcolumnno", i);
				 }
			 });
			 
			 //重建FormField 索引
			 var $fields = $fieldFormList.find('field');
			 
			 $.each($fields, function(i, field) {
				 var $this = $(field);
				 var newcolumnno = funs.get($this, "newcolumnno");
				 
				 if(!funs.isNull(newcolumnno)) {
					 newcolumnno = parseInt(newcolumnno);
					 
					 var $input = $this.find(':hidden');
					 
					 $.each($input, function(j, input) {
						 var $hidden = $(input);
						 
						 var name = funs.get($hidden, 'name');
						 name = name.replace(/\d+/, newcolumnno);
						 
						 funs.set($hidden, 'name', name);
						 
						 if(/mapinfoOrder$/.test(name)) {
							 $hidden.val(newcolumnno + 1);
						 }
						 
					 });
					 
					 funs.set($this, columnNo, newcolumnno);
				 }
				 
				 funs.set($this, "newcolumnno", '');
			 });
			 
		},

		set : function($object, attr, val) {
			$object.attr(attr, val);
		},

		get : function($object, attr) {
			return $object.attr(attr);
		},

		setText : function($object, text) {
			$object.text(text);
		},

		setData : function(key, data) {
			$sysObject.data(key, data);
		},

		getData : function(key) {
			return $sysObject.data(key);
		},

		/**
		 * 解析Sql语句
		 * 
		 * @param querysql
		 * @param fn
		 */
		resolveQuerySql : function(object, fn) {
			$.post(DWZ.contextPath + '/dde/exportSql!resolveQuerySql.do', {
				querySql : object.querysql,
				sourceDatabaseName : object.sourcedb
			}, fn, 'json');
		},
		
		/**
		 * 分割Sql语句
		 * @param querysql
		 * @param fn
		 */
		splitQuerySql : function(querysql, fn) {
			$.post(DWZ.contextPath + '/dde/exportSql!splitQuerySql.do', {
				querySql : querysql
			}, fn, 'json');
		},
		
		/**
		 * 从Field生成QeruySql
		 */
		getQuerySqlByField : function() {
			var objList = $fieldFormList.find('field>:input').serializeArray();
			
			var values = [];
			for(var i = 0; i < objList.length; i++) {
				if(0 == i % 6) {
					var object = {};
					for(var k = 0; k < i + 6; k++) {
						var names = objList[k].name.split(/\./);
						var name = names[names.length - 1];
						
						object[name] = objList[k].value;
						
						//获取在查询结果集中的位置
						var index = objList[k].name.match(/\[(\d+)\]/)[1];
						
						object[columnNo] = index;
					}
					
					values.push(object);
				}
				
			}
			return values;
		},

		alert : function(message) {
			DWZ.ajaxDone({
				statusCode : DWZ.statusCode.error,
				message : message
			});
		},
		
		close : function() {
			$.pdialog.close($.pdialog.getCurrent());
		},
		
		clean : function() {
			$tbodyField.children().remove();
			$exportFieldForm.children().remove();
		},
		
		isNull : function(val) {
			return 'undefined' == typeof val || '' == $.trim(val);
		},
		
		selected : function($obj, val) {
			$obj.attr("value", val);
		}
	};

	this.pubfuns = {
		exportSqlInit : function() {
			// 定时数据源时初始化
			var $defDatasource = $('#div_data_source_page_content_dataoptinfo');
			var $exportSqlForm = $('#div_export_field_dataoptinfo');

			var $objList = $defDatasource.find('textarea');
			
			$.each($objList, function(index, object) {
				var $object = $(object);

				var name = $object.attr('name');

				$object.val($exportSqlForm.find(':hidden[name="' + name + '"]').val());
			});

			var $name = $defDatasource.find('select');

			$defDatasource.find('option[value=' + $exportSqlForm.find(':hidden[name=sourceDatabaseName]').val() + ']').prop('selected', true);
		},
		
		exportFieldCallback : function(object) {
			var $object = $(object);
			
			var columnNoVal = funs.get($object.parents('tr:eq(0)'), columnNo);
			
			var href = DWZ.contextPath + '/dde/dataOptInfo!formField.do?columnNo=' + columnNoVal;
			funs.set($object, 'href', href);
		},
		
		exportFieldInit : function(index) {
			if('undefined' == typeof index || '' == $.trim(index)) {
				return;
			}

			var object = funs.getField(index);
			
			var optType = object['optType'];
			var $optType = $(':radio[name="optType"][value="' + optType + '"]');
			$optType.prop('checked', true);
			
			var $importId = $('#div_dataoptinfo_importId');
			var $osId = $('#div_dataoptinfo_osId');
			
			if('1' == optType) {
				$importId.show();
				$osId.hide();
				funs.selected($('#sel_dataoptinfoId'), object['importId']);
			} else if('2' == optType) {
				$importId.hide();
				$osId.show();
				funs.selected($('#sel_dataoptinfo_osId'), object['osId']);
			}
			
		}
	};
}



var D = function(object) {console.dir(object);};
