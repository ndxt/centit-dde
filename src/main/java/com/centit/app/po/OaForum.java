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

public class OaForum implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private Long forumid;

    private Long boardid;
    private String forumname;
    private String forumpic;
    private String announcement;
    private Integer joinright;
    private Integer viewright;
    private Integer postright;
    private Integer replyright;
    private Integer isforumer;
    private Date createtime;
    private Long mebernum;
    private Long threadnum;
    private Long replynum;
    private Set<OaThread> oaThreads = null;// new ArrayList<OaThread>();

    // Constructors

    /**
     * default constructor
     */
    public OaForum() {
    }

    /**
     * minimal constructor
     */
    public OaForum(
            Long forumid
    ) {


        this.forumid = forumid;

    }

    /**
     * full constructor
     */
    public OaForum(
            Long forumid
            , Long boardid, String forumname, String forumpic, String announcement, Integer joinright, Integer viewright, Integer postright, Integer replyright, Integer isforumer, Date createtime, Long mebernum, Long threadnum, Long replynum) {


        this.forumid = forumid;

        this.boardid = boardid;
        this.forumname = forumname;
        this.forumpic = forumpic;
        this.announcement = announcement;
        this.joinright = joinright;
        this.viewright = viewright;
        this.postright = postright;
        this.replyright = replyright;
        this.isforumer = isforumer;
        this.createtime = createtime;
        this.mebernum = mebernum;
        this.threadnum = threadnum;
        this.replynum = replynum;
    }


    public Long getForumid() {
        return this.forumid;
    }

    public void setForumid(Long forumid) {
        this.forumid = forumid;
    }
    // Property accessors

    public Long getBoardid() {
        return this.boardid;
    }

    public void setBoardid(Long boardid) {
        this.boardid = boardid;
    }

    public String getForumname() {
        return this.forumname;
    }

    public void setForumname(String forumname) {
        this.forumname = forumname;
    }

    public String getForumpic() {
        return this.forumpic;
    }

    public void setForumpic(String forumpic) {
        this.forumpic = forumpic;
    }

    public String getAnnouncement() {
        return this.announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public Integer getJoinright() {
        return this.joinright;
    }

    public void setJoinright(Integer joinright) {
        this.joinright = joinright;
    }

    public Integer getViewright() {
        return this.viewright;
    }

    public void setViewright(Integer viewright) {
        this.viewright = viewright;
    }

    public Integer getPostright() {
        return this.postright;
    }

    public void setPostright(Integer postright) {
        this.postright = postright;
    }

    public Integer getReplyright() {
        return this.replyright;
    }

    public void setReplyright(Integer replyright) {
        this.replyright = replyright;
    }

    public Integer getIsforumer() {
        return this.isforumer;
    }

    public void setIsforumer(Integer isforumer) {
        this.isforumer = isforumer;
    }

    public Date getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getMebernum() {
        return this.mebernum;
    }

    public void setMebernum(Long mebernum) {
        this.mebernum = mebernum;
    }

    public Long getThreadnum() {
        return this.threadnum;
    }

    public void setThreadnum(Long threadnum) {
        this.threadnum = threadnum;
    }

    public Long getReplynum() {
        return this.replynum;
    }

    public void setReplynum(Long replynum) {
        this.replynum = replynum;
    }


    public Set<OaThread> getOaThreads() {
        if (this.oaThreads == null)
            this.oaThreads = new HashSet<OaThread>();
        return this.oaThreads;
    }

    public void setOaThreads(Set<OaThread> oaThreads) {
        this.oaThreads = oaThreads;
    }

    public void addOaThread(OaThread oaThread) {
        if (this.oaThreads == null)
            this.oaThreads = new HashSet<OaThread>();
        this.oaThreads.add(oaThread);
    }

    public void removeOaThread(OaThread oaThread) {
        if (this.oaThreads == null)
            return;
        this.oaThreads.remove(oaThread);
    }

    public OaThread newOaThread() {
        OaThread res = new OaThread();

        res.setForumid(this.getForumid());

        return res;
    }

    /**
     * 替换子类对象数组，这个函数主要是考虑hibernate中的对象的状态，以避免对象状态不一致的问题
     */
    public void replaceOaThreads(List<OaThread> oaThreads) {
        List<OaThread> newObjs = new ArrayList<OaThread>();
        for (OaThread p : oaThreads) {
            if (p == null)
                continue;
            OaThread newdt = newOaThread();
            newdt.copyNotNullProperty(p);
            newObjs.add(newdt);
        }
        //delete
        boolean found = false;
        Set<OaThread> oldObjs = new HashSet<OaThread>();
        oldObjs.addAll(getOaThreads());

        for (Iterator<OaThread> it = oldObjs.iterator(); it.hasNext(); ) {
            OaThread odt = it.next();
            found = false;
            for (OaThread newdt : newObjs) {
                if (odt.getThreadid().equals(newdt.getThreadid())) {
                    found = true;
                    break;
                }
            }
            if (!found)
                removeOaThread(odt);
        }
        oldObjs.clear();
        //insert or update
        for (OaThread newdt : newObjs) {
            found = false;
            for (Iterator<OaThread> it = getOaThreads().iterator();
                 it.hasNext(); ) {
                OaThread odt = it.next();
                if (odt.getThreadid().equals(newdt.getThreadid())) {
                    odt.copy(newdt);
                    found = true;
                    break;
                }
            }
            if (!found)
                addOaThread(newdt);
        }
    }


    public void copy(OaForum other) {

        this.setForumid(other.getForumid());

        this.boardid = other.getBoardid();
        this.forumname = other.getForumname();
        this.forumpic = other.getForumpic();
        this.announcement = other.getAnnouncement();
        this.joinright = other.getJoinright();
        this.viewright = other.getViewright();
        this.postright = other.getPostright();
        this.replyright = other.getReplyright();
        this.isforumer = other.getIsforumer();
        this.createtime = other.getCreatetime();
        this.mebernum = other.getMebernum();
        this.threadnum = other.getThreadnum();
        this.replynum = other.getReplynum();

        this.oaThreads = other.getOaThreads();
    }

    public void copyNotNullProperty(OaForum other) {

        if (other.getForumid() != null)
            this.setForumid(other.getForumid());

        if (other.getBoardid() != null)
            this.boardid = other.getBoardid();
        if (other.getForumname() != null)
            this.forumname = other.getForumname();
        if (other.getForumpic() != null)
            this.forumpic = other.getForumpic();
        if (other.getAnnouncement() != null)
            this.announcement = other.getAnnouncement();
        if (other.getJoinright() != -1)
            this.joinright = other.getJoinright();
        if (other.getViewright() != -1)
            this.viewright = other.getViewright();
        if (other.getPostright() != -1)
            this.postright = other.getPostright();
        if (other.getReplyright() != -1)
            this.replyright = other.getReplyright();
        if (other.getIsforumer() != -1)
            this.isforumer = other.getIsforumer();
        if (other.getCreatetime() != null)
            this.createtime = other.getCreatetime();
        if (other.getMebernum() != null)
            this.mebernum = other.getMebernum();
        if (other.getThreadnum() != null)
            this.threadnum = other.getThreadnum();
        if (other.getReplynum() != null)
            this.replynum = other.getReplynum();

        this.oaThreads = other.getOaThreads();
    }

    public void clearProperties() {

        this.boardid = null;
        this.forumname = null;
        this.forumpic = null;
        this.announcement = null;
        this.joinright = -1;
        this.viewright = -1;
        this.postright = -1;
        this.replyright = -1;
        this.isforumer = -1;
        this.createtime = null;
        this.mebernum = null;
        this.threadnum = null;
        this.replynum = null;

        this.oaThreads = new HashSet<OaThread>();
    }
}
