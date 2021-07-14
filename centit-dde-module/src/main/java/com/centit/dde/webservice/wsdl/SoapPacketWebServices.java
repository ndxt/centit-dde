package com.centit.dde.webservice.wsdl;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.io.IOException;

@WebService(name = "SoapPacketWebServices",//暴露服务名称
    targetNamespace = "http://wsdl.webservice.dde.centit.com/")//命名空间，一般是接口包名的倒序
public interface SoapPacketWebServices {

    @WebMethod
    void runTestTaskExchange(@WebParam(name = "packetId") String packetId) throws IOException;

    @WebMethod
    void debugRunTestTaskExchange(@WebParam(name = "packetId") String packetId)throws IOException;

    @WebMethod
    String runTaskExchange(@WebParam(name = "packetId") String packetId)throws IOException;

    @WebMethod
    void debugRunTaskExchange(@WebParam(name = "packetId") String packetId) throws IOException;

    @WebMethod
    void runPostTest(@WebParam(name = "packetId") String packetId)throws IOException;

    @WebMethod
    void runTaskPost(@WebParam(name = "packetId") String packetId)throws IOException;

}
