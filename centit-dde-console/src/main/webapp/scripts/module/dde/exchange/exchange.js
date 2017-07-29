function ExportSql() {
    //var $defDatasource = $('#div_data_source_page_content');

    /**
     * 隐藏域提交表单
     * @type {*|jQuery|HTMLElement}
     */
    var $mainExchangeMapinfo = $('#frm_main_exchange_mapinfo');

    var $exportSqlForm = $('#div_export_field');
    var $exchangeFieldForm = $('#div_exchange_field_form');

    //源数据源Form页面
    var $exchangeSourceDsForm = $('#div_exchange_sourceds_form');

    //目标数据源Form页面
    var $exchangeDestDsForm = $('#div_exchange_destds_form');

    // 新增触发器Form页面
    var $exchangeTriggerForm = $('#div_exchange_trigger_form');

    // 添加数据源按钮
    var $btnSourceDs = $('#btn_source_ds');
    var $btnDestDs = $('#btn_dest_ds');

    // 添加字段按钮
    var $btnExchangeField = $('#btn_exchange_field');

    // 保存query sql 隐藏域
    var $querysql = $('#hid_querysql');

    //目标字段查询语句
    var $queryDestSql = $('#hid_dest_querysql');

    // ExportField 表格
    var $tbodySourceField = $('#tbody_source_field');
    var $tbodyDestField = $('#tbody_dest_field');

    // Field 隐藏Form
    var $fieldFormList = $('#hid_field_form_list');

    var defDsKey = 'DEF_DS_KEY';
    var columnNo = 'columnNo';

    // 添加触发器按钮
    var $btnExchangeTrigger = $('#btn_exchange_trigger');

    // Trigger 列表
    var $tbodyTrigger = $('#tbody_export_trigger');

    // 触发器 隐藏Form
    var $triggerFormList = $('#hid_trigger_form_list');

    // 排序表格列
    var $tbodyIndex = $('#tbody_index');


    //centitui.frag.xml中模板
    var trExFieldSourceTemplate = 'TrExchangeFieldSourceTemplate';
    var trExFieldDestTemplate = 'TrExchangeFieldDestTemplate';

    var exchangeSourceField = 'ExchangeSourceField';
    var exchangeDestField = 'ExchangeDestField';

    var trExchangeTriggerTemplate = 'TrExchangeTriggerTemplate';
    var exchangeTrigger = 'ExchangeTrigger';


    var $txtFieldName = $('#txt_field_name_export');
    var $txaFieldSentence = $('#txa_field_sentence_export');


    var fieldSource = 'fieldSource';
    var fieldDest = 'fieldDest';
    var trigger = 'trigger';


    var source = 'source';
    var dest = 'dest';


    var $txtFieldName = $('#txt_field_name', $exchangeFieldForm);
    var $txaFieldSentence = $('#txa_field_sentence', $exchangeFieldForm);

    this.init = function () {

        for (var e in binds) {
           binds[e]();
        	            
        }
    };
    this.bind = function (method) {
        binds[method]();
    };
    var binds = {
    		
        bindSourceDs: function () {
            $btnSourceDs.click(function () {
            	
                funs.exportSqlComplete($exchangeSourceDsForm);

                if (funs.isNull($('#txt_sourceTablename', $exchangeSourceDsForm).val())) {
                    funs.alert('未填写源表名');
                    return;
                }

                var querysql = $.trim($querysql.val());
                if (funs.isNull(querysql)) {
                    var $sourctTable = $('#txt_exchangemapinfo_st', $exportSqlForm);
                    $querysql.val('SELECT * FROM ' + $sourctTable.val());
                }

                funs.updateExportSql(funs.executeCallback, {
                    'querysql': $.trim($querysql.val()),
                    'datasource': $('#txt_exchangemapinfo_sds').val(),
                    'field': fieldSource,
                    'setTableText': funs.setSourceTableText
                });
            });
        },
        bindDestDs: function () {
            $btnDestDs.click(function () {
                funs.exportSqlComplete($exchangeDestDsForm);

                var $destTablename = $('#txt_exchangemapinfo_dt', $exportSqlForm);

                if (funs.isNull($('#txt_destTablename', $exchangeDestDsForm).val())) {
                    funs.alert('未填写目标表名');
                    return;
                }

                var querysql = funs.val($('#txt_dest_querySql', $exchangeDestDsForm));
                if (funs.isNull(querysql)) {
                    var tableName = funs.val($destTablename);
                    var $selDest = $('#sel_destds', $exchangeDestDsForm);
                    //根据目标数据库类型生成查询单条数据的语句
                    var dbType = PUBLIC.getDestDbType($selDest);
                    querysql = PUBLIC.mergeDestSql('*', tableName, dbType);
                }
                $queryDestSql.val(querysql);

                funs.updateExportSql(funs.executeCallback, {
                    'querysql': querysql,
                    'datasource': $('#txt_exchangemapinfo_dds').val(),
                    'field': fieldDest,
                    'setTableText': funs.setDestTableText
                });
            });
        },

        bindExportField: function () {
            $btnExchangeField.click(function () {
                //常量
                var $txtdestFieldDefault = $('#txt_destFieldDefault');

                if (funs.isNull($txtFieldName.val()) && funs.isNull($txtdestFieldDefault.val())) {
                    funs.alert('源字段名或常量二者必选其一');
                    return;
                }


                //目标字段
                var $txtdestFieldName = $('#txt_destFieldName');
                if (!funs.isNull($txtdestFieldDefault.val()) && funs.isNull($txtdestFieldName.val())) {
                    funs.alert('常量和目标字段必须同时不为空');
                    return;
                }

                if (funs.isNull($querysql.val())) {
                    funs.alert('先配置数据源');
                    return;
                }


                //拼接描述
                var $sourceFieldSentence = $('#txt_sourceFieldSentence');
                var sourceFieldSentence = funs.val($sourceFieldSentence);
                if (!funs.isNull($txtFieldName.val())) {
                    var fieldName = funs.val($txtFieldName);

                    if (!new RegExp(fieldName + '$').test(sourceFieldSentence)) {
                        if ('' != sourceFieldSentence) {
                            fieldName = sourceFieldSentence + ' as ' + fieldName;
                        }
                    }
                }
                $sourceFieldSentence.val(fieldName);

                var sourceIndex = $('#hid_exchange_field_form_columnno', $exchangeFieldForm).val();
                var destIndex = sourceIndex;
                if (funs.isNull(sourceIndex)) {
                    sourceIndex = funs.getFormFieldMaxIndex(fieldSource, source);
                    destIndex = funs.getFormFieldMaxIndex(fieldSource, dest);
                }
                var objArray = $exchangeFieldForm.find(':input, textarea').serializeArray();

                //获取表单数据
                var object = [];

                for (var i = 0; i < objArray.length; i++) {
                    object[objArray[i].name] = objArray[i].value;
                }
                object[columnNo] = sourceIndex;

                //更新表格
                funs.update(fieldSource, object, sourceIndex, funs.setSourceTableText);
                funs.update(fieldDest, object, destIndex, funs.setDestTableText);

                //更新隐藏域中form值
                funs.setField(fieldSource, sourceIndex, object, funs.updateFieldFormList, source);
                funs.setField(fieldDest, destIndex, object, funs.updateFieldFormList, dest);


                //更新ExportSql 中 querysql字段值
                var querysql = $querysql.val();

                funs.updateExportSqlByField(querysql, fieldSource, funs.executeCallback);
            });
        },

        bindFieldName: function () {
            $txtFieldName.bind('keyup', function () {
                var $this = $(this);
                var sentence = $.trim($txaFieldSentence.val());

                if (funs.isNull(sentence)) {
                    $txaFieldSentence.val($this.val());
                }

                var fs = sentence.split(/\s+/);

                if (1 == fs.length) {
                    $txaFieldSentence.val(fs[0] + ' ' + $this.val());
                } else {
                    fs[fs.length - 1] = $this.val();

                    $txaFieldSentence.val(fs.join(' '));
                }

                $this.val(funs.toUpperCase($this.val()));
                $txaFieldSentence.val(funs.toUpperCase($txaFieldSentence.val()));
            });
        },

        bindFieldType: function () {
            $('#txt_field_type').bind('keyup', function () {
                var $this = $(this);
                $this.val(funs.toUpperCase($this.val()));
            });
        },

        bindFieldSentence: function () {
            $txaFieldSentence.bind('keyup', function () {
                var $this = $(this);

                var sentence = $.trim($this.val());
                if (funs.isNull(sentence)) {
                    $txtFieldName.val($this.val());

                    return;
                }

                var fs = sentence.split(/\s+/);
                if (1 == fs.length) {
                    $txtFieldName.val(fs[0]);
                } else {
                    $txtFieldName.val(fs[fs.length - 1]);
                }


                $this.val(funs.toUpperCase($this.val()));
                $txtFieldName.val(funs.toUpperCase($txtFieldName.val()));
            });
        },

        bindTrigger: function () {
            $btnExchangeTrigger.click(function () {
                var index = $exchangeTriggerForm.find('#hid_exchange_trigger_form_triggerId').val();

                if (funs.isNull(index)) {
                    index = funs.getFormFieldMaxIndex(trigger);
                }
                //获取表单数据
                var object = funs.getFormToObject($exchangeTriggerForm, index);
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
        bindDelete: function () {
            for (var key in exportBind) {
                exportBind[key].table.find('a.delete').die('click').live('click', function () {
                    var $tr = $(this).parent().parent();
                    var index = funs.get($tr, columnNo);
                    funs.deleteFieldFun($tr, index);
                });
            }
        },

        bindDeleteLine: function () {
            $('a.delete').live('click', function () {
                var index = -1;
                var $trs = $tbodyIndex.find('tr');

                for (var i = 0; i < $trs.length; i++) {
                    if ($($trs[i]).hasClass('selected')) {
                        index = i;
                    }
                }

                if (-1 == index) {
                    return;
                }

                var $sourceTrs = $('tr', $tbodySourceField);
                var $destTrs = $('tr', $tbodyDestField);
                if (index <= $sourceTrs.length) {
                    var $tr = $($sourceTrs[index]);

                    funs.deleteFieldFun($tr, index);
                }


                if (index <= $destTrs.length) {
                    var $tr = $($destTrs[index]);

                    funs.deleteFieldFun($tr, index);
                }

            });
        },

        bindToUpperCase: function () {
            var keys = [
                $('#txt_sourceTablename', $exchangeSourceDsForm),
                $('#txt_destTablename', $exchangeDestDsForm),
                $('#txt_field_name', $exchangeFieldForm),
                $(':input[name="sourceFieldType"]', $exchangeFieldForm),
                $(':input[name="destFieldName"]', $exchangeFieldForm),
                $(':input[name="destFieldType"]', $exchangeFieldForm)
            ];
            for (var i = 0; i < keys.length; i++) {
                $(keys[i]).bind('keyup', function () {
                    $(this).val(funs.toUpperCase($(this).val()));
                });
            }
        },
        bindImportField: function () {
            $('#exchange_file_upload').uploadify({
                'buttonText': '导入字段配置',
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
                            sourceFieldName: data[key]['sourceFieldName'],
                            sourceFieldType: data[key]['sourceFieldType'],
                            destFieldName: data[key]['destFieldName'],
                            destFieldType: data[key]['destFieldType']
                        });
                        funs.update(fieldSource, objList[len], len, funs.setSourceTableText);
                        funs.update(fieldDest, objList[len], len, funs.setDestTableText);

                        //更新隐藏域中form值
                        funs.setField(fieldSource, len, objList[len], funs.updateFieldFormList, source);
                        funs.setField(fieldDest, len, objList[len], funs.updateFieldFormList, dest);

                        len++;
                    }
                    
                    

                    
                }
            });
        },

        initSortDrag: function (type) {
            // ;
            var options = {
                cursor: 'move', // selector 的鼠标手势
                sortBoxs: 'tbody.sortDrag', // 拖动排序项父容器
                replace: true, // 2个sortBox之间拖动替换
                items: '> tr', // 拖动排序项选择器
                selector: 'td:first:not(.notMove)', // 拖动排序项用于拖动的子元素的选择器，为空时等于item
                zIndex: 1000,
                callback: funs.sortDragCallback
            };

            if (funs.isNull(type)) {
                for (var key in exportBind) {
                    options.callback = exportBind[key].callback;
                    exportBind[key].table.sortDrag(options);
                }
            } else {
                options.callback = exportBind[type].callback;
                exportBind[type].table.sortDrag(options);
            }
        },

        /**
         * 初始化时绑定数据库数据类型
         */
        initCacheDataType: function () {
            PUBLIC.initCacheDataType($mainExchangeMapinfo);
        },

        initAnalysisDataType: function () {
            funs.executeAnalysisDataType();
        }
    };

    var funs = {
        exportSqlComplete: function ($sdForm) {
            // 保存数据源时将数据存在页面的hidden中
            var $objList = $sdForm.find('textarea,select,:input');

            $.each($objList, function (index, object) {
                var $object = $(object);
                var name = $object.attr('name');
                $exportSqlForm.find(':input[name="' + name + '"]').val($object.val());
            });
        },

        /**
         * 表格拖动列排序回调方法
         * @param $srcBox
         * @param $destBox
         */
        sortDragCallback: function ($srcBox, $destBox) {
            var f = fieldSource;
            funs.splitQuerySql($querysql.val(), function (jsonResult) {
                funs.updateFieldIndex(f, null, null, source);

                var splitsql = jsonResult.splitsql;

                var fieldList = funs.getFieldFormList(f, source);

                var querysql = 'SELECT ';


                var fields = [];
                for (var field in fieldList) {
                    var val = '';
                    if(funs.isNull(fieldList[field].destFieldDefault)) {
                        var sfs = fieldList[field].sourceFieldSentence;
                        if(!funs.isNull(sfs) && (-1 != sfs.indexOf('as') || -1 != sfs.indexOf(' '))) {
                            val = sfs;
                        } else {
                            val = fieldList[field].sourceFieldName;
                        }
                    } else {
                        val = fieldList[field].destFieldDefault;
                    }

                    if(!funs.isNull(val)) {
                        fields.push(val);
                    }
                }

                querysql += (fields.join(', ') + ' FROM ' + splitsql[splitsql.length - 1]);

                $querysql.val(querysql);

                funs.executeAnalysisDataType();
            });
        },


        /**
         * 目标表格拖动列排序回调方法
         * @param $srcBox
         * @param $destBox
         */
        destSortDragCallback: function ($srcBox, $destBox) {
            funs.updateFieldIndex(fieldDest, null, null, dest);

            funs.generateDestQuery();
            funs.executeAnalysisDataType();
        },


        triggerSortDragCallback: function ($srcBox, $destBox) {
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
        save: function (type, objList, setTableText, index) {
            // 获取原先表格中的字段

            // 根据query sql解析的字段进行更新
            for (var i = 0; i < objList.length; i++) {
                var object = objList[i];

                var $tr = $(DWZ.frag[exportBind[type].ttemplate]);

                setTableText($tr, object);
                funs.set($tr, columnNo, object.columnNo);
                $tr.show();
                funs.trClass($tr, object.columnNo);
                exportBind[type].table.append($tr);
            }
            $('.sortDrag tr').each(function () {
                $(this).bind('click', function () {
                    $('.sortDrag tr.selected').removeClass('selected');
                    $(this).addClass('selected');
                });
            });
            exportBind[type].table.find('tr td').each(function () {
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
        update: function (type, object, index, setTableText) {
            var $tr = exportBind[type].table.find('tr[columnNo=' + index + ']');
            if (0 == $tr.size()) {

                funs.save(type, [object], setTableText, index);
            	
                return;
            }
            setTableText($tr, object);

            funs.set($tr, columnNo, object.columnNo);

        },

        /**
         * 更新querysql字段
         */
        updateExportSql: function (callbackfn, object) {
            // resolve sql
            //var querysql = querysql || $.trim($querysql.val());
            // 前一次val值
            //var beforeval = $querysql.attr('beforeval');

            // 未更新query sql 字段值
            //if (querysql == beforeval) {
            //return;
            //}

            funs.resolveQuerySql({
                querysql: object.querysql,
                sourcedb: object.datasource
            }, function (jsonResult) {
                if (undefined != jsonResult.message) {
                    funs.alert(jsonResult.message);
                    //return;
                }

                //获取原数据及最新数据
                var $fields = funs.getFieldFormList(object.field);
                var efList = jsonResult.efList;

                //将对应位置数据中的字段复制到后台返回的数据
                for (var i = 0; i < efList.length; i++) {
                    var ef = efList[i];
                    var fieldObj = $fields[i];

                    if (funs.isNull(fieldObj)) {
                        continue;
                    }

                    if (!funs.isNull(fieldObj.fieldType)) {
                        ef.fieldType = funs.isNull(fieldObj.fieldType);
                    }
                    if (!funs.isNull(fieldObj.fieldFormat)) {
                        ef.fieldFormat = fieldObj.fieldFormat;
                    }
                    if (!funs.isNull(fieldObj.fieldStoreType)) {
                        ef.fieldStoreType = fieldObj.fieldStoreType;
                    }
                    if (!funs.isNull(fieldObj.isPk)) {
                        ef.isPk = fieldObj.isPk;
                    }
                    if (!funs.isNull(fieldObj.isNull)) {
                        ef.isNull = fieldObj.isNull;
                    }
                }

                //通过服务器验证sql有效性后才做以下操作
                if (funs.isNull(jsonResult.simple)) {
                    //清空本地页面
                    funs.clean(object.field);

                    // 更新Field字段
                    funs.save(object.field, efList, object.setTableText);


                    //向隐藏Form中添加数据
                    funs.updateFieldFormList(object.field, efList, true);

                }

                if (undefined != callbackfn && $.isFunction(callbackfn)) {
                    callbackfn();
                }
            });
        },


        /**
         * 更新Field或删除Field字段后，根据最新的数据更新QuerySql的值
         * @param callbackfn
         */
        updateExportSqlByField: function (querysql, type, callbackfn) {

            var objList = funs.getQuerySqlByField(type);
            //调整隐藏Form中排序顺序，objList需要按顺序重新排序
            var tempObj = [];
            for (var i = 0; i < objList.length; i++) {
                tempObj[objList[i].columnNo] = objList[i];
            }

            var tempObjList = [];
            for(var i = 0; i < objList.length; i++) {
                tempObjList.push(tempObj[i]);
            }
            objList = tempObjList;



            funs.splitQuerySql(querysql, function (jsonResult) {

                var splitsql = jsonResult.splitsql;

                var sqlField = [];
                for (var obj in objList) {
                    //常量优先，
                    //sql语句拼接
                    if (!funs.isNull(objList[obj].sourceFieldSentence)) {
                        sqlField.push(objList[obj].sourceFieldSentence);
                    }
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
        setSourceTableText: function ($tr, object) {
            var $tdArray = $tr.find('td');

            var fieldName = funs.isNull(object.fieldName) ? object.sourceFieldName : object.fieldName;

            funs.setText($tdArray.eq(0), fieldName);
            funs.set($tdArray.eq(0), 'title', fieldName);


            funs.setText($tdArray.eq(1), funs.isNull(object.fieldSentence) ? object.sourceFieldSentence : object.fieldSentence);
            funs.setText($tdArray.eq(2), funs.isNull(object.fieldType) ? object.sourceFieldType : object.fieldType);

        },

        setDestTableText: function ($tr, object) {
            var $tdArray = $tr.find('td');

            var fieldName = funs.isNull(object.fieldName) ? object.destFieldName : object.fieldName;
            funs.setText($tdArray.eq(0), fieldName);

            funs.set($tdArray.eq(0), 'title', fieldName);

            funs.setText($tdArray.eq(1), funs.isNull(object.fieldType) ? object.destFieldType : object.fieldType);

            if (!funs.isNull(object.isPk)) {
                funs.setText($tdArray.eq(2), '1' == object.isPk ? '是' : '否');
            } else {
                funs.setText($tdArray.eq(2), '否');
            }

            if (!funs.isNull(object.isNull)) {
                funs.setText($tdArray.eq(3), '1' == object.isNull ? '是' : '否');
            } else {
                funs.setText($tdArray.eq(3), '否');
            }

            if (!funs.isNull(object.destFieldDefault)) {
                funs.setText($tdArray.eq(4), object.destFieldDefault);
            } else {
                funs.setText($tdArray.eq(4), '');
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

            var triggerDatabase = '数据源';
            if ('D' == object.triggerDatabase) {
                triggerDatabase = '数据目标';
            }

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
            //
            funs.setText($tdArray.eq(1), triggerDatabase);
            funs.setText($tdArray.eq(2), triggerType);
            funs.setText($tdArray.eq(3), triggerTime);
            funs.setText($tdArray.eq(4), parseInt(object.index) + 1);
            funs.setText($tdArray.eq(5), 'T' == object.isprocedure ? '是' : '否');
        },


        updateFieldFormList: function (type, objList, remove) {
            if (remove) {
                if (fieldDest == type) {
                    exportBind[type].form.children(funs.getFieldType(dest)).remove();
                } else {
                    exportBind[type].form.children(funs.getFieldType(source)).remove();
                }
            }
            //替换排序
            for (var i = 0; i < objList.length; i++) {
                var object = objList[i];
                var exportFieldText = DWZ.frag[exportBind[type].ftemplate];

                exportFieldText = exportFieldText.replaceAll('{columnNo}', object.columnNo);

                var $ef = $(exportFieldText);

                for (var name in object) {
                    //if(fieldSource == type || fieldDest == type) {
                    //$ef.find(':hidden[name$="' + name + '"]').val(object[name]);
                    //} else {
                    //在通过sql语句添加时，查询数据库返回字段列表，匹配时首字母需要大写
                    //在本地通过Js添加字段时，需要首字母小写

                    //首字母大写
                    var nametemp = name.substring(0, 1).toUpperCase() + name.substring(1, name.length);

                    var $fieldObject = $ef.find(':hidden[name$="' + nametemp + '"]');
                    if ('undefined' == typeof $fieldObject || null == $fieldObject || 0 == $fieldObject.size()) {
                        $fieldObject = $ef.find(':hidden[name$="' + name + '"]')
                    }

                    $fieldObject.val(object[name]);
                    //}

                    //$ef.find(':hidden[name$="' + name + '"]').val(object[name]);

                }

                exportBind[type].form.append($ef);
            }

        },


        /**
         * 删除Field操作
         * @param index
         */
        deleteField: function (type, index) {
            //删除隐藏Form中数据
            var $field = funs.getFieldObject(type, index);
            $field.remove();

            var fieldType = undefined;
            if (fieldSource == type) {
                fieldType = source;
            } else if (fieldDest == type) {
                fieldType = dest;
            }

            //如果删除的非最后一行数据，更新 columnNo值
            funs.updateFieldIndex(type, null, null, fieldType);

            if (fieldSource != type) {
                funs.updateIndex();
                funs.close();

                return;
            }
            //更新ExportSql 中 querysql字段值
            var querysql = $querysql.val();

            funs.updateExportSqlByField(querysql, type, funs.executeCallback);
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
        setField: function (type, index, object, updateFieldFormList, fieldType) {
            var $object = exportBind[type].form;

            var li = 'li[columnNo=' + index + ']';
            if (fieldType) {
                li = 'li[columnNo=' + index + '][type=' + fieldType + ']';
            }
            var $field = $object.find(li);

            if (0 == $field.size()) {
                updateFieldFormList(type, [object]);

                return;
            }

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
        getField: function (type, index) {
            var $field = funs.getFieldObject(type, index);

            var $input = $field.find(':hidden');

            var object = [];
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
        getFieldObject: function (type, index) {
            var li = 'li[columnNo="' + index + '"]';
            if (fieldSource == type) {
                li += '[type="' + source + '"]';
            } else if (fieldDest == type) {
                li += '[type="' + dest + '"]';
            }


            return exportBind[type].form.find(li);
        },

        /**
         * 将Form元素收集至Object对象中
         * @param $object
         * @param index
         */
        getFormToObject: function ($object, index) {
            var objArray = $object.find(':input, textarea').serializeArray();

            //获取表单数据
            var object = [];

            for (var i = 0; i < objArray.length; i++) {
                object[objArray[i].name] = objArray[i].value;
            }
            object[columnNo] = index;


            return object;
        },

        /**
         * 所有FieldForm 数据
         */
        getFieldFormList: function (type, fieldType) {
            var fields = [];

            var li = funs.getFieldType(fieldType);

            var $fields = exportBind[type].form.find(li);

            $.each($fields, function (index, o) {
                fields.push(funs.getField(type, index));
            });

            return fields;
        },

        /**
         * 获取隐藏域中有多少个Field
         * @params type
         *
         */
        getFormFieldMaxIndex: function (type, fieldType) {
            var li = funs.getFieldType(fieldType);

            var index = exportBind[type].form.find(li).size();
            //return 0 == index ? -1 : index;
            return index;
        },

        /**
         * 更新索引，在有移动，删除Table中列的时候重建Index
         */
        updateFieldIndex: function (type, textfn, formfn, fieldType) {
            //重建Table展示功能 区的索引
            var newcol = "newcolumnno";


            var $trs = exportBind[type].table.find('tr');
            $.each($trs, function (i, tr) {
                var $this = $(tr);
                var index = funs.get($this, columnNo);

                funs.set($this, columnNo, i);

                var $sourceField = funs.getFieldObject(type, index);
                funs.set($sourceField, newcol, i);


                if (textfn) {
                    textfn($this, i);
                }

            });

            //重建FormField 索引
            var li = funs.getFieldType(fieldType);
            var $lis = exportBind[type].form.find(li);

            $.each($lis, function (i, li) {
                var $this = $(li);
                var newcolumnno = funs.get($this, newcol);

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


            $.each(exportBind[type].table.find('tr'), function (index, tr) {
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

        setData: function (key, data) {
            $sysObject.data(key, data);
        },

        getData: function (key) {
            return $sysObject.data(key);
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
        getQuerySqlByField: function (type) {
            var objList = exportBind[type].form.find('li[type="source"]>:input').serializeArray();

            //将隐藏域中的值构建Object对象
            var length = exportBind[type].form.find('li[type="source"]:eq(0)>:input').size();

            var values = [];
            for (var i = 0; i < objList.length; i++) {
                if (0 == i % length) {
                    var object = [];
                    for (var k = 0; k < length; k++) {
                        try {
                            var names = objList[i + k].name.split(/\./);
                            var name = names[names.length - 1];

                            object[name] = objList[i + k].value;

                            //获取在查询结果集中的位置
                            var index = objList[i + k].name.match(/\[(\d+)\]/)[1];

                            object[columnNo] = index;

                        } catch (ex) {
                            //console.debug('length = ' + objList.length + ' i = ' + i + ' k = ' + k);
                            //
                        }
                    }

                    values.push(object);
                }

            }

//
            return values;
        },


        deleteFieldFun: function ($tr, index) {
            //删除文本
            var type = funs.get($tr, 'type');
            $tr.remove();
            funs.deleteField(type, index);
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

        /**
         * 更新左边表格中的序列
         */
        updateIndex: function () {
            $tbodyIndex.children().remove();

            var sourceSize = $('tr', exportBind[fieldSource].table).size();
            var destSize = $('tr', exportBind[fieldDest].table).size();

            for (var i = 1; i <= (sourceSize >= destSize ? sourceSize : destSize); i++) {
                $tbodyIndex.append('<tr><td nowrap class="notMove" align="center">' + i + '</td></tr>');
            }

            $.each($('tr', $tbodyIndex), function (index, tr) {
                funs.trClass($(tr), index);
            });


            //$tbodyIndex.initUI(navTab.getCurrentPanel());
            $tbodyIndex.parent('table').cssTable();
        },

        /**
         * 通过目标字段生成目标的查询语句
         */
        generateDestQuery: function () {
            //初始化查询目标字段语句
            //目标表名
            var destTable = funs.val($('#txt_exchangemapinfo_dt', $exportSqlForm));

            var queryDestSql = funs.val($queryDestSql);
            if (funs.isNull(queryDestSql) && !funs.isNull(destTable)) {
                var objList = funs.getFieldFormList(fieldDest, dest);

                var fields = [];

                $.each(objList, function (i, object) {
                    fields.push(object.destFieldName);
                });

                queryDestSql = PUBLIC.mergeDestSql(fields.join(', '), destTable, PUBLIC.getDestDbType($('#sel_destds', $exchangeDestDsForm)));//'select ' + fields.join(', ') + ' from ' + destTable;

                $queryDestSql.val(queryDestSql);
            }

            $('#txt_dest_querySql', $exchangeDestDsForm).val(queryDestSql);
        },

        /**
         * 显示源和目标数据类型不匹配，并标注
         */
        executeAnalysisDataType: function () {
            var sources = funs.getFieldFormList(fieldSource, source);
            var dests = funs.getFieldFormList(fieldDest, dest);


            var s = PUBLIC.getArrayObjectProperty(sources, 'sourceFieldType');
            var d = PUBLIC.getArrayObjectProperty(dests, 'destFieldType');

            var noMatchFieldIndex = PUBLIC.listDataTypeNoMatchField($mainExchangeMapinfo, s, d);

            PUBLIC.identifyNoMatchField(noMatchFieldIndex, $tbodySourceField, $tbodyDestField);
        },

        /**
         *  操作执行完回调操作
         */
        executeCallback: function () {
            funs.updateIndex();
            funs.close();

            funs.executeAnalysisDataType();
        },

        getFieldType: function (fieldType) {
            if (fieldType) {
                return 'li[type="' + fieldType + '"]';
            }
            return 'li';
        },

        alert: function (message) {
            DWZ.ajaxDone({
                statusCode: DWZ.statusCode.error,
                message: message
            });
        },

        close: function () {
            try {
                $.pdialog.close($.pdialog.getCurrent());
            } catch (ex) {

            }
        },

        clean: function (type) {
            exportBind[type].table.children().remove();

            if (fieldDest == type) {
                exportBind[type].form.children(funs.getFieldType(dest)).remove();
            } else {
                exportBind[type].form.children(funs.getFieldType(source)).remove();
            }
        },

        isNull: function (val) {
            return 'undefined' == typeof val || null == val || '' == $.trim(val);
        },

        toUpperCase: function (val) {
            //if(!funs.isNull(val) && /[a-z]/.test(val)) {
            //return val.toUpperCase();
            //}

            return val;
        },

        val: function ($object) {
            return $.trim($object.val());
        }
    };


    // 元素的绑定，隐藏Form 展示表格
    var exportBind = {
        'fieldSource': {
            'form': $fieldFormList,
            'table': $tbodySourceField,
            'ttemplate': trExFieldSourceTemplate,
            'ftemplate': exchangeSourceField,
            'callback': funs.sortDragCallback
        },
        'fieldDest': {
            'form': $fieldFormList,
            'table': $tbodyDestField,
            'ttemplate': trExFieldDestTemplate,
            'ftemplate': exchangeDestField,
            'callback': funs.destSortDragCallback
        },
        'trigger': {
            'form': $triggerFormList,
            'table': $tbodyTrigger,
            'ttemplate': trExchangeTriggerTemplate,
            'ftemplate': exchangeTrigger,
            'callback': funs.triggerSortDragCallback
        }
    };

    this.pubfuns = {

        /**
         * 点击数据源定义，对弹出框内容进行初始化
         */
        defSourceDsInit: function () {
            var $exportSqlForm = $('#div_export_field');

            $('#txt_sourceTablename', $exchangeSourceDsForm).val($('#txt_exchangemapinfo_st', $exportSqlForm).val());
            $('#text_querySql', $exchangeSourceDsForm).val($('#hid_querysql', $exportSqlForm).val());
            $('#sel_sourceds', $exchangeSourceDsForm).val($('#txt_exchangemapinfo_sds', $exportSqlForm).val());
        },

        /**
         * 点击目标定义，对弹出框内容进行初始化
         */
        defDestDsInit: function () {
            var $exportSqlForm = $('#div_export_field');
            //目标表名
            var destTable = funs.val($('#txt_exchangemapinfo_dt', $exportSqlForm));

            $('#txt_destTablename', $exchangeDestDsForm).val(destTable);

            //

            $('#sel_destds', $exchangeDestDsForm).val($('#txt_exchangemapinfo_dds', $exportSqlForm).val());
//

            funs.generateDestQuery();
        },

        exportSqlInit: function () {
            // 定时数据源时初始化
            var $defDatasource = $('#div_data_source_page_content');
            var $exportSqlForm = $('#div_export_field');

            var $objList = $defDatasource.find('textarea');

            $.each($objList, function (index, object) {
                var $object = $(object);

                var name = $object.attr('name');

                $object.val($exportSqlForm.find(':hidden[name="' + name + '"]').val());
            });

            var $name = $defDatasource.find('select');

            $defDatasource.find('option[value=' + $exportSqlForm.find(':hidden[name=sourceDatabaseName]').val() + ']').prop('selected', true);
        },

        exportFieldCallback: function (object, method) {
            var $object = $(object);

            var columnNoVal = funs.get($object.parents('tr:eq(0)'), columnNo);

            var href = DWZ.contextPath + '/dde/exchangeMapInfo!' + method + '.do?columnNo=' + columnNoVal;

            funs.set($object, 'href', href);
        },

        exportFieldInit: function (index) {

            if ('undefined' == typeof index || '' == $.trim(index)) {
                return;
            }
            var sourceObject = funs.getField(fieldSource, index);
            var destObject = funs.getField(fieldDest, index);

            var object = [];
            $.extend(object, sourceObject, destObject);

            var $input = $exchangeFieldForm.find(':text, textarea');
            $.each($input, function (i, input) {
                var $object = $(input);
                var name = funs.get($object, 'name');
                $object.val(object[name]);

            });
            //var fstVal = object['fieldStoreType'];
            //$exchangeFieldForm.find(':radio[value=' + fstVal + ']').attr('checked', true);

            var ipVal = object['isPk'];
            if (1 == ipVal) {
                $exchangeFieldForm.find('#chk_ispk').attr('checked', true);
            }
            ipVal = object['isNull'];
            if (1 == ipVal) {
                $exchangeFieldForm.find('#chk_isNull').attr('checked', true);
            }

            //将描述与字段匹配
            var $fn = $('#txt_field_name', $exchangeFieldForm);
            var $sfs = $('#txt_sourceFieldSentence', $exchangeFieldForm);

            if ($fn.val() == $sfs.val()) {
                return;
            }

            var arr = $sfs.val().split(/\s+/);
            if (1 == arr.length) {
                return;
            }

            $sfs.val(arr[0]);

        },

        exportTriggerInit: function (index) {

            //在页面中显示可引用的字段

            var fieldFormList = funs.getFieldFormList(fieldSource, source);
            var fields = [];

            for (var key in fieldFormList) {
                fields.push('<span style="background: #A09F9A; margin-right: 5px; padding: 1px; width: 280px; float: left;">' + fieldFormList[key].sourceFieldName + '</span>');
            }
            //
            $('#exchange_trigger_refer', $exchangeTriggerForm).html(fields.join(''));

            //console.debug(fields);


            if ('undefined' == typeof index || '' == $.trim(index)) {
                return;
            }
            var object = funs.getField(trigger, index);

            var $input = $exchangeTriggerForm.find(':text, textarea');
            $.each($input, function (i, input) {
                var $object = $(input);
                var name = funs.get($object, 'name');
                $object.val(object[name]);

            });
            $exchangeTriggerForm.find(':radio[value="' + object['triggerDatabase'] + '"]').prop('checked', true);
            $exchangeTriggerForm.find(':radio[value="' + object['triggerType'] + '"]').prop('checked', true);
            $exchangeTriggerForm.find(':radio[value="' + object['triggerTime'] + '"]').prop('checked', true);
            $exchangeTriggerForm.find(':checkbox[value="' + object['isprocedure'] + '"]').prop('checked', true);


        },

        updateIndex: funs.updateIndex
    };
}

