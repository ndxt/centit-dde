package com.centit.dde.bizopt;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSONObject;
import com.centit.dde.core.BizModel;
import com.centit.dde.core.BizOperation;
import com.centit.dde.core.SimpleDataSet;
import com.centit.dde.vo.OFDConvertVo;
import com.centit.fileserver.client.FileClientImpl;
import com.centit.fileserver.client.po.FileInfo;
import com.centit.fileserver.common.FileStore;
import com.centit.framework.common.ResponseData;
import com.suwell.ofd.custom.agent.HTTPAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 原始文件转换成OFD文件
 */
public class OFDConvertBizOperation implements BizOperation {

    public static final Log log = LogFactory.getLog(OFDConvertBizOperation.class);

    private FileClientImpl fileClient;
    private FileStore fileStore;

    public OFDConvertBizOperation(FileClientImpl fileClient, FileStore fileStore) {
        this.fileClient = fileClient;
        this.fileStore = fileStore;
    }

    public OFDConvertBizOperation() {
    }


    @Override
    public ResponseData runOpt(BizModel bizModel, JSONObject bizOptJson) throws Exception {
        Map<String, Object> modelTag = bizModel.getModelTag();
        OFDConvertVo ofdConvertVo = bizOptJson.toJavaObject(OFDConvertVo.class);
        String fileIdMaps = (String)modelTag.get("fileId");
        String fileids =StringUtils.isNotBlank(fileIdMaps)?fileIdMaps:ofdConvertVo.getFileid();
        if (StringUtils.isBlank(fileids)) {
            return ResponseData.makeErrorMessage("文件id不能为空！");
        }
        String[] fileidArr = fileids.split(",");
        List<File> fileList = new ArrayList<>();
        for (String fileId : fileidArr) {
            //获取上传文件（文件服务器获取）
            FileInfo fileInfo = fileClient.getFileInfo(fileId);
            String fileName = System.currentTimeMillis()+"."+fileInfo.getFileType();
            File file = fileStore.getFile(fileInfo.getFileId());
            String  path = file.getParent();
            boolean b = file.renameTo(new File(path + File.separator +fileName));
            fileList.add(new File(path + File.separator + fileName));
        }
        TimeInterval timer = DateUtil.timer();
        HTTPAgent httpAgent = null;
        ByteArrayOutputStream stream = null;
        try {
            httpAgent = new HTTPAgent(ofdConvertVo.getHttpUrl());
            System.out.println("1");
            stream = new ByteArrayOutputStream();
            System.out.println("2");
            httpAgent.officesToOFD(fileList, stream);
            System.out.println("----"+fileList.get(0));
            System.out.println("3");
            bizModel.putDataSet(ofdConvertVo.getId(),new SimpleDataSet(stream));
            System.out.println("4");
            log.info("请求转换文件服务,请求服务地址："+ofdConvertVo.getHttpUrl()+"，请求耗时："+timer.interval()+"ms");
        } catch (Exception e) {
            BuiltInOperation.getResponseData(0,500,"请求转换文件服务异常，异常信息:"+e.getMessage());
            log.error("请求转换文件服务异常，异常信息："+e.getMessage());
        } finally {
            System.out.println("5");
            try {
                if (httpAgent != null) {
                    httpAgent.close();
                }
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e) {
                System.out.println("6");
                e.getMessage();
            }
            System.out.println("7");
        }
        return ResponseData.successResponse;
    }
}
