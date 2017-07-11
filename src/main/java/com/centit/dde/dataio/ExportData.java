package com.centit.dde.dataio;

import com.centit.dde.datafile.TableFileWriter;
import com.centit.dde.po.ExportSql;
import com.centit.dde.po.TaskDetailLog;

public interface ExportData {
    int doExportSql(ExportSql exportSql, TableFileWriter tableWriter, String userCode,
                    TaskDetailLog taskDetailLog);

    /**
     * 导出离线文件
     *
     * @param exportID
     * @param userCode
     * @return >=0 为正常，<0 为错误编码
     */
    int doExport(Long exportID, String userCode);

    /**
     * 执行导出任务
     *
     * @param taskID
     * @param userCode
     * @param runType  1:手动 0：系统自动 2:WebService接口
     * @return >=0 为正常，<0 为错误编码
     */
    String runExportTask(Long taskID, String userCode, String runType,String taskType);
}
