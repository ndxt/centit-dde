package com.centit.dde.dataio;

import com.centit.dde.datafile.TableFileWriter;
import com.centit.dde.po.ExportSql;
import com.centit.dde.po.TaskDetailLog;

public interface ExportData {
    int doExportSql(ExportSql exportSql, TableFileWriter tableWriter, String usercode,
                    TaskDetailLog taskDetailLog);

    /**
     * 导出离线文件
     *
     * @param exportID
     * @param usercode
     * @return >=0 为正常，<0 为错误编码
     */
    public int doExport(Long exportID, String usercode);

    /**
     * 导出离线文件
     *
     * @param taskID
     * @param usercode
     * @param runType  1:手动 0：系统自动 2:WebService接口
     * @return >=0 为正常，<0 为错误编码
     */
    public String runExportTask(Long taskID, String usercode, String runType,String taskType);
}
