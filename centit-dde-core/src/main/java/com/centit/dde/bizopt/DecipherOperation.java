package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.framework.common.ResponseData;
import com.centit.framework.security.utils.SM4Util;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.algorithm.ByteBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.security.AESSecurityUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

public class DecipherOperation implements BizOperation {

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {


        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        //AES / SM4
        String algorithm = BuiltInOperation.getJsonFieldString(bizOptJson, "algorithm", "AES");
        String password = BuiltInOperation.getJsonFieldString(bizOptJson, "password", "change it");
        Boolean base64 =
            BooleanBaseOpt.castObjectToBoolean(
                BuiltInOperation.getJsonFieldString(bizOptJson, "base64", "true"), true);

        DataSet dataSet = bizModel.fetchDataSetByName(sourDsName);
        if (dataSet == null){
            return BuiltInOperation.createResponseData(0, 1,
                ResponseData.ERROR_OPERATION, "解密算法异常，请指定数据集！");
        }
        String cipherFieldName = BuiltInOperation.getJsonFieldString(bizOptJson, "cipherField", sourDsName);

        Object cipherData;
        if(StringUtils.isNotBlank(cipherFieldName)){
            cipherData = dataSet.getFirstRow().get(cipherFieldName);
        } else {
            cipherData = dataSet.getData();
        }
        if (cipherData == null){
            return BuiltInOperation.createResponseData(0, 1,
                ResponseData.ERROR_OPERATION, "解密算法异常，指定数据内容为null！");
        }
        byte[] cipherBytes;
        if(base64){
            String cipherText = StringBaseOpt.castObjectToString(cipherData);
            cipherBytes = Base64.decodeBase64(cipherText);
        }else {
            cipherBytes = ByteBaseOpt.castObjectToBytes(cipherData);
        }
        byte[] objectText = "SM4".equalsIgnoreCase(algorithm)?
            //这个国密算法有很多策略，现在也搞不懂，随便搞一个
            SM4Util.decryptEcbPadding(password.getBytes(StandardCharsets.UTF_8), cipherBytes):
            AESSecurityUtils.encrypt(cipherBytes, password);

        String jsonString = new String(objectText);
        DataSet objectToDataSet = new DataSet(JSON.parse(jsonString));
        bizModel.putDataSet(targetDsName, objectToDataSet);
        return BuiltInOperation.createResponseSuccessData(objectToDataSet.getSize());
    }
}
