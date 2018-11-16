package com.centit.dde.controller;

import com.centit.dde.dataio.CallWebService;
import com.centit.dde.dataio.ExportData;
import com.centit.dde.dataio.ImportData;
import com.centit.dde.po.ExchangeTask;
import com.centit.dde.service.ExchangeTaskManager;
import com.centit.dde.service.TransferManager;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.security.model.CentitUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/transfer")
public class TransferController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(TransferController.class);

    private static final long serialVersionUID = 1L;

    @Resource
    private ExchangeTaskManager exchangeTaskMag;

    @Resource
    private TransferManager transferManager;

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

    
    @RequestMapping(value="/doTransfer/{taskIds}", method = {RequestMethod.POST})
    public void doTransfer(@PathVariable Long taskIds,  HttpServletRequest request,HttpServletResponse response) {
        final long taskId = taskIds;
        final CentitUserDetails userDetails = getLoginUser(request);

        ExchangeTask exchangeTask = exchangeTaskMag.getObjectById(taskId);
        
        String Type = exchangeTask.getTaskType();

        final String taskType = Type;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        //ThreadPoolExecutor
        executorService.execute(() -> {
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
                logger.error(e.getMessage());
            }

        });

//        dwzResultParam = new DwzResultParam();
//        dwzResultParam.setForwardUrl( "/dde/exchangeTask!list.do?s_taskType=" + taskType);
        //dwzResultParam.setMessage(SUCCESS);
        Collection<String> s= new ArrayList<String>();
        s.add("");
//        this.setActionMessages(s);
//        return SUCCESS;
        JsonResultUtils.writeSuccessJson(response);
    }

    @RequestMapping(value="/transferExecute", method = {RequestMethod.GET})
    public void transferExecute(HttpServletRequest request,HttpServletResponse response) {
//        return "transferExecute";
        JsonResultUtils.writeSuccessJson(response);
    }

}
