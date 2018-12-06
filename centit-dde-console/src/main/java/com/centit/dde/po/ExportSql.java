package com.centit.dde.po;

import com.centit.support.database.orm.GeneratorType;
import com.centit.support.database.orm.Lazy;
import com.centit.support.database.orm.ValueGenerator;

import javax.persistence.*;
import java.util.*;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name="D_EXPORT_SQL")
public class ExportSql implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="EXPORT_ID")
    @ValueGenerator(strategy = GeneratorType.AUTO)
    private Long exportId;

    /**
     * 源数据库名称
     */
    @Column(name="SOURCE_DATABASE_NAME")
    private String sourceDatabaseName;

    /**
     * 业务系统
     */
    @Column(name="SOURCE_OS_ID")
    private String sourceOsId;

    /**
     * 导出名称
     */
    @Column(name="EXPORT_NAME")
    private String exportName;

    /**
     * 查询语句
     */
    @Column(name="QUERY_SQL")
    private String querySql;

    /**
     * 创建人员
     */
    @Column(name="CREATED")
    private String created;

    /**
     * 导出后处理语句
     */
    @Column(name="AFTER_SQL_BLOCK")
    private String afterSqlBlock;

    /**
     * 导出说明
     */
    @Column(name="EXPORT_DESC")
    private String exportDesc;

    /**
     * 最后更新时间
     */
    @Column(name="LAST_UPDATE_TIME")
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    @Column(name="CREATE_TIME")
    private Date createTime;

    /**
     * 导出前处理语句
     */
    @Column(name="BEFORE_SQL_BLOCK")
    private String beforeSqlBlock;

    /**
     * 数据处理操作ID
     */
    @Column(name="Data_Opt_ID")
    private String dataOptId;

    /**
     * 存储类型
     */
    @Column(name="TABLE_STORE_TYPE")
    private String tableStoreType;

    @Lazy
    @OneToMany(targetEntity=ExportTrigger.class)
    @JoinColumn(name="EXPORT_ID", referencedColumnName="EXPORT_ID")
    private List<ExportTrigger> exportTriggers = null;

    @Lazy
    @OneToMany(targetEntity=ExportField.class)
    @JoinColumn(name="EXPORT_ID", referencedColumnName="EXPORT_ID")
    private List<ExportField> exportFields = null;// new
    // ArrayList<ExportField>();

    @Transient
    private Long exportsqlOrder;

    // Constructors

    /**
     * default constructor
     */
    public ExportSql() {
    }

    /**
     * minimal constructor
     */
    public ExportSql(Long exportId, String exportName) {

        this.exportId = exportId;

        this.exportName = exportName;
    }

    /**
     * full constructor
     */
    public ExportSql(Long exportId, String sourceDatabaseName, String sourceOsId, String exportName, String querySql,
                     String created, String afterSqlBlock, String exportDesc, Date lastUpdateTime, Date createTime,
                     String beforeSqlBlock, String mapinfoId, String tableStoreType) {

        this.exportId = exportId;

        this.sourceDatabaseName = sourceDatabaseName;
        this.sourceOsId = sourceOsId;
        this.exportName = exportName;
        this.querySql = querySql;
        this.created = created;
        this.afterSqlBlock = afterSqlBlock;
        this.exportDesc = exportDesc;
        this.lastUpdateTime = lastUpdateTime;
        this.createTime = createTime;
        this.beforeSqlBlock = beforeSqlBlock;
        this.dataOptId = mapinfoId;
        this.tableStoreType = tableStoreType;
    }

    public Long getExportId() {
        return this.exportId;
    }

    public void setExportId(Long exportId) {
        this.exportId = exportId;
    }

    // Property accessors

    public String getSourceDatabaseName() {
        return this.sourceDatabaseName;
    }

    public void setSourceDatabaseName(String sourceDatabaseName) {
        this.sourceDatabaseName = sourceDatabaseName;
    }

    public String getSourceOsId() {
        return this.sourceOsId;
    }

    public void setSourceOsId(String sourceOsId) {
        this.sourceOsId = sourceOsId;
    }

    public String getExportName() {
        return this.exportName;
    }

    public void setExportName(String exportName) {
        this.exportName = exportName;
    }

    public String getQuerySql() {
        return this.querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getAfterSqlBlock() {
        return this.afterSqlBlock;
    }

    public void setAfterSqlBlock(String afterSqlBlock) {
        this.afterSqlBlock = afterSqlBlock;
    }

    public String getExportDesc() {
        return this.exportDesc;
    }

    public void setExportDesc(String exportDesc) {
        this.exportDesc = exportDesc;
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

    public String getBeforeSqlBlock() {
        return this.beforeSqlBlock;
    }

    public void setBeforeSqlBlock(String beforeSqlBlock) {
        this.beforeSqlBlock = beforeSqlBlock;
    }

    public String getDataOptId() {
        return this.dataOptId;
    }

    public void setDataOptId(String mapinfoId) {
        this.dataOptId = mapinfoId;
    }

    /**
     * @return the tableStoreType
     */
    public String getTableStoreType() {
        return tableStoreType;
    }

    /**
     * @param tableStoreType the tableStoreType to set
     */
    public void setTableStoreType(String tableStoreType) {
        this.tableStoreType = tableStoreType;
    }


    public List<ExportTrigger> getExportTriggers() {
        if (this.exportTriggers == null)
            this.exportTriggers = new ArrayList<ExportTrigger>();
        return exportTriggers;
    }


    public void setExportTriggers(List<ExportTrigger> exportTriggers) {
        this.exportTriggers = exportTriggers;
    }


    public ExportTrigger getTrigger(int index) {
        if (this.exportTriggers == null)
            return null;
        return this.exportTriggers.get(index);
    }

    public void addExportTrigger(ExportTrigger exportTrigger) {
        if (this.exportTriggers == null)
            this.exportTriggers = new ArrayList<ExportTrigger>();
        this.exportTriggers.add(exportTrigger);
    }

    public void removeExportTrigger(ExportTrigger exportTrigger) {
        if (this.exportTriggers == null)
            return;
        this.exportTriggers.remove(exportTrigger);
    }

    public ExportTrigger newExportTrigger() {
        ExportTrigger res = new ExportTrigger();

        res.setExportId(this.getExportId());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */

    public void replaceExportTriggers(List<ExportTrigger> exportTriggers) {
        List<ExportTrigger> newObjs = new ArrayList<>();
        for (ExportTrigger p : exportTriggers) {
            if (p == null)
                continue;
            ExportTrigger newdt = newExportTrigger();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        // delete
        boolean found = false;
        Set<ExportTrigger> oldObjs = new HashSet<>();
        oldObjs.addAll(getExportTriggers());

        for (Iterator<ExportTrigger> it = oldObjs.iterator(); it.hasNext(); ) {
            ExportTrigger odt = it.next();
            found = false;
            for (ExportTrigger newdt : newObjs) {
                if (odt.getExportId().equals(newdt.getExportId()) && odt.getTriggerId().equals(newdt.getTriggerId())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                removeExportTrigger(odt);
        }
        oldObjs.clear();
        // insert or update
        for (ExportTrigger newdt : newObjs) {
            found = false;
            for (Iterator<ExportTrigger> it = getExportTriggers().iterator(); it.hasNext(); ) {
                ExportTrigger odt = it.next();
                if (odt.getExportId().equals(newdt.getExportId()) && odt.getTriggerId().equals(newdt.getTriggerId())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addExportTrigger(newdt);
        }
    }


    public ExportField getExportField(int index) {
        if (this.exportFields == null)
            return null;
        return this.exportFields.get(index);
    }

    public int getExportFieldCount() {
        if (this.exportFields == null)
            return 0;
        return this.exportFields.size();
    }

    public List<ExportField> getExportFields() {
        if (this.exportFields == null)
            this.exportFields = new ArrayList<ExportField>();
        return this.exportFields;
    }

    public void setExportFields(List<ExportField> exportFields) {
        this.exportFields = exportFields;
    }

    public void addExportField(ExportField exportField) {
        if (this.exportFields == null)
            this.exportFields = new ArrayList<ExportField>();
        this.exportFields.add(exportField);
    }

    public void removeExportField(ExportField exportField) {
        if (this.exportFields == null)
            return;
        this.exportFields.remove(exportField);
    }

    public ExportField newExportField() {
        ExportField res = new ExportField();

        res.setExportId(this.getExportId());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */
    public void replaceExportFields(List<ExportField> exportFields) {
        List<ExportField> newObjs = new ArrayList<ExportField>();
        for (ExportField p : exportFields) {
            if (p == null)
                continue;
            ExportField newdt = newExportField();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        // delete
        boolean found = false;
        Set<ExportField> oldObjs = new HashSet<ExportField>();
        oldObjs.addAll(getExportFields());

        for (Iterator<ExportField> it = oldObjs.iterator(); it.hasNext(); ) {
            ExportField odt = it.next();
            found = false;
            for (ExportField newdt : newObjs) {
                if (odt.getColumnNo().equals(newdt.getColumnNo()) && odt.getExportId().equals(newdt.getExportId())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                removeExportField(odt);
        }
        oldObjs.clear();
        // insert or update
        for (ExportField newdt : newObjs) {
            found = false;
            for (Iterator<ExportField> it = getExportFields().iterator(); it.hasNext(); ) {
                ExportField odt = it.next();
                if (odt.getColumnNo().equals(newdt.getColumnNo()) && odt.getExportId().equals(newdt.getExportId())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addExportField(newdt);
        }
    }

    public ExchangeTaskDetail newExchangeTaskdetail() {
        ExchangeTaskDetail res = new ExchangeTaskDetail();

        res.setMapInfoId(this.getExportId());

        return res;
    }

    public void copy(ExportSql other) {

        this.setExportId(other.getExportId());

        this.sourceDatabaseName = other.getSourceDatabaseName();
        this.sourceOsId = other.getSourceOsId();
        this.exportName = other.getExportName();
        this.querySql = other.getQuerySql();
        this.created = other.getCreated();
        this.afterSqlBlock = other.getAfterSqlBlock();
        this.exportDesc = other.getExportDesc();
        this.lastUpdateTime = other.getLastUpdateTime();
        this.createTime = other.getCreateTime();
        this.beforeSqlBlock = other.getBeforeSqlBlock();
        this.dataOptId = other.getDataOptId();
        this.tableStoreType = other.getTableStoreType();
        this.exportFields = other.getExportFields();

        this.exportTriggers = other.getExportTriggers();
    }

    public void copyNotNullProperty(ExportSql other) {

        if (other.getExportId() != null)
            this.setExportId(other.getExportId());

        if (other.getSourceDatabaseName() != null)
            this.sourceDatabaseName = other.getSourceDatabaseName();
        if (other.getSourceOsId() != null)
            this.sourceOsId = other.getSourceOsId();
        if (other.getExportName() != null)
            this.exportName = other.getExportName();
        if (other.getQuerySql() != null)
            this.querySql = other.getQuerySql();
        if (other.getCreated() != null)
            this.created = other.getCreated();
        if (other.getAfterSqlBlock() != null)
            this.afterSqlBlock = other.getAfterSqlBlock();
        if (other.getExportDesc() != null)
            this.exportDesc = other.getExportDesc();
        if (other.getLastUpdateTime() != null)
            this.lastUpdateTime = other.getLastUpdateTime();
        if (other.getCreateTime() != null)
            this.createTime = other.getCreateTime();
        if (other.getBeforeSqlBlock() != null)
            this.beforeSqlBlock = other.getBeforeSqlBlock();
        if (other.getDataOptId() != null)
            this.dataOptId = other.getDataOptId();
        if (other.getTableStoreType() != null)
            this.tableStoreType = other.getTableStoreType();
//		 this.exportFields = other.getExportFields();
    }

    public void clearProperties() {

        this.sourceDatabaseName = null;
        this.sourceOsId = null;
        this.exportName = null;
        this.querySql = null;
        this.created = null;
        this.afterSqlBlock = null;
        this.exportDesc = null;
        this.lastUpdateTime = null;
        this.createTime = null;
        this.beforeSqlBlock = null;
        this.dataOptId = null;
        this.tableStoreType = "0";
        this.exportFields = new ArrayList<ExportField>();
        this.exportTriggers = new ArrayList<ExportTrigger>();
    }

    public Long getExportsqlOrder() {
        return exportsqlOrder;
    }

    public void setExportsqlOrder(Long exportsqlOrder) {
        this.exportsqlOrder = exportsqlOrder;
    }

}
