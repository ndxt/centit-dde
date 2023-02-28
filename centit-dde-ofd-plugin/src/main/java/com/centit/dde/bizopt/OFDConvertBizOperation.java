package com.centit.dde.bizopt;

import com.alibaba.fastjson2.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.DataOptContext;
import com.centit.dde.core.DataSet;
import com.centit.dde.utils.ConstantValue;
import com.centit.dde.vo.OFDConvertVo;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import com.suwell.ofd.custom.agent.HTTPAgent;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 原始文件转换成OFD文件
 */
public class OFDConvertBizOperation implements BizOperation {

    private FileStore fileStore;

    public OFDConvertBizOperation(FileStore fileStore) {
        this.fileStore = fileStore;
    }
    public OFDConvertBizOperation() {
    }


    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson, DataOptContext dataOptContext) throws Exception {
        Map<String, Object> modelTag = (Map<String, Object>) bizModel.getStackData(ConstantValue.REQUEST_PARAMS_TAG);
        OFDConvertVo ofdConvertVo = bizOptJson.toJavaObject(OFDConvertVo.class);
        String fileIdMaps = (String) modelTag.get("fileId");
        String fileids = StringUtils.isNotBlank(fileIdMaps) ? fileIdMaps : ofdConvertVo.getFileid();
        if (StringUtils.isBlank(fileids)) {
            return ResponseData.makeErrorMessage("文件id不能为空！");
        }
        String[] fileidArr = fileids.split(",");
        List<File> fileList = new ArrayList<>();
        for (String fileId : fileidArr) {
            //获取上传文件（文件服务器获取）
            File file = fileStore.getFile(fileId);
            if(file.length()==0) {
                continue;
            }
            int pos = fileId.lastIndexOf('.');
            String fileType ="dat";
            if (pos >= 0) {
                fileType = fileId.substring(pos + 1);
            } /*else {
                FileInfo fileInfo = fileStore.getFileInfo(fileId);
                fileType = fileInfo.getFileType();
            }*/
            String fileName = System.currentTimeMillis() + "." + fileType;
            String path = file.getParent();
            file.renameTo(new File(path + File.separator + fileName));
            fileList.add(new File(path + File.separator + fileName));
        }
        HTTPAgent httpAgent = new HTTPAgent(ofdConvertVo.getHttpUrl());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        httpAgent.officesToOFD(fileList, stream);
        bizModel.putDataSet(ofdConvertVo.getId(), new DataSet(stream));
        for(File file:fileList){
            file.delete();
        }
        if (httpAgent != null) {
            httpAgent.close();
        }
        if (stream != null) {
            stream.close();
        }
        return ResponseData.successResponse;
    }
}
