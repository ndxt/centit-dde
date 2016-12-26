package com.centit.dde.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */
@Entity
@Table(name="D_IMPORT_OPT")
public class ImportOpt implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="IMPORTID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private Long importId;

    @Column(name="DESTDATABASENAME")
    private String destDatabaseName;

    @Column(name="SOURCEOSID")
    private String sourceOsId;

    @Column(name="IMPORTNAME")
    private String importName;

    @Column(name="TABLENAME")
    private String tableName;

    @Column(name="CREATED")
    private String created;

    @Column(name="IMPORTDESC")
    private String importDesc;

    @Column(name="LASTUPDATETIME")
    private Date lastUpdateTime;

    @Column(name="CREATETIME")
    private Date createTime;

    @Column(name="RECORDOPERATE")
    private String recordOperate;

    @OneToMany(orphanRemoval=true,fetch=FetchType.LAZY)
    @JoinColumn(name="importId") //这里表示数据库的外键 在t_street里面创建
    private List<ImportTrigger> importTriggers = null;
    
    @OneToMany(orphanRemoval=true,fetch=FetchType.LAZY)
    @JoinColumn(name="importId") //这里表示数据库的外键 在t_street里面创建
    private List<ImportField> importFields = null;// new
    // ArrayList<ImportField>();

    // Constructors

    /**
     * default constructor
     */
    public ImportOpt() {   }

    /**
     * minimal constructor
     */
    public ImportOpt(Long importId, String importName) {

        this.importId = importId;

        this.importName = importName;
    }

    /**
     * full constructor
     */
    public ImportOpt(Long importId, String destDatabaseName, String sourceOsId, String importName, String tableName,
                     String created, String afterImportBlock, String beforeImportBlock, String importDesc, Date lastUpdateTime,
                     String recordOperate, Date createTime) {

        this.importId = importId;

        this.destDatabaseName = destDatabaseName;
        this.sourceOsId = sourceOsId;
        this.importName = importName;
        this.tableName = tableName;
        this.created = created;
        this.importDesc = importDesc;
        this.lastUpdateTime = lastUpdateTime;

        this.recordOperate = recordOperate;
        this.createTime = createTime;
    }

    public Long getImportId() {
        return this.importId;
    }

    public void setImportId(Long importId) {
        this.importId = importId;
    }

    // Property accessors

    public String getDestDatabaseName() {
        return this.destDatabaseName;
    }

    public void setDestDatabaseName(String destDatabaseName) {
        this.destDatabaseName = destDatabaseName;
    }

    public String getSourceOsId() {
        return this.sourceOsId;
    }

    public void setSourceOsId(String sourceOsId) {
        this.sourceOsId = sourceOsId;
    }

    public String getImportName() {
        return this.importName;
    }

    public void setImportName(String importName) {
        this.importName = importName;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCreated() {
        return this.created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getImportDesc() {
        return importDesc;
    }

    public void setImportDesc(String importDesc) {
        this.importDesc = importDesc;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getRecordOperate() {
        return recordOperate;
    }

    public void setRecordOperate(String recordOperate) {
        this.recordOperate = recordOperate;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


    public List<ImportTrigger> getImportTriggers() {
        if (this.importTriggers == null)
            this.importTriggers = new ArrayList<ImportTrigger>();
        return importTriggers;
    }


    public void setImportTriggers(List<ImportTrigger> importTriggers) {
        this.importTriggers = importTriggers;
    }


    public ImportTrigger getTrigger(int index) {
        if (this.importTriggers == null)
            return null;
        return this.importTriggers.get(index);
    }

    public void addImportTrigger(ImportTrigger importTrigger) {
        if (this.importTriggers == null)
            this.importTriggers = new ArrayList<ImportTrigger>();
        this.importTriggers.add(importTrigger);
    }

    public void removeImportTrigger(ImportTrigger importTrigger) {
        if (this.importTriggers == null)
            return;
        this.importTriggers.remove(importTrigger);
    }

    public ImportTrigger newImportTrigger() {
        ImportTrigger res = new ImportTrigger();

        res.setImportId(this.getImportId());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */
    public void replaceImportTriggers(List<ImportTrigger> importTriggers) {
        List<ImportTrigger> newObjs = new ArrayList<ImportTrigger>();
        for (ImportTrigger p : importTriggers) {
            if (p == null)
                continue;
            ImportTrigger newdt = newImportTrigger();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        // delete
        boolean found = false;
        Set<ImportTrigger> oldObjs = new HashSet<ImportTrigger>();
        oldObjs.addAll(getImportTriggers());

        for (Iterator<ImportTrigger> it = oldObjs.iterator(); it.hasNext(); ) {
            ImportTrigger odt = it.next();
            found = false;
            for (ImportTrigger newdt : newObjs) {
                if (odt.getCid().equals(newdt.getCid())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                removeImportTrigger(odt);
        }
        oldObjs.clear();
        // insert or update
        for (ImportTrigger newdt : newObjs) {
            found = false;
            for (Iterator<ImportTrigger> it = getImportTriggers().iterator(); it.hasNext(); ) {
                ImportTrigger odt = it.next();
                if (odt.getCid().equals(newdt.getCid())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addImportTrigger(newdt);
        }
    }
/*----------------------------------------------------------------------*/


    public List<ImportField> getImportFields() {
        if (this.importFields == null)
            this.importFields = new ArrayList<ImportField>();
        return this.importFields;
    }


    public void setImportFields(List<ImportField> importFields) {
        this.importFields = importFields;
    }

    public String getSourceFieldName(int index) {
        if (this.importFields == null)
            return "";
        ImportField field = this.importFields.get(index);
        if (field == null)
            return "";
        return field.getSourceFieldName();
    }

    public ImportField getField(int index) {
        if (this.importFields == null)
            return null;
        return this.importFields.get(index);
    }

    public void addImportField(ImportField importField) {
        if (this.importFields == null)
            this.importFields = new ArrayList<ImportField>();
        this.importFields.add(importField);
    }

    public void removeImportField(ImportField importField) {
        if (this.importFields == null)
            return;
        this.importFields.remove(importField);
    }

    public ImportField newImportField() {
        ImportField res = new ImportField();

        res.setImportId(this.getImportId());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */
    public void replaceImportFields(List<ImportField> importFields) {
        List<ImportField> newObjs = new ArrayList<ImportField>();
        for (ImportField p : importFields) {
            if (p == null)
                continue;
            ImportField newdt = newImportField();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        // delete
        boolean found = false;
        Set<ImportField> oldObjs = new HashSet<ImportField>();
        oldObjs.addAll(getImportFields());

        for (Iterator<ImportField> it = oldObjs.iterator(); it.hasNext(); ) {
            ImportField odt = it.next();
            found = false;
            for (ImportField newdt : newObjs) {
                if (odt.getCid().equals(newdt.getCid())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                removeImportField(odt);
        }
        oldObjs.clear();
        // insert or update
        for (ImportField newdt : newObjs) {
            found = false;
            for (Iterator<ImportField> it = getImportFields().iterator(); it.hasNext(); ) {
                ImportField odt = it.next();
                if (odt.getCid().equals(newdt.getCid())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addImportField(newdt);
        }
    }

    public void copy(ImportOpt other) {

        this.setImportId(other.getImportId());

        this.destDatabaseName = other.getDestDatabaseName();
        this.sourceOsId = other.getSourceOsId();
        this.importName = other.getImportName();
        this.tableName = other.getTableName();
        this.created = other.getCreated();
        this.importDesc = other.getImportDesc();
        this.lastUpdateTime = other.getLastUpdateTime();
        this.createTime = other.getCreateTime();
        this.recordOperate = other.getRecordOperate();
        this.importFields = other.getImportFields();
        this.importTriggers = other.getImportTriggers();

    }

    public void copyNotNullProperty(ImportOpt other) {

        if (other.getImportId() != null)
            this.setImportId(other.getImportId());

        if (other.getDestDatabaseName() != null)
            this.destDatabaseName = other.getDestDatabaseName();
        if (other.getSourceOsId() != null)
            this.sourceOsId = other.getSourceOsId();
        if (other.getImportName() != null)
            this.importName = other.getImportName();
        if (other.getTableName() != null)
            this.tableName = other.getTableName();
        if (other.getCreated() != null)
            this.created = other.getCreated();
        if (other.getImportDesc() != null)
            this.importDesc = other.getImportDesc();
        if (other.getLastUpdateTime() != null)
            this.lastUpdateTime = other.getLastUpdateTime();
        if (other.getRecordOperate() != null)
            this.recordOperate = other.getRecordOperate();
        if (other.getCreateTime() != null)
            this.createTime = other.getCreateTime();

        // this.importFields = other.getImportFields();
    }

    public void clearProperties() {

        this.destDatabaseName = null;
        this.sourceOsId = null;
        this.importName = null;
        this.tableName = null;
        this.created = null;
        this.importDesc = null;
        this.lastUpdateTime = null;
        this.createTime = null;
        this.recordOperate = null;
        this.importFields = new ArrayList<ImportField>();
        this.importTriggers = new ArrayList<ImportTrigger>();
    }
}
