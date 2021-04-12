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
        System.out.println("文件map："+modelTag.toString());
        OFDConvertVo ofdConvertVo = bizOptJson.toJavaObject(OFDConvertVo.class);
        String fileIdMaps = (String)modelTag.get("fileId");
        String fileids =StringUtils.isNotBlank(fileIdMaps)?fileIdMaps:ofdConvertVo.getFileid();
        System.out.println("fileids："+fileids);
        if (StringUtils.isBlank(fileids)) {
            return ResponseData.makeErrorMessage("文件id不能为空！");
        }
        String[] fileidArr = fileids.split(",");
        List<File> fileList = new ArrayList<>();
        for (String fileId : fileidArr) {
            //获取上传文件（文件服务器获取）
            FileInfo fileInfo = fileClient.getFileInfo(fileId);
            System.out.println("文件信息："+fileInfo.getFileName());
            System.out.println("文件信息："+fileInfo.toString());
            String fileName = fileInfo.getFileName();
            System.out.println("文件名："+fileName);
            File file = fileStore.getFile(fileInfo.getFileId());
            System.out.println("文件："+file.toString());
            String  path = file.getParent();
            System.out.println("路径："+path);
            boolean b = file.renameTo(new File(path + File.separator + fileName));
            System.out.println("重命名结果："+b);
            fileList.add(new File(path + File.separator + fileName));
        }
        TimeInterval timer = DateUtil.timer();
        HTTPAgent httpAgent = null;
        ByteArrayOutputStream stream = null;
        try {
            httpAgent = new HTTPAgent(ofdConvertVo.getHttpUrl());
            stream = new ByteArrayOutputStream();
            httpAgent.officesToOFD(fileList, stream);
            bizModel.putDataSet(ofdConvertVo.getId(),new SimpleDataSet(stream));
            System.out.println("执行结束：");
            log.info("请求转换文件服务,请求服务地址："+ofdConvertVo.getHttpUrl()+"，请求耗时："+timer.interval()+"ms");
        } catch (Exception e) {
            BuiltInOperation.getResponseData(0,500,"请求转换文件服务异常，异常信息:"+e.getMessage());
            log.error("请求转换文件服务异常，异常信息："+e.getMessage());
        } finally {
            System.out.println("关闭开始");
            try {
                if (httpAgent != null) {
                    httpAgent.close();
                }
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception e) {
                System.out.println("关闭异常");
                e.getMessage();
            }
            System.out.println("关闭结束");
        }
        return ResponseData.successResponse;
    }
}
