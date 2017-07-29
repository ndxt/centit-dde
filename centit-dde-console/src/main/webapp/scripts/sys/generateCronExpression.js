var GenerateCronExpression = function (params) {
    this.run = function () {
        fun.init();
    };

    var fun = {
        init: function () {
            fun.getDateData($('#sel_hour'), 24);
            fun.getDateData($('#sel_minute'), 60);

            fun.getWeek($('#td_week'));

            fun.getInitCron();

            event.bindEvent();

        },

        getInitCron: function () {
            var cron = $.trim(params.cronExpression);
            var cronArray = cron.split(' ');
//debugger;
            if (6 >= cronArray.length) {
                for (var i = 6 - (6 - cronArray.length); i <= 6; i++) {
                    if (3 == i || 5 == i) {
                        cronArray.push('?');
                    } else if(i < 3) {
                        cronArray.push('0');
                    } /*else{
                        cronArray.push('*');
                    }*/
                }
            }

            $('#txt_cron').val(cronArray.join(' '));
        },

        getDateData: function ($select, max_date) {
            for (var i = 0; i < max_date; i++) {
                var x = i;
                if (x < 10) {
                    x = '0' + i;
                }
                $select.append("<option value='" + i + "'>" + x + "</option>");
            }
        },

        getWeek: function ($obj) {
            var week = {
                'Sun': '周日',
                'Mon': '周一',
                'Tues': '周二',
                'Wed': '周三',
                'Thur': '周四',
                'Fri': '周五',
                'Sat': '周六'
            };

            for (var w in week) {
                $obj.append("<input type = 'checkbox' value = '" + w + "' />" + week[w]);
            }
        },

        getIndex: function () {
            return $('#ul_tabs>li.selected').index();
        },

        getSelectedHour: function () {
            if('checked' == $('#chk_first_run_hour').attr('checked')) {
                return $('#sel_hour').val();
            }

            return '*';
        },

        getSelectMinute: function () {
            if('checked' == $('#chk_first_run_minute').attr('checked')) {
                return $('#sel_minute').val();
            }

            return '*';
        },

        getFirstRunHour: function () {
            return 'checked' == $('#chk_first_run_hour').attr('checked');
        },

        getFirstRunMinute: function () {
            return 'checked' == $('#chk_first_run_minute').attr('checked');
        },

        getCheckedWeek: function () {
            return $('#td_week').find('input:checkbox').filter(function () {
                return 'checked' == $(this).attr('checked');
            });
        },

        getInputValTrim: function (id) {
            return $.trim($('#' + id).val());
        },

        setArrayVal: function (index, val) {
            $('#ul_tabs').data('cronArray').splice(index, 1, val || '*');
        },

        getCron: function () {

            var cron = fun.getInputValTrim('txt_cron');
            var cronArray = cron.split(' ');

            //将cronArray设置于页面缓存
            $('#ul_tabs').data('cronArray', cronArray);


            var hour = fun.getSelectedHour();
            var minute = fun.getSelectMinute();

            //debugger;

            //不关注秒，直接设置'0'
            fun.setArrayVal(0, '0');


            //分钟
            var txtMinutes = fun.getInputValTrim('txtMinutes');
            if ('' == txtMinutes) {
                fun.setArrayVal(1, minute);
            } else {
                fun.setArrayVal(1, fun.getFirstRunMinute() ? minute + "/" + txtMinutes : '*/' + txtMinutes);
            }

            //小时
            var txtHour = fun.getInputValTrim('txtHourly');
            var txtHourBegin=fun.getInputValTrim('txtHour_begin');
            var txtHourEnd=fun.getInputValTrim('txtHour_end');
            if ('' == txtHour) {
                fun.setArrayVal(2, hour);
            } else {
                fun.setArrayVal(2, fun.getFirstRunHour() ? hour + "/" + txtHour : '*/' + txtHour);
            }
            if (''!=txtHourBegin && ''!=txtHourBegin){
              if ('' == txtHour){
            	  fun.setArrayVal(2, txtHourBegin+"-"+txtHourEnd);
              }
              else{
            	  fun.setArrayVal(2, txtHourBegin+"-"+txtHourEnd+ "/" + txtHour);
            	  }
            }

            //天
            var radDay = $('#tabDay').find(':radio:checked').val();

            if (('undefined' == typeof radDay) || -1 == radDay) {
                fun.setArrayVal(3, '?');
            } else {
                if (0 == radDay) {
                    //debugger;
                    var txtDay = fun.getInputValTrim('txtDaily');
                    var txtDailyStr0 = fun.getInputValTrim('txtDaily_str0');
                    if ('' == txtDay && '' == txtDailyStr0) {
                        cronArray.splice(3, 1, '?');
                    } else if ('' == txtDaily_str0) {
                        cronArray.splice(3, 1, 1 + '/' + txtDay);
                    } else {
                        fun.setArrayVal(3, txtDailyStr0 + '/' + txtDay);
                    }
                } else if (1 == radDay) {
                    var txtDailyStr = fun.getInputValTrim('txtDaily_str');
                    var txtDailyEnd = fun.getInputValTrim('txtDaily_end');
                    if ('' == txtDailyStr || '' == txtDailyEnd) {
                        cronArray.splice(3, 1, '?');
                    } else {
                        cronArray.splice(3, 1, txtDailyStr + '-' + txtDailyEnd);
                    }
                } else if (2 == radDay) {
                    cronArray.splice(3, 1, 'L');
                } else if (3 == radDay) {
                    cronArray.splice(3, 1, 'LW');
                } else if (4 == radDay) {
                    var txtDailyLast = fun.getInputValTrim('txtDaily_last');
                    if ('' == txtDailyLast) {
                        fun.setArrayVal(3, '?');
                    } else {
                        fun.setArrayVal(3, txtDailyLast + "L");
                    }
                }
            }

            //月
            var radMonth = $('#tabMonth :radio:checked').val();
            if (('undefined' == typeof radMonth) || -1 == radMonth) {
                fun.setArrayVal(4);
            } else {
                if (0 == radMonth) {
                    var txtChoiceMonthStr0 = fun.getInputValTrim('choiceMonth_str0');
                    var txtChoiceMonth = fun.getInputValTrim('choiceMonth');

                    if ('' == txtChoiceMonthStr0 || '' == txtChoiceMonth) {
                        fun.setArrayVal(4);
                    } else {
                        fun.setArrayVal(4, txtChoiceMonthStr0 + '/' + txtChoiceMonth);
                    }
                } else if (1 == radMonth) {
                    var txtChoiceMonthStr = fun.getInputValTrim('choiceMonth_str');
                    var txtChoiceMonthEnd = fun.getInputValTrim('choiceMonth_end');

                    if ('' == txtChoiceMonthStr && '' == txtChoiceMonthEnd) {
                        fun.setArrayVal(4);
                    } else if ('' == txtChoiceMonthStr) {
                        fun.setArrayVal(4, '1-' + txtChoiceMonthEnd);
                    } else if ('' == txtChoiceMonthEnd) {
                        fun.setArrayVal(4, txtChoiceMonthStr + '-12');
                    } else {
                        fun.setArrayVal(4, txtChoiceMonthStr + '-' + txtChoiceMonthEnd);
                    }
                }

            }

            //星期

            var radWeek = $('#tabWeek :radio:checked').val();
            if (('undefined' == typeof radWeek) || -1 == radWeek) {
                fun.setArrayVal(5);
            } else {
                if (0 == radWeek) {
                    var checkedWeek = [];

                    $.each(fun.getCheckedWeek(), function (index, check) {
                        checkedWeek.push($(check).val());
                    });

                    if (0 < checkedWeek.length) {
                        fun.setArrayVal(5, checkedWeek.join(','))
                    } else {
                        fun.setArrayVal(5);
                    }
                } else if (1 == radWeek) {
                    var txtWeekStr = fun.getInputValTrim('txtWeek_str');
                    var txtWeekEnd = fun.getInputValTrim('txtWeek_end');
                    if ('' == txtWeekStr && '' == txtWeekEnd) {
                        fun.setArrayVal(5);
                    } else if ('' == txtWeekStr) {
                        fun.setArrayVal(5, '1-' + txtWeekEnd);
                    } else if ('' == txtWeekEnd) {
                        fun.setArrayVal(5, txtWeekStr + '-7');
                    } else {
                        fun.setArrayVal(5, txtWeekStr + '-' + txtWeekEnd);
                    }
                } else if (2 == radWeek) {
                    fun.setArrayVal(5, 6);
                } else if (3 == radWeek) {
                    fun.setArrayVal(5, '6L');
                } else if (4 == radWeek) {
                    var txtWeekHow = fun.getInputValTrim('txtWeek_how');
                    var txtWeekDay = fun.getInputValTrim('txtWeek_day');
                    if ('' == txtWeekHow || '' == txtWeekDay) {
                        fun.setArrayVal(5);
                    } else {
                        fun.setArrayVal(5, txtWeekDay + '#' + txtWeekHow);
                    }
                }
            }


            $('#txt_cron').val($('#ul_tabs').data('cronArray').join(' '));
        }

    };

    var event = {
        bindEvent: function () {
            for (var e in event) {
                if ('bindEvent' != e && $.isFunction(event[e])) {
                    event[e]();
                }
            }
        },

        bindGenerateCron: function () {
            $('#btnGenerate').click(function () {
                fun.getCron();
            });
        },

        bindCallback : function(){
            $('#btn_callback').click(function () {
                params.cronExpressionCallback.val($('#txt_cron').val());
                $('#btn_close').click();
            });
        }


    };
};

