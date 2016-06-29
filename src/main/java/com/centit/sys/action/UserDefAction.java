package com.centit.sys.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.springframework.util.StringUtils;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.dao.CodeBook;
import com.centit.core.utils.DwzResultParam;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.LabelValueBean;
import com.centit.core.utils.PageDesc;
import com.centit.support.utils.Algorithm;
import com.centit.support.utils.Algorithm.ParentChild;
import com.centit.sys.po.FDatadictionary;
import com.centit.sys.po.FUnitinfo;
import com.centit.sys.po.FUserPwd;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.po.FUserrole;
import com.centit.sys.po.FUserroleId;
import com.centit.sys.po.FUserunit;
import com.centit.sys.po.FUserunitId;
import com.centit.sys.security.CaptchaImageUtil;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.service.CodeRepositoryManager;
import com.centit.sys.service.CodeRepositoryUtil;
import com.centit.sys.service.SysUnitManager;
import com.centit.sys.service.SysUserManager;
import com.centit.sys.service.impl.SysUserManagerImpl;
import com.centit.sys.util.ISysOptLog;
import com.centit.sys.util.SysOptLogFactoryImpl;
import com.opensymphony.xwork2.ActionContext;


@SuppressWarnings("unchecked")
public class UserDefAction extends BaseEntityDwzAction<FUserinfo> {
    private static final long serialVersionUID = 1L;
    public static final Log log = LogFactory.getLog(UserDefAction.class);

    private static final ISysOptLog SYS_OPT_LOG = SysOptLogFactoryImpl.getSysOptLog("USERMAG");

    private SysUserManager sysUserMgr;
    private CodeRepositoryManager codeRepositoryManager;
    private List<LabelValueBean> unitLabelValueBeans;
    private FUserPwd userMPwd;
    private SysUnitManager sysUnitManager;

    private List<FUserrole> userroles;

    private List<FUserunit> userunits;

    public void setSysUnitManager(SysUnitManager sysUnitManager) {
        this.sysUnitManager = sysUnitManager;
    }

    public List<LabelValueBean> getUnitLabelValueBeans() {
        return unitLabelValueBeans;
    }

    public void setUnitLabelValueBeans(List<LabelValueBean> unitLabelValueBeans) {
        this.unitLabelValueBeans = unitLabelValueBeans;
    }

    public List<FUserrole> getUserroles() {
        return userroles;
    }

    public void setUserroles(List<FUserrole> userroles) {
        this.userroles = userroles;
    }

    public SysUserManager getSysUserMgr() {
        return sysUserMgr;
    }

    public void setUserunits(List<FUserunit> userunits) {
        this.userunits = userunits;
    }

    public List<FUserunit> getUserunits() {
        return userunits;
    }

    public void setSysUserMgr(SysUserManager sysUserMgr) {
        this.sysUserMgr = sysUserMgr;
        this.setBaseEntityManager(sysUserMgr);
    }

    public FUserPwd getUserMPwd() {
        if (userMPwd == null)
            userMPwd = new FUserPwd();
        return userMPwd;
    }

    public void setUserMPwd(FUserPwd userMPwd) {
        this.userMPwd = userMPwd;
    }

    public void setCodeRepositoryManager(CodeRepositoryManager codeRepositoryManager) {
        this.codeRepositoryManager = codeRepositoryManager;
    }

    public void setSysuserMgr(SysUserManager sysuserMagr) {
        this.sysUserMgr = sysuserMagr;
        setBaseEntityManager(sysuserMagr);
    }

    public String list() {

        super.list();

        unitList = CodeRepositoryUtil.getAllUnits("A");

        ServletActionContext.getContext().put("unitListJson", JSONArray.fromObject(unitList).toString());

        return LIST;

        /*try {
            FUserDetail uinfo = ((FUserDetail) getLoginUser());

            FUserunit dept = sysUserMgr.getUserPrimaryUnit(uinfo.getUsercode());

            unitLabelValueBeans = convertList2LabelValueBean(sysUnitManager.getSubUnits(uinfo.getPrimaryUnit()));

            Map<Object, Object> paramMap = request.getParameterMap();
            resetPageParam(paramMap);

            String orderField = request.getParameter("orderField");
            String orderDirection = request.getParameter("orderDirection");

            Map<String, Object> filterMap = convertSearchColumn(paramMap);
            if (!StringUtils.isBlank(orderField) && !StringUtils.isBlank(orderDirection)) {
                filterMap.put(CodeBook.SELF_ORDER_BY, orderField + " " + orderDirection);
            }

            filterMap.put("queryUnderUnit", uinfo.getPrimaryUnit());



            PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
            objList = sysUserMgr.listUnderUnit(filterMap, pageDesc);
            // totalRows = pageDesc.getTotalRows();

            this.pageDesc = pageDesc;
            return LIST;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }*/

    }


    public String roleUserList() {
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        String orderField = request.getParameter("orderField");
        String orderDirection = request.getParameter("orderDirection");

        Map<String, Object> filterMap = convertSearchColumn(paramMap);

        filterMap.put("queryByRole", (String) filterMap.get("rolecode"));

        if (StringUtils.hasText(orderField) && StringUtils.hasText(orderDirection)) {

            filterMap.put(CodeBook.SELF_ORDER_BY, orderField + " " + orderDirection);
        }
        PageDesc pageDesc = makePageDesc();
        objList = baseEntityManager.listObjects(filterMap, pageDesc);

        this.pageDesc = pageDesc;

        return "roleUserDefList";
    }


    public String roleUserAdd() {
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        Map<String, Object> filterMap = convertSearchColumn(paramMap);

        filterMap.put("queryByRole", (String) filterMap.get("rolecode"));

        objList = baseEntityManager.listObjects(filterMap);
        List<String> chooseUser = new ArrayList<String>();
        for (FUserinfo userinfo : objList) {
            chooseUser.add(userinfo.getUsercode());
        }


        List<FUnitinfo> allUnits = CodeRepositoryUtil.getAllUnits("A");
        ParentChild<FUnitinfo> c = new Algorithm.ParentChild<FUnitinfo>() {

            @Override
            public boolean parentAndChild(FUnitinfo p, FUnitinfo c) {
                return p.getUnitcode().equals(c.getParentunit());
            }
        };

        Algorithm.sortAsTree(allUnits, c);


        List<FUserinfo> allUsers = CodeRepositoryUtil.getAllUsers("A");

        List<Map<String, String>> unit = new ArrayList<Map<String, String>>();
        for (FUnitinfo unitinfo : allUnits) {
            Map<String, String> m = new LinkedHashMap<String, String>();
            m.put("id", unitinfo.getUnitcode());
            m.put("pId", unitinfo.getParentunit());
            m.put("name", unitinfo.getUnitname());
            m.put("open", "true");
            m.put("p", "true");

            unit.add(m);
        }
        for (FUserinfo userinfo : allUsers) {
            Map<String, String> m = new LinkedHashMap<String, String>();
            m.put("id", userinfo.getUsercode());
            m.put("pId", userinfo.getPrimaryUnit());
            m.put("name", userinfo.getUsername());
            m.put("p", "false");

            unit.add(m);
        }

        JSONArray ja = new JSONArray();
        ja.addAll(unit);

        ActionContext.getContext().put("unitJson", ja.toString());
        ActionContext.getContext().put("chooseUserJson", JSONArray.fromObject(chooseUser).toString());


        return "roleUserAdd";
    }

    public String roleUserSave() {
        String usercode = object.getUsercode();
        String rolecode = ServletActionContext.getRequest().getParameter("s_rolecode");
        if (StringUtils.hasText(usercode) && StringUtils.hasText(rolecode)) {
            sysUserMgr.saveBatchUserRole(rolecode, Arrays.asList(usercode.split(",")));
        }

        super.dwzResultParam = new DwzResultParam();
        super.dwzResultParam.setNavTabId(request.getParameter("tabid"));

        return "roleUserSave";
    }

    public String roleUserDel() {
        String usercode = object.getUsercode();
        String rolecode = ServletActionContext.getRequest().getParameter("s_rolecode");

        sysUserMgr.deleteUserrole(usercode, rolecode);

        super.dwzResultParam = new DwzResultParam();
        super.dwzResultParam.setNavTabId(request.getParameter("tabid"));
        return "roleUserDel";
    }

    private List<FUnitinfo> unitList;

    public List<FUnitinfo> getUnitList() {
        return unitList;
    }

    public String listUserInfo() {
        try {
            FUserDetail user = ((FUserDetail) getLoginUser());//.getUserinfo（）;
            FUserunit dept = sysUserMgr.getUserPrimaryUnit(user.getUsercode());
            if (dept != null) {
                Map<Object, Object> paramMap = request.getParameterMap();
                //HttpSession session=request.getSession();
                resetPageParam(paramMap);
                Map<String, Object> filterMap = convertSearchColumn(paramMap);
                //filterMap.put("queryUnderUnit", dept.getUnitcode());
                if ("thisunit".equals(filterMap.get("byUnderUnit")) || !StringUtils.hasText((String) filterMap.get("byUnderUnit"))) {
                    filterMap.put("byUnderUnit", dept.getUnitcode());
                    filterMap.remove("queryUnderUnit");
                }
                if ("true".equals(filterMap.get("queryUnderUnit"))) {
                    filterMap.put("queryUnderUnit", dept.getUnitcode());
                    filterMap.remove("byUnderUnit");
                }
                //filterMap.put("order", "");
                PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
                objList = sysUserMgr.listUnderUnit(filterMap, pageDesc);
                super.pageDesc = pageDesc;
                //totalRows = pageDesc.getTotalRows();
                unitList = sysUnitManager.getAllSubUnits(dept.getUnitcode());
                userUnit = new FUserunit();
                userUnit.setUnitcode(dept.getUnitcode());


                ServletActionContext.getContext().put("unitListJson", JSONArray.fromObject(unitList).toString());
            }
            return "listUserInfo";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    /**
     * 返回AJAX数据
     *
     * @return
     */
    public String getUsers() {
        HttpServletResponse response = ServletActionContext.getResponse();
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print(sysUserMgr.getJSONUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * 管理员重置用户密码
     */
    public String resetpwd() {
        try {
            sysUserMgr.resetPwd(object.getUsercode());
//            savedMessage();

            dwzResultParam = new DwzResultParam();
//            dwzResultParam.setNavTabId("external_USERMAG");
//            dwzResultParam.setCallbackType("closeCurrent");
            dwzResultParam.setMessage("已重置密码");

            return "resetpwd";
        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }

    public Integer uu_totalRows;
    public Integer ur_totalRows;

    public Integer getUr_totalRows() {
        return ur_totalRows;
    }

    public void setUr_totalRows(Integer ur_totalRows) {
        this.ur_totalRows = ur_totalRows;
    }

    public Integer getUu_totalRows() {
        return uu_totalRows;
    }

    public void setUu_totalRows(Integer uu_totalRows) {
        this.uu_totalRows = uu_totalRows;
    }

    @Override
    public String view() {

        try {
            FUserinfo ri = sysUserMgr.getObjectById(object.getUsercode());
            if (ri != null) {
                object.copyNotNullProperty(ri);
            }
            userUnit = sysUserMgr.getUserPrimaryUnit(object.getUsercode());

            userunits = sysUserMgr.getSysUnitsByUserId(object.getUsercode());
            uu_totalRows = userunits.size();

            userroles = sysUserMgr.getAllUserRoles(object.getUsercode(), "G-");
            ur_totalRows = userroles.size();

            ServletActionContext.getContext().put("AgencyMode", SysUserManagerImpl.getAgencyMode());

            return VIEW;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }


    public String viewUnderUnit() {
        super.pageDesc = DwzTableUtils.makePageDesc(request);
        try {
            FUserDetail user = ((FUserDetail) getLoginUser());//.getUserinfo（）;
            FUserunit dept = sysUserMgr.getUserPrimaryUnit(user.getUsercode());
            FUserinfo ri = sysUserMgr.getObjectById(object.getUsercode());
            if (ri != null)
                object.copyNotNullProperty(ri);

            userunits = sysUserMgr.getSysUnitsByUserId(object.getUsercode());

            userUnit = userunits.get(0);

            pageDesc.setTotalRows(userunits.size());
            userroles = sysUserMgr.getAllUserRoles(object.getUsercode(), dept.getUnitcode() + "-");
            List<FUserrole> list = sysUserMgr.getAllUserRoles(object.getUsercode(), "P-");
            if (list != null)
                userroles.addAll(list);
            pageDesc.setTotalRows(userunits.size());
            return "viewUnderUnit";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String viewUserunits() {

        try {
            FUserinfo ri = sysUserMgr.getObjectById(object.getUsercode());
            if (ri != null) {
                object.copyNotNullProperty(ri);
            }
            userunits = sysUserMgr.getSysUnitsByUserId(object.getUsercode());

            uu_totalRows = userunits.size();

            ServletActionContext.getContext().put("AgencyMode", SysUserManagerImpl.getAgencyMode());

            return "viewUserunits";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String viewUserroles() {

        try {
            FUserinfo ri = sysUserMgr.getObjectById(object.getUsercode());
            if (ri != null) {
                object.copyNotNullProperty(ri);
            }

            userroles = sysUserMgr.getAllUserRoles(object.getUsercode(), "G-");
            ur_totalRows = userroles.size();
            return "viewUserroles";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String viewUnderUserroles() {

        try {
            FUserinfo ri = sysUserMgr.getObjectById(object.getUsercode());
            if (ri != null) {
                object.copyNotNullProperty(ri);
            }

            userroles = sysUserMgr.getAllUserRoles(object.getUsercode(), "");
            ur_totalRows = userroles.size();
            return "viewUnderUserroles";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    // 设置用户角色
    public FUserrole userrole;
    private Date obtaindate;

    public FUserrole getUserrole() {
        return userrole;
    }

    public void setUserrole(FUserrole userrole) {
        this.userrole = userrole;
    }

    public Date getObtaindate() {
        return obtaindate;
    }

    public void setObtaindate(Date obtaindate) {
        this.obtaindate = obtaindate;
    }

    public String bulitUserRole() {
        try {
            FUserroleId id = new FUserroleId();
            id.setUsercode(object.getUsercode());
            id.setObtaindateToToday();
            userrole = new FUserrole();
            userrole.setId(id);

            return "editUserRole";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String builtUnderUnit() {
        FUserDetail user = ((FUserDetail) getLoginUser());//.getUserinfo（）;
        FUserunit dept = sysUserMgr.getUserPrimaryUnit(user.getUsercode());
        object.setUsercode(sysUserMgr.getNextUserCode('u'));
        if (dept != null) {
            unitList = sysUnitManager.getAllSubUnits(dept.getUnitcode());
            userUnit = new FUserunit();
            userUnit.setUnitcode(dept.getUnitcode());
        }
        return EDIT;

    }

    public String editUserRole() {
        try {
            FUserroleId id = new FUserroleId();
            id.setUsercode(object.getUsercode());
            id.setRolecode(userrole.getRolecode());
            id.setObtaindate(obtaindate);
            // userrole=sysUserMgr.getFUserroleByID(id);
            userrole = sysUserMgr.getValidUserrole(id.getUsercode(), id.getRolecode());

            if (userrole == null) {
                userrole = new FUserrole();
                id.setObtaindateToToday(); // setObtaindate(new Date());
                userrole.setId(id);
            }

            return "editUserRole";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String saveUserRole() {
        try {
            String optContent = null;
            String oldValue = null;

            FUserrole desobj = sysUserMgr.getValidUserrole(userrole.getUsercode(), userrole.getRolecode());
            userrole.setObtaindateToToday();
            if (desobj != null) {
                oldValue = desobj.display();

                desobj.copyNotNullProperty(userrole);
                userrole = desobj;
            }
            try {
                sysUserMgr.saveUserrole(userrole);
                savedMessage();
                this.dwzResultParam = new DwzResultParam("/sys/userDef!view.do" + "?usercode="
                        + userrole.getId().getUsercode() + "&type=role");
                optContent = userrole.display();
                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), userrole.getId().toString(),
                        optContent, oldValue);
            } catch (Exception e) {
                log.error(e.getMessage(), e);

                return "editUserRole";
            }

            return "saveUserRole";

        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }

    public String deleteUserRole() {
        try {
            String optContent = "删除用户 [" + CodeRepositoryUtil.getValue("usercode", userrole.getUsercode()) + " ] 角色 ["
                    + CodeRepositoryUtil.getValue("rolecode", userrole.getRolecode()) + " ]";
            String oldValue = null;

            FUserroleId id = new FUserroleId();
            id.setUsercode(userrole.getUsercode());
            id.setRolecode(userrole.getRolecode());
            id.setObtaindate(obtaindate);

            oldValue = userrole.display();
            String tagId = id.toString();

            sysUserMgr.deleteUserrole(id.getUsercode(), id.getRolecode());
            deletedMessage();

            SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), tagId, optContent, oldValue);
//            this.dwzResultParam = new DwzResultParam("/sys/userDef!view.do" + "?usercode="
//                    + userrole.getId().getUsercode() + "&type=role");


            dwzResultParam = new DwzResultParam();
            dwzResultParam.setNavTabId(request.getParameter("tabid"));

            return "deleteUserRole";
        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }

    // 设置用户单位
    private FUserunit userUnit;

    /*
     * private String userrank; private String userstation; private String unitcode;
     */

    public String addUserUnit() {
        userUnit = new FUserunit();
        userUnit.setUsercode(object.getUsercode());
        FUserDetail uinfo = ((FUserDetail) getLoginUser());

        // 只能编辑主机构下的机构
        unitLabelValueBeans = convertList2LabelValueBean(sysUnitManager.getSubUnits(uinfo.getPrimaryUnit()));

        ServletActionContext.getContext().put("AgencyMode", SysUserManagerImpl.getAgencyMode());

        return "editUserUnit";
    }

    public String addUserUnderUnit() {
        FUserDetail user = ((FUserDetail) getLoginUser());
        FUserunit dept = sysUserMgr.getUserPrimaryUnit(user.getUsercode());
        userUnit = new FUserunit();
        userUnit.setUsercode(object.getUsercode());
        if (dept != null) {
            unitList = sysUnitManager.getAllSubUnits(dept.getUnitcode());
            userUnit.setUnitcode(dept.getUnitcode());
        }
        return "editUserUnitUnderUnit";
    }

    public String editUserUnit() {

        try {
            FUserDetail uinfo = ((FUserDetail) getLoginUser());

            // 只能编辑主机构下的机构
            unitLabelValueBeans = convertList2LabelValueBean(sysUnitManager.getSubUnits(uinfo.getPrimaryUnit()));

            FUserunitId id = new FUserunitId();
            id.setUsercode(userUnit.getUsercode());
            id.setUnitcode(userUnit.getUnitcode());
            id.setUserrank(userUnit.getUserrank());
            id.setUserstation(userUnit.getUserstation());

            userUnit = sysUserMgr.findUserUnitById(id);

            if (userUnit == null) {
                userUnit = new FUserunit();
                userUnit.setId(id);
            }


            ServletActionContext.getContext().put("AgencyMode", SysUserManagerImpl.getAgencyMode());

            return "editUserUnit";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String saveUserUnit() {
        try {
            //String optContent = null;
            //String oldValue = null;

            try {
                sysUserMgr.saveUserUnit(userUnit);

                savedMessage();
                this.dwzResultParam = new DwzResultParam("/sys/userDef!view.do" + "?usercode=" + userUnit.getUsercode()
                        + "&type=unit");
                //optContent = userUnit.display();

                //SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), tagId, optContent, oldValue);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return "editUserUnit";
            }

            codeRepositoryManager.refresh("userunit");

            return "saveUserUnit";

        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }

    public String deleteUserUnit() {
        try {
            String oldValue = userUnit.display();

            FUserunitId id = new FUserunitId();
            id.setUsercode(userUnit.getUsercode());
            id.setUnitcode(userUnit.getUnitcode());
            id.setUserrank(userUnit.getUserrank());
            id.setUserstation(userUnit.getUserstation());

            String tagId = id.toString();

            sysUserMgr.deleteUserUnit(id);
            deletedMessage();

            SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), tagId,
                    "岗位角色定义删除 [" + CodeRepositoryUtil.getValue("usercode", userUnit.getUsercode()) + "] ", oldValue);
            return "deleteUserUnit";
        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }

    public String delete() {
        try {
            try {
                object = sysUserMgr.getObjectById(object.getUsercode());

                sysUserMgr.disableObject(object);
                codeRepositoryManager.refresh("usercode");
                deletedMessage();

                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), object.getUsercode(), "禁用用户 ["
                        + object.getUsername() + " ]");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return EDIT;
            }
            return "delete";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String renew() {
        try {
            baseEntityManager.renewObject(object);

            codeRepositoryManager.refresh("usercode");

            SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), object.getUsercode(), "启用用户 ["
                    + CodeRepositoryUtil.getValue("usercode", object.getUsercode()) + " ]");
            return "delete";
        } catch (Exception e) {
            log.error(e.getMessage());
            this.saveError(e.getMessage());
            return EDIT;
        }

    }

    public String built() {
        try {
            FUserDetail uinfo = ((FUserDetail) getLoginUser());

            // 只能编辑主机构下的机构
            unitLabelValueBeans = convertList2LabelValueBean(sysUnitManager.getSubUnits(uinfo.getPrimaryUnit()));

            object = new FUserinfo();
            object.setUsercode(sysUserMgr.getNextUserCode('u'));
            userUnit = new FUserunit();
            return BUILT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String edit() {

        try {
            FUserDetail uinfo = ((FUserDetail) getLoginUser());

            // 只能编辑主机构下的机构
            unitLabelValueBeans = convertList2LabelValueBean(sysUnitManager.getSubUnits(uinfo.getPrimaryUnit()));

            if (object != null) {
                FUserinfo dbobject = sysUserMgr.getObjectById(object.getUsercode());
                if (dbobject != null) {
                    sysUserMgr.copyObjectNotNullProperty(object, dbobject);
                    userUnit = sysUserMgr.getUserPrimaryUnit(object.getUsercode());
                }
            }
            return EDIT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String editUnderUnit() {
        try {
            if (object != null) {
                FUserinfo dbobject = sysUserMgr.getObjectById(object
                        .getUsercode());
                if (dbobject != null) {
                    sysUserMgr.copyObjectNotNullProperty(object, dbobject);
                    userUnit = sysUserMgr.getUserPrimaryUnit(object
                            .getUsercode());
                    request.setAttribute("underUnit", "T");
                }
            }
            return EDIT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }

    }

    public String save() {
        try {
            String optContent = null;
            String oldValue = null;

            FUserinfo dbobject = sysUserMgr.getObjectById(object.getUsercode());
            if (dbobject != null) {
                sysUserMgr.copyObjectNotNullProperty(dbobject, object);
                object = dbobject;
            }
            userUnit.setUsercode(object.getUsercode());
            userUnit.setIsprimary("T");
            FUserunit dbuserunit = sysUserMgr.findUserUnitById(userUnit.getId());
            if (dbuserunit != null) {
                oldValue = dbuserunit.display();

                dbuserunit.copyNotNullProperty(userUnit);
                userUnit = dbuserunit;
            }
            //sysUserMgr.saveObject(object);
            sysUserMgr.saveUserUnit(object, userUnit);

            optContent = object.display() + "  " + userUnit.display();

            codeRepositoryManager.refresh("usercode");

            SYS_OPT_LOG
                    .log(((FUserinfo) this.getLoginUser()).getUsercode(), object.getUsercode(), optContent, oldValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        savedMessage();

        super.dwzResultParam = new DwzResultParam();
        dwzResultParam.setNavTabId(request.getParameter("tabid"));

        return "success";
    }

    /*
     * 用户修改自己的登录密码，1.0版本在mainframeAction中
     */
    public String modifyPwdPage() {
        getUserMPwd();
        FUserinfo ui = ((FUserDetail) getLoginUser());
        userMPwd.setLoginname(ui.getLoginname());
        return "modifyPwdPage";
    }

    public String modifypwd() {
        try {

            FUserinfo ui = ((FUserDetail) getLoginUser());
            if (!userMPwd.getNewPassword().equals(userMPwd.getConfirmPassword())) {
                saveMessage("两次输入的密码不一致，请重新输入。");
            } else {
                sysUserMgr.setNewPassword(ui.getUsercode(), userMPwd.getOldPassword(), userMPwd.getNewPassword());
            }

        } catch (Exception ee) {
            ee.printStackTrace();
            saveError("密码更新失败！");
            return "modifypwd";

        }
        return "modifypwd";

    }

    /*
     * 网页用户注册
     */

    public String registerpage() {
        getUserMPwd();
        // 将用户登录名设置为空
        sysUserMgr.clearObjectProperties(object);
        // FUserinfo ui = ((FUserDetail)getLoginUser());
        userMPwd.setLoginname("");// ui.getLoginname());
        // userPwd.setCaptchaKey(captchaImage.generateCaptchaKey());
        return "registerPage";
    }

    private InputStream imageStream;

    public InputStream getImageStream() {
        return imageStream;
    }

    public void setImageStream(InputStream is) {
        this.imageStream = is;
    }

    public String captchaimage() {
        try {
            String checkcode = CaptchaImageUtil.getRandomString();
            request.getSession().setAttribute(CaptchaImageUtil.SESSIONCHECKCODE, checkcode);

            BufferedImage img = CaptchaImageUtil.generateCaptchaImage(checkcode);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(img, "jpeg", out);
            byte[] bbuf = out.toByteArray();
            this.setImageStream(new ByteArrayInputStream(bbuf));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "captchaimage";
    }

    // private JSONObject result;

    public String register() {

        String session_checkcode = request.getSession().getAttribute(CaptchaImageUtil.SESSIONCHECKCODE).toString();
        String request_checkcode = request.getParameter(CaptchaImageUtil.REQUESTCHECKCODE).toString();
        boolean validCaptcha = session_checkcode != null && session_checkcode.equalsIgnoreCase(request_checkcode);

        if (!validCaptcha) {
            this.saveError("输入的验证码不正确！");
            return DIVERROR;// "registerPage";
        }
        if (!userMPwd.getNewPassword().equals(userMPwd.getConfirmPassword())) {
            saveMessage("两次输入的密码不一致，请重新输入。");
            return DIVERROR;
        }

        userMPwd.setLoginname(object.getLoginname());
        String pwd = userMPwd.getNewPassword();// request.getParameter("password");

        String sUC = sysUserMgr.getNextUserCode('w');
        object.setUsercode(sUC);
        object.setUserpin(sysUserMgr.encodePassword(pwd, sUC));
        object.setIsvalid("T");
        try {
            sysUserMgr.saveObject(object);
        } catch (Exception e) {
            this.saveError("您输入登录名或者email已被别人使用！");
            return DIVERROR;
        }

        FDatadictionary dItem = CodeRepositoryUtil.getDataPiece("SYSPARAM", "EnableWebUsr");

        FUserrole userrole = new FUserrole();
        String sRC = dItem.getDatavalue();
        userrole.setUsercode(sUC);
        userrole.setRolecode(sRC);
        userrole.setObtaindateToToday();
        userrole.setSecededate(null);
        userrole.setChangedesc("网络注册用户自动赋予的权限");
        sysUserMgr.saveUserrole(userrole);

        FUserunit uu = new FUserunit();
        String sUintC = dItem.getExtracode2();
        uu.setUnitcode(sUintC);
        uu.setUsercode(sUC);
        uu.setUserstation("WEB");
        uu.setIsprimary("T");
        sysUserMgr.saveUserUnit(uu);
        return "registerSuccess";
    }

    /*
     * private AddressBookManager addressBookManager; private AddressBook addressBook;
     * 
     * public AddressBook getAddressBook() { return addressBook; }
     * 
     * public void setAddressBook(AddressBook addressBook) { this.addressBook = addressBook; }
     * 
     * public void setAddressBookManager(AddressBookManager addressBookManager) { this.addressBookManager =
     * addressBookManager; }
     * 
     * public String editAddressBook() {
     * 
     * 
     * if (object.getAddrbookid()==null||object.getAddrbookid().equals(0L)) { long
     * id=addressBookManager.getNextAddressId(); addressBook=new AddressBook(); addressBook.setAddrbookid(id);
     * addressBook.setBodycode(object.getUsercode()); addressBook.setBodytype("U");
     * addressBook.setRepresentation(object.getUsername()); object.setAddrbookid(id); sysUserMgr.saveObject(object);
     * addressBookManager.saveObject(addressBook); //关联的保存？？有问题了 }
     * 
     * FUserinfo dbobject = sysUserMgr.getObjectById(object.getUsercode()); object = dbobject; return "editAddressBook";
     * }
     */
    public FUserunit getUserUnit() {
        return userUnit;
    }

    public void setUserUnit(FUserunit userUnit) {
        this.userUnit = userUnit;
    }

    public List<LabelValueBean> convertList2LabelValueBean(List<FUnitinfo> units) {
        List<LabelValueBean> list = new ArrayList<LabelValueBean>();

        for (FUnitinfo unit : units) {
            list.add(new LabelValueBean(unit.getUnitname(), unit.getUnitcode()));
        }

        return list;
    }


}
