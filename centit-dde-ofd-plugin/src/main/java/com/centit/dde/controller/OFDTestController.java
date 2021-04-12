package com.centit.dde.controller;

import com.centit.dde.vo.OFDConvertVo;
import com.centit.fileserver.common.FileStore;
import com.suwell.ofd.custom.agent.HTTPAgent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "文件上传测试")
@RestController
@RequestMapping("/test/")
public class OFDTestController {

    @Resource
    private FileStore fileStore;

    @ApiOperation("测试")
    @GetMapping("upload")
    public Object uploadtest(OFDConvertVo ofdConvertVo) throws Exception {
        HTTPAgent ha = new HTTPAgent("http://192.168.137.50:8080/convert-issuer/");
        FileOutputStream fileOutputStream=null;
        try {
            List<File> inList = new ArrayList<File>();
            inList.add(new File("D:\\tomcat\\apache-tomcat-8.5.59\\temp\\南大先腾外出记录单.doc"));
            inList.add(new File("D:\\tomcat\\apache-tomcat-8.5.59\\temp\\新员工网络指南.docx"));
            inList.add(new File("D:\\tomcat\\apache-tomcat-8.5.59\\temp\\数科文档网页轻阅读系统V3.0技术白皮书.pdf"));
            fileOutputStream = new FileOutputStream(new File("D:\\ofdfiletest\\a.ofd"));
            ha.officesToOFD(inList, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ha.close();//注意：一定要记得关闭 ha
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
