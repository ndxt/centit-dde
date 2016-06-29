package com.centit.sys.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.sys.po.OptRunRec;
import com.centit.sys.po.OptRunRecId;

public class OptRunRecDao extends BaseDaoImpl<OptRunRec> {
    private static final long serialVersionUID = 1L;

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("actionurl", CodeBook.LIKE_HQL_ID);
            filterField.put("funcname", CodeBook.LIKE_HQL_ID);
            filterField.put("lastreqtime", " lastreqtime >= to_date(?,'yyyy-mm-dd') ");
        }
        return filterField;
    }

    public void recRunTime(String sActionUrl, String method) {
        OptRunRec optRec = this.getObjectById(new OptRunRecId(sActionUrl, method));
        if (optRec == null) {
            optRec = new OptRunRec();
            optRec.setActionurl(sActionUrl);
            optRec.setFuncname(method);
            optRec.setReqtimes(1L);
        } else
            optRec.setReqtimes(optRec.getReqtimes() + 1L);
        ;
        optRec.setLastreqtime(new Date(System.currentTimeMillis()));

        this.saveObject(optRec);
    }
}
