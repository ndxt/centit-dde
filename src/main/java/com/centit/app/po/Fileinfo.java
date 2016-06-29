package com.centit.app.po;

import java.util.Date;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class Fileinfo implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private String filecode;

    private String filename;

    private String path;

    private String fileext;

    private String filedsc;

    private String optcode;

    private Long instid;

    private String recorder;

    private Date recorderDate;

    private String indb;

    private Long filesize;

    private String md5;

    private String isindex;

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getIsindex() {
        return isindex;
    }

    public void setIsindex(String isindex) {
        this.isindex = isindex;
    }

    public String getFilecode() {
        return this.filecode;
    }

    public void setFilecode(String filecode) {
        this.filecode = filecode;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileext() {
        return fileext;
    }

    public void setFileext(String fileext) {
        this.fileext = fileext;
    }

    public String getFiledsc() {
        return filedsc;
    }

    public void setFiledsc(String filedsc) {
        this.filedsc = filedsc;
    }

    public String getOptcode() {
        return optcode;
    }

    public void setOptcode(String optcode) {
        this.optcode = optcode;
    }

    public Long getInstid() {
        return instid;
    }

    public void setInstid(Long instid) {
        this.instid = instid;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public Date getRecorderDate() {
        return recorderDate;
    }

    public void setRecorderDate(Date recorderDate) {
        this.recorderDate = recorderDate;
    }

    public String getIndb() {
        return indb;
    }

    public void setIndb(String indb) {
        this.indb = indb;
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }


    public void copy(Fileinfo other) {

        this.setFilecode(other.getFilecode());

        this.filename = other.getFilename();
        this.path = other.getPath();
        this.filedsc = other.getFiledsc();
        this.fileext = other.getFileext();
        this.filesize = other.getFilesize();

        this.indb = other.getIndb();
        this.optcode = other.getOptcode();
        this.instid = other.getInstid();
        this.recorder = other.getRecorder();
        this.recorderDate = other.getRecorderDate();

        this.md5 = other.getMd5();
        this.isindex = other.getIsindex();

    }

    public void copyNotNullProperty(Fileinfo other) {

        if (other.getFilecode() != null)
            this.setFilecode(other.getFilecode());

        if (other.getFilename() != null)
            this.filename = other.getFilename();

        if (other.getPath() != null)
            this.path = other.getPath();

        if (other.getFiledsc() != null) {
            this.filedsc = other.getFiledsc();
        }

        if (other.getFileext() != null) {
            this.fileext = other.getFileext();
        }

        if (other.getFilesize() != 0) {
            this.filesize = other.getFilesize();
        }

        if (!"".equals(other.getIndb())) {
            this.indb = other.getIndb();
        }

        if (other.getInstid() != 0) {
            this.instid = other.getInstid();
        }

        if (other.getOptcode() != null) {
            this.optcode = other.getOptcode();
        }

        if (other.getRecorder() != null) {
            this.recorder = other.getRecorder();
        }

        if (other.getRecorderDate() != null) {
            this.recorderDate = other.getRecorderDate();
        }

        if (other.getMd5() != null) {
            this.md5 = other.getMd5();
        }

        if (!"".equals(other.getIsindex())) {
            this.isindex = other.getIsindex();
        }
    }

    public void clearProperties() {

        this.filename = null;
        this.path = null;
        this.filedsc = null;
        this.fileext = null;
        this.filesize = 0L;

        this.indb = null;
        this.optcode = null;
        this.instid = 1L;
        this.recorder = null;
        this.recorderDate = null;

        this.md5 = null;
        this.isindex = null;
    }
}
