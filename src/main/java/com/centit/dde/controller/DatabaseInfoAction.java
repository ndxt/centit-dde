package com.centit.dde.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzResultParam;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.dde.po.DatabaseInfo;
import com.centit.dde.po.OsInfo;
import com.centit.dde.service.DatabaseInfoManager;
import com.centit.dde.service.OsInfoManager;
import com.centit.support.utils.StringBaseOpt;
import com.centit.sys.security.FUserDetail;

public class DatabaseInfoAction extends BaseEntityDwzAction<DatabaseInfo> {
    private static final Log log = LogFactory.getLog(DatabaseInfoAction.class);

    private static final long serialVersionUID = 1L;

    private DatabaseInfoManager databaseInfoMag;

    private OsInfoManager osInfoManager;

    public void setOsInfoManager(OsInfoManager osInfoManager) {
        this.osInfoManager = osInfoManager;
    }

    public void setDatabaseInfoManager(DatabaseInfoManager basemgr) {
        databaseInfoMag = basemgr;
        this.setBaseEntityManager(databaseInfoMag);
    }

    @Override
    public String list() {
        try {
            Map<Object, Object> paramMap = request.getParameterMap();
            resetPageParam(paramMap);

            Map<String, Object> filterMap = convertSearchColumn(paramMap);
            PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
            objList = baseEntityManager.listObjects(filterMap, pageDesc);

            //将密码解码
            /*for (DatabaseInfo databaseInfo : objList) {
                databaseInfo.setPassword(StringBaseOpt.decryptBase64Des(databaseInfo.getPassword()));
            }*/


            this.pageDesc = pageDesc;
            return LIST;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    @Override
    public String edit() {
        try {
            FUserDetail uinfo = ((FUserDetail) getLoginUser());
            if (object == null) {
                object = getEntityClass().newInstance();
            } else {
                object = baseEntityManager.getObject(object);
                object.setCreated(uinfo.getUsercode());
                //密码解密
                String password = StringBaseOpt.decryptBase64Des(object.getPassword());
                request.setAttribute("password", password);
            }

            // 业务系统
            List<OsInfo> osinfoList = osInfoManager.listObjects();
            request.setAttribute("osinfoList", osinfoList);


            return EDIT;
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    public String add() {
        try {
            if (object == null) {
                object = getEntityClass().newInstance();
            } else {
                DatabaseInfo o = baseEntityManager.getObject(object);
                if (o != null)
                    // 将对象o copy给object，object自己的属性会保留
                    baseEntityManager.copyObject(object, o);
                else
                    baseEntityManager.clearObjectProperties(object);
            }

            // 业务系统
            List<OsInfo> osinfoList = osInfoManager.listObjects();
            request.setAttribute("osinfoList", osinfoList);

            return "add";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    @Override
    public String save() {
        try {
            FUserDetail uinfo = ((FUserDetail) getLoginUser());
            DatabaseInfo dbObject = baseEntityManager.getObject(object);
            if (dbObject != null) {
                baseEntityManager.copyObjectNotNullProperty(dbObject, object);
                object = dbObject;
            }


            object.setPassword(StringBaseOpt.encryptDesBase64(object.getPassword()));
            object.setCreated(uinfo.getUsercode());
            if (object.getCreateTime() == null) {
                object.setCreateTime(new Date());
            }
            baseEntityManager.saveObject(object);


            object.setPassword(StringBaseOpt.decryptBase64Des(object.getPassword()));

            //连接是否成功不影响保存
            if (databaseInfoMag.connectionTest(object)) {
                this.saveMessage("数据库连接测试成功");
            } else {
                dwzResultParam = new DwzResultParam();
                this.saveMessage("<span style='color:red;'>数据库连接测试失败</span>");
            }
            return SUCCESS;
        } catch (Exception e) {
            saveError(e.getMessage());
            return ERROR;
        }
    }

    public String editAndsave() {
        try {
            FUserDetail uinfo = ((FUserDetail) getLoginUser());
            object.setPassword(StringBaseOpt.decryptBase64Des(object.getPassword()));
            if (databaseInfoMag.connectionTest(object)) {
                object.setPassword(StringBaseOpt.encryptDesBase64(object.getPassword()));
                object.setCreated(uinfo.getUsercode());
                baseEntityManager.saveObject(object);
                this.saveMessage("数据库连接测试成功");
                return SUCCESS;
            } else {
                this.saveError("数据库连接测试失败");
                return ERROR;
            }
        } catch (Exception e) {
            saveError("数据库连接测试失败");
            return ERROR;
        }
    }

    public String delete() {
        super.delete();

        return "delete";
    }

    public String defDataBase() {
        if (request.getParameter("issave") != null) {
            try {
                DatabaseInfo dbObject = baseEntityManager.getObject(object);
                if (dbObject != null) {
                    baseEntityManager.copyObjectNotNullProperty(dbObject, object);
                    object = dbObject;
                }
                if (object.getType().equals("source")) {
                    object.setSourceDatabaseName(object.getDatabaseName());
                }
                if (object.getType().equals("dest")) {
                    object.setGoalDatabaseName(object.getDatabaseName());
                }
                baseEntityManager.saveObject(object);
                savedMessage();
                return "defDataBase";
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                saveError(e.getMessage());
                return ERROR;
            }
        } else {
            return "defDataBase";
        }
    }

    public String defSourceAndDestDatabase() {

        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);
        Map<String, Object> filterMap = convertSearchColumn(paramMap);
        return "defSourceDatabase";
    }

    public String importdatabase() {
        try {
            Map<Object, Object> paramMap = request.getParameterMap();
            resetPageParam(paramMap);

            Map<String, Object> filterMap = convertSearchColumn(paramMap);
            PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
            objList = baseEntityManager.listObjects(filterMap, pageDesc);

            this.pageDesc = pageDesc;
            return "importdatabase";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }
}
