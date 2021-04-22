package com.centit.dde.transaction;

import com.centit.product.metadata.vo.ISourceInfo;
import com.centit.support.network.HttpExecutor;
import com.centit.support.network.HttpExecutorContext;
import com.centit.support.network.UrlOptUtils;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author zhf
 */
abstract class AbstractHttpConnectPools {
    private static final Logger logger = LoggerFactory.getLogger(AbstractHttpConnectPools.class);
    private static final
    Map<ISourceInfo, HttpExecutorContext> HTTP_DATA_SOURCE_POOLS
        = new ConcurrentHashMap<>();

    private AbstractHttpConnectPools() {
        throw new IllegalAccessError("Utility class");
    }

    private static HttpExecutorContext mapHttpSource(ISourceInfo dsDesc) throws IOException {
        HttpClientContext context = HttpClientContext.create();
        BasicCookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        if(dsDesc.getDatabaseUrl()!=null) {
            HttpExecutor.formPost(HttpExecutorContext.create(httpClient).context(context),
                dsDesc.getDatabaseUrl(),
                dsDesc.getExtProps(), false);
        }
        context.setCookieStore(cookieStore);
        return HttpExecutorContext.create(httpClient).context(context);
    }

    static synchronized HttpExecutorContext getHttpConnect(ISourceInfo dsDesc) throws  IOException {
        HttpExecutorContext ds = HTTP_DATA_SOURCE_POOLS.get(dsDesc);
        if (ds == null) {
            ds = mapHttpSource(dsDesc);
            HTTP_DATA_SOURCE_POOLS.put(dsDesc, ds);
        }
        return ds;
    }
    static void releaseHttp(ISourceInfo dsDesc){
        HTTP_DATA_SOURCE_POOLS.remove(dsDesc);
    }
}
