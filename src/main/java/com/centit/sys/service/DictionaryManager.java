package com.centit.sys.service;

import java.util.List;
import java.util.Map;

import com.centit.core.service.BaseEntityManager;
import com.centit.sys.po.FDatacatalog;
import com.centit.sys.po.FDatadictionary;
import com.centit.sys.po.FDatadictionaryId;

public interface DictionaryManager extends BaseEntityManager<FDatacatalog> {
    public List<FDatacatalog> getSysCdctgs();

    public List<FDatacatalog> getUserCdctgs();

    public List<FDatacatalog> getGBCdctgs();

    public List<FDatadictionary> getList(String code, String name);

    public List<FDatadictionary> getcodeList(String code);

    public void deleteCditms(FDatadictionaryId id);

    public void saveCditms(FDatadictionary dd);

    public List<FDatadictionary> getSysCditms(Map<String, Object> filterDescMap);

    public FDatadictionary findById(FDatadictionaryId id);

    public List<FDatadictionary> findByCdtbnm(String cdtbnm);

    public String[] getFieldsDesc(String sDesc, String sType);

    public String findByJshtoxml(String loginName, String loginPassword,
                                 String slat, String datacatalogcode);

    public String toxmlstring(List<FDatadictionary> list);

    public List<FDatadictionary> findByJsh();

    public String getdatadetail(String datacatalogcode, String datacatalogname);

    public String getcatalog(String datacatalogcode, String lastModifyDate);

    public List<FDatacatalog> getcatalogbylastModify(String lastModifyDate);
}
