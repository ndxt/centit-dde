package com.centit.dde.bizopt;

import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.framework.common.ResponseData;
import org.springframework.stereotype.Component;

/**
 * 文件下载节点
 */
@Component
public class FileDownloadBizOperation implements BizOperation {
    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) {
        return null;
    }
}
