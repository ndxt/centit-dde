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
import com.centit.support.algorithm.ByteBaseOpt;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.common.ObjectException;
import com.centit.support.security.SM2Util;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;

/**
 * 文件签名与验证
 */
public class SignatureVerifyOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        //SM2_WITH_SM3
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        /**
         * 加密数据 encryptData，整个数据集 dataSet，
         *  某一个具体的字段 field ： 密文字段 fieldName ，解密后字段 encryptFieldName
         *  file 加密文件， 加密文件是不需要base64编码的
         */
        String optType = BuiltInOperation.getJsonFieldString(bizOptJson, "optType", "sign");
        String fieldName = BuiltInOperation.getJsonFieldString(bizOptJson, "signatureData", ConstantValue.FILE_CONTENT);
        //SM2_WITH_SM3
        // String signatureType = BuiltInOperation.getJsonFieldString(bizOptJson, "signatureType", "SM2_WITH_SM3");
        String password = BuiltInOperation.getJsonFieldString(bizOptJson, "password", "");
        DataSet dataSet = bizModel.getDataSet(sourDsName);
        BizModelJSONTransform transform = new BizModelJSONTransform(bizModel, dataSet==null? null : dataSet.getData());
        Object mw = DataSetOptUtil.fetchFieldValue(transform, fieldName);
        if (dataSet == null || mw == null) {
            return BuiltInOperation.createResponseData(0, 1,
                ObjectException.DATA_NOT_FOUND_EXCEPTION,
                dataOptContext.getI18nMessage("dde.604.data_source_not_found"));
        }
        if(StringUtils.isBlank(password)){
            return BuiltInOperation.createResponseData(0, 1,
                ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                dataOptContext.getI18nMessage("error.701.field_is_blank", "password"));
        }

        password = StringBaseOpt.castObjectToString(
            DataSetOptUtil.fetchFieldValue(transform, password), password);
        String encodeType = BuiltInOperation.getJsonFieldString(bizOptJson, "encodeType", "HEX").toUpperCase();

        if("verify".equalsIgnoreCase(optType)){
            String signatureValueFormula = BuiltInOperation.getJsonFieldString(bizOptJson, "signatureValue", "");
            Object signatureValue =  StringUtils.isBlank(signatureValueFormula)? null : DataSetOptUtil.fetchFieldValue(transform, signatureValueFormula);
            if(signatureValue == null){
                return BuiltInOperation.createResponseData(0, 1,
                    ResponseData.ERROR_FIELD_INPUT_NOT_VALID,
                    dataOptContext.getI18nMessage("error.701.field_is_blank", "signatureValue"));
            }
            byte[] signatureBytes;
            switch (encodeType){
                case "BASE64":
                    signatureBytes = Base64.decodeBase64(StringBaseOpt.castObjectToString(signatureValue));
                    break;
                case "RAW":
                    signatureBytes = ByteBaseOpt.castObjectToBytes(signatureValue);
                    break;
                case "HEX":
                default:
                    signatureBytes = Hex.decode(StringBaseOpt.castObjectToString(signatureValue));
                    break;
            }

            boolean succeed = SM2Util.verify(ByteBaseOpt.castObjectToBytes(mw), password, signatureBytes);
            DataSet objectToDataSet = new DataSet(CollectionsOpt.createHashMap("verifyResult", succeed));
            bizModel.putDataSet(targetDsName, objectToDataSet);
            return BuiltInOperation.createResponseSuccessData(1);
        } else {
            byte[] signatureBytes = SM2Util.sign(ByteBaseOpt.castObjectToBytes(mw), password);
            Object signatureValue;
            switch (encodeType){
                case "BASE64":
                    signatureValue = Base64.encodeBase64String(signatureBytes);
                    break;
                case "RAW":
                    signatureValue = signatureBytes;
                    break;
                case "HEX":
                default:
                    signatureValue = Hex.toHexString(signatureBytes);
                    break;
            }
            DataSet objectToDataSet = new DataSet(CollectionsOpt.createHashMap("signatureValue", signatureValue));
            bizModel.putDataSet(targetDsName, objectToDataSet);
            return BuiltInOperation.createResponseSuccessData(1);
        }
    }
}
