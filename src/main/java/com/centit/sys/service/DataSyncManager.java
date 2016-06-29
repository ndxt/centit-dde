package com.centit.sys.service;

import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 13-5-16
 * Time: 上午10:46
 * To change this template use File | Settings | File Templates.
 */
public interface DataSyncManager {
    /**
     * 需要同步的数据量
     *
     * @param lastModDate 最后更新时间
     * @return
     */
    Map<String, Long> dataSyncCount(Date lastModDate);

    /**
     * 同步用户，机构，用户机构关系数据
     *
     * @param param       同步条件
     * @param lastModDate 最后更新时间
     */
    void update(Map<String, Object> param, Date lastModDate) throws DataSyncException;

    /**
     * 同步特定用户
     *
     * @param usercode 用户代码或登录名
     */
    void updateByUsercode(String usercode) throws DataSyncException;

    /**
     * 更新用户密码
     *
     * @param usercode 用户代码或登录名
     * @param pwd      经过MD5加密后的密码
     * @return True:更新成功，False:更新失败
     */
    boolean updateUserPwd(String usercode, String pwd) throws DataSyncException;

    /**
     * 同步LDAP中用户，机构数据
     *
     * @param param       同步条件
     * @param lastModDate
     */
    void updateByLdap(Map<String, Object> param, Date lastModDate) throws DataSyncException;
}
