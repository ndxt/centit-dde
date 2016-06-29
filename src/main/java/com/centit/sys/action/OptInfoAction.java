package com.centit.sys.action;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzResultParam;
import com.centit.support.utils.Algorithm;
import com.centit.support.utils.Algorithm.ParentChild;
import com.centit.sys.po.FOptinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.service.CodeRepositoryManager;
import com.centit.sys.service.CodeRepositoryUtil;
import com.centit.sys.service.FunctionManager;
import com.centit.sys.service.SysRoleManager;
import com.centit.sys.util.ISysOptLog;
import com.centit.sys.util.SysOptLogFactoryImpl;

public class OptInfoAction extends BaseEntityDwzAction<FOptinfo> {

    private static final long serialVersionUID = 1L;

    private static final ISysOptLog SYS_OPT_LOG = SysOptLogFactoryImpl.getSysOptLog("OPTINFO");

    private FunctionManager functionManager;
    private SysRoleManager sysRoleManager;
    private CodeRepositoryManager codeRepositoryManager;
    private Integer totalRows;

    public Integer getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Integer totalRows) {
        this.totalRows = totalRows;
    }

    public void setSysRoleManager(SysRoleManager sysRoleManager) {
        this.sysRoleManager = sysRoleManager;
    }

    public void setCodeRepositoryManager(CodeRepositoryManager codeRepositoryManager) {
        this.codeRepositoryManager = codeRepositoryManager;
    }

    public void setFunctionManager(FunctionManager functionManager) {
        this.functionManager = functionManager;
        setBaseEntityManager(functionManager);
    }

    // 应为这个业务管理页面 不需要进行分页
    public String list() {

        try {

            objList = functionManager.listObjects();

            // totalRows= objList.size();

            ParentChild<FOptinfo> c = new Algorithm.ParentChild<FOptinfo>() {
                public boolean parentAndChild(FOptinfo p, FOptinfo c) {
                    return p.getOptid().equals(c.getPreoptid());
                }
            };

            Algorithm.sortAsTree(objList, c);
            List<Integer> optIndex = Algorithm.makeJqueryTreeIndex(objList, c);
            // fOptinfos = sortOptInfoAsTree ( CodeRepositoryUtil.getOptinfoList("R"),optIndex);
            ServletActionContext.getContext().put("INDEX", getOptIndex(optIndex));

            return "newlist";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    private static String getOptIndex(List<Integer> optIndex) {
        JSONObject jo = new JSONObject();
        jo.put("indexes", optIndex);
        return jo.toString();
    }

    public String built() {
        try {

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

            FOptinfo dbobject = functionManager.getObjectById(object.getOptid());
            if (dbobject != null) {
                oldValue = dbobject.display();

                functionManager.copyObjectNotNullProperty(dbobject, object);
                object = dbobject;

            }
            try {
                functionManager.saveObject(object);
                // 刷新系统中的缓存
                codeRepositoryManager.refresh("optid");
                savedMessage();

                optContent = object.display();

                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), object.getOptid(), optContent, oldValue);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                e.printStackTrace();
                return EDIT;
            }


            super.dwzResultParam = new DwzResultParam();
            super.dwzResultParam.setNavTabId(request.getParameter("tabid"));

            return SUCCESS;

            // return "saveSuccess";
        } catch (Exception ee) {
            ee.printStackTrace();
            return ERROR;
        }
    }

    public String delete() {
        try {

            try {
                String tagId = object.getOptid();
                functionManager.deleteObject(object);
                // 刷新系统中的缓存
                codeRepositoryManager.refresh("optid");
                deletedMessage();

                SYS_OPT_LOG.log(((FUserinfo) this.getLoginUser()).getUsercode(), tagId, "删除业务 [" + CodeRepositoryUtil.getValue("optid", object.getOptid()) + "]");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return EDIT;
            }
            // return SUCCESS;

            return "delete";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }

    }

}
