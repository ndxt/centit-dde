package com.centit.dde.po;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.ldap.odm.annotations.Id;


/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name="D_MAPINFO_TRIGGER")
public class MapinfoTrigger implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    @Column(name="CID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private MapinfoTriggerId cid;

    @Column(name="TRIGGER_SQL")
    private String triggerSql;
    
    @Column(name="TRIGGER_DESC")
    private String triggerDesc;
    
    @Column(name="TRIGGER_TYPE")
    private String triggerType;
    
    @Column(name="TRIGGER_TIME")
    private String triggerTime;
    
    @Column(name="TRIGGER_DATABASE")
    private String triggerDatabase;
    
    @Column(name="TIGGER_ORDER")
    private Long tiggerOrder;
    
    @Column(name="ISPROCEDURE")
    private String isprocedure;

    /**
     * 是否是 交换前表级的触发器 源库
     *
     * @return
     */
    public boolean isBeforeTransferAtSource() {
        return "S".equals(triggerDatabase) && "B".equals(triggerTime) && "T".equals(triggerType);
    }

    /**
     * 是否是  交换前表级的触发器 目标库
     *
     * @return
     */
    public boolean isBeforeTransferAtDest() {
        return "D".equals(triggerDatabase) && "B".equals(triggerTime) && "T".equals(triggerType);
    }

    /**
     * 是否是 交换后表级的触发器 源库
     *
     * @return
     */
    public boolean isAfterTransferAtSource() {
        return "S".equals(triggerDatabase) && "A".equals(triggerTime) && "T".equals(triggerType);
    }

    /**
     * 是否是  交换后表级的触发器 目标库
     *
     * @return
     */
    public boolean isAfterTransferAtDest() {
        return "D".equals(triggerDatabase) && "A".equals(triggerTime) && "T".equals(triggerType);
    }

    /**
     * 是否是 交换前行级的触发器 源库
     *
     * @return
     */
    public boolean isBeforeWriteferAtSource() {
        return "S".equals(triggerDatabase) && "B".equals(triggerTime) && "L".equals(triggerType);
    }

    /**
     * 是否是 交换前行级的触发器 目标库
     *
     * @return
     */
    public boolean isBeforeWriteferAtDest() {
        return "D".equals(triggerDatabase) && "B".equals(triggerTime) && "L".equals(triggerType);
    }

    /**
     * 是否是 交换后行级的触发器 源库
     *
     * @return
     */
    public boolean isAfterWriteAtSource() {
        return "S".equals(triggerDatabase) && "A".equals(triggerTime) && "L".equals(triggerType);
    }

    /**
     * 是否是 交换后行级的触发器 目标库
     *
     * @return
     */
    public boolean isAfterWriteAtDest() {
        return "D".equals(triggerDatabase) && "A".equals(triggerTime) && "L".equals(triggerType);
    }

    /**
     * 是否是 失败后行级的触发器 源库
     *
     * @return
     */
    public boolean isWriteErrorAtSource() {
        return "S".equals(triggerDatabase) && "E".equals(triggerTime) && "L".equals(triggerType);
    }

    /**
     * 是否是 失败后行级的触发器 目标库
     *
     * @return
     */
    public boolean isWriteErrorAtDest() {
        return "D".equals(triggerDatabase) && "E".equals(triggerTime) && "L".equals(triggerType);
    }

    public String getIsprocedure() {
        return isprocedure;
    }

    public void setIsprocedure(String isprocedure) {
        this.isprocedure = isprocedure;
    }
    // Constructors

    /**
     * default constructor
     */
    public MapinfoTrigger() {
    }

    /**
     * minimal constructor
     */
    public MapinfoTrigger(MapinfoTriggerId id

    ) {
        this.cid = id;


    }

    /**
     * full constructor
     */
    public MapinfoTrigger(MapinfoTriggerId id

            , String triggerSql, String triggerDesc, String triggerType, String triggerTime, String triggerDatabase, Long tiggerOrder, String isprocedure) {
        this.cid = id;


        this.triggerSql = triggerSql;
        this.triggerDesc = triggerDesc;
        this.triggerType = triggerType;
        this.triggerTime = triggerTime;
        this.triggerDatabase = triggerDatabase;
        this.tiggerOrder = tiggerOrder;
        this.isprocedure = isprocedure;
    }

    public MapinfoTriggerId getCid() {
        return this.cid;
    }

    public void setCid(MapinfoTriggerId id) {
        this.cid = id;
    }

    public Long getTriggerId() {
        if (this.cid == null)
            this.cid = new MapinfoTriggerId();
        return this.cid.getTriggerId();
    }

    public void setTriggerId(Long triggerId) {
        if (this.cid == null)
            this.cid = new MapinfoTriggerId();
        this.cid.setTriggerId(triggerId);
    }

    public Long getMapinfoId() {
        if (this.cid == null)
            this.cid = new MapinfoTriggerId();
        return this.cid.getMapinfoId();
    }

    public void setMapinfoId(Long mapinfoId) {
        if (this.cid == null)
            this.cid = new MapinfoTriggerId();
        this.cid.setMapinfoId(mapinfoId);
    }


    // Property accessors

    public String getTriggerSql() {
        return this.triggerSql;
    }

    public void setTriggerSql(String triggerSql) {
        this.triggerSql = triggerSql;
    }

    public String getTriggerDesc() {
        return this.triggerDesc;
    }

    public void setTriggerDesc(String triggerDesc) {
        this.triggerDesc = triggerDesc;
    }

    public String getTriggerType() {
        return this.triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerTime() {
        return this.triggerTime;
    }

    public void setTriggerTime(String triggerTime) {
        this.triggerTime = triggerTime;
    }

    public String getTriggerDatabase() {
        return this.triggerDatabase;
    }

    public void setTriggerDatabase(String triggerDatabase) {
        this.triggerDatabase = triggerDatabase;
    }

    public Long getTiggerOrder() {
        return this.tiggerOrder;
    }

    public void setTiggerOrder(Long tiggerOrder) {
        this.tiggerOrder = tiggerOrder;
    }


    public void copy(MapinfoTrigger other) {

        this.setTriggerId(other.getTriggerId());
        this.setMapinfoId(other.getMapinfoId());

        this.triggerSql = other.getTriggerSql();
        this.triggerDesc = other.getTriggerDesc();
        this.triggerType = other.getTriggerType();
        this.triggerTime = other.getTriggerTime();
        this.triggerDatabase = other.getTriggerDatabase();
        this.tiggerOrder = other.getTiggerOrder();
        this.isprocedure = other.getIsprocedure();

    }

    public void copyNotNullProperty(MapinfoTrigger other) {

        if (other.getTriggerId() != null)
            this.setTriggerId(other.getTriggerId());
        if (other.getMapinfoId() != null)
            this.setMapinfoId(other.getMapinfoId());

        if (other.getTriggerSql() != null)
            this.triggerSql = other.getTriggerSql();
        if (other.getTriggerDesc() != null)
            this.triggerDesc = other.getTriggerDesc();
        if (other.getTriggerType() != null)
            this.triggerType = other.getTriggerType();
        if (other.getTriggerTime() != null)
            this.triggerTime = other.getTriggerTime();
        if (other.getTriggerDatabase() != null)
            this.triggerDatabase = other.getTriggerDatabase();
        if (other.getTiggerOrder() != null)
            this.tiggerOrder = other.getTiggerOrder();
        if (other.getIsprocedure() != null)
            this.isprocedure = other.getIsprocedure();

    }

    public void clearProperties() {

        this.triggerSql = null;
        this.triggerDesc = null;
        this.triggerType = null;
        this.triggerTime = null;
        this.triggerDatabase = null;
        this.tiggerOrder = null;
        this.isprocedure = null;

    }
}
