function ExportSql() {
	var $defDatasource = $('#div_data_source_page_content');
	var $exportSqlForm = $('#div_export_field');
	var $exportFieldForm = $('#div_export_field_form');
	
	// 新增触发器Form页面
	var $exportTriggerForm = $('#div_export_trigger_form');

	// 添加数据源按钮
	var $btnDefDatasource = $('#btn_def_datasource');

	// 添加字段按钮
	var $btnExportField = $('#btn_export_field');

	// 保存query sql 隐藏域
	var $querysql = $('#hid_querysql');

	// ExportField 表格
	var $tbodyField = $('#tbody_export_field');

	// Field 隐藏Form
	var $fieldFormList = $('#hid_field_form_list');

	var defDsKey = 'DEF_DS_KEY';
	var columnNo = 'columnNo';
	
	// 添加触发器按钮
	var $btnExportTrigger = $('#btn_export_trigger');
	
	// Trigger 列表
	var $tbodyTrigger = $('#tbody_export_trigger');
	
	// 触发器 隐藏Form
	var $triggerFormList = $('#hid_trigger_form_list');
	
	
	//centitui.frag.xml中模板
	var trExportFieldTemplate = 'TrExportFieldTemplate';
	var trExportTriggerTemplate = 'TrExportTriggerTemplate';
	var exportField = 'ExportField';
	var exportTrigger = 'ExportTrigger';
	
	
	var $txtFieldName = $('#txt_field_name_export');
	var $txaFieldSentence = $('#txa_field_sentence_export');
	
	
	var field = 'field';
	var trigger = 'trigger';
	
	
	
	

	
	
	
	
	var $txtFieldName = $('#txt_field_name');
	var $txaFieldSentence = $('#txa_field_sentence');

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
				
				if(funs.isNull($txtFieldName.val())) {
					funs.alert('源字段名为必填项');
					return;
				}
				
				if(funs.isNull($querysql.val())) {
					funs.alert('先配置数据源');
					return;
				}
				var index = $exportFieldForm.find('#hid_export_field_form_columnno').val();
				if(funs.isNull(index)) {
					index = funs.getFormFieldMaxIndex(field);
				}
				var objArray = $exportFieldForm.find(':input, textarea').serializeArray();
				
				//获取表单数据
				var object = {};
				
				for(var i = 0; i < objArray.length; i++) {
					object[objArray[i].name] = objArray[i].value;
				}
				object[columnNo] = index;
				
				//更新表格
				funs.update(field, object, index, funs.setTableText);
				
				//更新隐藏域中form值
				
				funs.setField(field, index, object, funs.updateFieldFormList);
				
				//更新ExportSql 中 querysql字段值
				var querysql = $querysql.val();

				funs.updateExportSqlByField(querysql, field, funs.close);
				
				
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
				
				$this.val(funs.toUpperCase($this.val()));
				$txaFieldSentence.val(funs.toUpperCase($txaFieldSentence.val()));
			});
		},
		
		bindFieldType : function() {
			$('#txt_field_type').bind('keyup', function() {
				var $this = $(this);
				$this.val(funs.toUpperCase($this.val()));
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
				
				
				$this.val(funs.toUpperCase($this.val()));
				$txtFieldName.val(funs.toUpperCase($txtFieldName.val()));
			});
		},
		
		bindTrigger : function() {
			$btnExportTrigger.click(function() {
				var index = $exportTriggerForm.find('#hid_export_trigger_form_triggerId').val();

				if(funs.isNull(index)) {
					index = funs.getFormFieldMaxIndex(trigger);
				}
				//获取表单数据
				var object = funs.getFormToObject($exportTriggerForm, index);
				object.index = index;
				//更新表格
				funs.update(trigger, object, index, funs.setTriggerTableText);
				
				//设置tiggerOrder隐藏域的值
				object.tiggerOrder = index;
				//更新隐藏域中form值

				funs.setField(trigger, index, object, funs.updateFieldFormList);
				funs.close();
			});
		},
		/**
		 * 绑定删除功能
		 */
		bindDelete : function() {
			for(var key in exportBind) {
				exportBind[key].table.find('a.delete').die('click').live('click', function() {
                    //;

                    var $tr = $(this).parent().parent('tr');

//					var $tr = $(this).parent().parent();


					var index = funs.get($tr, columnNo);
                    var type = funs.get($tr, 'type');

					//删除文本
					$tr.remove();

					
					funs.deleteField(type, index);
				});
			}
		},
		
		initSortDrag : function(type) {
			// ;
			var options = {
				cursor : 'move', // selector 的鼠标手势
				sortBoxs : 'tbody.sortDrag', // 拖动排序项父容器
				replace : true, // 2个sortBox之间拖动替换
				items : '> tr', // 拖动排序项选择器
				selector : 'td:first:not(.notMove)', // 拖动排序项用于拖动的子元素的选择器，为空时等于item
				zIndex : 1000,
				callback : funs.sortDragCallback
			};

			if(funs.isNull(type)) {
				for(var key in exportBind) {
					options.callback = exportBind[key].callback;
					exportBind[key].table.sortDrag(options);
				}
			} else {
				options.callback = exportBind[type].callback;
				exportBind[type].table.sortDrag(options);
			}
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
			$('#txt_sdn').val($('#hid_sdn').val());
		},

		/**
		 * 表格拖动列排序回调方法
		 * @param $srcBox
		 * @param $destBox
		 */
		sortDragCallback : function($srcBox, $destBox) {
			var f = field;
			funs.splitQuerySql($querysql.val(), function(jsonResult) {
				funs.updateFieldIndex(f);

				var splitsql = jsonResult.splitsql;

				var fieldList = funs.getFieldFormList(f);

				var querysql = 'SELECT ';

				var fields = [];
				for(var field in fieldList) {
					fields.push(funs.isNull(fieldList[field].fieldSentence) ? fieldList[field].fieldName : fieldList[field].fieldSentence);
				}

				querysql += (fields.join(', ') + ' FROM ' + splitsql[splitsql.length - 1]);

				$querysql.val(querysql);
			});
		},


		triggerSortDragCallback : function($srcBox, $destBox) {
			funs.updateFieldIndex(trigger, function($tr, index) {
				var $td = $tr.find('td:eq(3)');
				funs.setText($td, index + 1);
			}, function($input, name, index) {
				if(/tiggerOrder$/.test(name)) {
					$input.val(index);
				}
			});
		},

		/**
		 * 将Field表单中的值保存或更新进表格
		 *
		 * @param $objList
		 */
		/*save : function(objList) {
			// ;
			// 获取原先表格中的字段


			// 根据query sql解析的字段进行更新
			for ( var i = 0; i < objList.length; i++) {
				var object = objList[i];

				var $tr = $(DWZ.frag['TrExportFieldTemplate']);

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
		},*/

		/*update : function(object, index) {
			var $tr = $tbodyField.find('tr[columnNo='+index+']');
			if(0 == $tr.size()) {
				funs.save([object], index);

				return;
			}
			funs.setTableText($tr, object);

			funs.set($tr, columnNo, object.columnNo);

		},*/


		/**
		 * 将Field表单中的值保存或更新进表格
		 *
		 * @param $objList
		 */
		save : function(type, objList, setTableText, index) {
			// 获取原先表格中的字段

			// 根据query sql解析的字段进行更新
			for ( var i = 0; i < objList.length; i++) {
				var object = objList[i];

				var $tr = $(DWZ.frag[exportBind[type].ttemplate]);

				setTableText($tr, object);
				funs.set($tr, columnNo, object.columnNo);
				$tr.show();
				funs.trClass($tr, object.columnNo);
				exportBind[type].table.append($tr);
			}
			$('.sortDrag tr').each(function(){
				$(this).bind('click',function(){
					$('.sortDrag tr.selected').removeClass('selected');
					$(this).addClass('selected');
				});
			});
			exportBind[type].table.find('tr td').each(function(){
				$(this).unbind();
			});
			// 重新绑定
			binds.initSortDrag(type);
			exportBind[type].table.initUI();
		},

		/**
		 *
		 * @param $object
		 * @param object
		 * @param index
		 * @param fn 设置TableText回调方法
		 */
		update : function(type, object, index, setTableText) {
			var $tr = exportBind[type].table.find('tr[columnNo='+index+']');
			if(0 == $tr.size()) {
				funs.save(type, [object], setTableText, index);

				return;
			}
			setTableText($tr, object);

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
				sourcedb : $('#sel_sourcedb').val()
			}, function(jsonResult) {
				if (undefined != jsonResult.message) {
					funs.alert(jsonResult.message);
					return;
				}

				//获取原数据及最新数据
				var $fields = funs.getFieldFormList(field);
				var efList = jsonResult.efList;
				//;
				//将对应位置数据中的字段复制到后台返回的数据
				for(var i = 0; i < efList.length; i++) {
					var ef = efList[i];
					var fieldObj = $fields[i];

					if(funs.isNull(fieldObj)) {
						continue;
					}

					ef.fieldType = fieldObj.fieldType;
					ef.fieldFormat = fieldObj.fieldFormat;
					ef.fieldStoreType = fieldObj.fieldStoreType;
					ef.isPk = fieldObj.isPk;

				}

				//清空本地页面
				funs.clean();

				// 更新Field字段
				funs.save(field, efList, funs.setTableText);


				//向隐藏Form中添加数据
				funs.updateFieldFormList(field, efList, true);

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
		updateExportSqlByField : function(querysql, type, callbackfn) {
			var objList = funs.getQuerySqlByField(type);

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

			funs.setText($tdArray.eq(0), object.fieldName);
			funs.setText($tdArray.eq(1), object.fieldSentence);
			funs.setText($tdArray.eq(2), object.fieldType);
			funs.setText($tdArray.eq(3), object.fieldFormat);


			var fieldStoreType = object.fieldStoreType;
			if(1 == fieldStoreType) {
				funs.setText($tdArray.eq(4), 'infile');
			}else if(0 === parseInt(fieldStoreType)) {
				funs.setText($tdArray.eq(4), 'embedded');
			}

			funs.setText($tdArray.eq(5), 1 == object.isPk ? '是' : '否');
		},

		/**
		 * 设置页面中Table中的页面值
		 * @param $tr
		 * @param object
		 */
		setTriggerTableText : function($tr, object) {
			var $tdArray = $tr.find('td');

			var triggerSql = object.triggerSql;
			var $triggerSqlObj = $tdArray.eq(0);
			funs.set($triggerSqlObj, 'title', triggerSql);

			if(35 < triggerSql.length) {
				triggerSql = triggerSql.substring(0, 34) + '...';
			}

			funs.setText($triggerSqlObj, triggerSql);

			var triggerType = '行触发器';
			if('T' == object.triggerType) {
				triggerType = '表触发器';
			}

			var triggerTime = '交换前';
			if('A' == object.triggerTime) {
				triggerTime = '交换后';
			} else if('E' == object.triggerTime) {
				triggerTime = '交换失败后';
			}

			funs.setText($tdArray.eq(1), triggerType);
			funs.setText($tdArray.eq(2), triggerTime);
			funs.setText($tdArray.eq(3), parseInt(object.index) + 1);
			funs.setText($tdArray.eq(4), 'T' == object.isprocedure ? '是' : '否');
		},


		updateFieldFormList : function(type, objList, remove) {
			if(remove) {
				exportBind[type].form.children().remove();
			}
			//替换排序
			for(var i = 0; i < objList.length; i++) {
				//;
				var object = objList[i];
				var exportFieldText = DWZ.frag[exportBind[type].ftemplate];

				exportFieldText = exportFieldText.replaceAll('{columnNo}', object.columnNo);

				var $ef = $(exportFieldText);

				for(var name in object) {
					$ef.find(':hidden[name$='+name+']').val(object[name]);
				}

				exportBind[type].form.append($ef);
			}

		},


		/**
		 * 删除Field操作
		 * @param index
		 */
		deleteField : function(type, index) {
			//删除隐藏Form中数据
			var $field = funs.getFieldObject(type, index);
			$field.remove();

			//如果删除的非最后一行数据，更新 columnNo值
			funs.updateFieldIndex(type);

			//更新ExportSql 中 querysql字段值
			var querysql = $querysql.val();

			funs.updateExportSqlByField(querysql, type, funs.close);
		},

		/**
		 * 设置隐藏Field Form中的值
		 * @param object
		 * @param index
		 */
		/*setField : function(object, index) {
			var $field = $fieldFormList.find('li[columnNo=' + index + ']');

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
		},*/


		/**
		 * 设置隐藏Field Form中的值
		 * @param object
		 * @param index
		 */
		setField : function(type, index, object, updateFieldFormList) {
			var $object = exportBind[type].form;
			var $field = $object.find('li[columnNo=' + index + ']');

			if(0 == $field.size()) {
				updateFieldFormList(type, [object]);

				return;
			}

			var $input = $field.find(':hidden');

			$.each($input, function(i, obj){
				var $object = $(obj);

				var names = funs.get($object, 'name').split(/\./);
				var name = names[names.length - 1];

				$object.val(funs.isNull(object[name]) ? '' : object[name]);
			});
		},


		/**
		 * 获取隐藏Field Form中的值
		 * @param index
		 * @returns {___anonymous5974_5975}
		 */
		getField : function(type, index) {
			var $field = funs.getFieldObject(type, index);

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
		 */
		getFieldObject : function(type, index) {
			return exportBind[type].form.find('li[columnNo="' + index + '"]');
		},

		/**
		 * 将Form元素收集至Object对象中
		 * @param $object
		 * @param index
		 */
		getFormToObject : function($object, index) {
			var objArray = $object.find(':input, textarea').serializeArray();

			//获取表单数据
			var object = {};

			for(var i = 0; i < objArray.length; i++) {
				object[objArray[i].name] = objArray[i].value;
			}
			object[columnNo] = index;


			return object;
		},

		/**
		 * 所有FieldForm 数据
		 */
		getFieldFormList : function(type) {
			var fields = [];
			var $fields = exportBind[type].form.find('li');

			$.each($fields, function(index, o) {
				fields.push(funs.getField(type, index));
			});

			return fields;
		},

		/**
		 * 获取隐藏域中有多少个Field
		 * @params type
		 *
		 */
		getFormFieldMaxIndex : function(type) {
			var index = exportBind[type].form.find('li').size();
			//return 0 == index ? -1 : index;
			return index;
		},

		/**
		 * 更新索引，在有移动，删除Table中列的时候重建Index
		 */
		updateFieldIndex : function(type, textfn, formfn) {
			//debugger;
			//重建Table展示功能 区的索引
			var newcol = "newcolumnno";


			 var $trs = exportBind[type].table.find('tr');
			 $.each($trs, function(i, tr) {
				 var $this = $(tr);
				 var index = funs.get($this, columnNo);

				 funs.set($this, columnNo, i);

				 var $sourceField = funs.getFieldObject(type, index);
				 funs.set($sourceField, newcol, i);


				 if(textfn) {
					 textfn($this, i);
				 }

			 });

			 //重建FormField 索引
			 var $lis = exportBind[type].form.find("li");

			 $.each($lis, function(i, li) {
				 var $this = $(li);
				 var newcolumnno = funs.get($this, newcol);

				 if(!funs.isNull(newcolumnno)) {
					 newcolumnno = parseInt(newcolumnno);

					 var $input = $this.find(':hidden');

					 $.each($input, function(j, input) {
						 var $hidden = $(input);

						 var name = funs.get($hidden, 'name');
						 name = name.replace(/\d+/, newcolumnno);

						 funs.set($hidden, 'name', name);

						 if(formfn) {
							 formfn($hidden, name, newcolumnno);
						 }
					 });
				 }

				 funs.set($this, columnNo, newcolumnno);
			 });


			 $.each(exportBind[type].table.find('tr'), function(index, tr) {
				 funs.trClass($(tr), index);
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
		getQuerySqlByField : function(type) {
			var objList = exportBind[type].form.find('li>:input').serializeArray();

			//将隐藏域中的值构建Object对象
			var length = exportBind[type].form.find('li:eq(0)>:input').size();

			var values = [];
			for(var i = 0; i < objList.length; i++) {
				if(0 == i % length) {
					var object = {};
					for(var k = 0; k < i + length; k++) {
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

		trClass : function($tr, columnNo) {
			if(1 == parseInt(columnNo) % 2){
				$tr.addClass('trbg');
			} else {
				$tr.removeClass('trbg');
			}

			$tr.hover(function(){
				$(this).addClass('hover');
			},function(){
				$(this).removeClass('hover');
			});
		},

		alert : function(message) {
			DWZ.ajaxDone({
				statusCode : DWZ.statusCode.error,
				message : message
			});
		},

		close : function() {
            try {
			    $.pdialog.close($.pdialog.getCurrent());
            } catch(ex) {

            }

		},

		clean : function() {
			$tbodyField.children().remove();
			$exportFieldForm.children().remove();
		},
		
		isNull : function(val) {
			return 'undefined' == typeof val || null == val ||'' == $.trim(val);
		},
		
		toUpperCase : function(val) {
			if(!funs.isNull(val)) {
				return val.toUpperCase();
			}
		}
	};

	
	// 元素的绑定，隐藏Form 展示表格
	var exportBind = {
		'field' : {
			'form' : $fieldFormList,
			'table' : $tbodyField,
			'ttemplate' : trExportFieldTemplate,
			'ftemplate' : exportField,
			'callback' : funs.sortDragCallback
		},
		'trigger' : {
			'form' : $triggerFormList,
			'table' : $tbodyTrigger,
			'ttemplate' : trExportTriggerTemplate,
			'ftemplate' : exportTrigger,
			'callback' : funs.triggerSortDragCallback
		}
	};
	
	this.pubfuns = {
		exportSqlInit : function() {
			// 定时数据源时初始化
			var $defDatasource = $('#div_data_source_page_content');
			var $exportSqlForm = $('#div_export_field');

			var $objList = $defDatasource.find('textarea');
			
			$.each($objList, function(index, object) {
				var $object = $(object);

				var name = $object.attr('name');

				$object.val($exportSqlForm.find(':hidden[name="' + name + '"]').val());
			});

			var $name = $defDatasource.find('select');

			$defDatasource.find('option[value=' + $exportSqlForm.find(':hidden[name=sourceDatabaseName]').val() + ']').prop('selected', true);
		},
		
		exportFieldCallback : function(object, method) {
			var $object = $(object);
			
			var columnNoVal = funs.get($object.parents('tr:eq(0)'), columnNo);
			
			var href = DWZ.contextPath + '/dde/exportSql!' + method + '.do?columnNo=' + columnNoVal;
			
			funs.set($object, 'href', href);
		},
		
		exportFieldInit : function(index) {
			if('undefined' == typeof index || '' == $.trim(index)) {
				return;
			}

			var object = funs.getField(field, index);
			
			var $input = $exportFieldForm.find(':text, textarea');
			$.each($input, function(i, input){
				var $object = $(input);
				var name = funs.get($object, 'name');
				$object.val(object[name]);
				
			});
			
			var fstVal = object['fieldStoreType'];
			$exportFieldForm.find(':radio[value=' + fstVal + ']').attr('checked', true);
			
			var ipVal = object['isPk'];
			if(1 == ipVal) {
				$exportFieldForm.find('#chk_ispk').attr('checked', true);
			}
		},
		
		exportTriggerInit : function(index) {
			if('undefined' == typeof index || '' == $.trim(index)) {
				return;
			}
			var object = funs.getField(trigger, index);
			
			var $input = $exportTriggerForm.find(':text, textarea');
			$.each($input, function(i, input){
				var $object = $(input);
				var name = funs.get($object, 'name');
				$object.val(object[name]);
				
			});
			$exportTriggerForm.find(':radio[value="' + object['triggerType'] + '"]').prop('checked', true);
			$exportTriggerForm.find(':radio[value="' + object['triggerTime'] + '"]').prop('checked', true);
			$exportTriggerForm.find(':checkbox[value="' + object['isprocedure'] + '"]').prop('checked', true);
			
		}
	};
}

