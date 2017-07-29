function ExportSql() {
    var $defDatasource = $('#div_data_source_page_content_import');
    var $exportSqlForm = $('#div_import_field');
    var $exportFieldForm = $('#div_import_field_form');

    // 新增触发器Form页面
    var $exportTriggerForm = $('#div_import_trigger_form');

    // 添加数据源按钮
    var $btnDefDatasource = $('#btn_def_datasource_import');

    // 添加字段按钮
    var $btnExportField = $('#btn_import_field');

    // 添加触发器按钮
    var $btnExportTrigger = $('#btn_import_trigger');

    // 保存query sql 隐藏域
    var $querysql = $('#hid_querysql_import');

    // ExportField 表格
    var $tbodyField = $('#tbody_import_field');

    // Trigger 列表
    var $tbodyTrigger = $('#tbody_import_trigger');

    // Field 隐藏Form
    var $fieldFormList = $('#hid_field_form_list_import');
    // 触发器 隐藏Form
    var $triggerFormList = $('#hid_trigger_form_list_import');

    var defDsKey = 'DEF_DS_KEY';
    var columnNo = 'columnNo';


    var trImportFieldTemplate = 'TrImportFieldTemplate';
    var trImportTriggerTemplate = 'TrImportTriggerTemplate';


    var importField = 'ImportField';
    var importTrigger = 'ImportTrigger';


    var $txtFieldName = $('#txt_field_name_import');
    var $txaFieldSentence = $('#txa_field_sentence_import');


    // 元素的绑定，隐藏Form 展示表格
    var importBind = {
        'field': {
            'form': $fieldFormList,
            'table': $tbodyField
        },
        'trigger': {
            'form': $triggerFormList,
            'table': $tbodyTrigger
        }
    };

    this.init = function () {
        for (var e in binds) {
            binds[e]();
        }
    };

    this.bind = function (method) {
        binds[method]();
    };

    var binds = {
        bindDefDatasource: function () {
            // $defDatasource.unbind('click');
            $btnDefDatasource.click(function () {
                funs.exportSqlComplete();

                $querysql.val('select * from ' + $('#txt_tableName_import').val());

                funs.updateExportSql(function () {

                    $exportSqlForm.find('#hid_tableName').val($defDatasource.find('#txt_tableName_import').val());
                    funs.close();
                });
            });
        },

        bindExportField: function () {
            $btnExportField.click(function () {
                if (funs.isNull($txtFieldName.val())) {
                    funs.alert('源字段名为必填项');
                    return;
                }

                var index = $exportFieldForm.find('#hid_import_field_form_columnno').val();
                if (funs.isNull(index)) {
                    index = funs.getFormFieldMaxIndex();
                }

                //获取表单数据
                var object = funs.getFormToObject($exportFieldForm, index);

                //更新表格
                funs.update($tbodyField, object, trImportFieldTemplate, index, funs.setTableText);

                //更新隐藏域中form值
                funs.setField($fieldFormList, $fieldFormList.find('field[columnNo=' + index + ']'), object, importField, funs.updateFieldFormList);

                funs.close();
            });
        },

        bindTrigger: function () {
            $btnExportTrigger.click(function () {
                var index = $exportTriggerForm.find('#hid_import_trigger_form_triggerId').val();
                if (funs.isNull(index)) {
                    index = funs.getFormFieldMaxIndex($triggerFormList, 'trigger');
                }
                //获取表单数据
                var object = funs.getFormToObject($exportTriggerForm, index);
                object.index = index;
                //更新表格
                funs.update($tbodyTrigger, object, trImportTriggerTemplate, index, funs.setTriggerTableText);

                //设置tiggerOrder隐藏域的值
                object.tiggerOrder = index;
                //更新隐藏域中form值
                funs.setField($triggerFormList, $triggerFormList.find('trigger[columnNo=' + index + ']'), object, importTrigger, funs.updateFieldFormList);

                funs.close();
            });
        },

        /**
         * 绑定删除功能
         */
        bindDelete: function () {
            for (var key in importBind) {
                importBind[key].table.find('a.delete').die('click').live('click', function () {
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

        initSortDrag: function () {
            var options = {
                cursor: 'move', // selector 的鼠标手势
                sortBoxs: 'tbody.sortDrag', // 拖动排序项父容器
                replace: true, // 2个sortBox之间拖动替换
                items: '> tr', // 拖动排序项选择器
                selector: 'td:first:not(.notMove)', // 拖动排序项用于拖动的子元素的选择器，为空时等于item
                zIndex: 1000,
                callback: funs.sortDragCallback
            };
            $tbodyField.sortDrag(options);


            options.callback = funs.sortDragTriggerCallback;
            $tbodyTrigger.sortDrag(options);
        },


        bindImportField: function () {
            $('#file_upload').uploadify({
                'buttonText': '导入源字段配置',
                'height': 24,
                'folder': '/uploads',
                'fileObjName': 'uploadify',
                'swf': DWZ.contextPath + '/scripts/plugin/uploadify-v3.1/uploadify.swf',
                'uploader': DWZ.contextPath + '/dde/importOpt!uploadify.do',
                'onUploadSuccess': function (file, data, response) {
                   
                	data = $.parseJSON(data);

                    var objList = [];
                    var len = 0;
                    for (var key in data) {
                        objList.push({
                            columnNo: len,
                            destFieldName: data[key]['fieldName'],
                            destFieldType: data[key]['fieldType']
                        });

                        len++;
                    }

                    //清理页面和隐藏域中的值，清理掉超出的部分
                    var fieldFormList = funs.getFieldFormList();
                    var fieldLength = fieldFormList.length;


                    //导入数据与隐藏域的差值
                    var objLength = objList.length;

                    var differenceLength = fieldLength - objLength;

                    //清理超出的部分
                    if (differenceLength > 0) {
                        var $tdList = $('tr', $tbodyField).find('td:eq(0)');
                        for (var i = objList.length; i < fieldLength; i++) {

                            $($tdList.get(i)).empty();

                            //只将隐藏域中的值置空
                            //var $field = funs.getFieldObject($fieldFormList, 'field', i);
                            //var $sourceFieldName = $("input[name$='sourceFieldName']", $field);
                            //funs.set($sourceFieldName, 'value', '');

                            //删除隐藏域中的值
                            var $field = funs.getFieldObject($fieldFormList, 'field', i);
                            $field.remove();

                            //以上逻辑二选一
                        }
                        //以源或目标最大的值为基准，清理其它的值

                        var $differ = $('tr:gt(' + (objLength - 1) + ')', $tbodyField);

                        $differ.remove();

                    }


                    funs.updateSourceField($tbodyField, objList, trImportFieldTemplate, funs.setTableText);

                    funs.updateFieldFormList($fieldFormList, objList, importField, false);

                }
            });
        },


        bindDestToSourceAync: function () {
            $('#a_import_synchronization').click(function () {
                if (confirm('目标字段将覆盖源字段，是否确定覆盖？')) {
                    var $tdList = $('tr', $tbodyField).find('td:lt(2)');
                    //更新表格文本内容
                    for (var i = 0; i < $tdList.length; i += 2) {
                        funs.setText($($tdList.get(i)), $($tdList.get(i + 1)).text())
                    }


                    //更新隐藏域
                    var maxLength = funs.getFormFieldMaxIndex($fieldFormList, 'field');
                    for (var i = 0; i < maxLength; i++) {
                        var $fieldObject = funs.getFieldObject($fieldFormList, 'field', i);

                        var $sourceFieldName = $("input[name$='sourceFieldName']", $fieldObject);
                        var $destFieldName = $("input[name$='destFieldName']", $fieldObject);

                        funs.set($sourceFieldName, 'value', funs.get($destFieldName, 'value'));
                    }
                }
            });
        }
    };

    var funs = {
        exportSqlComplete: function () {
            // 保存数据源时将数据存在页面的hidden中
            var $objList = $defDatasource.find('input,textarea,select');

            $.each($objList, function (index, object) {
                var $object = $(object);

                var name = $object.attr('name');
                $exportSqlForm.find(':hidden[name="' + name + '"]').val($object.val());
            });

            //D($defDatasource.find(':checked[name="recordOperate"]'));
            $('#hid_recordOperate_import').val($defDatasource.find(':checked[name="recordOperate"]').val());
            //datasource
            $('#txt_sdn_import').val($('#hid_sdn_import').val());
        },

        /**
         * 表格拖动列排序回调方法
         * @param $srcBox
         * @param $destBox
         */
        sortDragCallback: function ($srcBox, $destBox) {
            funs.updateFieldIndex('field');
        },

        /**
         * 表格拖动列排序回调方法
         * @param $srcBox
         * @param $destBox
         */
        sortDragTriggerCallback: function ($srcBox, $destBox) {
            funs.updateFieldIndex(trigger, function ($tr, index) {
                var $td = $tr.find('td:eq(3)');
                funs.setText($td, index + 1);
            }, function ($input, name, index) {
                if (/tiggerOrder$/.test(name)) {
                    $input.val(index);
                }
            });

        },

        /**
         * 将Field表单中的值保存或更新进表格
         *
         * @param $objList
         */
        save: function ($object, objList, template, setTableText, index) {
            // 获取原先表格中的字段
            // 根据query sql解析的字段进行更新
            for (var i = 0; i < objList.length; i++) {
                var object = objList[i];

                var $tr = $(DWZ.frag[template]);

                setTableText($tr, object);
                funs.set($tr, columnNo, object.columnNo);
                $tr.show();
                if (parseInt(object.columnNo) % 2 == 1) {
                    $tr.addClass('trbg');
                }
                $tr.hover(function () {
                    $(this).addClass('hover');
                }, function () {
                    $(this).removeClass('hover');
                });
                $object.append($tr);
            }
            $('.sortDrag tr').each(function () {
                $(this).bind('click', function () {
                    $('.sortDrag tr.selected').removeClass('selected');
                    $(this).addClass('selected');
                });
            });
            $object.find('tr td').each(function () {
                $(this).unbind();
            });
            // 重新绑定
            binds.initSortDrag();
            $object.initUI();
        },

        //源字段通过上传文件进行更新
        updateSourceField: function ($object, objList, template, setTableText, index) {
            // 根据query sql解析的字段进行更新

            var fieldFormList = funs.getFieldFormList();
            var fieldFormSize = fieldFormList.length;

            for (var i = 0; i < objList.length; i++) {
                var object = objList[i];

                var $tr = $(DWZ.frag[template]);

                setTableText($tr, object);
                funs.set($tr, columnNo, object.columnNo);
                $tr.show();
                if (parseInt(object.columnNo) % 2 == 1) {
                    $tr.addClass('trbg');
                }
                $tr.hover(function () {
                    $(this).addClass('hover');
                }, function () {
                    $(this).removeClass('hover');
                });

                if (i > fieldFormSize) {
                    $object.append($tr);
                } else {
                    funs.update($tbodyField, object, template, i, setTableText);
                }

            }
            $('.sortDrag tr').each(function () {
                $(this).bind('click', function () {
                    $('.sortDrag tr.selected').removeClass('selected');
                    $(this).addClass('selected');
                });
            });
            $object.find('tr td').each(function () {
                $(this).unbind();
            });
            // 重新绑定
            binds.initSortDrag();
            $object.initUI();
        },


        /**
         *
         * @param $object
         * @param object
         * @param index
         * @param fn 设置TableText回调方法
         */
        update: function ($object, object, template, index, setTableText) {
            var $tr = $object.find('tr[columnNo=' + index + ']');
            if (0 == $tr.size()) {
                funs.save($object, [object], template, setTableText, index);

                return;
            }
            setTableText($tr, object);

            funs.set($tr, columnNo, object.columnNo);

        },

        /**
         * 更新querysql字段
         */
        updateExportSql: function (callbackfn) {
            // resolve sql
            var querysql = $.trim($querysql.val());
            // 前一次val值
            var beforeval = $querysql.attr('beforeval');

            // 未更新query sql 字段值
            if (querysql == beforeval) {
                return;
            }
            //;
            funs.resolveQuerySql({
                querysql: querysql,
                sourcedb: $('#sel_sourcedb_import').val()
            }, function (jsonResult) {
                if (undefined != jsonResult.message) {
                    funs.alert(jsonResult.message);
                    return;
                }

                //获取原数据及最新数据
                var $fields = funs.getFieldFormList();
                var efList = jsonResult.efList;
                //;
                //将对应位置数据中的字段复制到后台返回的数据
                for (var i = 0; i < efList.length; i++) {
                    var ef = efList[i];
                    var field = $fields[i];

                    if ('undefined' == typeof field) {
                        continue;
                    }
                    //;
                    //ef.fieldType = field.fieldType;
                    //ef.fieldFormat = field.fieldFormat;
                    //ef.fieldStoreType = field.fieldStoreType;
                    ef.isPk = field.isPk;
                    ef.isNull = field.isNull;

                }

                //清空本地页面
                funs.clean();
//debugger;
                // 更新Field字段
                funs.save($tbodyField, efList, trImportFieldTemplate, funs.setTableText);


                //向隐藏Form中添加数据
                funs.updateFieldFormList($fieldFormList, efList, importField, true);

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
        updateExportSqlByField: function (querysql, callbackfn) {
//			;
            var objList = funs.getQuerySqlByField();

            funs.splitQuerySql(querysql, function (jsonResult) {

                var splitsql = jsonResult.splitsql;

                var sqlField = [];
                for (var obj in objList) {

                    sqlField.push('' == $.trim(objList[obj].fieldSentence) ? objList[obj].fieldName : objList[obj].fieldSentence);
                }

                //重新拼装querysql并替换
                querysql = splitsql[0] + ' ' + sqlField.join(', ') + ' from ' + splitsql[splitsql.length - 1];

                $querysql.val(querysql);


                callbackfn();
            });
        },


        updateFieldText: function (object, index) {
        },

        /**
         * 设置页面中Table中的页面值
         * @param $tr
         * @param object
         */
        setTableText: function ($tr, object) {
            var $tdArray = $tr.find('td');

            try {
                funs.setText($tdArray.eq(0), object.sourceFieldName);
                funs.setText($tdArray.eq(1), funs.isNull(object.fieldName) ? object.destFieldName : object.fieldName);
                funs.setText($tdArray.eq(2), funs.isNull(object.fieldType) ? object.destFieldType : object.fieldType);
                funs.setText($tdArray.eq(3), 1 == object.isPk ? '是' : '否');
                funs.setText($tdArray.eq(4), 1 == object.isNull ? '是' : '否');
            } catch (ex) {
                //console.error(ex);
            }

        },

        /**
         * 设置页面中Table中的页面值
         * @param $tr
         * @param object
         */
        setTriggerTableText: function ($tr, object) {
            var $tdArray = $tr.find('td');

            var triggerSql = object.triggerSql;
            var $triggerSqlObj = $tdArray.eq(0);
            funs.set($triggerSqlObj, 'title', triggerSql);

            if (35 < triggerSql.length) {
                triggerSql = triggerSql.substring(0, 34) + '...';
            }

            funs.setText($triggerSqlObj, triggerSql);

            var triggerType = '行触发器';
            if ('T' == object.triggerType) {
                triggerType = '表触发器';
            }

            var triggerTime = '交换前';
            if ('A' == object.triggerTime) {
                triggerTime = '交换后';
            } else if ('E' == object.triggerTime) {
                triggerTime = '交换失败后';
            }

            funs.setText($tdArray.eq(1), triggerType);
            funs.setText($tdArray.eq(2), triggerTime);
            funs.setText($tdArray.eq(3), parseInt(object.index) + 1);
            funs.setText($tdArray.eq(4), 'T' == object.isprocedure ? '是' : '否');
        },


        updateFieldFormList: function ($object, objList, template, remove) {
            if (remove)
                $object.children().remove();
        	// 替换排序
            for (var i = 0; i < objList.length; i++) {
                var object = objList[i];
                var exportFieldText = DWZ.frag[template];
                exportFieldText = exportFieldText.replaceAll('{columnNo}', object.columnNo);
                var $ef = $(exportFieldText);
                if (!$object.find("input")[0] || !$object.find(":hidden[columnno='" + object.columnNo + "']")[0])
                	$object.append($ef);
                $ef = $object.find(":hidden[columnno='" + object.columnNo + "']");
                for (var name in object) {
                    $ef.find(':hidden[name$=' + name + ']').val(object[name]);
                    if ('fieldName' == name) {
                        //$ef.find(':hidden[name$="sourceFieldName"]').val(object[name]);
                        $ef.find(':hidden[name$="destFieldName"]').val(object[name]);
                    } else if ('fieldType' == name)
                        $ef.find(':hidden[name$="destFieldType"]').val(object.fieldType);
                }
                var $tr = funs.getFieldObject($fieldFormList, 'field', object.columnNo);
                var $sourceFieldName = $("input[name$='sourceFieldName']", $tr);
                funs.set($sourceFieldName, 'value', objList[i].sourceFieldName);
            }
        },


        /**
         * 删除Field操作
         * @param index
         */
        deleteField: function (type, index) {
            //删除隐藏Form中数据
            var $field = funs.getFieldObject(importBind[type].form, type, index);
            $field.remove();

            //如果删除的非最后一行数据，更新 columnNo值
            funs.updateFieldIndex(type);

            //更新ExportSql 中 querysql字段值
            var querysql = $querysql.val();

            funs.updateExportSqlByField(querysql, /*funs.close*/function () {
            });
        },

        /**
         * 设置隐藏Field Form中的值
         * @param object
         * @param index
         */
        setField: function ($object, $field, object, template, updateFieldFormList) {
//            if (0 == $field.size()) {
                updateFieldFormList($object, [object], template);

                return;
//            }

            var $input = $field.find(':hidden');

            $.each($input, function (i, obj) {
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
        getField: function ($object, field, index) {
            var $field = funs.getFieldObject($object, field, index);

            var $input = $field.find(':hidden');

            var object = {};
            object[columnNo] = index;
            $.each($input, function (i, obj) {
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
        getFieldObject: function ($object, field, index) {
            return $object.find(field + '[columnNo="' + index + '"]');
        },

        /**
         * 所有FieldForm 数据
         */
        getFieldFormList: function () {
            var fields = [];
            var $fields = importBind.field.form.find('field');

            $.each($fields, function (index, o) {
                fields.push(funs.getField(importBind.field.form, 'field', index));
            });

            return fields;
        },

        /**
         * 获取隐藏域中有多少个Field
         */
        getFormFieldMaxIndex: function ($object, field) {
            var index = 0;
            //return 0 == index ? -1 : index;

            if (undefined == $object && undefined == field) {
                index = importBind.field.form.find('field').size();
            } else {
                index = $object.find(field).size();
            }

            return index;
        },

        /**
         * 更新索引，在有移动，删除Table中列的时候重建Index
         */
        updateFieldIndex: function (type, textfn, formfn) {
            //重建Table展示功能 区的索引

            var $trs = importBind[type].table.find('tr');
            $.each($trs, function (i, tr) {
                var $this = $(tr);
                var index = funs.get($this, columnNo);

                if (i != index) {
                    funs.set($this, columnNo, i);

                    var $sourceField = funs.getFieldObject(importBind[type].form, type, index);
                    funs.set($sourceField, "newcolumnno", i);
                }


                if (textfn) {
                    textfn($this, i);
                }
            });

            //重建FormField 索引
            var $fields = importBind[type].form.find(type);

            $.each($fields, function (i, field) {
                var $this = $(field);
                var newcolumnno = funs.get($this, "newcolumnno");

                if (!funs.isNull(newcolumnno)) {
                    newcolumnno = parseInt(newcolumnno);

                    var $input = $this.find(':hidden');

                    $.each($input, function (j, input) {
                        var $hidden = $(input);

                        var name = funs.get($hidden, 'name');
                        name = name.replace(/\d+/, newcolumnno);

                        funs.set($hidden, 'name', name);

                        if (formfn) {
                            formfn($hidden, name, newcolumnno);
                        }
                    });
                }

                funs.set($this, columnNo, newcolumnno);
            });

            $.each(importBind[type].table.find('tr'), function (index, tr) {
                funs.trClass($(tr), index);
            });

        },

        set: function ($object, attr, val) {
            $object.attr(attr, val);
        },

        get: function ($object, attr) {
            return $object.attr(attr);
        },

        setText: function ($object, text) {
            $object.text(text);
        },


        /**
         * 解析Sql语句
         *
         * @param querysql
         * @param fn
         */
        resolveQuerySql: function (object, fn) {
            $.post(DWZ.contextPath + '/dde/exportSql!resolveQuerySql.do', {
                querySql: object.querysql,
                sourceDatabaseName: object.sourcedb
            }, fn, 'json');
        },

        /**
         * 分割Sql语句
         * @param querysql
         * @param fn
         */
        splitQuerySql: function (querysql, fn) {
            $.post(DWZ.contextPath + '/dde/exportSql!splitQuerySql.do', {
                querySql: querysql
            }, fn, 'json');
        },

        /**
         * 从Field生成QeruySql
         */
        getQuerySqlByField: function () {
            var objList = $fieldFormList.find('field>:input').serializeArray();

            var values = [];
            for (var i = 0; i < objList.length; i++) {
                if (0 == i % 6) {
                    var object = {};
                    for (var k = 0; k < i + 6; k++) {
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

        /**
         * 将Form元素收集至Object对象中
         * @param $form
         * @param index
         */
        getFormToObject: function ($form, index) {
            var objArray = $form.find(':input, textarea').serializeArray();

            //获取表单数据
            var object = {};

            for (var i = 0; i < objArray.length; i++) {
                object[objArray[i].name] = objArray[i].value;
            }
            object[columnNo] = index;


            return object;
        },

        trClass: function ($tr, columnNo) {
            if (1 == parseInt(columnNo) % 2) {
                $tr.addClass('trbg');
            } else {
                $tr.removeClass('trbg');
            }

            $tr.hover(function () {
                $(this).addClass('hover');
            }, function () {
                $(this).removeClass('hover');
            });
        },

        alert: function (message) {
            DWZ.ajaxDone({
                statusCode: DWZ.statusCode.error,
                message: message
            });
        },

        close: function () {
            $.pdialog.close($.pdialog.getCurrent());
        },

        clean: function () {
            $tbodyField.children().remove();
            $exportFieldForm.children().remove();
        },

        isNull: function (val) {
            return 'undefined' == typeof val || '' == $.trim(val);
        }
    };

    this.pubfuns = {
        exportSqlInit: function () {
            // 定时数据源时初始化
            var $defDatasource = $('#div_data_source_page_content_import');
            var $exportSqlForm = $('#div_import_field');


            $defDatasource.find('#sel_sourcedb_import').val($exportSqlForm.find('#txt_sdn_import').val());
            $defDatasource.find('#txt_tableName_import').val($exportSqlForm.find('#hid_tableName').val());

        },

        /**
         * @params method ImportAction中执行的Method方法名称
         */
        exportFieldCallback: function (object, method) {
            var $object = $(object);

            var columnNoVal = funs.get($object.parents('tr:eq(0)'), columnNo);

            var href = DWZ.contextPath + '/dde/importOpt!' + method + '.do?columnNo=' + columnNoVal;

            funs.set($object, 'href', href);
        },

        exportFieldInit: function (index) {
            if ('undefined' == typeof index || '' == $.trim(index)) {
                return;
            }

            var object = funs.getField($fieldFormList, 'field', index);

            var $input = $exportFieldForm.find(':text, textarea');
            $.each($input, function (i, input) {
                var $object = $(input);
                var name = funs.get($object, 'name');
                $object.val(object[name]);

            });

            var isNull = object['isNull'];
            if (1 == isNull) {
                $exportFieldForm.find('#chk_isNull').attr('checked', true);
            }

            var ipVal = object['isPk'];
            if (1 == ipVal) {
                $exportFieldForm.find('#chk_ispk').attr('checked', true);
            }
        },

        exportTriggerInit: function (index) {
            if ('undefined' == typeof index || '' == $.trim(index)) {
                return;
            }

            var object = funs.getField($triggerFormList, 'trigger', index);

            var $input = $exportTriggerForm.find(':text, textarea');
            $.each($input, function (i, input) {
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


var D = function (object) {
    console.dir(object);
};
