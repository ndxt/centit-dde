package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.dataset.FileDataSet;
import com.centit.dde.utils.BizModelJSONTransform;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.utils.DataSetOptUtil;
import com.centit.dde.utils.FileDataSetOptUtil;
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
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author codefan@sina.com
 * 加密算法
 */
/**
 * 加密算法
 */
public class EncryptOperation  implements BizOperation {


    private Pair<Integer, Object> encodeBytes(byte[] bytes, String encodeType) {
        switch (encodeType) {
            case "base64": {
                String base64String = Base64.encodeBase64String(bytes); //new String(Base64.encodeBase64(cipherText))
                return new MutablePair<>(base64String.length(), base64String);
            }
            case "hex": {
                String HexString = String.valueOf(Hex.encodeHex(bytes));
                return new MutablePair<>(HexString.length(), HexString);
            }
            case "raw":
            default:
                return new MutablePair<>(bytes.length, bytes);
        }
    }

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
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet == null) {
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        }

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
                BooleanBaseOpt.castObjectToBoolean(baseStr, !"file".equals(encryptDataType));
            if (base64Compatible) {
                encodeType = "base64";
            }
        }

        if("dataSet".equals(encryptDataType)) {
            byte[] cipherText = null;
            switch (algorithm){
                case "SM4":
                    cipherText = SM4Util.encryptEcbPadding(password.getBytes(StandardCharsets.UTF_8),
                        dataSet.toJSONString().getBytes(StandardCharsets.UTF_8));
                    break;
                case "AES_CBC": {
                    Pair<byte[], byte[]> keyAndIv = SecurityOptUtils.makeCbcKey(password, "AES");
                    cipherText = AESSecurityUtils.encryptAsCBCType(dataSet.toJSONString().getBytes(StandardCharsets.UTF_8),
                        keyAndIv.getKey(), keyAndIv.getValue());
                    }
                    break;
                case "SM4_CBC": {
                    Pair<byte[], byte[]> keyAndIv = SecurityOptUtils.makeCbcKey(password, "SM4");
                    cipherText = SM4Util.encryptAsCBCType(dataSet.toJSONString().getBytes(StandardCharsets.UTF_8),
                        keyAndIv.getKey(), keyAndIv.getValue());
                    }
                    break;
                case "SM2":
                    cipherText = SM2Util.encryptUsePublicKey(dataSet.toJSONString().getBytes(StandardCharsets.UTF_8),
                        password);
                    break;
                case "AES":
                default:
                    cipherText = AESSecurityUtils.encrypt(dataSet.toJSONString().getBytes(StandardCharsets.UTF_8),
                        password);
                    break;
            }

            DataSet objectToDataSet = new DataSet(encodeBytes(cipherText, encodeType).getRight());
            bizModel.putDataSet(targetDsName, objectToDataSet);
            return BuiltInOperation.createResponseSuccessData(objectToDataSet.getSize());
        } else {
            String fieldName = BuiltInOperation.getJsonFieldString(bizOptJson, "fieldName", "");
            String encryptFieldName = BuiltInOperation.getJsonFieldString(bizOptJson, "encryptFieldName", "");

            if ("file".equals(encryptDataType)) {
                //ConstantValue.FILE_CONTENT
                if (StringUtils.isBlank(fieldName)) {
                    fieldName = ConstantValue.FILE_CONTENT;
                }
                //获取文件数据集内容; 多个文件 自动压缩为 zip文件
                dataSet = FileDataSetOptUtil.attainFileDataset(bizModel, dataSet, bizOptJson, false);
                encryptFieldName = fieldName; //文件加密 不需要填写这个字段
            }

            if (StringUtils.isBlank(fieldName) || StringUtils.isBlank(encryptFieldName)) {
                return BuiltInOperation.createResponseData(0, 1,
                    ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "encryptFieldName"));
            }
            List<Map<String, Object>> encryptData = dataSet.getDataAsList();
            if (encryptData.isEmpty()) {
                return BuiltInOperation.createResponseSuccessData(0);
            }

            for (Map<String, Object> map : encryptData) {
                Object mw = map.get(fieldName);

                if (mw != null) {
                    byte[] cipherText = null;

                    switch (algorithm) {
                        case "SM4":
                            cipherText = SM4Util.encryptEcbPadding(
                                password.getBytes(StandardCharsets.UTF_8),
                                ByteBaseOpt.castObjectToBytes(mw));
                            break;
                        case "AES_CBC": {
                            Pair<byte[], byte[]> keyAndIv = SecurityOptUtils.makeCbcKey(password, "AES");
                            cipherText = AESSecurityUtils.encryptAsCBCType(ByteBaseOpt.castObjectToBytes(mw),
                                keyAndIv.getKey(), keyAndIv.getValue());
                        }
                        break;
                        case "SM4_CBC": {
                            Pair<byte[], byte[]> keyAndIv = SecurityOptUtils.makeCbcKey(password, "SM4");
                            cipherText = SM4Util.encryptAsCBCType(ByteBaseOpt.castObjectToBytes(mw),
                                keyAndIv.getKey(), keyAndIv.getValue());
                        }
                        break;
                        case "SM2":
                            cipherText = SM2Util.encryptUsePublicKey(ByteBaseOpt.castObjectToBytes(mw),
                                password);
                            break;
                        case "AES":
                        default:
                            cipherText = AESSecurityUtils.encrypt(ByteBaseOpt.castObjectToBytes(mw),
                                password);
                            break;
                    }
                    Pair<Integer, Object> data = encodeBytes(cipherText, encodeType);
                    map.put(encryptFieldName, data.getRight());
                    if ("file".equals(encryptDataType)) {
                        map.put(ConstantValue.FILE_SIZE, data.getLeft());
                    }
                }
            }
            if ("file".equals(encryptDataType)) {
                FileDataSet fileDataSet = FileDataSetOptUtil.castToFileDataSet(new DataSet(encryptData.get(0)));
                bizModel.putDataSet(targetDsName, fileDataSet);
            } else {
                DataSet objectToDataSet = dataSet.getSize() == 1 ? new DataSet(encryptData.get(0)) : new DataSet(encryptData);
                bizModel.putDataSet(targetDsName, objectToDataSet);
            }
            return BuiltInOperation.createResponseSuccessData(dataSet.getSize());
        }
    }
}
