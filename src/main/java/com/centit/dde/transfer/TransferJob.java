package com.centit.dde.transfer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.centit.framework.model.basedata.IUserInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.WebUtils;

import com.centit.dde.dataio.CallWebService;
import com.centit.dde.dataio.ExportData;
import com.centit.dde.dataio.ImportData;

public class TransferJob {

    private static Log logger = LogFactory.getLog(TransferJob.class);

    private TransferManager transferManager;

    public TransferManager getTransferManager() {
        return transferManager;
    }

    public void setTransferManager(TransferManager transferManager) {
        this.transferManager = transferManager;
    }

    private ExportData exportData;

    private ImportData importData;

    private CallWebService callWebService;

    public void setCallWebService(CallWebService callWebService) {
        this.callWebService = callWebService;
    }

    public void setImportData(ImportData importData) {
        this.importData = importData;
    }

    public void setExportData(ExportData exportData) {
        this.exportData = exportData;
    }

    private static final long serialVersionUID = 1L;

    public String doTransfer() {

        final IUserInfo userDetails = (IUserInfo) this.getLoginUser();
        final Long taskId = Long.valueOf(request.getParameter("id"));

        final String taskType = WebUtils.findParameterValue(request, "s_taskType");

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void run() {
                try {
                    if ("1".equals(taskType)) {
                        transferManager.runTransferTask(taskId, userDetails.getUserCode(), "1",taskType);
                    } else if ("2".equals(taskType)) {
                        exportData.runExportTask(taskId, userDetails.getUserCode(), "1",taskType);
                    } else if ("3".equals(taskType)) {
                        importData.runImportTask(taskId, userDetails.getUserCode(), "1");
                    } else if("4".equals(taskType)) {
                        callWebService.runCallServiceTask(taskId, userDetails.getUserCode(), "1",taskType);
                    }
                } catch (RuntimeException e) {
                    logger.error(e);
                }

            }
        });

        dwzResultParam = new DwzResultParam();
        dwzResultParam.setForwardUrl( "/dde/exchangeTask!list.do?s_taskType=" + taskType);
        //dwzResultParam.setMessage(SUCCESS);
        Collection<String> s= new ArrayList<String>();
        s.add("");
        this.setActionMessages(s);
        return SUCCESS;
    }

    public String transferExecute() {
        return "transferExecute";
    }

}
