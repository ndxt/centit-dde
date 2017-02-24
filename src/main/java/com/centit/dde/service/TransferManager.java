package com.centit.dde.service;

public interface TransferManager {

    /**
     * 不作为任务运行的交换
     *
     * @param mapinfoID
     * @return
     */
    public int doTransfer(Long mapinfoID, String usercode);

    /**
     * @param taskID
     * @param usercode
     * @param runType  1:手动 0：系统自动 2:WebService接口
     * @return
     */
    public String runTransferTask(Long taskID, String usercode, String runType,String taskType);

}
