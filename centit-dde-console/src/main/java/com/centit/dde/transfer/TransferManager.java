package com.centit.dde.transfer;

public interface TransferManager {

    /**
     * 不作为任务运行的交换
     *
     * @param mapInfoID
     * @return
     */
    int doTransfer(Long mapInfoID, String userCode);

    /**
     * @param taskID
     * @param userCode
     * @param runType  1:手动 0：系统自动 2:WebService接口
     * @param taskType  
     * @return
     */
    String runTransferTask(Long taskID, String userCode, String runType,String taskType);

}
