package com.centit.dde.dataio;

public interface ImportData {
    /**
     * 导入离线文件
     *
     * @param zipFilePath
     * @param userCode
     * @param runType     1:手动 0：系统自动 2:WebService接口
     * @return >=0 为正常，<0 为错误编码
     */
    int doImportZipFile(String zipFilePath, String userCode, String runType);

    int doImportZipFile(String zipFilePath, String userCode, String runType, Long taskId);

    /**
     * 导入离线文件
     *
     * @param filePath
     * @param userCode
     * @param runType  1:手动 0：系统自动 2:WebService接口
     * @return >=0 为正常，<0 为错误编码
     */
    int doImport(String filePath, String userCode, String runType);

    int doImport(String filePath, String userCode, String runType, Long taskId);

    /**
     * @param taskID
     * @param userCode
     * @param runType  1:手动 0：系统自动 2:WebService接口
     * @return
     */
    String runImportTask(Long taskID, String userCode, String runType);
}
