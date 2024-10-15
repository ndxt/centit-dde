package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.framework.common.ResponseData;
import com.centit.support.file.FileSystemOpt;

import java.io.File;

public class SqliteImportOperation implements BizOperation {

    private String dbHome;

    public SqliteImportOperation(String appHome){
        if(appHome.endsWith("/") || appHome.endsWith("\\")) {
            this.dbHome = appHome + "sqlite";
        } else {
            this.dbHome = appHome + File.separatorChar + "sqlite";
        }
        FileSystemOpt.createDirect(this.dbHome);
    }

    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        return null;
    }
}
