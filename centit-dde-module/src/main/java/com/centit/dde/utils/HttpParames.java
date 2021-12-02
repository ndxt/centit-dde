package com.centit.dde.utils;

public class HttpParames {
    private String packetName;
    //登录接口资源ID
    private String loginUrlCode;
    //请求接口资源code（接口前缀，如：http:127.0.0.1:8080/conctext/methodName）
    private String httpUrlCode;
    //请求接口方法名称(接口路径)
    private String methodName;
    //请求方式  取值： GET    POST
    private String methodType;

    private String requestBody;
    //参数列表  [{key:value},{key:value}]
    private Object[] paramesList;

    private String osId;

    private String optId;

    public String getPacketName() {
        return packetName;
    }

    public void setPacketName(String packetName) {
        this.packetName = packetName;
    }

    public String getLoginUrlCode() {
        return loginUrlCode;
    }

    public void setLoginUrlCode(String loginUrlCode) {
        this.loginUrlCode = loginUrlCode;
    }

    public String getHttpUrlCode() {
        return httpUrlCode;
    }

    public void setHttpUrlCode(String httpUrlCode) {
        this.httpUrlCode = httpUrlCode;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public Object[] getParamesList() {
        return paramesList;
    }

    public void setParamesList(Object[] paramesList) {
        this.paramesList = paramesList;
    }

    public String getOsId() {
        return osId;
    }

    public void setOsId(String osId) {
        this.osId = osId;
    }

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId;
    }
}
