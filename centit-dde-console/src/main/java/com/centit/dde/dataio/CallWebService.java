package com.centit.dde.dataio;

public interface CallWebService {
    /**
     * 导出离线文件
     *
     * @param exportID
     * @param userCode
     * @return >=0 为正常，<0 为错误编码
     */
    int doCallService(Long exportID, String userCode);

    /**
     * 导出离线文件
     *
     * @param taskID
     * @param userCode
     * @param runType  1:手动 0：系统自动 2:WebService接口
     * @return >=0 为正常，<0 为错误编码
     */
    String runCallServiceTask(Long taskID, String userCode, String runType,String taskType);
}
