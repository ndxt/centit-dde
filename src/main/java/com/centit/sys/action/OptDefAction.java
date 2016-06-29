package com.centit.sys.action;

import java.util.List;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.sys.po.FOptdef;
import com.centit.sys.po.FOptinfo;
import com.centit.sys.service.CodeRepositoryManager;
import com.centit.sys.service.FunctionManager;
import com.centit.sys.service.OptdefManager;

public class OptDefAction extends BaseEntityDwzAction<FOptdef> {


    private static final long serialVersionUID = 1L;
    private OptdefManager optdefManager;

    private FunctionManager functionManager;
    private CodeRepositoryManager codeRepositoryManager;
    private FOptinfo optinfo;
    private List<FOptdef> optdefs;
    private Integer totalRows;

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public List<FOptdef> getOptdefs() {
        return optdefs;
    }

    public void setOptdefs(List<FOptdef> optdefs) {
        this.optdefs = optdefs;
    }

    public FOptinfo getOptinfo() {
        return optinfo;
    }

    public void setOptinfo(FOptinfo optinfo) {
        this.optinfo = optinfo;
    }

    public void setCodeRepositoryManager(CodeRepositoryManager codeRepositoryManager) {
        this.codeRepositoryManager = codeRepositoryManager;
    }


    public void setFunctionManager(FunctionManager functionManager) {
        this.functionManager = functionManager;
    }

    public void setOptdefManager(OptdefManager optdefManager) {
        this.optdefManager = optdefManager;
        setBaseEntityManager(optdefManager);
    }

    public String list() {

        try {
            optinfo = functionManager.getObjectById(object.getOptid());
            optdefs = optdefManager.getOptDefByOptID(object.getOptid());
            return LIST;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String edit1() {
        try {
            if (object == null) {
                object = getEntityClass().newInstance();
            } else {
                FOptdef o = baseEntityManager.getObject(object);
                if (o != null)
                    // 将对象o copy给object，object自己的属性会保留
                    baseEntityManager.copyObject(object, o);
                else
                    baseEntityManager.clearObjectProperties(object);
            }
            return EDIT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String built() {
        try {
            FOptdef fOptdef = new FOptdef();
            fOptdef.setOptid(object.getOptid());
            object = fOptdef;
            object = optdefManager.getObject(object);
            return BUILT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String delete1() {

        try {
            object = optdefManager.getObjectById(object.getOptcode());
            optdefManager.deleteObject(object);
            codeRepositoryManager.refresh("optcode");
            deletedMessage();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return EDIT;
        }
        return "delete";

    }

    public String save() {

        try {

            FOptdef dbobject = optdefManager.getObjectById(object.getOptcode());
            if (dbobject != null) {
                dbobject.copyNotNullProperty(object);
                object = dbobject;
            }
            try {
                optdefManager.saveObject(object);
                codeRepositoryManager.refresh("optcode");
                savedMessage();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return EDIT;
            }
            return SUCCESS;
        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }


}
