package com.centit.dde.bizopt;

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

/**
 * @author codefan@sina.com
 * 加密算法
 */
public class EncryptOperation  implements BizOperation {


    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        /**
         * 加密数据 encryptData，整个数据集 dataSet，
         *  某一个具体的字段 field ： 密文字段 fieldName ，解密后字段 encryptFieldName
         *  file 加密文件， 加密文件是不需要base64编码的
         */
        String encryptDataType = BuiltInOperation.getJsonFieldString(bizOptJson, "encryptData", "dataSet");
        //AES(ECB) / AES_CBC (CBC)  / SM4(EBC) / SM4_CBC(CBC)
        String algorithm = BuiltInOperation.getJsonFieldString(bizOptJson, "algorithm", "AES");
        String password = BuiltInOperation.getJsonFieldString(bizOptJson, "password", "");

        if(StringUtils.isNotBlank(password)){
            BizModelJSONTransform transform = new BizModelJSONTransform(bizModel);
            password = StringBaseOpt.castObjectToString(
                DataSetOptUtil.fetchFieldValue(transform, password), password);
        }

        Boolean base64 =
            BooleanBaseOpt.castObjectToBoolean(
                BuiltInOperation.getJsonFieldString(bizOptJson, "base64", ""), ! "file".equals(encryptDataType));

        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null) {
            return BuiltInOperation.createResponseData(0, 1,
                ResponseData.ERROR_OPERATION, "加密计算异常，请指定数据集！");
        }
        if("dataSet".equals(encryptDataType)) {
            byte[] cipherText = null;
            switch (algorithm){
                case "SM4":
                    cipherText = SM4Util.encryptEcbPadding(password.getBytes(StandardCharsets.UTF_8),
                        dataSet.toJSONString().getBytes(StandardCharsets.UTF_8));
                    break;
                case "AES_CBC": {
                    Pair<String, String> keyAndIv = SecurityOptUtils.makeCbcKey(password, "AES");
                    cipherText = AESSecurityUtils.encryptAsCBCType(dataSet.toJSONString().getBytes(StandardCharsets.UTF_8),
                        keyAndIv.getKey(), keyAndIv.getValue());
                    }
                    break;
                case "SM4_CBC": {
                    Pair<String, String> keyAndIv = SecurityOptUtils.makeCbcKey(password, "SM4");
                    cipherText = SM4Util.encryptAsCBCType(dataSet.toJSONString().getBytes(StandardCharsets.UTF_8),
                        keyAndIv.getKey(), keyAndIv.getValue());
                    }
                    break;
                case "AES":
                default:
                    cipherText = AESSecurityUtils.encrypt(dataSet.toJSONString().getBytes(StandardCharsets.UTF_8),
                        password);
                    break;
            }

            DataSet objectToDataSet = new DataSet(base64 ? new String(Base64.encodeBase64(cipherText)) : cipherText);
            bizModel.putDataSet(targetDsName, objectToDataSet);
            return BuiltInOperation.createResponseSuccessData(objectToDataSet.getSize());
        } else {
            String fieldName = BuiltInOperation.getJsonFieldString(bizOptJson, "fieldName", "");
            String encryptFieldName = BuiltInOperation.getJsonFieldString(bizOptJson, "encryptFieldName", "");

            if("file".equals(encryptDataType)) {
                //ConstantValue.FILE_CONTENT
                if(StringUtils.isBlank(fieldName)){
                    fieldName = ConstantValue.FILE_CONTENT;
                }
                encryptFieldName = fieldName; //文件加密 不需要填写这个字段
            }

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
                    byte[] cipherText = null;

                    switch (algorithm){
                        case "SM4":
                            cipherText = SM4Util.encryptEcbPadding(password.getBytes(StandardCharsets.UTF_8),
                                ByteBaseOpt.castObjectToBytes(mw) );
                            break;
                        case "AES_CBC": {
                            Pair<String, String> keyAndIv = SecurityOptUtils.makeCbcKey(password, "AES");
                            cipherText = AESSecurityUtils.encryptAsCBCType(ByteBaseOpt.castObjectToBytes(mw),
                                keyAndIv.getKey(), keyAndIv.getValue());
                        }
                        break;
                        case "SM4_CBC": {
                            Pair<String, String> keyAndIv = SecurityOptUtils.makeCbcKey(password, "SM4");
                            cipherText = SM4Util.encryptAsCBCType(ByteBaseOpt.castObjectToBytes(mw),
                                keyAndIv.getKey(), keyAndIv.getValue());
                        }
                        break;
                        case "AES":
                        default:
                            cipherText = AESSecurityUtils.encrypt(ByteBaseOpt.castObjectToBytes(mw),
                                password);
                            break;
                    }
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
