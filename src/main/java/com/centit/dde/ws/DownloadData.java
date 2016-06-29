package com.centit.dde.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(targetNamespace = "http://dde.centit.com/ws/")
public interface DownloadData {

    /**
     * 导出数据内容，和数据导出的内容一样
     * @param userName
     * @param userPin
     * @param exportId 导出数据的ID，这个需要通过权限控制
     * @return
     */
    @WebMethod
    String downloadTableAsXml(
            @WebParam(name = "userName") String userName,
            @WebParam(name = "userPin") String userPin,
            @WebParam(name = "exportId") long exportId);
    
}
