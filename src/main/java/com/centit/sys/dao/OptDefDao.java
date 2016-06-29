package com.centit.sys.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.support.utils.StringBaseOpt;
import com.centit.sys.po.FOptdef;
import com.centit.sys.po.VOptDef;

public class OptDefDao extends BaseDaoImpl<FOptdef> {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public List<FOptdef> getOptDefByOptID(String sOptID) {
        return getHibernateTemplate()
                .find("FROM FOptdef WHERE optid =?", sOptID);
    }

    public int getOptDefSumByOptID(String sOptID) {
        return Integer.valueOf(getHibernateTemplate().find(
                "SELECT count(optcode) FROM FOptdef WHERE optid = ?", sOptID)
                .get(0).toString()
        );
    }

    @SuppressWarnings("unchecked")
    public List<VOptDef> getVOptDefs(String wfcode) {
        return getHibernateTemplate().find("FROM VOptDef WHERE wfcode= ?", wfcode);
    }

    public void deleteOptdefOfOptID(String sOptID) {
        this.doExecuteHql("DELETE FROM FOptdef WHERE optid = ?", sOptID);
    }

    public void initOptdefOfOptID(String sOptID) {
        FOptdef newDef = new FOptdef();
        String sOptCode = getNextOptCode();
        //list
        newDef.setOptid(sOptID);
        newDef.setOptdesc("系统自动添加");
        newDef.setOptcode(sOptCode);
        newDef.setOptmethod("list");
        newDef.setOptname("查询");
        saveObject(newDef);

        //edit
        sOptCode = StringBaseOpt.nextCode(sOptCode);
        newDef = new FOptdef();
        newDef.setOptid(sOptID);
        newDef.setOptdesc("系统自动添加");
        newDef.setOptcode(sOptCode);
        newDef.setOptmethod("edit");
        newDef.setOptname("修改/添加");
        saveObject(newDef);
        //delete
        sOptCode = StringBaseOpt.nextCode(sOptCode);
        newDef = new FOptdef();
        newDef.setOptid(sOptID);
        newDef.setOptdesc("系统自动添加");
        newDef.setOptcode(sOptCode);
        newDef.setOptmethod("delete");
        newDef.setOptname("删除");
        saveObject(newDef);
        //view
        sOptCode = StringBaseOpt.nextCode(sOptCode);
        newDef = new FOptdef();
        newDef.setOptid(sOptID);
        newDef.setOptdesc("系统自动添加");
        newDef.setOptcode(sOptCode);
        newDef.setOptmethod("view");
        newDef.setOptname("查看明细");
        saveObject(newDef);
    }


    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("OPTID", CodeBook.EQUAL_HQL_ID);
            filterField.put("PREOPTID", CodeBook.EQUAL_HQL_ID);
            filterField.put("ISINTOOLBAR", CodeBook.EQUAL_HQL_ID);
            filterField.put("TOPOPTID", CodeBook.EQUAL_HQL_ID);
            filterField.put("OPTTYPE", CodeBook.EQUAL_HQL_ID);
            filterField.put("OPTNAME", CodeBook.LIKE_HQL_ID);
        }
        return filterField;
    }

    public String getNextOptCode() {
        return getNextKeyByHqlStrOfMax("optcode", "FOptdef", 8);
    }

}
