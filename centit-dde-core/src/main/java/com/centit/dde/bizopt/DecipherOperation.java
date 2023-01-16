package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.framework.common.ResponseData;
import com.centit.framework.security.utils.SM4Util;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.ByteBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.security.AESSecurityUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class DecipherOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {


        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        //AES / SM4
        String algorithm = BuiltInOperation.getJsonFieldString(bizOptJson, "algorithm", "AES");
        String password = BuiltInOperation.getJsonFieldString(bizOptJson, "password", "change it");
        BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);
        password = StringBaseOpt.castObjectToString(transform.attainExpressionValue(password), password);
        Boolean base64 =
            BooleanBaseOpt.castObjectToBoolean(
                BuiltInOperation.getJsonFieldString(bizOptJson, "base64", "true"), true);

        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null){
            return BuiltInOperation.createResponseData(0, 1,
                ResponseData.ERROR_OPERATION, "解密算法异常，请指定数据集！");
        }

        String cipherDataType = BuiltInOperation.getJsonFieldString(bizOptJson, "fieldName", "");
        String fieldName = BuiltInOperation.getJsonFieldString(bizOptJson, "fieldName", "");

        /**
         * 解密数据 cipherData ，整个数据集 dataSet， 某一个具体的字段 field ： fieldName ， cipherFieldName
         */
        if("dataSet".equals(cipherDataType)) {
            Object cipherData;
            if (StringUtils.isNotBlank(fieldName)) {
                cipherData = dataSet.getFirstRow().get(fieldName);
            } else {
                cipherData = dataSet.getData();
            }
            if (cipherData == null) {
                return BuiltInOperation.createResponseData(0, 1,
                    ResponseData.ERROR_OPERATION, "解密算法异常，指定数据内容为null！");
            }
            byte[] cipherBytes;
            if (base64) {
                String cipherText = StringBaseOpt.castObjectToString(cipherData);
                cipherBytes = Base64.decodeBase64(cipherText);
            } else {
                cipherBytes = ByteBaseOpt.castObjectToBytes(cipherData);
            }
            byte[] objectText = "SM4".equalsIgnoreCase(algorithm) ?
                //这个国密算法有很多策略，现在也搞不懂，随便搞一个
                SM4Util.decryptEcbPadding(password.getBytes(StandardCharsets.UTF_8), cipherBytes) :
                AESSecurityUtils.decrypt(cipherBytes, password);
            //整个数据集都是以json形式存储的
            String jsonString = new String(objectText);
            DataSet objectToDataSet = new DataSet(JSON.parse(jsonString));
            bizModel.putDataSet(targetDsName, objectToDataSet);
            return BuiltInOperation.createResponseSuccessData(objectToDataSet.getSize());
        } else {
            String cipherField = BuiltInOperation.getJsonFieldString(bizOptJson, "cipherFieldName", "");
            if (StringUtils.isBlank(fieldName) || StringUtils.isBlank(cipherField)) {
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
                    byte[] cipherBytes;
                    if (base64) {
                        String cipherText = StringBaseOpt.castObjectToString(mw);
                        cipherBytes = Base64.decodeBase64(cipherText);
                    } else {
                        cipherBytes = ByteBaseOpt.castObjectToBytes(mw);
                    }

                    byte[] objectText = "SM4".equalsIgnoreCase(algorithm) ?
                        //这个国密算法有很多策略，现在也搞不懂，随便搞一个
                        SM4Util.decryptEcbPadding(password.getBytes(StandardCharsets.UTF_8), cipherBytes) :
                        AESSecurityUtils.decrypt(cipherBytes, password);

                    map.put(cipherField, JSON.parse(StringBaseOpt.castObjectToString(objectText)));// objectText);
                }
            }
            DataSet objectToDataSet = dataSet.getSize() == 1? new DataSet(encryptData.get(0)) : new DataSet(encryptData);
            bizModel.putDataSet(targetDsName, objectToDataSet);
            return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
        }
    }
}
