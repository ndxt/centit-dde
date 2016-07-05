package com.centit.dde.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.centit.support.security.DESSecurityUtils;


@Entity
@Table(name = "D_DATABASE_INFO")
public class DatabaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    public static String DESKEY="0123456789abcdefghijklmnopqrstuvwxyzABCDEF"; 
    // 数据库名
    @Id
    @Column(name = "DATABASE_CODE")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private String databaseCode;
    
    @Column(name = "DATABASE_NAME")
    private String databaseName;

	// 数据库别名
    @Column(name = "DATABASE_NAMES")
    @Length(max = 100, message = "字段长度不能大于{max}")
    private String databaseNames;
    @Column(name = "DATABASE_TYPE")
    @NotBlank(message = "字段不能为空")
    private String databaseType;
    @Column(name = "HOSTPORT")
    @Length(max = 100, message = "字段长度不能大于{max}")
    private String hostPort;
    @Column(name = "DATABASE_URL")
    @Length(max = 1000, message = "字段长度不能大于{max}")
    private String databaseUrl;
    @Column(name = "USERNAME")
    @Length(max = 100, message = "字段长度不能大于{max}")
    private String username;
    @Column(name = "PASSWORD")
    @Length(max = 100, message = "字段长度不能大于{max}")
    private String password;
    @Column(name = "DATA_DESC")
    @Length(max = 100, message = "字段长度不能大于{max}")
    private String dataDesc;
    @Column(name = "CREATED")    
    private String created;
    @Column(name = "CREATE_TIME")
    private Date createTime;
    @Column(name = "SOURCE_OS_ID")
    private String sourceOsId;

    
    
    public String getSourceOsId() {
        return sourceOsId;
    }

    public void setSourceOsId(String sourceOsId) {
        this.sourceOsId = sourceOsId;
    }
    public String getDatabaseCode() {
		return databaseCode;
	}

	public void setDatabaseCode(String databaseCode) {
		this.databaseCode = databaseCode;
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
    
    public String getClearPassword(){
    	return DESSecurityUtils.decryptBase64String(
    			getPassword(),DatabaseInfo.DESKEY);
    }

}
