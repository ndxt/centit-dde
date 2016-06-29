package com.centit.sys.service;

import java.util.List;

import com.centit.core.service.BaseEntityManager;
import com.centit.sys.po.FOptWithPower;
import com.centit.sys.po.FRoleinfo;
import com.centit.sys.po.FRolepower;

public interface SysRoleManager extends BaseEntityManager<FRoleinfo> {

    public List<FRolepower> getRolePowers(String rolecode); // 角色操作权限

    public void saveRolePowers(List<FRolepower> rolePowers); // 1对1的操作权限保存

    public void saveRolePowers(String rolecode, String[] powerCodes); // 1对多的操作权限保存

    public List<FOptWithPower> getOptWithPowerUnderUnit(String sUnitCode); // 各种角色代码获得角色说拥有的业务


}
