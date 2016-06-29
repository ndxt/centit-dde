package com.centit.sys.action;

//import com.centit.app.po.AddressBook;
//import com.centit.app.service.AddressBookManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.centit.core.utils.LabelValueBean;
import com.centit.core.utils.PageDesc;
import com.centit.support.utils.Algorithm;
import com.centit.support.utils.Algorithm.ParentChild;
import com.centit.sys.po.AddressBook;
import com.centit.sys.po.FOptinfo;
import com.centit.sys.po.FRolepower;
import com.centit.sys.po.FUnitinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.po.FUserunit;
import com.centit.sys.po.FUserunitId;
import com.centit.sys.security.FUserDetail;
import com.centit.sys.service.AddressBookManager;
import com.centit.sys.service.CodeRepositoryManager;
import com.centit.sys.service.CodeRepositoryUtil;
import com.centit.sys.service.SysRoleManager;
import com.centit.sys.service.SysUnitManager;
import com.centit.sys.service.SysUserManager;
import com.centit.sys.util.ISysOptLog;
import com.centit.sys.util.SysOptLogFactoryImpl;

public class UnitAction extends BaseEntityDwzAction<FUnitinfo> {
    private static final long serialVersionUID = 1L;
    private SysUnitManager sysUnitManager;
    private CodeRepositoryManager codeRepositoryManager;
    private SysRoleManager sysRoleManager;
    private AddressBookManager addressBookMag;
    private SysUserManager sysUserMgr;
    private List<FUserunit> unitusers;
    private Integer totalRows;
    private List<FOptinfo> fOptinfos;
    private FUserunit userunit;
    private List<LabelValueBean> unitLabelValueBeans;
    private List<FUserinfo> userinfoList;
    public static final Log log = LogFactory.getLog(UnitAction.class);

    private static final ISysOptLog SYS_OPT_LOG = SysOptLogFactoryImpl.getSysOptLog("UNITMAG");

    public List<FUserinfo> getUserinfoList() {
        return userinfoList;
    }

    public void setUserinfoList(List<FUserinfo> userinfoList) {
        this.userinfoList = userinfoList;
    }

    public void setSysUserMgr(SysUserManager sysUserMgr) {
        this.sysUserMgr = sysUserMgr;
    }

    public List<LabelValueBean> getUnitLabelValueBeans() {
        return unitLabelValueBeans;
    }

    public void setUnitLabelValueBeans(List<LabelValueBean> unitLabelValueBeans) {
        this.unitLabelValueBeans = unitLabelValueBeans;
    }

    public void setSysRoleManager(SysRoleManager sysRoleManager) {
        this.sysRoleManager = sysRoleManager;
    }

    public FUserunit getUserunit() {
        return userunit;
    }

    public void setUserunit(FUserunit userunit) {
        this.userunit = userunit;
    }

    public void setCodeRepositoryManager(CodeRepositoryManager codeRepositoryManager) {
        this.codeRepositoryManager = codeRepositoryManager;
    }

    public void setAddressBookMag(AddressBookManager addressBookMag) {
        this.addressBookMag = addressBookMag;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public List<FOptinfo> getFOptinfos() {
        return fOptinfos;
    }

    public void setFOptinfos(List<FOptinfo> fOptinfos) {
        this.fOptinfos = fOptinfos;
    }

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public List<FUserunit> getUnitusers() {
        return unitusers;
    }

    public void setUnitusers(List<FUserunit> unitusers) {
        this.unitusers = unitusers;
    }

    public void setAddressBookManager(AddressBookManager basemgr) {
        addressBookMag = basemgr;
    }

    public void setSysUnitManager(SysUnitManager sysUnitManager) {
        this.sysUnitManager = sysUnitManager;
        setBaseEntityManager(sysUnitManager);
    }

    public String list() {
        try {
            //FUserDetail uinfo = ((FUserDetail) getLoginUser());

//            objList = sysUnitManager.getSubUnits(uinfo.getPrimaryUnit());
            objList = sysUnitManager.listObjects();

            ParentChild<FUnitinfo> c = new Algorithm.ParentChild<FUnitinfo>() {

                @Override
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

            return LIST;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String view() {
        try {
            PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
            object = sysUnitManager.getObjectById(object.getUnitcode());

            Map<String, Object> filterMap = new HashMap<String, Object>();
            String orderField = request.getParameter("orderField");
            String orderDirection = request.getParameter("orderDirection");

            if (StringUtils.hasText(orderField) && StringUtils.hasText(orderDirection)) {
                filterMap.put(CodeBook.SELF_ORDER_BY, orderField + " " + orderDirection);
            }
            unitusers = sysUnitManager.getSysUsersByUnitId(object.getUnitcode(), pageDesc, filterMap);

            super.pageDesc = pageDesc;
            return VIEW;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String edit() {
        try {

            FUserDetail uinfo = ((FUserDetail) getLoginUser());

            // 只能编辑主机构下的机构
            unitLabelValueBeans = convertList2LabelValueBeanUnit(sysUnitManager.getSubUnits(uinfo.getPrimaryUnit()));

            FUnitinfo o = sysUnitManager.getObject(object);
            if (o != null)
                // 将对象o copy给object，object自己的属性会保留
                object.copy(o);

            ServletActionContext.getContext().put("edit", "1");


            List<FUnitinfo> unitinfos = unitNames(object.getUnitcode(), request.getParameter("superUnitcode"));

            ServletActionContext.getContext().put("unitNames", unitinfos);
            ServletActionContext.getContext().put("unitNamesJson", JSONArray.fromObject(unitinfos).toString());
            return EDIT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public List<FUnitinfo> unitNames(String unitcode, String superUnitcode) {

        List<FUnitinfo> allUnits = sysUnitManager.getAllSubUnits(superUnitcode);
        List<FUnitinfo> subUnits = sysUnitManager.getAllSubUnits(unitcode);
        List<FUnitinfo> units = new ArrayList<FUnitinfo>();

        for (FUnitinfo obj : allUnits) {
            Map<String, String> name = new HashMap<String, String>();

            // 新增时列出所有的机构名,编辑时只添加除本身及其下属的机构
            //if ( !unitcode.equals(obj.getUnitcode()) && !subUnits.contains(obj)) {
            if (!unitcode.equals(obj.getUnitcode()) && !subUnits.contains(obj)) {

                //name.put("code", obj.getUnitcode());
                //name.put("name", obj.getUnitname());
                //unitNames.add(name);

                units.add(obj);
            }
        }
//        return unitNames;
        return units;
    }

    public String builtNext() {
        try {
            FUnitinfo o = new FUnitinfo();
            o.setParentunit(object.getParentunit());
            sysUnitManager.copyObject(object, o);
            object.setUnitcode(sysUnitManager.getNextKey());
            return EDIT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    private Map<String, String> powerlist;

    public Map<String, String> getPowerlist() {
        return powerlist;
    }

    public void setPowerlist(Map<String, String> powerlist) {
        this.powerlist = powerlist;
    }

    public String editUnitPower() {

        try {

            FUnitinfo dbobject = sysUnitManager.getObjectById(object.getUnitcode());
            object = dbobject;
            List<FRolepower> list = sysRoleManager.getRolePowers("G$" + object.getUnitcode());

            powerlist = new HashMap<String, String>();
            for (FRolepower p : list) {
                powerlist.put(p.getOptcode(), "1");
            }
            fOptinfos = CodeRepositoryUtil.getOptinfoList("R");

            ParentChild<FOptinfo> c = new Algorithm.ParentChild<FOptinfo>() {
                public boolean parentAndChild(FOptinfo p, FOptinfo c) {
                    return p.getOptid().equals(c.getPreoptid());
                }
            };

            Algorithm.sortAsTree(fOptinfos, c);
            List<Integer> optIndex = Algorithm.makeJqueryTreeIndex(fOptinfos, c);

            JSONObject jo = new JSONObject();
            jo.put("indexes", optIndex);

            ServletActionContext.getContext().put("INDEX", jo.toString());

            return "editUnitPower";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String[] optcodelist;

    public String[] getOptcodelist() {
        return optcodelist;
    }

    public void setOptcodelist(String[] optcodelist) {
        this.optcodelist = optcodelist;
    }

    public String saveUnitPower() {
        try {

            sysRoleManager.saveRolePowers("G$" + object.getUnitcode(), optcodelist);
            savedMessage();

            SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), object.getUnitcode(), "更新 ["
                    + CodeRepositoryUtil.getValue("unitcode", object.getUnitcode()) + "] 机构权限信息");
            return SUCCESS;
        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }

    private String usercode;

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String deleteUnitUser() {

        try {
            sysUnitManager.deleteUnitUser(userunit.getId());
            deletedMessage();
            object = sysUnitManager.getObjectById(userunit.getUnitcode());

            this.dwzResultParam = new DwzResultParam("/sys/unit!view.do" + "?unitcode=" + userunit.getUnitcode());
            SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), object.getUnitcode(),
                    "删除 [" + CodeRepositoryUtil.getValue("unitcode", userunit.getUnitcode()) + "] 机构用户 ["
                            + CodeRepositoryUtil.getValue("usercode", userunit.getUsercode()) + "]");

            return "reView";

        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String builtUnitUser() {
        FUserDetail uinfo = ((FUserDetail) getLoginUser());

        // 只能编辑主机构下的机构
        userinfoList = sysUserMgr.getUserUnderUnit(uinfo.getPrimaryUnit());

        return "editUnitUser";
    }

    public String editUnitUser() {

        try {
            FUserunitId id = new FUserunitId();
            id.setUnitcode(object.getUnitcode());
            id.setUsercode(usercode);
            FUserunit dbobject = sysUnitManager.findUnitUserById(id);
            if (dbobject == null) {
                dbobject = new FUserunit();
                dbobject.setId(id);
            }
            userunit = dbobject;

            return "editUnitUser";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String saveUnitUser() {

        try {
            String optContent = null;
            String oldValue = null;

            FUserunit dbobject = sysUnitManager.findUnitUserById(userunit.getId());

            if (dbobject != null) {
                this.setEorroMessage("你保存的用户已存在");
            } else {
                oldValue = userunit.display();

                List<FUserunit> ls = sysUnitManager.getSysUnitsByUserId(userunit.getUsercode());
                userunit.setIsprimary("T");
                for (FUserunit l : ls) {
                    if (l.getIsprimary().equals("T"))
                        userunit.setIsprimary("F");
                }
            }
            try {
                sysUnitManager.saveUnitUser(userunit);
                optContent = userunit.display();

                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), userunit.getId().toString(),
                        optContent, oldValue);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                saveError(e.getMessage());
                return "editUnitUser";
            }
            this.dwzResultParam = new DwzResultParam("/sys/unit!view.do" + "?unitcode=" + userunit.getUnitcode() + "&tabid=" + request.getParameter("tabid"));

            return "saveUnitUser";

        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }

    public String delete() {

        try {

            try {
                object = sysUnitManager.getObjectById(object.getUnitcode());

                String tagId = object.getUnitcode();

                sysUnitManager.disableObject(object);
                // 刷新内存中的单位信息
                codeRepositoryManager.refresh("unitcode");
                deletedMessage();

                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), tagId, "禁用 [" + object.getUnitname()
                        + "] 部门");
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
            try {
                object = sysUnitManager.getObjectById(object.getUnitcode());

                String tagId = object.getUnitcode();

                sysUnitManager.renewObject(object);
                renewedMessage();

                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), tagId, "启用 [" + object.getUnitname()
                        + "] 部门");
            } catch (Exception e) {

                return EDIT;
            }
            return "renew";
        } catch (Exception e) {
            return ERROR;
        }
    }

    public String save() {

        try {
            String optContent = null;
            String oldValue = null;
            String tagId = null;

            if (object != null) {
                FUnitinfo dbobject = sysUnitManager.getObjectById(object.getUnitcode());
                if (dbobject != null) {
                    oldValue = dbobject.display();
                    tagId = object.getUnitcode();

                    dbobject.copyNotNullProperty(object);
                    object = dbobject;
                }
            }
            try {
                sysUnitManager.saveObject(object);
                // 刷新内存中的单位信息
                codeRepositoryManager.refresh("unitcode");
                savedMessage();

                optContent = object.display();

                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), tagId, optContent, oldValue);

                super.dwzResultParam = new DwzResultParam();
                dwzResultParam.setNavTabId(request.getParameter("tabid"));

                return SUCCESS;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return EDIT;
            }

        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }

    public String selectUser() {

        return "selectUser";
    }

    private AddressBook addressBook;

    public AddressBook getAddressBook() {
        return addressBook;
    }

    public void setAddressBook(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    public String editAddressBook() {

        try {

            // FUnitinfo dbobject =
            // sysUnitManager.getObjectById(object.getUnitcode());
            // if (dbobject == null ) {
            // return ERROR;
            // }

            if (object.getAddrbookid() == null || object.getAddrbookid().equals(0L)) {
                Long id = addressBookMag.getNextAddressId();
                addressBook = new AddressBook();
                addressBook.setUsercode(String.valueOf(id));
                addressBook.setBodycode(object.getUnitcode());
                addressBook.setBodytype("D");
                addressBook.setRepresentation(object.getUnitname());
                object.setAddrbookid(id);
                sysUnitManager.saveObject(object);
                addressBookMag.saveObject(addressBook);
            }
            return "editAddressBook";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public JSONObject result;
    public String selectvalue;

    public String getSelectvalue() {
        return selectvalue;
    }

    public void setSelectvalue(String selectvalue) {
        this.selectvalue = selectvalue;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public List<LabelValueBean> convertList2LabelValueBeanUnit(List<FUnitinfo> units) {
        List<LabelValueBean> list = new ArrayList<LabelValueBean>();

        for (FUnitinfo unit : units) {
            list.add(new LabelValueBean(unit.getUnitname(), unit.getUnitcode()));
        }

        return list;
    }

    public List<LabelValueBean> convertList2LabelValueBeanUser(List<FUserinfo> users) {
        List<LabelValueBean> list = new ArrayList<LabelValueBean>();

        for (FUserinfo unit : users) {
            list.add(new LabelValueBean(unit.getUsername(), unit.getUsercode()));
        }

        return list;
    }

    public String getUnituser() {

        Set<FUserinfo> userList = CodeRepositoryUtil.getUnitUsers(selectvalue);
        Map<String, String> map = new HashMap<String, String>();

        for (FUserinfo userinfo : userList) {

            map.put(userinfo.getUsercode(), userinfo.getUsername());

        }
        result = JSONObject.fromObject(map);

        return "unituser";
    }

}
