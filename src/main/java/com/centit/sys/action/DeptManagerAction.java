package com.centit.sys.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.util.StringUtils;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.dao.CodeBook;
import com.centit.core.utils.DwzResultParam;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.support.utils.Algorithm;
import com.centit.support.utils.Algorithm.ParentChild;
import com.centit.support.utils.StringBaseOpt;
import com.centit.sys.po.FOptWithPower;
import com.centit.sys.po.FRoleinfo;
import com.centit.sys.po.FRolepower;
import com.centit.sys.po.FUnitinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.po.FUserrole;
import com.centit.sys.po.FUserroleId;
import com.centit.sys.po.FUserunit;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.service.CodeRepositoryManager;
import com.centit.sys.service.CodeRepositoryUtil;
import com.centit.sys.service.SysRoleManager;
import com.centit.sys.service.SysUnitManager;
import com.centit.sys.service.SysUserManager;
import com.centit.sys.util.ISysOptLog;
import com.centit.sys.util.SysOptLogFactoryImpl;
import com.opensymphony.xwork2.ActionContext;

public class DeptManagerAction extends BaseEntityDwzAction<FUnitinfo> {

    private static final long serialVersionUID = 1L;
    private SysUnitManager sysUnitManager;
    private CodeRepositoryManager codeRepositoryManager;
    private SysRoleManager sysRoleManager;
    private SysUserManager sysUserManager;
    private List<FOptWithPower> fOptPowers;
    public static final Log log = LogFactory.getLog(DeptManagerAction.class);

    private static final ISysOptLog SYS_OPT_LOG = SysOptLogFactoryImpl.getSysOptLog("DEPTROLE");

    public void setCodeRepositoryManager(CodeRepositoryManager codeRepositoryManager) {
        this.codeRepositoryManager = codeRepositoryManager;
    }

    private List<FRoleinfo> unitRoles;
    private List<FUserrole> userroles;

    public List<FOptWithPower> getFOptPowers() {
        if (fOptPowers == null)
            fOptPowers = new ArrayList<FOptWithPower>();
        return fOptPowers;
    }

    public void setFOptPowers(List<FOptWithPower> fOptPowers) {
        this.fOptPowers = fOptPowers;
    }

    public List<FUserrole> getUserroles() {
        if (userroles == null)
            userroles = new ArrayList<FUserrole>();
        return userroles;
    }

    public void setUserroles(List<FUserrole> userroles) {
        this.userroles = userroles;
    }

    public void setSysUnitManager(SysUnitManager sysUnitManager) {
        this.sysUnitManager = sysUnitManager;
        this.setBaseEntityManager(sysUnitManager);
    }

    public void setSysRoleManager(SysRoleManager sysRoleManager) {
        this.sysRoleManager = sysRoleManager;
    }

    public void setSysUserManager(SysUserManager sysUserManager) {
        this.sysUserManager = sysUserManager;
    }

    public List<FRoleinfo> getUnitRoles() {
        if (unitRoles == null)
            unitRoles = new ArrayList<FRoleinfo>();
        return unitRoles;
    }

    public List<FOptWithPower> getAllPowers() {
        return fOptPowers;
    }

    public void setAllPowers(List<FOptWithPower> ops) {
        fOptPowers = ops;
    }

    public String listunit() {
        try {
            FUserDetail user = ((FUserDetail) getLoginUser());// .getUserinfo（）;
            FUserunit dept = sysUserManager.getUserPrimaryUnit(user.getUsercode());
            if (dept != null) {
                String sParentUnit = dept.getUnitcode();
                objList = sysUnitManager.getSubUnits(sParentUnit);

                ParentChild<FUnitinfo> c = new Algorithm.ParentChild<FUnitinfo>() {
                    public boolean parentAndChild(FUnitinfo p, FUnitinfo c) {
                        return p.getUnitcode().equals(c.getParentunit());
                    }
                };

                Algorithm.sortAsTree(objList, c);
                List<Integer> optIndex = Algorithm.makeJqueryTreeIndex(objList, c);
                JSONObject jo = new JSONObject();
                jo.put("indexes", optIndex);
                ServletActionContext.getContext().put("INDEX", jo.toString());

                if (!objList.isEmpty()) {
                    ServletActionContext.getContext().put("superUnitcode", objList.get(0).getUnitcode());
                } else ServletActionContext.getContext().put("superUnitcode", "0");
            }
            return "listunit";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }


    public String deptRoleUserAdd() {
        FUserDetail user = ((FUserDetail) getLoginUser());// .getUserinfo（）;
        FUserunit dept = sysUserManager.getUserPrimaryUnit(user.getUsercode());
        if (null == dept) {
            return ERROR;
        }
        String sParentUnit = dept.getUnitcode();
        objList = sysUnitManager.getSubUnits(sParentUnit);

        Map<String, Object> userFilterMap = new HashMap<String, Object>();
        userFilterMap.put("isvalid", "T");
        userFilterMap.put("byUnderUnit", dept.getUnitcode());
        List<FUserinfo> userinfos = sysUserManager.listUnderUnit(userFilterMap);


        List<Map<String, String>> unit = new ArrayList<Map<String, String>>();
        for (FUnitinfo unitinfo : objList) {
            Map<String, String> m = new LinkedHashMap<String, String>();
            m.put("id", unitinfo.getUnitcode());
            m.put("pId", unitinfo.getParentunit());
            m.put("name", unitinfo.getUnitname());
            m.put("open", "true");
            m.put("p", "true");

            unit.add(m);
        }
        for (FUserinfo userinfo : userinfos) {
            Map<String, String> m = new LinkedHashMap<String, String>();
            m.put("id", userinfo.getUsercode());
            m.put("pId", userinfo.getPrimaryUnit());
            m.put("name", userinfo.getUsername());
            m.put("p", "false");

            unit.add(m);
        }


        userFilterMap.remove("byUnderUnit");
        userFilterMap.put("queryByRole", (String) ServletActionContext.getRequest().getParameter("s_rolecode"));

        List<FUserinfo> chooseUserinfos = sysUserManager.listObjects(userFilterMap);

        List<String> chooseUser = new ArrayList<String>();
        for (FUserinfo userinfo : chooseUserinfos) {
            chooseUser.add(userinfo.getUsercode());
        }


        JSONArray ja = new JSONArray();
        ja.addAll(unit);

        ActionContext.getContext().put("unitJson", ja.toString());
        ActionContext.getContext().put("chooseUserJson", JSONArray.fromObject(chooseUser).toString());
        return "deptRoleUserAdd";
    }

    private Map<String, String> powerlist;

    public Map<String, String> getPowerlist() {
        if (powerlist == null)
            powerlist = new HashMap<String, String>();
        return powerlist;
    }

    public void setPowerlist(Map<String, String> powerlist) {
        this.powerlist = powerlist;
    }

    public String editDeptPower() {
        try {
            FUserDetail user = ((FUserDetail) getLoginUser());
            FUserunit dept = sysUserManager.getUserPrimaryUnit(user.getUsercode());
            String sParentUnit = dept.getUnitcode();

            FUnitinfo o = sysUnitManager.getObjectById(object.getUnitcode());
            if (o != null)
                object.copyNotNullProperty(o);

            List<FRolepower> list = sysRoleManager.getRolePowers(sParentUnit + "$" + object.getUnitcode());

            powerlist = new HashMap<String, String>();

            for (FRolepower p : list) {
                powerlist.put(p.getOptcode(), "1");
            }
            fOptPowers = sysRoleManager.getOptWithPowerUnderUnit(sParentUnit);

            ParentChild<FOptWithPower> c = new Algorithm.ParentChild<FOptWithPower>() {
                public boolean parentAndChild(FOptWithPower p, FOptWithPower c) {
                    return p.getOptid().equals(c.getPreoptid());
                }
            };

            Algorithm.sortAsTree(fOptPowers, c);
            List<Integer> optIndex = Algorithm.makeJqueryTreeIndex(fOptPowers, c);

            JSONObject jo = new JSONObject();
            jo.put("indexes", optIndex);
            ServletActionContext.getContext().put("INDEX", jo.toString());

            return "editDeptPower";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    private String[] optcodelist;

    public String[] getOptcodelist() {
        return optcodelist;
    }

    public String saveDeptPower() {
        try {
            String optContent = null;

            FUserDetail user = ((FUserDetail) getLoginUser());
            FUserunit dept = sysUserManager.getUserPrimaryUnit(user.getUsercode());
            String sParentUnit = dept.getUnitcode();

            object = sysUnitManager.getObjectById(object.getUnitcode());
            optContent = object.display();

            if (object == null || object.getUnitcode() == null) {
                return "editDeptPower";
            }
            try {
                sysRoleManager.saveRolePowers(sParentUnit + "$" + object.getUnitcode(), optcodelist);
                savedMessage();

                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), "DEPTPOW",
                        String.valueOf(object.getUnitcode()), "saveDeptPower", optContent, null);
                return "saveDeptPower";

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                saveError(e.getMessage());
                return "editDeptPower";
            }
        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }

    public void setOptcodelist(String[] optcodelist) {
        this.optcodelist = optcodelist;
    }

    private List<FRoleinfo> fRoleinfos;

    public List<FRoleinfo> getFRoleinfos() {
        return fRoleinfos;
    }

    public void setFRoleinfos(List<FRoleinfo> fRoleinfos) {
        this.fRoleinfos = fRoleinfos;
    }

    @SuppressWarnings("unchecked")
    public String listrole() {
        try {
            PageDesc pageDesc = DwzTableUtils.makePageDesc(this.request);

            Map<Object, Object> paramMap = request.getParameterMap();
            resetPageParam(paramMap);

            Map<String, Object> filterMap = convertSearchColumn(paramMap);

            FUserDetail user = ((FUserDetail) getLoginUser());
            FUserunit dept = sysUserManager.getUserPrimaryUnit(user.getUsercode());
            if (dept != null) {
                String sParentUnit = dept.getUnitcode();
                filterMap.put("PROLECODE", sParentUnit + "-%");

                fRoleinfos = sysRoleManager.listObjects(this.addFiter(filterMap), pageDesc);

            }

            this.pageDesc = pageDesc;

            return "listrole";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    private FRoleinfo roleinfo;

    public FRoleinfo getRoleinfo() {
        return roleinfo;
    }

    public void setRoleinfo(FRoleinfo roleinfo) {
        this.roleinfo = roleinfo;
    }

    public String editDeptRole() {

        try {
            FUserDetail user = ((FUserDetail) getLoginUser());
            FUserunit dept = sysUserManager.getUserPrimaryUnit(user.getUsercode());
            String sParentUnit = dept.getUnitcode();

            if (roleinfo != null) {
                FRoleinfo dbobject = sysRoleManager.getObjectById(roleinfo.getRolecode());

                if (dbobject != null) {
                    roleinfo = dbobject;
                }

                List<FRolepower> list = sysRoleManager.getRolePowers(roleinfo.getRolecode());
                powerlist = new HashMap<String, String>();
                for (FRolepower p : list) {
                    powerlist.put(p.getOptcode(), "1");// p.getRolecode());
                }
            }
            fOptPowers = sysRoleManager.getOptWithPowerUnderUnit(sParentUnit);

            this.convertTree();


            return "editDeptRole";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    private void convertTree() {
        ParentChild<FOptWithPower> c = new Algorithm.ParentChild<FOptWithPower>() {

            @Override
            public boolean parentAndChild(FOptWithPower p, FOptWithPower c) {

                //对业务操作名进行拼音顺序排序
                Collections.sort(p.getPowerlist(), new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        String s1 = CodeRepositoryUtil.getValue("optcode", o1);
                        String s2 = CodeRepositoryUtil.getValue("optcode", o2);
                        if (StringUtils.hasText(s1) && StringUtils.hasText(s2)) {
                            return StringBaseOpt.getFirstLetter(s1).compareTo(StringBaseOpt.getFirstLetter(s2));
                        }
                        return 1;
                    }
                });

                return p.getOptid().equals(c.getPreoptid());
            }

        };

        Algorithm.sortAsTree(fOptPowers, c);
        List<Integer> optIndex = Algorithm.makeJqueryTreeIndex(fOptPowers, c);
        JSONObject jo = new JSONObject();
        jo.put("indexes", optIndex);
        ServletActionContext.getContext().put("INDEX", jo.toString());
    }

    public String builtDeptRole() {
        try {
            FUserDetail user = ((FUserDetail) getLoginUser());
            FUserunit dept = sysUserManager.getUserPrimaryUnit(user.getUsercode());
            if (dept != null) {
                String sParentUnit = dept.getUnitcode();

                roleinfo = new FRoleinfo();
                // List<FRolepower> list = sysRoleManager.getRolePowers(roleinfo.getRolecode());
                powerlist = new HashMap<String, String>();
                // for(FRolepower p:list){
                // powerlist.put(p.getOptcode(), "1");//p.getRolecode());
                // }
                fOptPowers = sysRoleManager.getOptWithPowerUnderUnit(sParentUnit);

                this.convertTree();
            }
            return "editDeptRole";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    private String[] optlist;

    public String[] getOptlist() {
        return optlist;
    }

    public void setOptlist(String[] optlist) {
        this.optlist = optlist;
    }

    public String saveDeptRole() {

        try {
            String optContent = null;
            String oldValue = null;

            FUserDetail user = ((FUserDetail) getLoginUser());
            FUserunit dept = sysUserManager.getUserPrimaryUnit(user.getUsercode());
            if (dept == null) {
                saveError("dept is null!");
                return ERROR;
            }
            String sParentUnit = dept.getUnitcode();

            if (roleinfo.getRolecode() == null) {
                return "savedrole";
            }
            // 检测角色代码前缀
            try {
                if (!roleinfo.getRolecode().startsWith(sParentUnit + "-")) {

                    roleinfo.setRolecode(sParentUnit + "-" + roleinfo.getRolecode());
                }
                FRoleinfo dbRoleinfo = sysRoleManager.getObjectById(roleinfo.getRolecode());

                if (dbRoleinfo != null) {
                    oldValue = dbRoleinfo.display();
                    optContent = roleinfo.display();
                    // 更新新值
                    dbRoleinfo.copyNotNullProperty(roleinfo);
                    // dbRoleinfo.setIsvalid("T");
                    sysRoleManager.saveObject(dbRoleinfo);
                    sysRoleManager.saveRolePowers(dbRoleinfo.getRolecode(), optlist);
                    // roleinfo = dbRoleinfo;

                } else {
                    optContent = roleinfo.display();

                    if (roleinfo.getIsvalid() == null)
                        roleinfo.setIsvalid("T");
                    sysRoleManager.saveObject(roleinfo);
                    sysRoleManager.saveRolePowers(roleinfo.getRolecode(), optlist);
                    roleinfo = new FRoleinfo();
                }
                // 刷新系统中的缓存
                codeRepositoryManager.refresh("rolecode");
                savedMessage();

                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), roleinfo.getRolecode(), optContent, oldValue);

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                saveError(e.getMessage());
                return ERROR;
            }
            return "saveDeptRole";
        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }

    public String deleteDeptRole() {
        try {

            try {
                FRoleinfo dbObject = sysRoleManager.getObjectById(roleinfo.getRolecode());

                String optContent = dbObject.display();
                String tagId = dbObject.getRolecode();
                sysRoleManager.disableObject(dbObject);
                deletedMessage();
                // 刷新系统中的缓存
                codeRepositoryManager.refresh("rolecode");

                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), tagId, optContent);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return "saveDeptRole";
            }
            return "deleteDeptRole";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String renewDeptRole() {

        try {
            FRoleinfo dbObject = sysRoleManager.getObjectById(roleinfo.getRolecode());
            try {

                sysRoleManager.renewObject(dbObject);
                // 刷新系统中的缓存
                codeRepositoryManager.refresh("rolecode");
                renewedMessage();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                // saveDirectlyError( e.getMessage());

                return "saveDeptRole";
            }

            return "renewDeptRole";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    private List<FUserinfo> userList;

    public List<FUserinfo> getUserList() {
        return userList;
    }

    public void setUserList(List<FUserinfo> userList) {
        this.userList = userList;
    }

    @SuppressWarnings("unchecked")
    public String listuser() {
        try {
            PageDesc pageDesc = DwzTableUtils.makePageDesc(this.request);

            Map<Object, Object> paramMap = (Map<Object, Object>) request.getParameterMap();
            resetPageParam(paramMap);

            if (paramMap.get("unitcode") != null) {
                FUserDetail user = ((FUserDetail) getLoginUser());
                FUserunit dept = sysUserManager.getUserPrimaryUnit(user.getUsercode());

                if (dept != null) {
                    String sParentUnit = dept.getUnitcode();
//                    Map<String,Object> filter = new HashMap<String,Object>();

                    Map<String, Object> filterMap = convertSearchColumn(paramMap);

                    userList = sysUnitManager.getRelationUsers(sParentUnit, this.addFiter(filterMap), pageDesc);

                    //pageDesc.setTotalRows(userList.size());
                }
            } else {
                Map<String, Object> filterMap = convertSearchColumn(paramMap);
                userList = sysUserManager.listObjects(filterMap, pageDesc);
            }
            userList = (userList == null ? new ArrayList<FUserinfo>() : userList);

            this.pageDesc = pageDesc;

            return "listuser";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public Map<String, Object> addFiter(Map<String, Object> filter) {

        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");

        if (StringUtils.hasText(orderField) && StringUtils.hasText(orderDirection)) {
            filter.put(CodeBook.SELF_ORDER_BY, orderField + " " + orderDirection);
        }

        return filter;
    }

    private FUserinfo userinfo;

    public FUserinfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(FUserinfo userinfo) {
        this.userinfo = userinfo;
    }

    public String viewUserRole() {
        super.pageDesc = DwzTableUtils.makePageDesc(request);

        try {

            FUserinfo dbobject = sysUserManager.getObjectById(userinfo.getUsercode());
            if (dbobject == null) {
                saveError("entity.missing");
                return LIST;
            }
            userinfo = dbobject;

            FUserDetail user = ((FUserDetail) getLoginUser());
            FUserunit dept = sysUserManager.getUserPrimaryUnit(user.getUsercode());
            if (dept != null) {
                String sParentUnit = dept.getUnitcode();
                userroles = sysUserManager.getAllUserRoles(userinfo.getUsercode(), sParentUnit + "-");
                super.pageDesc.setTotalRows(userroles.size());
            }
            return "viewUserRole";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    // 用户角色设定
    private FUserrole userrole; // 用户角色

    public FUserrole getUserrole() {
        return userrole;
    }

    public void setUserrole(FUserrole userrole) {
        this.userrole = userrole;
    }

    private String roleprefix;

    public String getRoleprefix() {
        return roleprefix;
    }

    public void setRoleprefix(String roleprefix) {
        this.roleprefix = roleprefix;
    }

    public String bulitUserRole() {
        try {
            FUserroleId id = new FUserroleId();
            id.setUsercode(userrole.getUsercode());
            userrole = new FUserrole();
            id.setObtaindateToToday();
            userrole.setId(id);

            FUserDetail user = ((FUserDetail) getLoginUser());
            FUserunit dept = sysUserManager.getUserPrimaryUnit(user.getUsercode());
            if (dept != null) {
                String sParentUnit = dept.getUnitcode();
                roleprefix = sParentUnit + "-";
            }
            return "editUserRole";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String editUserRole() {
        try {
            FUserroleId id = new FUserroleId();
            id.setUsercode(userrole.getUsercode());
            id.setRolecode(userrole.getRolecode());
            userrole = sysUserManager.getValidUserrole(id.getUsercode(), id.getRolecode());
            FUserDetail user = ((FUserDetail) getLoginUser());
            FUserunit dept = sysUserManager.getUserPrimaryUnit(user.getUsercode());
            if (dept != null) {
                String sParentUnit = dept.getUnitcode();
                roleprefix = sParentUnit + "-";
            }
            return "editUserRole";
        } catch (Exception e) {
            e.printStackTrace();
            return (ERROR);
        }
    }

    public String saveUserRole() {
        try {
            String optContent = null;
            String oldValue = null;

            FUserrole desobj = sysUserManager.getValidUserrole(userrole.getUsercode(), userrole.getRolecode());
            if (desobj != null) {
                oldValue = desobj.display();

                desobj.copyNotNullProperty(userrole);
                userrole = desobj;
            }
            try {
                log.debug(userrole);
                sysUserManager.saveUserrole(userrole);
                savedMessage();

                optContent = userrole.display();

                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), "DEPTUR", userrole.getId().toString(), "saveUserRole", optContent,
                        oldValue);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return ERROR;
            }

            super.dwzResultParam = new DwzResultParam();
            super.dwzResultParam.setNavTabId(request.getParameter("tabid"));
            return "saveUserRole";
        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }

    public String deleteUserRole() {
        try {
            String optContent = null;

            userrole = sysUserManager.getFUserroleByID(userrole.getId());

            optContent = userrole.display();
            String tagId = String.valueOf(userrole.getId());
            sysUserManager.deleteUserrole(userrole);
            deletedMessage();

            SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), tagId, optContent);
            return "deleteUserRole";

        } catch (Exception ee) {
            ee.printStackTrace();
            return (ERROR);
        }
    }
}
