package com.centit.sys.util;

public class SysOptLogFactoryImpl {

    /**
     * 获取系统日志接口
     *
     * @param optId F_OPTINFO表中 OPTID 字段，对应此Action操作
     * @return
     */
    public static ISysOptLog getSysOptLog(String optId) {

        return new SysOptLogger(optId);
    }

}
