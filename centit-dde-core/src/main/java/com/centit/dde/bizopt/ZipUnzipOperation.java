package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.framework.common.ResponseData;
import com.centit.support.file.FileSystemOpt;

import java.io.File;

public class ZipUnzipOperation implements BizOperation {

    private String tempHome;

    public ZipUnzipOperation(String appHome) {
        if (appHome.endsWith("/") || appHome.endsWith("\\")) {
            this.tempHome = appHome + "temp";
        } else {
            this.tempHome = appHome + File.separatorChar + "temp";
        }
        FileSystemOpt.createDirect(this.tempHome);
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        String sourDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "source", bizModel.getModelName());
        String targetDsName = BuiltInOperation.getJsonFieldString(bizOptJson, "id", sourDsName);
        // zip / unzip
        String optType = BuiltInOperation.getJsonFieldString(bizOptJson, "optType", "zip");
        //暂时不支持 加密算法
        //String password = BuiltInOperation.getJsonFieldString(bizOptJson, "password", "");
        if("zip".equals(optType)){

        }

        return null;
    }
}
