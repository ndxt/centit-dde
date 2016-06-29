package com.centit.sys.po;

/**
 * FDatadictionaryId entity.
 *
 * @author MyEclipse Persistence Tools
 * @author codefan
 */
//数据字典的主键
public class FDatadictionaryId implements java.io.Serializable {
    private static final long serialVersionUID = -477457533900099603L;
    // Fields
    private String catalogcode; //类别代码
    private String datacode;    //数据代码

    // Constructors

    /**
     * default constructor
     */
    public FDatadictionaryId() {
    }

    /**
     * full constructor
     */
    public FDatadictionaryId(String catalogcode, String datacode) {
        this.catalogcode = catalogcode;
        this.datacode = datacode;
    }

    // Property accessors

    public String getCatalogcode() {
        return this.catalogcode;
    }

    public void setCatalogcode(String catalogcode) {
        this.catalogcode = catalogcode;
    }

    public String getDatacode() {
        return this.datacode;
    }

    public void setDatacode(String datacode) {
        this.datacode = datacode;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof FDatadictionaryId))
            return false;
        FDatadictionaryId castOther = (FDatadictionaryId) other;

        return ((this.getCatalogcode() == castOther.getCatalogcode()) || (this
                .getCatalogcode() != null
                && castOther.getCatalogcode() != null && this.getCatalogcode()
                .equals(castOther.getCatalogcode())))
                && ((this.getDatacode() == castOther.getDatacode()) || (this
                .getDatacode() != null
                && castOther.getDatacode() != null && this
                .getDatacode().equals(castOther.getDatacode())));
    }

    public int hashCode() {
        int result = 17;

        result = 37
                * result
                + (getCatalogcode() == null ? 0 : this.getCatalogcode()
                .hashCode());
        result = 37 * result
                + (getDatacode() == null ? 0 : this.getDatacode().hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "[类别代码=" + catalogcode + ", 数据代码=" + datacode + "]";
    }

}