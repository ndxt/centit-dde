package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.framework.common.ResponseData;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.ByteBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.security.AESSecurityUtils;
import com.centit.support.security.SM4Util;
import com.centit.support.security.SecurityOptUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class DecipherOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String cipherDataType = BuiltInOperation.getJsonFieldString(bizOptJson, "decipherData", "dataSet");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null){
            return BuiltInOperation.createResponseData(0, 1,
                ResponseData.ERROR_OPERATION, "解密算法异常，请指定数据集！");
        }
        //AES / SM4
        String algorithm = BuiltInOperation.getJsonFieldString(bizOptJson, "algorithm", "AES");
        String password = BuiltInOperation.getJsonFieldString(bizOptJson, "password", "");

        if(StringUtils.isNotBlank(password)){
            BizModelJSONTransform transform = new BizModelJSONTransform(bizModel, dataSet.getData());
            password = StringBaseOpt.castObjectToString(
                DataSetOptUtil.fetchFieldValue(transform, password), password);
        }
        Boolean base64 =
            BooleanBaseOpt.castObjectToBoolean(
                BuiltInOperation.getJsonFieldString(bizOptJson, "base64", ""),  ! "file".equals(cipherDataType));

        String fieldName = BuiltInOperation.getJsonFieldString(bizOptJson, "fieldName", "");

        /**
         * 解密数据 decipherData ，整个数据集 dataSet，
         * 对文件加密 file
         * 某一个具体的字段 field ： fieldName ， cipherFieldName
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
            byte[] objectText = null;

            switch (algorithm){
                case "SM4":
                    objectText = SM4Util.decryptEcbPadding(password.getBytes(StandardCharsets.UTF_8),
                        cipherBytes);
                    break;
                case "AES_CBC": {
                    Pair<String, String> keyAndIv = SecurityOptUtils.makeCbcKey(password, "AES");
                    objectText = AESSecurityUtils.decryptAsCBCType(cipherBytes,
                        keyAndIv.getKey(), keyAndIv.getValue());
                }
                break;
                case "SM4_CBC": {
                    Pair<String, String> keyAndIv = SecurityOptUtils.makeCbcKey(password, "SM4");
                    objectText = SM4Util.decryptAsCBCType(cipherBytes,
                        keyAndIv.getKey(), keyAndIv.getValue());
                }
                break;
                case "AES":
                default:
                    objectText = AESSecurityUtils.decrypt(cipherBytes, password);
                    break;
            }
            //整个数据集都是以json形式存储的
            String jsonString = new String(objectText);
            DataSet objectToDataSet = new DataSet(JSON.parse(jsonString));
            bizModel.putDataSet(targetDsName, objectToDataSet);
            return BuiltInOperation.createResponseSuccessData(objectToDataSet.getSize());
        } else {
            String cipherField = BuiltInOperation.getJsonFieldString(bizOptJson, "cipherFieldName", "");
            if("file".equals(cipherDataType)) {
                //ConstantValue.FILE_CONTENT
                if(StringUtils.isBlank(fieldName)){
                    fieldName = ConstantValue.FILE_CONTENT;
                }
                cipherField = fieldName; //文件加密 不需要填写这个字段
            }
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

                    byte[] objectText = null;
                    switch (algorithm){
                        case "SM4":
                            objectText = SM4Util.decryptEcbPadding(password.getBytes(StandardCharsets.UTF_8),
                                cipherBytes);
                            break;
                        case "AES_CBC": {
                            Pair<String, String> keyAndIv = SecurityOptUtils.makeCbcKey(password, "AES");
                            objectText = AESSecurityUtils.decryptAsCBCType(cipherBytes,
                                keyAndIv.getKey(), keyAndIv.getValue());
                        }
                        break;
                        case "SM4_CBC": {
                            Pair<String, String> keyAndIv = SecurityOptUtils.makeCbcKey(password, "SM4");
                            objectText = SM4Util.decryptAsCBCType(cipherBytes,
                                keyAndIv.getKey(), keyAndIv.getValue());
                        }
                        break;
                        case "AES":
                        default:
                            objectText = AESSecurityUtils.decrypt(cipherBytes, password);
                            break;
                    }
                    if("file".equals(cipherDataType)) {
                        map.put(cipherField, objectText);// objectText);
                    } else {
                        map.put(cipherField, JSON.parse(StringBaseOpt.castObjectToString(objectText)));// objectText);
                    }
                }
            }
            DataSet objectToDataSet = dataSet.getSize() == 1? new DataSet(encryptData.get(0)) : new DataSet(encryptData);
            bizModel.putDataSet(targetDsName, objectToDataSet);
            return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
        }
    }
}
