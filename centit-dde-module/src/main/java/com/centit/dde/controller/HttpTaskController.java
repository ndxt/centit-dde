package com.centit.dde.controller;

import com.alibaba.fastjson.JSON;
import com.centit.dde.po.DataPacket;
import com.centit.dde.service.ExchangeService;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.support.compiler.ObjectTranslate;
import com.centit.support.compiler.VariableFormula;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(value = "Http触发任务响应", tags = "Http触发任务响应")
@RestController
@RequestMapping(value = "httpTask")
public class HttpTaskController extends BaseController {

    private final DataPacketService dataPacketService;
    private final ExchangeService exchangeService;

    public HttpTaskController(DataPacketService dataPacketService, ExchangeService exchangeService) {
        this.dataPacketService = dataPacketService;
        this.exchangeService = exchangeService;
    }

    @GetMapping(value = "/run/{packetId}")
    @ApiOperation(value = "立即执行任务")
    public void runTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                HttpServletResponse response) {
        Map<String, Object> params = collectRequestParameters(request);
        JsonResultUtils.writeSingleDataJson(getObject(packetId, params), response);
    }

    private Object getObject(String packetId, Map<String, Object> params) {
        Object bizModel;
        DataPacket dataPacket = dataPacketService.getDataPacket(packetId);
        bizModel = dataPacketService.fetchDataPacketDataFromBuf(dataPacket, params);
        if (bizModel == null) {
            bizModel = exchangeService.runTask(packetId, params);
            dataPacketService.setDataPacketBuf(bizModel, dataPacket, params);
        }
        return bizModel;
    }

    @PostMapping(value = "/runPost/{packetId}")
    @ApiOperation(value = "立即执行任务Post")
    public void runTaskPost(@PathVariable String packetId, HttpServletRequest request,
                            HttpServletResponse response) {
        Map<String, Object> params = collectRequestParameters(request);
        params.put("request", request);
        JsonResultUtils.writeSingleDataJson(getObject(packetId, params), response);
    }

    @GetMapping(value = "/testformula")
    @ApiOperation(value = "测试表达式", notes =
        "可用表达式  \n" +
            "表达式:toJson(),名称:转json,示例:formula:toJson(name);json:{name:\\{sex:'man'}}  \n" +
            "表达式:uuid(),名称:获取uuid,示例:formula:uuid()  \n" +
            "表达式:toString(),名称:转换为String,示例:formula:toString(name)  \n" +
            "表达式:attr(),名称:获取对象属性,示例:formula:attr(name,'123')  \n" +
            "表达式:getat(),示例:求数组中的一个值,示例:formula:getat (0,\"a\",\"b\")  \n" +
            "表达式:singleton(),名称:返回集合，去重,示例:formula:singleton(name)  \n" +
            "表达式:getpy(),名称:取汉字拼音,示例:formula:getpy(name)  \n" +
            "表达式:byte(),名称:求位值,示例:formula:byte(4321.789,0)  \n" +
            "表达式:capital(),名称:数字转大写,示例:formula:capital(123.45)  \n" +
            "表达式:if(),名称:三目判断,示例:formula:if(name=='b','c','d');json:{name:'a'}  \n" +
            "表达式:case(),名称:swithcase判断,示例:formula:case(name,'b','c','d','e')  \n" +
            "表达式:regexmatch(),名称:匹配判断,*?为通配符,示例:formula:regexmatch('t??t',name)  \n" +
            "表达式:regexmatchvalue(),名称:获取匹配值,示例:formula:regexmatchvalue('t??t',name)  \n" +
            "表达式:count(),名称:计数,示例:formula:count(1,\"2\",3,\"5\",1,1,4)  \n" +
            "表达式:countnotnull(),名称:计数 非空参数,示例:formula:countnotnull(1,,\"2\",,,,1,1,4)  \n" +
            "表达式:countnull(),名称:计数空参数,示例:formula:countnull(1,,\"2\",,,,1,1,4)  \n" +
            "表达式:concat(),名称:连接字符串,示例:formula:concat('a','b')  \n" +
            "表达式:isempty(),名称:判断参数是否为空,示例:formula:isempty(name)  \n" +
            "表达式:isnotempty(),名称:判断参数是否非空,示例:formula:isnotempty(name)  \n" +
            "表达式:upcase(),名称:字符串大写,示例:formula:upcase(name)  \n" +
            "表达式:lowcase(),名称:字符串小写,示例:formula:lowcase(name)  \n" +
            "表达式:substr(),名称:求字符串子串,示例:formula:substr(name,0,2)  \n" +
            "表达式:lpad(),名称:左侧补充字符串,示例:formula:lpad(name,7,' ')  \n" +
            "表达式:rpad(),名称:右侧补充字符串,示例:formula:rpad(name,7,' ')  \n" +
            "表达式:find(),名称:求子串位置,示例:formula:find(name,'a')  \n" +
            "表达式:frequence(),名称:求子串个数,示例:formula:frequence(name,'a')  \n" +
            "表达式:split(),名称:字符串大写,示例:formula:split(name,',')  \n" +
            "表达式:toNumber(),名称:转换为数字,示例:formula:toNumber(name)  \n" +
            "表达式:max(),名称:求最大值,示例:formula:max(1,2,3,5,4)  \n" +
            "表达式:min(),名称:求最小值,示例:formula:min(1,2,3,5,4)  \n" +
            "表达式:ave(),名称:求均值,示例:formula:ave(1,2,3)  \n" +
            "表达式:sum(),名称:求和,示例:formula:sum(1,2,3,4,5)  \n" +
            "表达式:stddev(),名称:求标准偏差,示例:formula:stddev(1,2,3,5,4)  \n" +
            "表达式:round(),名称:四舍五入,示例:formula:round(5.4)  \n" +
            "表达式:floor(),名称:取整,示例:formula:floor(5.6)  \n" +
            "表达式:ceil(),名称:取整+1,示例:formula:ceil(5.4)  \n" +
            "表达式:int(),名称:求整数部分,示例:formula:int(name)  \n" +
            "表达式:frac(),名称:求小数部分,示例:formula:frac(name)  \n" +
            "表达式:log(),名称:求以10为底的对数,示例:formula:log(100)  \n" +
            "表达式:ln(),名称:求自然对数,示例:formula:ln(5.6)  \n" +
            "表达式:sin(),名称:求正弦,示例:formula:sin(100)  \n" +
            "表达式:cos(),名称:求余弦,示例:formula:cos(100)  \n" +
            "表达式:tan(),名称:求正切,示例:formula:tan(100)  \n" +
            "表达式:ctan(),名称:求余切,示例:formula:ctan(100)  \n" +
            "表达式:exp(),名称:求以e为底的指数,示例:formula:exp(100)  \n" +
            "表达式:sqrt(),名称:求平方根,示例:formula:sqrt(100)  \n" +
            "表达式:today(),名称:当前日期包括时间,示例:formula:today()  \n" +
            "表达式:currentDate(),名称:当前日期,示例:formula:currentDate()  \n" +
            "表达式:day(),名称:获取日,示例:formula:day()  \n" +
            "表达式:month(),名称:获取月,示例:formula:month()  \n" +
            "表达式:year(),名称:获取年,示例:formula:year()  \n" +
            "表达式:week(),名称:第几周,示例:formula:week()  \n" +
            "表达式:weekday(),名称:星期几,示例:formula:weekday(name)  \n" +
            "表达式:formatdate(),名称:格式化日期,示例:formula:formatdate(name)  \n" +
            "表达式:dayspan(),名称:求两日期之间的天数,示例:formula:dayspan(name,today())  \n" +
            "表达式:adddate(),名称:加天数,示例:formula:adddate(name,1)  \n" +
            "表达式:addmonths(),名称:加月数,示例:formula:addmonths(name,1)  \n" +
            "表达式:addyears(),名称:加年数,示例:formula:addyears(name,1)  \n" +
            "表达式:truncdate(),名称:截断日期  第二个参数  Y ，M , D 分别返回一年、月的第一天 ，或者一日的零点,示例:formula:truncdate(name,'y')  \n" +
            "表达式:lastofmonth(),名称:求这个月最后一天,示例:formula:lastofmonth(name)  \n" +
            "表达式:toDate(),名称:转换为日期,示例:formula:toDate(name)  \n")
    @ApiImplicitParams({@ApiImplicitParam(
        name = "formula", value = "表达式"
    ), @ApiImplicitParam(
        name = "jsonString", value = "需要测试的对象，json格式"
    )})
    @WrapUpResponseBody
    public Object testFormula(String formula, String jsonString) {
        Map object = (Map) JSON.parse(StringEscapeUtils.unescapeHtml4(jsonString));
        VariableFormula variableFormula = DataSetOptUtil.createFormula();
        variableFormula.setTrans(new ObjectTranslate(object));
        variableFormula.setFormula(StringEscapeUtils.unescapeHtml4(formula));
        return variableFormula.calcFormula();
    }
}
