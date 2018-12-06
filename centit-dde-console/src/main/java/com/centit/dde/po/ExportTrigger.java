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
@Table(name="D_EXPORT_TRIGGER")
public class ExportTrigger implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="TRIGGER_ID")
    @NotBlank(message = "字段不能为空")
    private Long triggerId;

    @Id
    @Column(name="EXPORT_ID")
    @NotBlank(message = "字段不能为空")
    private Long exportId;
    
    @Column(name="TRIGGER_SQL")
    private String triggerSql;
    
    @Column(name="TRIGGER_DESC")
    private String triggerDesc;
    
    @Column(name="TRIGGER_TYPE")
    private String triggerType;
    
    @Column(name="TRIGGER_TIME")
    private String triggerTime;

    @Column(name="TIGGER_ORDER")
    private Long tiggerOrder;
    
    @Column(name="ISPROCEDURE")
    private String isprocedure;

    /**
     * 是否是  交换前表级的触发器 源库
     *
     * @return
     */
    public boolean isBeforeTransferAtSource() {
        return "B".equals(triggerTime) && "T".equals(triggerType);
    }

    /**
     * 是否是  交换后表级的触发器 源库
     *
     * @return
     */
    public boolean isAfterTransferAtSource() {
        return "A".equals(triggerTime) && "T".equals(triggerType);
    }

    /**
     * 是否是 交换前行级的触发器 源库
     *
     * @return
     */
    public boolean isBeforeWriteAtSource() {
        return "B".equals(triggerTime) && "L".equals(triggerType);
    }

    /**
     * 是否是 交换后行级的触发器 源库
     *
     * @return
     */
    public boolean isAfterWriteAtSource() {
        return "A".equals(triggerTime) && "L".equals(triggerType);
    }

    /**
     * 是否是 失败后行级的触发器 源库
     *
     * @return
     */
    public boolean isWriteErrorAtSource() {
        return "E".equals(triggerTime) && "L".equals(triggerType);
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
    public ExportTrigger() {
    }

    public ExportTrigger(@NotBlank(message = "字段不能为空") Long triggerId, @NotBlank(message = "字段不能为空") Long exportId) {
        this.triggerId = triggerId;
        this.exportId = exportId;
    }

    public ExportTrigger(@NotBlank(message = "字段不能为空") Long triggerId, @NotBlank(message = "字段不能为空") Long exportId, String triggerSql, String triggerDesc, String triggerType, String triggerTime, Long tiggerOrder, String isprocedure) {
        this.triggerId = triggerId;
        this.exportId = exportId;
        this.triggerSql = triggerSql;
        this.triggerDesc = triggerDesc;
        this.triggerType = triggerType;
        this.triggerTime = triggerTime;
        this.tiggerOrder = tiggerOrder;
        this.isprocedure = isprocedure;
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


    public Long getTiggerOrder() {
        return this.tiggerOrder;
    }

    public void setTiggerOrder(Long tiggerOrder) {
        this.tiggerOrder = tiggerOrder;
    }

    public Long getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(Long triggerId) {
        this.triggerId = triggerId;
    }

    public Long getExportId() {
        return exportId;
    }

    public void setExportId(Long exportId) {
        this.exportId = exportId;
    }

    public void copy(ExportTrigger other) {

        this.setTriggerId(other.getTriggerId());
        this.setExportId(other.getExportId());

        this.triggerSql = other.getTriggerSql();
        this.triggerDesc = other.getTriggerDesc();
        this.triggerType = other.getTriggerType();
        this.triggerTime = other.getTriggerTime();
        this.tiggerOrder = other.getTiggerOrder();
        this.isprocedure = other.getIsprocedure();

    }

    public void copyNotNullProperty(ExportTrigger other) {

        if (other.getTriggerId() != null)
            this.setTriggerId(other.getTriggerId());
        if (other.getExportId() != null)
            this.setExportId(other.getExportId());

        if (other.getTriggerSql() != null)
            this.triggerSql = other.getTriggerSql();
        if (other.getTriggerDesc() != null)
            this.triggerDesc = other.getTriggerDesc();
        if (other.getTriggerType() != null)
            this.triggerType = other.getTriggerType();
        if (other.getTriggerTime() != null)
            this.triggerTime = other.getTriggerTime();

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
        this.tiggerOrder = null;
        this.isprocedure = null;

    }
}
