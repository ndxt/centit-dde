package com.centit.sys.service.impl;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.sys.dao.OptDefDao;
import com.centit.sys.dao.OptInfoDao;
import com.centit.sys.po.FOptdef;
import com.centit.sys.po.FOptinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.service.FunctionManager;

import java.util.List;

public class FunctionManagerImpl extends BaseEntityManagerImpl<FOptinfo> implements FunctionManager {
    private static final long serialVersionUID = 1L;
    private OptInfoDao dao;
    private OptDefDao optdefdao;

    public void setOptdefdao(OptDefDao pDao) {
        this.optdefdao = pDao;
    }

    public void setFunctionDao(OptInfoDao optinfodao) {
        setBaseDao(optinfodao);
        this.dao = optinfodao;
    }

    public void saveObject(FOptinfo object) {
        dao.saveObject(object);
        String sOptID = object.getOptid();
        if (!object.getOpturl().equals("...") && optdefdao.getOptDefSumByOptID(sOptID) < 1)
            optdefdao.initOptdefOfOptID(sOptID);
    }

    public List<FOptinfo> getMenuFuncByUser(FUserinfo user) {
        return dao.getMenuFuncByUserID(user.getUsercode());
    }

    @Override
    public List<FOptinfo> getMenuFuncByUserIDAndSuperFunctionId(FUserinfo user, String superFunctionId) {
        return this.dao.getMenuFuncByUserIDAndSuperFunctionId(user.getUsercode(), superFunctionId);
    }

    public List<FOptinfo> getFunctionsByUser(FUserinfo user) {
        return dao.getFunctionsByUserID(user.getUsercode());
    }

    public List<FOptinfo> getFunctionsByUserAndSuperFunctionId(FUserinfo user, String superFunctionId) {
        return dao.getFunctionsByUserAndSuperFunctionId(user.getUsercode(), superFunctionId);
    }

    public List<FOptdef> getMethodByUserAndOptID(String sUserCode, String sOptid) {
        return dao.getMethodByUserAndOptid(sUserCode, sOptid);
    }

    public List<FOptdef> getMethodByUserAndOptID(FUserinfo user, String sOptid) {
        return getMethodByUserAndOptID(user.getUsercode(), sOptid);
    }

}
