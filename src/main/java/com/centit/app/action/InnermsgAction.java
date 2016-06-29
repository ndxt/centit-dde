package com.centit.app.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.springframework.util.StringUtils;

import com.centit.app.po.Innermsg;
import com.centit.app.po.Msgannex;
import com.centit.app.po.UserMailConfig;
import com.centit.app.service.FileinfoManager;
import com.centit.app.service.InnermsgManager;
import com.centit.app.service.UserMailConfigManager;
import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzResultParam;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.support.utils.HtmlFormUtils;
import com.centit.sys.po.FUnitinfo;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.service.CodeRepositoryUtil;
import com.opensymphony.xwork2.ActionContext;

public class InnermsgAction extends BaseEntityDwzAction<Innermsg> {
    /**
     *
     */
    private static final long serialVersionUID = -2551187666778246797L;
    private InnermsgManager innermsgMag;
    private UserMailConfigManager userMailConfigManager;
    private FileinfoManager fileinfoManager;

    // private List<Innermsg> replyList;
    // private List<FileInfo> fileList;

    public void setInnermsgManager(InnermsgManager basemgr) {
        innermsgMag = basemgr;
        this.setBaseEntityManager(innermsgMag);
    }

    public void setFileinfoManager(FileinfoManager fileinfoManager) {
        this.fileinfoManager = fileinfoManager;
    }

    /**
     * 消息发件箱
     */
    @SuppressWarnings("unchecked")
    @Override
    public String list() {
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        Map<String, Object> filterMap = convertSearchColumn(paramMap);

        if (!CodeRepositoryUtil.getValue(Innermsg.MSG_TYPE, Innermsg.MSG_TYPE_A).equalsIgnoreCase(
                String.valueOf(filterMap.get("msgtype")))) {
            filterMap.put("sender", this.getLoginUserCode());
            filterMap.put("msgstateNoEq", CodeRepositoryUtil.getValue(Innermsg.MSG_STAT, Innermsg.MSG_STAT_D));
        }

        PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
        objList = baseEntityManager.listObjects(filterMap, pageDesc);

        this.pageDesc = pageDesc;

        return LIST;
    }

    /**
     * 邮件账户
     *
     * @return
     */
    public String listMailAccount() {
        // 当前用户所拥有Mail账号
        ActionContext.getContext().put("LISTMAILACCOUNT", this.listUserMail());

        return "listMailAccount";
    }

    private List<UserMailConfig> listUserMail() {
        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("usercode", this.getLoginUserCode());

        // 当前用户所拥有Mail账号
        return this.userMailConfigManager.listObjects(filterMap);
    }

    /**
     * 邮箱列表
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public String listMailBox() {
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        Map<String, Object> filterMap = convertSearchColumn(paramMap);

        return this.forwardListMailBox(filterMap, "listMailAccount");
    }


    /**
     * 配置跳转到邮件列表
     *
     * @param filterMap
     * @param result
     * @return
     */
    private String forwardListMailBox(Map<String, Object> filterMap, String result) {

        filterMap.put("msgtype", CodeRepositoryUtil.getValue(Innermsg.MSG_TYPE, "M"));

        PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
        objList = this.innermsgMag.listObjects(filterMap, pageDesc);

        this.pageDesc = pageDesc;

        // return "listMailBox";
        ActionContext.getContext().put("LISTMAILACCOUNT", this.listUserMail());

        // 标识MailAccount
        ActionContext.getContext().put("MailAccount", this.request.getParameter("account"));

        return result;
    }


    public String add() {
        return "add";
    }

    @SuppressWarnings("unchecked")
    public String addMail() {
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);
        return "addMail";
    }

    /**
     * 发送邮件
     *
     * @return
     */
    public String saveSendMail() {
        try {
            this.addMsgannex();
            this.innermsgMag.saveSendMail(this.object);
        } catch (RuntimeException e) {
            super.dwzResultParam = DwzResultParam.getErrorDwzResultParam(e.getMessage());

            return ERROR;
        }
//        DwzResultParam param = new DwzResultParam();
//        // 刷新列表
//        param.setForwardUrl("/app/innermsg!listMailBox.do?s_mailtype=" + object.getMailtype() + "&s_emailid="
//                + object.getEmailid() + "&account=false");
//        param.setNavTabId("external_EMAIL");
//
//        super.dwzResultParam = param;


        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("mailtype", object.getMailtype());
        filterMap.put("emailid", object.getEmailid());
        filterMap.put("isvalid", "T");
        filterMap.put("account", "true");


        return this.forwardListMailBox(filterMap, "saveSendMail");

//        return "saveSendMail";
    }

    /**
     * 收取邮件
     *
     * @return
     */
    public String saveReceiveMail() {
        int length = -1;
        try {
            length = this.innermsgMag.saveReceiveMail(this.object);
        } catch (RuntimeException e) {
            super.dwzResultParam = DwzResultParam.getErrorDwzResultParam(e.getMessage());

            return ERROR;
        }

        DwzResultParam param = new DwzResultParam();
        // 刷新列表
//        param.setForwardUrl("/app/innermsg!listMailBox.do?s_mailtype=" + object.getMailtype() + "&s_emailid="
//                + object.getEmailid() + "8&account=true");
        if (0 == length) {
            param.setMessage("无新邮件");

        } else {
            param.setMessage("成功接收邮件 " + length + " 封");

        }
        super.dwzResultParam = param;


        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("mailtype", object.getMailtype());
        filterMap.put("emailid", object.getEmailid());
        filterMap.put("isvalid", "T");


        return this.forwardListMailBox(filterMap, "saveReceiveMail");
        //return "saveReceiveMail";
    }

    /**
     * 邮件状态互换[inbox <--> outbox <--> ]
     *
     * @return
     */
    public String modifyMailBox() {
        final String view = "modifyMailBox";
        Map<String, Object> filterMap = new HashMap<String, Object>();

        //废件箱，完全删除
        if (object.getMailUnDelType().equalsIgnoreCase(object.getMailtype())) {
            this.object = this.baseEntityManager.getObject(this.object);

            filterMap = this.getFilterMap();

            try {
                this.innermsgMag.deleteMail(object);
            } catch (RuntimeException e) {

                super.dwzResultParam = DwzResultParam.getErrorDwzResultParam("删除失败");
                return ERROR;
            }

        } else if (CodeRepositoryUtil.getValue(Innermsg.MAIL_TYPE, "T").equalsIgnoreCase(this.object.getMailtype())) {
            //To 废件箱

            String mailUnDelType = this.object.getMailUnDelType();
            String mailtype = this.object.getMailtype();

            this.object = this.innermsgMag.getObject(this.object);

            this.object.setMailtype(mailtype);
            this.object.setMailUnDelType(mailUnDelType);

            filterMap = this.getFilterMap();
            filterMap.put("mailtype", mailtype);

            this.innermsgMag.saveObject(this.object);


        } else {
            //恢复
            this.object = this.innermsgMag.getObject(this.object);
            this.object.setMailtype(this.object.getMailUnDelType());
            this.object.setMailUnDelType(null);

            this.innermsgMag.saveObject(this.object);

            filterMap = this.getFilterMap();
        }


        return this.forwardListMailBox(filterMap, view);
    }

    private Map<String, Object> getFilterMap() {
        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("mailtype", object.getMailtype());
        filterMap.put("emailid", object.getC().getEmailid());
        filterMap.put("isvalid", "T");
        return filterMap;
    }

    /**
     * 获取部门人员数据
     *
     * @return
     */
    public String innermsgTree() {
        List<FUnitinfo> units = CodeRepositoryUtil.getAllUnits("A");
        List<FUserinfo> users = CodeRepositoryUtil.getAllUsers("A");
        List<Map<String, String>> unit = new ArrayList<Map<String, String>>();

        for (FUnitinfo unitinfo : units) {
            Map<String, String> m = new HashMap<String, String>();
            m.put("id", unitinfo.getUnitcode());
            m.put("pId", unitinfo.getParentunit());
            m.put("name", unitinfo.getUnitname());
            m.put("open", "true");
            m.put("p", "true");

            unit.add(m);
        }
        for (FUserinfo userinfo : users) {
            Map<String, String> m = new HashMap<String, String>();
            m.put("id", userinfo.getUsercode());
            m.put("pId", userinfo.getPrimaryUnit());
            m.put("name", userinfo.getUsername());
            m.put("p", "false");

            unit.add(m);
        }

        JSONArray ja = new JSONArray();
        ja.addAll(unit);
        ActionContext.getContext().put("unit", ja.toString());
        return "innermsgTree";
    }

    /**
     * 保存消息
     *
     * @return
     */
    public String saveMsg() {
        final String view = "saveMsg";

        object.setSender(this.getLoginUserCode());
        object.setSenddate(new Date());

        this.addMsgannex();
        // 公告
        if (CodeRepositoryUtil.getValue(Innermsg.MSG_TYPE, Innermsg.MSG_TYPE_A).equalsIgnoreCase(
                this.object.getMsgtype())) {
            this.object.setMsgstate(Innermsg.MSG_STAT_R);

            this.innermsgMag.saveAnnouncement(this.object);
            return view;
        }

        this.innermsgMag.saveMsg(object);
        return view;
    }


    /**
     * 添加附件
     */
    private void addMsgannex() {
        Object o = ActionContext.getContext().getParameters().get("filecodes");
        if (null != o) {
            String msgannex = HtmlFormUtils.getParameterString(o);
            if (StringUtils.hasText(msgannex)) {
                String[] msgannexs = msgannex.split(",");
                for (String s : msgannexs) {

                    object.getMsgannexs().add(new Msgannex(null, this.fileinfoManager.getObjectById(s)));
                }
            }
        }
    }

    /**
     * 删除消息
     *
     * @return
     */
    public String deleteMsg() {
        if (CodeRepositoryUtil.getValue(Innermsg.MAIL_TYPE, Innermsg.MAIL_TYPE_O)
                .equalsIgnoreCase(object.getMailtype())) {


            object.setSender(this.getLoginUserCode());
            if (CodeRepositoryUtil.getValue(Innermsg.MSG_TYPE, Innermsg.MSG_TYPE_A).equalsIgnoreCase(
                    object.getMsgtype())) {
                // 公告直接删除
                this.object = this.baseEntityManager.getObject(object);
                this.innermsgMag.deleteObject(object);
            } else {
                this.innermsgMag.deleteMsg(object,
                        CodeRepositoryUtil.getValue(Innermsg.MAIL_TYPE, Innermsg.MAIL_TYPE_O));
            }
        } else if (CodeRepositoryUtil.getValue(Innermsg.MAIL_TYPE, "I").equalsIgnoreCase(object.getMailtype())) {
            object.setReceiveUserCode(this.getLoginUserCode());

            this.innermsgMag.deleteMsg(object, CodeRepositoryUtil.getValue(Innermsg.MAIL_TYPE, Innermsg.MAIL_TYPE_I));
        }


        return "deleteMsg";
    }


    private String getLoginUserCode() {
        return ((FUserinfo) this.getLoginUser()).getUsercode();
    }

    @SuppressWarnings("unchecked")
    @Override
    public String view() {
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        object = this.innermsgMag.getObjectById(object.getMsgcode());
        
/*        if (CodeRepositoryUtil.getValue(Innermsg.MSG_TYPE, Innermsg.MSG_TYPE_A).equalsIgnoreCase(object.getMsgtype())) {

            // return "viewForA";
        }
*/
        // 邮件
        if (CodeRepositoryUtil.getValue(Innermsg.MSG_TYPE, Innermsg.MSG_TYPE_M).equalsIgnoreCase(
                this.object.getMsgtype())) {
            if (CodeRepositoryUtil.getValue(Innermsg.MSG_STAT, Innermsg.MSG_STAT_U).equalsIgnoreCase(
                    object.getMsgstate())) {

                object.setMsgstate(CodeRepositoryUtil.getValue(Innermsg.MSG_STAT, Innermsg.MSG_STAT_R));
                this.innermsgMag.saveObject(object);
            }

            // 草稿箱，跳转至 发送邮件
            if (CodeRepositoryUtil.getValue(Innermsg.MAIL_TYPE, Innermsg.MAIL_TYPE_D).equalsIgnoreCase(
                    object.getMailtype())) {

                // 将类型设为发件箱

                return "addMail";
            }

            return "viewMail";
        }

        return VIEW;
    }

    public void setUserMailConfigManager(UserMailConfigManager userMailConfigManager) {
        this.userMailConfigManager = userMailConfigManager;
    }

    // /**
    // * 消息列表
    // * @return
    // */
    // @SuppressWarnings("unchecked")
    // public String list() {
    // Map<Object, Object> paramMap = request.getParameterMap();
    // resetPageParam(paramMap);
    // Map<String, String> filterMap = convertSearchColumn(paramMap);
    //
    // String isReveive = request.getParameter("isReveive");
    //
    // if (isReveive != null && isReveive.equals("S")) {
    // filterMap.put("sender", ((FUserDetail)this.getLoginUser()).getUsercode());
    // } else {
    // filterMap.put("receive", ((FUserDetail)this.getLoginUser()).getUsercode());
    // request.setAttribute("isReveive", "R");
    // }
    // filterMap.put("receivetype", "P");
    // filterMap.put("nodelete", "D");
    //
    // Limit limit=ExtremeTableUtils.getLimit(request);
    // PageDesc pageDesc = ExtremeTableUtils.makePageDesc(limit);
    // objList = baseEntityManager.listObjects(filterMap, pageDesc);
    // totalRows = pageDesc.getTotalRows();
    //
    // return LIST;
    // }
    //
    // /**
    // * 公告列表
    // * @return
    // */
    // @SuppressWarnings("unchecked")
    // public String bulletinList() {
    // Map<Object, Object> paramMap = request.getParameterMap();
    // resetPageParam(paramMap);
    // Map<String, String> filterMap = convertSearchColumn(paramMap);
    // String isReveive = request.getParameter("isReveive");
    //
    // if (isReveive != null && isReveive.equals("S")) {
    // filterMap.put("sender", ((FUserDetail)this.getLoginUser()).getUsercode());
    // } /*else {
    // filterMap.put("receive", ((FUserDetail)this.getLoginUser()).getPrimaryUnit());
    // }*/
    // filterMap.put("receivetype", "O");
    // filterMap.put("nodelete", "D");
    //
    // PageDesc pageDesc = ExtremeTableUtils.makePageDesc( ExtremeTableUtils.getLimit(this.request));
    // objList = baseEntityManager.listObjects(new HashMap<String, String>(), pageDesc);
    //
    // ActionContext.getContext().put("totals",pageDesc.getTotalRows());
    //
    // List<String> userCode = new ArrayList<String>();
    // userCode.add((((FUserDetail)this.getLoginUser())).getUsercode());
    //
    // /*ServletActionContext.getContext().put("objList", objList);*/
    //
    // if (userCode.get(0).equals("99999999")) {
    // ActionContext.getContext().put("USER", "1");
    // } else {
    // ActionContext.getContext().put("USER", "0");
    // }
    //
    // return "bulletinList";
    // }
    //
    // public String built() {
    // object.clearProperties();
    // return EDIT;
    // }
    //
    // public String bulletinEdit() {
    // object.clearProperties();
    // return "bulletinEdit";
    // }
    //
    // /**
    // * 查看公告
    // * @return
    // */
    // public String viewBulletin(){
    // try {
    //
    // Innermsg msgObj = innermsgMag.getObjectById(object.getMsgcode());
    //
    // if (object == null) {
    //
    // return LIST;
    // }
    //
    // if (msgObj != null) {
    // innermsgMag.copyObject(object, msgObj);
    // fileList = innermsgMag.listFilesByMsg(object.getMsgcode());
    // }
    //
    // return "bulletinView";
    // } catch (Exception e) {
    // return ERROR;
    // }
    // }
    //
    // /**
    // * 查看消息
    // */
    // public String view() {
    //
    // try {
    //
    // Innermsg msgObj = innermsgMag.getObjectById(object.getMsgcode());
    //
    // if (object == null) {
    //
    // return LIST;
    // }
    //
    // if (msgObj != null) {
    // innermsgMag.copyObject(object, msgObj);
    // fileList = innermsgMag.listFilesByMsg(object.getMsgcode());
    // }
    // replyList = innermsgMag.listReplyMsgs(object.getMsgcode());
    // FUserDetail fuser = ((FUserDetail)this.getLoginUser());
    // if (fuser.getUsercode() != null
    // && fuser.getUsercode().equals(msgObj.getReceive())) {
    // if (msgObj.getMsgstate().equals("U")) {
    // msgObj.setMsgstate("R");// 设置邮件状态为已读
    // innermsgMag.saveObject(msgObj);
    // }
    // }
    // String type=request.getParameter("type");
    //
    // if(type.equals("R")){
    // request.setAttribute("type","1");
    // }else{
    // request.setAttribute("type","0");
    // }
    // return VIEW;
    // } catch (Exception e) {
    // return ERROR;
    // }
    // }
    //
    // public void commSave(){
    // FUserDetail fuser = ((FUserDetail)this.getLoginUser());
    //
    // Innermsg msgObj = innermsgMag.getObject(object);
    //
    // if (msgObj != null) {
    // innermsgMag.copyObjectNotNullProperty(msgObj, object);
    // object = msgObj;
    // }
    //
    // object.setSender(fuser.getUsercode());
    // object.setSenddate(DatetimeOpt.currentSqlDate());
    // innermsgMag.saveInnermsg(object);
    // }
    //
    // /**
    // * 保存消息
    // * @return
    // */
    // public String saveMsg() {
    // commSave();
    // return EDIT;
    // }
    //
    // /**
    // * 保存公告
    // * @return
    // */
    // public String saveBulletin() {
    // commSave();
    // return "bulletinEdit";
    // }
    //
    // /**
    // * 消息回复
    // * @return
    // */
    // public String reply() {
    // Innermsg msgObj = innermsgMag.getObjectById(object.getMsgcode());
    // if (msgObj != null) {
    // Innermsg msg = new Innermsg();
    // msg.setMsgtitle("答复:" + msgObj.getMsgtitle());
    // msg.setReceive(msgObj.getSender());
    // msg.setReceivetype("P");
    // msg.setReplymsgcode(msgObj.getMsgcode());
    // object.copy(msg);
    // }
    // return EDIT;
    // }
    //
    //
    // //新增删除公告方法
    // public String deleteBulletin(){
    // try {
    // try {
    // innermsgMag.deleteObject(object);
    // deletedMessage();
    // } catch (Exception e) {
    // log.error(e.getMessage(), e);
    // saveError( e.getMessage());
    // return EDIT;
    // }
    // List<String> userCode = new ArrayList<String>();
    // userCode.add((((FUserDetail)this.getLoginUser())).getUsercode());
    //
    // if (userCode.get(0).equals("99999999")) {
    // ActionContext.getContext().put("USER", "1");
    // } else {
    // ActionContext.getContext().put("USER", "0");
    // }
    // return "deleteBulletin";
    // } catch (Exception e) {
    // return ERROR;
    // }
    // }
    //
    //
    // /**
    // * 消息框架加载
    // * @return
    // */
    // public String showMsgFrame() {
    // return "msgFrame";
    // }
    //
    // /**
    // * 公告框架加载
    // * @return
    // */
    // public String showBulletin(){
    // return "bulletinFrame";
    // }
    //
    // public List<Innermsg> getReplyList() {
    // return replyList;
    // }
    //
    // public void setReplyList(List<Innermsg> replyList) {
    // this.replyList = replyList;
    // }
    //
    // public List<FileInfo> getFileList() {
    // return fileList;
    // }
    //
    // public void setFileList(List<FileInfo> fileList) {
    // this.fileList = fileList;
    // }

}
