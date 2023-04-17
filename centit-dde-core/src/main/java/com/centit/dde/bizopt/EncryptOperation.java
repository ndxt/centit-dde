package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.framework.security.utils.SM4Util;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.security.AESSecurityUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 * 加密算法
 */
public class EncryptOperation  implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        //AES / SM4
        String algorithm = BuiltInOperation.getJsonFieldString(bizOptJson, "algorithm", "AES");
        String password = BuiltInOperation.getJsonFieldString(bizOptJson, "password", "");

        if(StringUtils.isBlank(password)){
            password = AESSecurityUtils.AES_DEFAULT_KEY;
        } else {
            BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);
            password = StringBaseOpt.castObjectToString(
                DataSetOptUtil.fetchFieldValue(transform, password), password);
        }

        Boolean base64 =
            BooleanBaseOpt.castObjectToBoolean(
                BuiltInOperation.getJsonFieldString(bizOptJson, "base64", "true"), true);

        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null) {
            return BuiltInOperation.createResponseData(0, 1,
                ResponseData.ERROR_OPERATION, "加密计算异常，请指定数据集！");
        }
        /**
         * 加密数据 encryptData，整个数据集 dataSet， 某一个具体的字段 field ： 密文字段 fieldName ，解密后字段 encryptFieldName
         */
        String encryptDataType = BuiltInOperation.getJsonFieldString(bizOptJson, "encryptData", "dataSet");
        if("dataSet".equals(encryptDataType)) {
            byte[] cipherText = "SM4".equalsIgnoreCase(algorithm) ?
                //这个国密算法有很多策略，现在也搞不懂，随便搞一个
                SM4Util.encryptEcbPadding(password.getBytes(StandardCharsets.UTF_8),
                    dataSet.toJSONString().getBytes(StandardCharsets.UTF_8)) :
                AESSecurityUtils.encrypt(dataSet.toJSONString().getBytes(StandardCharsets.UTF_8),
                    password);

            DataSet objectToDataSet = new DataSet(base64 ? new String(Base64.encodeBase64(cipherText)) : cipherText);
            bizModel.putDataSet(targetDsName, objectToDataSet);
            return BuiltInOperation.createResponseSuccessData(objectToDataSet.getSize());
        } else {
            String fieldName = BuiltInOperation.getJsonFieldString(bizOptJson, "fieldName", "");
            String encryptFieldName = BuiltInOperation.getJsonFieldString(bizOptJson, "encryptFieldName", "");
            if (StringUtils.isBlank(fieldName) || StringUtils.isBlank(encryptFieldName)) {
                return BuiltInOperation.createResponseData(0, 1,
                    ResponseData.ERROR_OPERATION, "加密计算异常，请指定明文字段和者密文字段！");
            }
            List<Map<String, Object>> encryptData = dataSet.getDataAsList();
            if(encryptData.size()<1){
                return BuiltInOperation.createResponseSuccessData(0);
            }

            for(Map<String, Object> map : encryptData){
                Object mw = map.get(fieldName);
                if(mw!=null){
                    byte[] cipherText = "SM4".equalsIgnoreCase(algorithm) ?
                        //这个国密算法有很多策略，现在也搞不懂，随便搞一个
                        SM4Util.encryptEcbPadding(password.getBytes(StandardCharsets.UTF_8),
                            JSON.toJSONString(mw).getBytes(StandardCharsets.UTF_8)) ://
                            //ByteBaseOpt.castObjectToBytes(mw)) :
                        AESSecurityUtils.encrypt(JSON.toJSONString(mw).getBytes(StandardCharsets.UTF_8), password);
                    if(base64)
                        map.put(encryptFieldName, new String(Base64.encodeBase64(cipherText)));
                    else
                        map.put(encryptFieldName, cipherText);
                }
            }
            DataSet objectToDataSet = dataSet.getSize() == 1? new DataSet(encryptData.get(0)) : new DataSet(encryptData);
            bizModel.putDataSet(targetDsName, objectToDataSet);
            return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
        }
    }
}
