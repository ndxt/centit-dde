package com.centit.sys.service.impl;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.support.utils.StringBaseOpt;
import com.centit.sys.dao.RoleInfoDao;
import com.centit.sys.po.FOptWithPower;
import com.centit.sys.po.FRoleinfo;
import com.centit.sys.po.FRolepower;
import com.centit.sys.po.FRolepowerId;
import com.centit.sys.service.SysRoleManager;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class SysRoleManagerImpl extends BaseEntityManagerImpl<FRoleinfo>
        implements SysRoleManager {
    private static final long serialVersionUID = 1L;
    private RoleInfoDao sysroledao;

    public void setSysroleDao(RoleInfoDao roledao) {
        setBaseDao(roledao);
        this.sysroledao = roledao;
    }

    // 各种角色代码获得该角色的操作权限 1对多
    public List<FRolepower> getRolePowers(String rolecode) {
        return sysroledao.getRolePowers(rolecode);
    }

    // 保存1对1的角色操作权限表
    public void saveRolePowers(List<FRolepower> rolePowers) {
        sysroledao.saveRolePowers(rolePowers);
    }

    // 保存1对多的角色操作权限表
    public void saveRolePowers(String rolecode, String[] powerCodes) {
        if (ArrayUtils.isEmpty(powerCodes)) {
            //清空角色
            sysroledao.deleteRolePowers(rolecode);

            return;
        }

        List<FRolepower> rolePowers = new ArrayList<FRolepower>();
        for (int i = 0; i < powerCodes.length; i++) {
            if (StringBaseOpt.isNvl(powerCodes[i]))
                continue;
            FRolepower rp = new FRolepower(new FRolepowerId(rolecode,
                    powerCodes[i].trim()));
            rolePowers.add(rp);
        }
        saveRolePowers(rolePowers);
    }

    //
    public List<FOptWithPower> getOptWithPowerUnderUnit(String sUnitCode) {
        return sysroledao.getOptWithPowerUnderUnit(sUnitCode);
    }


}
