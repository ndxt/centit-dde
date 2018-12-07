package com.centit.dde.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name="D_MAPINFO_TRIGGER")
public class MapInfoTrigger implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="TRIGGER_ID")
    @NotBlank(message = "字段不能为空")
    private Long triggerId;

    @Id
    @Column(name="MAPINFO_ID")
    @NotBlank(message = "字段不能为空")
    private Long mapInfoId;

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
    public MapInfoTrigger() {
    }

    /**
     * minimal constructor
     */
    public MapInfoTrigger(@NotBlank(message = "字段不能为空") Long triggerId, @NotBlank(message = "字段不能为空") Long mapInfoId) {
        this.triggerId = triggerId;
        this.mapInfoId = mapInfoId;
    }

    public MapInfoTrigger(@NotBlank(message = "字段不能为空") Long triggerId, @NotBlank(message = "字段不能为空") Long mapInfoId, String triggerSql, String triggerDesc, String triggerType, String triggerTime, String triggerDatabase, Long tiggerOrder, String isprocedure) {
        this.triggerId = triggerId;
        this.mapInfoId = mapInfoId;
        this.triggerSql = triggerSql;
        this.triggerDesc = triggerDesc;
        this.triggerType = triggerType;
        this.triggerTime = triggerTime;
        this.triggerDatabase = triggerDatabase;
        this.tiggerOrder = tiggerOrder;
        this.isprocedure = isprocedure;
    }

    public Long getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(Long triggerId) {
        this.triggerId = triggerId;
    }

    public Long getMapInfoId() {
        return mapInfoId;
    }

    public void setMapInfoId(Long mapInfoId) {
        this.mapInfoId = mapInfoId;
    }

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


    public void copy(MapInfoTrigger other) {

        this.setTriggerId(other.getTriggerId());
        this.setMapInfoId(other.getMapInfoId());

        this.triggerSql = other.getTriggerSql();
        this.triggerDesc = other.getTriggerDesc();
        this.triggerType = other.getTriggerType();
        this.triggerTime = other.getTriggerTime();
        this.triggerDatabase = other.getTriggerDatabase();
        this.tiggerOrder = other.getTiggerOrder();
        this.isprocedure = other.getIsprocedure();

    }

    public void copyNotNullProperty(MapInfoTrigger other) {

        if (other.getTriggerId() != null)
            this.setTriggerId(other.getTriggerId());
        if (other.getMapInfoId() != null)
            this.setMapInfoId(other.getMapInfoId());

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
