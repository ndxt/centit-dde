package com.centit.dde.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name="D_DATA_OPT_INFO")
public class DataOptInfo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 数据处理ID
     */
    @Id
    @Column(name = "DATA_OPT_ID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private String dataOptId;// mapinfoID

    /**
     * 处理名称
     */
    @Column(name="OPT_NAME")
    private String optName;

    /**
     * 处理说明
     */
    @Column(name = "OPT_DESC")
    private String optDesc;

    /**
     * 创建人员
     */
    @Column(name = "CREATED")
    private String created;

    /**
     * 最后更新时间
     */
    @Column(name = "LAST_UPDATE_TIME")
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;
    
    @OneToMany(orphanRemoval=true,fetch=FetchType.LAZY)
    @JoinColumn(name="DATA_OPT_ID") //这里表示数据库的外键 在t_street里面创建
    private List<DataOptStep> dataOptSteps = null;

    /**
     * default constructor
     */
    public DataOptInfo() {
    }

    /**
     * minimal constructor
     */
    public DataOptInfo(String dataOptId) {

        this.dataOptId = dataOptId;
    }

    /**
     * full constructor
     */
    public DataOptInfo(String dataOptId, String optName, String optDesc, String created, Date lastUpdateTime, Date createTime) {

        this.dataOptId = dataOptId;
        this.optName = optName;
        this.optDesc = optDesc;
        this.created = created;
        this.lastUpdateTime = lastUpdateTime;
        this.createTime = createTime;
    }

    public String getDataOptId() {
        return this.dataOptId;
    }

    public void setDataOptId(String dataOptId) {
        this.dataOptId = dataOptId;
    }

    // Property accessors

    public String getOptName() {
        return this.optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public String getOptDesc() {
        return this.optDesc;
    }

    public void setOptDesc(String optDesc) {
        this.optDesc = optDesc;
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

    public List<DataOptStep> getDataOptSteps() {
        if (this.dataOptSteps == null)
            this.dataOptSteps = new ArrayList<>();
        return this.dataOptSteps;
    }

    public void setDataOptSteps(List<DataOptStep> dataOptSteps) {
        this.dataOptSteps = dataOptSteps;
    }

    public void addDataOptStep(DataOptStep dataOptStep) {
        if (this.dataOptSteps == null) {
            this.dataOptSteps = new ArrayList<>();
        }
        this.dataOptSteps.add(dataOptStep);
    }

    public void removeDataOptStep(DataOptStep dataOptStep) {
        if (this.dataOptSteps == null) {
            return;
        }
        this.dataOptSteps.remove(dataOptStep);
    }

    public DataOptStep newDataOptStep() {
        DataOptStep res = new DataOptStep();

        res.setDataOptId(this.getDataOptId());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */
    public void replaceDataOptSteps(List<DataOptStep> dataOptSteps) {
        List<DataOptStep> newObjs = new ArrayList<>();
        for (DataOptStep p : dataOptSteps) {
            if (p == null) {
                continue;
            }
            DataOptStep newdt = newDataOptStep();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        // delete
        boolean found = false;
        Set<DataOptStep> oldObjs = new HashSet<>();
        oldObjs.addAll(getDataOptSteps());

        for (Iterator<DataOptStep> it = oldObjs.iterator(); it.hasNext(); ) {
            DataOptStep odt = it.next();
            found = false;
            for (DataOptStep newdt : newObjs) {
                if (odt.getOptStepId().equals(newdt.getOptStepId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                removeDataOptStep(odt);
            }
        }
        oldObjs.clear();
        // insert or update
        for (DataOptStep newdt : newObjs) {
            found = false;
            for (Iterator<DataOptStep> it = getDataOptSteps().iterator(); it.hasNext(); ) {
                DataOptStep odt = it.next();
                if (odt.getOptStepId().equals(newdt.getOptStepId())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addDataOptStep(newdt);
        }
    }

    public void copy(DataOptInfo other) {

        this.setDataOptId(other.getDataOptId());

        this.optDesc = other.getOptDesc();
        this.created = other.getCreated();
        this.lastUpdateTime = other.getLastUpdateTime();
        this.createTime = other.getCreateTime();
        this.optName = other.getOptName();
        this.dataOptSteps = other.getDataOptSteps();
    }

    public void copyNotNullProperty(DataOptInfo other) {

        if (other.getDataOptId() != null) {
            this.setDataOptId(other.getDataOptId());
        }
        if (other.getOptDesc() != null) {
            this.optDesc = other.getOptDesc();
        }
        if (other.getCreated() != null) {
            this.created = other.getCreated();
        }
        if (other.getLastUpdateTime() != null) {
            this.lastUpdateTime = other.getLastUpdateTime();
        }
        if (other.getCreateTime() != null) {
            this.createTime = other.getCreateTime();
        }
        if (null != other.getOptName()) {
            this.optName = other.getOptName();
        }
        // this.dataOptSteps = other.getDataOptSteps();
    }

    public void clearProperties() {

        this.optDesc = null;
        this.created = null;
        this.lastUpdateTime = null;
        this.createTime = null;
        this.optName = null;
        // this.dataOptSteps = new HashSet<DataOptStep>();
    }



}
