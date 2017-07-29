var PUBLIC = {
    /**
     * 设置缓存
     * @param $object
     * @param key
     * @param value
     */
    setCache: function ($object, key, value) {
        $object.data(key, value);
    },

    /**
     * 获取缓存
     * @param $object
     * @param key
     * @returns {*}
     */
    getCache: function ($object, key) {
        return $object.data(key);
    },

    /**
     * 获取数组中对象属性
     * @param array
     * @param property
     * @returns {Array}
     */
    getArrayObjectProperty: function (array, property) {
        var prop = [];
        for (var i = 0; i < array.length; i++) {
            prop.push(array[i][property]);
        }

        return prop;
    },

    /**
     * 初始化时绑定数据库数据类型
     */
    initCacheDataType: function ($object, callback) {
        PUBLIC.setCache($object, 'data_type', DWZ.CACHE.DATA_TYPE);
    },


    //获取目标数据库
    getDestDbType: function ($selDest) {
        var dbType = $selDest.find("option:selected").attr('db_type');

        return dbType;
    },


    mergeDestSql: function (field, tableName, dbType) {
        var querysql = '';
        if ('1' == dbType) {
            querysql = 'SELECT top 1 ' + field + ' FROM ' + tableName;
        } else if ('2' == dbType) {
            querysql = 'SELECT ' + field + ' FROM ' + tableName + ' where rownum = 1';
        } else if ('3' == dbType) {
            querysql = 'SELECT ' + field + ' FROM ' + tableName + ' fetch first 1 rows only';
        } else if ('5' == dbType) {
            querysql = 'SELECT ' + field + ' FROM ' + tableName + ' limit 0,1';
        } else {
            querysql = 'SELECT ' + field + ' FROM ' + tableName;
        }

        return querysql;
    },

    /**
     * 获取不匹配的数据类型索引
     * @param caches 数据类型缓存数据
     * @param sourceData 源数据类型集合
     * @param destData 目标数据类型集合
     */
    listDataTypeNoMatchField: function ($object, sourceData, destData, callback) {

        var caches = PUBLIC.getCache($object, 'data_type');
        var noMatch = [];

        var dataObject = {};

        for (var key in caches) {
            if (caches.hasOwnProperty(key)) {
                for (var i = 0; i < caches[key].length; i++) {
                    dataObject[caches[key][i]] = key;
                }
            }
        }
        var slength = sourceData.length;
        var dlength = destData.length;

        var length = slength > dlength ? dlength : slength;
        for (var i = 0; i < length; i++) {

            if (dataObject[sourceData[i].toLowerCase()] != dataObject[destData[i].toLowerCase()] ||
                (undefined == dataObject[sourceData[i].toLowerCase()] && undefined == dataObject[destData[i].toLowerCase()])) {
                noMatch.push(i);
            }
        }

        for (var i = length; i < (slength > dlength ? slength : dlength); i++) {
            noMatch.push(i);
        }
        return noMatch;

    },

    /**
     * 标识不匹配的行
     */
    identifyNoMatchField: function (noMatch, $sourceTable, $destTable) {
        $('tr', $sourceTable).css('border-bottom', '');
        $('tr', $destTable).css('border-bottom', '');

        var css = '1px dashed red';
        for (var i = 0; i < noMatch.length; i++) {
            $('tr[columnNo="' + noMatch[i] + '"]', $sourceTable).css('border-bottom', css);
            $('tr[columnNo="' + noMatch[i] + '"]', $destTable).css('border-bottom', css);
        }
    }
};