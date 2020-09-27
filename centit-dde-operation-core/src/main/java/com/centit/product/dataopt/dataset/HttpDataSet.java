package com.centit.product.dataopt.dataset;

import com.centit.framework.appclient.AppSession;
import com.centit.framework.appclient.HttpReceiveJSON;
import com.centit.framework.appclient.RestfulHttpRequest;
import com.centit.product.dataopt.core.DataSetReader;
import com.centit.product.dataopt.core.SimpleDataSet;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author zhf
 */
@Data
public class HttpDataSet implements DataSetReader {
    private String sUrl;

    public HttpDataSet(String sUrl) {
        this.sUrl = sUrl;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SimpleDataSet load(Map<String, Object> params) {
        HttpReceiveJSON receiveJson = RestfulHttpRequest.getResponseData(new AppSession(),
            sUrl, params);
        SimpleDataSet dataSet = new SimpleDataSet();
        dataSet.setData((List)receiveJson.getJSONArray("objList"));
        return dataSet;
    }
}
