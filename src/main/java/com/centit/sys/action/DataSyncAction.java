package com.centit.sys.action;

import java.util.Date;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.centit.cas.sys.service.ICasWebClient;
import com.centit.core.action.BaseAction;
import com.centit.core.utils.DwzResultParam;
import com.centit.support.utils.DatetimeOpt;
import com.centit.sys.service.CodeRepositoryManager;
import com.centit.sys.service.DataSyncException;
import com.centit.sys.service.DataSyncManager;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 13-5-16
 * Time: 上午10:26
 * To change this template use File | Settings | File Templates.
 */
public class DataSyncAction extends BaseAction {
    private static final long serialVersionUID = -9122799732668306983L;

    private DataSyncManager dataSyncManager;

    private CodeRepositoryManager codeRepositoryManager;

    private ICasWebClient casWebClient;

    public void setCasWebClient(ICasWebClient casWebClient) {
        this.casWebClient = casWebClient;
    }

    public void setDataSyncManager(DataSyncManager dataSyncManager) {
        this.dataSyncManager = dataSyncManager;
    }

    public void setCodeRepositoryManager(CodeRepositoryManager codeRepositoryManager) {
        this.codeRepositoryManager = codeRepositoryManager;
    }

    public String list() {
        return "list";
    }

    private DwzResultParam dwzResultParam;

    public DwzResultParam getDwzResultParam() {
        return dwzResultParam;
    }


    public String syncAll() {
        Map<Object, Object> paramMap = request.getParameterMap();
        Map<String, Object> filterMap = convertSearchColumn(paramMap);

        Date lastModDate = null;
        String date = (String) filterMap.get("lastModDate");
        if (StringUtils.hasText(date)) {
            lastModDate = DatetimeOpt.convertStringToDate(date, "yyyy-MM-dd HH:mm:ss");
        }

        try {
            if ("LDAP".equalsIgnoreCase((String) filterMap.get("SyncLocation"))) {
                dataSyncManager.updateByLdap(filterMap, lastModDate);
            } else {
                dataSyncManager.update(filterMap, lastModDate);
            }

            codeRepositoryManager.refresh("usercode");
            codeRepositoryManager.refresh("unitcode");
            codeRepositoryManager.refresh("userunit");
        } catch (DataSyncException e) {
            dwzResultParam = DwzResultParam.getErrorDwzResultParam("同步数据失败");

            return ERROR;
        }

        dwzResultParam = new DwzResultParam(DwzResultParam.STATUS_CODE_200, "同步数据成功");
        return "syncAll";
    }


    public String syncUser() {
        Map<Object, Object> paramMap = request.getParameterMap();
        Map<String, Object> filterMap = convertSearchColumn(paramMap);

        try {
            dataSyncManager.updateByUsercode((String) filterMap.get("usercode"));

            codeRepositoryManager.refresh("usercode");
            codeRepositoryManager.refresh("unitcode");
            codeRepositoryManager.refresh("userunit");
        } catch (DataSyncException e) {
            dwzResultParam = DwzResultParam.getErrorDwzResultParam(e.getMessage());

            return ERROR;
        }

        return "syncUser";
    }
}
