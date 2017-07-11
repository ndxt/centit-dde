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
@Table(name="D_EXCHANGE_MAPINFO")
public class ExchangeMapInfo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="MAPINFO_ID")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private Long mapInfoId;
    
    @Column(name="DEST_DATABASE_NAME")
    private String destDatabaseName;
    
    @Column(name="MAPINFO_NAME")
    private String mapInfoName;
    
    @Column(name="QUERY_SQL")
    private String querySql;
    
    @Column(name="SOURCE_DATABASE_NAME")
    private String sourceDatabaseName;

    @Column(name="SOURCE_TABLENAME")
    private String sourceTableName;

    @Column(name="DEST_TABLENAME")
    private String destTableName;

    @Column(name="IS_REPEAT")
    private String isRepeat;

    @Column(name="MAPINFO_DESC")
    private String mapInfoDesc;

    @Column(name="RECORD_OPERATE")
    private String recordOperate;

    @Transient
    private Long mapInfoOrder;

    @OneToMany(orphanRemoval=true,fetch=FetchType.LAZY)
    @JoinColumn(name="MAPINFO_ID")
    private List<MapInfoDetail> mapInfoDetails = null;

    @OneToMany(orphanRemoval=true,fetch=FetchType.LAZY)
    @JoinColumn(name="MAPINFO_ID")
    private List<MapInfoTrigger> mapInfoTriggers = null;

    @OneToMany(orphanRemoval=true,fetch=FetchType.LAZY)
    @JoinColumn(name="MAPINFO_ID") //这里表示数据库的外键 在t_street里面创建
    private List<ExchangeTaskDetail> exchangeTaskDetails = null;

    public List<ExchangeTaskDetail> getExchangeTaskDetails() {
        if (null == exchangeTaskDetails) {
            exchangeTaskDetails = new ArrayList<ExchangeTaskDetail>();
        }
        return exchangeTaskDetails;
    }

    public void setExchangeTaskDetails(List<ExchangeTaskDetail> exchangeTaskDetails) {
        this.exchangeTaskDetails = exchangeTaskDetails;
    }

    public String getRecordOperate() {
        return recordOperate;
    }

    public void setRecordOperate(String recordOperate) {
        this.recordOperate = recordOperate;
    }

    public Long getMapInfoOrder() {
        return mapInfoOrder;
    }

    public void setMapInfoOrder(Long mapInfoOrder) {
        this.mapInfoOrder = mapInfoOrder;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName;
    }

    public List<MapInfoDetail> getMapInfoDetails() {
        if (null == mapInfoDetails) {
            mapInfoDetails = new ArrayList<>();
        }
        return mapInfoDetails;
    }

    public void setMapInfoDetails(List<MapInfoDetail> mapInfoDetails) {
        this.mapInfoDetails = mapInfoDetails;
    }

    public List<MapInfoTrigger> getMapInfoTriggers() {
        if (null == mapInfoTriggers) {
            mapInfoTriggers = new ArrayList<MapInfoTrigger>();
        }
        return mapInfoTriggers;
    }

    public void setMapInfoTriggers(List<MapInfoTrigger> mapInfoTriggers) {
        this.mapInfoTriggers = mapInfoTriggers;
    }

    // Constructors

    /**
     * default constructor
     */
    public ExchangeMapInfo() {
    }

    /**
     * minimal constructor
     */
    public ExchangeMapInfo(Long mapInfoId, String mapInfoName) {

        this.mapInfoId = mapInfoId;

        this.mapInfoName = mapInfoName;
    }

    /**
     * full constructor
     */
    public ExchangeMapInfo(Long mapInfoId, String destDatabaseName, String mapInfoName, String querySql,
                           String sourceDatabaseName, String destTableName, String isRepeat, String mapInfoDesc) {

        this.mapInfoId = mapInfoId;

        this.destDatabaseName = destDatabaseName;
        this.mapInfoName = mapInfoName;
        this.querySql = querySql;
        this.sourceDatabaseName = sourceDatabaseName;
        this.destTableName = destTableName;
        this.isRepeat = isRepeat;
        this.mapInfoDesc = mapInfoDesc;
    }

    public Long getMapInfoId() {
        return this.mapInfoId;
    }

    public void setMapInfoId(Long mapInfoId) {
        this.mapInfoId = mapInfoId;
    }

    // Property accessors

    public String getDestDatabaseName() {
        return this.destDatabaseName;
    }

    public void setDestDatabaseName(String destDatabaseName) {
        this.destDatabaseName = destDatabaseName;
    }

    public String getMapInfoName() {
        return this.mapInfoName;
    }

    public void setMapInfoName(String mapInfoName) {
        this.mapInfoName = mapInfoName;
    }

    public String getQuerySql() {
        return this.querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public String getSourceDatabaseName() {
        return this.sourceDatabaseName;
    }

    public void setSourceDatabaseName(String sourceDatabaseName) {
        this.sourceDatabaseName = sourceDatabaseName;
    }

    public String getDestTableName() {
        return this.destTableName;
    }

    public void setDestTableName(String destTableName) {
        this.destTableName = destTableName;
    }

    public String getIsRepeat() {
        return this.isRepeat;
    }

    public void setIsRepeat(String isRepeat) {
        this.isRepeat = isRepeat;
    }

    public String getMapInfoDesc() {
        return this.mapInfoDesc;
    }

    public void setMapInfoDesc(String mapInfoDesc) {
        this.mapInfoDesc = mapInfoDesc;
    }

    public void addMapInfoDetail(MapInfoDetail mapInfoDetail) {
        if (this.mapInfoDetails == null)
            this.mapInfoDetails = new ArrayList<MapInfoDetail>();
        this.mapInfoDetails.add(mapInfoDetail);
    }

    public void removeMapInfoDetail(MapInfoDetail mapInfoDetail) {
        if (this.mapInfoDetails == null)
            return;
        this.mapInfoDetails.remove(mapInfoDetail);
    }

    public MapInfoDetail newMapInfoDetail() {
        MapInfoDetail res = new MapInfoDetail();

        res.setMapinfoId(this.getMapInfoId());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */
    public void replaceMapInfoDetails(List<MapInfoDetail> mapInfoDetails) {
        List<MapInfoDetail> newObjs = new ArrayList<>();
        for (MapInfoDetail p : mapInfoDetails) {
            if (p == null) {
                continue;
            }
            MapInfoDetail newdt = newMapInfoDetail();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        // delete
        boolean found = false;
        Set<MapInfoDetail> oldObjs = new HashSet<>();
        oldObjs.addAll(getMapInfoDetails());

        for (Iterator<MapInfoDetail> it = oldObjs.iterator(); it.hasNext(); ) {
            MapInfoDetail odt = it.next();
            found = false;
            for (MapInfoDetail newdt : newObjs) {
                if (odt.getCid().equals(newdt.getCid())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                removeMapInfoDetail(odt);
            }
        }
        oldObjs.clear();
        // insert or update
        for (MapInfoDetail newdt : newObjs) {
            found = false;
            for (Iterator<MapInfoDetail> it = getMapInfoDetails().iterator(); it.hasNext(); ) {
                MapInfoDetail odt = it.next();
                if (odt.getCid().equals(newdt.getCid())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addMapInfoDetail(newdt);
        }
    }

    public void addMapInfoTrigger(MapInfoTrigger mapInfoTrigger) {
        if (this.mapInfoTriggers == null)
            this.mapInfoTriggers = new ArrayList<>();
        this.mapInfoTriggers.add(mapInfoTrigger);
    }

    public void removeMapInfoTrigger(MapInfoTrigger mapInfoTrigger) {
        if (this.mapInfoTriggers == null)
            return;
        this.mapInfoTriggers.remove(mapInfoTrigger);
    }

    public MapInfoTrigger newMapInfoTrigger() {
        MapInfoTrigger res = new MapInfoTrigger();

        res.setMapinfoId(this.getMapInfoId());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */
    public void replaceMapInfoTriggers(List<MapInfoTrigger> mapInfoTriggers) {

        List<MapInfoTrigger> newObjs = new ArrayList<>();
        for (MapInfoTrigger p : mapInfoTriggers) {
            if (p == null)
                continue;
            MapInfoTrigger newdt = newMapInfoTrigger();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        // delete
        boolean found = false;
        Set<MapInfoTrigger> oldObjs = new HashSet<>();
        oldObjs.addAll(getMapInfoTriggers());

        for (Iterator<MapInfoTrigger> it = oldObjs.iterator(); it.hasNext(); ) {
            MapInfoTrigger odt = it.next();
            found = false;
            for (MapInfoTrigger newdt : newObjs) {
                if (odt.getCid().equals(newdt.getCid())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                removeMapInfoTrigger(odt);
        }
        oldObjs.clear();
        // insert or update
        for (MapInfoTrigger newdt : newObjs) {
            found = false;
            for (Iterator<MapInfoTrigger> it = getMapInfoTriggers().iterator(); it.hasNext(); ) {
                MapInfoTrigger odt = it.next();
                if (odt.getCid().equals(newdt.getCid())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addMapInfoTrigger(newdt);
        }
    }

    public void copy(ExchangeMapInfo other) {

        this.setMapInfoId(other.getMapInfoId());

        this.destDatabaseName = other.getDestDatabaseName();
        this.mapInfoName = other.getMapInfoName();
        this.querySql = other.getQuerySql();
        this.sourceDatabaseName = other.getSourceDatabaseName();
        this.destTableName = other.getDestTableName();
        this.isRepeat = other.getIsRepeat();
        this.mapInfoDesc = other.getMapInfoDesc();

    }

    public void copyNotNullProperty(ExchangeMapInfo other) {

        if (other.getMapInfoId() != null)
            this.setMapInfoId(other.getMapInfoId());

        if (other.getDestDatabaseName() != null)
            this.destDatabaseName = other.getDestDatabaseName();
        if (other.getMapInfoName() != null)
            this.mapInfoName = other.getMapInfoName();
        if (other.getQuerySql() != null)
            this.querySql = other.getQuerySql();
        if (other.getSourceDatabaseName() != null)
            this.sourceDatabaseName = other.getSourceDatabaseName();
        if (other.getDestTableName() != null)
            this.destTableName = other.getDestTableName();
        if (other.getIsRepeat() != null)
            this.isRepeat = other.getIsRepeat();
        if (other.getMapInfoDesc() != null)
            this.mapInfoDesc = other.getMapInfoDesc();
        if (null != other.getRecordOperate()) {
            this.recordOperate = other.getRecordOperate();
        }

    }

    public void clearProperties() {

        this.destDatabaseName = null;
        this.mapInfoName = null;
        this.querySql = null;
        this.sourceDatabaseName = null;
        this.destTableName = null;
        this.isRepeat = null;
        this.mapInfoDesc = null;

    }

    public void addMapinfoDetail(MapInfoDetail mapInfoDetail) {
        if (null == mapInfoDetails) {
            mapInfoDetails = new ArrayList<MapInfoDetail>();
        }

        mapInfoDetails.add(mapInfoDetail);
    }

    public void addMapinfoTrigger(MapInfoTrigger mapInfoTrigger) {
        if (null == mapInfoTriggers) {
            mapInfoTriggers = new ArrayList<MapInfoTrigger>();
        }

        mapInfoTriggers.add(mapInfoTrigger);
    }
}
