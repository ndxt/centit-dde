package com.centit.dde.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.dde.vo.FormulaParameter;
import com.centit.dde.vo.UpdateOptIdParameter;
import com.centit.framework.common.ResponseData;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.support.compiler.ObjectTranslate;
import com.centit.support.compiler.VariableFormula;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.text.StringEscapeUtils;
import org.quartz.CronExpression;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * @author zhf
 */
@Api(value = "Http触发任务响应", tags = "Http触发任务响应")
@RestController
@RequestMapping(value = "run")
public class HttpTaskController extends DoApiController {

    /**
     * 草稿-----------请求方法
     */
    @GetMapping(value = "/draft/{packetId}")
    @ApiOperation(value = "草稿：立即执行任务GET")
    public void runGetDraftTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_DEBUG, ConstantValue.TASK_TYPE_GET,
            null, request, response);
    }

    @PostMapping(value = "/draft/{packetId}")
    @ApiOperation(value = "草稿：立即执行任务POST")
    public void runPostDraftTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                         HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_DEBUG, ConstantValue.TASK_TYPE_POST,
            null, request, response);
    }

    @PutMapping(value = "/draft/{packetId}")
    @ApiOperation(value = "草稿：立即执行任务PUT")
    public void runPutDraftTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_DEBUG, ConstantValue.TASK_TYPE_PUT,
            null, request, response);
    }

    @DeleteMapping(value = "/draft/{packetId}")
    @ApiOperation(value = "草稿：立即执行任务DELETE")
    public void runDelDraftTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_DEBUG, ConstantValue.TASK_TYPE_DELETE,
            null, request, response);
    }

    /**
     * 发布-------请求方法
     */

    @GetMapping(value = "/{packetId}")
    @ApiOperation(value = "发布：立即执行任务GET")
    public void runGetTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_GET,
            null, request, response);
    }

    @PostMapping(value = "/{packetId}")
    @ApiOperation(value = "发布：立即执行任务POST")
    public void runPostTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_POST,
            null, request, response);
    }

    @PutMapping(value = "/{packetId}")
    @ApiOperation(value = "发布：立即执行任务PUT")
    public void runPutTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_PUT,
            null, request, response);
    }

    @DeleteMapping(value = "/{packetId}")
    @ApiOperation(value = "发布：立即执行任务DELETE")
    public void runDelTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_DELETE,
            null, request, response);
    }

    @PostMapping(value = "/testformula")
    @ApiOperation(value = "测试表达式", notes =
        "可用表达式  \n" +
            "表达式:toObject(string),名称:转json,示例:formula:toObject(name);json:{name:\\{sex:'man'}}  \n" +
            "表达式:uuid(),名称:获取uuid,示例:formula:uuid()  \n" +
            "表达式:random(len),名称:获取随机数,示例:formula:random(6)  \n" +
            "表达式:password(rawPassword),名称:对字符串加密,示例:formula:password(password)  \n" +
            "表达式:toByteArray(inputStream),名称:把输入流转为byte数组,示例:formula:toByteArray(name)  \n" +
            "表达式:dict(catalog,key),名称:获取代码对应的数据字典,示例:formula:dict('userCode',name)  \n" +
            "表达式:dictTrans(catalog,expression),名称:获取代码表达式对应的数据字典,示例:formula:dictTrans('userCode',names),names为逗号分开多个name组成  \n" +
            "表达式:replace(rawString,searchString,replaceString),名称:替换字符串,示例:formula:replace('abc','b','a')  \n" +
            "表达式:size(array),名称:获取数组多少元素,示例:formula:size(names)  \n" +
            "表达式:toString(),名称:转换为String,示例:formula:toString(name)  \n" +
            "表达式:attr(),名称:获取对象属性,示例:formula:attr(name,'123')  \n" +
            "表达式:getat(position,list),示例:求数组中的一个值,示例:formula:getat (0,\"a\",\"b\")  \n" +
            "表达式:singleton(listObject),名称:返回list，去重,示例:formula:singleton(name)  \n" +
            "表达式:distinct(listObject),名称:返回set，去重,示例:formula:distinct(name)  \n" +
            "表达式:getpy(),名称:取汉字拼音,示例:formula:getpy(name)  \n" +
            "表达式:byte(num,bit)/byte(text,index),名称:求位值,示例:formula:byte(4321.789,0)  \n" +
            "表达式:capital(string,[isSimple:true]),名称:字符串转大写,示例:formula:capital(123.45)  \n" +
            "表达式:if(判断条件,真值,假值),名称:三目判断,示例:formula:if(name=='b','c','d');json:{name:'a'}  \n" +
            "表达式:case(string/true/digit,匹配值1,返回值1,[匹配值2,返回值2]),名称:swithcase判断,示例:formula:case(name,'b','c','d','e')  \n" +
            "表达式:match(regex,string),名称:匹配判断,*?为通配符,示例:formula:match('t??t',name)  \n" +
            "表达式:startsWith(regex,string),名称:判断字符串中是以某个字符开头,示例:formula:startsWith('a',name)  \n" +
            "表达式:regexmatch(regex,string),名称:匹配判断,示例:formula:regexmatch('t??t',name)  \n" +
            "表达式:regexmatchvalue(regex,string),名称:获取匹配值list,示例:formula:regexmatchvalue('t??t',name)  \n" +
            "表达式:count(listObject),名称:计数,示例:formula:count(1,\"2\",3,\"5\",1,1,4)  \n" +
            "表达式:countnotnull(listObject),名称:计数 非空参数,示例:formula:countnotnull(1,,\"2\",,,,1,1,4)  \n" +
            "表达式:countnull(listObject),名称:计数空参数,示例:formula:countnull(1,,\"2\",,,,1,1,4)  \n" +
            "表达式:strcat(str1,[str2..]),名称:连接字符串,示例:formula:strcat('a','b')  \n" +
            "表达式:isempty(string),名称:判断参数是否为空,示例:formula:isempty(name)  \n" +
            "表达式:isnotempty(string),名称:判断参数是否非空,示例:formula:isnotempty(name)  \n" +
            "表达式:upcase(),名称:字符串大写,示例:formula:upcase(name)  \n" +
            "表达式:lowcase(),名称:字符串小写,示例:formula:lowcase(name)  \n" +
            "表达式:substr(),名称:求字符串子串,示例:formula:substr(name,0,2)  \n" +
            "表达式:lpad(),名称:左侧补充字符串,示例:formula:lpad(name,7,' ')  \n" +
            "表达式:rpad(),名称:右侧补充字符串,示例:formula:rpad(name,7,' ')  \n" +
            "表达式:find(),名称:求子串位置,示例:formula:find(name,'a')  \n" +
            "表达式:frequence(),名称:求子串个数,示例:formula:frequence(name,'a')  \n" +
            "表达式:split(),名称:字符串大写,示例:formula:split(name,',')  \n" +
            "表达式:toNumber(),名称:转换为数字,示例:formula:toNumber(name)  \n" +
            "表达式:max(listObject),名称:求最大值,示例:formula:max(1,2,3,5,4)  \n" +
            "表达式:min(listObject),名称:求最小值,示例:formula:min(1,2,3,5,4)  \n" +
            "表达式:ave(listDigit),名称:求均值,示例:formula:ave(1,2,3)  \n" +
            "表达式:sum(listObject),名称:求和,示例:formula:sum(1,2,3,4,5)  \n" +
            "表达式:stddev(listDigit),名称:求标准偏差,示例:formula:stddev(1,2,3,5,4)  \n" +
            "表达式:round(digit),名称:四舍五入,示例:formula:round(5.4)  \n" +
            "表达式:floor(digit),名称:取整,示例:formula:floor(5.6)  \n" +
            "表达式:ceil(digit),名称:取整+1,示例:formula:ceil(5.4)  \n" +
            "表达式:int(digit),名称:求整数部分,示例:formula:int(name)  \n" +
            "表达式:frac(digit),名称:求小数部分,示例:formula:frac(name)  \n" +
            "表达式:log(digit),名称:求以10为底的对数,示例:formula:log(100)  \n" +
            "表达式:ln(digit),名称:求自然对数,示例:formula:ln(5.6)  \n" +
            "表达式:sin(digit),名称:求正弦,示例:formula:sin(100)  \n" +
            "表达式:cos(digit),名称:求余弦,示例:formula:cos(100)  \n" +
            "表达式:tan(digit),名称:求正切,示例:formula:tan(100)  \n" +
            "表达式:ctan(digit),名称:求余切,示例:formula:ctan(100)  \n" +
            "表达式:exp(digit),名称:求以e为底的指数,示例:formula:exp(100)  \n" +
            "表达式:sqrt(digit),名称:求平方根,示例:formula:sqrt(100)  \n" +
            "表达式:today(),名称:当前日期包括时间,示例:formula:today()  \n" +
            "表达式:currentDate(),名称:当前日期,示例:formula:currentDate()  \n" +
            "表达式:day([date]),名称:获取日,示例:formula:day()  \n" +
            "表达式:month([date]),名称:获取月,示例:formula:month()  \n" +
            "表达式:year([date]),名称:获取年,示例:formula:year()  \n" +
            "表达式:week([date]),名称:第几周,示例:formula:week()  \n" +
            "表达式:weekday([date]),名称:星期几,示例:formula:weekday(name)  \n" +
            "表达式:formatdate(),名称:格式化日期,示例:formula:formatdate(name)  \n" +
            "表达式:dayspan(),名称:求两日期之间的天数,示例:formula:dayspan(name,today())  \n" +
            "表达式:adddate(),名称:加天数,示例:formula:adddate(name,1)  \n" +
            "表达式:addmonths(),名称:加月数,示例:formula:addmonths(name,1)  \n" +
            "表达式:addyears(),名称:加年数,示例:formula:addyears(name,1)  \n" +
            "表达式:truncdate(),名称:截断日期  第二个参数  Y ，M , D 分别返回一年、月的第一天 ，或者一日的零点,示例:formula:truncdate(name,'y')  \n" +
            "表达式:lastofmonth(),名称:求这个月最后一天,示例:formula:lastofmonth(name)  \n" +
            "表达式:random(),名称:生成随机数random(5),random(1,5),random(string,20),random(string,uuid22/uuid32/uuid36)示例:formula:random()  \n" +
            "表达式:hash(),名称:签名计算,hash(object),hash(object,md5/sha,base64),hash(object,hmac-sha1,secret-key),hash(object,hmac-sha1,secret-key,base64)示例:formula:hash(name)  \n")
    @WrapUpResponseBody
    public Object testFormula(@RequestBody FormulaParameter formulaParams) {
        if (formulaParams.getGetVariable()) {
            return VariableFormula.attainFormulaVariable(StringEscapeUtils.unescapeHtml4(formulaParams.getFormula()), DataSetOptUtil.extendFuncs);
        } else {
            Map object = (Map) JSON.parse(StringEscapeUtils.unescapeHtml4(formulaParams.getJsonString()));
            VariableFormula variableFormula = new VariableFormula();
            variableFormula.setExtendFuncMap(DataSetOptUtil.extendFuncs);
            variableFormula.setTrans(new ObjectTranslate(object));
            variableFormula.setFormula(StringEscapeUtils.unescapeHtml4(formulaParams.getFormula()));
            return variableFormula.calcFormula();
        }
    }

    @ApiOperation(value = "DDE扩展函数接口")
    @GetMapping(value = "/extensionFunction")
    public JSONArray extensionFunction() {
        String extensionFunction = "[" +
            "{\"password\": \"对字符串加密,示例:formula:password(password)\"}," +
            "{\"dict\": \"获取代码对应的数据字典,示例:formula:dict('userCode',name)\"}," +
            "{\"dictTrans\": \"获取代码表达式对应的数据字典,示例:formula:dictTrans('userCode',names),names为逗号分开多个name组成\"}" +
            "]";
        return JSON.parseArray(extensionFunction);
    }

    @ApiOperation(value = "修改数据所属业务模块(删除菜单迁移数据(接口页面))")
    @PutMapping(value = "/updateOptId")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData updateOptIdByOptCodes(@RequestBody UpdateOptIdParameter updateOptIdParamVo) {
        String[] apiIds = updateOptIdParamVo.getApiIds();
        int[] dataPacketDraftCount = apiIds != null && apiIds.length > 0 ?
            dataPacketDraftService.batchUpdateOptIdByApiId(updateOptIdParamVo.getOptId(), Arrays.asList(apiIds)) : null;
        int[] dataPacketCount = apiIds != null && apiIds.length > 0 ?
            dataPacketService.batchUpdateOptIdByApiId(updateOptIdParamVo.getOptId(), Arrays.asList(apiIds)) : null;
        JSONObject result = new JSONObject();
        result.put("dataPacketDraftCount", dataPacketDraftCount);
        result.put("dataPacketCount", dataPacketCount);
        return ResponseData.makeSuccessResponse(result.toJSONString());
    }

    @ApiOperation(value = "查询单个API网关权限")
    @GetMapping(value = "/roles/{apiId}")
    @WrapUpResponseBody
    public List<ConfigAttribute> getRolesWithApiId(@PathVariable String apiId) {
        return platformEnvironment.getRolesWithApiId(apiId);
    }

    @ApiOperation(value = "查询近5次cron运行时间")
    @GetMapping(value = "/testCron")
    @WrapUpResponseBody
    public List<String> testCron(String cron) {
        List<String> result = new ArrayList<>();
        try {
            CronExpression cc = new CronExpression(cron);
            Date next = new Date();
            for (int i = 0; i < 5; i++) {
                next = cc.getNextValidTimeAfter(next);
                result.add(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(next));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
