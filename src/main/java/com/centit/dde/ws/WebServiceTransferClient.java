package com.centit.dde.ws;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.common.gzip.GZIPInInterceptor;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

public class WebServiceTransferClient {

    /**
     * 为与获取服务端连接的超时时间，单位为毫秒
     */
    private static final int CXF_CLIENT_CONNECT_TIMEOUT = 15 * 1000;

    /**
     * 为获取连接后接收数据的超时时间，单位为毫秒
     */
    private static final int CXF_CLIENT_RECEIVE_TIMEOUT = 60 * 1000;

    /**
     * @param sWSDLUrl 业务系统WebService 的wsdl的url
     * @param clazz    接口类型
     * @return 指定的接口
     */
    public static <T> T getWSDLInterface(String sWSDLUrl, Class<T> clazz) {

        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.getInInterceptors().add(new LoggingInInterceptor());
        factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
        factoryBean.getInInterceptors().add(new GZIPInInterceptor());
        factoryBean.getOutInterceptors().add(new GZIPOutInterceptor());
        factoryBean.setServiceClass(clazz);
        factoryBean.setAddress(sWSDLUrl);

        @SuppressWarnings("unchecked")
        T instance = (T) factoryBean.create();

        Client proxy = ClientProxy.getClient(instance);
        HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
        HTTPClientPolicy policy = new HTTPClientPolicy();
        policy.setConnectionTimeout(CXF_CLIENT_CONNECT_TIMEOUT);
        policy.setReceiveTimeout(CXF_CLIENT_RECEIVE_TIMEOUT);
        conduit.setClient(policy);

        return instance;

    }
}
