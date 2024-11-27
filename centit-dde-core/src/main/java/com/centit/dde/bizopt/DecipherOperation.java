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
import com.centit.support.common.ObjectException;
import com.centit.support.security.AESSecurityUtils;
import com.centit.support.security.SM2Util;
import com.centit.support.security.SM4Util;
import com.centit.support.security.SecurityOptUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 解密算法
 */
public class DecipherOperation implements BizOperation {

    private byte[] decodeBytes(Object cipherData, String encodeType) {
        switch (encodeType) {
            case "base64": {
                String cipherText = StringBaseOpt.castObjectToString(cipherData);
                return Base64.decodeBase64(cipherText);
            }
            case "hex": {
                try {
                    String cipherText = StringBaseOpt.castObjectToString(cipherData);
                    return Hex.decodeHex(cipherText);
                } catch (Exception e) {
                    throw new ObjectException(ObjectException.ILLEGALACCESS_EXCEPTION,
                        "HEX解码失败" +e.getMessage(), e);
                }
            }
            case "raw":
            default:
                return ByteBaseOpt.castObjectToBytes(cipherData);
        }
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        String cipherDataType = BuiltInOperation.getJsonFieldString(bizOptJson, "decipherData", "dataSet");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null){
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        }
        //AES / SM4
        String algorithm = BuiltInOperation.getJsonFieldString(bizOptJson, "algorithm", "AES");
        String password = BuiltInOperation.getJsonFieldString(bizOptJson, "password", "");

        if(StringUtils.isNotBlank(password)){
            BizModelJSONTransform transform = new BizModelJSONTransform(bizModel, dataSet.getData());
            password = StringBaseOpt.castObjectToString(
                DataSetOptUtil.fetchFieldValue(transform, password), password);
        }
        // raw hex base64
        String encodeType = BuiltInOperation.getJsonFieldString(bizOptJson, "encodeType", "raw");
        String baseStr = BuiltInOperation.getJsonFieldString(bizOptJson, "base64", "");
        if(StringUtils.isNotBlank(baseStr)) {
            Boolean base64Compatible =
                BooleanBaseOpt.castObjectToBoolean(baseStr, !"file".equals(cipherDataType));
            if (base64Compatible) {
                encodeType = "base64";
            }
        }
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
                    ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "cipherData"));
            }
            byte[] cipherBytes = decodeBytes(cipherData, encodeType);
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
                case "SM2":
                    objectText = SM2Util.decryptUserPrivateKey(cipherBytes, password);
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
                    ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "cipherData"));
            }

            List<Map<String, Object>> encryptData = dataSet.getDataAsList();
            if(encryptData.size()<1){
                return BuiltInOperation.createResponseSuccessData(0);
            }

            for(Map<String, Object> map : encryptData){
                Object mw = map.get(fieldName);
                if(mw!=null){
                    byte[] cipherBytes = decodeBytes(mw, encodeType);
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
                        case "SM2":
                            objectText = SM2Util.decryptUserPrivateKey(cipherBytes, password);
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
