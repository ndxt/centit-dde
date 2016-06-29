package com.centit.dde.ws;

import javax.jws.WebService;

import com.centit.dde.dao.ExportSqlDao;
import com.centit.dde.datafile.TableFileWriter;
import com.centit.dde.dataio.ExportData;
import com.centit.dde.po.ExportSql;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.service.SysUserManager;

@WebService(endpointInterface = "com.centit.dde.ws.DownloadData")
public class DownloadDataImpl implements DownloadData {


    private ExportSqlDao exportSqlDao;

    private ExportData exportData;

    private SysUserManager sysUserManager;

    private ValidatorWs validatorWs;

    public void setExportSqlDao(ExportSqlDao exportSqlDao) {
        this.exportSqlDao = exportSqlDao;
    }

    public void setExportData(ExportData exportData) {
        this.exportData = exportData;
    }

    public void setSysUserManager(SysUserManager sysUserManager) {
        this.sysUserManager = sysUserManager;
    }

    public void setValidatorWs(ValidatorWs validatorWs) {
        this.validatorWs = validatorWs;
    }

    @Override
    public String downloadTableAsXml(String userName, String userPin, long exportId) {

        String validator = validatorWs.validatorUserinfo(userName, userPin);
        if (null != validator) {
            return validator;
        }
        FUserinfo userInfo = sysUserManager.getUserByLoginname(userName);

        //解决在WebService环境下集合对象延迟加载问题
        ExportSql exportSql = exportSqlDao.fetchObjectById(exportId);

        TableFileWriter tableWriter = new TableFileWriter();

        tableWriter.setExportName("exp" + exportSql.getExportId());
        tableWriter.setDataOptId(exportSql.getDataOptId());
        tableWriter.setSorceOsId(exportSql.getSourceOsId());
        tableWriter.setSourceDBName(exportSql.getSourceDatabaseName());


        validator = validatorWs.validatorDataOptId(userName, tableWriter.getDataOptId(), "E");
        if (null != validator) {
            return validator;
        }
        tableWriter.prepareMemoryWriter();

        int nRes = exportData.doExportSql(exportSql, tableWriter, userInfo.getUsercode(), null);


        String xmlData = tableWriter.getMemoryDataXML();//.toString();

        tableWriter.closeWriter();

        if (nRes > 0) {
            return xmlData;

        }

        return "1:导出失败";
    }
}
