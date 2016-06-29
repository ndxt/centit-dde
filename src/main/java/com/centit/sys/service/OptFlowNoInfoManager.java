package com.centit.sys.service;

import java.util.Date;

import com.centit.core.service.BaseEntityManager;
import com.centit.sys.po.OptFlowNoInfo;

/**
 * 流水号生成规则，流水号根据   编码属主    OwnerCode  ，编码类别    CodeCode ，编码依据日期  CodeDate 三个属性进行顺序编码
 *
 * @author codefan
 * @create 2012-6-11
 */
public interface OptFlowNoInfoManager extends BaseEntityManager<OptFlowNoInfo> {
    public final static String DefaultOwnerCode = "noowner";
    public final static Date DefaultCodeDate = com.centit.support.utils.DatetimeOpt.createUtilDate(2000, 1, 1);

    /**
     * 获取下一个流水号，流水好是根据 拥有者、类别代码、编码的基准时间
     *
     * @param ownerCode    根据 拥有者，如果设置为  DefaultOwnerCode 则这个编码则依赖于编码
     * @param codeCode     类别代码
     * @param codeBaseDate 编码的基准时间
     * @return 返回流水号
     */
    public long createNextLsh(String ownerCode, String codeCode, Date codeBaseDate);

    /**
     * 获取下一个流水号，流水好是根据 拥有者、类别代码、编码的基准时间这个时间是按照天来编制的就是同一天中顺序编号
     *
     * @param ownerCode    根据 拥有者
     * @param codeCode     类别代码
     * @param codeBaseDate 编码的基准时间
     * @return 返回流水号
     */
    public long createNextLshBaseDay(String ownerCode, String codeCode, Date codeBaseDate);

    /**
     * 获取下一个流水号，流水好是根据 拥有者、类别代码、编码的基准时间这个时间是按照月来编制的就是同一月中顺序编号
     *
     * @param ownerCode    根据 拥有者
     * @param codeCode     类别代码
     * @param codeBaseDate 编码的基准时间
     * @return 返回流水号
     */
    public long createNextLshBaseMonth(String ownerCode, String codeCode, Date codeBaseDate);

    /**
     * 获取下一个流水号，流水好是根据 拥有者、类别代码、编码的基准时间这个时间是按照年来编制的就是同一年中顺序编号
     *
     * @param ownerCode    根据 拥有者
     * @param codeCode     类别代码
     * @param codeBaseDate 编码的基准时间
     * @return 返回流水号
     */
    public long createNextLshBaseYear(String ownerCode, String codeCode, Date codeBaseDate);

    /**
     * 这个只根据 类别代码来编号，他类似于序列，sql server中没有序列可以用这个来模拟
     *
     * @param ownerCode 根据 拥有者
     * @param codeCode
     * @return
     */
    public long createNextLsh(String ownerCode, String codeCode);

    /**
     * 这个只根据 类别代码来编号，他类似于序列，sql server中没有序列可以用这个来模拟
     *
     * @param codeCode
     * @return
     */
    public long createNextLsh(String codeCode);

    /**
     * get 这一组方法和上面一样，则是这一组并没有记录当前获取的值，如果反复调用则会得到相同的值，
     * 调用这组方法后再调用下面对应的record方法会获得和上面create相对应的函数的效果
     * 这组方法的用处是为了避免编码跳号，但是带来的另一个副作用就是会获得相同的编码，使用要注意处理相应的异常。
     */

    public long getNextLsh(String ownerCode, String codeCode, Date codeBaseDate);

    public long getNextLshBaseDay(String ownerCode, String codeCode, Date codeBaseDate);

    public long getNextLshBaseMonth(String ownerCode, String codeCode, Date codeBaseDate);

    public long getNextLshBaseYear(String ownerCode, String codeCode, Date codeBaseDate);

    public long getNextLsh(String ownerCode, String codeCode);

    public long getNextLsh(String codeCode);

    /**
     * 配合get对应的方法使用。
     */
    public void recordNextLsh(String ownerCode, String codeCode, Date codeBaseDate, long currCode);

    public void recordNextLshBaseDay(String ownerCode, String codeCode, Date codeBaseDate, long currCode);

    public void recordNextLshBaseMonth(String ownerCode, String codeCode, Date codeBaseDate, long currCode);

    public void recordNextLshBaseYear(String ownerCode, String codeCode, Date codeBaseDate, long currCode);

    public void recordNextLsh(String ownerCode, String codeCode, long currCode);

    public void recordNextLsh(String codeCode, long currCode);

}
