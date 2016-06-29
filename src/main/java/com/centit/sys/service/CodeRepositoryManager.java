package com.centit.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.centit.core.utils.LabelValueBean;
import com.centit.sys.po.FDatadictionary;
import com.centit.sys.po.FOptdef;
import com.centit.sys.po.FOptinfo;
import com.centit.sys.po.FRoleinfo;
import com.centit.sys.po.FUnitinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.po.FUserunit;

public interface CodeRepositoryManager {
    public static final Map<String, FUserinfo> USERREPO = new HashMap<String, FUserinfo>();
    public static final Map<String, FUnitinfo> UNITREPO = new HashMap<String, FUnitinfo>();
    public static final List<FUserunit> USERUNIT = new ArrayList<FUserunit>();

    public static final Map<String, FRoleinfo> ROLEREPO = new HashMap<String, FRoleinfo>();
    public static final Map<String, FOptinfo> OPTREPO = new HashMap<String, FOptinfo>();
    public static final Map<String, FOptdef> POWERREPO = new HashMap<String, FOptdef>();

    public static final List<LabelValueBean> DATACATALOG = new ArrayList<LabelValueBean>();
    public static final Map<String, List<FDatadictionary>> REPOSITORIES = new HashMap<String, List<FDatadictionary>>();

    // usercode 与 scriptSessionId 关联
    public static final Map<String, String> USERCODE_SCRIPTSESSION = new HashMap<String, String>();

    // key = 项目ID, value = {key = 用户Code, value = 权限集合}
    //public static final Map<String, Map<String, List<Purview>>> PM_USER_PURVIEW = new HashMap<String, Map<String, List<Purview>>>();

    public void refreshAll();

    public void refresh(String sCatalog);

}
