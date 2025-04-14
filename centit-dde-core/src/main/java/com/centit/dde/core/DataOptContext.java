package com.centit.dde.core;

import com.centit.dde.adapter.po.CallApiLog;
import com.centit.dde.utils.ConstantValue;
import com.centit.framework.common.WebOptUtils;
import com.centit.framework.model.security.CentitUserDetails;
import com.centit.framework.security.SecurityContextUtils;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.network.HardWareUtils;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Data
public class DataOptContext {

    private String runType;
    private String needRollback;
    private CallApiLog callApiLog;
    private String optId;
    private String debugId;
    private String osId;
    private String packetId;
    private Integer logLevel;
    private Integer breakStepNo;
    private String topUnit;
    /**
     * 调用参数
     * RequestBody/File
     * Session
     */
    @Setter(lombok.AccessLevel.NONE)
    private Map<String, Object> callStackData;

    @Setter(lombok.AccessLevel.NONE)
    private MessageSource messageSource;

    @Setter(lombok.AccessLevel.NONE)
    private Locale locale;

    public DataOptContext(MessageSource messageSource, Locale locale) {
        this.callStackData = new HashMap<>(8);
        this.messageSource = messageSource;
        this.locale = locale;
        breakStepNo = -1;
        debugId = null;
    }

    public String getI18nMessage(String code, Object ... args) {
        return messageSource.getMessage(code, args, "Message:" + code, locale);
    }

    public String getLogId() {
        if (callApiLog ==null){
            return null;
        }
        return this.callApiLog.getLogId();
    }

    public int getStepNo() {
        if (callApiLog ==null){
            callApiLog = new CallApiLog();
        }
        return this.callApiLog.getStepNo();
    }
    public int plusStepNo() {
        if (callApiLog ==null){
            callApiLog = new CallApiLog();
        }
        return this.callApiLog.plusStepNo();
    }

    //String runType = StringBaseOpt.castObjectToString(bizModel.getStackData(ConstantValue.RUN_TYPE_TAG));
    public String getRunType() {
        return ConstantValue.RUN_TYPE_DEBUG.equals(runType) ? runType : ConstantValue.RUN_TYPE_NORMAL;
    }

    public void setStackData(String key, Object value) {
        if (key == null || !key.startsWith(ConstantValue.DOUBLE_UNDERLINE)) {
            throw new ObjectException("内部堆栈数据必须以'__'开头");
        }
        callStackData.put(key, value);
    }

    public Object getStackData(String key) {
        if (callStackData==null) {
            return null;
        }
        return callStackData.get(key);
    }

    public CentitUserDetails getCurrentUserDetail(){
        if(callStackData.get(ConstantValue.SESSION_DATA_TAG) instanceof CentitUserDetails) {
            return (CentitUserDetails) callStackData.get(ConstantValue.SESSION_DATA_TAG);
        }else{
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public String getSessionId(){
        Map<String, String> headers = (Map<String, String>) getStackData(ConstantValue.REQUEST_HEADERS_TAG);
        if(headers != null) {
            String sid = headers.get(WebOptUtils.SESSION_ID_TOKEN);
            if(StringUtils.isNotBlank(sid))
                return sid;
        }
        Map<String, Object> params = (Map<String, Object>) getStackData(ConstantValue.REQUEST_PARAMS_TAG);
        return StringBaseOpt.castObjectToString(params.get(SecurityContextUtils.SecurityContextTokenName));
    }

    public String getLoginIp(){
        CentitUserDetails ud = getCurrentUserDetail();
        if(ud!=null){
            return ud.getLoginIp();
        }
        InetAddress loacalHost = HardWareUtils.getLocalhost();
        if(loacalHost!=null)
            return loacalHost.getHostAddress();
        return "127.0.0.1";
    }

    public String getCurrentUserCode(){
        CentitUserDetails currentUserDetails = getCurrentUserDetail();
        if(currentUserDetails!=null){
            return currentUserDetails.getUserCode();
        }
        return null;
    }

    public String getCurrentUnitCode(){
        CentitUserDetails currentUserDetails = getCurrentUserDetail();
        if(currentUserDetails!=null){
            return currentUserDetails.getCurrentUnitCode();
        }
        return null;
    }

    public String getRequestParams (){
        return StringBaseOpt.castObjectToString(
            this.getStackData(ConstantValue.REQUEST_PARAMS_TAG));
    }

    /**
     * 用于记录日志
     * @return callStackData 的部分内容
     */
    public Map<String, Object> getContextData(){
        //MODULE_CALL_TAG
        Map<String, Object> objectMap = new HashMap<>();
        Object modulaTag = callStackData.get(ConstantValue.MODULE_CALL_TAG);
        if(modulaTag != null){
            objectMap.put(ConstantValue.MODULE_CALL_TAG, modulaTag);
            return objectMap;
        }
        for(String key : callStackData.keySet()){
            if(key.startsWith("__request_")){
                objectMap.put(key, callStackData.get(key));
            }
        }
        CentitUserDetails userDetails = this.getCurrentUserDetail();
        if(userDetails != null && userDetails.getUserInfo() != null) {
            objectMap.put(ConstantValue.SESSION_DATA_TAG, userDetails.getUserInfo().toJsonWithoutSensitive());
            objectMap.put("currentUnit", userDetails.getCurrentUnitCode());
        }
        return objectMap;
    }
}
