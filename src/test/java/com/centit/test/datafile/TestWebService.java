package com.centit.test.datafile;

import com.centit.dde.ws.DownloadData;
import com.centit.dde.ws.UploadData;
import com.centit.dde.ws.WebServiceTransferClient;
import com.centit.support.security.DESSecurityUtils;

public class TestWebService {

    private static void testWS(){
        UploadData data_if = WebServiceTransferClient.getWSDLInterface("http://sxpc:8090/dde/ws/uploadData?wsdl", UploadData.class);
        if(data_if==null){
            System.out.println("Error");
            return ;
        }
        String res = data_if.uploadTableAsXml("ws", "7aO6+ICDeXs=", "xmldata");
        System.out.println(res);
    }

    static void testExportWs() {
        DownloadData intance = WebServiceTransferClient.getWSDLInterface("http://sxpc:8090/dde/ws/downloadData?wsdl", DownloadData.class);

        String ws = intance.downloadTableAsXml("ws", "7aO6+ICDeXs=", 882);

        System.out.println(ws);
    }

    /**
     * 对传输密码进行加密
     * @param pwd
     */
    static void encrypwd(String pwd) {
        System.out.println(DESSecurityUtils.encryptAndBase64(pwd,"0123456789abcdefghijklmnopqrstuvwxyzABCDEF"));

        //System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());

//        validator();
    }

    static void validator() {
        System.out.println(Thread.currentThread().getStackTrace()[2].getMethodName());
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
//        testWS();
//        testExportWs();

        encrypwd("dde2");



//
    }





}
