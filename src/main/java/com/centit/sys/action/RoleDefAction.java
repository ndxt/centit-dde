package com.centit.sys.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.util.StringUtils;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.dao.CodeBook;
import com.centit.core.utils.DwzResultParam;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.support.utils.Algorithm;
import com.centit.support.utils.Algorithm.ParentChild;
import com.centit.sys.po.FOptWithPower;
import com.centit.sys.po.FOptinfo;
import com.centit.sys.po.FRoleinfo;
import com.centit.sys.po.FRolepower;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.security.CentitSecurityMetadata;
import com.centit.sys.service.CodeRepositoryManager;
import com.centit.sys.service.CodeRepositoryUtil;
import com.centit.sys.service.FunctionManager;
import com.centit.sys.service.SysRoleManager;
import com.centit.sys.util.ISysOptLog;
import com.centit.sys.util.SysOptLogFactoryImpl;
import com.opensymphony.xwork2.Action;

public class RoleDefAction extends BaseEntityDwzAction<FRoleinfo> implements ServletRequestAware, Action {
    private static final long serialVersionUID = 1L;

    private static final ISysOptLog SYS_OPT_LOG = SysOptLogFactoryImpl.getSysOptLog("ROLEMAG");

    private SysRoleManager sysRoleManager;

    private CodeRepositoryManager codeRepositoryManager;
    private CentitSecurityMetadata roleSecurityMetadata;

    private List<FOptinfo> fOptinfos;

    private List<FOptWithPower> fOptPowers;

    public void setFunctionManager(FunctionManager functionManager) {
    }

    public void setFOptinfos(List<FOptinfo> fOptinfos) {
        this.fOptinfos = fOptinfos;
    }

    public List<FOptinfo> getFOptinfos() {
        return fOptinfos;
    }

    public List<FOptWithPower> getAllPowers() {
        return fOptPowers;
    }

    public void setAllPowers(List<FOptWithPower> ops) {
        fOptPowers = ops;
    }

    public SysRoleManager getSysRoleManager() {
        return sysRoleManager;
    }

    public void setSysRoleManager(SysRoleManager sysRoleManager) {
        this.sysRoleManager = sysRoleManager;
        this.setBaseEntityManager(sysRoleManager);
    }

    public void setRoleSecurityMetadata(CentitSecurityMetadata roleSecurityMetadata) {
        this.roleSecurityMetadata = roleSecurityMetadata;
    }

    public void setCodeRepositoryManager(CodeRepositoryManager codeRepositoryManager) {
        this.codeRepositoryManager = codeRepositoryManager;
    }

    // 角色权限类别
    private Map<String, String> powerlist;

    public Map<String, String> getPowerlist() {
        return powerlist;
    }

    public void setPowerlist(Map<String, String> powerlist) {
        this.powerlist = powerlist;
    }

    @SuppressWarnings("unchecked")
    public String list() {
        try {
            PageDesc pageDesc = DwzTableUtils.makePageDesc(this.request);
            Map<String, Object> filterMap = convertSearchColumn((Map<Object, Object>) request.getParameterMap());
            filterMap.put("PROLECODE", "G-%");

            String orderField = request.getParameter("orderField");
            String orderDirection = request.getParameter("orderDirection");

            if (StringUtils.hasText(orderField) && StringUtils.hasText(orderDirection)) {
                filterMap.put(CodeBook.SELF_ORDER_BY, orderField + " " + orderDirection);
            }

            objList = baseEntityManager.listObjects(filterMap, pageDesc);
            this.pageDesc = pageDesc;

            setbackSearchColumn(filterMap);
            return LIST;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String edit() {

        try {
            FRoleinfo ri = sysRoleManager.getObjectById(object.getRolecode());
            if (ri != null)
                object.copyNotNullProperty(ri);

            powerlist = new HashMap<String, String>();
            // System.out.println(object.getRolecode());
            List<FRolepower> List = sysRoleManager.getRolePowers(object.getRolecode());
            for (FRolepower p : List) {
                powerlist.put(p.getOptcode(), "1");
            }
            fOptinfos = CodeRepositoryUtil.getOptinfoList("P");

            return EDIT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String newEdit() {
        FRoleinfo ri = sysRoleManager.getObjectById(object.getRolecode());
        if (ri != null)
            object.copyNotNullProperty(ri);

        powerlist = new HashMap<String, String>();
        // System.out.println(object.getRolecode());
        List<FRolepower> List = sysRoleManager.getRolePowers(object.getRolecode());
        for (FRolepower p : List) {
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
        // fOptinfos = sortOptInfoAsTree ( CodeRepositoryUtil.getOptinfoList("R"),optIndex);

        JSONObject jo = new JSONObject();
        jo.put("indexes", optIndex);
        ServletActionContext.getContext().put("INDEX", jo.toString());

        return "newEdit";
    }

    public String save() {

        try {
            if (!StringUtils.hasText(this.object.getOptcodelist())) {
                dwzResultParam = DwzResultParam.getErrorDwzResultParam("未选择角色权限");
                return ERROR;
            }
            String[] optcodelist = this.object.getOptcodelist().split(",");
            if (0 == optcodelist.length) {
                dwzResultParam = DwzResultParam.getErrorDwzResultParam("未选择角色权限");
                return ERROR;
            }
            if (object.getRolecode() == null) {
                return EDIT;
            }

            if (!object.getRolecode().startsWith("P-")
                    && !object.getRolecode().startsWith("G-")) {
                if ("T".equals(request.getParameter("isPublic"))) {
                    object.setRolecode("P-" + object.getRolecode());

                } else {
                    object.setRolecode("G-" + object.getRolecode());
                }


            }

            if (object.getIsvalid() == null)
                object.setIsvalid("T");

            String optContent = null;
            String oldValue = null;
            try {
                FRoleinfo dbObject = sysRoleManager.getObject(object);
                if (dbObject != null) {
                    oldValue = dbObject.display();

                    dbObject.copyNotNullProperty(object);
                    object = dbObject;
                }
                sysRoleManager.saveObject(object);
                sysRoleManager.saveRolePowers(object.getRolecode(), optcodelist);
                codeRepositoryManager.refresh("rolecode");
                roleSecurityMetadata.loadRoleSecurityMetadata();
                savedMessage();

                optContent = object.display();

                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), object.getRolecode(), optContent, oldValue);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return EDIT;
            }
            return SUCCESS;
        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }

    public String delete() {
        try {
            try {

                object = sysRoleManager.getObjectById(object.getRolecode());

                String tagId = object.getRolecode();

                sysRoleManager.disableObject(object);
                codeRepositoryManager.refresh("rolecode");
                roleSecurityMetadata.loadRoleSecurityMetadata();
                deletedMessage();

                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), tagId, "禁用角色定义 [" + object.getRolename() + "]");
            } catch (Exception e) {
                saveError(e.getMessage());
                log.error(e.getMessage(), e);

            }
        } catch (Exception e) {
            saveError(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return "delete";
    }

    @Override
    public String renew() {
        SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), object.getRolecode(), "启用角色定义 [" + CodeRepositoryUtil.getValue("rolecode", object.getRolecode()) + "]");
        super.renew();
        return "delete";
    }


}
