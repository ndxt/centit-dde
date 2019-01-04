package com.centit.dde.dataio;

import com.centit.dde.dao.ExchangeTaskDao;
import com.centit.dde.datafile.ExchangeFileReader;
import com.centit.dde.datafile.TableFileReader;
import com.centit.dde.exception.SqlResolveException;
import com.centit.dde.po.ExchangeTask;
import com.centit.dde.po.TaskLog;
import com.centit.dde.service.TaskLogManager;
import com.centit.dde.util.TaskConsoleWriteUtils;
import com.centit.framework.common.SysParametersUtils;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.ZipCompressor;
import com.centit.support.file.FileSystemOpt;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Service
public class ImportDataImpl implements ImportData {
    public static final Log logger = LogFactory.getLog(ImportDataImpl.class);

    private static boolean debugEnabled = logger.isDebugEnabled();

    private static final String IMPORT = SysParametersUtils.getStringValue("import");

    @Resource
    private ExecuteDataMap executeDataMap;

    @Resource
    private TaskLogManager taskLogManager;

    @Resource
    private ExchangeTaskDao exchangeTaskDao;

    /*public void setExecuteDataMap(ExecuteDataMap executeDataMap) {
        this.executeDataMap = executeDataMap;
    }

    public void setTaskLogManager(TaskLogManager taskLogManager) {
        this.taskLogManager = taskLogManager;
    }

    public void setExchangeTaskDao(ExchangeTaskDao exchangeTaskDao) {
        this.exchangeTaskDao = exchangeTaskDao;
    }*/

    private static String calcZipFolderPath(String zipFilePath) {
        String dataPath = IMPORT + "/" + DatetimeOpt.currentDate() + "/" + FileSystemOpt.extractFileName(zipFilePath)
                + DatetimeOpt.convertDateToString(DatetimeOpt.currentUtilDate(), "HHmmss");
        FileSystemOpt.createDirect(dataPath);
        return dataPath;
    }

    @Override
    public int doImport(String filePath, String userCode, String runType, Long taskId) {
        String msg = null;
        if (!FileSystemOpt.existFile(filePath + "/exchange.xml")) {
            msg = "未找到解压后指定目录" + filePath + "/exchange.xml" + " 中的文件";
            logger.error(msg);

            TaskConsoleWriteUtils.writeError(taskId, msg);
            return -1;
        }

        ExchangeFileReader reader = new ExchangeFileReader();

        Long taskLogId = taskLogManager.getTaskLogId();
        TaskLog taskLog = new TaskLog();
        taskLog.setLogId(taskLogId);
        taskLog.setTaskId(taskId);
        taskLog.setRunBeginTime(DatetimeOpt.currentSqlDate());
        taskLog.setRunType(runType);
        taskLog.setRunner(userCode);
        taskLog.setTaskType("3");
        taskLogManager.saveNewObject(taskLog);
        int nError = 0;
        int nSucceed = 0;
        reader.setDataDirPath(filePath);

        if (debugEnabled) {
            logger.debug("开始解析导入XML文件");
        }

        reader.readExchangeInfo();

        if (debugEnabled) {
            logger.debug("解析导入XML文件结束");
        }

        for (int i = 0; i < reader.getTableSum(); i++) {
            TableFileReader tr = reader.getTableFileReader(i);
            // System.out.println(tr.getExportName());

            if (debugEnabled) {
                logger.debug("执行导入 " + tr.getFilePath() + " 目录文件");
            }

            int nRes = -1;
            try {
                nRes = executeDataMap.doExecute(tr, userCode, runType, taskLogId);
            } catch (SqlResolveException e) {
                logger.error(e.getMessage(), e);
            }

            if (debugEnabled) {
                logger.debug("执行导入 " + tr.getFilePath() + " 目录文件结束");
                logger.debug("执行导入结果集 nRes = " + nRes);
            }
            if (nRes < 0) {
                nError++;
                logger.error("执行导入 " + tr.getFilePath() + " 目录文件错误, dataOptId = " + tr.getDataOptId()
                        + " sourceOsId = " + tr.getSourceOsId() + " tableName = " + tr.getExportName());

            } else {
                nSucceed++;
            }
        }

        String message = "完成" + String.valueOf(nError + nSucceed) + "组交换,成功" + String.valueOf(nSucceed) + "组，失败"
                + String.valueOf(nError) + "组。";
        msg = "导入" + filePath + ":" + message;
        logger.info(msg);

        TaskConsoleWriteUtils.write(taskId, msg);

        taskLog.setRunEndTime(DatetimeOpt.currentSqlDate());
        taskLog.setOtherMessage(message);
        taskLogManager.updateObject(taskLog);
        return 0;
    }

    public int doImport(String filePath, String userCode, String runType) {
        return doImport(filePath, userCode, runType, 0l);
    }

    @Override
    public int doImportZipFile(String zipFilePath, String userCode, String runType, Long taskId) {
        String dataPath = calcZipFolderPath(zipFilePath);

        if (debugEnabled) {
            logger.debug("开始解压.zip文件");
        }
        try {
            ZipCompressor.release(zipFilePath, dataPath);
        } catch (RuntimeException ex) {
            logger.error("解压 zip 文件出错，" + ex.getMessage());
            TaskConsoleWriteUtils.writeError(taskId, "解压 zip 文件出错，" + ex.getMessage());
        }

        if (debugEnabled) {
            logger.debug("解压.zip文件结束");
        }
        int n = doImport(dataPath, userCode, runType, taskId);
        
        FileSystemOpt.deleteDirect(new File(dataPath));
        return n;
    }

    public int doImportZipFile(String zipFilePath, String userCode, String runType) {
        return doImportZipFile(zipFilePath, userCode, runType, 0l);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public String runImportTask(Long taskID, String userCode, String runType) {
        String msg = null;

        ExchangeTask exchangeTask = exchangeTaskDao.getObjectById(taskID);
        exchangeTask.setLastRunTime(DatetimeOpt.currentSqlDate());
        exchangeTaskDao.updateObject(exchangeTask);
        msg = "开始执行导入：" + exchangeTask.getTaskName() + "........";
        logger.info(msg);

        TaskConsoleWriteUtils.writeInfo(taskID, msg);

        String monitorFolder = exchangeTask.getMonitorFolder();
        //
        List<File> dataFiles = FileSystemOpt.findFilesByExt(monitorFolder, "zip");

        if (CollectionUtils.isEmpty(dataFiles)) {
            msg = "导入文件目录中无可用的.zip文件";

            logger.error(msg);
            TaskConsoleWriteUtils.writeError(taskID, msg);
        }


        for (File f : dataFiles) {
            String fileName = f.getPath();

            msg = "导入文件全路径 = " + fileName;
            logger.info(msg);

            int resultCode = doImportZipFile(fileName, userCode, runType, taskID);

            if (0 == resultCode) {
                msg = "成功完成导入操作";
                logger.info(msg);

                TaskConsoleWriteUtils.writeInfo(taskID, msg);
            } else {
                msg = "导入操作失败";
                logger.error(msg);

                TaskConsoleWriteUtils.writeError(taskID, msg);
            }


            boolean result = f.renameTo(new File(fileName + ".bak"));

            if (result) {
                msg = "导入操作结束后成功将源文件修改为.bak备份文件";
                TaskConsoleWriteUtils.writeInfo(taskID, msg);
            } else {
                msg = "导入操作结束后将源文件修改为.bak备份文件失败，为防止重复导入，请手工将文件后缀修改为.bak。";
                TaskConsoleWriteUtils.writeError(taskID, msg);
            }

        }

        TaskConsoleWriteUtils.stop(taskID);

        return "";
    }

}
