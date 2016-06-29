package com.centit.app.service.impl;

import com.centit.app.dao.OaStatMonthDao;
import com.centit.app.dao.OaWorkDayDao;
import com.centit.app.dao.OaWorkingTimeDao;
import com.centit.app.po.OaStatMonth;
import com.centit.app.po.OaWorkDay;
import com.centit.core.utils.WorkTimeSpan;
import com.centit.support.utils.DatetimeOpt;
import com.centit.sys.service.WorkCalendar;

import java.util.Date;
import java.util.List;

public class WorkCalendarImpl implements WorkCalendar {

    private OaStatMonthDao oaStatMonthDao;
    private OaWorkDayDao oaWorkDayDao;
    private OaWorkingTimeDao oaWorkingTimeDao;

    public OaStatMonthDao getOaStatMonthDao() {
        return oaStatMonthDao;
    }

    public void setOaStatMonthDao(OaStatMonthDao oaStatMonthDao) {
        this.oaStatMonthDao = oaStatMonthDao;
    }

    public OaWorkDayDao getOaWorkDayDao() {
        return oaWorkDayDao;
    }

    public void setOaWorkDayDao(OaWorkDayDao oaWorkDayDao) {
        this.oaWorkDayDao = oaWorkDayDao;
    }

    public OaWorkingTimeDao getOaWorkingTimeDao() {
        return oaWorkingTimeDao;
    }

    public void setOaWorkingTimeDao(OaWorkingTimeDao oaWorkingTimeDao) {
        this.oaWorkingTimeDao = oaWorkingTimeDao;
    }

    /**
     * 获取某个统计月的起止日期
     *
     * @param sMonth 格式为YYYYMM 字符串，比如201202
     * @return 两个元素的一维数组。
     */
    public Date[] getStatMonth(String sMonth) {

        OaStatMonth dbobject = oaStatMonthDao.getObjectById(sMonth);
        Date[] statMonth = {dbobject.getBeginDay(), dbobject.getEendDay()};
        return statMonth;
    }

    /**
     * 根据企业日历计算实际工作时间，剔除假期和休息时间   加上加班的天数 (defaut规则是剔除了星期六和星期天 ，和法定节假日:从数据库里面的读的)
     *
     * @param beginTime 开始时间,精确到天
     * @param endTime   结束时间,精确到天
     * @return 以日为单位 半天的以一天为准
     */
    public WorkTimeSpan getWorkTime(Date beginTime, Date endTime) {

        long m = DatetimeOpt.calcSpanDays(beginTime, endTime)
                - DatetimeOpt.calcWeekendDays(beginTime, endTime);


        long n = getholidays(beginTime, endTime);
        long i = getAdddays(beginTime, endTime);
        return new WorkTimeSpan(m - n + i, 0, 0);

    }

    /**
     * 4.根据某个基础工作时间点和工作时间段偏差计算工作日期
     *
     * @param baseWorkDate 基点工作时间
     * @param workTime     工作时间偏移，<0 向前， >0向后
     * @return
     */


    public Date getWorkDate(Date baseWorkDate, String workTime) {
        WorkTimeSpan wt = new WorkTimeSpan();
        wt.fromString(workTime);
        Date endWorkDate = getWorkDate(baseWorkDate, wt);

        return endWorkDate;
    }

    @SuppressWarnings("deprecation")
    public Date getWorkDate(Date baseWorkDate, WorkTimeSpan workTime) {
        Date endWorkDate = null;
        long d = workTime.getDays();
        long m = d / 5 * 7;
        Date lsdate = new Date(baseWorkDate.getTime() + m * 24 * 60 * 60 * 1000);  //临时
        long holidays = getholidays(baseWorkDate, lsdate);         //正常工作日变成假期的
        long adddays = getAdddays(baseWorkDate, lsdate);           //正常假期变成工作日的
        long zctime = m - DatetimeOpt.calcWeekendDays(baseWorkDate, lsdate) - holidays + adddays;
        endWorkDate = lsdate;
        if (d > zctime) {
            long needAddedDays = d - zctime;
            long i = 0;
            while (i < needAddedDays) {
                endWorkDate = new Date(endWorkDate.getTime() + 24 * 60 * 60 * 1000);
                if (isWorkDay(endWorkDate))
                    i++;

            }
        } else if (d < zctime) {
            long needAddedDays = zctime - d;
            long i = 0;
            while (i < needAddedDays) {
                endWorkDate = new Date(endWorkDate.getTime() - 24 * 60 * 60 * 1000);
                if (isWorkDay(endWorkDate))
                    i++;
            }
        }

        //算小时和分钟
        if (workTime.getHours() != 0 || workTime.getMinutes() != 0) {
            //不超过一天
            if (((baseWorkDate.getHours() * 60 + baseWorkDate.getMinutes()) * 60 * 1000 +
                    (workTime.getHours() * 60 + workTime.getMinutes()) * 60 * 1000) < 24 * 60 * 60 * 1000
                    ) {
                endWorkDate = new Date(endWorkDate.getTime() +
                        (workTime.getHours() * 60 + workTime.getMinutes()) * 60 * 1000);

            }
            //超过一天
            else {
                endWorkDate = new Date(endWorkDate.getTime() +
                        (workTime.getHours() * 60 + workTime.getMinutes()) * 60 * 1000);
                if (!isWorkDay(endWorkDate)) {
                    endWorkDate = new Date(endWorkDate.getTime() +
                            (workTime.getHours() * 60 + workTime.getMinutes()) * 60 * 1000 + 24 * 60 * 60 * 1000);
                }
            }
        }
        return endWorkDate;
    }

    /**
     * 获取某个统计月的工作时间
     *
     * @param sMonth 格式为YYYYMM 字符串，比如201202
     * @return
     */
    public WorkTimeSpan getStatMonthWorkTime(String sMonth) {
        // TODO Auto-generated method stub
        return new WorkTimeSpan();
    }

    //Datype:B表示周末加班，A表示工作日放假
    public boolean isWorkDay(Date date) {
        boolean result = true;
        OaWorkDay dbobject = oaWorkDayDao.getObjectById(date);
        if (DatetimeOpt.getDayOfWeek(date) == 0
                || DatetimeOpt.getDayOfWeek(date) == 6) //周末的情况
        {
            result = false;
            if (dbobject != null) {
                if (dbobject.getDaytype().equals("B"))      //周末加班的情况
                    result = true;
            }

        }
        if (dbobject != null) {
            if (dbobject.getDaytype().equals("A")) //工作日放假的情况
                result = false;
        }
        return result;
    }

    public long getholidays(Date beginTime, Date endTime) {
        long i = 0;
        List<OaWorkDay> list = oaWorkDayDao.getListByDate(beginTime, endTime);
        for (OaWorkDay a : list) {
            if (a.getDaytype().equals("A"))
                i++;
        }

        return i;
    }

    public long getAdddays(Date beginTime, Date endTime) {
        long i = 0;
        List<OaWorkDay> list = oaWorkDayDao.getListByDate(beginTime, endTime);
        for (OaWorkDay a : list) {
            if (a.getDaytype().equals("B"))
                i++;
        }

        return i;
    }

    @Override
    public boolean isWorkTime(Date workTime) {
        // TODO 夏成  Auto-generated method stub
        return true;
    }


}
