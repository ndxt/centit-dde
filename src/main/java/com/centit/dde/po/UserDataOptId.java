package com.centit.dde.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by sx on 2014/12/10.
 */
@Entity
@Table(name="D_USER_DATAOPTID")
public class UserDataOptId implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @Id
    @Column(name="UD_ID")
    private Long udId;

    /**
     * 用户代码
     */
    @Column(name="USERCODE")
    private String usercode;

    /**
     * 业务操作ID
     */
    @Column(name="DATAOPTID")
    private String dataOptId;

    /**
     * 创建时间
     */
    @Column(name="CREATE_DATE")
    private Date createDate;

    /**
     * 最后更新时间
     */
    @Column(name="LAST_MODIFY_DATE")
    private Date lastModifyDate;

    /**
     * 描述
     */
    @Column(name="DESCRIBE")
    private String describe;

    /**
     * 业务操作类型
     */
    @Column(name="DATAOPT_TYPE")
    private String dataoptType;

    public String getDataoptType() {
        return dataoptType;
    }

    public void setDataoptType(String dataoptType) {
        this.dataoptType = dataoptType;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Long getUdId() {
        return udId;
    }

    public void setUdId(Long udId) {
        this.udId = udId;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getDataOptId() {
        return dataOptId;
    }

    public void setDataOptId(String dataOptId) {
        this.dataOptId = dataOptId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public UserDataOptId() {
    }

    public UserDataOptId(String usercode, String dataOptId) {
        this.usercode = usercode;
        this.dataOptId = dataOptId;

        this.createDate = new Date();
    }


    public void copy(UserDataOptId other) {
        setUdId(other.getUdId());
        setCreateDate(other.getCreateDate());
        setLastModifyDate(other.getLastModifyDate());
        setDataOptId(other.getDataOptId());
        setDescribe(other.getDescribe());
        setUsercode(other.getUsercode());
        setDataoptType(other.getDataoptType());

    }

    public void copyNotNullProperty(UserDataOptId other) {
        if (null != other.getUdId()) {
            setUdId(other.getUdId());
        }
        if (null != other.getCreateDate()) {
            setCreateDate(other.getCreateDate());
        }
        if (null != other.getLastModifyDate()) {
            setLastModifyDate(other.getLastModifyDate());
        }
        if (null != other.getDataOptId()) {
            setDataOptId(other.getDataOptId());
        }
        if (null != other.getDescribe()) {
            setDescribe(other.getDescribe());
        }
        if (null != other.getUsercode()) {
            setUsercode(other.getUsercode());
        }
        if (null != other.getDataoptType()) {
            setDataoptType(other.getDataoptType());
        }

    }
}
