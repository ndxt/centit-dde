package com.centit.dde;

import com.centit.dde.bizopt.OFDConvertBizOperation;
import com.centit.dde.bizopt.OFDPreviewBizOperation;
import com.centit.dde.core.BizOptFlow;
import com.centit.fileserver.common.FileStore;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
public class OFDRegisterOperation {
    @Resource
    BizOptFlow bizOptFlow;

    @Resource
    private FileStore fileStore;

    @PostConstruct
    void registerOperation(){
        //注册服务
        bizOptFlow.registerOperation("convertofd",new OFDConvertBizOperation(fileStore));
        bizOptFlow.registerOperation("ofdreading",new OFDPreviewBizOperation());
    }

    public BizOptFlow getBizOptFlow() {
        return bizOptFlow;
    }

    public void setBizOptFlow(BizOptFlow bizOptFlow) {
        this.bizOptFlow = bizOptFlow;
    }
}
