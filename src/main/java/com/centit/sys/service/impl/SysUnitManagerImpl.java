package com.centit.sys.service.impl;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.core.utils.PageDesc;
import com.centit.support.utils.Algorithm;
import com.centit.support.utils.Algorithm.ParentChild;
import com.centit.sys.dao.UnitInfoDao;
import com.centit.sys.dao.UserUnitDao;
import com.centit.sys.po.FUnitinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.po.FUserunit;
import com.centit.sys.po.FUserunitId;
import com.centit.sys.service.SysUnitManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysUnitManagerImpl extends BaseEntityManagerImpl<FUnitinfo>
        implements SysUnitManager {
    private static final long serialVersionUID = 1L;
    private UnitInfoDao sysunitdao;
    private UserUnitDao unituserDao;

    public void setSysunitdao(UnitInfoDao unitdao) {
        setBaseDao(unitdao);
        this.sysunitdao = unitdao;
    }

    public void setUnituserDao(UserUnitDao userunitdao) {
        this.unituserDao = userunitdao;
    }

    public List<FUnitinfo> getSubUnits(String superUnitID) {
        return sysunitdao.getSubUnits(superUnitID);

    }

    public List<FUnitinfo> getAllSubUnits(String superUnitID) {
        return sysunitdao.getAllSubUnits(superUnitID);
    }

    public List<Object> getUnits(String superUnitID) {
        List<FUnitinfo> unitinfo = new ArrayList<FUnitinfo>();
        unitinfo = sysunitdao.getUnits(superUnitID);
        int i = 0;

        ParentChild<FUnitinfo> c = new Algorithm.ParentChild<FUnitinfo>() {
            public boolean parentAndChild(FUnitinfo p, FUnitinfo c) {
                return p.getUnitcode().equals(c.getParentunit());
            }
        };
        Algorithm.sortAsTree(unitinfo, c);

        List<Object> unitinfos = new ArrayList<Object>();

        for (FUnitinfo info : unitinfo) {
            Map<String, String> map = new HashMap<String, String>();
            if (i == 0) {
                map.put("id", info.getUnitcode());
                map.put("pId", info.getParentunit());
                map.put("name", "员工");
                i++;

            } else {
                map.put("id", info.getUnitcode());
                map.put("pId", info.getParentunit());
                map.put("name", info.getUnitname());
            }
            unitinfos.add(map);
        }


        return unitinfos;
    }

    public List<String> getAllUnitCodes(String unitcode) {
        return this.sysunitdao.getAllUnitCodes(unitcode);
    }

    /**
     * 查找对象，如果没有新建一个空对象，并附一个默认的编码
     */
    public FUnitinfo getObject(FUnitinfo object) {
        FUnitinfo newObj = sysunitdao.getObjectById(object.getUnitcode());
        if (newObj == null) {
            newObj = object;
            newObj.setUnitcode(sysunitdao.getNextKey());
            newObj.setIsvalid("T");
        }
        return newObj;
    }

    public List<FUserunit> getSysUnitsByUserId(String userCode) {
        return sysunitdao.getSysUnitsByUserId(userCode);
    }

    public List<FUserunit> getSysUsersByUnitId(String unitCode) {
        return sysunitdao.getSysUsersByUnitId(unitCode);
    }

    public List<FUserunit> getSysUsersByUnitId(String unitCode, PageDesc pageDesc, Map<String, Object> filterMap) {
        return unituserDao.getSysUsersByUnitId(unitCode, pageDesc, filterMap);
    }


    public List<FUserinfo> getUnitUsers(String unitCode) {
        return sysunitdao.getUnitUsers(unitCode);
    }

    public List<FUserinfo> getRelationUsers(String unitCode, Map<String, Object> filter, PageDesc pageDesc) {
        return sysunitdao.getRelationUsers(unitCode, filter, pageDesc);
    }

    public FUserunit findUnitUserById(FUserunitId id) {
        return unituserDao.getObjectById(id);
    }

    public void saveUnitUser(FUserunit object) {
        unituserDao.saveObject(object);

    }

    public String getNextKey() {
        String sKey = "00000000000" + sysunitdao.getNextKey();
        return "d" + sKey.substring(sKey.length() - 5);
    }

    public void deleteUnitUser(FUserunitId id) {
        unituserDao.deleteObjectById(id);

    }

    public List<FUserunit> getSysUsersByRoleAndUnit(String roleType,
                                                    String roleCode, String unitCode) {
        return unituserDao.getSysUsersByRoleAndUnit(roleType, roleCode,
                unitCode);
    }

}
