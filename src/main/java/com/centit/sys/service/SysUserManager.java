package com.centit.sys.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.centit.core.service.BaseEntityManager;
import com.centit.core.utils.PageDesc;
import com.centit.sys.po.FRoleinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.po.FUserrole;
import com.centit.sys.po.FUserroleId;
import com.centit.sys.po.FUserunit;
import com.centit.sys.po.FUserunitId;
import com.centit.sys.po.Usersetting;
import com.centit.sys.security.FUserDetail;

public interface SysUserManager extends BaseEntityManager<FUserinfo> {


    public Collection<GrantedAuthority> loadUserAuthorities(String username);


    public FUserDetail loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException;

    /**
     * 获取用户JSON数据
     *
     * @return
     */
    public String getJSONUsers();

    public void resetPwd(String usid);

    public FUserrole getFUserroleByID(FUserroleId id);

    public List<FRoleinfo> getSysRolesByUsid(String usercode);

    public List<FUserrole> getUserRoles(String usercode, String rolePrefix);

    public List<FUserrole> getAllUserRoles(String usercode, String rolePrefix);

    public FUserrole getValidUserrole(String usercode, String rolecode);

    public int deleteUserrole(String usercode, String rolecode);

    public int deleteUserrole(FUserrole userrole);

    public void saveUserrole(FUserrole userrole);

    public String encodePassword(String password, String usercode);

    public void setNewPassword(String userID, String oldPassword,
                               String newPassword);

    public List<FUserunit> getSysUnitsByUserId(String userCode);

    public FUserunit getUserPrimaryUnit(String userId);

    public FUserunit findUserUnitById(FUserunitId id);
    //public void saveUserWithPrimaryUnit(FUserinfo userinfo, FUserunit unitinfo);

    public void saveUserUnit(FUserinfo userinfo, FUserunit userunit);

    public void saveUserUnit(FUserunit userunit);

    void saveUserUnit(FUserunit object, FUserunit oldObject);

    public void deleteUserUnit(FUserunitId id);

    public String getNextUserCode(char cType);

    public Usersetting getUserSetting(String usercode);

    public FUserunit getUserunitByUserid(String userid);

    public void saveUserUnitFromXc(FUserunit object);

    //
    public List<FUserinfo> listUnderUnit(Map<String, Object> filterMap, PageDesc pageDesc);

    public List<FUserinfo> listUnderUnit(Map<String, Object> filterMap);

    public List<FUserinfo> getUserUnderUnit(String unitcode);


    void saveBatchUserRole(String rolecode, List<String> usercode);


    FUserinfo getUserByLoginname(String loginname);
}
