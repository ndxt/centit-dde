package com.centit.dde.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "D_OS_INFO")

public class OsInfo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "OS_ID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private String osId;
    @Column(name = "OS_NAME")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String osName;
    @Column(name = "HAS_INTERFACE")
    @NotBlank(message = "字段不能为空")
    private String hasInterface;
    @Column(name = "INTERFACE_URL")
    @Length(max = 200, message = "字段长度不能大于{max}")
    private String interfaceUrl;
    @Column(name = "CREATED")
    @Length(max = 8, message = "字段长度不能大于{max}")
    private String created;
    @Column(name = "OS_USER")
    @Length(max = 20, message = "字段长度不能大于{max}")
    private String osUser;
    @Column(name = "OS_PASSWORD")
    @Length(max = 100, message = "字段长度不能大于{max}")
    private String osPassword;
    @Column(name = "LAST_UPDATE_TIME")
    private Date lastUpdateTime;
    @Column(name = "CREATE_TIME")
    private Date createTime;


    // Constructors

    /**
     * default constructor
     */
    public OsInfo() {
    }

    /**
     * minimal constructor
     */
    public OsInfo(
            String osId
            , String osName, String hasInterface) {


        this.osId = osId;

        this.osName = osName;
        this.hasInterface = hasInterface;
    }

    /**
     * full constructor
     */
    public OsInfo(
            String osId
            , String osName, String osUser, String osPassword,
            String hasInterface, String interfaceUrl, String created, Date lastUpdateTime, Date createTime) {


        this.osId = osId;

        this.osName = osName;
        this.hasInterface = hasInterface;
        this.osUser = osUser;
        this.osPassword = osPassword;
        this.interfaceUrl = interfaceUrl;
        this.created = created;
        this.lastUpdateTime = lastUpdateTime;
        this.createTime = createTime;
    }


    public String getOsUser() {
        return osUser;
    }

    public void setOsUser(String osUser) {
        this.osUser = osUser;
    }

    public String getOsPassword() {
        return osPassword;
    }

    public void setOsPassword(String osPassword) {
        this.osPassword = osPassword;
    }

    public String getOsId() {
        return this.osId;
    }

    public void setOsId(String osId) {
        this.osId = osId;
    }
    // Property accessors

    public String getOsName() {
        return this.osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getHasInterface() {
        return this.hasInterface;
    }

    public void setHasInterface(String hasInterface) {
        this.hasInterface = hasInterface;
    }

    public String getInterfaceUrl() {
        return this.interfaceUrl;
    }

    public void setInterfaceUrl(String interfaceUrl) {
        this.interfaceUrl = interfaceUrl;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public void copy(OsInfo other) {

        this.setOsId(other.getOsId());

        this.osName = other.getOsName();
        this.osUser = other.getOsUser();
        this.osPassword = other.getOsPassword();

        this.hasInterface = other.getHasInterface();
        this.interfaceUrl = other.getInterfaceUrl();
        this.created = other.getCreated();
        this.lastUpdateTime = other.getLastUpdateTime();
        this.createTime = other.getCreateTime();

    }

    public void copyNotNullProperty(OsInfo other) {

        if (other.getOsId() != null)
            this.setOsId(other.getOsId());

        if (other.getOsName() != null)
            this.osName = other.getOsName();
        if (other.getOsUser() != null)
            this.osUser = other.getOsUser();
        if (other.getOsPassword() != null)
            this.osPassword = other.getOsPassword();
        if (other.getHasInterface() != null)
            this.hasInterface = other.getHasInterface();
        if (other.getInterfaceUrl() != null)
            this.interfaceUrl = other.getInterfaceUrl();
        if (other.getCreated() != null)
            this.created = other.getCreated();
        if (other.getLastUpdateTime() != null)
            this.lastUpdateTime = other.getLastUpdateTime();
        if (other.getCreateTime() != null)
            this.createTime = other.getCreateTime();

    }

    public void clearProperties() {

        this.osName = null;
        this.osUser = null;
        this.osPassword = null;
        this.hasInterface = null;
        this.interfaceUrl = null;
        this.created = null;
        this.lastUpdateTime = null;
        this.createTime = null;

    }
}
