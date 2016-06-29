package com.centit.dde.po;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class DatabaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    // 数据库别名
    private String databaseName;

    // 数据库名
    private String databaseNames;

    private String databaseType;

    private String hostPort;

    private String databaseUrl;

    private String username;

    private String password;

    private String dataDesc;

    private String sourceDatabaseName;

    private String goalDatabaseName;

    private String created;

    private Date createTime;

    private String sourceOsId;
    private OsInfo osInfo;

    public OsInfo getOsInfo() {
        return osInfo;
    }

    public void setOsInfo(OsInfo osInfo) {
        this.osInfo = osInfo;
    }

    public String getSourceOsId() {
        return sourceOsId;
    }

    public void setSourceOsId(String sourceOsId) {
        this.sourceOsId = sourceOsId;
    }

    public String getDatabaseNames() {
        return databaseNames;
    }

    public void setDatabaseNames(String databaseNames) {
        this.databaseNames = databaseNames;
    }

    public String getHostPort() {
        return hostPort;
    }

    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    // 区分源数据和目标数据
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private Set<ExchangeMapinfo> exchangeMapinfosources = null;

    private Set<ExchangeMapinfo> exchangeMapinfodests = null;

    public String getSourceDatabaseName() {
        return sourceDatabaseName;
    }

    public void setSourceDatabaseName(String sourceDatabaseName) {
        this.sourceDatabaseName = sourceDatabaseName;
    }

    public String getGoalDatabaseName() {
        return goalDatabaseName;
    }

    public void setGoalDatabaseName(String goalDatabaseName) {
        this.goalDatabaseName = goalDatabaseName;
    }

    public Set<ExchangeMapinfo> getExchangeMapinfosources() {
        return exchangeMapinfosources;
    }

    public void setExchangeMapinfosources(Set<ExchangeMapinfo> exchangeMapinfosources) {
        this.exchangeMapinfosources = exchangeMapinfosources;
    }

    public Set<ExchangeMapinfo> getExchangeMapinfodests() {
        return exchangeMapinfodests;
    }

    public void setExchangeMapinfodests(Set<ExchangeMapinfo> exchangeMapinfodests) {
        this.exchangeMapinfodests = exchangeMapinfodests;
    }

    // Constructors

    /**
     * default constructor
     */
    public DatabaseInfo() {
    }

    /**
     * minimal constructor
     */
    public DatabaseInfo(String databaseName) {

        this.databaseName = databaseName;

    }

    /**
     * full constructor
     */
    public DatabaseInfo(String databaseName, String databaseType, String databaseUrl, String username, String password,
                        String dataDesc) {

        this.databaseName = databaseName;

        this.databaseType = databaseType;
        this.databaseUrl = databaseUrl;
        this.username = username;
        this.password = password;
        this.dataDesc = dataDesc;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    // Property accessors

    public String getDatabaseType() {
        return this.databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getDatabaseUrl() {
        return this.databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDataDesc() {
        return this.dataDesc;
    }

    public void setDataDesc(String dataDesc) {
        this.dataDesc = dataDesc;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */

    public ExchangeMapinfo newExchangeMapinfo() {
        ExchangeMapinfo res = new ExchangeMapinfo();

        res.setSourceDatabaseName(this.getDatabaseName());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */

    public void copy(DatabaseInfo other) {

        this.setDatabaseName(other.getDatabaseName());

        this.databaseType = other.getDatabaseType();
        this.databaseUrl = other.getDatabaseUrl();
        this.username = other.getUsername();
        this.password = other.getPassword();
        this.dataDesc = other.getDataDesc();

    }

    public void copyNotNullProperty(DatabaseInfo other) {

        if (other.getDatabaseName() != null)
            this.setDatabaseName(other.getDatabaseName());
        if (null != other.getDatabaseNames()) {
            this.setDatabaseNames(other.getDatabaseNames());
        }
        if (null != other.getHostPort()) {
            this.setHostPort(other.getHostPort());
        }
        if (other.getDatabaseType() != null)
            this.databaseType = other.getDatabaseType();
        if (other.getDatabaseUrl() != null)
            this.databaseUrl = other.getDatabaseUrl();
        if (other.getUsername() != null)
            this.username = other.getUsername();
        if (other.getPassword() != null)
            this.password = other.getPassword();
        if (other.getDataDesc() != null)
            this.dataDesc = other.getDataDesc();

    }

    public void clearProperties() {

        this.databaseType = null;
        this.databaseUrl = null;
        this.username = null;
        this.password = null;
        this.dataDesc = null;
    }

    @Override
    public String toString() {
        return "DatabaseInfo [databaseName=" + databaseName + ", databaseUrl=" + databaseUrl + ", username=" + username
                + ", sourceOsId=" + sourceOsId + "]";
    }

}
