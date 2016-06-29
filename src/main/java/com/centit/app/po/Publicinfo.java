package com.centit.app.po;

import java.util.Date;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class Publicinfo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String infocode;
    private String parentinfocode;

    private Fileinfo fileinfo;

    private String filename;
    private String fileextension;
    private String ownercode;
    private String uploader;
    private Long readcount;
    private Long downloadcount;
    private Date uploadtime;
    private Date modifytime;
    private String status;
    private String type;
    private String isfolder;
    private String filedescription;
    private String unitcode;
    private String authority;
    private Long filesize;
    private String zipPath;

    public String getZipPath() {
        return zipPath;
    }

    public void setZipPath(String zipPath) {
        this.zipPath = zipPath;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }

    // Constructors

    /**
     * default constructor
     */
    public Publicinfo() {
    }

    /**
     * minimal constructor
     */
    public Publicinfo(String infocode) {

        this.infocode = infocode;

    }

    /**
     * full constructor
     */
    public Publicinfo(String infocode, String parentinfocode, Fileinfo fileinfo,
                      String filename, String fileextension, String ownercode,
                      Long readcount, Long downloadcount, String md5, Date uploadtime,
                      Date modifytimes, String status, String type, String isfolder,
                      String filedescription) {

        this.infocode = infocode;

        this.parentinfocode = parentinfocode;
        this.fileinfo = fileinfo;
        this.filename = filename;
        this.fileextension = fileextension;
        this.ownercode = ownercode;
        this.readcount = readcount;
        this.downloadcount = downloadcount;
        this.uploadtime = uploadtime;
        this.modifytime = modifytimes;
        this.status = status;
        this.type = type;
        this.isfolder = isfolder;
        this.filedescription = filedescription;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitCode) {
        this.unitcode = unitCode;
    }

    public String getInfocode() {
        return this.infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    // Property accessors

    public String getParentinfocode() {
        return this.parentinfocode;
    }

    public void setParentinfocode(String parentinfocode) {
        this.parentinfocode = parentinfocode;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileextension() {
        return this.fileextension;
    }

    public void setFileextension(String fileextension) {
        this.fileextension = fileextension;
    }

    public String getOwnercode() {
        return this.ownercode;
    }

    public void setOwnercode(String ownercode) {
        this.ownercode = ownercode;
    }

    public Long getReadcount() {
        return this.readcount;
    }

    public void setReadcount(Long readcount) {
        this.readcount = readcount;
    }

    public Long getDownloadcount() {
        return this.downloadcount;
    }

    public void setDownloadcount(Long downloadcount) {
        this.downloadcount = downloadcount;
    }

    public Date getUploadtime() {
        return this.uploadtime;
    }

    public void setUploadtime(Date uploadtime) {
        this.uploadtime = uploadtime;
    }

    public Date getModifytime() {
        return this.modifytime;
    }

    public void setModifytime(Date modifytimes) {
        this.modifytime = modifytimes;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsfolder() {
        return this.isfolder;
    }

    public void setIsfolder(String isfolder) {
        this.isfolder = isfolder;
    }

    public String getFiledescription() {
        return this.filedescription;
    }

    public void setFiledescription(String filedescription) {
        this.filedescription = filedescription;
    }

    public Fileinfo getFileinfo() {
        return fileinfo;
    }

    public void setFileinfo(Fileinfo fileinfo) {
        this.fileinfo = fileinfo;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */

    public void copy(Publicinfo other) {

        this.setInfocode(other.getInfocode());

        this.parentinfocode = other.getParentinfocode();
        this.fileinfo = other.getFileinfo();
        this.filename = other.getFilename();
        this.fileextension = other.getFileextension();
        this.ownercode = other.getOwnercode();
        this.readcount = other.getReadcount();
        this.downloadcount = other.getDownloadcount();
        this.uploadtime = other.getUploadtime();
        this.modifytime = other.getModifytime();
        this.status = other.getStatus();
        this.type = other.getType();
        this.isfolder = other.getIsfolder();
        this.filedescription = other.getFiledescription();

        this.unitcode = other.getUnitcode();
    }

    public void copyNotNullProperty(Publicinfo other) {

        if (other.getInfocode() != null)
            this.setInfocode(other.getInfocode());

        if (other.getParentinfocode() != null)
            this.parentinfocode = other.getParentinfocode();
        if (other.getFileinfo() != null)
            this.fileinfo = other.getFileinfo();
        if (other.getFilename() != null)
            this.filename = other.getFilename();
        if (other.getFileextension() != null)
            this.fileextension = other.getFileextension();
        if (other.getOwnercode() != null)
            this.ownercode = other.getOwnercode();
        if (other.getReadcount() != null)
            this.readcount = other.getReadcount();
        if (other.getDownloadcount() != null)
            this.downloadcount = other.getDownloadcount();
        if (other.getUploadtime() != null)
            this.uploadtime = other.getUploadtime();
        if (other.getModifytime() != null)
            this.modifytime = other.getModifytime();
        if (other.getStatus() != null)
            this.status = other.getStatus();
        if (other.getType() != null)
            this.type = other.getType();
        if (other.getIsfolder() != null)
            this.isfolder = other.getIsfolder();
        if (other.getFiledescription() != null)
            this.filedescription = other.getFiledescription();
        if (null != other.getUnitcode()) {
            this.unitcode = other.getUnitcode();
        }

    }

    public void clearProperties() {

        this.parentinfocode = null;
        this.fileinfo = null;
        this.filename = null;
        this.fileextension = null;
        this.ownercode = null;
        this.readcount = null;
        this.downloadcount = null;
        this.uploadtime = null;
        this.modifytime = null;
        this.status = null;
        this.type = null;
        this.isfolder = null;
        this.filedescription = null;

    }

}
