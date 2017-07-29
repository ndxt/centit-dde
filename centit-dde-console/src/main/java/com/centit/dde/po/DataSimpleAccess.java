package com.centit.dde.po;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sx
 * Date: 13-7-10
 * Time: 下午3:10
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="DATASIMPLEACCESS")
public class DataSimpleAccess implements Serializable {
    private static final long serialVersionUID = 6776496325081845118L;

    @Id
    @Column(name = "TABLE")
    @GeneratedValue(generator = "assignedGenerator")
    @GenericGenerator(name = "assignedGenerator", strategy = "assigned")
    private String table;
    
    @Column(name = "FIELD")
    private String field;
    
    @Column(name = "WHERE")
    private String where;
    
    @Column(name = "ORDER")
    private String order;
    
    @Column(name = "PAGINATION")
    private String pagination;


    public DataSimpleAccess() {
    }

    public DataSimpleAccess(String table, String field) {
        this.table = table;
        this.field = field;
    }

    public DataSimpleAccess(String table, String field, String where) {
        this.table = table;
        this.field = field;
        this.where = where;
    }

    public DataSimpleAccess(String table, String field, String where, String order) {
        this.table = table;
        this.field = field;
        this.where = where;
        this.order = order;
    }

    public DataSimpleAccess(String table, String field, String where, String order, String pagination) {
        this.table = table;
        this.field = field;
        this.where = where;
        this.order = order;
        this.pagination = pagination;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPagination() {
        return pagination;
    }

    public void setPagination(String pagination) {
        this.pagination = pagination;
    }
}
