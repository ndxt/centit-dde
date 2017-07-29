/**
 * 通讯录组件，依赖jquery.js,jquery.colorbox.js及colorbox.css文件
 */
var addressBook = (function() {
    function showAddressBookDetail(id, options) {
        var appPath = centit.ajax.getAppPath();
        var url ='/'+appPath+'/sys/addressBook!load.do?addrbookid=' + id;
        options = $.extend({url:url,labelFields:undefined,nullReplace:'未设置',displayNull:false}, options);
        var labelFields = options.labelFields || addressBookFields;
        id = id || undefined;
        var filteredField = options.displayField;// ['representation','bodytype','addrbookid','homepage'];

        $.post(options.url, {}, function(data) {
            var result = wrapData(data, labelFields, {displayNull:options.displayNull,nullReplace:options.nullReplace,displayField:filteredField});
            $.colorbox({ open:true,  title:'<span class="cboxTitle">' + data.representation + '的通讯录</span>',
                opacity:0.4,html:'<div style="width:100%">' + result + '</div>'});
        });
    }

    function wrapData(d, label, options) {
        var items = '';
        var count = 0;
        var filteredFields = displayFileds();//过滤指定不显示的字段

        $.each(d, function(i, e) {
            items += oneItemData(filteredFields[i], e, options);
        });

        function displayFileds() {
            var onlyDisplay = options.displayField;
            var result = {};
            if (onlyDisplay && onlyDisplay.length != 0) {
                $.map(label, function(n, i) {
                    //  alert("i="+i+",n="+n+",onlyDisplay="+onlyDisplay+",inArray="+$.inArray(i,onlyDisplay));
                    if ($.inArray(i, onlyDisplay) != -1) {
                        result[i] = n;
                    }
                });
                return result;
            } else {
                return label;
            }
        }

        function oneItemData(label, item, options) {
            var settings = $.extend({displayNull:false,nullReplace:'--'}, options);
            if ((!item && !settings.displayNull) || !label) {
                return '';
            } else {
                var result = '';
                count++;
                (!item) ? result = settings.nullReplace : result = item;
                var row1 = '<div style="clear:both">';
                var row2 = '</div>';

                var one = '<span class="cboxItem" >' + label + ":" + result + '</span>';
                return  (count % 3 == 1) ? row1 + one : (count % 3 == 0) ? one + row2 + '\n' : one;
            }
        }

        return items;
    }

    var addressBookFields = {"addrbookid":"通讯主体ID",
        "bodytype":"通讯主体类别","bodycode":"通讯主体编号","representation":"表示为","unitname":"单位",
        "deptname":"部门","rankname":"职务","email":"电子邮件1","email2":"电子邮件2","email3":"电子邮件3",
        "homepage":"网页地址","qq":"QQ","msn":"MSN","wangwang":"旺旺","buzphone":"商务电话","buzphone2":"商务电话2",
        "buzfax":"商务传真","assiphone":"助理电话","callbacphone":"回电话","carphone":"车载电话","unitphone":"单位电话",
        "homephone":"住宅电话","homephone2":"住宅2电话","homephone3":"住宅3电话","homefax":"住宅传真",
        "mobilephone":"移动电话","mobilephone2":"移动电话2","mobilephone3":"无线电话3","unitzip":"单位邮编",
        "unitprovince":"单位省","unitcity":"单位市","unitdistrict":"单位区","unitstreet":"单位街道",
        "unitaddress":"单位地址","homezip":"住宅邮编","homeprovince":"住宅省","homecity":"住宅市",
        "homedistrict":"住宅区","homestreet":"住宅街道","homeaddress":"住宅地址","home2zip":"住宅邮编2",
        "home2province":"住宅省2","home2city":"住宅市2","home2district":"住宅区2","home2street":"住宅街道2",
        "home2address":"住宅地址2","inuseaddress":"常用通讯地址","searchstring":"快速查询串","memo":"便签"};
    return {
        showDetail:function(id, options) {
            showAddressBookDetail(id, options);
        }
    }
})();

