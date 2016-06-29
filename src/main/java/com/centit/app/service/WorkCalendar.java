package com.centit.app.service;

import java.util.Date;

/**
 * 工作日历
 *
 * @author codefan
 * @create 2012-2-20
 */
public interface WorkCalendar {
    /**
     * 获取某个统计月的起止日期
     *
     * @param sMonth 格式为YYYYMM 字符串，比如201202
     * @return 两个元素的一维数组。
     */
    Date[] getStatMonth(String sMonth);

    /**
     * 根据企业日历计算实际工作时间，剔除假期和休息时间
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 以小时为单位
     */
    WorkTimeSpan getWorkTime(Date beginTime, Date endTime);

    /**
     * 4.根据某个基础工作时间点和工作时间段偏差计算工作日期
     *
     * @param baseWorkDate 基点工作时间
     * @param workTime     工作时间偏移，<0 向前， >0向后
     * @return
     */
    Date getWorkDate(Date baseWorkDate, WorkTimeSpan workTime);

    /**
     * 4.根据某个基础工作时间点和工作时间段偏差计算工作日期
     *
     * @param baseWorkDate 基点工作时间
     * @param workTime     工作时间偏移，<0 向前， >0向后
     * @return
     */
    Date getWorkDate(Date baseWorkDate, String workTime);

    /**
     * 获取某个统计月的工作时间
     *
     * @param sMonth 格式为YYYYMM 字符串，比如201202
     * @return
     */
    WorkTimeSpan getStatMonthWorkTime(String sMonth);


}
