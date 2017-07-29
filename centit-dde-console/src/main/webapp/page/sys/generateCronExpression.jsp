<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/page/common/taglibs.jsp" %>


<script type="text/javascript" src="${contextPath }/scripts/sys/generateCronExpression.js"></script>

<script type="text/javascript">
    $(function () {
        var params = {'cronExpression': $('#txt_cron').val(), 'cronExpressionCallback' : $('#${param["txtCallback"] }')};
        new GenerateCronExpression(params).run();

    });

</script>


<div class="pageContent" layoutH="30">
    <div class="tabs" currentIndex="0" eventType="click" style=" background: none;">
        <div class="tabsHeader">
            <div class="tabsHeaderContent" style=" background: none;">
                <ul id="ul_tabs">
                    <li><a href="javascript:;"><span>按分钟</span></a></li>
                    <li><a href="javascript:;"><span>按小时</span></a></li>
                    <li><a href="javascript:;"><span>按天</span></a></li>
                    <li><a href="javascript:;"><span>按月</span></a></li>
                    <li><a href="javascript:;"><span>按周</span></a></li>
                </ul>
            </div>
        </div>
        <div class="tabsContent" style="height:160px;">
            <div>
                <table>
                    <tr>
                        <td>每 <input type="text" size="5" maxlength="2" name="txtMinutes" id="txtMinutes"/>分钟</td>
                    </tr>
                </table>
            </div>
            <div>
                <table>
                    <tr>
                        <td>每 <input type="text" size="5" maxlength="2" name="txtHourly" id="txtHourly"/> 小时</td>
                        
                    </tr>
                    <tr>
                        <td>从 <input type="text" size="5" maxlength="2" id="txtHour_begin"/>时到
                             <input type="text" size="5" maxlength="2"  id="txtHour_end"/> 时
                        </td>
                    </tr>
                </table>
            </div>
            <div>
                <table id="tabDay">
                    <tr>
                        <td>
                            <label>
                                <input type="radio" name="rad_day" value="-1"/>不关注
                            </label>
                        </td>
                    </tr>
                    <tr>
                        <td><label><input type="radio" name="rad_day" value="0"/>
                            从 <input type="text" size="5" maxlength="2" id="txtDaily_str0"/> 号开始
                            每隔 <input type="text" size="5" maxlength="2" name="txtDaily" id="txtDaily"/> 天</label>
                        </td>
                    </tr>
                    <tr>
                        <td><label><input type="radio" name="rad_day" value="1"/>
                            从 <input type="text" size="5" maxlength="2" id="txtDaily_str"/> 号
                            到 <input type="text" size="5" maxlength="2" id="txtDaily_end"/> 号</label>
                        </td>
                    </tr>
                    <tr>
                        <td><label><input type="radio" name="rad_day" value="2"/>
                            每月最后一天</label>
                        </td>
                    </tr>
                    <tr>
                        <td><label><input type="radio" name="rad_day" value="3"/>
                            每月最后一个工作日</label>
                        </td>
                    </tr>
                    <tr>
                        <td><label><input type="radio" name="rad_day" value="4"/>
                            离<input type="text" size="5" maxlength="2" id="txtDaily_last"/>号最近的一个工作日</label>
                        </td>
                    </tr>
                </table>
            </div>
            <div>
                <table id="tabMonth">
                    <tr>

                        <td><label><input type="radio" name="rad_month" value="-1"/>
                            不关注 </label>
                        </td>

                    </tr>
                    <tr>
                        <td><label><input type="radio" name="rad_month" value="0"/>
                            从<input type="text" size="5" maxlength="2" id="choiceMonth_str0"/>
                            开始每隔 <input type="text" size="5" maxlength="2" id="choiceMonth"/>个月</label>
                        </td>
                    </tr>
                    <tr>
                        <td><label><input type="radio" name="rad_month" value="1"/>
                            从
                            <input type="text" size="5" maxlength="2" id="choiceMonth_str"/>
                            月到
                            <input type="text" size="5" maxlength="2" id="choiceMonth_end"/>
                            月</label>
                        </td>
                    </tr>

                </table>
            </div>
            <div>
                <table id="tabWeek">
                    <tr>
                        <td><label><input type="radio" name="rad_week" value="-1"/>
                            不关注</label>
                        </td>
                    </tr>
                    <tr>
                        <td id="td_week"><label><input type="radio" name="rad_week" value="0"/>选择星期</label></td>
                    </tr>
                    <tr>
                        <td>
                            <label><input type="radio" name="rad_week" value="1"/>
                                选择星期
                                <input type="text" size="5" maxlength="2" id="txtWeek_str"/>(数字1-7)
                                至星期
                                <input type="text" size="5" maxlength="2" id="txtWeek_end"/>(数字1-7,1为星期日)</label>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label><input type="radio" name="rad_week" value="2"/>周末</label>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label><input type="radio" name="rad_week" value="3"/>本月周末</label>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label><input type="radio" name="rad_week" value="4"/>
                                第
                                <input type="text" size="5" maxlength="2" id="txtWeek_how"/>
                                个星期的周
                                <input type="text" size="5" maxlength="2" id="txtWeek_day"/>
                                几(例:第2个星期的周5)</label>
                        </td>
                    </tr>

                </table>
            </div>

        </div>

        <div class="tabsFooter" style="height: 65px; background: #eef4f5;">
            <div class="tabsFooterContent" style="height: 65px; padding:0 auto; ">
                <span>第一次运行时间:
                <input id="chk_first_run_hour" type="checkbox"/>
                <select id="sel_hour"></select>点
                <input id="chk_first_run_minute" type="checkbox"/>
                <select id="sel_minute"></select>分</span>
                <c:set var="defaultCron" value="0 0 0 ? * ?"/>
                <div>CRON表达式： <input type="text" style="width: 200px" id="txt_cron"
                                     value="${empty param['taskCron'] ? defaultCron : param['taskCron']}"/></div>
            </div>
        </div>


    </div>



</div>

    <div class="formBar">
        <ul>
            <li>
                <div class="buttonActive">
                    <div class="buttonContent">
                        <button id="btnGenerate" type="button">生成表达式</button>
                    </div>
                </div>
            </li>
            <li>
                <div class="buttonActive">
                    <div class="buttonContent">
                        <button id="btn_callback" type="button">保存</button>
                    </div>
                </div>
            </li>
            <li>
                <div class="button">
                    <div class="buttonContent">
                        <button id="btn_close" type="button" class="close">取消</button>
                    </div>
                </div>
            </li>
        </ul>
    </div>






