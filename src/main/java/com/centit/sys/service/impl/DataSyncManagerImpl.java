package com.centit.sys.service.impl;

import com.centit.cas.sys.service.IUserUnitProvider;
import com.centit.cas.sys.service.UserUnitProviderBean;
import com.centit.core.dao.BaseDao;
import com.centit.support.utils.DatetimeOpt;
import com.centit.sys.dao.UnitInfoDao;
import com.centit.sys.dao.UserInfoDao;
import com.centit.sys.dao.UserInfoLdapDao;
import com.centit.sys.dao.UserUnitDao;
import com.centit.sys.po.FUnitinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.po.FUserunit;
import com.centit.sys.po.FUserunitId;
import com.centit.sys.service.DataSyncException;
import com.centit.sys.service.DataSyncManager;
import com.centit.sys.util.ISysOptLog;
import com.centit.sys.util.SysOptLogFactoryImpl;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.GreaterThanOrEqualsFilter;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 13-5-16
 * Time: 上午10:47
 * 数据同步，WebService和Ldap
 */
public class DataSyncManagerImpl implements DataSyncManager, Serializable {
    private static final long serialVersionUID = 4885276288652405737L;

    private static final ISysOptLog SYS_OPT_LOG = SysOptLogFactoryImpl.getSysOptLog("DATASYNC");

    /**
     * Ldap接口
     */
    private UserInfoLdapDao userInfoLdapDao;

    private UserInfoDao userInfoDao;

    private UnitInfoDao unitInfoDao;

    private UserUnitDao userUnitDao;

    /**
     * WebService接口
     */
    private IUserUnitProvider userUnitProvider;

    public void setUserInfoLdapDao(UserInfoLdapDao userInfoLdapDao) {
        this.userInfoLdapDao = userInfoLdapDao;
    }

    public void setUserInfoDao(UserInfoDao userInfoDao) {
        this.userInfoDao = userInfoDao;
    }

    public void setUnitInfoDao(UnitInfoDao unitInfoDao) {
        this.unitInfoDao = unitInfoDao;
    }

    public void setUserUnitDao(UserUnitDao userUnitDao) {
        this.userUnitDao = userUnitDao;
    }

    public void setUserUnitProvider(IUserUnitProvider userUnitProvider) {
        this.userUnitProvider = userUnitProvider;
    }

    @Override
    public Map<String, Long> dataSyncCount(Date lastModDate) {
        UserUnitProviderBean userUnitProviderBean = userUnitProvider.listAllCount(this.convertDate(lastModDate));

        Map<String, Long> result = new HashMap<String, Long>();
        result.put("countuser", userUnitProviderBean.getCountuser());
        result.put("countunit", userUnitProviderBean.getCountunit());
        result.put("countuserunit", userUnitProviderBean.getCountuserunit());

        return result;
    }

    @Override
    public void update(Map<String, Object> param, Date lastModDate) throws DataSyncException {
        UserUnitProviderBean userUnitProviderBean = userUnitProvider.listAll(this.convertDate(lastModDate));
        this.update(param, userUnitProviderBean);


    }

    private void update(Map<String, Object> param, UserUnitProviderBean userUnitProviderBean) throws DataSyncException {
        List<com.centit.cas.sys.service.FUserinfo> wsUserinfos = userUnitProviderBean.getUserinfos();
        List<com.centit.cas.sys.service.FUnitinfo> wsUnitinfos = userUnitProviderBean.getUnitinfos();
        List<com.centit.cas.sys.service.FUserunit> wsUserunits = userUnitProviderBean.getUserunits();


        final String message = "Data copy error";

        //删除原有数据，全覆盖
        if ("cover".equals(param.get("DSMode"))) {
            //清空数据
            userUnitDao.deleteAll(FUserinfo.class);
            userUnitDao.deleteAll(FUnitinfo.class);
            userUnitDao.deleteAll(FUserunit.class);


            try {
                if (!org.springframework.util.CollectionUtils.isEmpty(wsUserinfos)) {
                    List<FUserinfo> userinfos = this.convertList(wsUserinfos, FUserinfo.class);

                    userInfoDao.batchSave(userinfos);
                }

                if (!org.springframework.util.CollectionUtils.isEmpty(wsUnitinfos)) {
                    List<FUnitinfo> unitinfos = this.convertList(wsUnitinfos, FUnitinfo.class);

                    unitInfoDao.batchSave(unitinfos);
                }

                if (!org.springframework.util.CollectionUtils.isEmpty(wsUserunits)) {
                    List<FUserunit> userunits = this.convertUserunit(wsUserunits);

                    userUnitDao.batchSave(userunits);
                }
            } catch (IllegalAccessException e) {
                throw new DataSyncException(message, e);
            } catch (InstantiationException e) {
                throw new DataSyncException(message, e);
            } catch (NoSuchMethodException e) {
                throw new DataSyncException(message, e);
            } catch (InvocationTargetException e) {
                throw new DataSyncException(message, e);
            } catch (Exception e) {
                throw new DataSyncException(message, e);
            }


        } else {
            //增量更新，如不存在，新增
            //交集，进行更新,差集，进行添加
            //用户
            try {
                if (!org.springframework.util.CollectionUtils.isEmpty(wsUserinfos)) {
                    List<FUserinfo> dbUserinfos = this.listDbObjsByRemoteType(wsUserinfos, FUserinfo.class, userInfoDao, "usercode");
                    if (!org.springframework.util.CollectionUtils.isEmpty(dbUserinfos)) {
                        userInfoDao.batchSave(dbUserinfos);
                    }
                }

                //机构
                if (!org.springframework.util.CollectionUtils.isEmpty(wsUnitinfos)) {
                    List<FUnitinfo> dbUnitinfos = this.listDbObjsByRemoteType(wsUnitinfos, FUnitinfo.class, unitInfoDao, "unitcode");

                    if (!org.springframework.util.CollectionUtils.isEmpty(dbUnitinfos)) {
                        unitInfoDao.batchSave(dbUnitinfos);
                    }
                }

                //用户机构关联
                if (!org.springframework.util.CollectionUtils.isEmpty(wsUserunits)) {
                    List<FUserunit> remoteUserunits = this.convertUserunit(wsUserunits);
                    List<FUserunit> dbUserunits = this.listDbObjsByLocalType(remoteUserunits, userUnitDao, "id");

                    if (!org.springframework.util.CollectionUtils.isEmpty(dbUserunits)) {
                        userUnitDao.batchSave(dbUserunits);
                    }
                }
            } catch (InvocationTargetException e) {
                throw new DataSyncException(message, e);
            } catch (NoSuchMethodException e) {
                throw new DataSyncException(message, e);
            } catch (InstantiationException e) {
                throw new DataSyncException(message, e);
            } catch (IllegalAccessException e) {
                throw new DataSyncException(message, e);
            } catch (Exception e) {
                throw new DataSyncException(message, e);
            }

        }
    }

    @Override
    public void updateByUsercode(String usercode) throws DataSyncException {
        UserUnitProviderBean userUnitProviderBean = this.userUnitProvider.listUnitinfoByUsercode(usercode);
        if (null == userUnitProviderBean) {
            throw new DataSyncException("服务器不存在此用户");
        }
        //设置增量更新
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("DSMode", "incremental");
        this.update(param, userUnitProviderBean);

    }

    @Override
    public boolean updateUserPwd(String usercode, String pwd) {
        return this.userUnitProvider.updateUserPwd(usercode, pwd);
    }

    @Override
    public void updateByLdap(Map<String, Object> param, Date lastModDate) throws DataSyncException {
        String data = null;
        if (null != lastModDate) {
            data = DatetimeOpt.convertDateToString(lastModDate, "yyyyMMddHHmmss") + ".0Z";
        }

        Map<String, List<?>> listMap = this.listUserunitByLdap(data);

        List<FUserinfo> userinfos = (List<FUserinfo>) listMap.get("userinfo");
        List<FUnitinfo> unitinfos = (List<FUnitinfo>) listMap.get("unitinfo");
        List<FUserunit> userunits = (List<FUserunit>) listMap.get("userunit");

        final String message = "data copy or comparison error";
        try {
            userinfos = listDbByLdap(userinfos, userInfoDao, "loginname", new String[]{"usercode", "userpin"});

            Map<String, String> usercodeCache = new HashMap<String, String>();
            Map<String, String> unitcodeCache = new HashMap<String, String>();


            if (!org.springframework.util.CollectionUtils.isEmpty(userinfos)) {
                //保存并生成主键
                userInfoDao.batchSave(userinfos);

                //loginname与usercode关联
                for (FUserinfo userinfo : userinfos) {
                    usercodeCache.put(userinfo.getLoginname(), userinfo.getUsercode());
                }
            }

            unitinfos = listDbByLdap(unitinfos, unitInfoDao, "unitalias", new String[]{"unitcode", "unittype", "unitgrade", "unitorder"});
            if (!org.springframework.util.CollectionUtils.isEmpty(unitinfos)) {
                unitInfoDao.batchSave(unitinfos);

                for (FUnitinfo unitinfo : unitinfos) {
                    unitcodeCache.put(unitinfo.getUnitalias(), unitinfo.getUnitcode());
                }
            }

            if (!org.springframework.util.CollectionUtils.isEmpty(userunits)) {
                //重新将usercode字段中loginname的值更新为usercode
                for (FUserunit userunit : userunits) {
                    userunit.setUsercode(usercodeCache.get(userunit.getUsercode()));
                    userunit.setUnitcode(unitcodeCache.get(userunit.getUnitcode()));
                }
                userUnitDao.batchSave(userunits);
            }

        } catch (NoSuchMethodException e) {
            throw new DataSyncException(message, e);
        } catch (IllegalAccessException e) {
            throw new DataSyncException(message, e);
        } catch (InvocationTargetException e) {
            throw new DataSyncException(message, e);
        } catch (Exception e) {
            throw new DataSyncException(message, e);
        }
    }


    /**
     * @param list
     * @param db
     * @param prop
     * @param ignoreProperties copy属性过滤属性
     * @param <T>
     * @param <DB>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws DataSyncException
     */
    private <T extends Comparator, DB extends BaseDao> List<T> listDbByLdap(List<T> list, DB db, String prop, String... ignoreProperties) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, DataSyncException {
        List<? extends Serializable> pks = getPropList(list, prop);

        //通过主键查出出的数据库中的数据
        List<T> dbObjs = listObjectByPks(db, pks);


        //数据库中无数据，直接返回
        if (org.springframework.util.CollectionUtils.isEmpty(dbObjs)) {
            return list;
        }

        //交集，更新，如果是Fuserinfo不更新密码
        Collection<T> intersection = CollectionUtils.intersection(list, dbObjs);

        //排序，Copy值，进行更新
        List<T> intersectionList = new ArrayList<T>(intersection);
        Collections.sort(intersectionList, intersectionList.get(0));
        Collections.sort(dbObjs, dbObjs.get(0));

        //密码不进行更新
        for (int i = 0; i < intersectionList.size(); i++) {
            BeanUtils.copyProperties(intersectionList.get(i), dbObjs.get(i), ignoreProperties);
        }

        //差集，保存
        Collection<T> subtract = CollectionUtils.subtract(list, dbObjs);

        if (!org.springframework.util.CollectionUtils.isEmpty(subtract)) {
            dbObjs.addAll(subtract);
        }

        return dbObjs;
    }


    /**
     * 通过LDAP属性构建Fuserinfo
     *
     * @return
     */
    private AttributesMapper getUserAttributes() {
        return new AttributesMapper() {
            @Override
            public Object mapFromAttributes(Attributes userAttributes) throws NamingException {
                FUserinfo ui = new FUserinfo();

                //ui.setUsercode(userInfoDao.getNextKey());
                ui.setLoginname((String) userAttributes.get("sAMAccountName").get());

                //设置初始密码
                //ui.setUserpin(new Md5PasswordEncoder().encodePassword("000000", ui.getUsercode()));
                ui.setUsername((String) userAttributes.get("cn").get());

                if (null != userAttributes.get("mail")) {
                    ui.setRegemail((String) userAttributes.get("mail").get());
                }

                ui.setCreateDate(DatetimeOpt.convertStringToDate((String) userAttributes.get("whenCreated").get(), "yyyyMMddHHmmss"));
                ui.setLastModifyDate(DatetimeOpt.convertStringToDate((String) userAttributes.get("whenChanged").get(), "yyyyMMddHHmmss"));

                ui.setIsvalid("T");

                return ui;
            }
        };
    }

    /**
     * 通过LDAP属性构建用户，机构关系
     *
     * @param userAttributesMapper
     * @return
     */
    private AttributesMapper getUnitAttributes(final AttributesMapper userAttributesMapper) {
        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("PARENTUNIT", "0");
        //顶级主机构
        final List<FUnitinfo> ps = unitInfoDao.listObjects(filterMap);

        return new AttributesMapper() {
            @Override
            public Object mapFromAttributes(Attributes unitAttributes) throws NamingException {
                Map<FUnitinfo, List<FUserinfo>> valueMap = new HashMap<FUnitinfo, List<FUserinfo>>();

                //用户信息缓存
                Map<String, FUserinfo> cncache = new HashMap<String, FUserinfo>();

                FUnitinfo unitinfo = new FUnitinfo();

                //unitinfo.setUnitcode((String) unitAttributes.get("cn").get());
                unitinfo.setUnitalias((String) unitAttributes.get("cn").get());
                if (null != unitAttributes.get("description")) {
                    unitinfo.setUnitname((String) unitAttributes.get("description").get());
                } else {
                    unitinfo.setUnitname(unitinfo.getUnitalias());
                }
                unitinfo.setCreateDate(DatetimeOpt.convertStringToDate((String) unitAttributes.get("whenCreated").get(), "yyyyMMddHHmmss"));
                unitinfo.setLastModifyDate(DatetimeOpt.convertStringToDate((String) unitAttributes.get("whenChanged").get(), "yyyyMMddHHmmss"));

                unitinfo.setIsvalid("T");

                unitinfo.setParentunit(ps.get(0).getUnitcode());

                //机构下用户
                Attribute member = unitAttributes.get("member");
                if (null != member) {
                    NamingEnumeration<String> all = (NamingEnumeration<String>) member.getAll();

                    List<FUserinfo> userinfos = new ArrayList<FUserinfo>();

                    while (all.hasMoreElements()) {
                        String s = all.nextElement();
                        s = s.substring(0, s.indexOf(","));

                        FUserinfo userinfo = null;
                        if (cncache.containsKey(s)) {
                            userinfo = cncache.get(s);
                        } else {
                            try {
                                userinfo = (FUserinfo) userInfoLdapDao.getObject(s, userAttributesMapper);
                            } catch (Exception e) {
                                //e.printStackTrace();
                            }
                        }

                        if (null != userinfo) {
                            cncache.put(s, userinfo);
                            userinfos.add(userinfo);
                        }

                    }

                    //不保存空部门
                    if (!org.springframework.util.CollectionUtils.isEmpty(userinfos)) {
                        valueMap.put(unitinfo, userinfos);
                    }
                }


                return valueMap;
            }
        };
    }


    private Map<String, List<?>> listUserunitByLdap(String lastModDate) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "group"));
        if (null != lastModDate) {
            filter.and(new GreaterThanOrEqualsFilter("whenChanged", lastModDate));
        }

        //获取LDAP中机构用户
        List<Map<FUnitinfo, List<FUserinfo>>> valueMaps = userInfoLdapDao.listObjects(filter, getUnitAttributes(getUserAttributes()));

        Map<String, List<?>> listMap = new HashMap<String, List<?>>();


        List<FUnitinfo> unitinfos = new ArrayList<FUnitinfo>();
        Set<FUserinfo> userinfos = new HashSet<FUserinfo>();
        List<FUserunit> userunits = new ArrayList<FUserunit>();
        for (Map<FUnitinfo, List<FUserinfo>> valueMap : valueMaps) {
            if (org.springframework.util.CollectionUtils.isEmpty(valueMap)) {
                continue;
            }

            for (Map.Entry<FUnitinfo, List<FUserinfo>> entry : valueMap.entrySet()) {
                unitinfos.add(entry.getKey());
                userinfos.addAll(entry.getValue());

                for (FUserinfo userinfo : entry.getValue()) {
                    FUserunit userunit = new FUserunit();
                    FUserunitId id = new FUserunitId();

                    //从LDAP中获取的用户无usercode，暂时保存loginname，后期进行重新更新
                    id.setUsercode(userinfo.getLoginname());
                    id.setUnitcode(entry.getKey().getUnitalias());

                    userunit.setId(id);
                    userunit.setCreateDate(new Date());

                    userunit.setIsprimary("T");
                    userunit.setUserrank("F");
                    userunit.setUserstation("F");

                    userunits.add(userunit);

                }
            }

        }
        listMap.put("userinfo", new ArrayList<Object>(userinfos));
        listMap.put("unitinfo", unitinfos);
        listMap.put("userunit", userunits);

        return listMap;
    }


    private <L, DB extends BaseDao<L>> List<L> listDbObjsByLocalType(List<L> remotes, DB db, String pk) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, DataSyncException {
        List<L> local = db.listObjects();

        //差集，保存
        Collection<L> subtract = CollectionUtils.subtract(remotes, local);

        //获取远程中所有对象PK集合，并查询数据库
        List<? extends Serializable> pks = this.getPropList(remotes, pk);

        List<L> dbs = this.listObjectByPks(db, pks);

        //交集，更新
        Collection<L> intersection = CollectionUtils.intersection(remotes, dbs);

        intersection.addAll(subtract);

        return new ArrayList<L>(intersection);
    }


    /**
     * 转换远程对象至本地对象，并与本地数据库进行比对
     *
     * @param remotes    远程对象集合
     * @param localClass 本地对象类型
     * @param db         数据库对你
     * @param pk         主键名
     * @param <R>        远程对象类型
     * @param <L>        本地对象类型
     * @param <DB>       数据库操作类型
     * @return 比对后本地数据集合
     */
    private <R, L, DB extends BaseDao<L>> List<L> listDbObjsByRemoteType(List<R> remotes, Class<L> localClass, DB db, String pk) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, DataSyncException {
        List<L> remoteLists = this.convertList(remotes, localClass);

        return this.listDbObjsByLocalType(remoteLists, db, pk);
    }


    private <L, DB extends BaseDao<L>, PK extends Serializable> List<L> listObjectByPks(DB db, List<PK> pks) throws DataSyncException {
        if (org.springframework.util.CollectionUtils.isEmpty(pks)) {
            return new ArrayList<L>();
        }

        if (db instanceof UserInfoDao) {
            return (List<L>) ((UserInfoDao) db).listUserinfoByUsercodes((List<String>) pks);
        } else if (db instanceof UnitInfoDao) {
            return (List<L>) ((UnitInfoDao) db).listUnitinfoByUnitcodes((List<String>) pks);
        } else if (db instanceof UserUnitDao) {
            return (List<L>) ((UserUnitDao) db).listUserunitByIds((List<FUserunitId>) pks);
        }

        throw new DataSyncException("DB not in the assign range");
    }


    private XMLGregorianCalendar convertDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return XMLGregorianCalendarImpl.createDateTime(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE),
                calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)
        );
    }


    private Date convertDate(XMLGregorianCalendar xmlGregorianCalendar) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(xmlGregorianCalendar.getYear(), xmlGregorianCalendar.getMonth() + 1,
                xmlGregorianCalendar.getDay(), xmlGregorianCalendar.getHour(),
                xmlGregorianCalendar.getMinute(), xmlGregorianCalendar.getSecond());

        return calendar.getTime();
    }


    /**
     * 转换远程机构用户关联至本地关联
     *
     * @param remoteUserunits
     * @return
     */
    private List<FUserunit> convertUserunit(List<com.centit.cas.sys.service.FUserunit> remoteUserunits) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<FUserunit> localUserunits = new ArrayList<FUserunit>();
        for (com.centit.cas.sys.service.FUserunit remoteUserunit : remoteUserunits) {
            FUserunit localUserunit = new FUserunit();
            FUserunitId id = new FUserunitId();

            BeanUtils.copyProperties(remoteUserunit, localUserunit, new String[]{"createDate", "lastModifyDate"});


            this.invokeMethod(remoteUserunit, localUserunit, "createDate");
            this.invokeMethod(remoteUserunit, localUserunit, "lastModifyDate");


            BeanUtils.copyProperties(remoteUserunit, id);

            localUserunit.setId(id);

            localUserunits.add(localUserunit);
        }
        return localUserunits;
    }


    /**
     * 将 com.centit.cas.sys.service.包中PO转换为本系统中使用的PO
     *
     * @param r
     * @param clazz
     * @param <R>
     * @param <L>
     * @return
     */
    private <R, L> List<L> convertList(List<R> r, Class<L> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        List<L> result = new ArrayList<L>();

        for (R e1 : r) {

            L l = clazz.newInstance();
            BeanUtils.copyProperties(e1, l, new String[]{"createDate", "lastModifyDate"});

            this.invokeMethod(e1, l, "createDate");
            this.invokeMethod(e1, l, "lastModifyDate");

            result.add(l);

        }
        return result;
    }


    private void invokeMethod(Object source, Object target, String properties) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        XMLGregorianCalendar createDate = (XMLGregorianCalendar) MethodUtils.invokeExactMethod(source, "get" + (properties.substring(0, 1).toUpperCase() + properties.substring(1)), null);
        if (null == createDate) {
            return;
        }
        MethodUtils.invokeExactMethod(target, "set" + (properties.substring(0, 1).toUpperCase() + properties.substring(1)), this.convertDate(createDate));
    }


    /**
     * 获取集合中某一属性集合
     *
     * @param es
     * @param properties
     * @param <T>
     * @param <E>
     * @return
     */
    private <T, E> List<T> getPropList(Collection<E> es, String properties) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<T> result = new ArrayList<T>();
        for (E e : es) {
            result.add((T) MethodUtils.invokeExactMethod(e, "get" + (properties.substring(0, 1).toUpperCase() + properties.substring(1)), null));
        }

        return result;
    }


}
