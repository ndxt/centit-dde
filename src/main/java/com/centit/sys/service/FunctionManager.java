package com.centit.sys.service;

import java.util.List;

import com.centit.core.service.BaseEntityManager;
import com.centit.sys.po.FOptdef;
import com.centit.sys.po.FOptinfo;
import com.centit.sys.po.FUserinfo;

public interface FunctionManager extends BaseEntityManager<FOptinfo> {
    /**
     * @return
     */

    public List<FOptinfo> getFunctionsByUser(FUserinfo user);

    public List<FOptinfo> getMenuFuncByUser(FUserinfo user);

    List<FOptinfo> getMenuFuncByUserIDAndSuperFunctionId(FUserinfo user, String superFunctionId);

    /**
     * @param user
     * @param superFunctionId
     * @return 获得的某个功能菜单下面的某个用户可以访问的所有子菜单
     */
    public List<FOptinfo> getFunctionsByUserAndSuperFunctionId(FUserinfo user, String superFunctionId);

    public List<FOptdef> getMethodByUserAndOptID(FUserinfo user, String sOptid);

    public List<FOptdef> getMethodByUserAndOptID(String sUserCode, String sOptid);
}
