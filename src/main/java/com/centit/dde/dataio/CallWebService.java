package com.centit.dde.dataio;

public interface CallWebService {
    /**
     * 导出离线文件
     *
     * @param exportID
     * @param usercode
     * @return >=0 为正常，<0 为错误编码
     */
    public int doCallService(Long exportID, String usercode);

    /**
     * 导出离线文件
     *
     * @param taskID
     * @param usercode
     * @param runType  1:手动 0：系统自动 2:WebService接口
     * @return >=0 为正常，<0 为错误编码
     */
    public String runCallServiceTask(Long taskID, String usercode, String runType,String taskType);
}
