package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.framework.common.ResponseData;
import com.centit.framework.security.utils.SM4Util;
import com.centit.support.algorithm.BooleanBaseOpt;
import com.centit.support.security.AESSecurityUtils;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

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
        String password = BuiltInOperation.getJsonFieldString(bizOptJson, "password", "change it");
        Boolean base64 =
            BooleanBaseOpt.castObjectToBoolean(
                BuiltInOperation.getJsonFieldString(bizOptJson, "base64", "true"), true);

        DataSet dataSet = bizModel.getDataSet(sourDsName);
        if (dataSet==null){
            return BuiltInOperation.createResponseData(0, 1,
                ResponseData.ERROR_OPERATION, "加密计算异常，请指定数据集！");
        }

        byte[] cipherText = "SM4".equalsIgnoreCase(algorithm)?
            //这个国密算法有很多策略，现在也搞不懂，随便搞一个
            SM4Util.encryptEcbPadding(password.getBytes(StandardCharsets.UTF_8),
                dataSet.toJSONString().getBytes(StandardCharsets.UTF_8)):
            AESSecurityUtils.encrypt(dataSet.toJSONString().getBytes(StandardCharsets.UTF_8),
                password);

        DataSet objectToDataSet = new DataSet(base64? new String(Base64.encodeBase64(cipherText)): cipherText );
        bizModel.putDataSet(targetDsName, objectToDataSet);
        return BuiltInOperation.createResponseSuccessData(objectToDataSet.getSize());
    }
}
