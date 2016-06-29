package com.centit.app.action;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.centit.app.po.Innermsg;
import com.centit.app.po.InnermsgRecipient;
import com.centit.app.service.InnermsgRecipientManager;
import com.centit.core.action.BaseEntityDwzAction;
import com.centit.core.utils.DwzTableUtils;
import com.centit.core.utils.PageDesc;
import com.centit.sys.po.FUserinfo;
import com.centit.sys.service.CodeRepositoryUtil;

public class InnermsgRecipientAction extends BaseEntityDwzAction<InnermsgRecipient> {
    private static final Log log = LogFactory.getLog(InnermsgRecipientAction.class);

    // private static final ISysOptLog sysOptLog = SysOptLogFactoryImpl.getSysOptLog("optid");

    private static final long serialVersionUID = 1L;
    private InnermsgRecipientManager innermsgRecipientMag;

    public void setInnermsgRecipientManager(InnermsgRecipientManager basemgr) {
        innermsgRecipientMag = basemgr;
        this.setBaseEntityManager(innermsgRecipientMag);
    }

    public String delete() {
        super.delete();

        return "delete";
    }

    @SuppressWarnings("unchecked")
    @Override
    public String list() {
        Map<Object, Object> paramMap = request.getParameterMap();
        resetPageParam(paramMap);

        Map<String, Object> filterMap = convertSearchColumn(paramMap);

        filterMap.put("receive", this.getLoginUserCode());

        setFilterMapValue(filterMap, "msgtitle");
        setFilterMapValue(filterMap, "sender");

        PageDesc pageDesc = DwzTableUtils.makePageDesc(request);
        objList = baseEntityManager.listObjects(filterMap, pageDesc);

        this.pageDesc = pageDesc;

        return LIST;
    }

    @Override
    public String view() {
        this.object = this.innermsgRecipientMag.getObjectById(this.object.getId());

        this.setRead();
        return VIEW;
    }

    /**
     * 回复信息
     *
     * @return
     */
    public String recipient() {
        this.object = this.innermsgRecipientMag.getObject(object);
        this.setRead();
        return "recipient";
    }

    /**
     * 设置已读
     */
    private void setRead() {
        if (!CodeRepositoryUtil.getValue(Innermsg.MSG_STAT, Innermsg.MSG_STAT_R).equals(this.object.getMsgstate())) {

            this.object.setMsgstate(CodeRepositoryUtil.getValue(Innermsg.MSG_STAT, Innermsg.MSG_STAT_R));
            this.innermsgRecipientMag.saveObject(object);
        }
    }

    private static void setFilterMapValue(Map<String, Object> filterMap, String key) {
        if (filterMap.containsKey(key)) {
            filterMap.put(key, "%" + filterMap.get(key) + "%");
        }
    }

    private String getLoginUserCode() {
        return ((FUserinfo) this.getLoginUser()).getUsercode();
    }

}
