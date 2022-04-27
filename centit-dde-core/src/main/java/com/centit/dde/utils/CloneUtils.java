package com.centit.dde.utils;

import com.centit.dde.core.DataSet;
import org.apache.commons.lang3.SerializationUtils;

/**
 * 封装clone方法 这种clone方式必须实现序列化接口才行  方便后期维护时更改clone方式
 * 注意：有几种clone出来的数据格式有变化，变化后的数据格式会处理失败,
 * 必须是进去的时候是什么格式，出来后就是什么格式，否则会处理失败
 */
public class CloneUtils {

    public static DataSet clone(DataSet dataSet){
       return SerializationUtils.clone(dataSet);
    }
}
