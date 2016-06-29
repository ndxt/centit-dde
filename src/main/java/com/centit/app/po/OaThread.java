package com.centit.app.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * create by scaffold
 *
 * @author codefan@hotmail.com
 */

public class OaThread implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private Long threadid;

    private Long forumid;
    private String titol;
    private String content;
    private String wirterid;
    private String wirter;
    private Date posttime;
    private Long viewnum;
    private Long replnum;
    private Set<OaReply> oaReplys = null;// new ArrayList<OaReply>();

    // Constructors

    /**
     * default constructor
     */
    public OaThread() {
    }

    /**
     * minimal constructor
     */
    public OaThread(
            Long threadid
    ) {


        this.threadid = threadid;

    }

    /**
     * full constructor
     */
    public OaThread(
            Long threadid
            , Long forumid, String titol, String content, String wirterid, String wirter, Date posttime, Long viewnum, Long replnum) {


        this.threadid = threadid;

        this.forumid = forumid;
        this.titol = titol;
        this.content = content;
        this.wirterid = wirterid;
        this.wirter = wirter;
        this.posttime = posttime;
        this.viewnum = viewnum;
        this.replnum = replnum;
    }


    public Long getThreadid() {
        return this.threadid;
    }

    public void setThreadid(Long threadid) {
        this.threadid = threadid;
    }
    // Property accessors

    public Long getForumid() {
        return this.forumid;
    }

    public void setForumid(Long forumid) {
        this.forumid = forumid;
    }

    public String getTitol() {
        return this.titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWirterid() {
        return this.wirterid;
    }

    public void setWirterid(String wirterid) {
        this.wirterid = wirterid;
    }

    public String getWirter() {
        return this.wirter;
    }

    public void setWirter(String wirter) {
        this.wirter = wirter;
    }

    public Date getPosttime() {
        return this.posttime;
    }

    public void setPosttime(Date posttime) {
        this.posttime = posttime;
    }

    public Long getViewnum() {
        return this.viewnum;
    }

    public void setViewnum(Long viewnum) {
        this.viewnum = viewnum;
    }

    public Long getReplnum() {
        return this.replnum;
    }

    public void setReplnum(Long replnum) {
        this.replnum = replnum;
    }


    public Set<OaReply> getOaReplys() {
        if (this.oaReplys == null)
            this.oaReplys = new HashSet<OaReply>();
        return this.oaReplys;
    }

    public void setOaReplys(Set<OaReply> oaReplys) {
        this.oaReplys = oaReplys;
    }

    public void addOaReply(OaReply oaReply) {
        if (this.oaReplys == null)
            this.oaReplys = new HashSet<OaReply>();
        this.oaReplys.add(oaReply);
    }

    public void removeOaReply(OaReply oaReply) {
        if (this.oaReplys == null)
            return;
        this.oaReplys.remove(oaReply);
    }

    public OaReply newOaReply() {
        OaReply res = new OaReply();

        res.setThreadid(this.getThreadid());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */
    public void replaceOaReplys(List<OaReply> oaReplys) {
        List<OaReply> newObjs = new ArrayList<OaReply>();
        for (OaReply p : oaReplys) {
            if (p == null)
                continue;
            OaReply newdt = newOaReply();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        //delete
        boolean found = false;
        Set<OaReply> oldObjs = new HashSet<OaReply>();
        oldObjs.addAll(getOaReplys());

        for (Iterator<OaReply> it = oldObjs.iterator(); it.hasNext(); ) {
            OaReply odt = it.next();
            found = false;
            for (OaReply newdt : newObjs) {
                if (odt.getReplyid().equals(newdt.getReplyid())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                removeOaReply(odt);
        }
        oldObjs.clear();
        //insert or update
        for (OaReply newdt : newObjs) {
            found = false;
            for (Iterator<OaReply> it = getOaReplys().iterator();
                 it.hasNext(); ) {
                OaReply odt = it.next();
                if (odt.getReplyid().equals(newdt.getReplyid())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addOaReply(newdt);
        }
    }


    public void copy(OaThread other) {

        this.setThreadid(other.getThreadid());

        this.forumid = other.getForumid();
        this.titol = other.getTitol();
        this.content = other.getContent();
        this.wirterid = other.getWirterid();
        this.wirter = other.getWirter();
        this.posttime = other.getPosttime();
        this.viewnum = other.getViewnum();
        this.replnum = other.getReplnum();

        this.oaReplys = other.getOaReplys();
    }

    public void copyNotNullProperty(OaThread other) {

        if (other.getThreadid() != null)
            this.setThreadid(other.getThreadid());

        if (other.getForumid() != null)
            this.forumid = other.getForumid();
        if (other.getTitol() != null)
            this.titol = other.getTitol();
        if (other.getContent() != null)
            this.content = other.getContent();
        if (other.getWirterid() != null)
            this.wirterid = other.getWirterid();
        if (other.getWirter() != null)
            this.wirter = other.getWirter();
        if (other.getPosttime() != null)
            this.posttime = other.getPosttime();
        if (other.getViewnum() != null)
            this.viewnum = other.getViewnum();
        if (other.getReplnum() != null)
            this.replnum = other.getReplnum();

        this.oaReplys = other.getOaReplys();
    }

    public void clearProperties() {

        this.forumid = null;
        this.titol = null;
        this.content = null;
        this.wirterid = null;
        this.wirter = null;
        this.posttime = null;
        this.viewnum = null;
        this.replnum = null;

        this.oaReplys = new HashSet<OaReply>();
    }
}
