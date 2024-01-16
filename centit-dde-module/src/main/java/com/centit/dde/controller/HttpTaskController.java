package com.centit.dde.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.adapter.po.DataPacket;
import com.centit.dde.adapter.po.DataPacketInterface;
import com.centit.dde.adapter.utils.ConstantValue;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataOptResult;
import com.centit.dde.services.BizModelService;
import com.centit.dde.services.DataPacketDraftService;
import com.centit.dde.services.DataPacketService;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.dde.vo.FormulaParameter;
import com.centit.dde.vo.UpdateOptIdParameter;
import com.centit.fileserver.utils.UploadDownloadUtils;
import com.centit.framework.common.JsonResultUtils;
import com.centit.framework.common.ResponseData;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.core.controller.BaseController;
import com.centit.framework.core.controller.WrapUpResponseBody;
import com.centit.framework.model.adapter.PlatformEnvironment;
import com.centit.framework.model.basedata.OsInfo;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.DatetimeOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.compiler.ObjectTranslate;
import com.centit.support.compiler.VariableFormula;
import com.centit.support.file.FileIOOpt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhf
 */
@Api(value = "Http触发任务响应", tags = "Http触发任务响应")
@RestController
@RequestMapping(value = "run")
public class HttpTaskController extends BaseController {

    @Autowired
    private PlatformEnvironment platformEnvironment;

    private final DataPacketService dataPacketService;
    private final DataPacketDraftService dataPacketDraftService;
    private final BizModelService bizmodelService;

    public Map<String, DataPacket> dataPacketCachedMap = new ConcurrentHashMap<>(10000);
    public HttpTaskController(DataPacketService dataPacketService, DataPacketDraftService dataPacketDraftService,
                              BizModelService bizmodelService) {
        this.dataPacketService = dataPacketService;
        this.dataPacketDraftService = dataPacketDraftService;
        this.bizmodelService = bizmodelService;
    }

    /**
     * 草稿-----------请求方法
     */

    @GetMapping(value = "/draft/{packetId}")
    @ApiOperation(value = "草稿：立即执行任务GET")
    public void runGetDraftTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_DEBUG, ConstantValue.TASK_TYPE_GET, request, response);
    }

    @PostMapping(value = "/draft/{packetId}")
    @ApiOperation(value = "草稿：立即执行任务POST")
    public void runPostDraftTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                         HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_DEBUG, ConstantValue.TASK_TYPE_POST, request, response);
    }

    @PutMapping(value = "/draft/{packetId}")
    @ApiOperation(value = "草稿：立即执行任务PUT")
    public void runPutDraftTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_DEBUG, ConstantValue.TASK_TYPE_PUT, request, response);
    }

    @DeleteMapping(value = "/draft/{packetId}")
    @ApiOperation(value = "草稿：立即执行任务DELETE")
    public void runDelDraftTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_DEBUG, ConstantValue.TASK_TYPE_DELETE, request, response);
    }


    /**
     * 发布-------请求方法
     */

    @GetMapping(value = "/{packetId}")
    @ApiOperation(value = "发布：立即执行任务GET")
    public void runGetTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_GET, request, response);
    }

    @PostMapping(value = "/{packetId}")
    @ApiOperation(value = "发布：立即执行任务POST")
    public void runPostTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_POST, request, response);
    }

    @PutMapping(value = "/{packetId}")
    @ApiOperation(value = "发布：立即执行任务PUT")
    public void runPutTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_PUT, request, response);
    }

    @DeleteMapping(value = "/{packetId}")
    @ApiOperation(value = "发布：立即执行任务DELETE")
    public void runDelTaskExchange(@PathVariable String packetId, HttpServletRequest request,
                                   HttpServletResponse response) throws IOException {
        returnObject(packetId, ConstantValue.RUN_TYPE_NORMAL, ConstantValue.TASK_TYPE_DELETE, request, response);
    }

    private void judgePower(@PathVariable String packetId, String loginUser, String runType) {
        if (ConstantValue.RUN_TYPE_DEBUG.equals(runType)) {
            if (StringUtils.isBlank(loginUser)) {
                throw new ObjectException(ResponseData.ERROR_USER_NOT_LOGIN, "您未登录！");
            }
            DataPacketInterface dataPacket = dataPacketDraftService.getDataPacket(packetId);
            if (!platformEnvironment.loginUserIsExistWorkGroup(dataPacket.getOsId(), loginUser)) {
                throw new ObjectException(ResponseData.HTTP_NON_AUTHORITATIVE_INFORMATION, "您没有权限！");
            }
        }
    }

    private void returnObject(String packetId, String runType, String taskType,
                              HttpServletRequest request, HttpServletResponse response) throws IOException {
        //judgePower(packetId,runType);
        DataPacketInterface dataPacketInterface;
        if(ConstantValue.RUN_TYPE_DEBUG.equals(runType)){
            dataPacketInterface = dataPacketDraftService.getDataPacket(packetId);
        }else {
            DataPacket dataPacket = dataPacketService.getDataPacket(packetId);
            DataPacket cachedPacket = dataPacketCachedMap.get(packetId);
            if(cachedPacket == null || DatetimeOpt.compareTwoDate(dataPacket.getPublishDate(), cachedPacket.getPublishDate()) != 0){
                dataPacketInterface = dataPacket;
                dataPacketCachedMap.put(packetId, dataPacket);
            } else {
                dataPacketInterface = cachedPacket;
            }
        }

        if (dataPacketInterface == null) {
            throw new ObjectException(ResponseData.ERROR_INTERNAL_SERVER_ERROR, "API接口：" + packetId + "不存在！");
        }
        if (ConstantValue.TASK_TYPE_MSG.equals(dataPacketInterface.getTaskType())) {
            throw new ObjectException(ResponseData.HTTP_METHOD_NOT_ALLOWED, "消息触发不支持请求，该类型任务会自动触发！");
        }
        if (!taskType.equals(dataPacketInterface.getTaskType()) &&
            !ConstantValue.TASK_TYPE_TIME.equals(dataPacketInterface.getTaskType())) {
            throw new ObjectException(ResponseData.ERROR_INTERNAL_SERVER_ERROR, "任务类型和请求方式不匹配，请保持一致！");
        }
        if (dataPacketInterface.getIsDisable() != null && dataPacketInterface.getIsDisable()) {
            throw new ObjectException(ResponseData.HTTP_METHOD_NOT_ALLOWED, "API接口已被禁用，请先恢复！");
        }
        //根据api默认值初始化
        Map<String, Object> params = new HashMap<>(dataPacketInterface.getPacketParamsValue());
        params.putAll(collectRequestParameters(request));
        //保存内部逻辑变量，有些时候需要将某些值传递到其它标签节点，这时候需要用到它
        DataOptContext dataOptContext = new DataOptContext();
        dataOptContext.setRunType(runType);

        dataOptContext.setDebugId((String) params.getOrDefault("debugId", ""));

        if (taskType == ConstantValue.TASK_TYPE_POST || taskType == ConstantValue.TASK_TYPE_PUT ||
            "POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
            String contentType = request.getHeader("Content-Type");
            if (StringUtils.contains(contentType, "application/json")) {
                String bodyString = FileIOOpt.readStringFromInputStream(request.getInputStream(), String.valueOf(StandardCharsets.UTF_8));
                dataOptContext.setStackData(ConstantValue.REQUEST_BODY_TAG, JSON.parse(bodyString));
            } else if(StringUtils.contains(contentType, "application/x-www-form-urlencoded")){
                Map<String, Object> bodyMap = new HashMap<>(32);
                for(Map.Entry<String, String[]> ent : request.getParameterMap().entrySet()){
                    if(ent.getValue().length==1){
                        bodyMap.put(ent.getKey(), ent.getValue()[0]);
                    }else {
                        bodyMap.put(ent.getKey(), ent.getValue());
                    }
                }
                dataOptContext.setStackData(ConstantValue.REQUEST_BODY_TAG, bodyMap);
            }  else if(StringUtils.contains(contentType, "application/octet-stream")){
                String fileName = request.getHeader("filename");
                String fileName2 = StringBaseOpt.castObjectToString(params.get("fileName"));
                if (StringUtils.isBlank(fileName)) {
                    fileName = request.getHeader("fileName");
                    if(StringUtils.isBlank(fileName)) {
                        if (StringUtils.isNotBlank(fileName2)) {
                            fileName = fileName2;
                        }
                    }
                }
                if (StringUtils.isNotBlank(fileName) && StringUtils.isBlank(fileName2)) {
                    params.put("fileName", fileName);
                }

                InputStream inputStream = UploadDownloadUtils.fetchInputStreamFromMultipartResolver(request).getRight();
                if (inputStream != null) {
                    dataOptContext.setStackData(ConstantValue.REQUEST_FILE_TAG,
                        CollectionsOpt.createHashMap(
                            "fileName", fileName,
                            "fileSize", inputStream.available(),
                            "fileContent", inputStream));
                }
            } else if(StringUtils.contains(contentType, "text/plain")){
                String bodyString = FileIOOpt.readStringFromInputStream(request.getInputStream(), String.valueOf(StandardCharsets.UTF_8));
                dataOptContext.setStackData(ConstantValue.REQUEST_BODY_TAG, bodyString);
            } else if(StringUtils.contains(contentType, "multipart/form-data")){
                MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
                MultipartHttpServletRequest multiRequest = resolver.resolveMultipart(request);
                Map<String, MultipartFile> map = multiRequest.getFileMap();
                Map<String, Object> bodyMap = new HashMap<>(32);
                for (Map.Entry<String, MultipartFile> entry : map.entrySet()) {
                    CommonsMultipartFile cMultipartFile = (CommonsMultipartFile) entry.getValue();
                    FileItem fi = cMultipartFile.getFileItem();
                    String fieldName = fi.getFieldName();
                    String itemType = fi.getHeaders().getHeader("Content-Type");
                    if (!fi.isFormField() || itemType.contains("application/octet-stream")) {
                        String filename = fi.getHeaders().getHeader("filename");
                        if (StringUtils.isBlank(filename)) {
                            filename = StringBaseOpt.castObjectToString(params.get("filename"));
                            if (StringUtils.isBlank(filename)) {
                                filename = StringBaseOpt.castObjectToString(params.get("fileName"));
                            }
                        }
                        String fileName2 = StringBaseOpt.castObjectToString(params.get("fileName"));
                        if (StringUtils.isNotBlank(filename) && StringUtils.isBlank(fileName2)) {
                            params.put("fileName", filename);
                        }
                        InputStream inputStream = fi.getInputStream();
                        if (inputStream != null) {
                            Map<String, Object> fileData = CollectionsOpt.createHashMap(
                                "fileName", filename,
                                "fileSize", inputStream.available(),
                                "fileContent", inputStream);
                            dataOptContext.setStackData(ConstantValue.REQUEST_FILE_TAG, fileData);
                            bodyMap.put(fieldName, fileData);
                        }
                    } else if (itemType.contains("application/json")) {
                        String bodyString = fi.getString();
                        bodyMap.put(fieldName, JSON.parse(bodyString));
                    } else { //if (itemType.contains("text/plain")) {
                        String bodyString = fi.getString();
                        bodyMap.put(fieldName, bodyString);
                    }
                }
                dataOptContext.setStackData(ConstantValue.REQUEST_BODY_TAG, bodyMap);
            } else { //
                throw new ObjectException(ObjectException.FUNCTION_NOT_SUPPORT, "不支持的表单格式，Content-Type:" + contentType);
            }
        }

        dataOptContext.setStackData(ConstantValue.REQUEST_PARAMS_TAG, params);

        Map<String, String> cookies = WebOptUtils.fetchRequestCookies(request);
        if(cookies!=null){
            dataOptContext.setStackData(ConstantValue.REQUEST_COOKIES_TAG, cookies);
        }

        Map<String, String> headers = WebOptUtils.fetchRequestHeaders(request);
        if(cookies!=null){
            dataOptContext.setStackData(ConstantValue.REQUEST_HEADERS_TAG, headers);
        }

        //request.getCookies().
        OsInfo osInfo = platformEnvironment.getOsInfo(dataPacketInterface.getOsId());
        dataOptContext.setStackData(ConstantValue.APPLICATION_INFO_TAG, osInfo);
        CentitUserDetails userDetails = WebOptUtils.getCurrentUserDetails(request);
        if(userDetails!=null) {
            dataOptContext.setStackData(ConstantValue.SESSION_DATA_TAG, userDetails);
            dataOptContext.setTopUnit(userDetails.getTopUnitCode());
        }

        DataOptResult result = bizmodelService.runBizModel(dataPacketInterface, dataOptContext);
        if (result.getResultType() == DataOptResult.RETURN_FILE_STREAM) {
            String fileName = result.getReturnFilename();
            InputStream in = result.getReturnFileStream();
            if (in != null && fileName != null) {
                UploadDownloadUtils.downFileRange(request, response, in,
                    in.available(), fileName, request.getParameter("downloadType"), null);
            } else {
                JsonResultUtils.writeOriginalObject(
                    ResponseData.makeErrorMessage("没不错误，没有返回的文件实体！"), response);
            }
            return;
        }
        if (result.getResultType() == DataOptResult.RETURN_DATA_AS_RAW) {
            JsonResultUtils.writeOriginalObject(result.getResultObject(), response);
        } else {
            JsonResultUtils.writeOriginalObject(result.toResponseData(), response);
        }
    }

    @PostMapping(value = "/testformula")
    @ApiOperation(value = "测试表达式", notes =
        "可用表达式  \n" +
            "表达式:toObject(string),名称:转json,示例:formula:toObject(name);json:{name:\\{sex:'man'}}  \n" +
            "表达式:uuid(),名称:获取uuid,示例:formula:uuid()  \n" +
            "表达式:random(len),名称:获取随机数,示例:formula:random(6)  \n" +
            "表达式:encode(rawPassword),名称:对字符串加密,示例:formula:encode(name)  \n" +
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
            "{\"encode\": \"对字符串加密,示例:formula:encode(name)\"}," +
            "{\"dict\": \"获取代码对应的数据字典,示例:formula:dict('userCode',name)\"}," +
            "{\"dictTrans\": \"获取代码表达式对应的数据字典,示例:formula:dictTrans('userCode',names),names为逗号分开多个name组成\"}" +
            "]";
        return JSON.parseArray(extensionFunction);
    }

    @ApiOperation(value = "修改数据所属业务模块(删除菜单迁移数据(接口页面))")
    @PutMapping(value = "/updateOptId")
    @Transactional(rollbackFor = Exception.class)
    public ResponseData updateOptIdByOptCodes(@RequestBody UpdateOptIdParameter updateOptIdParamVo) {
        int[] optdefCount = platformEnvironment.updateOptIdByOptCodes(updateOptIdParamVo.getOptId(), Arrays.asList(updateOptIdParamVo.getOptCodes()));
        String[] apiIds = updateOptIdParamVo.getApiIds();
        int[] dataPacketDraftCount = apiIds != null && apiIds.length > 0 ? dataPacketDraftService.batchUpdateOptIdByApiId(updateOptIdParamVo.getOptId(), Arrays.asList(apiIds)) : null;
        int[] dataPacketCount = apiIds != null && apiIds.length > 0 ? dataPacketService.batchUpdateOptIdByApiId(updateOptIdParamVo.getOptId(), Arrays.asList(apiIds)) : null;
        JSONObject result = new JSONObject();
        result.put("optdefCount", optdefCount);
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

}
