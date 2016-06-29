package com.centit.sys.action;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.dao.CodeBook;
import com.centit.core.utils.DwzResultParam;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.support.utils.Algorithm;
import com.centit.support.utils.Algorithm.ParentChild;
import com.centit.sys.po.FDatacatalog;
import com.centit.sys.po.FDatadictionary;
import com.centit.sys.po.FDatadictionaryId;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.service.CodeRepositoryManager;
import com.centit.sys.service.DictionaryManager;
import com.centit.sys.util.ISysOptLog;
import com.centit.sys.util.SysOptLogFactoryImpl;

public class DictionaryAction extends BaseEntityDwzAction<FDatacatalog> {

    private static final long serialVersionUID = 1L;

    private static final ISysOptLog SYS_OPT_LOG = SysOptLogFactoryImpl.getSysOptLog("DICTSET");

    private DictionaryManager dictManger;
    private CodeRepositoryManager codeRepo;

    private FDatacatalog catalog;
    private FDatadictionaryId id;
    private FDatadictionary datadictionary;

    private String[] fdesc;
    private List<FDatadictionary> dictDetails;

    private Integer dc_totalRows;

    public FDatacatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(FDatacatalog catalog) {
        this.catalog = catalog;
    }

    public String[] getFdesc() {
        return fdesc;
    }

    public void setFdesc(String[] fdesc) {
        this.fdesc = fdesc;
    }

    public List<FDatadictionary> getDictDetails() {
        return dictDetails;
    }

    public void setDictDetails(List<FDatadictionary> dictDetails) {
        this.dictDetails = dictDetails;
    }

    public Integer getDc_totalRows() {
        return dc_totalRows;
    }

    public void setDc_totalRows(Integer dc_totalRows) {
        this.dc_totalRows = dc_totalRows;
    }

    public FDatadictionaryId getId() {
        return id;
    }

    public void setId(FDatadictionaryId id) {
        this.id = id;
    }

    public FDatadictionary getDatadictionary() {
        return datadictionary;
    }

    public void setDatadictionary(FDatadictionary datadictionary) {
        this.datadictionary = datadictionary;
    }

    public void setCodeRepoManager(CodeRepositoryManager crm) {
        this.codeRepo = crm;
    }

    public void setDictManger(DictionaryManager dm) {
        this.dictManger = dm;
        setBaseEntityManager(dm);
    }

    public String refresh() {

        codeRepo.refreshAll();
        saveMessage("成功刷新系统代码库");
        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    public String list() {

        PageDesc pageDesc = DwzTableUtils.makePageDesc(this.request);

        Map<String, Object> filterMap = convertSearchColumn(request.getParameterMap());

        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");

        if (!StringUtils.isBlank(orderField) && !StringUtils.isBlank(orderDirection)) {

            filterMap.put(CodeBook.SELF_ORDER_BY, orderField + " " + orderDirection);

//            request.setAttribute("orderDirection", orderDirection);
//            request.setAttribute("orderField", orderField);
        }

        objList = baseEntityManager.listObjects(filterMap, pageDesc);
        setbackSearchColumn(filterMap);

        this.pageDesc = pageDesc;
        return LIST;
    }

    private static String getOptIndex(List<Integer> optIndex) {
        JSONObject jo = new JSONObject();
        jo.put("indexes", optIndex);
        return jo.toString();
    }

    public String view() {
        try {
            FDatacatalog dbobject = dictManger.getObjectById(object.getCatalogcode());
            catalog = dbobject;
            fdesc = dictManger.getFieldsDesc(dbobject.getFielddesc(), dbobject.getCatalogtype());
            dictDetails = dictManger.findByCdtbnm(object.getCatalogcode());

            // 树形结构
            if ("T".equals(catalog.getCatalogtype())) {

                ParentChild<FDatadictionary> c = new Algorithm.ParentChild<FDatadictionary>() {
                    public boolean parentAndChild(FDatadictionary p, FDatadictionary c) {
                        return p.getDatacode().equals(c.getExtracode());
                    }
                };

                Algorithm.sortAsTree(dictDetails, c);
                List<Integer> optIndex = Algorithm.makeJqueryTreeIndex(dictDetails, c);
                ServletActionContext.getContext().put("INDEX", getOptIndex(optIndex));

            }

            return VIEW;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            saveError(e.getMessage());
            return ERROR;
        }
    }

    public String save() {
        // this.forwardUrlParam = "";
        try {
            FDatacatalog dbobject = dictManger.getObject(object);
            if (dbobject != null) {
                dbobject.copyNotNullProperty(object);
                object = dbobject;
            }

            dictManger.saveObject(object);
            savedMessage();

        } catch (Exception ee) {
            log.error(ee.getMessage(), ee);
            saveError(ee.getMessage());
        }

        super.dwzResultParam = new DwzResultParam();
        dwzResultParam.setNavTabId(request.getParameter("tabid"));
        return "successDialog";
    }

    public String delete() {
        try {
            baseEntityManager.deleteObject(object);
            deletedMessage();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            saveError(e.getMessage());
        }

        return SUCCESS;
    }

    @SuppressWarnings("unchecked")
    public String edit() {
        try {
            Map<String, Object> params = convertSearchColumn(request.getParameterMap());
            setbackSearchColumn(params);

            FDatacatalog o = baseEntityManager.getObject(object);
            if (o != null) {
                baseEntityManager.copyObject(object, o);

            }

            return "edit";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            saveError(e.getMessage());

            return ERROR;
        }
    }

    public String deleteDetail() {

        try {
            String catalogCode = request.getParameter("catalogcode");
            String dataCode = request.getParameter("datacode");
            id = new FDatadictionaryId();
            if (StringUtils.isNotBlank(catalogCode)) {
                id.setCatalogcode(catalogCode);
            }
            if (StringUtils.isNotBlank(dataCode)) {
                id.setDatacode(dataCode);
            }
            dictManger.deleteCditms(id);
            codeRepo.refresh(id.getCatalogcode());

            FDatacatalog dbobject = dictManger.getObjectById(id.getCatalogcode());

            if (dbobject == null) {
                saveError("entity.missing");
            }

            SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), id.toString(), "删除字典代码 [" + dbobject.getCatalogcode() + "]");

        } catch (Exception e) {
            log.error(e.getMessage());
            saveError(e.getMessage());
        }

        return "successDetail";
    }

    public String editDetail() {

        try {
            String catalogCode = request.getParameter("catalogcode");
            String dataCode = request.getParameter("datacode");
            id = new FDatadictionaryId();

            if (StringUtils.isNotBlank(catalogCode)) {
                id.setCatalogcode(catalogCode);
            }
            if (StringUtils.isNotBlank(dataCode)) {
                id.setDatacode(dataCode);
            }

            datadictionary = dictManger.findById(id);
            if (datadictionary == null) {
                datadictionary = new FDatadictionary();
                datadictionary.setId(id);
            }

            catalog = dictManger.getObjectById(id.getCatalogcode());
            fdesc = dictManger.getFieldsDesc(catalog.getFielddesc(), catalog.getCatalogtype());

            return "editDetail";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String saveDetail() {
        try {
            String optContent = null;
            String oldValue = null;

            FDatadictionary desobj = dictManger.findById(datadictionary.getId());

            if (desobj != null) {
                oldValue = desobj.display();

                desobj.copyNotNullProperty(datadictionary);
                datadictionary = desobj;
            }

            dictManger.saveCditms(datadictionary);
            savedMessage();

            // 刷新缓存中的字典
            codeRepo.refresh(datadictionary.getCatalogcode());

            optContent = datadictionary.display();

            SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), datadictionary.getId().toString(), optContent, oldValue);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            saveError(e.getMessage());
        }

        return "successDetailDialog";

    }

}
