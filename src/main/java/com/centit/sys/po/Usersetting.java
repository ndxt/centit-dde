package com.centit.sys.po;

import java.util.Date;


/**
 * FAddressBook entity.
 *
 * @author codefan@hotmail.com
 */
//用户设置信息表
public class Usersetting implements java.io.Serializable {
    private static final long serialVersionUID = 1L;


    private String usercode;    //用户代码

    private String framelayout;    //框架布局
    private String menustyle;        //菜单样式
    private String pagestyle;        //页面样式
    private String mainpage;        //首页
    private Long linesperpage;        //列表默认分页行数
    private String boardlayout;    //仪表板布局
    private String favorurl1;
    private String favorurl2;
    private String favorurl3;
    private String favorurl4;
    private String favorurl5;
    private String favorurl6;
    private String favorurl7;
    private String favorurl8;
    private String favorurl9;


    private Date createDate = new Date();
    private Date lastModifyDate;

    // Constructors

    /**
     * default constructor
     */
    public Usersetting() {
    }

    /**
     * minimal constructor
     */
    public Usersetting(
            String usercode
    ) {


        this.usercode = usercode;

    }

    /**
     * full constructor
     */
    public Usersetting(
            String usercode
            , String framelayout, String menustyle, String pagestyle, String mainpage, Long linesperpage, String boardlayout, String favorurl1, String favorurl2, String favorurl3, String favorurl4, String favorurl5, String favorurl6, String favorurl7, String favorurl8, String favorurl9) {


        this.usercode = usercode;

        this.framelayout = framelayout;
        this.menustyle = menustyle;
        this.pagestyle = pagestyle;
        this.mainpage = mainpage;
        this.linesperpage = linesperpage;
        this.boardlayout = boardlayout;
        this.favorurl1 = favorurl1;
        this.favorurl2 = favorurl2;
        this.favorurl3 = favorurl3;
        this.favorurl4 = favorurl4;
        this.favorurl5 = favorurl5;
        this.favorurl6 = favorurl6;
        this.favorurl7 = favorurl7;
        this.favorurl8 = favorurl8;
        this.favorurl9 = favorurl9;
    }


    public String getUsercode() {
        return this.usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }
    // Property accessors

    public String getFramelayout() {
        return this.framelayout;
    }

    public void setFramelayout(String framelayout) {
        this.framelayout = framelayout;
    }

    public String getMenustyle() {
        return this.menustyle;
    }

    public void setMenustyle(String menustyle) {
        this.menustyle = menustyle;
    }

    public String getPagestyle() {
        return this.pagestyle;
    }

    public void setPagestyle(String pagestyle) {
        this.pagestyle = pagestyle;
    }

    public String getMainpage() {
        return this.mainpage;
    }

    public void setMainpage(String mainpage) {
        this.mainpage = mainpage;
    }

    public Long getLinesperpage() {
        return this.linesperpage;
    }

    public void setLinesperpage(Long linesperpage) {
        this.linesperpage = linesperpage;
    }

    public String getBoardlayout() {
        return this.boardlayout;
    }

    public void setBoardlayout(String boardlayout) {
        this.boardlayout = boardlayout;
    }

    public String getFavorurl1() {
        return this.favorurl1;
    }

    public void setFavorurl1(String favorurl1) {
        this.favorurl1 = favorurl1;
    }

    public String getFavorurl2() {
        return this.favorurl2;
    }

    public void setFavorurl2(String favorurl2) {
        this.favorurl2 = favorurl2;
    }

    public String getFavorurl3() {
        return this.favorurl3;
    }

    public void setFavorurl3(String favorurl3) {
        this.favorurl3 = favorurl3;
    }

    public String getFavorurl4() {
        return this.favorurl4;
    }

    public void setFavorurl4(String favorurl4) {
        this.favorurl4 = favorurl4;
    }

    public String getFavorurl5() {
        return this.favorurl5;
    }

    public void setFavorurl5(String favorurl5) {
        this.favorurl5 = favorurl5;
    }

    public String getFavorurl6() {
        return this.favorurl6;
    }

    public void setFavorurl6(String favorurl6) {
        this.favorurl6 = favorurl6;
    }

    public String getFavorurl7() {
        return this.favorurl7;
    }

    public void setFavorurl7(String favorurl7) {
        this.favorurl7 = favorurl7;
    }

    public String getFavorurl8() {
        return this.favorurl8;
    }

    public void setFavorurl8(String favorurl8) {
        this.favorurl8 = favorurl8;
    }

    public String getFavorurl9() {
        return this.favorurl9;
    }

    public void setFavorurl9(String favorurl9) {
        this.favorurl9 = favorurl9;
    }


    public void copy(Usersetting other) {

        this.framelayout = other.getFramelayout();
        this.menustyle = other.getMenustyle();
        this.pagestyle = other.getPagestyle();
        this.mainpage = other.getMainpage();
        this.linesperpage = other.getLinesperpage();
        this.boardlayout = other.getBoardlayout();
        this.favorurl1 = other.getFavorurl1();
        this.favorurl2 = other.getFavorurl2();
        this.favorurl3 = other.getFavorurl3();
        this.favorurl4 = other.getFavorurl4();
        this.favorurl5 = other.getFavorurl5();
        this.favorurl6 = other.getFavorurl6();
        this.favorurl7 = other.getFavorurl7();
        this.favorurl8 = other.getFavorurl8();
        this.favorurl9 = other.getFavorurl9();
    }

    public void copyNotNullProperty(Usersetting other) {

        if (other.getFramelayout() != null)
            this.framelayout = other.getFramelayout();
        if (other.getMenustyle() != null)
            this.menustyle = other.getMenustyle();
        if (other.getPagestyle() != null)
            this.pagestyle = other.getPagestyle();
        if (other.getMainpage() != null)
            this.mainpage = other.getMainpage();
        if (other.getLinesperpage() != null)
            this.linesperpage = other.getLinesperpage();
        if (other.getBoardlayout() != null)
            this.boardlayout = other.getBoardlayout();
        if (other.getFavorurl1() != null)
            this.favorurl1 = other.getFavorurl1();
        if (other.getFavorurl2() != null)
            this.favorurl2 = other.getFavorurl2();
        if (other.getFavorurl3() != null)
            this.favorurl3 = other.getFavorurl3();
        if (other.getFavorurl4() != null)
            this.favorurl4 = other.getFavorurl4();
        if (other.getFavorurl5() != null)
            this.favorurl5 = other.getFavorurl5();
        if (other.getFavorurl6() != null)
            this.favorurl6 = other.getFavorurl6();
        if (other.getFavorurl7() != null)
            this.favorurl7 = other.getFavorurl7();
        if (other.getFavorurl8() != null)
            this.favorurl8 = other.getFavorurl8();
        if (other.getFavorurl9() != null)
            this.favorurl9 = other.getFavorurl9();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }
}
