package com.centit.sys.service.impl;

import com.centit.core.service.BaseEntityManagerImpl;
import com.centit.core.service.ObjectException;
import com.centit.core.utils.PageDesc;
import com.centit.sys.dao.*;
import com.centit.sys.po.*;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.service.CodeRepositoryUtil;
import com.centit.sys.service.SysUserManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;

import java.util.*;


public class SysUserManagerImpl extends BaseEntityManagerImpl<FUserinfo> implements
        SysUserManager, UserDetailsService
        , AuthenticationUserDetailsService {
    private static final long serialVersionUID = 1L;
    // 加密
    Md5PasswordEncoder passwordEncoder;

    private RoleInfoDao sysroledao;

    public void setSysroleDao(RoleInfoDao roledao) {
        this.sysroledao = roledao;
    }

    public void setPasswordEncoder(Md5PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encodePassword(String password, String usercode) {
        return passwordEncoder.encodePassword(password, usercode);
    }

    private String getDefaultPassword(String usercode) {
        final String defaultPassword = "000000";
        return encodePassword(defaultPassword, usercode);
    }

    private UserInfoDao sysuserdao;

    public void setSysuserdao(UserInfoDao userdao) {
        setBaseDao(userdao);
        this.sysuserdao = userdao;
    }

    private UserUnitDao unituserDao;

    public void setUnituserDao(UserUnitDao userunitdao) {
        this.unituserDao = userunitdao;
    }

    private UnitInfoDao sysunitdao;

    private UserRoleDao userRoleDao;
    private UserSettingDao userSettingdao;

    public void setSysunitdao(UnitInfoDao sysunitdao) {
        this.sysunitdao = sysunitdao;
    }

    public void setUserSettingDao(UserSettingDao usersettingdao) {
        this.userSettingdao = usersettingdao;
    }

    public void setUserRoleDao(UserRoleDao sysusrodao) {
        this.userRoleDao = sysusrodao;
    }

    private OptInfoDao daoOptinfo;

    public void setFunctionDao(OptInfoDao dao) {
        this.daoOptinfo = dao;
    }

    public List<FRoleinfo> getSysRolesByUsid(String usercode) {
        List<FRoleinfo> roles = userRoleDao.getSysRolesByUsid(usercode);
        FRoleinfo publicRole = sysroledao.getObjectById("G-public");
        if (publicRole != null)
            roles.add(publicRole);
        return roles;
    }

    public List<FUserrole> getUserRoles(String usercode, String rolePrefix) {
        return userRoleDao.getUserRolesByUsid(usercode, rolePrefix);
    }

    public List<FUserrole> getAllUserRoles(String usercode, String rolePrefix) {
        return userRoleDao.getAllUserRolesByUsid(usercode, rolePrefix);
    }

    public Collection<GrantedAuthority> loadUserAuthorities(String loginname)
            throws UsernameNotFoundException {
        FUserDetail sysuser = (FUserDetail) sysuserdao.loadUserByLoginname(loginname);
        sysuser.setSysusrodao(userRoleDao);
        List<FRoleinfo> roles = getSysRolesByUsid(sysuser.getUsercode());
        sysuser.setAuthoritiesByRoles(roles);
        return sysuser.getAuthorities();
    }

    public FUserDetail loadUserByUsername(String loginname)
            throws UsernameNotFoundException, DataAccessException {
        //TODO 发改委要求用户登录时输入用户号 不区分大小写
        FUserDetail sysuser = (FUserDetail) sysuserdao.loadUserByLoginname(loginname.toLowerCase());
        sysuser.setSysusrodao(userRoleDao);
        List<FRoleinfo> roles = getSysRolesByUsid(sysuser.getUsercode());
        List<FUserunit> usun = sysunitdao.getSysUnitsByUserId(sysuser
                .getUsercode());
        sysuser.setUserUnits(usun);
        sysuser.setUserSetting(getUserSetting(sysuser.getUsercode()));
        sysuser.setUserFuncs(daoOptinfo.getMenuFuncByUserID(sysuser.getUsercode()));

        sysuser.setAuthoritiesByRoles(roles);
        return sysuser;
    }

    public UserDetails loadUserDetails(Authentication token)
            throws UsernameNotFoundException {
        return loadUserByUsername(token.getName());
    }

    public void resetPwd(String usid) {
        FUserinfo user = sysuserdao.getObjectById(usid);
        ;
        user.setUserpin(getDefaultPassword(user.getUsercode()));
        sysuserdao.saveObject(user);
    }

    public void setNewPassword(String userID, String oldPassword,
                               String newPassword) {
        FUserinfo user = sysuserdao.getObjectById(userID);
        if (!user.getUserpin().equals(
                encodePassword(oldPassword, user.getUsercode())))
            throw new ObjectException("旧密码不正确");
        user.setUserpin(encodePassword(newPassword, user.getUsercode()));
        sysuserdao.saveObject(user);
    }

    public void saveObject(FUserinfo sysuser) {
        boolean hasExist = sysuserdao.checkIfUserExists(sysuser);// 查该登录名是不是已经被其他用户使

        if (StringUtils.isBlank(sysuser.getUsercode())) {// 新添
            //sysuser.setUsercode( getNextUserCode('u'));
            sysuser.setIsvalid("T");
            sysuser.setUserpin(getDefaultPassword(sysuser.getUsercode()));
        }
        if (!hasExist && StringUtils.isBlank(sysuser.getUserpin()))
            sysuser.setUserpin(getDefaultPassword(sysuser.getUsercode()));

        sysuserdao.saveObject(sysuser);
    }

    public FUserrole getFUserroleByID(FUserroleId id) {
        return userRoleDao.getObjectById(id);
    }

    public List<FUserunit> getSysUnitsByUserId(String userCode) {
        return sysunitdao.getSysUnitsByUserId(userCode);
    }

    public FUserunit getUserPrimaryUnit(String userId) {
        return sysunitdao.getUserPrimaryUnit(userId);
    }

    public FUserunit findUserUnitById(FUserunitId id) {
        return unituserDao.getObjectById(id);
    }

    public void saveUserUnit(FUserunit userunit) {
        //当前用户机构模式
        FDatadictionary agencyMode = getAgencyMode();

        //一对多模式,更换主机构   //多对多，删除当前主机构
        if ("O".equalsIgnoreCase(agencyMode.getDatavalue()) || "D".equalsIgnoreCase(agencyMode.getExtracode())) {
            FUserunit pUserUnit = sysunitdao.getUserPrimaryUnit(userunit.getUsercode());
            if (null != pUserUnit) {
                deleteUserUnit(pUserUnit.getId());
            }
        }
        FUserunitId id = new FUserunitId();
        id.setUsercode(userunit.getUsercode());
        id.setUnitcode(userunit.getUnitcode());
        id.setUserrank(userunit.getUserrank());
        id.setUserstation(userunit.getUserstation());
        FUserunit dbobject = findUserUnitById(id);

        if (dbobject != null) {
            dbobject.copyNotNullProperty(userunit);
            userunit = dbobject;
        }

        if (userunit.getIsprimary() != null && "T".equals(userunit.getIsprimary())) {
            sysuserdao.deleteOtherPrimaryUnit(userunit);
        }

        userunit.setIsprimary("T");


        unituserDao.saveObject(userunit);


    }

    public void saveUserUnit(FUserinfo userinfo, FUserunit userunit) {
        userinfo.setPrimaryUnit(userunit.getUnitcode());
        sysuserdao.saveObject(userinfo);


        saveUserUnit(userunit);
    }

    @Override
    public void saveUserUnit(FUserunit object, FUserunit oldObject) {
        unituserDao.deleteObject(oldObject);
        if (object.getIsprimary() != null && "T".equals(object.getIsprimary())) {
            sysuserdao.deleteOtherPrimaryUnit(object);
        }
        unituserDao.saveObject(object);
    }

    public void saveUserUnitFromXc(FUserunit object) {
        unituserDao.saveObject(object);
    }

/*	
    public void saveUserWithPrimaryUnit(FUserinfo userinfo, FUserunit unitinfo) {
		sysuserdao.saveObject(userinfo);
		sysuserdao.deleteOtherPrimaryUnit(unitinfo);
		unituserDao.saveObject(unitinfo);
	}
*/

    public void deleteUserUnit(FUserunitId id) {
        unituserDao.deleteObjectById(id);
    }

    public FUserrole getValidUserrole(String usercode, String rolecode) {
        return userRoleDao.getValidUserrole(usercode, rolecode);
    }

    public int deleteUserrole(String usercode, String rolecode)  //这个方法不需要了
    {
        FUserrole userrole = getValidUserrole(usercode, rolecode);
        return deleteUserrole(userrole);
    }

    public int deleteUserrole(FUserrole userrole) {
        if (userrole == null)
            return -1;

//        Date today = new Date(System.currentTimeMillis());

//        if (userrole.getSecededate() == null || userrole.getSecededate().before(today)) {
//            userRoleDao.deleteObject(userrole);
//            return 1;
//        } else {
//            userrole.setSecededateToToday(); //.setSecededate( java.util.Date.valueOf( (new SimpleDateFormat("yyyy-MM-dd")).format(new Date() ) ));
//            userRoleDao.saveObject(userrole);
//            return 2;
//        }

        userRoleDao.deleteObject(userrole);
        return 1;
    }

    public void saveUserrole(FUserrole userrole) {
        FUserrole desobj = getValidUserrole(userrole.getUsercode(), userrole.getRolecode());
        if (desobj != null) {
            if (desobj.getObtaindate().after(new Date(System.currentTimeMillis()))) {
                desobj.setObtaindate(userrole.getObtaindate());
            } //else if(desobj.getObtaindate().before(userrole.getObtaindate() ) )
            desobj.setChangedesc(userrole.getChangedesc());
            desobj.setSecededate(userrole.getSecededate());
        } else
            desobj = userrole;
        userRoleDao.saveObject(desobj);
    }

    public String getNextUserCode(char cType) {
        String sKey = "00000000000" + sysuserdao.getNextValueOfSequence("S_USERCODE");
        return cType + sKey.substring(sKey.length() - 7);
    }

    public Usersetting getUserSetting(String usercode) {
        Usersetting us = userSettingdao.getObjectById(usercode);
        if (us == null)
            us = userSettingdao.getObjectById("default");
        return us;
    }

    public FUserunit getUserunitByUserid(String userid) {
        return unituserDao.getUserunitByUserid(userid);
    }

    /**
     * 获取用户JSON数据
     *
     * @return
     */
    public String getJSONUsers() {
        List<FUserinfo> userList = sysuserdao.listObjects();
        JSONArray jsonArr = new JSONArray();
        for (FUserinfo userInfo : userList) {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("usercode", userInfo.getUsercode());
            jsonObj.put("username", userInfo.getUsername());
            jsonArr.add(jsonObj);
        }
        return jsonArr.toString();
    }

    /* (non-Javadoc)
     * @see com.centit.sys.service.SysUserManager#listUnderUnit(java.util.Map, com.centit.core.utils.PageDesc)
     */
    @Override
    public List<FUserinfo> listUnderUnit(Map<String, Object> filterMap, PageDesc pageDesc) {
        return sysuserdao.listUnderUnit(filterMap, pageDesc);
    }


    @Override
    public List<FUserinfo> listUnderUnit(Map<String, Object> filterMap) {
        return sysuserdao.listUnderUnit(filterMap);
    }

    /* (non-Javadoc)
         * @see com.centit.sys.service.SysUserManager#getUserUnderUnit(java.lang.String)
         */
    @Override
    public List<FUserinfo> getUserUnderUnit(String unitcode) {
        return sysuserdao.getUserUnderUnit(unitcode);
    }

    @Override
    public void saveBatchUserRole(String rolecode, List<String> usercodes) {
        usercodes = new ArrayList<String>(usercodes);

        //新添加起始时间为当前，原先数据不进行修改
        Map<String, Object> paramFilter = new HashMap<String, Object>();
        paramFilter.put("rolecode", rolecode);
        List<FUserrole> userroles = userRoleDao.listObjects(paramFilter);
        if (CollectionUtils.isEmpty(userroles)) {
            for (String usercode : usercodes) {
                FUserrole userrole = new FUserrole(new FUserroleId(usercode, rolecode, new Date()));

                userRoleDao.saveObject(userrole);
            }
        } else {
            List<FUserrole> delUsercodes = new ArrayList<FUserrole>();
            for (FUserrole userrole : userroles) {
                if (!usercodes.contains(userrole.getId().getUsercode())) {
                    //已经不存在于当前角色中
                    delUsercodes.add(userrole);
                } else {
                    //存在于当前角色中，不再进行保存
                    usercodes.remove(userrole.getId().getUsercode());
                }
            }

            if (!CollectionUtils.isEmpty(delUsercodes)) {
                userRoleDao.getHibernateTemplate().deleteAll(delUsercodes);
            }


            for (String usercode : usercodes) {
                FUserrole userrole = new FUserrole(new FUserroleId(usercode, rolecode, new Date()));

                userRoleDao.saveObject(userrole);
            }
        }

    }

    @Override
    public FUserinfo getUserByLoginname(String loginname) {
        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("LOGINNAME_EQ", loginname);
        List<FUserinfo> listObjects = listObjects(filterMap);
        if (CollectionUtils.isEmpty(listObjects) || 1 != listObjects.size()) {

            return null;
        }

        return listObjects.get(0);
    }

    /**
     * 用户，机构，一对多，多对多模式
     *
     * @return
     */
    public static FDatadictionary getAgencyMode() {
        List<FDatadictionary> sysparam = CodeRepositoryUtil.getDictionaryIgnoreD("SYSPARAM");

        for (FDatadictionary d : sysparam) {
            if ("AgencyMode".equalsIgnoreCase(d.getId().getDatacode())) {
                return d;
            }
        }

        return null;
    }
}

