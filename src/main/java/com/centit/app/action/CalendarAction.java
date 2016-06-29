package com.centit.app.action;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.centit.app.po.OaWorkDay;
import com.centit.app.service.OaWorkDayManager;
import com.centit.core.action.BaseEntityDwzAction;
import com.centit.sys.service.WorkCalendar;

public class CalendarAction extends BaseEntityDwzAction<OaWorkDay> {

    private static final long serialVersionUID = 1L;
    private OaWorkDayManager oaWorkDayManager;
    private WorkCalendar workCalendar;
    private JSONObject result;


    public WorkCalendar getWorkCalendar() {
        return workCalendar;
    }

    public void setWorkCalendar(WorkCalendar workCalendar) {
        this.workCalendar = workCalendar;
    }

    public void setOaWorkDayManager(OaWorkDayManager oaWorkDayManager) {
        this.oaWorkDayManager = oaWorkDayManager;
        super.setBaseEntityManager(oaWorkDayManager);
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public String view() {

        return "calendar";
    }

    public String getUnusualDays() {
        return "unusual";
    }

    public String savedate() {
        try {
            OaWorkDay dbobject = oaWorkDayManager.getObjectById(object.getWorkday());
            if (dbobject != null) {
                if (object.getDaytype().equals("C")) {        //当把这个日期设置为正常时 把这个日期从数据库里面删除
                    oaWorkDayManager.deleteObject(dbobject);
                } else {
                    oaWorkDayManager.copyObjectNotNullProperty(dbobject, object);
                    object = dbobject;
                }
            }
            if (!object.getDaytype().equals("C")) {
                oaWorkDayManager.saveObject(object);
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("DayType", object.getDaytype());
            result = JSONObject.fromObject(map);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "save";
    }

    private Date beginDate;
    private Date endDate;

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @SuppressWarnings("deprecation")
    public String getDayArray() {

        List<OaWorkDay> list = oaWorkDayManager.getListByDay(beginDate, endDate);
        if (list != null) {
            Map<String, String> map1 = new HashMap<String, String>();
            Map<String, String> map2 = new HashMap<String, String>();
            Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
            for (OaWorkDay day : list) {
                if (day.getDaytype().equals("A")) {
                    map1.put(Integer.toString(day.getWorkday().getDate()), Integer.toString(day.getWorkday().getMonth() + 1));
                } else {
                    map2.put(Integer.toString(day.getWorkday().getDate()), Integer.toString(day.getWorkday().getMonth() + 1));
                }
            }
            map.put("A", map1);
            map.put("B", map2);
            result = JSONObject.fromObject(map);
        } else {
            result = null;
        }
        return "jsonArray";
    }

    @SuppressWarnings("deprecation")
    public String test() {
        String s = "20D6H40M";
        Date now = new Date();
        try {
            Date time = workCalendar.getWorkDate(now, s);
            System.out.println(time.toLocaleString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
