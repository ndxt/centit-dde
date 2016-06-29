package com.centit.sys.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.centit.core.dao.BaseDaoImpl;
import com.centit.core.dao.CodeBook;
import com.centit.sys.po.FOptRoleMap;
import com.centit.sys.po.FOptdef;
import com.centit.sys.po.FOptinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.po.FVUseroptlist;
import com.centit.sys.po.FVUseroptmoudlelist;

public class OptInfoDao extends BaseDaoImpl<FOptinfo> {
    private static final long serialVersionUID = 1L;

    public Map<String, String> getFilterField() {
        if (filterField == null) {
            filterField = new HashMap<String, String>();
            filterField.put("OPTID", CodeBook.EQUAL_HQL_ID);
            filterField.put("OPTURL", CodeBook.EQUAL_HQL_ID);
            filterField.put("OPTNAME", CodeBook.LIKE_HQL_ID);
            filterField.put("PREOPTID", CodeBook.EQUAL_HQL_ID);
            filterField.put("OPTTYPE", CodeBook.EQUAL_HQL_ID);
            filterField.put("TOPOPTID", CodeBook.EQUAL_HQL_ID);
            filterField.put("ISINTOOLBAR", CodeBook.EQUAL_HQL_ID);
            filterField.put(CodeBook.ORDER_BY_HQL_ID, " preoptid, orderind");
        }
        return filterField;
    }

    @SuppressWarnings("unchecked")
    public List<FOptinfo> getFunctionsByUserID(String userID) {
        String[] params = null;
        String hql = "FROM FVUseroptmoudlelist where usercode=?";
        // + " ORDER BY preoptid, formcode";

        params = new String[]{userID};

        List<FVUseroptmoudlelist> ls = getHibernateTemplate().find(hql, (Object[]) params);
        List<FOptinfo> opts = new ArrayList<FOptinfo>();
        for (FVUseroptmoudlelist opm : ls) {
            FOptinfo opt = new FOptinfo();
            opt.setFormcode(opm.getFormcode());
            opt.setImgindex(opm.getImgindex());
            opt.setIsintoolbar(opm.getIsintoolbar());
            opt.setMsgno(opm.getMsgno());
            opt.setMsgprm(opm.getMsgprm());
            opt.setOptid(opm.getOptid());
            opt.setOptname(opm.getOptname());
            opt.setOpturl(opm.getOpturl());
            opt.setPreoptid(opm.getPreoptid());
            opt.setTopoptid(opm.getTopoptid());
            opts.add(opt);
        }
        return opts;
    }

    /**
     * @param userID
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<FOptinfo> getMenuFuncByUserID(String userID) {
        String hql1 = "FROM FOptinfo where opturl='...' order by orderind ";
        List<FOptinfo> preOpts = getHibernateTemplate().find(hql1);

        String hql = "FROM FVUseroptmoudlelist where isintoolbar='Y' and usercode=? ORDER BY orderind";
        // + " ORDER BY preoptid, formcode";
        List<FVUseroptmoudlelist> ls = (List<FVUseroptmoudlelist>) getHibernateTemplate().find(hql,
                new Object[]{userID});

        return getMenuFuncs(preOpts, ls);
    }

    @SuppressWarnings("unchecked")
    public List<FOptinfo> getMenuFuncByUserIDAndSuperFunctionId(String userID, String superFunctionId) {
        String hql1 = "FROM FOptinfo where opturl='...' order by orderind ";

        List<FOptinfo> preOpts = super.getHibernateTemplate().find(hql1);
        String hql = "select f FROM FVUseroptmoudlelist f, VHioptinfo v where f.optid = v.optid and f.isintoolbar='Y' and f.usercode=? and v.topoptid=? ORDER BY orderind";
        // + " ORDER BY preoptid, formcode";
        List<FVUseroptmoudlelist> ls = (List<FVUseroptmoudlelist>) super.getHibernateTemplate().find(hql,
                new Object[]{userID, superFunctionId});

        return getMenuFuncs(preOpts, ls);
    }

    private static List<FOptinfo> getMenuFuncs(List<FOptinfo> preOpts, List<FVUseroptmoudlelist> ls) {
        boolean isNeeds[] = new boolean[preOpts.size()];
        for (int i = 0; i < preOpts.size(); i++) {
            isNeeds[i] = false;
        }
        List<FOptinfo> opts = new ArrayList<FOptinfo>();

        for (FVUseroptmoudlelist opm : ls) {
            FOptinfo opt = new FOptinfo();
            opt.setFormcode(opm.getFormcode());
            opt.setImgindex(opm.getImgindex());
            opt.setIsintoolbar(opm.getIsintoolbar());
            opt.setMsgno(opm.getMsgno());
            opt.setMsgprm(opm.getMsgprm());
            opt.setOptid(opm.getOptid());
            opt.setOpttype(opm.getOpttype());
            opt.setOptname(opm.getOptname());
            opt.setOpturl(opm.getOpturl());
            opt.setPreoptid(opm.getPreoptid());
            opt.setTopoptid(opm.getTopoptid());
            opt.setPageType(opm.getPageType());

            opts.add(opt);
            for (int i = 0; i < preOpts.size(); i++) {
                if (opt.getPreoptid() != null && opt.getPreoptid().equals(preOpts.get(i).getOptid())) {
                    isNeeds[i] = true;
                    break;
                }
            }
        }

        List<FOptinfo> needAdd = new ArrayList<FOptinfo>();
        for (int i = 0; i < preOpts.size(); i++) {
            if (isNeeds[i]) {
                needAdd.add(preOpts.get(i));
            }
        }

        boolean isNeeds2[] = new boolean[preOpts.size()];
        while (true) {
            int nestedMenu = 0;
            for (int i = 0; i < preOpts.size(); i++)
                isNeeds2[i] = false;

            for (int i = 0; i < needAdd.size(); i++) {
                for (int j = 0; j < preOpts.size(); j++) {
                    if (!isNeeds[j] && needAdd.get(i).getPreoptid() != null
                            && needAdd.get(i).getPreoptid().equals(preOpts.get(j).getOptid())) {
                        isNeeds[j] = true;
                        isNeeds2[j] = true;
                        nestedMenu++;
                        break;
                    }
                }
            }
            if (nestedMenu == 0)
                break;

            needAdd.clear();
            for (int i = 0; i < preOpts.size(); i++) {
                if (isNeeds2[i]) {
                    needAdd.add(preOpts.get(i));
                }
            }

        }

        for (int i = 0; i < preOpts.size(); i++) {
            if (isNeeds[i]) {
                opts.add(preOpts.get(i));
            }
        }
        // end
        return makeOptTree(opts);
    }


    private static List<FOptinfo> makeOptTree(List<FOptinfo> opts) {
        List<FOptinfo> treeOpts = new ArrayList<FOptinfo>();
        // 获取 顶层节点
        List<FOptinfo> tempOpts = new ArrayList<FOptinfo>();
        for (FOptinfo opt : opts) {
            if ("0".equals(opt.getPreoptid()))
                tempOpts.add(opt);
        }
        treeOpts.addAll(tempOpts);
        opts.removeAll(tempOpts);
        while (tempOpts.size() > 0 && opts.size() > 0) {
            List<FOptinfo> parentOpts = tempOpts;
            tempOpts = new ArrayList<FOptinfo>();
            for (FOptinfo opt : opts) {
                for (FOptinfo parOpt : parentOpts) {
                    if (parOpt.getOptid().equals(opt.getPreoptid())) {
                        tempOpts.add(opt);
                        break;
                    }
                }
            }
            treeOpts.addAll(tempOpts);
            opts.removeAll(tempOpts);
        }

        return treeOpts;
    }

    /**
     * 按照业务类别获取 操作业务 M:管理业务,S: 系统业务,O:普通业务,W: 流程业务,'H': D：首页面显示模块 E：系统外
     *
     * @param type 业务类别
     * @return List<FOptinfo>
     */
    public List<FOptinfo> findMenuFuncByType(String type) {
        String[] params = new String[]{type};
        String hql = "FROM FOptinfo where PREOPTID != '0' and OPTTYPE=? order by orderind ";

        @SuppressWarnings("unchecked")
        List<FOptinfo> opts = getHibernateTemplate().find(hql, (Object[]) params);

        return opts;
    }

    @SuppressWarnings("unchecked")
    public List<FOptinfo> getFunctionsByUserAndSuperFunctionId(String userID, String superFunctionId) {
        String[] params = null;
        String hql = "FROM FVUseroptmoudlelist  where usercode=? and topoptid=?" + " ORDER BY preoptid, orderind";

        params = new String[]{userID, superFunctionId};
        List<FVUseroptmoudlelist> ls = (List<FVUseroptmoudlelist>) getHibernateTemplate().find(hql, (Object[]) params);
        List<FOptinfo> opts = new ArrayList<FOptinfo>();
        for (FVUseroptmoudlelist opm : ls) {
            FOptinfo opt = new FOptinfo();
            opt.setFormcode(opm.getFormcode());
            opt.setImgindex(opm.getImgindex());
            opt.setIsintoolbar(opm.getIsintoolbar());
            opt.setMsgno(opm.getMsgno());
            opt.setOpttype(opm.getOpttype());
            opt.setMsgprm(opm.getMsgprm());
            opt.setOptid(opm.getOptid());
            opt.setOptname(opm.getOptname());
            opt.setOpturl(opm.getOpturl());
            opt.setPreoptid(opm.getPreoptid());
            opt.setTopoptid(opm.getTopoptid());
            opts.add(opt);
            System.out.print(opt.getOpttype());
        }

        return opts;
    }

    @SuppressWarnings("unchecked")
    public List<FOptdef> getMethodByUserAndOptid(String userCode, String optid) {
        String[] params = null;
        String hql = "FROM FVUseroptlist urv where urv.id.usercode=? and optid= ?";

        params = new String[]{userCode, optid};
        List<FVUseroptlist> ls = (List<FVUseroptlist>) getHibernateTemplate().find(hql, (Object[]) params);
        List<FOptdef> methods = new ArrayList<FOptdef>();
        for (FVUseroptlist opm : ls) {
            FOptdef method = new FOptdef();
            method.setOptcode(opm.getId().getOptcode());
            method.setOptid(opm.getOptid());
            method.setOptmethod(opm.getOptmethod());
            method.setOptname(opm.getOptname());
            methods.add(method);
        }
        return methods;
    }

    @SuppressWarnings("unchecked")
    public List<FOptRoleMap> getAllOptRoleMap() {

        String sSqlsen = "select c.opturl,b.optmethod,a.rolecode,c.optid,b.optcode "
                + "from F_ROLEPOWER a join F_OPTDEF b on(a.optcode=b.optcode) "
                + "join f_optinfo c on(b.optid=c.optid) " + "where c.OptType<>'W' and c.opturl<>'...' "
                + "order by c.opturl,b.optmethod,a.rolecode";
        List<FOptRoleMap> l = (List<FOptRoleMap>) findObjectsBySql(sSqlsen, FOptRoleMap.class);
        return l;
    }

    @SuppressWarnings("unchecked")
    public List<FOptinfo> getParentMenu() {
        String hql1 = "FROM FOptinfo where OPTURL='...' order by PREOPTID ";
        List<FOptinfo> parentMenuList = (List<FOptinfo>) getHibernateTemplate().find(hql1);
        return parentMenuList;
    }

    @SuppressWarnings("unchecked")
    public List<FVUseroptmoudlelist> getMenuItem(String userID) {
        String hql = "FROM FVUseroptmoudlelist where isintoolbar='Y' and usercode=? ORDER BY orderind";

        String[] params = new String[]{userID};
        return (List<FVUseroptmoudlelist>) getHibernateTemplate().find(hql, (Object[]) params);
    }

    @SuppressWarnings("unchecked")
    public List<FVUseroptmoudlelist> getMenuItem(String userID, String pid) {
        String hql = "FROM FVUseroptmoudlelist where isintoolbar='Y' and usercode=? and preoptid = ? ORDER BY orderind";

        String[] params = new String[]{userID, pid};
        return (List<FVUseroptmoudlelist>) getHibernateTemplate().find(hql, (Object[]) params);
    }

    public List<FVUseroptmoudlelist> getMenuItem(FUserinfo fUserinfo) {
        if (null != fUserinfo && !"".equals(fUserinfo.getUsercode())) {
            return this.getMenuItem(fUserinfo.getUsercode());
        } else
            return null;

    }
}
