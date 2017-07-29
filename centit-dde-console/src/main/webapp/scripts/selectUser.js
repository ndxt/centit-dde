/**
 * 用户信息组件
 **/

var userInfo = (function() {
    //初始化页面中的userName值，此方法暂时不用，已有其他方法替代，计划删除
    function initializeUserName(options) {
        var codeValue = $("#userCode").val() || "";
        var codeList = $.map(options.list, function(n, i) {
            return n.usercode;
        });

        var indexOfList = $.inArray(codeValue, codeList);

        var userNameObject = $(options.userName);
        userNameObject.text((indexOfList === -1) ? "" : options.list[indexOfList].username);
    }

    return {
        /**
         * 自动提示选择用户，可以输入userCode、用户中文名称、用户拼音进行自动筛选
         * @param options
         */
        choose:function(obj, options) {
            options = $.extend({userName:$('#userName')}, options);
            //  alert("选择用户 ");
            var target = $(obj);//options.object;
            target.autocomplete(options.dataList, {
                minChars: 0,
                width: 250,
                matchContains: true,
                autoFill: false,
                matchCase :true,
                scroll:50,
                formatItem: function(row, i, max) {
                    return "<span style='width:70px;text-align: left'><b>" +
                        row.usercode + "</b></span> [<b>" + row.username + "</b>]"
                },
                formatMatch: function(row, i, max) {   // 按照下面三个属性匹配
                    return row.username + " " + row.usercode + " " + " " + row.pinyin;
                },
                formatResult: function(row) {
                    return row.usercode;
                }
            }).result(function(e, row) {  //选择后处理
            	if($("#userName")){
            		$("#userName").val(row.username);
            	}
                 if($("#getName")){
                	 $("#getName").html(row.username);
                 }
                });
            /*target.change(valueChanged);
            //   target.blur(valueChanged);
            function valueChanged() {
                var v = target.val();
                var n = $.pluck(options.dataList, 'usercode');
                // alert(v+"in "+n + "\n"+$.inArray(v,n));
                var index = $.inArray(v, n);
                if (index == -1) {
                    options.userName.html("");
                } else {
                    options.userName.html(options.dataList[index].username);
                }

                "" == $.trim(v) ? options.userName.html("") : $.noop();
            }*/
        }
    }

})();

