package com.centit.sys.service;

import java.util.List;
import java.util.Map;

import com.centit.core.service.BaseEntityManager;
import com.centit.core.utils.PageDesc;
import com.centit.sys.po.FUnitinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.po.FUserunit;
import com.centit.sys.po.FUserunitId;

public interface SysUnitManager extends BaseEntityManager<FUnitinfo> {

    public List<FUnitinfo> getSubUnits(String superUnitID);

    public List<FUnitinfo> getAllSubUnits(String superUnitID);

    public List<FUserunit> getSysUsersByUnitId(String unitCode);

    public List<FUserunit> getSysUsersByUnitId(String unitCode, PageDesc pageDesc, Map<String, Object> filterMap);

    public List<FUserunit> getSysUnitsByUserId(String userCode);

    public List<FUserinfo> getUnitUsers(String unitCode);

    public List<FUserinfo> getRelationUsers(String unitCode, Map<String, Object> filter, PageDesc pageDesc);

    public void deleteUnitUser(FUserunitId id);

    public FUserunit findUnitUserById(FUserunitId id);

    public void saveUnitUser(FUserunit object);

    public String getNextKey();

    public List<FUserunit> getSysUsersByRoleAndUnit(String roleType,
                                                    String roleCode, String unitCode);

    public List<Object> getUnits(String superUnitID);

    public List<String> getAllUnitCodes(String unitcode);


}
