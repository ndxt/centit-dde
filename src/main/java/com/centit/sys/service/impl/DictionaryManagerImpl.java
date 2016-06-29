package com.centit.sys.service.impl;

/**
 @author codefan@centit.com
 */

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.support.utils.DatetimeOpt;
import com.centit.sys.dao.DataCatalogDao;
import com.centit.sys.dao.DataDictionaryDao;
import com.centit.sys.po.FDatacatalog;
import com.centit.sys.po.FDatadictionary;
import com.centit.sys.po.FDatadictionaryId;
import com.centit.sys.service.DictionaryManager;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class DictionaryManagerImpl extends BaseEntityManagerImpl<FDatacatalog> implements DictionaryManager {
    private static final long serialVersionUID = 1L;
    private DataCatalogDao catalogDao;
    private DataDictionaryDao dictionaryDao;

    public void setCatalogDao(DataCatalogDao dao) {
        this.catalogDao = dao;
        setBaseDao(dao);
    }

    public void setDictionaryDao(DataDictionaryDao dao) {
        this.dictionaryDao = dao;
    }

    public List<FDatadictionary> findByCdtbnm(String cdtbnm) {
        return dictionaryDao.findByCdtbnm(cdtbnm);
    }

    public FDatadictionary findById(FDatadictionaryId id) {
        return dictionaryDao.getObjectById(id);
    }

    public List<FDatacatalog> getGBCdctgs() {
        return catalogDao.getGBCdctgs();
    }

    public List<FDatacatalog> getSysCdctgs() {
        return catalogDao.getSysCdctgs();
    }

    public List<FDatacatalog> getUserCdctgs() {
        return catalogDao.getUserCdctgs();
    }

    public List<FDatadictionary> getSysCditms(Map<String, Object> filterDescMap) {
        return dictionaryDao.listObjects(filterDescMap);
    }

    public void deleteCditms(FDatadictionaryId id) {
        FDatacatalog datacatalog = catalogDao.getObjectById(id.getCatalogcode());
        datacatalog.setIsupload("0");
        datacatalog.setLastModifyDate(DatetimeOpt.currentUtilDate());
        catalogDao.saveObject(datacatalog);
        dictionaryDao.deleteObjectById(id);
    }

    public void saveCditms(FDatadictionary dd) {
        FDatacatalog datacatalog = catalogDao.getObjectById(dd.getCatalogcode());
        datacatalog.setIsupload("0");
        datacatalog.setLastModifyDate(DatetimeOpt.currentUtilDate());
        catalogDao.saveObject(datacatalog);
        dd.setLastModifyDate(DatetimeOpt.currentUtilDate());
        dictionaryDao.saveObject(dd);
    }

    public String[] getFieldsDesc(String sDesc, String sType) {
        String[] nRes = {"数据代码", "扩展代码", "扩展代码2", "标记", "数值", "类型", "数据描述"};
        if ("T".equals(sType))
            nRes[1] = "上级代码";
        if (sDesc == null || "".equals(sDesc))
            return nRes;
        String[] s = StringUtils.split(sDesc, ';');
        if (s == null)
            return nRes;
        int n = s.length;

        for (int i = 0; i < n; i++) {
            int p = s[i].indexOf(':');
            if (p > 1)
                nRes[i] = s[i].substring(0, p);
            else
                nRes[i] = s[i];
        }
        return nRes;
    }

    public List<FDatadictionary> getList(String code, String name) {

        return dictionaryDao.getList(code, name);
    }

    public List<FDatadictionary> getcodeList(String code) {

        return dictionaryDao.getcodeList(code);
    }

    @Override
    public String toxmlstring(List<FDatadictionary> list) {
        String rs = "<?xml version='1.0' encoding='gbk'?><rs><result>success</result>"
                + "<message></message>" + "<list>";
        if (list != null) {
            for (FDatadictionary datadictionary : list) {
                rs = rs + "<datacatalog catalogcode='"
                        + datadictionary.getCatalogcode() + "' " + "datacode='"
                        + datadictionary.getDatacode() + "' " + "datavalue='"
                        + datadictionary.getDatavalue() + "' " + "extracode='"
                        + datadictionary.getExtracode() + "' " + "extracode2='"
                        + datadictionary.getExtracode2() + "' " + "datatag='"
                        + datadictionary.getDatatag() + "' " + "datastyle='"
                        + datadictionary.getDatastyle() + "' " + "datadesc='"
                        + datadictionary.getDatadesc() + "' " + "lastModifyDate='"
                        + datadictionary.getLastModifyDate() + "'" + "createDate='"
                        + datadictionary.getCreateDate() + "'/>";
            }
        }
        rs = rs + "</list></rs>";

        return rs;
    }

    @Override
    public List<FDatadictionary> findByJsh() {
        return dictionaryDao.findByJsh();
    }

    @Override
    public String findByJshtoxml(String loginName, String loginPassword,
                                 String slat, String datacatalogcode) {


        List<FDatadictionary> list = null;
        if ((datacatalogcode == null) || (datacatalogcode.equals(""))) {
            list = findByJsh();
        } else {
            list = findByCdtbnm(datacatalogcode);
        }
        return toxmlstring(list);

    }

    private List<FDatadictionary> listdata;

    public List<FDatadictionary> getListdata() {
        return listdata;
    }

    public void setListdata(List<FDatadictionary> listdata) {
        this.listdata = listdata;
    }

    public String getdatadetail(String datacatalogcode, String datacatalogname) {
        String rs = "<?xml version='1.0' encoding='gbk'?><rs><result>success</result>";
        if (!datacatalogcode.equals("")) {
            listdata = dictionaryDao.findByCdtbnm(datacatalogcode);
        }
        if (datacatalogcode.equals("") && !datacatalogname.equals("")) {
            listdata = dictionaryDao.getList(datacatalogcode, datacatalogname);
        }
        rs = rs + "<datalist>";
        if (listdata != null) {
            for (FDatadictionary datadictionary : listdata) {
                rs = rs + "<datadictionary catalogcode='"
                        + datadictionary.getCatalogcode() + "' " + "datacode='"
                        + datadictionary.getDatacode() + "' " + "datavalue='"
                        + datadictionary.getDatavalue() + "' " + "extracode='"
                        + datadictionary.getExtracode() + "' " + "extracode2='"
                        + datadictionary.getExtracode2() + "' " + "datatag='"
                        + datadictionary.getDatatag() + "' " + "datastyle='"
                        + datadictionary.getDatastyle() + "' " + "datadesc='"
                        + datadictionary.getDatadesc() + "' " + "lastModifyDate='"
                        + datadictionary.getLastModifyDate() + "'" + "createDate='"
                        + datadictionary.getCreateDate() + "'/>";
            }
        }
        rs = rs + "</datalist></rs>";
        return rs;
    }

    public String getcatalog(String datacatalogcode, String lastModifyDate) {
        String rs = "<?xml version='1.0' encoding='gbk'?><rs><result>success</result>";
        List<FDatacatalog> list = dictionaryDao.getcatalog(datacatalogcode, lastModifyDate);
        rs = rs + "<datalist>";
        if (list != null) {
            for (FDatacatalog fdatacatalog : list) {
                rs = rs + "<datacatalog catalogcode='"
                        + fdatacatalog.getCatalogcode() + "' " + "catalogname='"
                        + fdatacatalog.getCatalogname() + "' " + "catalogstyle='"
                        + fdatacatalog.getCatalogstyle() + "' " + "catalogtype='"
                        + fdatacatalog.getCatalogtype() + "' " + "catalogdesc='"
                        + fdatacatalog.getCatalogdesc() + "' " + "fielddesc='"
                        + fdatacatalog.getFielddesc() + "' " + "isupload='"
                        + fdatacatalog.getIsupload() + "' " + "createDate='"
                        + fdatacatalog.getCreateDate() + "' " + "lastModifyDate='"
                        + fdatacatalog.getLastModifyDate() + "'/>";
            }
        }
        rs = rs + "</datalist></rs>";

        return rs;
    }

    @Override
    public List<FDatacatalog> getcatalogbylastModify(
            String lastModifyDate) {
        List<FDatacatalog> list = dictionaryDao.getcatalog("", lastModifyDate);
        return list;
    }
}
